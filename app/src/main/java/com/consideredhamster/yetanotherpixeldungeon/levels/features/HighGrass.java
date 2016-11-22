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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		
//		Level.set( pos, Terrain.GRASS_EMPTY );
//		GameScene.updateMap( pos );
//
//		if (!Dungeon.isChallenged( Challenges.NO_HERBALISM )) {
//
//			float modifier = 1;
//
//			if (ch != null) {
//                for (RingOfHerbalism.Herbalism b : ch.buffs(RingOfHerbalism.Herbalism.class )) {
//                    modifier *= b.effect();
//                }
//			}
//
//			// Seed
//			if (modifier >= 0 && Random.Float() * modifier <= 0.05 ) {
//				bonus.drop( Generator.random( Generator.Category.HERB ), pos ).sprite.drop();
//			}
//
//			// Dew
//			if (modifier >= 0 && Random.Float() * modifier <= 0.15 ) {
//				bonus.drop( new Dewdrop(), pos ).sprite.drop();
//			}
//		}
		
//		int leaves = 4;
		
		// Warlock's barkskin
//		if (ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
//			Buff.affect( ch, Barkskin.class ).bonus( ch.HT / 3 );
//			leaves = 8;
//		}
		
//		CellEmitter.get( pos ).burst( LeafParticle.LEVEL_SPECIFIC, leaves );
//        if( Dungeon.visible[ pos ] ) {
//            CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, Random.Int(5, 6));
//            Dungeon.observe();
//        }
	}
}
