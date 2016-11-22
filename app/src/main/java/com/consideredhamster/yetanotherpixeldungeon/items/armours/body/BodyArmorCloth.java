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
 
package com.consideredhamster.yetanotherpixeldungeon.items.armours.body;

public abstract class BodyArmorCloth extends BodyArmor {

    public BodyArmorCloth(int tier) {
        super( tier );
    }

    @Override
    public int str( int bonus ) {
        return super.str(bonus) - 4;
    }

    @Override
    public int dr( int bonus ) {
        return super.dr( bonus ) - 4;
    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " light armor_. It offers less protection, but hinders your dexterity and stealth to much lesser extent.";
        return "cloth";
    }

}
