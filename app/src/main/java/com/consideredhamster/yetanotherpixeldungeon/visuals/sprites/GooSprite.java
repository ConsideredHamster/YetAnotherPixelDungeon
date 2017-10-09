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
package com.consideredhamster.yetanotherpixeldungeon.visuals.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.particles.Emitter.Factory;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Goo;

public class GooSprite extends MobSprite {
	
//	private Animation pump;
//	private Animation jump;

    private static final int NORMAL_FRAMERATE	= 10;
    private static final int ENRAGE_FRAMERATE	= 20;

	private Emitter spray;
	
	public GooSprite() {
		super();
		
		texture( Assets.GOO );
		
		TextureFilm frames = new TextureFilm( texture, 20, 16 );
		
		idle = new Animation( NORMAL_FRAMERATE, true );
		idle.frames( frames, 10, 11, 11, 12, 11, 11 );
		
		run = new Animation( NORMAL_FRAMERATE, true );
		run.frames( frames, 10, 11, 12, 11 );
		
//		pump = new Animation( NORMAL_FRAMERATE, true );
//		pump.frames( frames, 0, 1 );
		
//		jump = new Animation( 1, true );
//		jump.frames( frames, 6 );
		
		attack = new Animation( NORMAL_FRAMERATE, false );
		attack.frames( frames, 13, 14, 15 );
		
		die = new Animation( NORMAL_FRAMERATE, false );
		die.frames( frames, 16, 17, 18, 19 );

		play( idle );
	}

	@Override
	public void play( Animation anim, boolean force ) {
		super.play(anim, force);

		if (ch instanceof Goo && ((Goo)ch).phase && anim != die) {

            if (spray == null) {
                spray = emitter();
                spray.pour( GooParticle.FACTORY, 0.05f );
            }

            anim.delay = 1f / ENRAGE_FRAMERATE;

		} else {

            if (spray != null) {
                spray.on = false;
                spray = null;
            }

            anim.delay = 1f / NORMAL_FRAMERATE;
        }
	}

    @Override
    public void update() {

        super.update();

        if (spray != null) {
            spray.visible = visible;
        }
    }
	
	@Override
	public int blood() {
		return 0xFF000000;
	}
	
	public static class SpawnSprite extends GooSprite {

        public SpawnSprite() {
            super();
            texture( Assets.GOO );

            TextureFilm frames = new TextureFilm( texture, 20, 16 );

            idle = new Animation( NORMAL_FRAMERATE, true );
            idle.frames( frames, 0, 1, 1, 2, 1, 1 );

            run = new Animation( NORMAL_FRAMERATE, true );
            run.frames( frames, 0, 1, 2, 1 );

//		pump = new Animation( NORMAL_FRAMERATE, true );
//		pump.frames( frames, 0, 1 );

//		jump = new Animation( 1, true );
//		jump.frames( frames, 6 );

            attack = new Animation( NORMAL_FRAMERATE, false );
            attack.frames( frames, 3, 4, 5 );

            die = new Animation( NORMAL_FRAMERATE, false );
            die.frames( frames, 6, 7, 8, 9 );

            play( idle );
        }
    }

	public static class GooParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((GooParticle)emitter.recycle( GooParticle.class )).reset( x, y );
			}
		};
		
		public GooParticle() {
			super();
			
			color( 0x000000 );
			lifespan = 0.5f;
			
			acc.set( 0, +50 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			size = 4;
			speed.polar( -Random.Float( PointF.PI ), Random.Float( 12, 16 ) );
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.5f ? (1 - p) * 2f : 1;
		}
	}
}
