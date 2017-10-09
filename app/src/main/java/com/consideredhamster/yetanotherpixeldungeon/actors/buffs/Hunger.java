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
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSatiety;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundle;

public class Hunger extends Buff {

	private static final float STEP	= 6f;

    public static final float STARVING	= 600f;
    public static final float HUNGRY	= STARVING * 3 / 4;
    public static final float OVERFED   = STARVING * 1 / 4;

	private static final String TXT_OVERFED		= "You are overfed...  Can't eat anymore.";
	private static final String TXT_HUNGRY		= "You are hungry.";
	private static final String TXT_STARVING	= "You are starving!";
	
	private float level;

	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			Hero hero = (Hero)target;
			
			if (isStarving()) {

				if ( !target.stunned ) {

					hero.damage( hero.HT / 25 + ( hero.HT % 25 > Random.Int( 25 ) ? 1 : 0 ), this, DamageType.BODY );

				}

			} else {

				satisfy( (-1.0f) * STEP
//                    * ( hero.HP < hero.HT  ? 1.25f : 1.0f )
                    * ( hero.restoreHealth ? 0.50f : 1.0f )
                );

			}

            float modifier = target.ringBuffs(RingOfSatiety.Satiety.class);

			spend( STEP * modifier );
			
		} else {
			
			deactivate();
			
		}

		return true;
	}

    public void satisfy( float energy ) {
        satisfy( energy, false );
    }
	
	public void satisfy( float energy, boolean limited ) {

        energy *= target.ringBuffsThirded( RingOfSatiety.Satiety.class ) ;

        float newLevel = level - energy;
//        boolean statusUpdated = false;

         if ( newLevel <= OVERFED && level > OVERFED ) {

            GLog.w(TXT_OVERFED);

        }  else if ( newLevel >= STARVING && level < STARVING ) {

             ((Hero)target).interrupt( "You were awoken by a pain in you stomach." );
             GLog.n(TXT_STARVING);

        } else if ( newLevel >= HUNGRY && level < HUNGRY ) {

             ((Hero)target).interrupt( "You were awoken by a rumbling in your stomach." );
             GLog.w(TXT_HUNGRY);

        }

        if (newLevel < 0 ) {
            newLevel = 0;
        } else if (newLevel < OVERFED && limited ) {
            newLevel = OVERFED;
        } else if (newLevel > STARVING) {
            newLevel = STARVING;
        }

        level = newLevel;

//        if (statusUpdated) {

            BuffIndicator.refreshHero();

//        }

    }
	
	public boolean isStarving() {
		return level >= STARVING;
	}
	public boolean isOverfed() {
		return level <= OVERFED;
	}

    public float regenerationRate(){
        if (level < OVERFED) {
            return 1.5f;
        } else if (level < HUNGRY ) {
            return 1.0f;
        } else if (level < STARVING ) {
            return 0.5f;
        } else {
            return 0.5f;
        }
    }

	@Override
	public int icon() {
        if (level < OVERFED) {
            return BuffIndicator.OVERFED;
        } else if (level < HUNGRY) {
            return BuffIndicator.NONE;
        } else if (level < STARVING) {
			return BuffIndicator.HUNGER;
		} else {
			return BuffIndicator.STARVATION;
		}
	}
	
	@Override
	public String toString() {
        if (level < OVERFED) {
            return "Overfed";
        } else if (level < HUNGRY) {
            return "Satiated";
        } else if (level < STARVING) {
            return "Hungry";
        } else {
			return "Starving";
		}
	}

    public float value() {
        return ( STARVING - level ) / STARVING;
    }

    public float level() {
        return level;
    }

//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromHunger();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.HUNGER, Dungeon.depth ) );
//		GLog.n( TXT_DEATH );
//	}

}
