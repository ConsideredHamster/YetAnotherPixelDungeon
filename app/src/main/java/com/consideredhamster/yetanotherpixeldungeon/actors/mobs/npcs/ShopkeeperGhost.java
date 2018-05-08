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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShopkeeperGhostSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class ShopkeeperGhost extends Shopkeeper {

	private static final String TXT_GREETINGS = "Welcome, wanderer.\nWould you be my guest?";
	
	{
		name = "strange shopkeeper";
		spriteClass = ShopkeeperGhostSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public String description() {
		return 
			"This guy doesn't look threatening in any way. Yet, for some reason, mere presence of this mysterious fellow " +
            "unnerves you. You wonder what business he can lead here?";
	}
}
