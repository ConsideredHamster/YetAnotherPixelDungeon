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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.PhysicalResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfShield;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWebbing;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class EarthrootHerb extends Herb {

    private static final ItemSprite.Glowing BROWN = new ItemSprite.Glowing( 0x663300 );

    {
        name = "Earthroot herb";
        image = ItemSpriteSheet.HERB_EARTHROOT;

        cooking = ChewyMeat.class;
        message = "That herb was very hard to chew.";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add( PotionOfStrength.class );
        subPotions.add( PotionOfWebbing.class );
        subPotions.add( PotionOfShield.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, PhysicalResistance.class, duration );
//        Debuff.remove( hero, Ensnared.class );

    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, DURATION_HERB );
    }

    @Override
    public int price() {
        return 20 * quantity;
    }

    @Override
    public String desc() {
        return "Certain tribes use these roots as food in their rituals, as it is believed that " +
                "consuming them improves fortitude. Actually finding one of these roots is " +
                "usually a feat in itself, given that they usually grow in deep caverns." +
                "\n\n" +
                "These herbs are used to brew potions of _Shield_, _Webbing_ and _Strength_ " +
                "when combined with other herbs, but cannot be brewed with another such herb. " +
                "Consuming them will grant a short buff to your _physical_ resistance.";
    }

    public static class ChewyMeat extends MeatStewed {

        {
            name = "chewy meat";
            spiceGlow = BROWN;
            message = "That meat was very hard to chew.";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            EarthrootHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "This meat was stewed in a pot with an _Earthroot_ herb. It feels to be rough. " +
                "Consuming it will grant a long buff to your _physical_ resistance.";
        }

        @Override
        public int price() {
            return 40 * quantity;
        }

    }
}

