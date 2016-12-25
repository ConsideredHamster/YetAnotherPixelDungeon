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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs;

import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponFlintlock;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;

public class Combo extends Buff {
	
	private static String TXT_COMBO = "combo %dx!";
	
	public int count = 0;
	
//	@Override
//	public int icon() {
//		return BuffIndicator.COMBO;
//	}

    private static final String COUNT	= "count";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, count );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );
    }
	
	@Override
	public String toString() {
		return "Combo";
	}
	
	public void hit( ) {

		count++;

        if (count >= 3) {
            target.sprite.showStatus(CharSprite.DEFAULT, TXT_COMBO, count);
        }

        if( target instanceof Hero && ((Hero) target).rangedWeapon instanceof RangedWeaponFlintlock ) {

            postpone( target.attackDelay() * 3 / 2 + Float.MIN_VALUE );

        } else {

            postpone( target.attackDelay() + Float.MIN_VALUE );

        }
	}

    public float modifier() {

        if (count >= 2) {

            return (count - 1) * 0.125f;

        } else {

            return 0.0f;

        }
    }
	
	@Override
	public boolean act() {
		detach();
		return true;
	}
	
}
