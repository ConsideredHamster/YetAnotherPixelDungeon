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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.body;

import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.*;
import com.consideredhamster.yetanotherpixeldungeon.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public abstract class BodyArmor extends Armour {
	
	private static final String TXT_EQUIP_CURSED	= "your %s constricts around you painfully";

	public int appearance;

    public BodyArmor( int tier ) {

        super(tier);

    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);
		
		if (hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false )) {
			
			hero.belongings.armor = this;

            GLog.i(TXT_EQUIP, name());
			
			identify( CURSED_KNOWN );

			if (bonus < 0) {
				equipCursed( hero );
				GLog.n( TXT_EQUIP_CURSED, toString() );
			}
			
			((HeroSprite)hero.sprite).updateArmor();
			
			hero.spendAndNext(time2equip(hero));
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
			
		}
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			
			hero.belongings.armor = null;

			((HeroSprite)hero.sprite).updateArmor();
			
			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.armor == this;
    }

    @Override
	public int maxDurability() {
		return 150 ;
	}

    @Override
    protected float time2equip( Hero hero ) {
        return super.speedFactor( hero ) * 3;
    }

    @Override
    public int dr( int bonus ) {
        return tier * ( 4 + state )
                + ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                + ( glyph instanceof Durability && bonus >= 0 ? tier + bonus - 1 : 0 ) ;
    }

    @Override
    public int str(int bonus) {
        return 9 + tier * 4 - bonus * ( glyph instanceof Featherfall ? 2 : 1 );
    }
	
	@Override
	public String info() {

        final String p = "\n\n";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        int penalty = GameMath.gate( 0, penaltyBase(Dungeon.hero, strShown(isIdentified())), 20 ) * 5;
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
            info.append( "This _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )  + "armor_ requires _" + itemStr + " points of strength_ to use effectively and" +
                    ( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    "will increase your _armor class by " + armor + " points_.");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and dexterity in this armor " +
                                "will be _decreased by " + penalty + "%_ and your movement will be _" + (100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and dexterity while you are wearing this armor " +
                                "will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                                "and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "While you are wearing this armor, your stealth and dexterity will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_, " +
                                "but with additional strength this penalty can be reduced" : "_not be decreased_" ) + "." );
            }
        } else {
            info.append(  "Usually _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )  + "armors_ require _" + itemStr + " points of strength_ to be worn effectively and" +
                    ( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    "will increase your _armor class by " + armor + " points_." );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and dexterity in this armor " +
                                "probably will be _decreased by " + penalty + "%_ and your movement will be _" + (100 - 10000 / (100 + penalty)) + "% slower_." );
            } else if (itemStr < heroStr) {
                info.append(
                        "Because of your excess strength, your stealth and dexterity while you are wearing this armor " +
                                "probably will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
                                "and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "While you are wearing this armor, your stealth and dexterity probably will " +
                                ( penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_" ) +
                                ", unless your strength will be different from this armor's actual strength requirement." );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "You are wearing the " + name + "." );

            if( isCursedKnown() && bonus < 0 ) {
                info.append( " Because it is _cursed_, you are powerless to remove it." );
            } else if( isIdentified() ) {
                info.append( bonus > 0 ? " It appears to be _upgraded_." : " It appears to be _non-cursed_." );
            } else {
                info.append( " This " + name + " is _unidentified_." );
            }

            if( isEnchantKnown() && glyph != null ) {
                info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) + ", it seems to be _enchanted to " + glyph.desc(this) + "_." );
            }

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "The " + name + " is in your backpack. " );

            if( isCursedKnown() && bonus < 0 ) {
                info.append( "A malevolent _curse_ seems to be lurking within this " + name +". Equipping it will be most likely a very bad idea." );
            } else if( isIdentified() ) {
                info.append( bonus > 0 ? " It appears to be _upgraded_." : " It appears to be _non-cursed_." );
            } else {
                info.append( " This " + name + " is _unidentified_." );
            }

            if( isEnchantKnown() && glyph != null ) {
                info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) + ", it seems to be _enchanted to " + glyph.desc(this) + "_." );
            }

        } else {

            info.append( "The " + name + " lies on the dungeon's floor." );

        }

        return info.toString();

	}
	
	@Override
	public int price() {
		int price = 20 + state * 10;

        price *= lootChapter() + 1;

		if (isIdentified()) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
		} else {
            price /= 2;
        }

        if( glyph != null ) {
            price += price / 4;
        }

		return price;
	}
}
