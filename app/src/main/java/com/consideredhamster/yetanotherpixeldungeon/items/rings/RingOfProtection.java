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

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.DamageType;

public class RingOfProtection extends Ring {

	{
		name = "Ring of Protection";
        shortName = "Pr";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Protection();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
            ( bonus < 0 && isIdentified() ? "Normally, this ring " : "This ring " ) +
            "greatly boosts your survivability by allowing you to occasionally resist different " +
            "elemental threats such as fire, frost, electricity, acid, energy or withering. It " +
            "also increases your armor class by amount depending on your physical strength." +
            ( bonus < 0 && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
        super.desc();
	}

    public static final HashSet<Class<? extends DamageType>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(DamageType.Flame.class);
        RESISTS.add(DamageType.Frost.class);
        RESISTS.add(DamageType.Shock.class);
        RESISTS.add(DamageType.Acid.class);
        RESISTS.add(DamageType.Energy.class);
        RESISTS.add(DamageType.Unholy.class);
    }
	
	public class Protection extends RingBuff {
		
//		public float durationFactor() {
//			return 1.0f / target.ringBuffs(Protection.class);
//		}

        @Override
        public String desc() {
            return bonus >= 0 ?
                    "You feel more protected." :
                    "You feel more vulnerable." ;
        }
	}
}
