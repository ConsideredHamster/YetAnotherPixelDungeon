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
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;

public class WraithSprite extends MobSprite {

    private Animation blink;

	public WraithSprite() {
		super();
		
		texture( Assets.WRAITH );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );
		
		idle = new Animation( 5, true );
		idle.frames(frames, 0, 1);
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1 );

        blink = new Animation( 15, false );
        blink.frames( frames, 7, 6, 5, 4, 0 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 0, 2, 3 );

//        cast = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 0, 4, 5, 6, 7 );
		
		play( idle );
	}


//    public void cast(int cell) {
//
//        turnTo( ch.pos , cell );
//        play(cast);
//
//        MagicMissile.shadow(parent, ch.pos, cell,
//                new Callback() {
//                    @Override
//                    public void call() {
//                        ((Wraith) ch).onZapComplete();
//                    }
//                });
//        Sample.INSTANCE.play(Assets.SND_ZAP);
//    }


    @Override
    public void onComplete( Animation anim ) {
        if (anim == blink) {
            isMoving = false;
            idle();
//        } else if (anim == cast) {
//            idle();
        }
        super.onComplete(anim);
    }

    @Override
    public void die() {
        super.die();
        if (Dungeon.visible[ch.pos]) {
            emitter().burst( ShadowParticle.CURSE, 10 );
        }
    }

    public void blink( int from, int to ) {

        place( to );

        play( blink );
        turnTo( from , to );

        isMoving = true;

        ch.onMotionComplete();
    }
	
	@Override
	public int blood() {
		return 0x88000000;
	}
}
