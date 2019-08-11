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

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class WyrmflowerHerb extends Herb {

    private static final ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );

    private static final int HPBONUS_HERB = 2;
    private static final int HPBONUS_MEAT = 4;

    {
        name = "wyrmflower herb";
        image = ItemSpriteSheet.HERB_WYRMFLOWER;

        cooking = PotentMeat.class;
        message = "That herb had a very potent taste.";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add( PotionOfWisdom.class );
        subPotions.add( PotionOfStrength.class );
    }

    private static void onConsume( Hero hero, int hpBonus ) {
        hero.HP = hero.HT += hpBonus;
        hero.sprite.showStatus( CharSprite.POSITIVE, "+%d hp", hpBonus );
    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, HPBONUS_HERB );
    }

    @Override
    public int price() {
        return 25 * quantity;
    }

    @Override
    public String desc() {
        return "A very rare herb, it is often sought by alchemists for its powerful alchemical " +
                "properties. Being a key ingredient for the most potent of potions, such valuable " +
                "find cannot be underappreciated." +
                "\n\n" +
                "These herbs are used to brew potions of _Wisdom_ and _Strength_ " +
                "when combined with other herbs, but cannot be brewed with another such herb. " +
                "Consuming them will increase your maximum health by _2_ points.";
    }

    public static class PotentMeat extends MeatStewed {

        {
            name = "potent meat";
            spiceGlow = RED;
            message = "That meat had a very potent taste.";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            WyrmflowerHerb.onConsume( hero, HPBONUS_MEAT );
        }

        @Override
        public String desc() {
            return "This meat was stewed in a pot with a _Wyrmflower_ herb. It smells pretty potent. " +
                    "Consuming it will increase your maximum health by _4_ points.";
        }

        @Override
        public int price() {
            return 50 * quantity;
        }
    }
}

