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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfAcidSpray;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Vampiric extends Weapon.Enchantment {
	
	@Override
	public Glowing glowing() {
		return RED;
	}

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfAcidSpray.class;
    }

    @Override
    protected String name_p() {
        return "Vampiric %s";
    }

    @Override
    protected String name_n() {
        return "Malicious %s";
    }

    @Override
    protected String desc_p() {
        return "drain health from non-magical enemies";
    }

    @Override
    protected String desc_n() {
        return "damage you on hit";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if ( attacker.isAlive() ) {

            int effValue = Element.Resist.modifyValue( damage / 2, defender, Element.BODY );

            if( effValue > defender.HP ) {
                effValue = defender.HP;
            }

            if ( effValue > 0 ) {

                attacker.heal( effValue );

                if( attacker.sprite.visible ) {
                    attacker.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1);
                }
                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        attacker.damage(Random.IntRange(damage / 3, damage / 2), this, Element.BODY);
        attacker.sprite.burst(0x660022, (int) Math.sqrt(damage / 2) + 1);
        return true;
    }
}
