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
package com.consideredhamster.yetanotherpixeldungeon.visuals.effects;

import com.watabou.noosa.particles.Emitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.PointF;

public class CellEmitter {

	public static Emitter get( int cell ) {
		
		PointF p = DungeonTilemap.tileToWorld( cell );
		
		Emitter emitter = GameScene.emitter();

        if( emitter != null)
		    emitter.pos( p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE );
		
		return emitter;
	}
	
	public static Emitter center( int cell ) {
		
		PointF p = DungeonTilemap.tileToWorld( cell );
		
		Emitter emitter = GameScene.emitter();

        if( emitter != null)
		    emitter.pos( p.x + DungeonTilemap.SIZE / 2, p.y + DungeonTilemap.SIZE / 2 );
		
		return emitter;
	}

    public static Emitter bottom( int cell ) {

        PointF p = DungeonTilemap.tileToWorld( cell );

        Emitter emitter = GameScene.emitter();

        if( emitter != null)
            emitter.pos( p.x, p.y + DungeonTilemap.SIZE / 2, DungeonTilemap.SIZE, DungeonTilemap.SIZE / 2 );

        return emitter;
    }

    public static Emitter top( int cell ) {

        PointF p = DungeonTilemap.tileToWorld( cell );

        Emitter emitter = GameScene.emitter();

        if( emitter != null)
            emitter.pos( p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE / 4 );

        return emitter;
    }
}
