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

import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;

public class BlackguardSprite extends MobSprite {

	public BlackguardSprite() {
		super();
		
		texture( Assets.GUARD );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 0, 0, 2 );
		
		run = new Animation( 15, true );
		run.frames( frames, 3, 4, 5, 6, 7, 8 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 9, 10, 11 );
		
		die = new Animation( 5, false );
		die.frames( frames, 12, 13, 12, 14, 12, 13, 12, 14, 16, 17 );
		
		cast = new Animation( 12, false );
		cast.frames( frames, 9, 11, 10 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0x8800ff00;
	}
	
	@Override
	public void onComplete( Animation anim ) {
		
		super.onComplete(anim);
		
		if (anim == die) {
			Camera.main.shake( 1, 0.1f );
			Sample.INSTANCE.play( Assets.SND_ROCKS, 0.5f, 0.5f, 1.2f );
		}
	}
}
