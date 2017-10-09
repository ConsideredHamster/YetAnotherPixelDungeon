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

public class WarlockSprite extends MobSprite {

//    private int[] points = new int[2];

	public WarlockSprite() {
		super();
		
		texture( Assets.WARLOCK );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( 15, true );
		run.frames( frames, 0, 2, 3, 4 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 5, 6 );
		
//		cast = attack.clone();
		
		die = new Animation( 15, false );
		die.frames( frames, 0, 7, 8, 8, 9, 10 );
		
		play( idle );
	}
	
//	public void cast( int cell ) {
//
//		turnTo( ch.pos , cell );
//		play( cast );
//
//		MagicMissile.shadow( parent, ch.pos, cell,
//			new Callback() {
//				@Override
//				public void call() {
//					((DwarfWarlock)ch).onZapComplete();
//				}
//			} );
//		Sample.INSTANCE.play( Assets.SND_ZAP );
//	}

//	@Override
//	public void onComplete( Animation anim ) {
//		if (anim == cast) {
//			idle();
//		}
//		super.onComplete( anim );
//	}
}
