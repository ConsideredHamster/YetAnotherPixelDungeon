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
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Vorpal extends Weapon.Enchantment {
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

    @Override
    protected String name_p() {
        return "Unholy %s";
    }

    @Override
    protected String name_n() {
        return "Damned %s";
    }

    @Override
    protected String desc_p() {
        return "deal tremendous unholy damage to your enemy";
    }

    @Override
    protected String desc_n() {
        return "deal tremendous unholy damage to you";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if( damage > Random.Int( defender.HT * 2 ) ) {
            defender.damage( Math.max( damage, !Bestiary.isBoss(defender) ? defender.HT : defender.HT / 4 ), this, Element.UNHOLY);
            defender.sprite.emitter().burst(ShadowParticle.UP, (int) Math.sqrt(defender.HP / 4) + 1);

//            if (!defender.isAlive() && attacker instanceof Hero) {
//                Badges.validateGrimWeapon();
//            }

            return true;
        }

        return false;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        if( damage > Random.Int( attacker.HT * 2 ) ) {
            attacker.damage(attacker.HP / 2, this, Element.UNHOLY);
            attacker.sprite.emitter().burst(ShadowParticle.UP, (int) Math.sqrt(attacker.HP / 4) + 1);

            return true;
        }

        return false;
    }
}
