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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;

public class FireDrakeSprite extends MobSprite {

	public FireDrakeSprite() {
		super();
		
		texture( Assets.DRAKE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2, 3 );
		
		run = new Animation( 15, true );
		run.frames( frames, 0, 1, 2, 3 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 4, 5, 6, 7 );
		
		die = new Animation( 6, false );
		die.frames( frames, 8, 9, 10, 11 );

        cast = attack.clone();
		
		play( idle );
	}
}
