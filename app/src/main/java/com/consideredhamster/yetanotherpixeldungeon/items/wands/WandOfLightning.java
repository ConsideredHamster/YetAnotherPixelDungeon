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

import java.util.ArrayList;
import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfLightning extends Wand {

	{
		name = "Wand of Lightning";
        shortName = "Li";
	}

    @Override
    public int max() {
        return 20;
    }

    @Override
    public int min() {
        return 10;
    }
	
	private ArrayList<Char> affected = new ArrayList<Char>();
	
	private int[] points = new int[20];
	private int nPoints;
	
	@Override
	protected void onZap( int cell ) {
		// Everything is processed in fx() method
//		if (!curUser.isAlive()) {
//			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
//			GLog.n( "You killed yourself with your own Wand of Lightning..." );
//		}
	}
	
	private void hit( Char ch, int power ) {
		
		if (power < 1) {
			return;
		}

        ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, power / 2 + 1 );
        ch.sprite.flash();

        points[nPoints++] = ch.pos;
        affected.add(ch);

        if( Char.hit( curUser, ch, false, true ) ) {

            ch.damage(power, curUser, DamageType.SHOCK);

            HashSet<Char> ns = new HashSet<Char>();

            for (int i : Level.NEIGHBOURS8) {
                Char n = Actor.findChar(ch.pos + i);
                if (n != null && !affected.contains(n)) {
                    ns.add(n);
                }
            }

            if (ns.size() > 0) {
                hit(Random.element(ns), Random.NormalIntRange(power / 2, power));
            }

        } else {

            ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
            Sample.INSTANCE.play(Assets.SND_MISS);

        }
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		
		nPoints = 0;
		points[nPoints++] = Dungeon.hero.pos;

        float effectiveness = effectiveness();
		
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			
			affected.clear();
			hit( ch, (int)(Random.NormalIntRange( min(), max() ) * effectiveness ) );

		} else {

			points[nPoints++] = cell;
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
			
		}
		curUser.sprite.parent.add( new Lightning( points, nPoints, callback ) );
	}
	
	@Override
	public String desc() {
		return
			"This wand conjures forth deadly arcs of electricity, which deal damage " +
			"to several creatures standing close to each other.";
	}
}
