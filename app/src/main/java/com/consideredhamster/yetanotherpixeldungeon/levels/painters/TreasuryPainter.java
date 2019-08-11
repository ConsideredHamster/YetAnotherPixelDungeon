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
package com.consideredhamster.yetanotherpixeldungeon.levels.painters;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class TreasuryPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill(level, room, 1, Terrain.EMPTY);
		
		set( level, room.center(), Terrain.STATUE );

        int n = Dungeon.chapter() + Random.IntRange( 1, 3 );

        for (int i = 1; i <= n; i++) {
            int pos;
            do {
                pos = room.random();
            } while (level.map[pos] != Terrain.EMPTY);

            if( i < n ) {

                level.drop(new Gold().random(), pos, true).type = Heap.Type.CHEST;

            } else {

                level.drop( prize( level ), pos, true ).type = Random.Int( 6 - Dungeon.chapter() ) == 0 ? Heap.Type.CHEST_MIMIC : Heap.Type.CHEST ;

            }
        }

		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}

    private static Item prize( Level level ) {

        Item prize = level.itemToSpawnAsPrize( Ring.class );

        if (prize != null) {
            return prize;
        }

        return new Gold().random();
    }
}
