package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.ConfusionGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Darkness;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Explosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.FrigidVapours;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.ToxicGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.CavesBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SmokeParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SnowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GooSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

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
public class BombHazard extends Hazard {

    public static final int BOMB_STICK = 0;
    public static final int BOMB_BUNCH = 1;
    public static final int BOMB_ROUND = 2;
    public static final int BOMB_NINJA = 3;

    private int strength;
    private int distance;

    public BombHazard() {
        super();

        this.pos = 0;

        this.strength = 0;
        this.distance = 0;

        spriteClass = BombSprite.class;
        var = 0;

    }

    @Override
    public String desc() {
        return "There is a bomb lying here, ready to explode.";
    };

    public void setValues( int pos, int var, int strength, int distance ) {

        this.pos = pos;
        this.var = var;

        this.strength = strength;
        this.distance = distance;

        spend( TICK );

    }

    @Override
    public void press( int cell, Char ch ){
        // Do nothing
    }

    @Override
    protected boolean act(){
    
        explode( pos, strength, distance, var );
    
        if( var == BOMB_ROUND ){

            GameScene.add( Blob.seed( pos, 3, Fire.class ) );
        
            for( int n : Level.NEIGHBOURS8 ){
                if( Level.flammable[ pos + n ] || !Level.water[ pos + n ] &&
                        Level.passable[ pos + n ] && Random.Int( 2 ) == 0 ){
                    GameScene.add( Blob.seed( pos + n, 3, Fire.class ) );
                }
            }

        } else if( var == BOMB_NINJA ) {
            switch( strength ) {
                case 1:
                    GameScene.add( Blob.seed( pos, 50, Darkness.class ) );
                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( Speck.factory( Speck.DARKNESS ), 6 );
                    }
                    break;

                case 2:
                    GameScene.add( Blob.seed( pos, 50, ConfusionGas.class ) );
                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( Speck.factory( Speck.CONFUSION, true ), 6 );
                    }
                    break;

                case 3:
                    GameScene.add( Blob.seed( pos, 50, ToxicGas.class ) );
                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( Speck.factory( Speck.TOXIC, true ), 6 );
                    }
                    break;
                case 4:
                    GameScene.add( Blob.seed( pos, 50, FrigidVapours.class ) );
                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( SnowParticle.FACTORY, 10 );
                    }
                    break;
                case 5:
                    GameScene.add( Blob.seed( pos, 50, Miasma.class ) );
                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( GooSprite.GooParticle.FACTORY, 10 );
                    }
                    break;
                case 6:
                    GameScene.add( Blob.seed( pos, 2, Fire.class ) );

                    for( int n : Level.NEIGHBOURS8 ){
                        if( Level.flammable[ pos + n ] || !Level.water[ pos + n ] &&
                                Level.passable[ pos + n ] && Random.Int( 2 ) == 0 ){
                            GameScene.add( Blob.seed( pos + n, 2, Fire.class ) );
                        }
                    }

                    if ( Dungeon.visible[ pos ] ) {
                        CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 6 );
                    }
                    break;
            }


        }

        if ( Dungeon.visible[ pos ] ) {
            CellEmitter.get( pos ).burst( SmokeParticle.FACTORY, 6 );
        }
        ((BombHazard.BombSprite)sprite).disappear();
        destroy();

        return true;
    }
    
    public static void explode( int pos, int strength, int distance, int var ) {

        if( distance > 0 ) {

            PathFinder.buildDistanceMap(pos, BArray.not(Level.losBlockHigh, null), distance);

            Blob[] blobs = {
                    Dungeon.level.blobs.get(Fire.class),
                    Dungeon.level.blobs.get(ToxicGas.class),
                    Dungeon.level.blobs.get(ConfusionGas.class),
            };

            boolean terrainAffected = false;

            for (int c = 0; c < Level.LENGTH; c++) {

                if (PathFinder.distance[c] < Integer.MAX_VALUE) {

                    int r = PathFinder.distance[c];

                    terrainAffected = Explosion.affect(c, r, distance, strength, null) || terrainAffected;

                    for (Blob blob : blobs) {

                        if (blob == null) {
                            continue;
                        }

                        int value = blob.cur[c];
                        if (value > 0) {

                            blob.cur[c] = 0;
                            blob.volume -= value;
                        }
                    }
                }
            }

            for (int n : Level.NEIGHBOURS9) {

                int c = pos + n;

                if (Level.flammable[c]) {
                    Level.set(c, Terrain.EMBERS);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }

                if (var == BOMB_ROUND && c != ((CavesBossLevel) Dungeon.level).arenaDoor &&
                        (Dungeon.level.map[c] == Terrain.STATUE || Dungeon.level.map[c] == Terrain.GRATE)
                ) {
                    Level.set(c, Terrain.EMPTY_DECO);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }

//            Char ch = Actor.findChar(c);
//
//            if( n != 0 && ch != null ) {
//
//                Pushing.knockback( ch, pos, distance, strength / 2 );
//
//            }
            }

            if (terrainAffected) {
                Dungeon.observe();
            }

            for (Mob mob : Dungeon.level.mobs) {
                if (Level.distance(pos, mob.pos) <= (4 + 2 * distance)) {
                    mob.beckon(pos);
                }
            }
        }
    
        Sample.INSTANCE.play( Assets.SND_BLAST, 1 + distance );
        Camera.main.shake( 3 + distance, 0.2f + distance * 0.1f );
    }

    private static final String STRENGTH = "strength";
    private static final String DISTANCE = "distance";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( STRENGTH, strength );
        bundle.put( DISTANCE, distance );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        strength = bundle.getInt( STRENGTH );
        distance = bundle.getInt( DISTANCE );

    }

    public static class BombSprite extends HazardSprite {

        private final static float ANIM_TIME = 0.4f;

        private float time;
        private float dropInterval;

        public BombSprite(){

            super();
            time = 0.0f;

        }

        @Override
        protected String asset(){
            return Assets.HAZ_BOMB;
        }

        @Override
        public int spritePriority(){
            return 3;
        }

        @Override
        public void update() {
            super.update();

            if ( dropInterval > 0 ) {

                if ( (dropInterval -= Game.elapsed) <= 0 ){

                    speed.set( 0 );
                    acc.set( 0 );
                    place( hazard.pos );

                    if( visible ){
                        if( Level.water[ hazard.pos ] ){
                            GameScene.ripple( hazard.pos );
                        }
                    }
                }
            } else {
                time += Game.elapsed * 2;
                scale.set( 0.95f + (float)Math.sin( time ) * 0.05f );
            }

        }

        public void appear( ) {

            dropInterval = ANIM_TIME;

            speed.set( 0, -100 );
            acc.set( 0, -speed.y / ANIM_TIME * 2 );

        }

        public void disappear( ) {
            parent.add(new AlphaTweener( this, 0.0f, 0.0f ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }
    }
}
