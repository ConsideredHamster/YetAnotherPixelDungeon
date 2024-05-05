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
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.WellWater;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class BossExitPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
        fill( level, room.left, room.top - 1, room.width(), 1, Terrain.WALL );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
		
		level.exit = room.top * Level.WIDTH + (room.left + room.right) / 2;
		set( level, level.exit, Terrain.UNLOCKED_EXIT );

        Point c = room.center();
        set( level, c.x, c.y, Terrain.WELL );

        WellWater water = (WellWater)level.blobs.get( WellWater.class );
        if (water == null) {
            try {
                water = new WellWater();
            } catch (Exception e) {
                water = null;
            }
        }

        int amount = 3 + Dungeon.chapter() + Random.IntRange( 0, 4 );

        water.seed( c.x + Level.WIDTH * c.y, amount );
        level.blobs.put( WellWater.class, water );
	}
	
}
