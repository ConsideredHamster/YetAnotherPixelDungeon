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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SwarmSprite;

public class CarrionSwarm extends MobEvasive {

    public CarrionSwarm() {

        super( 5 );

        /*

            base maxHP  = 11
            armor class = 2

            damage roll = 1-7

            accuracy    = 11
            dexterity   = 14

            perception  = 100%
            stealth     = 120%

         */

        name = "carrion eater";
        spriteClass = SwarmSprite.class;

        flying = true;

        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Mind.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

	}

//    @Override
//    public float attackDelay() {
//        return 0.5f;
//    }

    @Override
    public String description() {
        return
                "The deadly swarm of flies buzzes angrily. These unclean foes " +
                "have uncanny sense of smell when it comes to anything edible.";
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked  ) {

        if( !blocked && damage > 0 ){

            Satiety hunger = enemy.buff( Satiety.class );

            if( hunger != null ){

                hunger.decrease( Satiety.POINT * 10 );

            }

        }

        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }
}
