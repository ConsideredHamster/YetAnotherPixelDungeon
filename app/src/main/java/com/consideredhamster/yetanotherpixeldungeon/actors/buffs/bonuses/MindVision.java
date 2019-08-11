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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class MindVision extends Bonus {

	public static final float DURATION = 15f;
	
	@Override
	public String toString() {
		return "Mind vision";
	}

    @Override
    public String statusMessage() { return "mind vision"; }

    @Override
    public String playerMessage() {
        return Dungeon.level.mobs.size() > 0 ?
            "You can somehow feel the presence of other creatures' minds!" :
            "You can somehow tell that you are alone on this level at the moment.";
    }

    @Override
    public int icon() {
        return BuffIndicator.MIND_VISION;
    }

//    @Override
//    public void applyVisual(){}
//
//    @Override
//    public void removeVisual() {}

    @Override
    public String description() {
        return "Woah, dude... Your mind feels completely open and connected to the minds of other " +
                "creatures on the current floor. This makes it possible for you to see their positions, " +
                "and increases your awareness.";
    }

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
}
