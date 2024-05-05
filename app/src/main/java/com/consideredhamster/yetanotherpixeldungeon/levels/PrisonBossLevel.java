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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndInfoItem;
import com.watabou.noosa.Scene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room.Type;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class PrisonBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

//    private static final int BOSS_ISHIDDEN = 0;
//    private static final int BOSS_APPEARED = 1;
//    private static final int BOSS_DEFEATED = 2;

//    private int progress = 0;
//    private Room anteroom;
//    private int arenaDoor;

	private static final int ARENA_WIDTH    = 13;
	private static final int ARENA_HEIGHT   = 13;
	private static final int CHAMBER_SIZE	= 5;

	private static final int TOP	= (HEIGHT - ARENA_HEIGHT) / 2;
	private static final int LEFT	= (WIDTH - ARENA_WIDTH) / 2;
	private static final int RIGHT	= LEFT + ARENA_WIDTH;
	private static final int BOTTOM	= TOP + ARENA_HEIGHT;

	private static final int CENTER_X = LEFT + ARENA_WIDTH / 2;
	private static final int CENTER_Y = TOP + ARENA_HEIGHT / 2;
	private static final int CENTER = CENTER_Y * WIDTH + CENTER_X;

	private static final int ENTRANCE = CENTER - ( ARENA_HEIGHT + CHAMBER_SIZE ) / 2 - 1 ;
	private static final int EXIT = ENTRANCE + ARENA_HEIGHT + CHAMBER_SIZE + 2; ;


	private static final int DOOR1 = CENTER_Y * WIDTH + LEFT - 1 ;
	private static final int DOOR2 = CENTER_Y * WIDTH + RIGHT;

	private int progress = 0;

	private static final int BOSS_ISHIDDEN = 0;
	private static final int BOSS_APPEARED = 1;
	private static final int BOSS_DEFEATED = 2;
	
	@Override
	protected boolean build() {

		// entrance room

		Painter.fill(this, LEFT - 1, CENTER_Y - CHAMBER_SIZE / 2, 1, 5, Terrain.GRATE );
		Painter.fill(this, LEFT - CHAMBER_SIZE - 1, TOP + ( ARENA_HEIGHT - CHAMBER_SIZE ) / 2,
				CHAMBER_SIZE, CHAMBER_SIZE, Terrain.EMPTY );

		entrance = ENTRANCE;
		map[entrance] = Terrain.ENTRANCE;

		int sign = entrance - WIDTH * 3;
		map[sign] = Terrain.WALL_SIGN;

		// main room

		Painter.fill(this, LEFT, TOP, ARENA_WIDTH, ARENA_HEIGHT, Terrain.HIGH_GRASS);
		Painter.fill(this, LEFT, CENTER_Y - 1 , 1, 3, Terrain.GRASS );
		Painter.fill(this, RIGHT - 1, CENTER_Y - 1 , 1, 3, Terrain.GRASS );

		for (int i=0; i < LENGTH; i++) {

			if( Math.sqrt( Math.pow( CENTER_X - i % WIDTH, 2 ) + Math.pow( CENTER_Y - i / WIDTH, 2 ) ) < 6 ) {
				map[i] = Terrain.WATER;
			}
		}

		Painter.fill(this, LEFT, CENTER_Y, ARENA_WIDTH, 1, Terrain.EMPTY );
		Painter.fill(this, CENTER_X - 1, CENTER_Y - 2, 3, 5, Terrain.EMPTY );
		Painter.fill(this, CENTER_X - 2, CENTER_Y - 1, 5, 3, Terrain.EMPTY );

		map[ CENTER - WIDTH * 4 ] = Terrain.WALL_DECO;
		map[ CENTER + WIDTH * 4 ] = Terrain.WALL_DECO;

		map[ CENTER - WIDTH * 2 - 4 ] = Terrain.WALL_DECO;
		map[ CENTER - WIDTH * 2 + 4 ] = Terrain.WALL_DECO;

		map[ CENTER + WIDTH * 2 - 4 ] = Terrain.WALL_DECO;
		map[ CENTER + WIDTH * 2 + 4 ] = Terrain.WALL_DECO;

		map[ CENTER - WIDTH * 2 - 1 ] = Terrain.STATUE;
		map[ CENTER - WIDTH * 2 + 1 ] = Terrain.STATUE;

		map[ CENTER + WIDTH * 2 - 1 ] = Terrain.STATUE;
		map[ CENTER + WIDTH * 2 + 1 ] = Terrain.STATUE;

		map[ CENTER ] = Terrain.PEDESTAL;

		// exit room

		exit = EXIT;
		
		Painter.fill(this, RIGHT, CENTER_Y - CHAMBER_SIZE / 2, 1, 5, Terrain.GRATE );
		Painter.fill(this, RIGHT + 1, TOP + ( ARENA_HEIGHT - CHAMBER_SIZE ) / 2,
				CHAMBER_SIZE, CHAMBER_SIZE, Terrain.EMPTY );

		Painter.fill(this, exit % WIDTH - 1, exit / WIDTH - 1, 3, 2, Terrain.WALL );

		map[ exit ] = Terrain.LOCKED_EXIT;
		map[ exit + 1 ] = Terrain.WALL_DECO;
		map[ exit - 1 ] = Terrain.WALL_DECO;

		map[ DOOR1 ] = Terrain.LOCKED_DOOR;
		map[ DOOR2 ] = Terrain.LOCKED_DOOR;

		return true;
	}
	
	@Override
	protected void decorate() {

        for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map[i] == Terrain.EMPTY) {
                if (Random.Int( 10 ) == 0 ) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < LENGTH ; i++) {
            if(
                map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                        Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }

		for ( int i=0; i < ARENA_WIDTH; i = i + 2 ) {
			map[ ( TOP - 1 ) * WIDTH + LEFT + i] = Terrain.WALL_DECO2;
			map[ BOTTOM * WIDTH + LEFT + i] = Terrain.WALL_DECO2;
		}

		ArrayList<Integer> cells = new ArrayList<>();

		for( int cell = 0 ; cell < Level.LENGTH ; cell++ ){
			if( ( map[ cell ] == Terrain.EMPTY || map[ cell ] == Terrain.WATER
				|| map[ cell ] == Terrain.HIGH_GRASS ) && insideArena( cell )
				&& !adjacent( cell, DOOR1 ) && !adjacent( cell, DOOR2 ) && !adjacent( cell, CENTER )
			){
				cells.add( cell );
			}
		}

		if( !cells.isEmpty() ) {
			for ( int i = 0; i < 8; i++ ) {

				int cell = Random.element( cells );

				if ( map[ cell ] == Terrain.EMPTY ) {
					map[ cell ] = Random.oneOf( Terrain.GRASS, Terrain.HIGH_GRASS, Terrain.BARRICADE );
				} else {
					map[ cell ] = Terrain.BARRICADE;
				}
			}
		}
	}
	
	@Override
	protected void createItems() {

		drop( new IronKey(), ENTRANCE - 1 ).type = Heap.Type.CHEST;

		Item item = Bones.get();
		if (item != null) {
			drop( item, entrance + 1 ).type = Heap.Type.BONES;
		}
	}

	@Override
	public ArrayList<Integer> getPassableCellsList() {

		ArrayList<Integer> result = new ArrayList<>();

		for( Integer cell : super.getPassableCellsList() ){
			if( progress != BOSS_ISHIDDEN && insideArena( cell ) || progress != BOSS_APPEARED && beforeArena( cell ) ){
				result.add( cell );
			}
		}

		return result;
	}

	private boolean insideArena(int cell) {
		return !beforeArena(cell) && !outOfArena(cell);
	}

	private boolean beforeArena(int cell) {
		return cell % WIDTH < LEFT;
	}

	private boolean outOfArena(int cell) {
		return cell % WIDTH >= RIGHT;
	}

	@Override
	public void press( int cell, Char ch ) {

		super.press( cell, ch );

		if (ch == Dungeon.hero && progress == BOSS_ISHIDDEN && cell % WIDTH > CENTER_X - 4 ) {

            progress = BOSS_APPEARED;

			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.HUNTING;
			boss.pos = CENTER;
			GameScene.add( boss, 2f );

			boss.sprite.turnTo( boss.pos, Dungeon.hero.pos );
			boss.beckon( Dungeon.hero.pos );
			boss.notice();

			press( boss.pos, boss );

			seal();
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
	}

	@Override
	public Heap drop( Item item, int cell ) {

		// FIXME

		if ( progress == BOSS_APPEARED && item instanceof Gold) {

			progress = BOSS_DEFEATED;

			Music.INSTANCE.play( currentTrack(), true );

			unseal();

			Dungeon.observe();
		}

		return super.drop( item, cell );
	}

	public void seal() {

		set( DOOR1, Terrain.LOCKED_DOOR );
		GameScene.updateMap( DOOR1 );

		set( DOOR2, Terrain.LOCKED_DOOR );
		GameScene.updateMap( DOOR2 );

	}

	public void unseal() {

		set( DOOR1, Terrain.DOOR_CLOSED );
		GameScene.updateMap( DOOR1 );

		set( DOOR2, Terrain.DOOR_CLOSED );
		GameScene.updateMap( DOOR2 );

		Dungeon.observe();
	}

	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

    @Override
    public int nMobs() {
        return 0;
    }

    @Override
    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

	@Override
	public String tileName( int tile ) {
		return PrisonLevel.tileNames( tile );
	}

	@Override
	public String tileDesc( int tile ) {
		return PrisonLevel.tileDescs(tile);
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}

	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		PrisonLevel.addVisuals( this, scene );
	}

    private static final String PROGRESS    = "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PROGRESS, progress );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        progress = bundle.getInt( PROGRESS );
    }
}
