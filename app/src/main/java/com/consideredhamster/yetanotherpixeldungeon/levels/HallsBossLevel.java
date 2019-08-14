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
import com.watabou.noosa.Scene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Yog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HallsBossLevel extends Level {
	
	{
		color1 = 0x801500;
		color2 = 0xa68521;

//		viewDistance = 4;
	}
	
	private static final int ROOM_LEFT		= WIDTH / 2 - 1;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
	private static final int ROOM_TOP		= HEIGHT / 2 - 1;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 1;

    private static final int BOSS_ISHIDDEN = 0;
    private static final int BOSS_APPEARED = 1;
    private static final int BOSS_DEFEATED = 2;

    private int progress = 0;
	private int stairs = -1;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {
		
		for (int i=0; i < 5; i++) {
			
			int top = Random.IntRange( 2, ROOM_TOP - 1 );
			int bottom = Random.IntRange( ROOM_BOTTOM + 1, 22 );
			Painter.fill( this, 2 + i * 4, top, 4, bottom - top + 1, Terrain.EMPTY );
			
			if (i == 2) {
				exit = (i * 4 + 3) + (top - 1) * WIDTH ;
			}
			
			for (int j=0; j < 4; j++) {
				if (Random.Int( 2 ) == 0) {
					int y = Random.IntRange( top + 1, bottom - 1 );
					map[i*4+j + y*WIDTH] = Terrain.WALL_DECO;
				}
			}
		}
		
		map[exit] = Terrain.LOCKED_EXIT;
		
		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1, 
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP, 
			ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP + 1, Terrain.EMPTY );
		
		entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) + 
			Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * WIDTH;
		map[entrance] = Terrain.ENTRANCE;
		
		boolean[] patch = Patch.generate( 0.45f, 6 );
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}
		
		return true;
	}
	
	@Override
	protected void decorate() {

		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
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
		
		if (progress == BOSS_ISHIDDEN && hero == Dungeon.hero && cell != entrance) {

            progress = BOSS_APPEARED;
			
			for (int i=ROOM_LEFT-1; i <= ROOM_RIGHT + 1; i++) {
				doMagic( (ROOM_TOP - 1) * WIDTH + i );
				doMagic( (ROOM_BOTTOM + 1) * WIDTH + i );
			}

			for (int i=ROOM_TOP; i < ROOM_BOTTOM + 1; i++) {
				doMagic( i * WIDTH + ROOM_LEFT - 1 );
				doMagic( i * WIDTH + ROOM_RIGHT + 1 );
			}

			doMagic( entrance );
			GameScene.updateMap();

			Dungeon.observe();
			
			Yog boss = new Yog();

			do {
				boss.pos = Random.Int( LENGTH );
			} while ( !passable[boss.pos] || Dungeon.visible[boss.pos] );

			GameScene.add( boss );
			boss.spawnFists();
            boss.yell( "Greetings, mortal. Are you ready to die?" );
			
			stairs = entrance;
			entrance = -1;

            Music.INSTANCE.play( currentTrack(), true );
		}
	}
	
	private void doMagic( int cell ) {
		set( cell, Terrain.EMPTY_SP );
		CellEmitter.get( cell ).start(FlameParticle.FACTORY, 0.1f, 3);
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (progress == BOSS_APPEARED && item instanceof SkeletonKey) {

            progress = BOSS_DEFEATED;
			
			entrance = stairs;
			set( entrance, Terrain.ENTRANCE );
			GameScene.updateMap( entrance );

            Music.INSTANCE.play( currentTrack(), true );
		}
		
		return super.drop( item, cell );
	}

    private boolean outsideEntranceRoom(int cell) {
        int cx = cell % WIDTH;
        int cy = cell / WIDTH;
        return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1 || cy > ROOM_BOTTOM + 1;
    }

    @Override
    public String tileName( int tile ) {
        return HallsLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return HallsLevel.tileDescs(tile);
    }

	@Override
	public void addVisuals( Scene scene ) {
		HallsLevel.addVisuals( this, scene );
	}

    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_FINAL_LOOP : super.currentTrack();
    }

    private static final String STAIRS	    = "stairs";
    private static final String PROGRESS    = "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STAIRS, stairs );
        bundle.put( PROGRESS, progress );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        stairs = bundle.getInt( STAIRS );
        progress = bundle.getInt( PROGRESS );
    }
}
