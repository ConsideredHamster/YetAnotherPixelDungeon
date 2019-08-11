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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tomahawks extends ThrowingWeaponHeavy {

	{
		name = "tomahawks";
		image = ItemSpriteSheet.TOMAHAWK;
	}
	
	public Tomahawks() {
		this( 1 );
	}
	
	public Tomahawks(int number) {
        super( 2 );
		quantity = number;
	}

    @Override
    public void proc( Char attacker, final Char defender, int damage ) {

        super.proc(attacker, defender, damage);

        if( damage > Random.Int( defender.totalHealthValue() ) ){
            Pushing.knockback( defender, attacker.pos, 1, 0 );
        }

    }
	
	@Override
	public String desc() {
		return 
			"These throwing axes are so heavy that on a successful throw they not only inflict " +
            "grievous wounds, but also push the target back for a short distance.";
	}
}
