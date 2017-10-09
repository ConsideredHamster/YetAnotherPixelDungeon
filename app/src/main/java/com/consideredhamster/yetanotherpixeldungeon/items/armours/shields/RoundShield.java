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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.shields;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;


public class RoundShield extends Shield {

	{
		name = "round shield";
		image = ItemSpriteSheet.SHIELD_ROUND;
	}

	public RoundShield() { super( 1 ); }
	
	@Override
	public String desc() {
		return "Simple yet very sturdily made shield.";
	}
}
