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

import com.consideredhamster.yetanotherpixeldungeon.Element;

import java.util.HashSet;

public class RingOfVitality extends Ring {
	
	{
		name = "Ring of Vitality";
        shortName = "Vi";
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Body.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Vitality();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
                ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
                "increases the body's regenerative properties, augmenting effects of all sources " +
                "of healing, and increasing your resistance to negative effects affecting your body." +
                ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
	}
	
	public class Vitality extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    "Warm feeling rushes down your veins, soothing the pain in your wounds." :
                    "Feeling of discomfort fills your body, making you feel sick." ;
        }
	}
}
