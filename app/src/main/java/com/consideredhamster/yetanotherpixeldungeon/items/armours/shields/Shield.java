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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.shields;

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Guard;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Durability;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Featherfall;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponHeavyTH;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class Shield extends Armour {


    public Shield(int tier) {

        super(tier);

    }

    private static final String TXT_NOTEQUIPPED = "You have to equip this shield first.";
    private static final String TXT_GUARD = "guard";

    private static final String AC_GUARD = "GUARD";

    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_GUARD );
//        return actions;
//    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {

                GLog.n(TXT_NOTEQUIPPED);

            }  else {

                hero.buff( Satiety.class ).decrease( Satiety.POINT * str() / hero.STR() );
                Buff.affect(hero, Guard.class ).reset( 1 );
                hero.spendAndNext( Actor.TICK );

            }

        } else {

            super.execute( hero, action );

        }
    }

    public void doEquipCarefully( Hero hero ) {


        if( hero.belongings.weap1!=null && this.incompatibleWith(hero.belongings.weap1) ) {

            final Hero heroFinal = hero;

            GameScene.show(
                    new WndOptions( TXT_ITEM_IS_INCOMPATIBLE, TXT_R_U_SURE_INCOMPATIBLE, TXT_YES, TXT_NO ) {

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Shield.super.doEquipCarefully( heroFinal );
                            }
                        }
                    }
            );

        }else
            super.doEquipCarefully( hero );
    }
	
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
		if (super.doUnequip( hero, collect, single )) {
			
			hero.belongings.weap2 = null;
            QuickSlot.refresh();

			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }

    @Override
	public int maxDurability() {
		return 100 ;
	}

    @Override
    public int dr( int bonus ) {
        return 5 - tier + tier * state
                + ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                + ( glyph instanceof Durability && bonus >= 0 ? 2 + bonus : 0 ) ;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + tier * 8 - 8 ;
    }

    @Override
    public int str( int bonus ) {
        return 6 + tier * 4 - bonus * ( glyph instanceof Featherfall ? 2 : 1 );
    }

    @Override
    public int strShown( boolean identified ) {
        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap2 && incompatibleWith( Dungeon.hero.belongings.weap1 ) ?
                        Dungeon.hero.belongings.weap1.str(
                                Dungeon.hero.belongings.weap1.isIdentified() ?
                                        Dungeon.hero.belongings.weap1.bonus : 0
                        ) : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return item instanceof MeleeWeaponHeavyTH; }
	
	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = GameMath.gate(0, penaltyBase(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;
        float armor = Math.max(0, isIdentified() ? dr() : dr(0) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "This _tier-" + tier + " shield_ requires _" + itemStr + " points of strength_ to use effectively and" +
                    ( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    "will occasionally increase your _armor class by " + armor + " points_.");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and dexterity with this shield " +
                                "will be _decreased by " + penalty + "%_ and your movement will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and dexterity with this shield " +
                                "will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                                "and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "While you are using this shield, your stealth and dexterity will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_, " +
                                "but with additional strength this penalty can be reduced" : "_not be decreased_" ) + "." );
            }
        } else {
            info.append(  "Usually _tier-" + tier + " shields_ require _" + itemStr + " points of strength_ to be used effectively and" +
                    ( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    "will occasionally increase your _armor class by " + armor + " points_." );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and dexterity with this shield " +
                                "probably will be _decreased by " + penalty + "%_ and your movement will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and dexterity with this shield " +
                                "probably will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                                "and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "While you are using this shield, your stealth and dexterity probably will " +
                                ( penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_" ) +
                                ", unless your strength will be different from this shield's actual strength requirement." );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "You hold the " + name + " at the ready." );

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

        if( isEnchantKnown() && glyph != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) + ", it seems to be _enchanted to " + glyph.desc(this) + "_." );
        }

        info.append( " This is a _" + lootChapterAsString() +"_ shield." );

        return info.toString();

	}

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }
	
	@Override
	public int price() {
		int price = 15 + state * 5;

        price *= lootChapter();

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if( glyph != null && isEnchantKnown() ) {
            price += price / 4;
        }

		return price;
	}
}
