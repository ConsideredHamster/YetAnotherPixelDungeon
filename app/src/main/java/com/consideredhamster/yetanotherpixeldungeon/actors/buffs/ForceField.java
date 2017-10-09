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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

import java.util.HashSet;

public class ForceField extends PassiveBuff {

    public static final HashSet<Class<? extends DamageType>> RESISTS;
    static {
        RESISTS = new HashSet<>();
        RESISTS.add(DamageType.Flame.class);
        RESISTS.add(DamageType.Frost.class);
        RESISTS.add(DamageType.Shock.class);
        RESISTS.add(DamageType.Acid.class);
        RESISTS.add(DamageType.Energy.class);
        RESISTS.add(DamageType.Unholy.class);
    }

//	@Override
//	public boolean act() {
//		if (target.isAlive()) {
//
//			spend( TICK );
//			if (--bonus <= 0) {
//				detach();
//			}
//
//		} else {
//
//			detach();
//
//		}
//
//		return true;
//	}
	
//	public int bonus() {
//		return bonus;
//	}
//
//	public void bonus( int value ) {
//		if (bonus < value) {
//			bonus = value;
//		}
//	}
	
	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}
	
	@Override
	public String toString() {
		return "Shield";
	}
}
