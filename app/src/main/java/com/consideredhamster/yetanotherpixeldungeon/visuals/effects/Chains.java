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
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;

public class Chains extends Group {

    private static final double A = 180 / Math.PI;

    private boolean reversed;
    private float spent;
    private float duration;

    private Image[] chains;

    private PointF from, to;

	public Chains( int from, int to, boolean reversed ) {
		
		super();

        this.reversed = reversed;

        this.from = DungeonTilemap.tileCenterToWorld( from );
        this.to = DungeonTilemap.tileCenterToWorld( to );

        float dx = this.to.x - this.from.x;
        float dy = this.to.y - this.from.y;

        float distance = (float)Math.hypot(dx, dy);
        float rotation = (float)(Math.atan2( dy, dx ) * A);

        spent = 0f;
        duration = distance / 90f + 0.0f;

        chains = new Image[ Math.round( distance / 4f ) + 1 ];
        for (int i = 0; i < chains.length; i++){
            chains[i] = new Image(Effects.get(Effects.Type.CHAIN));
            chains[i].scale.scale( 0.5f );
            chains[i].angle = rotation;
            chains[i].origin.set( chains[i].width(), chains[i].height() );
            add(chains[i]);
        }
	}
	
	@Override
	public void update() {

		super.update();

        if ((spent += Game.elapsed) > duration) {

            killAndErase();

        } else if ((spent += Game.elapsed) > duration / 2) {

            float dx = to.x - from.x;
            float dy = to.y - from.y;

            if( reversed ) {

                for (int i = 0; i < chains.length; i++) {
                    chains[i].center(new PointF(
                            to.x + ((dx * (i / (float) chains.length)) / duration * (spent - duration) * 2),
                            to.y + ((dy * (i / (float) chains.length)) / duration * (spent - duration) * 2)
                    ));
                }

            } else {

                for (int i = 0; i < chains.length; i++) {
                    chains[i].center(new PointF(
                            from.x + ((dx * (i / (float) chains.length)) / duration * (duration - spent) * 2),
                            from.y + ((dy * (i / (float) chains.length)) / duration * (duration - spent) * 2)
                    ));
                }

            }

        } else {

            float dx = to.x - from.x;
            float dy = to.y - from.y;

            for (int i = 0; i < chains.length; i++) {
                chains[i].center(new PointF(
                        from.x + ( ( dx * ( i / (float)chains.length ) ) / duration * spent * 2 ),
                        from.y + ( ( dy * ( i / (float)chains.length ) ) / duration * spent * 2 )
                ));
            }
        }
	}
}
