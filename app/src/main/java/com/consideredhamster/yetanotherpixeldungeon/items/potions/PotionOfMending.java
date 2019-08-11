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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Mending;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.watabou.utils.Random;

public class PotionOfMending extends Potion {

    public static final float DURATION	= 20f;

	{
		name = "Potion of Mending";
        shortName = "Me";
	}
	
	@Override
	protected void apply( Hero hero ) {

        int totalHP = (int)( hero.HT * hero.ringBuffsHalved(RingOfVitality.Vitality.class ) );

        hero.heal( totalHP / 4 + ( totalHP % 4 > Random.Int(4) ? 1 : 0 ) );

        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 5);

        BuffActive.add( hero, Mending.class, DURATION );

        setKnown();
    }

	@Override
	public String desc() {
		return
			"When imbibed, this elixir will vastly improve imbiber's natural regeneration and cure " +
            "any physical ailments as well.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 30 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 1.00f;
    }
}
