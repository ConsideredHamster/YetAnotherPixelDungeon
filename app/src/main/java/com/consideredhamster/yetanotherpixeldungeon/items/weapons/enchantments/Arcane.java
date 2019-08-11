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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfDisintegration;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandUtility;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Arcane extends Weapon.Enchantment {

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfDisintegration.class;
    }

    @Override
    protected String name_p() {
        return "Arcane %s";
    }

    @Override
    protected String name_n() {
        return "Negating %s";
    }

    @Override
    protected String desc_p() {
        return "recharge your wands when attacking";
    }

    @Override
    protected String desc_n() {
        return "discharge your wands when attacking";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if( attacker instanceof Hero ) {

            Hero hero = (Hero)attacker;

            for (Item item : hero.belongings) {
                if (item instanceof Wand) {
                    Wand wand = (Wand) item;
                    if ( wand instanceof WandUtility || Random.Int( 2 ) == 0 ) {
                        wand.setCharges( wand.getCharges() + 1 );
                    }
                }
            }

            attacker.sprite.centerEmitter().burst(EnergyParticle.FACTORY, (int) Math.sqrt(damage / 2) + 1);
        }

        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        if( attacker instanceof Hero ) {

            Hero hero = (Hero)attacker;

            for (Item item : hero.belongings) {
                if (item instanceof Wand) {
                    Wand wand = (Wand) item;
                    if ( wand instanceof WandUtility || Random.Int( 2 ) == 0 ) {
                        wand.setCharges( wand.getCharges() - 1 );
                    }
                }
            }

            attacker.sprite.centerEmitter().burst(SparkParticle.FACTORY, (int) Math.sqrt(damage / 2) + 1);
        }

        return true;
    }
}
