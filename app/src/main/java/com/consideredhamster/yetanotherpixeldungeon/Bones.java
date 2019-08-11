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
package com.consideredhamster.yetanotherpixeldungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.noosa.Game;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bones {

	private static final String BONES_FILE	= "bones_%d.dat";

	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";
	
	private static int depth = -1;
	private static Item item;
	
	public static void leave() {
		
		item = null;
		switch (Random.Int( 5 )) {
		case 0:
			item = Dungeon.hero.belongings.weap1;
			break;
		case 1:
            item = Dungeon.hero.belongings.weap2;
            break;
        case 2:
            item = Dungeon.hero.belongings.armor;
			break;
		case 3:
			item = Dungeon.hero.belongings.ring1;
			break;
		case 4:
			item = Dungeon.hero.belongings.ring2;
			break;
		}

		if (item == null) {
			if (Dungeon.gold > 0) {
				item = new Gold( Random.IntRange( 1, Dungeon.gold ) );
			} else {
				item = new Gold( 1 );
			}
		}
		
		depth = Dungeon.depth;
		
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, Statistics.deepestFloor );
		bundle.put( ITEM, item );

        String bonesFile = Utils.format( BONES_FILE, Dungeon.difficulty );

		try {
			OutputStream output = Game.instance.openFileOutput( bonesFile, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
		} catch (IOException e) {

		}
	}
	
	public static Item get() {

        String bonesFile = Utils.format( BONES_FILE, Dungeon.difficulty );

		if (depth == -1) {
			
			try {

				InputStream input = Game.instance.openFileInput( bonesFile ) ;
				Bundle bundle = Bundle.read( input );
				input.close();
				
				depth = bundle.getInt( LEVEL );
				item = (Item)bundle.get( ITEM );
				
				return get();
				
			} catch (IOException e) {
				return null;
			}
			
		} else {
			if (depth == Dungeon.depth) {
				Game.instance.deleteFile( bonesFile );
				depth = 0;
				
//				if (!item.stackable) {
//					item.cursed = true;
//					item.cursedKnown = true;
//					if (item.isUpgradeable()) {
//						int lvl = (Dungeon.depth - 1) * 3 / 5 + 1;
//						if (lvl < item.bonus) {
//							item.curse( item.bonus - lvl );
//						}
//						item.bonusKnown = false;
//					}
//				}

                item.identify( Item.ITEM_UNKNOWN, true );
				
				if (item instanceof Ring) {
					((Ring)item).syncGem();
				}

//                if (item instanceof Wand) {
//                    ((Wand)item).syncWood();
//                }
				
				return item;
			} else {
				return null;
			}
		}
	}
}
