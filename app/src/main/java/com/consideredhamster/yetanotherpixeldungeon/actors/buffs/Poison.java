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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.utils.Bundle;

public class Poison extends Buff {
	
	protected int left;
	
	private static final String LEFT	= "left";

    private static final float DELAY = 5f;
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getInt(LEFT);
	}
	
	public void set( int duration ) {
        this.left = duration;
    };

    public void addDuration( int duration ) {
        this.left += duration;
    };
	
	@Override
	public int icon() {
		return BuffIndicator.POISON;
	}
	
	@Override
	public String toString() {
        return Utils.format("Poisoned (%d)", left);
	}
	
	@Override
	public boolean act() {
		if (target.isAlive() && ( left -= 1 ) >= 0 ) {
//                target.damage(1, null, DamageType.BODY);
                target.damage( Math.max( 0, target.HT / 25 + ( target.HT % 25 > Random.Int(25) ? 1 : 0 ) ), this, DamageType.BODY );

//                float bonus = 1.0f;
//
//                if( target instanceof Hero ) {
//
//                    bonus *= 1.0f + ( (float)((Hero)target).lvl - 1.0f ) / 7.5f ;
//
//                } else if( target instanceof Mob ) {
//                    bonus *= 2.0f ;
//                }

//                spend( DELAY / bonus );
                spend( DELAY );

		} else {
			
			detach();
			
		}

		return true;
	}

//	public static float durationFactor( Char ch ) {
//		Protection r = ch.buff( Protection.class );
//		return r != null ? r.durationFactor() : 1;
//	}

//	@Override
//	public void onDeath() {
//		Badges.validateDeathFromPoison();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.POISON, Dungeon.depth ) );
//		GLog.n( "You died from poison..." );
//	}

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Body.class;
    }

}
