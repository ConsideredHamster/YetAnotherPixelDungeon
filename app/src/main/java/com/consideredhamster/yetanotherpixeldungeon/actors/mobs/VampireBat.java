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
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.BatSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;

public class VampireBat extends MobEvasive {

    public VampireBat() {

        super( 9 );

        /*

            base maxHP  = 17
            armor class = 3

            damage roll = 2-10

            accuracy    = 17
            dexterity   = 21

            perception  = 100%
            stealth     = 130%

         */

        name = "vampire bat";
        spriteClass = BatSprite.class;

        flying = true;

        baseSpeed = 2f;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );
	}

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
            && Level.distance( pos, Dungeon.hero.pos ) <= 2
            && detected( Dungeon.hero ) && detected(Dungeon.hero)
        ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() ) {

            int healed = Element.Resist.modifyValue( damage / 2, enemy, Element.BODY );

            if (healed > 0) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by " +
			"replenishing their health with each successful attack.";
	}
}
