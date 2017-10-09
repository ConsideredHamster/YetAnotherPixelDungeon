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

import com.watabou.noosa.Game;
import com.watabou.utils.PointF;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;

import javax.microedition.khronos.opengles.GL10;

public class UnholyArmor extends Halo {

    private float phase;

    private CharSprite target;

    public UnholyArmor(CharSprite sprite ) {

        super( 18, 0xFF0000, 0.1f );

        am = -1;
        aa = +1;

        phase = 1;

        target = sprite;
    }

    @Override
    public void update() {
        super.update();

        if (phase < 1) {
            if ((phase -= Game.elapsed) <= 0) {
                killAndErase();
            } else {
                scale.set( (2 - phase) * radius / RADIUS );
                am = phase * (-1);
                aa = phase * (+1);
            }
        }

        if (visible = target.visible) {
            PointF p = target.center();
            point(p.x, p.y );
        }
    }

    @Override
    public void draw() {
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        super.draw();
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
    }

    public void putOut() {
        phase = 0.999f;
    }
}
