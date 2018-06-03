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
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Elemental;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SnowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.watabou.utils.Random;

public class FrigidVapours extends Blob {

    public FrigidVapours() {
        super();

        name = "frigid vapours";
    }

    @Override
    protected void evolve() {
        super.evolve();


        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {

                Char ch = Actor.findChar( i );

                if( ch != null ){
//                    if( ch.buff( Frozen.class ) == null ){
                        BuffActive.add( ch, Frozen.class, TICK * 2 );
//                    } else {
//                        BuffActive.add( ch, Frozen.class, TICK );
//                    }
                }

                Heap heap = Dungeon.level.heaps.get( i );
                if (heap != null) {
                    heap.freeze( TICK );
                }
            }
        }

        Blob blob = Dungeon.level.blobs.get( Fire.class );

        if (blob != null) {

            for (int pos=0; pos < LENGTH; pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    blob.clear( pos );

                }
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( SnowParticle.FACTORY, 0.3f );
    }

    @Override
    public String tileDesc() {
        return "A cloud of freezing vapours is swirling here.";
    }

//
//
//	// Returns true, if this cell is visible
//	public static boolean affect( int cell, int duration, Fire fire ) {
//
//		Char ch = Actor.findChar( cell );
//		if (ch != null) {
//
//            BuffActive.add( ch, Frozen.class, (float)duration );
//
////			Buff.prolong( ch, Frozen.class, duration * ( Level.water[cell] && !ch.flying ? 2 : 1 ) );
//		}
//
//		if (Dungeon.visible[cell]) {
//			CellEmitter.get( cell ).start( SnowParticle.FACTORY, 0.2f, 6 + duration );
//			return true;
//		} else {
//			return false;
//		}
//	}
}
