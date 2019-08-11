/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.consideredhamster.yetanotherpixeldungeon.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Durability;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Tempered;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Item implements Bundlable {

	private static final String TXT_PACK_FULL	= "Your pack is too full for the %s";

	private static final String TXT_DEGRADED		= "Your %s has degraded.";
	private static final String TXT_UNUSABLE		= "Your %s has broken!";
	private static final String TXT_GONNA_DEGRADE	= "Your %s is going to degrade soon.";
    private static final String TXT_GONNA_BREAK	        = "Your %s is going to break soon.";

    private static final String TXT_TARGET_CHARMED	= "You can't bring yourself to throw anything at someone so... charming.";

	private static final String TXT_TO_STRING		= "%s";
	private static final String TXT_TO_STRING_X		= "%s x%d";
	private static final String TXT_TO_STRING_LVL	= "%s %+d";
	private static final String TXT_TO_STRING_LVL_X	= "%s %+d x%d";

    public static final float TIME_TO_THROW		= 1.0f;
//    public static final float TIME_TO_PICK_UP	= 1.0f;
    public static final float TIME_TO_DROP		= 0.5f;

	public static final String AC_DROP		= "DROP";
	public static final String AC_THROW		= "THROW";

    public static final int ITEM_UNKNOWN = 0;
    public static final int CURSED_KNOWN = 1;
    public static final int ENCHANT_KNOWN = 2;
    public static final int UPGRADE_KNOWN = 3;

    public String shortName = null;

    protected String name = "smth";
	protected int image = 0;

	public boolean stackable = false;
	public int quantity = 1;

	public int bonus = 0;
	public int state = 0;

	public boolean unique = false;
	public boolean visible = true;

    public int known = ITEM_UNKNOWN;
    public int durability = 0;

	private static Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare( Item lhs, Item rhs ) {
			return Generator.Category.order( lhs ) - Generator.Category.order( rhs );
		}
	};

	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add( AC_DROP );
		actions.add( AC_THROW );
		return actions;
	}

	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {

			GameScene.pickUp( this );

            QuickSlot.refresh();

//            hero.spendAndNext( TIME_TO_PICK_UP );

            return true;

		} else {
			return false;
		}
	}

	public void doDrop( Hero hero ) {
		hero.spendAndNext(TIME_TO_DROP);
		Dungeon.level.drop( detachAll( hero.belongings.backpack ), hero.pos ).sprite.drop(hero.pos);
	}

	public void doThrow( Hero hero ) {
		GameScene.selectCell(thrower);
	}

	public void execute( Hero hero, String action ) {

		curUser = hero;
		curItem = this;

		if (action.equals( AC_DROP )) {

			doDrop( hero );

		} else if (action.equals( AC_THROW )) {

			doThrow(hero);

		}
	}

	public void execute( Hero hero ) {
		execute( hero, quickAction() );
	}

	protected void onThrow( int cell ) {

        Item item = detach(curUser.belongings.backpack);

        Heap heap;

        if( Level.solid[ cell ] ){

            int wall = cell;

            cell = Ballistica.trace[ Ballistica.distance - 1 ];

            heap = Dungeon.level.drop( item, cell );

            if (!heap.isEmpty()){
                heap.sprite.drop( wall );
            }

            if (Dungeon.level.map[ wall ] == Terrain.DOOR_ILLUSORY ) {
                Door.discover( wall );
            }

            if (Dungeon.level.map[ wall ] == Terrain.DOOR_CLOSED) {
                Door.enter( wall );
            }

        } else {

            heap = Dungeon.level.drop( item, cell );
            if (!heap.isEmpty()) {
                heap.sprite.drop(cell);
            }

        }

        for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
            Char n = Actor.findChar( cell + Level.NEIGHBOURS9[i] );
            if (n != null && n instanceof Mob ) {
                ((Mob)n).inspect( cell );
            }
        }



	}

    public boolean collect() {
        return collect( Dungeon.hero.belongings.backpack );
    }

//	public boolean collect( Bag container ) {
//        return collect( container, false );
//    }

	public boolean collect( Bag container ) {

		ArrayList<Item> items = container.items;

        if (items.contains( this )) {
            return true;
        }

		for (Item item:items) {
			if (item instanceof Bag && ((Bag)item).grab( this )) {
				return collect( (Bag)item );
			}
		}

		if (stackable) {

			Class<?>c = getClass();
			for (Item item:items) {
				if (item.getClass() == c) {
					item.quantity += quantity;
					item.updateQuickslot();
					return true;
				}
			}
		}

//        if (
//            Dungeon.loaded() && Dungeon.hero != null && Dungeon.hero.isAlive() &&
//            this instanceof MeleeWeapon && this.isIdentified() &&
//            this.bonus >= 0 && Dungeon.hero.belongings.weap1 == null
//        ) {
//
//            Dungeon.hero.belongings.weap1 = (Weapon)this;
//            return true;
//
//        }

//		if (items.size() < container.size) {
		if ( container.countVisibleItems() < container.size || !visible ) {

//			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
//				Badges.validateItemLevelAcquired(this);
//			}

			items.add( this );
			QuickSlot.refresh();
			Collections.sort( items, itemComparator );
			return true;

		} else {

			GLog.n(TXT_PACK_FULL, name());
			return false;

		}
	}

    public final Item detach( Bag container, int amount ) {

        if (quantity <= 0) {

            return null;

        } else if (stackable) {

            try {

                if( quantity > amount ) {

                    quantity -= amount;
                    onDetach();

                } else {

                    detachAll( container );

                }

                updateQuickslot();

                return getClass().newInstance();

            } catch (Exception e) {
                return null;
            }
        } else {

            return detachAll( container );

        }
    }

	public final Item detach( Bag container ) {

        return detach(container, 1 );

	}

	public final Item detachAll( Bag container ) {

		for (Item item : container.items) {
			if (item == this) {
				container.items.remove( this );
				item.onDetach();
				QuickSlot.refresh();
				return this;
			} else if (item instanceof Bag) {
				Bag bag = (Bag)item;
				if (bag.contains( this )) {
					return detachAll( bag );
				}
			}
		}

		return this;
	}

	protected void onDetach() {
	}

    public Item uncurse( int n ) {

        for (int i=0; i < n; i++) {
            uncurse();
        }

        return this;
    }

    public Item uncurse() {

        if(bonus < 0) {
            bonus++;
        }

        QuickSlot.refresh();
        return this;
    }

	public Item upgrade() {

		if(bonus < 3) {
            bonus++;
        }

        QuickSlot.refresh();
		return this;
	}

	final public Item upgrade( int n ) {
		for (int i=0; i < n; i++) {
			upgrade();
		}

		return this;
	}

    public Item repair() {
        return repair(3);
    }

    final public Item repair( int n ) {

        if( state >= 3) {

            fix();

        } else {
            for (int i = 0; i < n; i++) {
                if (state < 3) {
                    state++;
                }
            }
        }

        QuickSlot.refresh();

        return this;
    }

	public Item curse() {

        if(bonus > -3) {
            this.bonus--;
        }

        QuickSlot.refresh();
		return this;
	}

	final public Item curse(int n ) {
		for (int i=0; i < n; i++) {
			curse();
		}

		return this;

    }

    public void use( int amount ) {
        use( amount, true );
    }

	public void use( int amount, boolean notify ) {

		if (state > 0 && !( bonus >= 0 && (
            this instanceof Weapon && ((Weapon)this).enchantment instanceof Tempered && Weapon.Enchantment.procced(bonus) ||
            this instanceof Armour && ((Armour)this).glyph instanceof Durability && Armour.Glyph.procced(bonus) ) ) ) {

			int threshold = maxDurability() / 5;

            for (int i=0; i < amount; i++) {

                float chanceToDecrease = 0.5f - bonus * 0.1f;

                if( notify ) {

                    float modifier = Dungeon.hero.ringBuffs( RingOfDurability.Durability.class );

                    if ( modifier > 1.0f ) {
                        chanceToDecrease /= modifier;
                    } else if ( modifier < 1.0f ) {
                        chanceToDecrease += (1.0f - chanceToDecrease) * ( 1.0f + modifier );
                    }
                }

                if ( durability >= 0 && Random.Float() < chanceToDecrease ) {

                    durability --;

                    if( durability == threshold && notify ) {

                        GLog.w( TXT_GONNA_DEGRADE, name() );

                    }

                    if (durability <= 0) {
                        if( state > 0 ) {

                            this.state--;

                            fix();

                            QuickSlot.refresh();

                            if( notify ) {

                                GLog.n(TXT_DEGRADED, name());
                                Dungeon.hero.interrupt();

                                CharSprite sprite = Dungeon.hero.sprite;

                                if (this instanceof Weapon) {
                                    sprite.showStatus(CharSprite.NEUTRAL, "weapon degraded");
                                } else if (this instanceof Shield) {
                                    sprite.showStatus(CharSprite.NEUTRAL, "shield degraded");
                                } else if (this instanceof BodyArmor) {
                                    sprite.showStatus(CharSprite.NEUTRAL, "armor degraded");
                                } else if (this instanceof Wand) {
                                    sprite.showStatus(CharSprite.NEUTRAL, "wand degraded");
                                }

                                Sample.INSTANCE.play(Assets.SND_DEGRADE);

                            }

                        }
                    }

                }
            }
		}
	}

	public Item fix() {
		durability = maxDurability();
        return this;
	}

	public void randomize_state() {
		state = Random.IntRange( 0, 3 );
        durability = maxDurability();
	}

    public void randomize_state( int min, int max ) {
        state = Random.IntRange( Math.max( 0, min ), Math.min( 3, max ) );
        durability = maxDurability();
    }

    protected String stateToString( int state ) {
        switch(state) {
            case 3:
                return "perfect";
            case 2:
                return "good";
            case 1:
                return "bad";
            case 0:
                return "horrible";
            default:
                return "unknown";
        }
    }

	public int durability() {
		return durability;
	}

    public int maxDurability() {
        return 0;
    }

	public boolean isUpgradeable() {
		return false ;
	}

    public boolean isRepairable() { return false ; }

	public boolean isIdentified() {	return true; }

    public boolean isEnchantKnown() { return true; }

    public boolean isCursedKnown() { return true; }

    public boolean isTypeKnown() { return true; }

    public boolean isEquipped( Hero hero ) { return false; }

	public Item identify() {

        identify( UPGRADE_KNOWN );

		return this;
	}

    public Item identify( int value ) {

        identify( value, false );

        return this;
    }

    public Item identify( int value, boolean forced ) {

        if( forced || known < value ) {
            known = value;
        }

		QuickSlot.refresh();

        return this;
    }

    public void activate( Char ch ) {
    }

	public static void evoke( Hero hero ) {
		hero.sprite.emitter().burst( Speck.factory( Speck.EVOKE ), 5 );
	}

	@Override
	public String toString() {

		if (isIdentified() && isUpgradeable()) {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_LVL_X, name(), bonus, quantity );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, name(), bonus);
			}
		} else {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_X, name(), quantity );
			} else {
				return Utils.format( TXT_TO_STRING, name() );
			}
		}
	}

    public String equipAction() {
        return null;
    }

	public String quickAction() {
		return null;
	}

	public String name() {
		return name;
	}

	public int image() {
		return image;
	}

    public int imageAlt() {
        return image;
    }

	public ItemSprite.Glowing glowing() {
		return null;
	}

	public String info() {
		return desc();
	}

	public String desc() {
		return "";
	}

	public int quantity() {
		return quantity;
	}

	public Item quantity( int value ) {
		quantity = value;
        return this;
	}



    public String lootChapterAsString() {
        switch( lootChapter() ){
            case 1:
                return "common";
            case 2:
                return "regular";
            case 3:
                return "infrequent";
            case 4:
                return "exceptional";
            case 5:
                return "mythical";
            default:
                return "unknown";
        }
    }

    public int lootChapter() {
        return 0;
    }

	public int lootLevel() {
		return 0;
	}

	public int price() {
		return 0;
	}

    public int priceModifier() { return 5; }

    public float stealingDifficulty() { return 0.5f; }

	public static Item virtual( Class<? extends Item> cl ) {
		try {

			Item item = cl.newInstance();
			item.quantity = 0;
			return item;

		} catch (Exception e) {
			return null;
		}
	}

	public Item random() {
		return this;
	}

	public String status() {
		return quantity != 1 ? Integer.toString( quantity ) : null;
	}

	public void updateQuickslot() {

		if (stackable) {
			Class<? extends Item> cl = getClass();
			if (QuickSlot.quickslot0 != null && QuickSlot.quickslot0.value == cl ||
                QuickSlot.quickslot1 != null && QuickSlot.quickslot1.value == cl ||
                QuickSlot.quickslot2 != null && QuickSlot.quickslot2.value == cl ||
                QuickSlot.quickslot3 != null && QuickSlot.quickslot3.value == cl
            ) {
				QuickSlot.refresh();
			}
		} else if (
            QuickSlot.quickslot0 != null && QuickSlot.quickslot0.value == this ||
            QuickSlot.quickslot1 != null && QuickSlot.quickslot1.value == this ||
            QuickSlot.quickslot2 != null && QuickSlot.quickslot2.value == this ||
            QuickSlot.quickslot3 != null && QuickSlot.quickslot3.value == this
        ) {
			QuickSlot.refresh();
		}
	}

	private static final String QUANTITY		= "quantity";
	private static final String BONUS           = "level";
	private static final String KNOWN       	= "identified";
	private static final String DURABILITY		= "durability";
	private static final String STATE		    = "condition";

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( QUANTITY, quantity );
        bundle.put( KNOWN, known );
        bundle.put( BONUS, bonus );
        bundle.put( STATE, state );
        bundle.put( DURABILITY, durability );
		QuickSlot.save(bundle, this);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		quantity = bundle.getInt(QUANTITY);
        known = bundle.getInt(KNOWN);
        bonus = bundle.getInt(BONUS);
        state = bundle.getInt(STATE);
        durability = bundle.getInt(DURABILITY);

		QuickSlot.restore( bundle, this );
	}

	public void throwAt(final Hero user, int dst) {

        curUser = user;
        curItem = this;

        if( curUser.buff( Vertigo.class ) != null ) {
            dst += Level.NEIGHBOURS8[ Random.Int( 8 ) ];
        }

        final int cell = Ballistica.cast( user.pos, dst, false, true );

        user.sprite.cast(cell);
        user.busy();

        Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

        Char enemy = Actor.findChar(cell);
        QuickSlot.target(this, enemy);

        float delay = TIME_TO_THROW;

        if (this instanceof Weapon) {
            delay /= ((Weapon) this).speedFactor(user);
        }

        final float finalDelay = delay;

        ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
            reset(user.pos, cell, this, new Callback() {
                @Override
                public void call() {
                    onThrow(cell);
                    user.spendAndNext(finalDelay);
            }
        });

        Invisibility.dispel();


	}

	public static Hero curUser = null;
	public static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curItem.throwAt(curUser, target);
			}
		}
		@Override
		public String prompt() {
			return "Choose direction of throw";
		}
	};
}
