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

import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Ethereal;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class EquipableItem extends Item {

	protected static final String TXT_EQUIP = "You equip your %s.";
	protected static final String TXT_UNEQUIP = "You unequip your %s.";
    protected static final String TXT_ISEQUIPPED	= "%s is already equipped";

	private static final String TXT_UNEQUIP_CURSED = "You fail to remove cursed %s.";
	private static final String TXT_DETECT_CURSED = "You manage to notice that %s is cursed just before you equip it!";

    protected static final String TXT_EQUIP_CURSED_HAND = "your grip involuntarily tightens around your %s";
    protected static final String TXT_EQUIP_CURSED_BODY = "your %s constricts around you painfully";
    protected static final String TXT_EQUIP_CURSED_RING = "your %s suddenly tightens around your finger";

	public static final String AC_EQUIP		= "EQUIP";
	public static final String AC_UNEQUIP	= "UNEQUIP";

    @Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

    public int str(int bonus) {
        return 0;
    }

    public int str() {
        return str(bonus);
    }

    public int strShown( boolean identified ) {
        return identified ? str() : str(0) ;
    }

    public int penaltyBase(Hero hero, int str) {

        int delta = str - hero.STR();

        return delta < 0 ? delta : delta * 2;

    }

    public float penaltyFactor( Hero hero, boolean identified ) {

        return 1.0f - 0.025f * GameMath.gate( 0, penaltyBase(hero, strShown( identified ) ) -
            ( this instanceof Weapon && ((Weapon)this).enchantment instanceof Ethereal ? bonus : 0 ), 20 );

    }

    public float speedFactor( Hero hero ) {

        return hero.STR() < strShown( true ) ?  1.0f / ( 2.0f - penaltyFactor( hero, true ) ) : 1.0f;

    }

    public boolean incompatibleWith( EquipableItem item ) { return false ; }
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EQUIP )) {
			doEquip( hero );
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
		} else {
			super.execute( hero, action );
		}
	}
	
	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}

	@Override
	public void onThrow( int cell ) {

		if (isEquipped( curUser ) ) {

            if (quantity == 1 && !this.doUnequip( curUser, false, false )) {
				return;
			}
        }

        super.onThrow( cell );
	}

//    @Override
//    protected void onThrow( int cell ) {
//
//        if (isEquipped( curUser )) {
//            if (quantity == 1 && !this.doUnequip( curUser, false, false )) {
//                return;
//            }
//        }
//
//        super.onThrow(cell);
//    }

    protected static boolean detectCursed( Item item, Hero hero ) {

        float chance = 0.2f;

        if( item instanceof Weapon ) {

            chance -= item.bonus * ( ((Weapon)item).isEnchanted() ? 0.1f : 0.05f );

        } else if( item instanceof Armour ) {

            chance -= item.bonus * ( ((Armour)item).isInscribed() ? 0.1f : 0.05f );

        } else if( item instanceof Wand ) {

            chance -= item.bonus * 0.1f ;

        } else if( item instanceof Ring ) {

            chance -= item.bonus * 0.05f ;

        }

        if( Random.Float() < chance * hero.awareness() ) {

            item.identify(CURSED_KNOWN);
            GLog.w( TXT_DETECT_CURSED, item.name() );

            Sample.INSTANCE.play( Assets.SND_CURSED, 0.8f, 0.8f, 0.8f );
            Camera.main.shake( 1, 0.1f );

            return true;

        } else {

            if( item instanceof BodyArmor ) {
                GLog.n( TXT_EQUIP_CURSED_BODY, item.name() );
            } else if( item instanceof Ring ) {
                GLog.n( TXT_EQUIP_CURSED_RING, item.name() );
            } else {
                GLog.n( TXT_EQUIP_CURSED_HAND, item.name() );
            }

            hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

            Sample.INSTANCE.play( Assets.SND_CURSED );

            return false;

        }
    }

	protected float time2equip( Hero hero ) {
		return 1.0f / speedFactor( hero );
	}

    public boolean disarmable() {
        return bonus >= 0;
    }
	
	public abstract boolean doEquip( Hero hero );
	
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

        if (bonus < 0) {
//            int dmg = hero.HP / 5;
//
//            if( dmg > Random.Int( hero.HT / ( 4 + bonus ) ) ) {
//                hero.damage(dmg, null, null);
//                GLog.p(TXT_UNEQUIP_CURSED_SUCCESS, name() );
//            } else {
//                hero.damage(dmg, null, null);
//                GLog.w(TXT_UNEQUIP_CURSED_FAIL, name() );
                GLog.w(TXT_UNEQUIP_CURSED, name() );
                return false;
//            }
        } else if (single) {
            hero.spendAndNext( time2equip( hero ) );
            GLog.i(TXT_UNEQUIP, name());
        }
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.pos );
		}


				
		return true;
	}
	
	public final boolean doUnequip( Hero hero, boolean collect ) {
		return doUnequip( hero, collect, true );
	}
}
