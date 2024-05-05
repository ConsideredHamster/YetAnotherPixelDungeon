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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.utils.Callback;

public class TenguSprite extends MobSprite {
	
	public TenguSprite() {
		super();
		
		texture( Assets.TENGU );
		
		TextureFilm frames = new TextureFilm( texture, 14, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 15, false );
		run.frames( frames, 2, 3, 4, 5, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 6, 7, 7, 0 );
		
		die = new Animation( 8, false );
		die.frames( frames, 8, 9, 10, 10, 10, 10, 10, 10 );
		
		play( run.clone() );
	}
	
	@Override
	public void move( int from, int to ) {
		
		place( to );
		
		play( run );
		turnTo( from , to );
		
		isMoving = true;
		
		ch.onMotionComplete();
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == run) {
			isMoving = false;
			idle();
		} else {
			super.onComplete( anim );
		}
	}
}
