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

public class Knives extends ThrowingWeaponLight {

	{
		name = "throwing knives";
		image = ItemSpriteSheet.THROWING_KNIFE;
	}
	
	public Knives() {
		this( 1 );
	}
	
	public Knives(int number) {
        super( 1 );
		quantity = number;
	}

    @Override
    public boolean canBackstab() {
        return true;
    }
	
	@Override
	public String desc() {
		return 
			"These simple metal blades are weighted to fly true and sting their prey " +
            "with a flick of the wrist, dealing increased damage on sneak attacks.";
	}
	

}
