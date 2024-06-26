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
package com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class HallsParticle extends PixelParticle {

	public static final Factory FACTORY = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((HallsParticle)emitter.recycle( HallsParticle.class )).reset( x, y );
		}
	};

	private static float angle = Random.Float( PointF.PI2 );
	private static PointF speed = new PointF().polar( angle, 5 );

	private float size;

	public HallsParticle() {
		super();
		
		lifespan = Random.Float( 1, 2 );
		scale.set( size = Random.Float( 3 ) );
        color(Random.Int(0x1000000) | 0x000080);
	}
	
	public void reset( float x, float y ) {
		revive();
		
		left = lifespan;
		
		super.speed.set( HallsParticle.speed );
		super.speed.scale( size );
		
		this.x = x - super.speed.x * lifespan / 2;
		this.y = y - super.speed.y * lifespan / 2;
		
		angle += Random.Float( -0.1f, +0.1f );
		speed = new PointF().polar( angle, 5 );
		
		am = 0;
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = left / lifespan;
		am = (p < 0.5f ? p : 1 - p) * size * 0.2f;
	}
	
	public static class Halls extends Group {
		
		private int pos;
		
		private float x;
		private float y;
		
		private float delay;
		
		public Halls( int pos ) {
			super();
			
			this.pos = pos;
			PointF p = DungeonTilemap.tileToWorld( pos );
			x = p.x;
			y = p.y;
			
			delay = Random.Float( 5 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( 5 );
					
					((HallsParticle)recycle( HallsParticle.class )).reset(
						x + Random.Float( DungeonTilemap.SIZE ), 
						y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
	}
}