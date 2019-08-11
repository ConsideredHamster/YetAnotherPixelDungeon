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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class PotionOfLevitation extends Potion {

    public static final float DURATION	= 25f;

	{
		name = "Potion of Levitation";
        shortName = "Le";
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Levitation.class, DURATION );
        setKnown();
    }
	
	@Override
	public String desc() {
		return
			"Drinking this curious liquid will cause you to hover in the air, moving faster and stealthier. " +
			"This state also allows drifting over traps or chasms and seeing over the high grass. Flames and gases " +
			"fill the air, however, and cannot be bypassed while airborne.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 80 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.55f;
    }
}
