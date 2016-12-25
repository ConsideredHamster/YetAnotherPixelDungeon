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

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.SmokeParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class Explosion {
	
	// Returns true, if this cell is visible
	public static boolean affect( int c, int r, int radius, int damage, Object source ) {

        boolean terrainAffected = false;

        if (Dungeon.visible[c]) {
            CellEmitter.get(c).burst( BlastParticle.FACTORY, 12 / ( r + 1 ) );
            CellEmitter.get(c).burst( SmokeParticle.FACTORY, 6 / ( r + 1 ) );
        }

        if (Level.flammable[c]) {
            Level.set(c, Terrain.EMBERS);
            GameScene.updateMap(c);
            terrainAffected = true;
        }

        Char ch = Actor.findChar(c);
        if (ch != null) {

            int dmg = (damage + Random.IntRange(ch.HT / 2, ch.HT)) / (r + 1);

            if (Bestiary.isBoss(ch)) {
                dmg /= 4;
            } else if ( ch instanceof Hero) {
                dmg /= 2;
            }

            if (dmg > 0) {
                ch.damage(Char.absorb(dmg, ch.armorClass()), source, null);
                if (ch.isAlive()) {
                    Buff.prolong(ch, Stun.class, radius - r + 2);
                }
            }
        }

        Heap heap = Dungeon.level.heaps.get(c);
        if (heap != null) {
            heap.blast("Explosion");
        }

        Dungeon.level.press(c, null);

        return terrainAffected;

	}
}
