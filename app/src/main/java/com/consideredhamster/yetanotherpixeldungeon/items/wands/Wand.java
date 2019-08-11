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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.ItemStatusHandler;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public abstract class Wand extends EquipableItem {

	private static final int USAGES_TO_KNOW	= 5;
	
	public static final String AC_ZAP	= "ZAP";
	
//	private static final String TXT_WOOD	= "This thin %s wand is warm to the touch. Who knows what it will do when used?";
//	private static final String TXT_DAMAGE	= "When this wand is used as a melee weapon, its average damage is %d points per hit.";
//	private static final String TXT_WEAPON	= "You can use this wand as a melee weapon.";
			
	private static final String TXT_FIZZLES		= "fizzle";
	private static final String TXT_SQUEEZE		= "You squeeze another charge from your wand";
//	private static final String TXT_MISCAST		= "The wand miscasts!";
	private static final String TXT_SELF_TARGET	= "You can't target yourself";
//	private static final String TXT_TARGET_CHARMED	= "You can't bring yourself to harm someone so... charming.";

	private static final String TXT_IDENTIFY	= "You are now familiar enough with your %s.";
	private static final String TXT_UNEQUIPPED	= "You can't use unequipped wands.";

	private static final float TIME_TO_ZAP	= 1f;

	protected float curCharges = maxCharges();
	
	private int usagesToKnow = Random.IntRange(USAGES_TO_KNOW, USAGES_TO_KNOW * 2);

	protected boolean hitChars = true;
	protected boolean goThrough = true;

	private static final Class<?>[] wands = {
		    WandOfMagicMissile.class,
            WandOfDisintegration.class,
            WandOfSmiting.class,
            WandOfLightning.class,
            WandOfAcidSpray.class,
            WandOfFirebrand.class,
            WandOfBlastWave.class,
            WandOfThornvines.class,
            WandOfIceBarrier.class,
            WandOfCharm.class,
            WandOfLifeDrain.class,
            WandOfDamnation.class,
	};

	// not really needed after the wands update, but I guess can stay here until moving to the Evan's engine
	private static final String[] woods =
		{"holly", "yew", "ebony", "cherry", "teak", "rowan", "willow", "mahogany", "bamboo", "purpleheart", "oak", "birch"};

	private static final Integer[] images = {
            ItemSpriteSheet.WAND_MAGICMISSILE,
            ItemSpriteSheet.WAND_DISINTEGRATION,
            ItemSpriteSheet.WAND_SMITING,
            ItemSpriteSheet.WAND_LIGHTNING,
            ItemSpriteSheet.WAND_ACIDSPRAY,
            ItemSpriteSheet.WAND_FIREBRAND,
            ItemSpriteSheet.WAND_BLAST_WAVE,
            ItemSpriteSheet.WAND_THORNVINE,
            ItemSpriteSheet.WAND_ICEBARRIER,
            ItemSpriteSheet.WAND_DOMINATION,
            ItemSpriteSheet.WAND_LIFEDRAIN,
            ItemSpriteSheet.WAND_DAMNATION,
	};
	
	private static ItemStatusHandler<Wand> handler;

    public boolean dud = false;

    public static boolean allKnown() {
        return handler.known().size() == wands.length;
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
//        syncWood();
//        shortName = "??";
	}

//    public void syncWood() {
//        image	= handler.image( this );
//        wood	= handler.label( this );
//    }

    @Override
    public boolean doEquip( Hero hero ) {

        detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) &&
                ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

            hero.belongings.weap2 = this;
            activate(hero);

            onEquip( hero );

            QuickSlot.refresh();

            hero.spendAndNext(time2equip(hero));
            return true;

        } else {

            QuickSlot.refresh();
            hero.spendAndNext(time2equip(hero) * 0.5f);

            if ( !collect( hero.belongings.backpack ) ) {
                Dungeon.level.drop( this, hero.pos ).sprite.drop();
            }

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

	public void charge( float modifier ) {
        if( curCharges < maxCharges() ){
            curCharges += modifier * rechargeRate();
        } else {
            curCharges = maxCharges();
        }
	}

    public abstract int damageRoll();

    public void addCharges( int value ) {
        curCharges = GameMath.gate( 0, (int)curCharges + value, maxCharges() );
        updateQuickslot();
    }

    public void setCharges( int value ) {
        curCharges = GameMath.gate( 0, value, maxCharges() );
        updateQuickslot();
    }

    public int getCharges() {
        return (int)curCharges;
    }

    public abstract int maxCharges( int bonus );
    public int maxCharges() {
        return maxCharges( bonus );
    };

    protected abstract float rechargeRate( int bonus );
    protected float rechargeRate() {
        return rechargeRate( bonus );
    };

    protected abstract float miscastChance( int bonus );
    protected float miscastChance() {
        return miscastChance( bonus );
    };

    protected abstract float squeezeChance( int bonus );
    protected float squeezeChance() {
        return squeezeChance( bonus );
    };

	protected abstract float effectiveness( int bonus );
    protected float effectiveness() { return effectiveness( bonus ); };

    protected abstract String wandInfo();

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

//    @Override
//    public String name() {
//        return name( isTypeKnown() );
//    }
//
//    public String name( boolean isTypeKnown ) {
//        return isTypeKnown ? super.name() : wood + " wand";
//    }
	
//	@Override
//	public String info() {
//		StringBuilder info = new StringBuilder( isTypeKnown() ? desc() : String.format( TXT_WOOD, wood ) );
//
//		return info.toString();
//	}

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

    public static HashSet<Class<? extends Wand>> getKnown() {
        return handler.known();
    }
	
	@Override
	public String status() {
		if (isIdentified()) {
			return getCharges() + "/" + maxCharges();
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

	protected void fx( int cell, Callback callback ) {
		MagicMissile.blueLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

    protected abstract void spendCharges();

	protected void wandUsed( boolean identify ) {

        spendCharges();

		if ( identify && !isIdentified() ) {

            float effect = curUser.ringBuffs(RingOfKnowledge.Knowledge.class);

            usagesToKnow -= (int) effect;
            usagesToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

            if ( usagesToKnow <= 0 ) {
                identify();
                GLog.w(TXT_IDENTIFY, name());
            }
		}

        updateQuickslot();

		curUser.spendAndNext( TIME_TO_ZAP );
	}


	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {

			if (target != null) {

                if (target == curUser.pos) {
                    GLog.i(TXT_SELF_TARGET);
                    return;
                }

                final Wand curWand = (Wand) Wand.curItem;

                if (curUser.buff(Vertigo.class) != null) {
                    target += Level.NEIGHBOURS8[Random.Int(8)];
                }

                final int cell = Ballistica.cast( curUser.pos, target, curWand.goThrough, curWand.hitChars );

                Char ch = Actor.findChar(cell);

                if (ch != null && curUser != ch && Dungeon.visible[cell]) {

                    QuickSlot.target(curItem, ch);
                    TagAttack.target((Mob) ch);

                }

                curUser.busy();

                curUser.sprite.cast(cell,
                        new Callback() {
                    @Override
                    public void call() {

                        if ( curWand.getCharges() > 0
                            && ( curWand.isIdentified() || Random.Float() > curWand.miscastChance() )
                            || curWand.getCharges() == 0 && Random.Float() < curWand.squeezeChance()
                        ) {

                            if (curWand.curCharges == 0) {

                                curWand.use(3);
                                curUser.buff( Satiety.class ).decrease( Satiety.POINT * 2.0f );

                                if (curWand.isIdentified()) {
                                    GLog.w(TXT_SQUEEZE);
                                }

                            } else {

                                curWand.use(curWand instanceof WandUtility ? (int)curWand.curCharges : 2);
                                curUser.buff( Satiety.class ).decrease( Satiety.POINT * 1.0f );

                            }

                            curWand.fx(cell, new Callback() {
                                @Override
                                public void call() {
                                    curWand.onZap(cell);
                                    curWand.wandUsed( true );
                                }
                            });

                        } else {

                            curUser.sprite.showStatus(CharSprite.WARNING, TXT_FIZZLES);
                            curWand.wandUsed( false );

                        }
                    }
                });

                Invisibility.dispel();
                curWand.setKnown();

            }
		}

		@Override
		public String prompt() {
			return "Choose direction to zap";
		}
	};

    @Override
    public int maxDurability() {
        return 50;
    }

    @Override
    public float stealingDifficulty() { return 0.75f; }

    @Override
    public int price() {

        int price = 40 + state * 20;

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        return price;
    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder( desc() );
        info.append( p );

        if( !dud ){

            info.append( wandInfo() );
            info.append( p );

            if( isEquipped( Dungeon.hero ) ){
                info.append( "You hold this wand at the ready." );
            } else if( Dungeon.hero.belongings.backpack.items.contains( this ) ){
                info.append( "This wand is in your backpack." );
            } else {
                info.append( "This wand lies on the dungeon's floor." );
            }

            if( bonus < 0 && isCursedKnown() ){

                info.append( " " );

                if( isEquipped( Dungeon.hero ) ){
                    info.append( "Malevolent magic prevents you from releasing it." );
                } else {
                    info.append( "You can feel a malevolent magic lurking within it." );
                }
            }
        }

        return info.toString();
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
        curCharges = bundle.getFloat( CUR_CHARGES );
    }
}
