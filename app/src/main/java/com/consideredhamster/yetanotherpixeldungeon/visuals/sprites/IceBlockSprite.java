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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class IceBlockSprite extends MobSprite {

    protected Emitter sparkles;

	public IceBlockSprite() {
		super();
		
		texture( Assets.ICEBLOCK );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 5, true );
		idle.frames( frames, 4 );
//		idle.frames( frames, 4, 5, 6, 7 );

		run = idle.clone();
		
		attack = idle.clone();

        spawn = new Animation( 10, false );
        spawn.frames( frames, 0, 1, 3, 4 );
		
		die = new Animation( 10, false );
		die.frames( frames, 8 );

		play( idle );
	}

	@Override
    public void link( Char ch ) {
        super.link( ch );
        if ( sparkles == null ){
            sparkles = emitter();
            sparkles.pour( Speck.factory( Speck.LIGHT ), 0.5f );
        }
    }

    @Override
    public void die() {
        super.die();

        if ( sparkles != null ){
            sparkles.on = false;
            sparkles = null;
        }

        if ( Dungeon.visible[ch.pos]) {
            emitter().burst( Speck.factory( Speck.ICESHARD ), 16 );
        }
    }


    @Override
    public void update(){

        super.update();

        if (sparkles != null) {
            sparkles.visible = visible;
        }
    }

    @Override
    public int blood() {
        return 0xffffff;
    }
}
