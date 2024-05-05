package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.AlphaTweener;

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
public class BossWarning extends Hazard {

    public static final int VAR_CHARGE = 0;
    public static final int VAR_BOMBS = 1;
    public static final int VAR_LASER = 2;
    public static final int VAR_SHOCK = 3;

    public BossWarning() {
        super();

        pos = 0;
        var = 0;

        spriteClass = WarningSprite.class;
    }

    @Override
    public String desc() {
        return "DM-300 has targeted this tile for its next attack.";
    };

    public void setValues( int pos, int var ) {

        this.pos = pos;
        this.var = var;

    }

    @Override
    public void press( int cell, Char ch ) {}

    @Override
    protected boolean act(){
        spend( TICK );
        return true;
    }

    @Override
    public void destroy() {

        ( (BossWarning.WarningSprite) sprite ).disappear();
        super.destroy();

    }

    public static void spawn( int cell, int var ) {

        BossWarning mark = new BossWarning();
        mark.setValues( cell, var );

        GameScene.add( mark );
        ( (BossWarning.WarningSprite) mark.sprite ).appear();

    }

    public static class WarningSprite extends HazardSprite {

        private static float ANIM_TIME = 0.25f;
        private float time;

        public WarningSprite(){

            super();
            time = 0.0f;

        }
        @Override
        public int spritePriority(){
            return 4;
        }

        @Override
        protected String asset(){
            return Assets.HAZ_MARK;
        }

        @Override
        public void update() {
            super.update();

            time += Game.elapsed * 3;

//            tint( 1.2f, 1.2f, 1.0f, 0.2f + (float)Math.sin( time ) * 0.1f );
            speed.polar( time, 1.0f );
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
    }
}
