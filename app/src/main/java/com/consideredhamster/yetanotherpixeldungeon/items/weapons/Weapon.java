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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons;

import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.*;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Weapon extends EquipableItem {

	private static final int HITS_TO_KNOW	= 10;
	
	private static final String TXT_IDENTIFY		= 
		"You are now familiar enough with your %s to identify it. It is %s.";
//	private static final String TXT_INCOMPATIBLE	=
//		"Interaction of different types of magic has negated the enchantment on this weapon!";
	private static final String TXT_TO_STRING		= "%s :%d";

//    private static final String TXT_BROKEN	= "Your %s has broken!";
	
	public int	tier = 1;

//	public int		STR	= 10;
//	public float	ACU	= 1;
//	public float	DLY	= 1f;

    public enum Type {
        M_SWORD, M_BLUNT, M_POLEARM,
        R_MISSILE, R_FLINTLOCK, UNSPECIFIED
    }
	
	private int hitsToKnow = Random.IntRange(HITS_TO_KNOW, HITS_TO_KNOW * 2);
	
	public Enchantment enchantment;

//    public Weapon() {
//        defaultAction = AC_THROW;
//    }


//    private static final String TXT_EQUIP	= "you equip %s";
//    private static final String TXT_UNEQUIP	= "you unequip %s";

//    public int		MIN	= 0;
//    public int		MAX = 1;

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
//        return actions;
//    }

    public void doEquipCarefully( Hero hero ) {


        if(!(this instanceof MeleeWeaponLightOH) && hero.belongings.weap2!=null && this.incompatibleWith(hero.belongings.weap2) ) {

            final Hero heroFinal = hero;

            GameScene.show(
                    new WndOptions( TXT_ITEM_IS_INCOMPATIBLE, TXT_R_U_SURE_INCOMPATIBLE, TXT_YES, TXT_NO ) {

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Weapon.super.doEquipCarefully( heroFinal );
                            }
                        }
                    }
            );

        }else
            super.doEquipCarefully( hero );
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap1 == this || hero.belongings.weap2 == this;
    }

    @Override
    public boolean doEquip( Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

            if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot1.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot2.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot3.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if ( ( hero.belongings.weap1 == null || hero.belongings.weap1.doUnequip(hero, true, false) ) &&
                    ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                hero.belongings.weap1 = this;
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
        } else {

            GLog.w(TXT_ISEQUIPPED, name());
            return false;

        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.weap1 = ( hero.belongings.weap1 == this ? null : hero.belongings.weap1 );

            hero.belongings.weap2 = ( hero.belongings.weap2 == this ? null : hero.belongings.weap2 );

            QuickSlot.refresh();

            return true;

        } else {

            return false;

        }
    }

    public void proc( Char attacker, Char defender, int damage ) {

		if ( enchantment != null ) {
			if( enchantment.proc( this, attacker, defender, damage ) && !isEnchantKnown() ) {
                identify(ENCHANT_KNOWN);
            }
		}
		
		if (!isIdentified()) {

            float effect = attacker.ringBuffs(RingOfKnowledge.Knowledge.class);

            hitsToKnow -= (int)effect ;
            hitsToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

			if (hitsToKnow <= 0) {
				identify();
				GLog.i( TXT_IDENTIFY, name(), toString() );
//				Badges.validateItemLevelAcquired(this);
			}
		}
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String IMBUE			= "imbue";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( ENCHANTMENT, enchantment );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchant( (Enchantment) bundle.get( ENCHANTMENT ) );
	}

	public int damageRoll( Hero hero ) {

		int dmg = Math.max( 0, Random.NormalIntRange( min(), max() ) );


        int exStr = hero.STR() - strShown( true );

        if (exStr > 0) {
            dmg += Random.IntRange( 0, exStr );
        }

        if( enchantment instanceof Heroic) {
            dmg += bonus >= 0
                    ? dmg * (hero.HT - hero.HP) * (bonus + 1) / hero.HT / 8
                    : dmg * (hero.HT - hero.HP) * (bonus) / hero.HT / 6;
        }

        return dmg;

	}

    public boolean canBackstab() {
        return false;
    }

    @Override
    public boolean disarmable() {
        return super.disarmable() && !(enchantment instanceof Heroic);
    }

    public int min( int bonus ) {
        return 0;
    }

    public int max( int bonus ) {
        return 0;
    }

    public int min() {
        return min(bonus);
    }

    public int max() {
        return max(bonus);
    }

    @Override
    public Item uncurse() {

        if(bonus == -1) {
            enchant(null);
        }

        return super.uncurse();
    }

    @Override
    public int lootChapter() {
        return tier;
    }

    @Override
    public int lootLevel() {
        return ( lootChapter() - 1 ) * 6 + state * 2 + bonus * 2 + ( isEnchanted() ? 3 + bonus : 0 );
    }

    public float breakingRateWhenShot() {
        return 0f;
    }

    @Override
    public int priceModifier() { return 3; }

	@Override
	public String name() {
        return enchantment != null && isEnchantKnown() ? enchantment.name( this ) : super.name();
    }

    public String simpleName() {
        return super.name();
    }

    public Type weaponType() { return Type.UNSPECIFIED; }
	
	public Weapon enchant( Enchantment ench ) {

        enchantment = ench;

		return this;
	}
	
	public Weapon enchant() {
		
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while (ench.getClass() == oldEnchantment) {
			ench = Enchantment.random();
		}
		
		return enchant( ench );
	}
	
	public boolean isEnchanted() {
		return enchantment != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && isEnchantKnown() ? enchantment.glowing() : null;
	}

//    @Override
//    protected void onThrow( int cell ) {
//        Char enemy = Actor.findChar(cell);
//
////        Invisibility.dispel();
//
//        if (!throwEquipped || enemy == curUser || enemy == null) {
//            super.onThrow( cell );
//        } else {
//
//            if( this instanceof ThrowingWeapon ) {
//
//                if (!curUser.shoot(enemy, this) || Random.Int( 60 ) > ((ThrowingWeapon)this).baseAmount() ) {
//
//                    if (this instanceof Chakrams || this instanceof Boomerangs || this instanceof Harpoons) {
//
//                        ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
//                                reset(cell, curUser.pos, curItem, null);
//
//                        curUser.belongings.weap2 = this;
////                    curUser.spend( -TIME_TO_EQUIP );
//
////                        QuickSlot.refresh();
//
//                    } else {
//                        super.onThrow(cell);
//                    }
//
//                } else {
//
//                    if (quantity == 1) {
//
//                        doUnequip( curUser, false, false );
//
//                    } else {
//
//                        detach( null );
//
//                    }
//                }
//
//            } else {
//
//                curUser.shoot(enemy, this);
//
//                super.onThrow(cell);
//            }
//        }
//
//        QuickSlot.refresh();
//    }

	public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[]{
            Blazing.class, Shocking.class, Freezing.class, Caustic.class,  Heroic.class,
            Arcane.class, Tempered.class, Vampiric.class, Ethereal.class, Vorpal.class
        };

		private static final float[] chances = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

        protected static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF5511 );
        protected static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
        protected static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x009900 );
        protected static ItemSprite.Glowing MUSTARD = new ItemSprite.Glowing( 0xBBBB33 );
        protected static ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0x00AAFF );
        protected static ItemSprite.Glowing GRAY = new ItemSprite.Glowing( 0x888888 );
        protected static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );
        protected static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x2244FF );
        protected static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
        protected static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0xAA00AA );
        protected static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF44 );

        protected abstract String name_p();
        protected abstract String name_n();
        protected abstract String desc_p();
        protected abstract String desc_n();

        protected abstract boolean proc_p( Char attacker, Char defender, int damage );
        protected abstract boolean proc_n( Char attacker, Char defender, int damage );

        public Class<? extends Wand> wandBonus() {
            return null;
        }

        public static boolean procced( int bonus ) {

            return Random.Float() < ( 0.1f + 0.1f * ( 1 + Math.abs( bonus ) )* ( bonus >= 0 ?
                Dungeon.hero.ringBuffs( RingOfMysticism.Mysticism.class ) : 1.0f ) );

        }

        public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {

            boolean result = procced( weapon.bonus )
                && ( weapon.bonus >= 0
                    ? proc_p(attacker, defender, damage)
                    : proc_n(attacker, defender, damage)
                );

            if( result ) {
                weapon.identify( ENCHANT_KNOWN );
            }

            return result;
        }

        public String name( Weapon weapon ) {
            return String.format( weapon.bonus >= 0 ? name_p() : name_n(), weapon.name );
        }

        public String desc( Weapon weapon ) {
            return weapon.bonus >= 0 ? desc_p() : desc_n();
        }

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}

		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}

		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
	}

    public String descType() {
        return "";
    }

    @Override
    public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = GameMath.gate( 0, penaltyBase(Dungeon.hero, strShown(isIdentified())) -
                ( isEnchantKnown() && enchantment instanceof Ethereal ? bonus : 0 ), 20 ) * 2.5f;
//        float power = Math.max(0, isIdentified() ? (float)(min() + max()) / 2 : ((float)(min(0) + max(0)) / 2) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "This _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )  + "weapon_ " +
                    "requires _" + itemStr + " points of strength_ to use effectively and" +
                    ( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    "will deal _" + min() + "-" + max() + " points of damage_ per hit.");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and accuracy with it " +
                        "will be _decreased by " + penalty + "%_ and attacking with it will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and accuracy with it " +
                        "will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                        "and attacking with it will deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
            } else {
                info.append(
                        "When wielding this weapon, your stealth and accuracy with it will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_, " +
                        "but with additional strength this penalty can be reduced" : "_not be decreased_" ) + "." );
            }
        } else {
            info.append(  "Usually _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )  + "weapons_ require _" + itemStr + " points of strength_ to use effectively and" +
                    ( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    "deal _" + min(0) + "-" + max(0) + " points of damage_ per hit." );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and accuracy with it " +
                                "probably will be _decreased by " + penalty + "%_ and attacking with it will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and accuracy with it " +
                                "probably will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                                "and attacking with it will deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
            } else {
                info.append(
                        "When wielding this weapon, your stealth and accuracy with it probably will " +
                                ( penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_" ) +
                                ", unless your strength will be different from this weapon's actual strength requirement." );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append("You hold the " + name + " at the ready.");

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "The " + name + " is in your backpack." );

        } else {

            info.append( "The " + name + " lies on the dungeon's floor." );

        }

        info.append( s );

        if( isIdentified() && bonus > 0 ) {
            info.append( "It appears to be _upgraded_." );
        } else if( isCursedKnown() ) {
            info.append( bonus >= 0 ? "It appears to be _non-cursed_." :
                    "A malevolent _curse_ seems to be lurking within this " + name +"." );
        } else {
            info.append( " This " + name + " is _unidentified_." );
        }

        info.append( s );

        if( isEnchantKnown() && enchantment != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) +
                    ", it seems to be _enchanted to " + enchantment.desc(this) + "_." );
        }

        info.append( " This is a _" + lootChapterAsString() +"_ weapon." );

        return info.toString();
    }
}
