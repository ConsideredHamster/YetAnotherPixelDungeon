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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged;

import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class Handcannon extends RangedWeaponFlintlock {

	{
		name = "handcannon";
		image = ItemSpriteSheet.HANDCANNON;
	}

	public Handcannon() {
		super( 3 );
	}

    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Bullets.class;
    }

	@Override
	public Type weaponType() {
		return Type.R_FLINTLOCK;
	}
	
	@Override
	public String desc() {
		return "While initially flintlock weapons were a human invention, dwarves quickly adapted " +
                "and improved the concept. Handcannons are heavy and require lots of gunpowder to " +
                "use, but they are far more deadly than any other firearm.";
	}
}
