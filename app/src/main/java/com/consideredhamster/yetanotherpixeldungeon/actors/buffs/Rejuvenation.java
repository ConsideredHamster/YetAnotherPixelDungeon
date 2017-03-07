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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs;

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Rejuvenation extends PassiveBuff {
	
	protected int left = 0;
	
	private static final String LEFT	= "left";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getInt(LEFT);
	}
	
	public void proliferate(int duration ) {

        if( left == 0 ) {
            this.left = duration;
        } else if( Random.Int( (int) Math.sqrt( left ) ) == 0 ) {
            this.left++;
        }

        if( target != null && target.HP < target.HT ) {

            int healing = Math.min(target.HT - target.HP, left );

            // FIXME

            if( target instanceof Hero && ((Hero)target).restoreHealth && ( target.HP + healing >= target.HT ) ) {
                ((Hero)target).interrupt();
            }

            target.HP += healing;

            target.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healing));

        }
    };
	
//	@Override
//	public int icon() {
//		return BuffIndicator.POISON;
//	}
	
//	@Override
//	public String toString() {
//        return Utils.format("Poisoned (%d)", left);
//	}
	
//	@Override
//	public boolean act() {
//		if (target.isAlive() && ( left -= 1 ) >= 0 ) {
//                target.damage(left, Poison.class);
//
//                spend( TICK );
//
//		} else {
//
//			detach();
//
//		}
//
//		return true;
//	}

//	public static float durationFactor( Char ch ) {
//		Resistance r = ch.buff( Resistance.class );
//		return r != null ? r.durationFactor() : 1;
//	}

//	@Override
//	public void onDeath() {
//        Badges.validateDeathFromGas();
//
//        Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
//        GLog.n( "You died from a toxic gas.." );
//	}

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Body.class;
    }

}
