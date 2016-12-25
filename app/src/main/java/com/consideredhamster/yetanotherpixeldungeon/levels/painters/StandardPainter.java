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
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class StandardPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );

		for (Room.Door door : room.connected.values()) {

//            door.set( Room.Door.Type.HIDDEN );

            switch ( Random.Int(10)) {
                case 0:
                    door.set( Dungeon.depth > 1 ? Room.Door.Type.HIDDEN : Room.Door.Type.EMPTY );

                    break;
                case 1:
                case 2:
                case 3:
                    door.set( Room.Door.Type.EMPTY );
                    break;
                case 4:
                case 5:
                case 6:
                    door.set( level.feeling == Level.Feeling.TRAPS ? Room.Door.Type.HIDDEN : Room.Door.Type.REGULAR );
                    break;
                case 7:
                case 8:
                case 9:
                    door.set( Room.Door.Type.REGULAR );
                    break;
            }
		}

        if( level.feeling == Level.Feeling.HAUNT &&	Math.min( room.width(), room.height() ) >= 4 && Random.Int( 3 ) == 0 ) {

            paintFissure( level, room );
            return;

        } else if( level.feeling == Level.Feeling.GRASS && Math.max(room.width(), room.height()) >= 4 && Random.Int( 3 ) == 0 ) {

            paintStriped(level, room);
            return;

        } else if( level.feeling == Level.Feeling.TRAPS && Math.min( room.width(), room.height() ) >= 4 && Random.Int( 3 ) == 0 ) {

            paintBurned(level, room);
            return;

        } else if( level.feeling == Level.Feeling.WATER && room.width() >= 4 && room.height() >= 4 && Random.Int( 3 ) == 0 ) {

            paintBridge(level, room);
            return;

        } else if (!Dungeon.bossLevel() && Random.Int( 3 ) == 0) {
            switch ( Random.Int(6) ) {
                case 0:
                    if (level.feeling != Level.Feeling.GRASS) {
                        if (Math.min(room.width(), room.height()) >= 4 && Math.max(room.width(), room.height()) >= 6) {
                            paintGraveyard(level, room);
                            return;
                        }
                        break;
                    } else {
//					 Burned room
                    }
                case 1:
                    if (Math.min(room.width(), room.height()) >= 4) {
                        paintBurned(level, room);
                        return;
                    }
                    break;
                case 2:
                    if (Math.max(room.width(), room.height()) >= 4) {
                        paintStriped(level, room);
                        return;
                    }
                    break;
                case 3:
                    if (room.width() >= 6 && room.height() >= 6) {
                        paintStudy(level, room);
                        return;
                    }
                    break;
                case 4:
                    if (room.width() >= 4 && room.height() >= 4) {
                        paintBridge(level, room);
                        return;
                    }
                    break;
                case 5:
                    if (!Dungeon.bossLevel() && Math.min(room.width(), room.height()) >= 5) {
                        paintFissure(level, room);
                        return;
                    }
                    break;

            }
        }

		fill( level, room, 1, Terrain.EMPTY );
	}
	
	private static void paintBurned( Level level, Room room ) {
		for (int i=room.top + 1; i < room.bottom; i++) {
			for (int j=room.left + 1; j < room.right; j++) {
				int t = Terrain.EMBERS;
				switch (Random.Int( 5 )) {
				case 0:
					t = Terrain.EMPTY;
					break;
				case 1:
					t = Terrain.FIRE_TRAP;
					break;
				case 2:
					t = Terrain.SECRET_FIRE_TRAP;
					break;
				case 3:
					t = Terrain.INACTIVE_TRAP;
					break;
				}
				level.map[i * Level.WIDTH + j] = t;
			}
		}

        if( Dungeon.depth > 1 ) {
            for (Room.Door door : room.connected.values()) {
                if (Random.Int(2) == 0) {
                    door.set(Room.Door.Type.HIDDEN);
                }
            }
        }
    }
	
	private static void paintGraveyard( Level level, Room room ) {
		fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 , Terrain.GRASS );
		
		int w = room.width() - 1;
		int h = room.height() - 1;
		int nGraves = Math.max( w, h ) / 2;
		
		int index = Random.Int( nGraves );
		
		int shift = Random.Int( 2 );
		for (int i=0; i < nGraves; i++) {
			int pos = w > h ?
				room.left + 1 + shift + i * 2 + (room.top + 2 + Random.Int( h-2 )) * Level.WIDTH :
				(room.left + 2 + Random.Int( w-2 )) + (room.top + 1 + shift + i * 2) * Level.WIDTH;	
			level.drop( new Gold().random(), pos, true ).type = Heap.Type.TOMB;
		}

        for (Room.Door door : room.connected.values()) {
            door.set( Room.Door.Type.EMPTY );
        }
	}
	
	private static void paintStriped( Level level, Room room ) {

        int chance = 9 - Dungeon.chapter();

		fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 , Terrain.EMPTY_SP );

		if (room.width() > room.height()) {
			for (int i = room.left + 2; i < room.right; i += 2) {

				fill( level, i, room.top + 1, 1, room.height() - 1, Terrain.HIGH_GRASS );

                for (int o = room.top + 1; o < room.bottom; o++) {
                    if( Random.Int( chance ) == 0  ) {
                        level.drop(Generator.random(Generator.Category.HERB), o * Level.WIDTH + i, true).type = Heap.Type.HEAP;
                    }
                }
			}
		} else {
			for (int i=room.top + 2; i < room.bottom; i += 2) {
				fill( level, room.left + 1, i, room.width() - 1, 1, Terrain.HIGH_GRASS );

                for (int o = room.left + 1; o < room.right; o++) {
                    if( Random.Int( chance ) == 0  ) {
                        level.drop(Generator.random(Generator.Category.HERB), i * Level.WIDTH + o, true).type = Heap.Type.HEAP;
                    }
                }
			}
		}
	}
	
	private static void paintStudy( Level level, Room room ) {

        fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 , Terrain.EMPTY_SP );

        if (room.width() > room.height()) {
            for (int i=room.left + 2; i < room.right; i += 2) {
                fill( level, i, room.top + 2, 1, room.height() - 3, Terrain.BOOKSHELF );
            }
        } else {
            for (int i=room.top + 2; i < room.bottom; i += 2) {
                fill( level, room.left + 2, i, room.width() - 3, 1, Terrain.BOOKSHELF );
            }
        }

//		fill( bonus, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 , Terrain.SHELF_EMPTY );
//		fill( bonus, room.left + 2, room.top + 2, room.width() - 3, room.height() - 3 , Terrain.EMPTY_SP );
//
		for (Point door : room.connected.values()) {
			if (door.x == room.left) {
				set( level, door.x + 1, door.y, Terrain.EMPTY_SP );
			} else if (door.x == room.right) {
				set( level, door.x - 1, door.y, Terrain.EMPTY_SP );
			} else if (door.y == room.top) {
				set( level, door.x, door.y + 1, Terrain.EMPTY_SP );
			} else if (door.y == room.bottom) {
				set( level, door.x , door.y - 1, Terrain.EMPTY_SP );
			}
		}
//
//		set( bonus, room.center(), Terrain.PEDESTAL );
	}
	
	private static void paintBridge( Level level, Room room ) {
		
        if( room.connected.size() == 2 ) {

            fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 ,
                    level.feeling == Level.Feeling.WATER ?
                            Terrain.WATER : Terrain.CHASM );

            Point door1 = null;
            Point door2 = null;
            for (Point p : room.connected.values()) {
                if (door1 == null) {
                    door1 = p;
                } else {
                    door2 = p;
                }
            }

            if ((door1.x == room.left && door2.x == room.right) ||
                    (door1.x == room.right && door2.x == room.left)) {

                int s = room.width() / 2;

                drawInside(level, room, door1, s, Terrain.EMPTY_SP);
                drawInside(level, room, door2, s, Terrain.EMPTY_SP);
                fill(level, room.center().x, Math.min(door1.y, door2.y), 1, Math.abs(door1.y - door2.y) + 1, Terrain.EMPTY_SP);

            } else if ((door1.y == room.top && door2.y == room.bottom) ||
                    (door1.y == room.bottom && door2.y == room.top)) {

                int s = room.height() / 2;

                drawInside(level, room, door1, s, Terrain.EMPTY_SP);
                drawInside(level, room, door2, s, Terrain.EMPTY_SP);
                fill(level, Math.min(door1.x, door2.x), room.center().y, Math.abs(door1.x - door2.x) + 1, 1, Terrain.EMPTY_SP);

            } else if (door1.x == door2.x) {

                fill(level, door1.x == room.left ? room.left + 1 : room.right - 1, Math.min(door1.y, door2.y), 1, Math.abs(door1.y - door2.y) + 1, Terrain.EMPTY_SP);

            } else if (door1.y == door2.y) {

                fill(level, Math.min(door1.x, door2.x), door1.y == room.top ? room.top + 1 : room.bottom - 1, Math.abs(door1.x - door2.x) + 1, 1, Terrain.EMPTY_SP);

            } else if (door1.y == room.top || door1.y == room.bottom) {

                drawInside(level, room, door1, Math.abs(door1.y - door2.y), Terrain.EMPTY_SP);
                drawInside(level, room, door2, Math.abs(door1.x - door2.x), Terrain.EMPTY_SP);

            } else if (door1.x == room.left || door1.x == room.right) {

                drawInside(level, room, door1, Math.abs(door1.x - door2.x), Terrain.EMPTY_SP);
                drawInside(level, room, door2, Math.abs(door1.y - door2.y), Terrain.EMPTY_SP);

            }
        } else {
            fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1, Terrain.EMPTY_SP );

            fill( level, room.left + 2, room.top + 2, room.width() - 3, room.height() - 3,
                    level.feeling == Level.Feeling.WATER ?
                            Terrain.WATER : Terrain.CHASM );
        }

        for (Room.Door door : room.connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }
	}
	
	private static void paintFissure( Level level, Room room ) {
		fill( level, room.left + 1, room.top + 1, room.width() - 1, room.height() - 1 ,Terrain.EMPTY );
		
		for (int i=room.top + 2; i < room.bottom - 1; i++) {
			for (int j=room.left + 2; j < room.right - 1; j++) {
				int v = Math.min( i - room.top, room.bottom - i );
				int h = Math.min( j - room.left, room.right - j );
				if (Math.min( v, h ) > 2 || Random.Int( 2 ) == 0) {
					set( level, j, i, Terrain.CHASM );
				}
			}
		}
	}
}
