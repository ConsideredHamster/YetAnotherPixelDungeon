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

import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;

public class BannerSprites {

	public enum  Type {
        TITLE_LOGO,
        TITLE_LOGO_RUNES,
        YET_ANOTHER,
        YET_ANOTHER_RUNES,
        SELECT_YOUR_HERO,
        BOSS_SLAIN,
        GAME_OVER,
	};
	
	public static Image get( Type type ) {
		Image icon = new Image( Assets.BANNERS );
		switch (type) {
		    case TITLE_LOGO:
		    	icon.frame( icon.texture.uvRect( 0, 0, 128, 63 ) );
		    	break;
            case TITLE_LOGO_RUNES:
                icon.frame( icon.texture.uvRect( 0, 64, 128, 127 ) );
                break;
            case YET_ANOTHER:
                icon.frame( icon.texture.uvRect( 0, 128, 128, 191 ) );
                break;
            case YET_ANOTHER_RUNES:
                icon.frame( icon.texture.uvRect( 0, 192, 128, 255 ) );
                break;
            case SELECT_YOUR_HERO:
                icon.frame( icon.texture.uvRect( 0, 256, 128, 311 ) );
                break;
            case GAME_OVER:
                icon.frame( icon.texture.uvRect( 0, 312, 128, 375 ) );
                break;
            case BOSS_SLAIN:
                icon.frame( icon.texture.uvRect( 0, 376, 128, 439 ) );
			    break;
		}
		return icon;
	}
}
