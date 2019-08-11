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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class MagicalResistance extends ElementResistance {
	
	@Override
	public String toString() {
		return "Magical resistance";
	}

    @Override
    public String statusMessage() { return "magical resistance"; }

    @Override
    public String playerMessage() {
        return "Your some kind of protective aura.";
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_MAGI;
    }

    @Override
    public Class<? extends Element> resistance(){
        return Element.Energy.class;
    }

    @Override
    public float resistanceValue() {
        return 0.5f;
    }

    @Override
    public String description() {
        return "You fell a kind of a faint protective aura around your body. " +
            "Your resistance to magical damage such as magic missiles or disintegration is increased.";
    }
}
