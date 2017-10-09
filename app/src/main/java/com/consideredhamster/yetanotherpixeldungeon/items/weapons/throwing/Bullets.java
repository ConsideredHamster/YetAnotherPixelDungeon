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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class Bullets extends ThrowingWeaponAmmo {

	{
		name = "bullets";
		image = ItemSpriteSheet.PELLET;
	}

	public Bullets() {
		this( 1 );
	}

	public Bullets(int number) {
        super( 1 );
		quantity = number;
	}

    @Override
    public int image() {
        return quantity > 1 ? ItemSpriteSheet.PELLETS : image;
//        return (isEquipped( Dungeon.hero ) || Dungeon.hero.belongings.backpack.items.contains(this) ) ? ItemSpriteSheet.PELLETS : image;
    }
	
	@Override
	public String desc() {
		return 
			"These small bullets are made of lead and are perfect ammunition for slings.";
	}
}
