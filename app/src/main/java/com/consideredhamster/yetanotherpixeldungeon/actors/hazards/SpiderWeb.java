package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.WebParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Bundle;
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
public class SpiderWeb extends Hazard {

    private int duration;

    public SpiderWeb() {
        super();

        this.pos = 0;
        this.duration = 0;

        spriteClass = WebbingSprite.class;
        var = Random.Int( 4 );

    }

    @Override
    public String desc() {
        return "There is a bunch of webs covering this tile.";
    };

    public void setValues( int pos, int duration ) {

        this.pos = pos;
        this.duration = duration;

    }

    @Override
    public void press( int cell, Char ch ){

        if( ch != null && !ch.flying ){

            BuffActive.add( ch, Ensnared.class, duration );

            CellEmitter.center( cell ).burst(
                    Speck.factory( Speck.COBWEB ), 4
            );

            ((SpiderWeb.WebbingSprite)sprite).trigger();
            destroy();

        }
    }

    @Override
    protected boolean act(){

        duration--;

        if( duration > 0 ){
            spend( TICK );
        } else {
            ((SpiderWeb.WebbingSprite)sprite).disappear();
            destroy();
        }

        return true;
    }

    public static void spawn( int cell, int duration ) {

        SpiderWeb web = Hazard.findHazard( cell, SpiderWeb.class );

        if( web == null ){

            web = new SpiderWeb();
            web.setValues( cell, duration );

            GameScene.add( web );
            ( (SpiderWeb.WebbingSprite) web.sprite ).appear();

        } else {

            web.duration += duration;

        }
    }


    private static final String DURATION = "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        bundle.put( DURATION, duration );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );
        duration = bundle.getInt( DURATION );

    }

    public static class WebbingSprite extends HazardSprite {

        private static float ANIM_TIME = 1.0f;

        private float time;

        public WebbingSprite(){

            super();
            time = 0.0f;

        }

        @Override
        protected String asset(){
            return Assets.HAZ_WEBS;
        }

        @Override
        public int spritePriority(){
            return 2;
        }

        @Override
        public void update() {
            super.update();

            time += Game.elapsed;
            scale.set( 0.95f + (float)Math.sin( time ) * 0.05f );

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

            parent.add(new AlphaTweener( this, 0.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }

        public void trigger() {

            parent.add(new ScaleTweener( this, new PointF(2, 2), ANIM_TIME ) {
                @Override
                protected void onComplete() {

                    WebbingSprite.this.killAndErase();
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
