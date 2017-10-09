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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfIdentify extends InventoryScroll {

//    private static final String TXT_REVEALED	= "You notice something peculiar!";
//    private static final String TXT_IDENTIFIED	= "Your equipped items are identified!";
//    private static final String TXT_NOTHING 	= "Nothing happens.";

	{
		name = "Scroll of Identify";
        shortName = "Id";

		inventoryTitle = "Select an item to identify";
		mode = WndBag.Mode.UNIDENTIFED;

        spellSprite = SpellSprite.SCROLL_IDENTIFY;
        spellColour = SpellSprite.COLOUR_RUNE;
	}

//    @Override
//    protected void doRead() {
//
//        boolean identified = identify(
//                curUser.belongings.weapon,
//                curUser.belongings.armor,
//                curUser.belongings.ring1,
//                curUser.belongings.ring2
//        );
//
//        boolean revealed = reveal();
//
//        if( identified )
//            GLog.i( TXT_IDENTIFIED );
//
//        if( revealed )
//            GLog.i( TXT_REVEALED );
//
//        if( !revealed && !identified )
//            GLog.i( TXT_NOTHING );
//
//        new Flare( 5, 32 ).color( 0x3399FF, true ).show(curUser.sprite, 2f);
//        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange( 7, 9));
//
//        super.doRead();
//    }

	@Override
	protected void onItemSelected( Item item ) {
		
//		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange(6, 9));

        item.identify();
        GLog.i("It is " + item);

//        Badges.validateItemLevelAcquired(item);

    }
	
	@Override
	public String desc() {
		return
			"Incantation etched on this scroll can bestow its reader with ability to perceive the " +
            "very nature of things, revealing their purposes and qualities. However, duration of " +
            "this spell is really short, so it is possible to assess only a single item with its help.";
	}

    public static boolean identify( Item... items ) {

        boolean procced = false;

        for (int i=0; i < items.length; i++) {
            Item item = items[i];
            if (item != null) {

                item.identify();
            }
        }

        return procced;
    }

//    public static boolean reveal() {
//
//        int length = Level.LENGTH;
//        int[] map = Dungeon.level.map;
//        boolean[] visible = Dungeon.visible;
//
//        boolean noticed = false;
//
//        for (int i=0; i < length; i++) {
//
//            int terr = map[i];
//
//            if (visible[i]) {
//
//                if (Dungeon.visible[i] && (Terrain.flags[terr] & Terrain.TRAPPED) != 0) {
//
//                    Level.set( i, Terrain.discover(terr) );
//                    GameScene.updateMap( i );
//
//                    GameScene.discoverTile(i, terr);
//                    CellEmitter.get(i).start(Speck.factory(Speck.DISCOVER), 0.1f, 5);
//
//                    noticed = true;
//                }
//            }
//        }
//        Dungeon.observe();
//
//        if (noticed) {
//            Sample.INSTANCE.play( Assets.SND_SECRET );
//        }
//
//        return noticed;
//    }
	
	@Override
	public int price() {
		return isTypeKnown() ? 55 * quantity : super.price();
	}
}
