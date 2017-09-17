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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

import com.consideredhamster.yetanotherpixeldungeon.DamageType;

import java.util.HashSet;

public class RingOfEnergy extends Ring {
	
	{
		name = "Ring of Concentration";
        shortName = "Co";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(DamageType.Mind.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Energy();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
                ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
                "was enchanted to improve mental fortitude of it's wearer. Rings of this kind are often used by " +
                "spellcasters of all kind, since greater willpower helps recharge magical wands much more efficiently. " +
                "It also offers a minor benefit of making it easier to shrug off all kinds of mind-affecting effects." +
                ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
	}
	
	public class Energy extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "Suddenly, you are filled with determination." :
                "Suddenly, you find it harder to concentrate." ;
        }
	}
}
