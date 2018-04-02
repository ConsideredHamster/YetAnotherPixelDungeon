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
package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.watabou.noosa.Image;

public class IconDifficulty extends Image {

	public IconDifficulty() {
		super();

        switch( Dungeon.difficulty ){
            case 3:
                copy( Icons.DIFF3.get() );
                break;
            case 2:
                copy( Icons.DIFF2.get() );
                break;
            case 1:
                copy( Icons.DIFF1.get() );
                break;
            default:
                copy( Icons.DIFF0.get() );
                break;
        }
		
		origin.set( width / 2, height / 2 );
	}
}
