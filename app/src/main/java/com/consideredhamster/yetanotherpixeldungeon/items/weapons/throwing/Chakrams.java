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

package com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class Chakrams extends ThrowingWeaponLight {

    {
        name = "chakrams";
        image = ItemSpriteSheet.THROWING_ANUS;
    }

    public Chakrams() {
        this( 1 );
    }

    public Chakrams(int number) {
        super( 3 );
        quantity = number;
    }

    @Override
    public int str( int bonus ) {
        return super.str( bonus ) + 2 ;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 2 ;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public String desc() {
        return "This razor-edged missile is made in such curious way that skilled user returns to " +
                "the hands of the thrower on successful hit.";
    }
}
