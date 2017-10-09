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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class Harpoons extends ThrowingWeaponHeavy {

	{
		name = "harpoons";
		image = ItemSpriteSheet.HARPOON;
	}

	public Harpoons() {
		this( 1 );
	}

	public Harpoons(int number) {
        super( 3 );
		quantity = number;
	}

    @Override
    public int image() {
        return ItemSpriteSheet.HARPOON;
    }

    @Override
    public int imageAlt() {
        return ItemSpriteSheet.HARPOON_THROWN;
    }

    @Override
    public void proc(Char attacker, Char defender, int damage ) {
        super.proc(attacker, defender, damage);

        if( !Level.adjacent( attacker.pos, defender.pos ) ) {

            if( attacker.STR() >= defender.STR() && !defender.immovable() ) {

                int distance = Math.max( 0, attacker.STR() - defender.STR() + 1 );

                int newPos = Ballistica.trace[Math.max( 1, Ballistica.distance - distance - 1 )];

                Actor.addDelayed(new Pushing(defender, defender.pos, newPos), -1);

                defender.pos = newPos;

                Dungeon.level.press( newPos, defender );

                defender.delay( 1f );

            } else {

                int distance = Math.max(0, defender.STR() - attacker.STR());

                int newPos = Ballistica.trace[Math.min( Ballistica.distance - 2, distance )];

                Actor.addDelayed(new Pushing(attacker, attacker.pos, newPos), -1);

                attacker.pos = newPos;

                Dungeon.level.press( newPos, attacker );

            }
        }
    }
	
	@Override
	public String desc() {
		return 
			"Harpoons can be used to pull your targets towards you - or to pull you towards your doom, depending on your target.";
	}
}
