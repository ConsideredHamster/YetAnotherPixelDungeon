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
package com.consideredhamster.yetanotherpixeldungeon.items.quest;

import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class DriedRose extends Item {
	
	{
		name = "dried rose";
		image = ItemSpriteSheet.ROSE;
		
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"The rose has dried long ago, but it has kept all its petals somehow.";
	}
}
