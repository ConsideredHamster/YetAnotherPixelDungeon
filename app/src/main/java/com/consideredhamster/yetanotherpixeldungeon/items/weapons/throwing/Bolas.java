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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Bolas extends ThrowingWeaponSpecial {

	{
		name = "bolas";
		image = ItemSpriteSheet.HUNTING_BOLAS;
	}

	public Bolas() {
		this( 1 );
	}

	public Bolas(int number) {
        super( 2 );
		quantity = number;
	}

    @Override
    public void proc( Char attacker, Char defender, int damage ) {
        super.proc(attacker, defender, damage);

        BuffActive.addFromDamage(defender, Ensnared.class, damageRoll( (Hero) attacker ) * 2 );
    }
	
	@Override
	public String desc() {
		return 
			"Bolas are mostly used for hunting and they usually don't do much damage but " +
            "they can ensnare the target. Such bolas are often made from spider's silk to " +
            "enhance their ensnaring capabilities.";
	}
}
