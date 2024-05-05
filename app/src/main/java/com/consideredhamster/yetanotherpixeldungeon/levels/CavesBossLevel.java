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

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class CavesBossLevel extends Level {
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

//		viewDistance = 6;
	}
	
	private static final int ROOM_LEFT		= WIDTH / 2 - 2;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 2;
	private static final int ROOM_TOP		= HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;

    private static final int BOSS_ISHIDDEN = 0;
    private static final int BOSS_APPEARED = 1;
    private static final int BOSS_DEFEATED = 2;

    private int progress = 0;
    public int arenaDoor;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	@Override
	protected boolean build() {
		
		int topMost = Integer.MAX_VALUE;
		
		for (int i=0; i < 8; i++) {
			int left, right, top, bottom;
			if (Random.Int( 2 ) == 0) {
				left = Random.Int( 1, ROOM_LEFT - 3 );
				right = ROOM_RIGHT + 3;
			} else {
				left = ROOM_LEFT - 3;
				right = Random.Int( ROOM_RIGHT + 3, WIDTH - 1 );
			}
			if (Random.Int( 2 ) == 0) {
				top = Random.Int( 2, ROOM_TOP - 3 );
				bottom = ROOM_BOTTOM + 3;
			} else {
				top = ROOM_LEFT - 3;
				bottom = Random.Int( ROOM_TOP + 3, HEIGHT - 1 );
			}
			
			Painter.fill( this, left, top, right - left + 1, bottom - top + 1, Terrain.EMPTY );
			
			if (top < topMost) {
				topMost = top;
				exit = Random.Int( left, right ) + (top - 1) * WIDTH;
			}
		}
		
		map[exit] = Terrain.LOCKED_EXIT;

		map[exit - 1] = Terrain.WALL_SIGN;
		map[exit + 1] = Terrain.WALL_SIGN;

        for( int w = 0 ; w < 10; w++ ){
            for( int h = 0 ; h < 10 ; h++ ){

                int x = w * 3 + Random.Int( 3 ) + 1;
                int y = h * 3 + Random.Int( 3 ) + 1;

                int pos = x * Level.WIDTH + y ;

                if( map[pos] == Terrain.EMPTY ){
                    map[ pos ] = Random.oneOf( Terrain.STATUE, Terrain.GRATE, Terrain.GRATE );
                }

            }
        }

		for( int i = ROOM_LEFT - 2 ; i < ROOM_RIGHT + 4 ; i = i + 2 ) {
			for (int o = ROOM_TOP - 2; o < ROOM_BOTTOM + 4; o = o + 2) {
				int pos = i * Level.WIDTH + o ;
				map[ pos ] = Terrain.INACTIVE_TRAP;
			}
		}

		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1,
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1,
				ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT, ROOM_TOP, 
			ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.TOXIC_TRAP );
		
		arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * WIDTH;
		map[arenaDoor] = Terrain.DOOR_CLOSED;

        // gotta make sure that player would not be trapped by debris
        map[arenaDoor + WIDTH] = Terrain.EMPTY_DECO;

		entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) + 
			Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * WIDTH;
		map[entrance] = Terrain.ENTRANCE;

		boolean[] water = Patch.generate( 0.45f, 5 );
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && water[i]) {
				map[i] = Terrain.WATER;
			}
		}

		boolean[] grass = Patch.generate( 0.35f, 3 );
		for (int i=WIDTH+1; i < LENGTH-WIDTH-1; i++) {
			if (map[i] == Terrain.EMPTY && grass[i]) {
				int count = 1;
				for (int n : NEIGHBOURS8) {
					if (grass[i + n]) {
						count++;
					}
				}
				map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
			}
		}
		
		return true;
	}
	
	@Override
	protected void decorate() {

        for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0 ) {
				map[i] = Terrain.EMPTY_DECO;
            }
        }

        for (int i=0; i < LENGTH ; i++) {
            if( map[i] == Terrain.WALL && Random.Int( 10 ) == 0 ) {

                map[i] = Random.oneOf(
                        Terrain.WALL_DECO, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }
	}
	
	@Override
	protected void createMobs() {	
	}

    @Override
    public int nMobs() {
        return 0;
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
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * WIDTH;
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.BONES;
		}
	}

    @Override
    public ArrayList<Integer> getPassableCellsList() {

        ArrayList<Integer> result = new ArrayList<>();

        for( Integer cell : super.getPassableCellsList() ){
            if( progress != BOSS_ISHIDDEN && outsideEntranceRoom( cell ) || progress != BOSS_APPEARED && !outsideEntranceRoom( cell ) ){
                result.add( cell );
            }
        }

        return result;
    }
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if ( progress == BOSS_ISHIDDEN && outsideEntranceRoom(cell) && hero == Dungeon.hero) {

            progress = BOSS_APPEARED;
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.SLEEPING;

            boss.pos = exit + WIDTH * 2;
			GameScene.add( boss );

            set( boss.pos, Terrain.EMPTY_DECO );
            GameScene.updateMap( boss.pos );
			
			set( arenaDoor, Terrain.GRATE );
			GameScene.updateMap( arenaDoor );
			
			CellEmitter.get( arenaDoor ).start(Speck.factory(Speck.ROCK), 0.07f, 10 );
			Camera.main.shake(3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
            Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
	}

    private boolean outsideEntranceRoom(int cell) {
        int cx = cell % WIDTH;
        int cy = cell / WIDTH;
        return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1 || cy > ROOM_BOTTOM + 1;
    }
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if ( progress == BOSS_APPEARED && item instanceof SkeletonKey) {

            progress = BOSS_DEFEATED;
			
			CellEmitter.get( arenaDoor ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
			
			set(arenaDoor, Terrain.EMPTY_DECO);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
		
		return super.drop( item, cell );
	}

	protected boolean[] water() {
		return Patch.generate( 0.45f, 6 );
	}

	protected boolean[] grass() {
		return Patch.generate( 0.35f, 3 );
	}

    @Override
    public String tileName( int tile ) {
        return CavesLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CavesLevel.tileDescs(tile);
    }
	
	@Override
	public void addVisuals( Scene scene ) {
        super.addVisuals( scene );
		CavesLevel.addVisuals( this, scene );
	}

    @Override
    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

    private static final String DOOR	    = "door";
    private static final String PROGRESS	= "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( DOOR, arenaDoor );
        bundle.put( PROGRESS, progress );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        arenaDoor = bundle.getInt( DOOR );
        progress = bundle.getInt( PROGRESS );
    }
}
