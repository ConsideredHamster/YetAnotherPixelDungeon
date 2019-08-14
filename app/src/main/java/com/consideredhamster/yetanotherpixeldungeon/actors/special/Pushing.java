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
package com.consideredhamster.yetanotherpixeldungeon.actors.special;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Pushing extends Actor {

    private static final float SPEED  = 240f;


	private CharSprite sprite;
	private int from;
	private int to;
	
//	private Effect effect;
	private Callback callback;

	public Pushing( Char ch, int from, int to ) {
        this( ch, from, to, null );
    }

	public Pushing( Char ch, int from, int to, Callback callback ) {

		sprite = ch.sprite;

		this.callback = callback;
		this.from = from;
		this.to = to;

	}
	
	@Override
	protected boolean act() {

		if (sprite != null) {

                PointF dest = sprite.worldToCamera( to );
                PointF d = PointF.diff( sprite.worldToCamera( from ), dest );

                PosTweener tweener = new PosTweener( sprite, dest, d.length() / SPEED );

                tweener.listener = new Tweener.Listener() {
                    @Override
                    public void onComplete( Tweener tweener ){
                        Actor.remove( Pushing.this );

                        if( callback != null ){
                            callback.call();
                        }

                        next();
                    }
                };

                sprite.parent.add( tweener );

			return false;

		} else {

			Actor.remove( Pushing.this );
			return true;
		}
	}

	public static void move( final Char ch, final int newPos, final Callback callback ) {

        // moved this method here to avoid repeatng the same pieces of code over and over
        // it is still not the most elegant
        Actor.addDelayed( new Pushing( ch, ch.pos, newPos, new Callback() {

            @Override
            public void call(){
            if( callback != null ){
                callback.call();
            }

            }

        }), -1 );

        // change target's positions immediately, but only after the animation started
        ch.pos = newPos;
    }

	public static void knockback( final Char ch, int pushFrom, int distance, final int damage ) {

        // resistance roughly halves knockback distance
        // vulnerability increases it roughly by half
        // immunity means that target simply receives damage

        distance = Element.Resist.modifyValue( distance, ch, Element.KNOCKBACK );

        if( distance > 0 ){

            // first, we "remove" target from the tilemap and check where it
            // should land when knocked back, as if it weren't there
            Actor.freeCell( ch.pos );
            int pushTo = Ballistica.cast( pushFrom, ch.pos, true, true );

            // then we calcualte where the targer would actually land, considering
            // the maximum distance which it is supposed to be knocked back
            Ballistica.cast( ch.pos, pushTo, true, true );
            Ballistica.distance = Math.min( Ballistica.distance, distance );

            if( Ballistica.distance > 0 ){

                // gotta make those final for the sake of using callback mechanics
                final int newPos = Ballistica.trace[ Ballistica.distance ];
                final Char pushedInto = Char.findChar( newPos );

                // apply visual effect of moving, with all of the important stuff
                // happening only when the knockback animation is finished
                move( ch, newPos, new Callback() {

                    @Override
                    public void call(){

                        // if target was pushed into a wall or another char, we damage/confuse
                        // it and move it back by a single tile of distance
                        if( pushedInto != null || Level.solid[ newPos ] ){
                            hitObstacle( ch, Ballistica.trace[ Ballistica.distance - 1 ], damage );
                        }


                        if( ch.isAlive() ){

                            // mobs get waken up by this effect  (beckon() is not the best way to
                            // do that but it works), but also get delayed for a turn to make the
                            // knockback thing actually matter on short distances
                            if( ch instanceof Mob ){
                                ( (Mob) ch ).beckon( ch.pos );
                                ch.delay( 1f );
                            }

                            // apply target's current position and activate traps there
                            // gotta re-check whether mobs killed by knockback activate traps or not
                            Actor.occupyCell( ch );
                            Dungeon.level.press( ch.pos, ch );
                        }

                        // if we knock our target mobs into another one, then apply knockback to this
                        // one as well - but only for one tile of distance because I don't know any better
                        if( pushedInto != null ){
                            knockback( pushedInto, ch.pos, 1, damage / 2 );
                        }

                    }
                } );

            }

        } else {

            // if the target is immovable, then just deal damage straight up
            // don't wanna this wand to be useless against Yog, for instance
            dealDamage( ch, damage );

        }
    }

    private static void hitObstacle( final Char ch, int pushTo, final int damage ) {

        // move() method handles changing the target's position value
        move( ch, pushTo, null );

        // deal damage, apply confusion debuff, usual stuff
        dealDamage( ch, damage );

        // attract nearby mobs because hitting a wall at 100500 mph would be kinda noisy
        for (Mob mob : Dungeon.level.mobs) {
            if ( ch != mob && Level.distance( ch.pos, mob.pos ) <= 4 ) {
                mob.beckon( ch.pos );
            }
        }

        // make sounds and shake the screen to make this effect meatier
        if( Dungeon.visible[ ch.pos ] ) {
            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.5f );
            Camera.main.shake( 2, 0.1f );
        }

    }

    private static void dealDamage( Char ch, int damage ) {

        if( damage > 0 ){

            int dmg = Char.absorb( damage, ch.armorClass() );

            ch.damage( dmg, null, Element.PHYSICAL );

            if( ch.isAlive() ) {
                BuffActive.addFromDamage( ch, Vertigo.class, damage );
            }
        }
    }
}
