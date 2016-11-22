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

import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.RatKing;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class RatKingPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );
		
		Room.Door entrance = room.entrance();
		entrance.set( Room.Door.Type.HIDDEN );
		int door = entrance.x + entrance.y * Level.WIDTH;
		
		for (int i=room.left + 1; i < room.right; i++) {
			addChest( level, (room.top + 1) * Level.WIDTH + i, door );
			addChest( level, (room.bottom - 1) * Level.WIDTH + i, door );
		}
		
		for (int i=room.top + 2; i < room.bottom - 1; i++) {
			addChest( level, i * Level.WIDTH + room.left + 1, door );
			addChest( level, i * Level.WIDTH + room.right - 1, door );
		}
		
		while (true) {
			Heap chest = level.heaps.get( room.random() );
			if (chest != null) {
				chest.type = Heap.Type.CHEST_MIMIC;
				break;
			}
		}
		
		RatKing king = new RatKing();
		king.pos = room.random( 1 );
		level.mobs.add( king );
	}
	
	private static void addChest( Level level, int pos, int door ) {
		
		if (pos == door - 1 || 
			pos == door + 1 || 
			pos == door - Level.WIDTH || 
			pos == door + Level.WIDTH) {
			return;
		}
		
		Item prize;
		switch (Random.Int( 10 )) {
		case 0:
			prize = Generator.random( Generator.Category.WEAPON );
			if (prize instanceof ThrowingWeapon) {
				prize.quantity( 1 );
			} else {
				prize.curse( Random.Int( 3 ) );
			}
			break;
		case 1:
			prize = Generator.random( Generator.Category.ARMOR ).curse( Random.Int( 3 ) );
			break;
		default:
			prize = new Gold( Random.IntRange( 1, 5 ) );
			break;
		}
		
		level.drop( prize, pos, true ).type = Heap.Type.CHEST;
	}
}
