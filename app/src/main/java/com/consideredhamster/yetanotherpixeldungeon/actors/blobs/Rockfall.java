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
package com.consideredhamster.yetanotherpixeldungeon.actors.blobs;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SmokeParticle;
import com.watabou.utils.Random;

public class Rockfall {

	public static void affect( int pos, int power, Object src ) {

        if( pos < 0 || pos > Level.LENGTH )
            return;

        if( Level.solid[pos] )
            return;

        Char ch = Actor.findChar(pos);
        if (ch != null) {

            int dmg = Char.absorb( Random.IntRange( power / 2 , power ), ch.armorClass() );

            ch.damage(dmg, src, Element.PHYSICAL);

            if ( ch.isAlive() ) {
                BuffActive.addFromDamage(ch, Vertigo.class, dmg);
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.shatter();
        }

        if (Dungeon.visible[pos]) {

            CellEmitter.get(pos).start( Speck.factory(Speck.ROCK), 0.2f, 3 );

        }
	}
}
