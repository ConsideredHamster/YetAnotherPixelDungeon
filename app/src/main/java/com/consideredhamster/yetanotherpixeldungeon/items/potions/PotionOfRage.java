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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;

public class PotionOfRage extends Potion {

    public static final float DURATION	= 20f;

	{
		name = "Potion of Rage";
        shortName = "Ra";
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Enraged.class, DURATION );
        setKnown();
    }
	
	@Override
	public String desc() {
		return
			"Drinking this potion will induce a berserker state, significantly " +
			"increasing strength of your blows for a limited time. "+
			"Killing an enemy in this state will extend this bloodthirsty state.";

	}

    @Override
    public int price() {
        return isTypeKnown() ? 100 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.35f;
    }

}
