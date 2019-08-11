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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Elemental;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.RainParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Thunderstorm extends Blob {

    public Thunderstorm() {
        super();

        name = "thunderstorm";
    }

	@Override
	protected void evolve() {

        super.evolve();

        if (volume > 0) {

            boolean mapUpdated = false;

            Blob blob = Dungeon.level.blobs.get( Fire.class );

            if (blob != null) {

                int par[] = blob.cur;

                for (int i=0; i < LENGTH; i++) {

                    if (cur[i] > 0) {
                        blob.volume -= par[i];
                        par[i] = 0;
                    }
                }
            }

            int count = 0;
            int humidity[] = new int[LENGTH];

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {

                    humidity[i] = 2;

                    for (int n : Level.NEIGHBOURS8) {
                        if (Dungeon.level.map[i + n] == Terrain.WATER) {
                            humidity[i]++;
                        }
                    }
                }
            }

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {

                    if (Random.Int(20) < humidity[i]
                            && !Level.water[i]
                            && !Level.important[i]
                            && !Level.solid[i]
                            && !Level.chasm[i]
                            ) {

                        int oldTile = Dungeon.level.map[i];
                        Level.set(i, Terrain.WATER);

//                        if (Dungeon.visible[i]) {
                            mapUpdated = true;
//                            GameScene.updateMap( i );
                            GameScene.discoverTile( i, oldTile );
//                        }
                    }

                    // attempt to fill the wells
                    if (
                        Random.Int(20) < humidity[i] &&
                        ( Dungeon.level.map[i] == Terrain.WELL ||
                        Dungeon.level.map[i] == Terrain.EMPTY_WELL )
                    ) {
                        fill( i );
                    }

                    Char ch = Actor.findChar(i);

                    if (ch != null) {
                        if( ch instanceof Elemental ) {
                            ch.damage( Random.IntRange( 1, (int)Math.sqrt( ch.HT / 2 + 1 ) ), this, null );
                        } else {
                            Buff.detach(ch, Burning.class);
                            Buff.detach(ch, Corrosion.class);
                        }
                    }

                    count++;

                }
            }

            if (mapUpdated) {
                GameScene.updateMap();
                Dungeon.observe();
            }

            int amount = count / 5;

            boolean viewed = false;
            boolean heared = false;
            boolean affected[] = new boolean[LENGTH];

            for (int i=0; i < amount; i++) {

                int cell = Random.Int( LENGTH );

                if( cur[cell] > 0 && !affected[cell] && !Level.solid[cell] && !Level.chasm[cell] ) {

                    amount--;

                    thunderstrike( cell, this );

                    for( int n : Level.NEIGHBOURS9 ) {
                        affected[ cell + n ] = true;
                    }

                    if( Dungeon.visible[ cell ] ) {

                        viewed = true;

                    } else if ( Level.distance( cell, Dungeon.hero.pos ) <= 4 ) {

                        heared = true;

                    }
                }
            }

            if ( viewed && Dungeon.hero.isAlive() ) {

                viewed();

            } else if ( heared && Dungeon.hero.isAlive() ) {

                listen();

            }
        }
	}

    public static void viewed() {
        GameScene.flash(0x888888);
        Sample.INSTANCE.play(Assets.SND_BLAST);
        Camera.main.shake(3, 0.3f);
        Dungeon.hero.interrupt();
    }

    public static void listen() {
        GLog.i("You hear thunder somewhere not far away.");
        Sample.INSTANCE.play(Assets.SND_BLAST, 1, 1, Random.Float(1.8f, 2.25f));
        Camera.main.shake(2, 0.2f);
    }

    public static void thunderstrike( int cell, Blob blob ) {

        Emitter emitter = CellEmitter.get( cell );

        int[] tiles = Random.Int( 2 ) == 0 ? Level.NEIGHBOURS5 : Level.NEIGHBOURSX ;

        for( int n : tiles ) {

            Char ch = Actor.findChar( cell + n );

            if (ch != null) {

                int power = Random.IntRange(ch.HT / 3, ch.HT * 2 / 3);

                if( Bestiary.isBoss(ch) ) {
                    power = power / 4;
                } else if ( ch instanceof Hero ) {
                    power = power / 2;
                }


                ch.damage(n == 0 ? power : power / 2, blob, Element.SHOCK);

            }

            if( Dungeon.visible[ cell + n ] ) {
                emitter.parent.add( new Lightning( cell, cell + n ) );
            }

        }

        if( Dungeon.visible[ cell ] ){
            emitter.burst( SparkParticle.FACTORY, Random.Int( 4, 6 ) );
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= 4 ) {
                mob.beckon(cell);
            }
        }
    }

    public void fill( int cell ) {

        if( Dungeon.depth == 24 )
            return;

        WellWater water = (WellWater)Dungeon.level.blobs.get( WellWater.class );
        if (water == null) {
            try {
                water = new WellWater();
            } catch (Exception e) {
                water = null;
            }
        }

        water.seed( cell, water.volume + 1 );

        if ( Dungeon.level.map[ cell ] == Terrain.EMPTY_WELL) {

            Level.set( cell, Terrain.WELL );
            GameScene.updateMap( cell );

        }

        if( Dungeon.visible[ cell ] ) {
            GLog.i("Rain fills the well!");
        }
    }
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use(emitter);
		emitter.start( RainParticle.FACTORY, 0.5f, 0 );

	}
	
	@Override
	public String tileDesc() {
		return "Storm clouds fly under the ceiling here, raining down water and, occasionally, lightning.";
	}
}
