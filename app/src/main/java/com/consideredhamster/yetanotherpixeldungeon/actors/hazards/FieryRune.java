package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
public class FieryRune extends Hazard {

    private final static int W = Level.WIDTH;
    private final static int MAX_LEVEL = 4;

    public static final int[][] RADIUS = {
        { 0 },
        { +1, -1, +W, -W },
        { +1+W, +1-W, -1+W, -1-W },
        { +2, -2, +W*2, -W*2, +W*2+2, -W*2+2, +W*2-2, -W*2-2 },
        { +2+W, +2-W, -2+W, -2-W, +W*2+1, +W*2-1, -W*2+1, -W*2-1 },
    };

    private int strength;
    private int duration;

    public FieryRune() {
        super();

        this.pos = 0;

        this.strength = 0;
        this.duration = 0;

        spriteClass = RuneSprite.class;
        var = 0;

    }

    @Override
    public String desc() {
        return "There is a Firebrand rune placed here.";
    };

    public void setValues( int pos, int strength, int duration ) {

        this.pos = pos;

        this.strength = strength;
        this.duration = duration;

    }

    @Override
    public boolean act() {

        duration--;

        if( duration > 0 ){
            spend( TICK );
        } else {
            ((FieryRune.RuneSprite)sprite).disappear();
            destroy();
        }

        return true;
    }

    public void press( int cell, Char ch ) {
        if( ch != Dungeon.hero ){
            explode();
        }
    }

    public void upgrade( int strength, int duration ) {

        if( var < MAX_LEVEL ) {

            var++;

            this.strength += strength;
            this.duration += duration;

        } else {

            this.duration += duration;
            GLog.w( "This rune seems to have to reached its limit already. Only duration is increased." );

        }

        ((RuneSprite)sprite).renew();

    }

    public void explode() {

        for( int r = 0 ; r <= var && r < RADIUS.length ; r++ ){

            for( int n : RADIUS [ r ] ){

                int cell = pos + n;

                if( cell == pos || Ballistica.cast( pos, cell, false, true ) == cell ){

                    Char ch = Actor.findChar( cell );
                    if( ch != null ){
                        ch.damage( ch != Dungeon.hero ? strength : strength / 2, this, Element.FLAME );
                    }

                    Heap heap = Dungeon.level.heaps.get( cell );
                    if (heap != null) {
                        heap.burn();
                    }

                    if( Level.flammable[ cell ] ){
                        GameScene.add( Blob.seed( cell, 3, Fire.class ) );
                    }

                    if( Dungeon.visible[ cell ] ){
                        CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 4 );
                        CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 6 );
                    }

                }
            }
        }

        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );
        Camera.main.shake( 1 + var, 0.1f + var * 0.1f );

        ((FieryRune.RuneSprite)sprite).explode();
        destroy();
    }


    private static final String STRENGTH = "strength";
    private static final String DURATION = "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( STRENGTH, strength );
        bundle.put( DURATION, duration );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        strength = bundle.getInt( STRENGTH );
        duration = bundle.getInt( DURATION );

    }

    public static class RuneSprite extends HazardSprite {

        private static float ANIM_TIME = 0.25f;

        private float time;
        protected Emitter burning;

        public RuneSprite(){

            super();
            time = 0.0f;

        }

        @Override
        protected String asset(){
            return Assets.HAZ_RUNE;
        }

        @Override
        public int spritePriority(){
            return 3;
        }

        @Override
        public void link( Hazard hazard ) {

            super.link( hazard );

            burning = GameScene.emitter();

            if( burning != null ){
                burning.pos( this );
                burning.pour( FlameParticle.FACTORY, 0.6f );
            }

            parent.add( burning );
        }

        @Override
        public void update() {
            super.update();

            time += Game.elapsed * 3;

            tint( 1.2f, 1.2f, 1.0f, 0.2f + (float)Math.sin( time ) * 0.1f );
            speed.polar( time, 1.0f );

            if (burning != null) {
                burning.visible = visible;
            }

        }

        public void appear( ) {

            am = 0.0f;

            parent.add(new AlphaTweener( this, 1.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }

        public void disappear( ) {

            if (burning != null) {
                burning.on = false;
                burning = null;
            }

            parent.add(new AlphaTweener( this, 0.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }

        public void renew( ) {

            parent.add(new AlphaTweener( this, 0.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    changeFrame( hazard.var );
                    appear();
                }
            });
        }

        public void explode() {

            if (burning != null) {
                burning.on = false;
                burning = null;
            }

            parent.add(new ScaleTweener( this, new PointF(2, 2), ANIM_TIME ) {
                @Override
                protected void onComplete() {

                    RuneSprite.this.killAndErase();
                    parent.erase( this );

                }

                @Override
                protected void updateValues(float progress) {
                    super.updateValues(progress);
                    am = 1 - progress;
                }
            });
        }
    }
}
