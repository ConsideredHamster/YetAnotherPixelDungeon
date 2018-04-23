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

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat extends Food {

	{
		name = "raw meat";
		image = ItemSpriteSheet.MEAT;
		energy = Satiety.MAXIMUM * 0.25f;
		message = "That food tasted... strange.";
	}

    @Override
    public void onConsume( Hero hero ) {

        GLog.w("You are not feeling well.");

        int maxDuration = (int)( Satiety.MAXIMUM - hero.buff( Satiety.class ).energy() ) / 250 + 1;

        Poisoned debuff = Debuff.add( hero, Poisoned.class, (float) Random.Int( maxDuration ) );

        if( debuff != null ) debuff.delay( time );

        super.onConsume( hero );
    }
	
	@Override
	public String desc() {
		return "Eating this is better than starving, but it is still better be cooked.";
	}
	
	public int price() {
		return 5 * quantity;
	};
}
