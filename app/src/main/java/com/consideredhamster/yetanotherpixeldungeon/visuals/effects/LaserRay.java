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

import android.opengl.GLES20;

import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

import javax.microedition.khronos.opengles.GL10;

public class LaserRay extends Image {

	private static final double A = 180 / Math.PI;

	private static final float DURATION	= 0.5f;

	private float timeLeft;

	public LaserRay( int source, int target ) {

		super( Effects.get( Effects.Type.LASER ) );
		
		origin.set( 0, height / 2 );

        PointF s = DungeonTilemap.tileCenterToWorld( source );
        PointF e = DungeonTilemap.tileCenterToWorld( target );

        x = s.x - origin.x;
		y = s.y - origin.y;
		
		float dx = e.x - s.x;
		float dy = e.y - s.y;
		angle = (float)(Math.atan2( dy, dx ) * A);
		scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / width;
		
//		Sample.INSTANCE.play( Assets.SND_RAY );
		
		timeLeft = DURATION;
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / DURATION;
		alpha( p );
		scale.set( scale.x, p );
		
		if ((timeLeft -= Game.elapsed) <= 0) {
			killAndErase();
		}
	}
	
	@Override
	public void draw() {
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
		super.draw();
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}
}
