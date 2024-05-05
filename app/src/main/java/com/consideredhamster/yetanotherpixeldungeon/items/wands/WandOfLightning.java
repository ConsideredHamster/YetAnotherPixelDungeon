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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Thunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfLightning extends WandCombat {

	{
		name = "Wand of Lightning";
        image = ItemSpriteSheet.WAND_LIGHTNING;

        goThrough = false;
    }

    private static ArrayList<Char> affected = new ArrayList<Char>();

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.20f;
    }
	
	@Override
	protected void onZap( int cell ) {

        affected.clear();
        hit( cell, 2 );

        if (!affected.isEmpty()) {

            int damage = damageRoll();

            for (Char aff : affected) {

                int power = cell == aff.pos ? damage : damage / 2;
                aff.damage(power, curUser, Element.SHOCK);

            }
        }
	}

	private void hit( int cell, int times ) {

        Char ch = Char.findChar( cell );

        if( ch != null && ( !Level.water[ cell ] || ch.flying ) ) {

            if( times > 0 && !affected.contains( ch ) ) {

                affected.add(ch);

                for (Char mob : Dungeon.level.mobs) {
                    if (!affected.contains(mob) && Level.distance(ch.pos, mob.pos) <= 2
                            && Level.distance( curUser.pos, mob.pos) > Level.distance( curUser.pos, ch.pos )
                            && Ballistica.cast(ch.pos, mob.pos, false, true) == mob.pos
                    ) {

                        CellEmitter.center(mob.pos).burst(SparkParticle.FACTORY, 3);
                        curUser.sprite.parent.add(new Lightning(ch.pos, mob.pos));

                        hit( mob.pos, times - 1 );
                    }
                }
            }

        } else {

            affected.addAll( Thunderstorm.spreadFrom( cell ) );

        }
    }
	
	@Override
	protected void fx( int cell, Callback callback ) {

        // moved all of the logic to the Thunderstorm blob

        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
        curUser.sprite.parent.add( new Lightning( curUser.pos, cell ) );
        callback.call();

	}
	
	@Override
	public String desc() {
		return
			"This wand conjures forth deadly arcs of electricity, roasting its target with a high " +
            "voltage zap. Effects of this wand can chain to further targets or be transmitted by " +
            "water, so it is better to not stand in the same pool as your target!";
	}
}
