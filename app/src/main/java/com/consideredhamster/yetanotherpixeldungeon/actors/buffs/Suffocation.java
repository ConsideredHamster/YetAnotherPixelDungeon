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

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;

public class Suffocation extends PassiveBuff {
	
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
        } else if( Random.Int(left) == 0 ) {
            this.left++;
        }

        target.damage( left, this, DamageType.BODY );
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
