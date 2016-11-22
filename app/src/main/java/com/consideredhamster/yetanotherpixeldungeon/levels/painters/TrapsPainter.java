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

import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class TrapsPainter extends Painter {

	public static void paint( Level level, Room room ) {
		 
//		Integer traps[] = {
//			Terrain.TOXIC_TRAP, Terrain.TOXIC_TRAP, Terrain.TOXIC_TRAP,
//			Terrain.CHASM, Terrain.CHASM, Terrain.CHASM,
//			!Dungeon.bossLevel( Dungeon.depth + 1 ) ? Terrain.CHASM : Terrain.SUMMONING_TRAP };

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.TOXIC_TRAP );
//
		Room.Door door = room.entrance();
		door.set( Room.Door.Type.REGULAR );
		
//		int lastRow = level.map[room.left + 1 + (room.top + 1) * Level.WIDTH] == Terrain.CHASM ? Terrain.CHASM : Terrain.EMPTY;

		int x = -1;
		int y = -1;
		if (door.x == room.left) {
			x = room.right - 1;
			y = room.top + room.height() / 2;
			fill( level, x, room.top + 1, 1, room.height() - 1 , Terrain.EMPTY );
		} else if (door.x == room.right) {
			x = room.left + 1;
			y = room.top + room.height() / 2;
			fill( level, x, room.top + 1, 1, room.height() - 1 , Terrain.EMPTY );
		} else if (door.y == room.top) {
			x = room.left + room.width() / 2;
			y = room.bottom - 1;
			fill( level, room.left + 1, y, room.width() - 1, 1 , Terrain.EMPTY );
		} else if (door.y == room.bottom) {
			x = room.left + room.width() / 2;
			y = room.top + 1;
			fill( level, room.left + 1, y, room.width() - 1, 1 , Terrain.EMPTY );
		}

		int pos = x + y * Level.WIDTH;
        set( level, pos, Terrain.PEDESTAL );
        level.drop( prize( level ), pos, true ).type = Heap.Type.HEAP;

//        Point pos = room.center();
//        int p = pos.x + pos.y * Level.WIDTH;
//
//        set( level, pos, Terrain.PEDESTAL );
//        level.drop( prize(level), p ).type = Heap.Type.HEAP;

		level.addItemToSpawn( new PotionOfLevitation() );
	}
	
	private static Item prize( Level level ) {

        Item prize = level.itemToSpawnAsPrize( Wand.class );

        if (prize != null) {
            return prize;
        }

		prize = level.itemToSpawnAsPrize();

		if (prize != null) {
			return prize;
		}
		
		prize = Generator.random( Random.oneOf(  
			Generator.Category.WEAPON, 
			Generator.Category.ARMOR 
		) );

		for (int i=0; i < 3; i++) {
			Item another = Generator.random( Random.oneOf(  
				Generator.Category.WEAPON, 
				Generator.Category.ARMOR 
			) );
			if (another.lootLevel() > prize.lootLevel()) {
				prize = another;
			}
		}
		
		return prize;
	}
}
