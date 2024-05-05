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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.CausticOoze;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ScorpionSprite;
import com.watabou.utils.Random;

public class CaveScorpion extends MobHealthy {

    public CaveScorpion() {

        super( 12 );

        /*

            base maxHP  = 32
            armor class = 12

            damage roll = 6-17

            accuracy    = 15
            dexterity   = 12

            perception  = 85%
            stealth     = 85%

         */

		name = "cave scorpion";
		info = "Corrosive attack, Acidic blood";

		spriteClass = ScorpionSprite.class;

        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.VULNERABLE );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

	}

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Corrosion.class, damage * 2 );
        }

        return damage;
    }

    @Override
	public void die( Object cause, Element dmg ) {

        CausticOoze.spawn( pos, (int)Math.sqrt( totalHealthValue() ) );

        super.die(cause, dmg);
    }

	@Override
	public String description() {
		return
			"These huge arachnid-like creatures pose a significant threat to any adventurer " +
            "due to a ability to inject acid with their tails.";
	}

}
