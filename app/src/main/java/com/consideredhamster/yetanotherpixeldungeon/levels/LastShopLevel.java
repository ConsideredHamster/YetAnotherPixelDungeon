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
package com.consideredhamster.yetanotherpixeldungeon.levels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.WellWater;
import com.watabou.noosa.Scene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room.Type;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LastShopLevel extends RegularLevel {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}

//	private static final Room.Type[] treasuries = {
//        Type.ARMORY, Type.LABORATORY, Type.LIBRARY, Type.TREASURY, Type.MAGIC_WELL, Type.GARDEN
//    };

	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

	@Override
	protected boolean build() {
		
		initRooms();

        heaps.clear();
        mobs.clear();
		
		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );

		do {
			int innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);
			
			innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomExit = Random.element( rooms );
			} while (roomExit == roomEntrance || roomExit.width() < 6 || roomExit.height() < 6 || roomExit.top == 0);
	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = Graph.buildPath( rooms, roomEntrance, roomExit ).size();
			
			if (retry++ > 10) {
				return false;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.BOSS_EXIT;
		
		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
		}

        Room roomShop = null;
		int shopSquare = 0;
		for (Room r : rooms) {
			if (r.type == Type.NULL && r.connected.size() > 0) {
				r.type = Type.PASSAGE; 
				if (r.square() > shopSquare) {
					roomShop = r;
					shopSquare = r.square();
				}
			}
		}
		
		if ( roomShop == null || roomShop.width() < 7 || roomShop.height() < 7 ) {
			return false;
		} else {
			roomShop.type = Room.Type.SHOP;
		}

//        HashMap<Room, Room> candidates = new HashMap<>(  );
//
//        for( Room p : path ) {
//            for( Room r : p.neighbours ) {
//                if( r.type == Type.NULL && r.connected.size() == 0 && r.width() > 3 && r.height() > 3 ) {
//                    candidates.put( r, p );
//                }
//            }
//        }
//
//		if( candidates.size() < treasuries.length )
//		    return false;
//
//        for( Room.Type t : treasuries ){
//            Map.Entry<Room, Room> e = Random.element( candidates.entrySet() );
//            Room r = e.getKey();
//            r.type = t;
//            r.connect( e.getValue() );
//        }

		paint();

        Room n = (Room)roomExit.connected.keySet().toArray()[0];
        if ( roomExit.connected.get( n ) == null || roomExit.connected.get( n ).y == roomExit.top ) {
            return false;
        }

		paintWater();
		paintGrass();

		return true;
	}
	
	@Override
	protected void decorate() {	
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) { 
				
				map[i] = Terrain.EMPTY_DECO;
				
			} else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
				
			} else if (map[i] == Terrain.DOOR_ILLUSORY) {
				
				map[i] = Terrain.DOOR_CLOSED;
				
			}
		}
		
//		if (Imp.Quest.isCompleted()) {
//            while (true) {
//                int pos = roomEntrance.random_top();
//                if (map[pos] == Terrain.WALL) {
//                    map[pos] = Terrain.SIGN;
//                    break;
//                }
//            }
//		}
	}
	
	@Override
	protected void createMobs() {	
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = roomEntrance.random();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.BONES;
		}
	}
	
//	@Override
//	public int randomRespawnCell() {
//		return -1;
//	}

    @Override
    public String tileName( int tile ) {
        return CityLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CityLevel.tileDescs(tile);
    }

	@Override
	protected boolean[] water() {
		return Patch.generate(0.35f, 4);
	}

	@Override
	protected boolean[] grass() {
		return Patch.generate( 0.30f, 3 );
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		CityLevel.addVisuals( this, scene );
	}

    @Override
    public int nMobs() {
        return 0;
    }
}
