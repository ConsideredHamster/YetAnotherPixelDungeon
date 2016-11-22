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
package com.consideredhamster.yetanotherpixeldungeon.levels.traps;

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.PoisonParticle;

public class PoisonTrap extends Trap {

	// 0xBB66EE
	
	public static void trigger( int pos ) {

        Char ch = Actor.findChar( pos );

        if (ch != null) {

            int power = 10 + Dungeon.chapter() * 6;

            Poison buff = Buff.affect( ch, Poison.class );

            if( buff != null ) {
                buff.addDuration( Random.IntRange(power / 2, power) );
            }
		}
		
		CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 3 );
		
	}
}
