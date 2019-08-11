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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;

public class PotionOfWisdom extends Potion {

	{
		name = "Potion of Wisdom";
        shortName = "Wi";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();

        int exp = hero.maxExp();

        float bonus = Dungeon.hero.ringBuffs(RingOfKnowledge.Knowledge.class) * exp - exp;

        exp += (int)bonus;
        exp += Random.Float() < bonus % 1 ? 1 : 0 ;

		hero.earnExp( exp );
        hero.lvlBonus++;
	}
	
	@Override
	public String desc() {
		return
			"The stored experiences of multitudes of lifetimes reduced to liquid form, " +
			"this draught will instantly raise your experience level.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 200 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.25f;
    }
}
