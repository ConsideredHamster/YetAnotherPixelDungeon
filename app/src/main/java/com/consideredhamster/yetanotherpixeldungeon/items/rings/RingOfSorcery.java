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

public class RingOfSorcery extends Ring {

	{
		name = "Ring of Sorcery";
        shortName = "So";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Sorcery();
	}

    @Override
    public String desc() {
        return isTypeKnown() ?
               ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
               "improves your magic skills, increasing your accuracy with your combat wands. Besides, this ring " +
               "also improves reliability of your enchanted equipment, increasing their chances to be activated." +
               ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
    }

    public class Sorcery extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    "Your arcane proficiency is improved." :
                    "Your arcane proficiency is decreased." ;
        }
    }
}
