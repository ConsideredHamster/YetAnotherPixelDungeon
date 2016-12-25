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

import com.watabou.utils.Point;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class GardenPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.HIGH_GRASS );
		fill( level, room, 2, Terrain.WATER );

        for (Point door : room.connected.values()) {
            if (door.x == room.left) {
                set( level, door.x + 1, door.y, Terrain.EMPTY );
            } else if (door.x == room.right) {
                set( level, door.x - 1, door.y, Terrain.EMPTY );
            } else if (door.y == room.top) {
                set( level, door.x, door.y + 1, Terrain.EMPTY );
            } else if (door.y == room.bottom) {
                set( level, door.x , door.y - 1, Terrain.EMPTY );
            }
        }

        int chance = 9 - Dungeon.chapter();

        for (int i=room.left + 1; i < room.right; i++) {
            if( Random.Int( chance ) == 0 ) {
                level.drop(Generator.random(Generator.Category.HERB), (room.top + 1) * Level.WIDTH + i, true).type = Heap.Type.HEAP;
            }

            if( Random.Int( chance ) == 0 ) {
                level.drop(Generator.random(Generator.Category.HERB), (room.bottom - 1) * Level.WIDTH + i, true).type = Heap.Type.HEAP;
            }
        }

        for (int i=room.top + 2; i < room.bottom - 1; i++) {
            if( Random.Int( chance ) == 0 ) {
                level.drop(Generator.random(Generator.Category.HERB), i * Level.WIDTH + room.left + 1, true).type = Heap.Type.HEAP;
            }

            if( Random.Int( chance ) == 0 ) {
                level.drop(Generator.random(Generator.Category.HERB), i * Level.WIDTH + room.right - 1, true).type = Heap.Type.HEAP;
            }
        }
		
//		if (Random.Int( 2 ) == 0) {
//			bonus.drop( new Honeypot(), room.random() );
//		} else {
//			int bushes = (Random.Int( 5 ) == 0 ? 2 : 1);
//			for (int i=0; i < bushes; i++) {
//				int pos = room.random();
//				set( bonus, pos, Terrain.GRASS );
//				bonus.plant( new Sungrass.Seed(), pos );
//			}
//		}
		
//		Foliage light = (Foliage)bonus.blobs.get( Foliage.class );
//		if (light == null) {
//			light = new Foliage();
//		}
//		for (int i=room.top + 1; i < room.bottom; i++) {
//			for (int j=room.left + 1; j < room.right; j++) {
//				light.seed( j + Level.WIDTH * i, 1 );
//			}
//		}
//		bonus.blobs.put( Foliage.class, light );

        room.entrance().set( Dungeon.depth > 1 ? Room.Door.Type.HIDDEN : Room.Door.Type.REGULAR );
	}
}
