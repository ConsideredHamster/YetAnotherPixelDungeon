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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;

public class EntrancePainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

        Room.Door.Type type = Room.Door.Type.REGULAR;

        if( Dungeon.depth == 2 && !(
            Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_1 ) ||
            Badges.isUnlocked( Badges.Badge.STRENGTH_ATTAINED_1 ) ||
            Badges.isUnlocked( Badges.Badge.ITEMS_UPGRADED_1) )
        ) {
            type = Room.Door.Type.HIDDEN;
        }

		for (Room.Door door : room.connected.values()) {
            door.set( type );
		}

		level.entrance = room.random( 1 );
		set( level, level.entrance, Terrain.ENTRANCE );
	}
	
}
