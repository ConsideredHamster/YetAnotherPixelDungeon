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
package com.consideredhamster.yetanotherpixeldungeon.items.herbs;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class WhirlvineHerb extends Herb {
    {
        name = "Whirlvine herb";
        image = ItemSpriteSheet.HERB_WHIRLVINE;

        alchemyClass = PotionOfLevitation.class;
        message = "That herb tasted... acerbic, but edible.";
    }

    @Override
    public void onConsume( Hero hero ) {

        int maxDuration = (int)( Satiety.MAXIMUM - hero.buff( Satiety.class ).energy() ) / 250 + 1;

        Vertigo debuff = Debuff.add( hero, Vertigo.class, (float) Random.Int( maxDuration ) );

        if( debuff != null ) debuff.delay( time );

        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "Some say that eating Whirlvine herbs can induce a very weird state, like your head " +
                "is floating. Such herbs are usually used to brew potions of Levitation.";
    }
}

