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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.ShockResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfThunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfToxicGas;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class WhirlvineHerb extends Herb {

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    {
        name = "Whirlvine herb";
        image = ItemSpriteSheet.HERB_WHIRLVINE;

        cooking = SourMeat.class;
        message = "That herb tasted sour, but edible.";

        mainPotion = PotionOfThunderstorm.class;

        subPotions.add( PotionOfLevitation.class );
        subPotions.add( PotionOfToxicGas.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, ShockResistance.class, duration );
//        Debuff.remove( hero, Shocked.class );

    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, DURATION_HERB );
    }

    @Override
    public int price() {
        return 15 * quantity;
    }

    @Override
    public String desc() {
        return "It is a pretty well-known fact that stalks of Whirlvines can be used to predict " +
                "stormy weather, and they are often associated with winds and lightning." +
                "\n\n" +
                "These herbs are used to brew potions of _Thunderstorm_, _Levitation_ and _Toxic Gas_. " +
                "Consuming them will grant a short buff to your _shock_ resistance.";
    }

    public static class SourMeat extends MeatStewed {

        {
            name = "sour meat";
            spiceGlow = WHITE;
            message = "That meat tasted sour, but edible.";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            WhirlvineHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "This meat was stewed in a pot with a _Whirlvine_ herb. It smells pretty sour. " +
                    "Consuming it will grant a long buff to your _shock_ resistance.";
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}

