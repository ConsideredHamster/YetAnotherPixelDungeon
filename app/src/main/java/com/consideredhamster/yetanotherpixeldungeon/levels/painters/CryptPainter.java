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
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorCloth;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class CryptPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;
		
		Room.Door entrance = room.entrance();
		
		entrance.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
		
		if (entrance.x == room.left) {
			set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
			cx = room.right - 2;
		} else if (entrance.x == room.right) {
			set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
			cx = room.left + 2;
		} else if (entrance.y == room.top) {
			set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
			cy = room.bottom - 2;
		} else if (entrance.y == room.bottom) {
			set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
			cy = room.top + 2;
		}

//        set(level, cx + cy * Level.WIDTH, Terrain.PEDESTAL);
        level.drop( prize(), cx + cy * Level.WIDTH, true ).type = Type.TOMB;
	}

    private static Item prize() {

        Armour prize = null;

        for (int i=0; i < 4; i++) {

            Armour another;
            do {
                another = (Armour)Generator.random( Generator.Category.ARMOR );
            } while (another instanceof BodyArmorCloth || another.bonus < 0 );

            if (prize == null || another.lootLevel() > prize.lootLevel()) {
                prize = another;
            }
        }

//        Item prize = Generator.random(Generator.Category.WEAPON);
//
//        for (int i=0; i < 3; i++) {
//            Item another = Generator.random( Generator.Category.WEAPON );
//            if (another.bonus > prize.bonus) {
//                prize = another;
//            }
//        }

        prize.inscribe().repair();

        return prize;
    }
}
