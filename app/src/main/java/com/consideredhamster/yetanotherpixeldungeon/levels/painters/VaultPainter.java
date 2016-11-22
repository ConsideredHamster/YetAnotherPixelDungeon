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
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap.Type;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.GoldenKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class VaultPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		int cx = (room.left + room.right) / 2;
		int cy = (room.top + room.bottom) / 2;
		int c = cx + cy * Level.WIDTH;
		
//		switch (Random.Int( 3 )) {
//
//		case 0:
			level.drop( prize( level ), c, true ).type = Type.LOCKED_CHEST;
			level.addItemToSpawn( new GoldenKey() );
//			break;
			
//		case 1:
//			Item i1, i2;
//			do {
//				i1 = prize( level );
//				i2 = prize( level );
//			} while (i1.getClass() == i2.getClass());
//			level.drop( i1, c ).type = Type.CRYSTAL_CHEST;
//			level.drop( i2, c + Level.NEIGHBOURS4[Random.Int( 4 )]).type = Type.CRYSTAL_CHEST;
//			level.addItemToSpawn( new GoldenKey() );
//			break;
//
//		case 2:
//			level.drop( prize( level ), c );
//			set( level, c, Terrain.PEDESTAL );
//			break;
//		}
		
		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
	
	private static Item prize( Level level ) {

        Item wand = level.itemToSpawnAsPrize( Wand.class );
        Item ring = level.itemToSpawnAsPrize( Ring.class );

        Item prize = Random.oneOf( wand, ring );

        if( wand != null && prize != wand )
            level.addItemToSpawn( wand );

        if( ring != null && prize != ring )
            level.addItemToSpawn( ring );

        if( prize != null ) {
            return prize;
        }

		return Generator.random( Random.oneOf(  
			Generator.Category.WAND, 
			Generator.Category.RING 
		) );
	}
}
