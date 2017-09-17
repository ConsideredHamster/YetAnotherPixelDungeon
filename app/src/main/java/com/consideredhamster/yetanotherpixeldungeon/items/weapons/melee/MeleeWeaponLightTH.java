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

package com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Tempered;

public abstract class MeleeWeaponLightTH extends MeleeWeapon {

    public MeleeWeaponLightTH(int tier) {

        super( tier);
    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " light two-handed weapon_. It can be used with shields, wands and throwing weapons, " +
//                "but its strength requirement will increase if paired with another melee weapon.";
        return "light two-handed";
    }

    @Override
    public int min( int bonus ) {
        return super.min(bonus) + ( enchantment instanceof Tempered ? 2 : 0 ) + 2;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + ( enchantment instanceof Tempered ? 2 : 0 ) + 8 ;
    }

    @Override
    public int dmgMod() {
        return super.dmgMod() + 2 ;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus) + 6;
    }

    @Override
    public int strShown( boolean identified ) {
        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap1 && incompatibleWith( Dungeon.hero.belongings.weap2 ) ?
                        Dungeon.hero.belongings.weap2.str(
                                Dungeon.hero.belongings.weap2.isIdentified() ?
                                        Dungeon.hero.belongings.weap2.bonus : 0
                        ) : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return item instanceof MeleeWeapon ; }

}
