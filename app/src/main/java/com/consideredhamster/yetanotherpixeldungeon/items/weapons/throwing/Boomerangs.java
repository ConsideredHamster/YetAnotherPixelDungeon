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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;

public class Boomerangs extends ThrowingWeaponSpecial {

	{
		name = "boomerangs";
		image = ItemSpriteSheet.BOOMERANG;
	}

	public Boomerangs() {
		this( 1 );
	}

	public Boomerangs(int number) {
        super( 3 );
		quantity = number;
	}

    @Override
    public int str( int bonus ) {
        return super.str( bonus ) + 1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 1 ;
    }

    @Override
    public void proc( Char attacker, Char defender, int damage ) {
        super.proc(attacker, defender, damage);

        if (Random.Int(defender.HT) < damage * 2) {
            Buff.prolong(defender, Stun.class, 1f);
        }
    }
	
	@Override
	public String desc() {
		return 
			"Thrown to the enemy these flat curved wooden missiles will return to the hands of its thrower when missed." +
            "Their blunt edges do not deal significant damage, but precise throw of a boomerang can stun the target.";
	}
}
