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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class AlchemyPot {

	private static final String TXT_SELECT_HERB = "Select a herb to throw";
	
	private static Hero hero;
	private static int pos;
	
	public static void operate( Hero hero, int pos ) {
		
		AlchemyPot.hero = hero;
		AlchemyPot.pos = pos;
		
		GameScene.selectItem( itemSelector, WndBag.Mode.HERB, TXT_SELECT_HERB);
	}
	
	private static final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
        if (item != null) {
            item.throwAt(hero, pos);
        }
		}
	};
}
