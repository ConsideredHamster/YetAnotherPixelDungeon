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

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Lightning extends Group {

    // How long the effect lasts
	private static final float DURATION = 0.4f;

    // How much segments will arc
	private static final float BENDING = 1.0f;

    // we will need this one later
    private static final double A = 180 / Math.PI;

	private float life;
	private Callback callback;
    private Segment[] segments1;
    private Segment[] segments2;

    // Yes, this version of lightning effect does not supports chaining.
    // I considered coding the forking logic in this class, but decided to keep it simple.

	public Lightning( int source, int target, Callback callback ) {

        super();

        life = DURATION;
        this.callback = callback;

        int cx1 = source % Level.WIDTH;
        int cy1 = source / Level.WIDTH;

        int cx2 = target % Level.WIDTH;
        int cy2 = target / Level.WIDTH;

        // We still need to get exact points between which we would draw our segments
        PointF sourceP = new PointF( ( cx1 + 0.5f ) * DungeonTilemap.SIZE, ( cy1 + 0.5f ) * DungeonTilemap.SIZE );
        PointF targetP = new PointF( ( cx2 + 0.5f ) * DungeonTilemap.SIZE, ( cy2 + 0.5f ) * DungeonTilemap.SIZE );

        // Amount of segments now depends on the distance between source and target cells
        int length = Math.max( Math.abs( cx1 - cx2 ), Math.abs( cy1 - cy2 ) ) + 1;
        segments1 = new Segment[ length ];
        segments2 = new Segment[ length ];

        // Segment are built between equidistant points and then added to the scene
        for( int number = 0 ; number < length ; number++ ) {

            float sourceX = sourceP.x + ( targetP.x - sourceP.x ) * number / length;
            float sourceY = sourceP.y + ( targetP.y - sourceP.y ) * number / length;

            float targetX = sourceP.x + ( targetP.x - sourceP.x ) * ( number + 1 ) / length;
            float targetY = sourceP.y + ( targetP.y - sourceP.y ) * ( number + 1 ) / length;

            Segment segment1 = new Segment( sourceX, sourceY, targetX, targetY );
            segments1[ number ] = segment1;
            add( segment1 );

            Segment segment2 = new Segment( sourceX, sourceY, targetX, targetY );
            segments2[ number ] = segment2;
            add( segment2 );
        }

        // Play the corresponding sound
        Sample.INSTANCE.play( Assets.SND_LIGHTNING );

        update();
    }

    public Lightning( int source, int target ) {
        this( source, target, null );
    }
	
	@Override
	public void update() {

		super.update();

		if ((life -= Game.elapsed) < 0) {

            // If timer has run out, snuff out the effect and call the callback

			killAndErase();

            if( callback != null ) {
                callback.call();
            }

		} else {

            // Otherwise we are gonna walk through each segment and adjust it
            for( int i = 0 ; i < segments1.length ; i++ ) {

                Segment segment1 = segments1[i];
                Segment segment2 = segments2[i];

                // If this is not the first segment, then set its starting point to the target
                // point of the previous segment. There always should be more than one segment.
                if( i > 0 ){
                    segment1.x = segments1[ i - 1 ].target.x;
                    segment1.y = segments1[ i - 1 ].target.y;

                    segment2.x = segments2[ i - 1 ].target.x;
                    segment2.y = segments2[ i - 1 ].target.y;
                }

                // If this is not the last segment, then modify its target point by a random value.
                // This effect stacks, so longer duration can lead to some funny looking arcs
                if( i < segments1.length - 1 ){
                    segment1.target.x += Random.Float( -BENDING, +BENDING );
                    segment1.target.y += Random.Float( -BENDING, +BENDING );

                    segment2.target.x += Random.Float( -BENDING, +BENDING );
                    segment2.target.y += Random.Float( -BENDING, +BENDING );
                }

                // Calculate delta value between those starting and target points
                float dx1 = segment1.target.x - segment1.x;
                float dy1 = segment1.target.y - segment1.y;

                float dx2 = segment2.target.x - segment2.x;
                float dy2 = segment2.target.y - segment2.y;

                // Adjust angle and length of our segment depending on delta value
                segment1.angle = (float)(Math.atan2( dy1, dx1 ) * A);
                segment1.scale.x = (float)Math.sqrt( dx1 * dx1 + dy1 * dy1 ) / segment1.width;

                segment2.angle = (float)(Math.atan2( dy2, dx2 ) * A);
                segment2.scale.x = (float)Math.sqrt( dx2 * dx2 + dy2 * dy2 ) / segment2.width;

                // Adjust transparency so the effect would slowly fade out
                segment1.am = life / DURATION;
                segment2.am = segment1.am * 1.0f;

                // Sometimes flip the segment vertically to make our lightning look more zappy
                if( Random.Int( 5 ) == 0 ){
                    segment1.scale.y *= -1;
                }

                if( Random.Int( 5 ) == 0 ){
                    segment2.scale.y *= -1;
                }

			}
		}
	}

    // Do some stuff I don't really understand.
	@Override
	public void draw() {
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
		super.draw();
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}

	// Here are our pet Segment class which was made just for the sake of some convenience.
    private static class Segment extends Image {

        public PointF target;

        private Segment( float sourceX, float sourceY, float targetX, float targetY ) {

            super( Effects.get( Effects.Type.LIGHTNING ) );

            target = new PointF( targetX, targetY );

            origin.set( 0, height / 2 );

            x = sourceX;
            y = sourceY;

        }
    }
}
