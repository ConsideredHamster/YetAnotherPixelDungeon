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

import java.util.Arrays;

import com.watabou.noosa.Scene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Amulet;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class LastLevel extends Level {

	private static final int SIZE = 9;
	
	{
//        viewDistance = 8;
		color1 = 0x801500;
		color2 = 0xa68521;
	}
	
	private int pedestal;
	
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

		Arrays.fill( map, Terrain.WALL );
		Painter.fill( this, 1, 1, SIZE, SIZE, Terrain.WATER );
		Painter.fill( this, 2, 2, SIZE-2, SIZE-2, Terrain.EMPTY );
//		Painter.fill( this, SIZE / 2, SIZE/2, 3, 3, Terrain.EMPTY_SP );
		
		entrance = SIZE * WIDTH + SIZE / 2 + 1;
		map[entrance] = Terrain.ENTRANCE;

        int deco = entrance - WIDTH * SIZE;

        for( int i = deco - 4 ; i <= deco + 4 ; i += 2 ) {
            map[i] = Terrain.WALL_DECO;
        }

//		exit = entrance - WIDTH * SIZE;
//		map[exit] = Terrain.LOCKED_EXIT;



        Painter.fill( this, SIZE / 2 - 1, SIZE / 2 + 1, 5, 1, Terrain.EMPTY_SP );
        Painter.fill( this, SIZE / 2 + 1, SIZE / 2 - 1, 1, 5, Terrain.EMPTY_SP );

        Painter.fill( this, SIZE / 2 - 1, SIZE / 2, 1, 3, Terrain.EMPTY_SP );
        Painter.fill( this, SIZE / 2 + 3, SIZE / 2, 1, 3, Terrain.EMPTY_SP );

        Painter.fill( this, SIZE / 2, SIZE / 2 - 1, 3, 1, Terrain.EMPTY_SP );
        Painter.fill( this, SIZE / 2, SIZE / 2 + 3, 3, 1, Terrain.EMPTY_SP );

        pedestal = (SIZE / 2 + 1) * (WIDTH + 1);
        map[pedestal] = Terrain.PEDESTAL;
        map[pedestal+WIDTH+1] = map[pedestal+WIDTH-1] = map[pedestal-WIDTH+1] = map[pedestal-WIDTH-1] = Terrain.STATUE;

        map[ pedestal + WIDTH * 3 + 3] =
        map[ pedestal + WIDTH * 3 - 3] =
        map[ pedestal - WIDTH * 3 + 3] =
        map[ pedestal - WIDTH * 3 - 3] = Terrain.WATER;

		feeling = Feeling.NONE;
		
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
	protected void createItems() {
		drop( new Amulet(), pedestal );
	}
	
//	@Override
//	public int randomRespawnCell() {
//		return -1;
//	}

    public int nMobs() {
        return 0;
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
}
