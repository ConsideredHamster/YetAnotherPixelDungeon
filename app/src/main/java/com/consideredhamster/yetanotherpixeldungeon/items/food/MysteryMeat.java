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
package com.consideredhamster.yetanotherpixeldungeon.items.food;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat extends Food {

	{
		name = "raw meat";
		image = ItemSpriteSheet.MEAT;
		energy = Hunger.STARVING / 4;
		message = "That food tasted... strange.";
	}

    @Override
    public void onConsume( Hero hero ) {

        super.onConsume( hero );

        if( Random.Int( 1 ) == 0 ) {

            GLog.w("You are not feeling well.");

            Poison buff = Buff.affect(hero, Poison.class);

            if (buff != null) {
                buff.addDuration(Random.Int(5, 10));
            }
        }
	}
	
	@Override
	public String info() {
		return "Eating this is better than starving, but it is still better be cooked.";
	}
	
	public int price() {
		return 5 * quantity;
	};
}
