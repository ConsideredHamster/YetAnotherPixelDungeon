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
package com.consideredhamster.yetanotherpixeldungeon.visuals.sprites;

import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;

public class SuccubusSprite extends MobSprite {

    private int cellToAttack;

	public SuccubusSprite() {
		super();
		
		texture( Assets.SUCCUBUS );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 12, 13, 14, 1 );
		
		run = new Animation( 15, true );
		run.frames( frames, 3, 4, 5, 6, 7, 8 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 9, 10, 11 );
		
		cast = new Animation( 10, false );
		cast.frames( frames, 12, 13, 14 );
		
		die = new Animation( 10, false );
		die.frames( frames, 15 );
		
		play( idle );
	}

//    @Override
//    public void attack( int cell ) {
//        if (!Level.adjacent(cell, ch.pos)) {
//
//            cellToAttack = cell;
//            turnTo( ch.pos , cell );
//            play(cast);
//
//        } else {
//
//            super.attack( cell );
//
//        }
//    }

//    @Override
//    public void onComplete( Animation anim ) {
//        if (anim == cast) {
//
//            Sample.INSTANCE.play( Assets.SND_ZAP );
//            MagicMissile.purpleLight(parent, ch.pos, cellToAttack,
//                new Callback() {
//                    @Override
//                    public void call() {
//                        ((Succubus)ch).onZapComplete();
//                    }
//                });
//
//            idle();
//
//        } else {
//            super.onComplete( anim );
//        }
//    }
	
	@Override
	public void die() {
		super.die();
		emitter().burst( Speck.factory( Speck.HEART ), 6 );
		emitter().burst( ShadowParticle.UP, 8 );
	}
}
