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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class EmptyBottle extends Potion {

	{
		name = "empty bottle";
        shortName = "";
        harmful = true;
		image = ItemSpriteSheet.POTION_EMPTY;
	}

    @Override
    public boolean isTypeKnown() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    protected void splash( int cell ) {
        Splash.at( cell, 0xFFFFFF, 10 );
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero );
        actions.remove( AC_DRINK );
        return actions;
    }

	@Override
	public String info() {
		return "Any alchemist knows that proper potion brewing requires having an airtight " +
            "container at hand, as most resulting chemicals either quickly lose their potence " +
            "when exposed to air or, even worse, react violently to it. These bottles can be " +
            "used only once, however.";
	}

	@Override
	public int price() {
		return isTypeKnown() ? 20 * quantity : super.price();
	}

}
