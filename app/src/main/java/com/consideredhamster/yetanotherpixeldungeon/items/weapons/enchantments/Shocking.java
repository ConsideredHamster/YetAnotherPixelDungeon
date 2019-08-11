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

import java.util.ArrayList;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfLightning;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;

public class Shocking extends Weapon.Enchantment {

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfLightning.class;
    }

    @Override
    protected String name_p() {
        return "Shocking %s";
    }

    @Override
    protected String name_n() {
        return "Sparking %s";
    }

    @Override
    protected String desc_p() {
        return "shock your enemies on hit";
    }

    @Override
    protected String desc_n() {
        return "shock you on hit";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        defender.damage( Random.IntRange(damage / 3, damage / 2), this, Element.SHOCK );
        defender.sprite.parent.add( new Lightning( defender.pos, defender.pos ) );

        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        attacker.damage(damage, this, Element.SHOCK);
        attacker.sprite.parent.add( new Lightning( attacker.pos, attacker.pos ) );

        return true;
    }
}
