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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments;

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfAvalanche;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Freezing extends Weapon.Enchantment {

    @Override
    public Glowing glowing() {
        return BLUE;
    }

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfAvalanche.class;
    }

    @Override
    protected String name_p() {
        return "Freezing %s";
    }

    @Override
    protected String name_n() {
        return "Chilling %s";
    }

    @Override
    protected String desc_p() {
        return "freeze your enemies on hit and increase recharge rate of wands of Freezing";
    }

    @Override
    protected String desc_n() {
        return "freeze you on hit";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        defender.damage(Random.IntRange(damage / 3, damage / 2), this, DamageType.FROST);

        Frozen buff = defender.buff( Frozen.class );

        if( buff != null ) {
            buff.set(2);
        }

        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        attacker.damage(Random.IntRange(damage / 3, damage / 2), this, DamageType.FROST);
        return true;

    }
}
