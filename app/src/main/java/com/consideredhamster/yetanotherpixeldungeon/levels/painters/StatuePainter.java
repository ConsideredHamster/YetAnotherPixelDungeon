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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Statue;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfInvisibility;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class StatuePainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

//		Point c = room.center();
//		int cx = c.x;
//		int cy = c.y;
		
		Room.Door door = room.entrance();
		
		door.set(Room.Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey());

        int x = -1;
        int y = -1;

        Room.Door entrance = room.entrance();
        Point a = null;
        Point b = null;

        if (door.x == room.left) {

            a = new Point( room.left+1, entrance.y-1 );
            b = new Point( room.left+1, entrance.y+1 );
            x = room.right - 1;
            y = room.top + room.height() / 2;

        } else if (door.x == room.right) {

            a = new Point( room.right-1, entrance.y-1 );
            b = new Point( room.right-1, entrance.y+1 );
            x = room.left + 1;
            y = room.top + room.height() / 2;

        } else if (door.y == room.top) {

            a = new Point( entrance.x+1, room.top+1 );
            b = new Point( entrance.x-1, room.top+1 );
            x = room.left + room.width() / 2;
            y = room.bottom - 1;

        } else if (door.y == room.bottom) {

            a = new Point( entrance.x+1, room.bottom-1 );
            b = new Point( entrance.x-1, room.bottom-1 );
            x = room.left + room.width() / 2;
            y = room.top + 1;

        }

        if (a != null && level.map[a.x + a.y * Level.WIDTH] == Terrain.EMPTY) {
            set( level, a, Terrain.STATUE );
        }
        if (b != null && level.map[b.x + b.y * Level.WIDTH] == Terrain.EMPTY) {
            set( level, b, Terrain.STATUE );
        }

        int pos = x + y * Level.WIDTH;

        level.drop( prize(), pos, true ).type =
        Random.Int(3) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP;
        set(level, pos, Terrain.PEDESTAL);

        Statue statue1 = new Statue();
        Statue statue2 = new Statue();

        if (door.x == room.left || door.x == room.right) {

            statue1.pos = pos + Level.WIDTH;
            statue2.pos = pos - Level.WIDTH;

        } else if (door.y == room.top || door.y == room.bottom) {

            statue1.pos = pos + 1;
            statue2.pos = pos - 1;

        }

        statue1.special = true;
        statue2.special = true;

        level.mobs.add(statue1);
        Actor.occupyCell(statue1);

        level.mobs.add(statue2);
        Actor.occupyCell(statue2);

        level.addItemToSpawn(new PotionOfInvisibility());
		
//		Statue statue = new Statue();
//		statue.pos = cx + cy * Level.WIDTH;
//		bonus.mobs.add(statue);
//		Actor.occupyCell( statue );

//        int cx = (room.left + room.right) / 2;
//        int cy = (room.top + room.bottom) / 2;
//        int c = cx + cy * Level.WIDTH;
//
//        set(bonus, c, Terrain.PEDESTAL);
//        bonus.drop(prize(bonus), c);
	}

    private static Item prize() {

        Weapon prize = null;

        for (int i=0; i < 4; i++) {

            Weapon another;
            do {
                another = (Weapon)Generator.random( Generator.Category.WEAPON );
            } while (another instanceof ThrowingWeapon || another.bonus < 0 );

            if (prize == null || another.lootLevel() > prize.lootLevel()) {
                prize = another;
            }
        }

        prize.repair();
//        prize.cursed = true;

        return prize;
    }
}
