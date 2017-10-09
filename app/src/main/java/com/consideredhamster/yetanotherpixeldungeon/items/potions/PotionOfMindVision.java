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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.MindVision;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class PotionOfMindVision extends Potion {

    public static final float DURATION	= 30f;
    public static final float MODIFIER	= 1.0f;

	{
		name = "Potion of Mind Vision";
        shortName = "Mi";
	}
	
	@Override
	protected void apply( Hero hero ) {
		Buff.affect(hero, MindVision.class, DURATION + alchemySkill() * MODIFIER );
        Dungeon.observe();

        GLog.i( Dungeon.level.mobs.size() > 0 ?
            "You can somehow feel the presence of other creatures' minds!" :
            "You can somehow tell that you are alone on this level at the moment."
        );

        setKnown();
    }
	
	@Override
	public String desc() {
		return
			"After drinking this, your mind will become attuned to the psychic signature " +
			"of distant creatures, enabling you to sense everyone on current floor through walls. " +
			"Also this potion will negate most of the disadvantages of blindness.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 35 * quantity : super.price();
    }
}
