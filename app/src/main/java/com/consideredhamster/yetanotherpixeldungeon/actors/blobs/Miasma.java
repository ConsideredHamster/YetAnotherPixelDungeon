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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Suffocation;
import com.consideredhamster.yetanotherpixeldungeon.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.GooSprite;

public class Miasma extends Blob {

    public Miasma() {
        super();

        name = "cloud of miasma";
    }

	@Override
	protected void evolve() {
		super.evolve();

		Char ch;
		for (int i=0; i < LENGTH; i++) {
			if (cur[i] > 0 && (ch = Actor.findChar( i )) != null) {

                if( !ch.immunities().contains( DamageType.Body.class ) ) {

                    if (ch.buff(Suffocation.class) == null) {
                        Buff.prolong(ch, Suffocation.class, TICK).apply((int) (Math.sqrt(ch.HT) / 2), this);
                    } else {
                        Buff.prolong(ch, Suffocation.class, TICK).proliferate(this);
                    }

                }

                if ( ch.buff( Burning.class ) != null) {
                    GameScene.add(Blob.seed(ch.pos, 2, Fire.class));
                }
			}
		}

        Blob blob = Dungeon.level.blobs.get( Fire.class );
        if (blob != null) {

            for (int pos=0; pos < LENGTH; pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    int flammability = 0;

                    for (int n : Level.NEIGHBOURS8) {
                        if ( blob.cur[ pos + n ] > 0 ) {
                            flammability++;
                        }
                    }

                    if( Random.Int( 4 ) < flammability ) {

                        blob.volume += ( blob.cur[ pos ] = 2 );

                        volume -= ( cur[pos] / 2 );
                        cur[pos] -=( cur[pos] / 2 );

                    }

                }
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( GooSprite.GooParticle.FACTORY, 0.04f );
	}
	
	@Override
	public String tileDesc() {
		return "A blackish cloud of suffocating miasma is swirling here.";
	}
	
//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromGas();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
//		GLog.n( "You died from a toxic gas.." );
//	}
}
