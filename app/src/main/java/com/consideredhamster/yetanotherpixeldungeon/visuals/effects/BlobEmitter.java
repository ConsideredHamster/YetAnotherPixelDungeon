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

import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.watabou.utils.Random;

public class BlobEmitter extends Emitter {

	private static final int WIDTH	= Blob.WIDTH;
	private static final int LENGTH	= Blob.LENGTH;
	
	private Blob blob;

    private float base_interval;
	
	public BlobEmitter( Blob blob ) {
		
		super();
		
		this.blob = blob;
		blob.use( this );
	}

    @Override
    public void start( Factory factory, float interval, int quantity ) {

        base_interval = interval;

        super.start( factory, interval, quantity );
    }

    @Override
    public void update() {

        if (on) {
            interval = Game.elapsed > base_interval ? Game.elapsed : base_interval;
        }

        super.update();
    }
	
	@Override
	protected void emit( int index ) {
		
		if (blob.volume <= 0) {
			return;
		}
		
		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] > 0 && Dungeon.visible[i]) {
				float x = ( ( i % WIDTH ) + Random.Float()) * size;
				float y = ( ( i / WIDTH ) + Random.Float()) * size;
				factory.emit( this, index, x, y );
			}
		}
	}
}
