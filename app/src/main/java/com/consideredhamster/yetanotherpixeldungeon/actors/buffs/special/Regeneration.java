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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;

public class Regeneration extends Buff {
	
	private static final float REGENERATION_DELAY = 20f;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT
                && !(target.isDamagedOverTime())
                && (target instanceof Hero && !((Hero)target).isStarving())
            ) {

               target.HP += 1;

                if( target.HP == target.HT && ((Hero) target).restoreHealth ) {
//                    ((Hero) target).restoreHealth = false;

                        ((Hero) target).interrupt( Level.water[ target.pos ] ?
                                "You don't feel well. Better not sleep in the water next time." :
                                "You feel well rested.",
                                !Level.water[ target.pos ]
                        );

                }
			}
			
			float bonus = target.ringBuffs(RingOfVitality.Vitality.class);

            if( target instanceof Hero ) {

                Hero hero = ((Hero)target);

                bonus *= ( 1.0f + ( hero.lvl - 1 ) * 0.1f + hero.strBonus * 0.1f );

                if( hero.restoreHealth && !Level.water[target.pos] ) {
                    bonus *= 3.0f;
                }

//                if( ((Hero) target).heroClass == HeroClass.WARRIOR )
//                    bonus *= 1.25;
//
//                else if( ((Hero) target).heroClass == HeroClass.ACOLYTE )
//                    bonus *= 0.75;
            }

//            if( target instanceof Mob && ((Mob) target).state != ((Mob) target).HUNTING )
//                bonus *= 2.0f;

//            for (Satiety b : target.buffs( Satiety.class )) {
//                bonus *= b.regenerationRate();
//            }

			spend( REGENERATION_DELAY / bonus );
			
		} else {
			
			deactivate();
			
		}

		return true;
	}
}
