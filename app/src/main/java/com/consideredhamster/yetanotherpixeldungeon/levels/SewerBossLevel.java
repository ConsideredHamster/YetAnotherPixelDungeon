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

import com.watabou.noosa.Scene;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.GoldenKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.AlarmTrap;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class SewerBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;

//        viewDistance = 8;
	}
	
//	private int stairs = 0;

    private static final int TOP			= 3;
    private static final int ARENA_WIDTH    = 11;
    private static final int ARENA_HEIGHT   = 11;
    private static final int CHAMBER_HEIGHT	= 3;

    private static final int LEFT	= (WIDTH - ARENA_WIDTH) / 2;
    private static final int CENTER_X = LEFT + ARENA_WIDTH / 2;
    private static final int CENTER_Y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT / 2;
    private static final int CENTER = CENTER_Y * WIDTH + CENTER_X;

    private boolean bossAppeared = false;
    private boolean bossDefeated = false;

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

    private static final String APPEARED	= "bossAppeared";
    private static final String DEFEATED	= "bossDefeated";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( APPEARED, bossAppeared);
        bundle.put( DEFEATED, bossDefeated);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        bossAppeared = bundle.getBoolean( APPEARED );
        bossDefeated = bundle.getBoolean( DEFEATED );
    }

    @Override
    protected boolean build() {

        Painter.fill(this, LEFT, TOP, ARENA_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY);
        Painter.fill(this, LEFT, TOP + CHAMBER_HEIGHT, ARENA_WIDTH, 1, Terrain.WALL_DECO);

//        int left = pedestal( true );
//        int right = pedestal( false );
//        map[left] = map[right] = Terrain.PEDESTAL;
//        for (int i=left+1; i < right; i++) {
//            map[i] = Terrain.EMPTY_SP;
//        }

        entrance = (TOP + 1) * WIDTH + CENTER_X;
        map[entrance] = Terrain.ENTRANCE;

        exit = (TOP - 1) * WIDTH + CENTER_X;
        map[exit] = Terrain.LOCKED_EXIT;

//        int sign = ( TOP - 1 ) * WIDTH + CENTER_X + 1;
//        map[sign] = Terrain.WALL_SIGN;

        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, ARENA_WIDTH, ARENA_HEIGHT, Terrain.WATER );
        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );
        Painter.fill( this, LEFT + ARENA_WIDTH - 1, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );

        int x = LEFT;
        int y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
        while (x < LEFT + ARENA_WIDTH) {
            map[(y - ARENA_HEIGHT) * WIDTH + x] = x % 2 == 0 ? Terrain.WALL : Terrain.WALL_DECO;
            map[y * WIDTH - WIDTH + x] = x % 2 == 0 ? Terrain.STATUE_SP : Terrain.WALL;
            map[y * WIDTH + x] = Terrain.WATER;
            x++;
        }

        int door1 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT ;
        int door2 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT + ARENA_WIDTH - 1;

        map[door1] = Terrain.LOCKED_DOOR;
        map[door2] = Terrain.LOCKED_DOOR;

        Painter.fill(this, 0, TOP + CHAMBER_HEIGHT + ARENA_HEIGHT + 1, WIDTH, HEIGHT - TOP - CHAMBER_HEIGHT - ARENA_HEIGHT - 1, Terrain.CHASM);

        Painter.fill( this, CENTER_X - 1, CENTER_Y - 1, 3, 3, Terrain.EMPTY );

        Painter.fill( this, CENTER_X - 3, CENTER_Y - 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X + 3, CENTER_Y - 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X - 3, CENTER_Y + 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X + 3, CENTER_Y + 3, 1, 1, Terrain.STATUE );

        return true;
    }

    @Override
    protected void decorate() {

//        for (int i=0; i < LENGTH; i++) {
//            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
//                map[i] = Terrain.EMPTY_DECO;
//            } else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
//                map[i] = Terrain.WALL_DECO;
//            }
//        }
    }

//    public static int pedestal( boolean left ) {
//        if (left) {
//            return (TOP + ARENA_HEIGHT / 2) * WIDTH + CENTER_X - 2;
//        } else {
//            return (TOP + ARENA_HEIGHT / 2) * WIDTH + CENTER_X + 2;
//        }
//    }

    @Override
    protected void createMobs() {
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {

        drop( new IronKey(), TOP * WIDTH + CENTER_X - 1 ).type = Heap.Type.CHEST;

        drop( new SkeletonKey(), CENTER ).type = Heap.Type.LOCKED_CHEST;

        drop( new GoldenKey(), CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;

//        drop( new Gold().random(), CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;

        Item item = Bones.get();
        if (item != null) {
            drop( item, CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;
        }
    }

    @Override
    public int randomRespawnCell() {

        int cell;

        if( !bossAppeared) {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null || !beforeArena(cell) );
        } else if( !bossDefeated) {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null || !insideArena(cell) );
        } else {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null || outOfArena(cell) );
        }

        return cell;
    }

    @Override
    public void press( int cell, Char hero ) {

        super.press(cell, hero);

        if (!bossAppeared && cell == CENTER ) {

            Heap chest = Dungeon.level.heaps.get( cell );

            if( chest.type != Heap.Type.LOCKED_CHEST ) {

                AlarmTrap.trigger(cell);
                set(cell, Terrain.INACTIVE_TRAP);
                GameScene.updateMap(cell);

                bossAppeared = true;

                Mob boss = Bestiary.mob(Dungeon.depth);
                GLog.i("The chest was trapped! Security system locks the doors!");
                GLog.i("\nSomething wicked comes this way, attracted by the sound of alarm!");

                boss.pos = getRandomSpawnPoint();
                boss.state = boss.HUNTING;

                GameScene.add(boss, 2f);
                boss.beckon(cell);

                if (Dungeon.visible[boss.pos]) {
                    boss.sprite.alpha(0);
                    boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.5f));
                }

                seal();

                Dungeon.observe();
            }
        }
    }

    @Override
    public Heap drop( Item item, int cell ) {

        if (bossAppeared && !bossDefeated && item instanceof Gold) {
//        if (bossAppeared && !bossDefeated && item instanceof IronKey && ((IronKey)item).depth == Dungeon.depth ) {

            bossDefeated = true;

            unseal();

            Dungeon.observe();
        }

        return super.drop( item, cell );
    }

    private boolean insideArena(int cell) {
        return !beforeArena(cell) && !outOfArena(cell);
    }

    private boolean beforeArena(int cell) {
        return cell / WIDTH < TOP + CHAMBER_HEIGHT;
    }

    private boolean outOfArena(int cell) {
        return cell / WIDTH > TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
    }

    @Override
    public boolean noTeleport() {
        return bossAppeared && !bossDefeated;
    }

    public int getRandomSpawnPoint() {

        ArrayList<Integer> list = new ArrayList<>();

        for (int i=0; i < 5; i++) {
            int pos = (TOP + CHAMBER_HEIGHT + 1) * WIDTH + LEFT + i * 2 + 1;

            if( Actor.findChar( pos ) == null ) {
                list.add( pos );
            }
        }

        return list.size() > 0 ? (int)(Random.oneOf( list.toArray() ) ) : 0;
    }

    public void seal() {
        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.DOOR_CLOSED || map[i] == Terrain.OPEN_DOOR) {
                set(i, Terrain.LOCKED_DOOR);
                GameScene.updateMap(i);
            }
        }
	}

	public void unseal() {
        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.LOCKED_DOOR) {
                set(i, Terrain.DOOR_CLOSED);
                GameScene.updateMap(i);
            }
        }

        Dungeon.observe();
	}
	
	/*@Override
	protected boolean build() {
		
		initRooms();
	
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
			distance = roomEntrance.distance();
			
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
		
		room = (Room)roomExit.connected.keySet().toArray()[0];
		if (roomExit.top == room.bottom) {
			return false;
		}
		
		for (Room r : rooms) {
			if (r.type == Type.NULL && r.connected.size() > 0) {
				r.type = Type.TUNNEL; 
			}
		}
		
//		ArrayList<Room> candidates = new ArrayList<Room>();
//		for (Room r : roomExit.neighbours) {
//			if (!roomExit.connected.containsKey( r ) &&
//				(roomExit.left == r.right || roomExit.right == r.left || roomExit.bottom == r.top)) {
//				candidates.add( r );
//			}
//		}
//		if (candidates.size() > 0) {
//			Room kingsRoom = Random.element( candidates );
//			kingsRoom.connect( roomExit );
//			kingsRoom.type = Room.Type.RAT_KING;
//		}

		paint();
		
		paintWater();
		paintGrass();
		
//		placeTraps();
		placeSign();

		return true;
	}

	@Override
	protected boolean[] water() {
		return Patch.generate( 0.5f, 5 );
	}

	@Override
	protected boolean[] grass() {
		return Patch.generate( 0.40f, 4 );
	}
	
	@Override
	protected void decorate() {
		int start = roomExit.top * WIDTH + roomExit.left + 1;
		int end = start + roomExit.width() - 1;
		for (int i=start; i < end; i++) {
			if (i != exit) {
				map[i] = Terrain.WALL_DECO;
				map[i + WIDTH] = Terrain.WATER;
			} else {
				map[i + WIDTH] = Terrain.EMPTY;
			}
		}
	}*/
	

	
	
//	@Override
//	protected void createMobs() {
//		Mob mob = Bestiary.mob( Dungeon.depth );
//		mob.pos = roomExit.random();
//		mobs.add(mob);
//	}
	
//	public Actor respawner() {
//		return null;
//	}
	
//	@Override
//	protected void createItems() {
//		Item item = Bones.get();
//		if (item != null) {
//			int pos;
//			do {
//				pos = roomEntrance.random();
//			} while (pos == entrance || map[pos] == Terrain.SIGN);
//			drop( item, pos ).type = Heap.Type.BONES;
//		}
//	}
//
//	public void seal() {
//		if (entrance != 0) {
//
//			set( entrance, Terrain.WATER_TILES );
//			GameScene.updateMap( entrance );
//			GameScene.ripple( entrance );
//
//			stairs = entrance;
//			entrance = 0;
//		}
//	}
//
//	public void unseal() {
//		if (stairs != 0) {
//
//			entrance = stairs;
//			stairs = 0;
//
//			set( entrance, Terrain.ENTRANCE );
//			GameScene.updateMap( entrance );
//		}
//	}

//    @Override
//    public boolean noTeleport() {
//        return stairs != 0;
//    }
//
//	private static final String STAIRS	= "stairs";
//
//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//		bundle.put( STAIRS, stairs );
//	}
//
//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle(bundle);
//		stairs = bundle.getInt( STAIRS );
//	}
	
	@Override
	public String tileName( int tile ) {
		return SewerLevel.tileNames( tile );
	}

	@Override
	public String tileDesc( int tile ) {
        return SewerLevel.tileDescs(tile);
	}

    @Override
    public void addVisuals( Scene scene ) {
        SewerLevel.addVisuals( this, scene );
    }

    @Override
    public int nMobs() {
        return 0;
    }
}
