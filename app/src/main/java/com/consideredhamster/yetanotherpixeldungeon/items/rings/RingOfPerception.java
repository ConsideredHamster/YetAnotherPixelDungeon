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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;

public class RingOfPerception extends Ring {

	{
		name = "Ring of Awareness";
        shortName = "Aw";
	}
	
//	@Override
//	public boolean doEquip( Hero hero ) {
//		if (super.doEquip( hero )) {
//			Dungeon.hero.search( false );
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	@Override
	protected RingBuff buff( ) {
		return new Perception();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
                ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
                "boosts your alertness when worn, improving your perception and increasing damage " +
                "against targets exposed to counterattacks." +
                ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
	}
	
	public class Perception extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    "You feel that your alertness is improved." :
                    "You feel that your alertness is dimmed." ;
        }
	}
}
