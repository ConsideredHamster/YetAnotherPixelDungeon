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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.ui.AttackIndicator;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.ItemStatusHandler;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSorcery;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class Wand extends EquipableItem {

	private static final int USAGES_TO_KNOW	= 10;
	
	public static final String AC_ZAP	= "ZAP";
	
	private static final String TXT_WOOD	= "This thin %s wand is warm to the touch. Who knows what it will do when used?";
//	private static final String TXT_DAMAGE	= "When this wand is used as a melee weapon, its average damage is %d points per hit.";
//	private static final String TXT_WEAPON	= "You can use this wand as a melee weapon.";
			
	private static final String TXT_FIZZLES		= "fizzle";
	private static final String TXT_SQUEEZE		= "You squeeze another charge from your wand";
	private static final String TXT_MISCAST		= "The wand miscasts!";
	private static final String TXT_SELF_TARGET	= "You can't target yourself";
	private static final String TXT_TARGET_CHARMED	= "You can't bring yourself to harm someone so... charming.";

	private static final String TXT_IDENTIFY	= "You are now familiar enough with your %s.";
	private static final String TXT_UNEQUIPPED	= "You can't use unequipped wands.";

	private static final float TIME_TO_ZAP	= 1f;

	public int curCharges = maxCharges();
	
	protected Charger charger;
	
//	private boolean curChargeKnown = false;
	
	private int usagesToKnow = Random.IntRange(USAGES_TO_KNOW, USAGES_TO_KNOW * 2);

	protected boolean hitChars = true;
	
	private static final Class<?>[] wands = {
		WandOfMagicMissile.class,
		WandOfPhasing.class,
		WandOfFreezing.class,
		WandOfFirebolt.class, 
		WandOfHarm.class,
		WandOfEntanglement.class,
		WandOfBlink.class,
		WandOfLightning.class,
		WandOfCharm.class,
//		WandOfTelekinesis.class,
		WandOfFlock.class,
		WandOfDisintegration.class,
		WandOfAvalanche.class
	};
	private static final String[] woods = 
		{"holly", "yew", "ebony", "cherry", "teak", "rowan", "willow", "mahogany", "bamboo", "purpleheart", "oak", "birch"};
	private static final Integer[] images = {
		ItemSpriteSheet.WAND_HOLLY, 
		ItemSpriteSheet.WAND_YEW, 
		ItemSpriteSheet.WAND_EBONY, 
		ItemSpriteSheet.WAND_CHERRY, 
		ItemSpriteSheet.WAND_TEAK, 
		ItemSpriteSheet.WAND_ROWAN, 
		ItemSpriteSheet.WAND_WILLOW, 
		ItemSpriteSheet.WAND_MAHOGANY, 
		ItemSpriteSheet.WAND_BAMBOO, 
		ItemSpriteSheet.WAND_PURPLEHEART, 
		ItemSpriteSheet.WAND_OAK, 
		ItemSpriteSheet.WAND_BIRCH};
	
	private static ItemStatusHandler<Wand> handler;

	private String wood;

	{
        shortName = "??";
	}

    @Override
    public String equipAction() {
        return AC_ZAP;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }
	
	@SuppressWarnings("unchecked")
	public static void initWoods() {
		handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images, bundle );
	}
	
	public Wand() {
		super();
		
//		calculateDamage();
		
		try {
			image = handler.image( this );
			wood = handler.label( this );
		} catch (Exception e) {
			// Wand of Magic Missile
		}
	}

    private static final String TXT_EQUIP_CURSED	= "you wince as your grip involuntarily tightens around your %s";

    @Override
    public boolean doEquip( Hero hero ) {

        detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if (hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false )) {

            hero.belongings.weap2 = this;
            activate(hero);

            GLog.i(TXT_EQUIP, name());

            identify( CURSED_KNOWN );

            if (bonus < 0) {
                equipCursed( hero );
                GLog.n( TXT_EQUIP_CURSED, toString() );
            }

            QuickSlot.refresh();

            hero.spendAndNext(time2equip(hero));
            return true;

        } else {

            collect(hero.belongings.backpack);
            return false;

        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

        onDetach();

        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.weap2 = null;
            QuickSlot.refresh();

            return true;

        } else {

            activate( hero );

            return false;

        }
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }
	
//	@Override
//	public ArrayList<String> actions( Hero hero ) {
//		ArrayList<String> actions = super.actions( hero );
//        actions.add(AC_ZAP);
//
//		return actions;
//	}
	
	@Override
	public void activate( Char ch ) {
//        stopCharging();
		charge(ch);
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_ZAP )) {

            if (isEquipped(hero)) {
                curUser = hero;
                curItem = this;
                GameScene.selectCell(zapper);
            } else {
                GLog.n( TXT_UNEQUIPPED );
            }
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	protected abstract void onZap( int cell );
	
	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null) {
				charge(container.owner);
			}
			return true;
		} else {
			return false;
		}
	};
	
	public void charge( Char owner ) {
		if (charger == null) {
			(charger = new Charger()).attachTo(owner);
		}
	}
	
	@Override
	public void onDetach() {
		stopCharging();
	}
	
	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}

    public int min() {
        return 0;
    }

    public int max() {
        return 0;
    }

    public int damageRoll() {
        float dmg = Random.NormalIntRange( min(), max() ) * effectiveness();

        if( curUser != null && curUser.buff( Withered.class ) != null )
            dmg = (int)( dmg * curUser.buff( Withered.class ).modifier() );

        return (int)dmg;
    }

    public int maxCharges() {
        return bonus > 0 ? 2 + bonus : 2 ;
    }

    public int maxDurability() {
        return 50;
    }

    protected float rechargeRate() {
        return 30f;
    }

    protected float miscastChance() {

        float chance = 0.125f - state * 0.025f;

        if( charger != null && charger.target instanceof Hero ) {

            chance /= charger.target.ringBuffs( RingOfSorcery.Sorcery.class );

            if( ((Hero)charger.target).belongings.weap1 instanceof Quarterstaff ) {
                chance /= 2.0f;
            }
        }

        return chance;

    }

    protected float squeezeChance() {

        float chance = 0.05f + state * 0.025f;

        if( charger != null && charger.target instanceof Hero ) {

            chance *= charger.target.ringBuffs( RingOfSorcery.Sorcery.class );

            if( ((Hero)charger.target).belongings.weap1 instanceof Quarterstaff ) {
                chance *= 2.0f;
            }
        }

        return chance;
    }

	
	public float effectiveness() {

        float power = 0.25f + state * 0.125f + Math.max( 0, bonus ) * 0.125f ;

        if (charger != null) {

            Hero hero = (Hero)charger.target;

            if(
                hero.belongings.weap1 != null &&
                hero.belongings.weap1.bonus >= 0 &&
                hero.belongings.weap1.enchantment != null &&
                hero.belongings.weap1.enchantment.wandBonus() == getClass()
            ) {
                power *= 1.125f + (hero.belongings.weap1.bonus >= 0 ? hero.belongings.weap1.bonus * 0.125f : 0.0f);
            }
		}

        return power;
	}



	@Override
	public Item identify() {

        super.identify();

        setKnown();

		updateQuickslot();
		
		return this;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder( super.toString() );
		
		String status = status();
		if (status != null) {
			sb.append(" (" + status + ")");
		}
		
		return sb.toString();
	}
	
	@Override
	public String name() {
		return isTypeKnown() ? super.name() : wood + " wand";
	}
	
	@Override
	public String info() {
		StringBuilder info = new StringBuilder( isTypeKnown() ? desc() : String.format( TXT_WOOD, wood ) );

		return info.toString();
	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
    public boolean isTypeKnown() {
        return handler.isKnown( this );
    }

    public void setKnown() {
        if (!isTypeKnown()) {
            handler.know(this);
        }

        Badges.validateAllWandsIdentified();
    }
	
	@Override
	public String status() {
		if (isIdentified()) {
			return curCharges + "/" + maxCharges();
		} else {
			return null;
		}
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();

		curCharges = Math.min( curCharges + 1, maxCharges() );
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item curse() {
		super.curse();

        curCharges = curCharges > maxCharges() ? maxCharges() : curCharges ;
		updateQuickslot();
		
		return this;
	}

    public Wand recharge() {
        curCharges = maxCharges();
        updateQuickslot();

        return this;
    }
	
//	protected void updateLevel() {
//		maxCharges = Math.min( initialCharges() + state, 5 );
//		curCharges = Math.min( curCharges + 1, maxCharges() );
		
//		calculateDamage();
//	}
	
//	private void calculateDamage() {
//		int appearance = 1 + bonus / 3;
//		MIN = appearance;
//		MAX = (appearance * appearance - appearance + 10) / 2 + bonus;
//	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.blueLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

    protected void spendCharges() {

        if( curCharges > 0 ) curCharges--;

        if( charger != null ) charger.delay();

    }

	protected void wandUsed() {

        spendCharges();

		if (!isIdentified() ) {

            float effect = curUser.ringBuffs(RingOfKnowledge.Knowledge.class);

            usagesToKnow -= (int) effect;
            usagesToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

            if ( usagesToKnow <= 0 ) {
                identify();
                GLog.w(TXT_IDENTIFY, name());
            }
		}

        updateQuickslot();

		curUser.spendAndNext(TIME_TO_ZAP);
	}

    @Override
    public Item random() {

        Float rand = Random.Float();

        int n = 0;

        if ( rand < Dungeon.depth * 0.03f ) {
            n++;
            if ( rand < Dungeon.depth * 0.02f ) {
                n++;
                if ( rand < Dungeon.depth * 0.01f ) {
                    n++;
                }
            }
        }

        if( n > 0 ) {
            if (Random.Int(Dungeon.depth) < Random.Int(n * 10)) {
                curse(n);
            } else {
                upgrade(n);
            }
        }

        randomize_state();

        curCharges = maxCharges();

        return this;
    }

//    @Override
//    public void randomize_state() {
//        super.randomize_state();
//
//        curCharges = maxCharges();
//    }
	
	public static boolean allKnown() {
		return handler.known().size() == wands.length;
	}

    public static HashSet<Class<? extends Wand>> getKnown() {
        return handler.known();
    }
	
	@Override
	public int price() {

        int price = 30 + state * 15;

        if (isIdentified()) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else {
            price /= 2;
        }

        return price;
	}
	
	private static final String UNFAMILIRIARITY		= "unfamiliarity";
	private static final String CUR_CHARGES			= "curCharges";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( CUR_CHARGES, curCharges );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt( CUR_CHARGES );
	}
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {
				
				if (target == curUser.pos) {
					GLog.i( TXT_SELF_TARGET );
					return;
				}

				final Wand curWand = (Wand)Wand.curItem;

                if( curUser.buff( Confusion.class ) != null ) {
                    target += Level.NEIGHBOURS8[Random.Int( 8 )];
                }

                final int cell = Ballistica.cast( curUser.pos, target, false, curWand.hitChars );

                Char ch = Actor.findChar(cell);

                if( ch != null ) {

                    if ( curUser.isCharmedBy( ch ) ) {
                        GLog.i( TXT_TARGET_CHARMED );
                        return;
                    }

                    QuickSlot.target(curItem, ch);
                    AttackIndicator.target( (Mob)ch );
                }

                curUser.sprite.cast(cell);
                curUser.busy();

                if ( curWand.curCharges > 0 && Random.Float() > curWand.miscastChance() ||
                        curWand.curCharges == 0 && Random.Float() < curWand.squeezeChance() ) {

                    if( curWand.curCharges == 0 ) {

                        curWand.use(3);

                        if( curWand.isIdentified() ) {
                            GLog.w(TXT_SQUEEZE);
                        }

                    } else {

                        curWand.use( curWand instanceof WandUtility ? curWand.curCharges : 2);

                    }

                    curWand.fx(cell, new Callback() {
                        @Override
                        public void call() {
                            curWand.onZap(cell);
                            curWand.wandUsed();
                        }
                    } );



                    Invisibility.dispel();

                    curWand.setKnown();

                } else {

                    if( curWand.curCharges > 0 && curWand.isIdentified() ) {
                        GLog.w(TXT_MISCAST);
                        curWand.spendCharges();
                    }

                    curUser.spendAndNext(TIME_TO_ZAP);

                    curUser.sprite.showStatus(CharSprite.WARNING, TXT_FIZZLES);

                    if( !(curWand instanceof WandUtility) )
                        curWand.use(1);

					curWand.updateQuickslot();
				}
			}
		}
		
		@Override
		public String prompt() {
			return "Choose direction to zap";
		}
	};

    protected class Charger extends Buff {

        @Override
        public boolean attachTo( Char target ) {
            super.attachTo( target );
            delay();

            return true;
        }

        @Override
        public boolean act() {

            if (curCharges < maxCharges()) {
                curCharges++;
                updateQuickslot();
            }

            delay();

            return true;
        }

        protected void delay() {

            postpone( rechargeRate() / (float)Math.pow( ( (Hero)target ).magicPower(), 2 ) );

        }
    }
}
