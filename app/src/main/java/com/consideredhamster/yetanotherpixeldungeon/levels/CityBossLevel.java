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
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarvenKing;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class CityBossLevel extends Level {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}


	
	private static final int TOP			= 2;
	private static final int HALL_WIDTH		= 11;
	private static final int HALL_HEIGHT	= 17;
	private static final int CHAMBER_HEIGHT	= 3;
	
	private static final int LEFT	= (WIDTH - HALL_WIDTH) / 2;
	private static final int CENTER	= LEFT + HALL_WIDTH / 2;

    private static final int THRONE	    = ( TOP + 2 ) * WIDTH + CENTER;
    private static final int PEDESTAL   = (TOP + HALL_HEIGHT / 2 ) * WIDTH + CENTER;
    private static final int FOUNTAIN	= (TOP + HALL_HEIGHT - 3 ) * WIDTH + CENTER;

	private static final int[] WELLS =
    {
        THRONE + WIDTH * 3 + 3,
        THRONE + WIDTH * 3 - 3,

        THRONE + WIDTH * 6 + 4,
        THRONE + WIDTH * 6 - 4,

        THRONE + WIDTH * 9 + 3,
        THRONE + WIDTH * 9 - 3,
    };

    private static final int BOSS_ISHIDDEN = 0;
    private static final int BOSS_APPEARED = 1;
    private static final int BOSS_DEFEATED = 2;

    private int progress = 0;
	private int arenaDoor;
	
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
		
		Painter.fill( this, LEFT, TOP, HALL_WIDTH, HALL_HEIGHT, Terrain.EMPTY );

        Painter.fill( this, LEFT, TOP, HALL_WIDTH, 1, Terrain.EMBERS);
        Painter.fill( this, LEFT, TOP + HALL_HEIGHT - 1, HALL_WIDTH, 1, Terrain.BOOKSHELF );

		Painter.fill( this, CENTER, TOP + 2, 1, HALL_HEIGHT - 2, Terrain.EMPTY_SP );
		Painter.fill( this, CENTER - 1, TOP + 1, 3, 3, Terrain.EMPTY_SP );

        map[THRONE] = Terrain.PEDESTAL;
        map[THRONE - WIDTH] = Terrain.PEDESTAL;

        map[THRONE - WIDTH + 2] = Terrain.STATUE_SP;
        map[THRONE - WIDTH - 2] = Terrain.STATUE_SP;

        Painter.fill( this, THRONE % WIDTH - 2, THRONE / WIDTH, 1, 2, Terrain.EMBERS );
        Painter.fill( this, THRONE % WIDTH + 2, THRONE / WIDTH, 1, 2, Terrain.EMBERS );

        int y = TOP + 2;
		while (y < TOP + HALL_HEIGHT) {
			map[y * WIDTH + CENTER - 4] = Terrain.STATUE;
			map[y * WIDTH + CENTER + 4] = Terrain.STATUE;
			y += 3;
		}

        for( int altar : WELLS) {
            map[ altar ] = Terrain.EMPTY_WELL;
        }

        Painter.fill( this, CENTER - 1, TOP + HALL_HEIGHT - 4, 3, 3, Terrain.EMPTY_SP );
        Painter.fill( this, CENTER - 1, TOP + HALL_HEIGHT / 2 - 1, 3, 3, Terrain.EMPTY_SP );

        map[ PEDESTAL]  = Terrain.PEDESTAL;
        map[ FOUNTAIN ] = Terrain.WATER;

        map[FOUNTAIN + WIDTH + 2] = Terrain.STATUE_SP;
        map[FOUNTAIN + WIDTH - 2] = Terrain.STATUE_SP;

        Painter.fill( this, FOUNTAIN % WIDTH - 2, FOUNTAIN / WIDTH - 1, 1, 2, Terrain.HIGH_GRASS );
        Painter.fill( this, FOUNTAIN % WIDTH + 2, FOUNTAIN / WIDTH - 1, 1, 2, Terrain.HIGH_GRASS );

		Painter.fill( this, LEFT, TOP + HALL_HEIGHT + 1, HALL_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY );
		Painter.fill( this, LEFT, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF );
		Painter.fill( this, LEFT + HALL_WIDTH - 1, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF );

        Painter.fill( this, CENTER - 1, TOP + HALL_HEIGHT + 1, 3, 3, Terrain.EMPTY_SP );
        Painter.fill( this, LEFT + 1, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.EMPTY_SP );
        Painter.fill( this, LEFT + HALL_WIDTH - 2, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.EMPTY_SP );

        arenaDoor = (TOP + HALL_HEIGHT) * WIDTH + CENTER;
        map[arenaDoor] = Terrain.DOOR_CLOSED;
		
		entrance = (TOP + HALL_HEIGHT + CHAMBER_HEIGHT / 2 + 1 ) * WIDTH + LEFT + HALL_WIDTH / 2 ;
		map[entrance] = Terrain.ENTRANCE;

        exit = (TOP - 1) * WIDTH + CENTER;
        map[exit] = Terrain.LOCKED_EXIT;

        map[arenaDoor - 1] = Terrain.WALL_SIGN;
        map[arenaDoor + 1] = Terrain.WALL_SIGN;

        map[arenaDoor - 3] = Terrain.WALL_DECO2;
        map[arenaDoor + 3] = Terrain.WALL_DECO2;

//        WellWater water = (WellWater)blobs.get( WaterOfHealth.class );
//        if (water == null) {
//            try {
//                water = new WaterOfHealth();
//                water.seed( FOUNTAIN, 9 );
//            } catch (Exception e) {
//                water = null;
//            }
//        }

//        blobs.put( WaterOfHealth.class, water );
		
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
	}
	
	public static int pedestal() {
        return PEDESTAL;
	}

    public static int[] wells() {
        return WELLS;
    }

    public static int throne( boolean well ) {
        return THRONE - WIDTH + ( well ? 1 : -1 );
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
				pos = 
					Random.IntRange( LEFT + 1, LEFT + HALL_WIDTH - 2 ) + 
					Random.IntRange( TOP + HALL_HEIGHT + 1, TOP + HALL_HEIGHT  + CHAMBER_HEIGHT ) * WIDTH;
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
		
		super.press(cell, hero);

		if ( progress == BOSS_ISHIDDEN && outsideEntranceRoom(cell) && hero == Dungeon.hero ) {

            progress = BOSS_APPEARED;
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.HUNTING;
            boss.pos = THRONE;

            GameScene.add( boss );
			
			if (Dungeon.visible[boss.pos]) {
				boss.notice();
				boss.sprite.alpha( 0 );
				boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
			}

			set( arenaDoor, Terrain.LOCKED_DOOR );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if ( progress == BOSS_APPEARED && item instanceof SkeletonKey ) {

            progress = BOSS_DEFEATED;
			
			set( arenaDoor, Terrain.DOOR_CLOSED);
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
		
		return super.drop( item, cell );
	}
	
	private boolean outsideEntranceRoom(int cell) {
		return cell / WIDTH < arenaDoor / WIDTH;
	}

    @Override
    public String tileName( int tile ) {
        return CityLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CityLevel.tileDescs(tile);
    }

    @Override
    public void addVisuals( Scene scene ) {
        super.addVisuals( scene );
        CityLevel.addVisuals(this, scene);
    }

    @Override
    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

    private static final String DOOR	    = "door";
    private static final String PROGRESS    = "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
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
