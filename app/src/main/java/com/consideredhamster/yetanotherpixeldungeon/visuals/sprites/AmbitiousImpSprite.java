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
import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;

public class AmbitiousImpSprite extends MobSprite {

	public AmbitiousImpSprite() {
		super();
		
		texture( Assets.NPC_IMP );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 5, true );
		idle.frames(frames, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 4, 5, 6, 7);

        run = new Animation( 1, true );
        run.frames( frames, 0 );

        die = new Animation( 1, false );
        die.frames( frames, 0 );
		
		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );

        if( Dungeon.depth != 25 ) {
            alpha( 0.25f );
        }
	}
	
//	@Override
//	public void onComplete( Animation anim ) {
//		if (anim == die) {
//
//			emitter().burst( Speck.factory( Speck.WOOL ), 15 );
//			killAndErase();
//
//		} else {
//			super.onComplete( anim );
//		}
//	}
}
