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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;

public class Durability extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return GRAY;
    }

    @Override
    protected String name_p() {
        return "%s of durability";
    }

    @Override
    protected String name_n() {
        return "%s of fragility";
    }

    @Override
    protected String desc_p() {
        return "last longer without breaking and offer a bit more protection";
    }

    @Override
    protected String desc_n() {
        return "break even faster than usual and offer a bit less protection";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }
}
