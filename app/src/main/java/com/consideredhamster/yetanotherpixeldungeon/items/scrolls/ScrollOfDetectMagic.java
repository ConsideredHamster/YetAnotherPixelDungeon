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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfDetectMagic extends Scroll {

//    private static final String TXT_REVEALED	= "You notice something peculiar!";
    private static final String TXT_IDENTIFIED	= "Your items are identified!";
    private static final String TXT_NOTHING 	= "Nothing happens.";

	{
		name = "Scroll of Detect Magic";
        shortName = "DM";

//		inventoryTitle = "Select an item to identify";
//		mode = WndBag.Mode.UNIDENTIFED;

        spellSprite = SpellSprite.SCROLL_IDENTIFY;
        spellColour = SpellSprite.COLOUR_RUNE;
	}

    @Override
    protected void doRead() {

        boolean identified = identify( curUser.belongings.backpack.items.toArray( new Item[0] ) );

        identified = identified || identify(
                curUser.belongings.weap1,
                curUser.belongings.weap2,
                curUser.belongings.armor,
                curUser.belongings.ring1,
                curUser.belongings.ring2
        );

        if( identified ){
            GLog.i( TXT_IDENTIFIED );
        } else {
            GLog.i( TXT_NOTHING );
        }

        new Flare( 5, 32 ).color( 0x3399FF, true ).show(curUser.sprite, 2f);
        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange( 7, 9));

        super.doRead();
    }

//	@Override
//	protected void onItemSelected( Item item ) {
//
//        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange(6, 9));
//
//        item.identify();
//        GLog.i("It is " + item);
//
//    }
	
	@Override
	public String desc() {
		return
                "The incantation etched in this scroll can bestow the ability to perceive the weaves of magic " +
                "upon its reader, revealing the items' supernatural qualities. This will allow its user " +
                "to tell the enchanted and cursed equipment from the mundane but without further details.";
	}

    public static boolean identify( Item... items ) {

        boolean procced = false;

        for (int i=0; i < items.length; i++) {
            Item item = items[i];
            if (
                item instanceof Weapon || item instanceof Armour ||
                item instanceof Ring || item instanceof Wand
            ) {
                if( !item.isIdentified() ){
                    if( item.isMagical() ){
                        item.identify( CURSED_KNOWN );
                    } else {
                        item.identify();
                    }
                    procced = true;
                }
            }
        }

        return procced;
    }

	@Override
	public int price() {
		return isTypeKnown() ? 55 * quantity : super.price();
	}
}
