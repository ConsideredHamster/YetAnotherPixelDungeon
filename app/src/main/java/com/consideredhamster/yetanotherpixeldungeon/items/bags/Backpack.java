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
package com.consideredhamster.yetanotherpixeldungeon.items.bags;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;

public class Backpack extends Bag {
	{
        name = "backpack";
        image = ItemSpriteSheet.BACKPACK;

        size = 19;
	}

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//
//        actions.remove( AC_THROW );
//        actions.remove( AC_DROP );
//
//        return actions;
//    }

    @Override
    public Icons icon() {
        return Icons.BACKPACK;
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public String info() {
        return "That's your backpack. Everything useful goes in here.";
    }

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( Backpack.class ) == null && super.doPickUp( hero ) ;

    }
}
