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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;

public class Retribution extends Armour.Glyph {
	
	@Override
	public Glowing glowing() {
		return RED;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Body.class;
    }

    @Override
    protected String name_p() {
        return "%s of retribution";
    }

    @Override
    protected String name_n() {
        return "%s of mercy";
    }

    @Override
    protected String desc_p() {
        return "reflect melee damage back to attacker and increase your physical resilience";
    }

    @Override
    protected String desc_n() {
        return "heal your attacker on hit";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            attacker.damage( Char.absorb( damage, attacker.armorClass() ), this, null);
            attacker.sprite.burst(0x660022, (int) Math.sqrt(damage / 2) + 1);
            return true;
        }

        return false;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        if ( !attacker.isMagical() ) {

            int effValue = Math.min( Random.IntRange(damage / 4, damage / 3), attacker.HT - attacker.HP );

            if( effValue > defender.HP ) {
                effValue = defender.HP;
            }

            if ( effValue > 0 ) {

                attacker.HP += effValue;
                attacker.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(effValue));
                defender.sprite.burst(0x660022, (int) Math.sqrt(effValue / 2) + 1);

                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
