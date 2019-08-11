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

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;

public abstract class ThrowingWeaponHeavy extends ThrowingWeapon {

    public ThrowingWeaponHeavy(int tier) {
        super( tier );
    }

    @Override
    public String descType() {
        return "heavy throwing";
    }

    @Override
    public int min( int bonus ) {
        return tier + 3;
    }

    @Override
    public int max( int bonus ) {
        return 8 + tier * 3;
    }

    @Override
    public int str( int bonus ) {
        return 10 + tier * 2;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + 4;
    }

    @Override
    public int baseAmount() {
        return 7 - tier;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public int price() {

        return quantity * tier * 15;

    }
}
