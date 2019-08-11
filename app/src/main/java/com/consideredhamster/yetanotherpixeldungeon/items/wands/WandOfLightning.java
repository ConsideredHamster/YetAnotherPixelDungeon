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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.PurpleParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfLightning extends WandCombat {

    private final static int MAX_DISTANCE = 16;

	{
		name = "Wand of Lightning";
        image = ItemSpriteSheet.WAND_LIGHTNING;

        goThrough = false;
    }

    private HashSet<Char> targets;

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.20f;
    }
	
	@Override
	protected void onZap( int cell ) {

        int size = targets.size();

        if( !targets.isEmpty() ){
            for( Char target : targets ) {

                // first target receivese full damage, everyone else receives partial damage
                // total damage goes like 150% for 2 targets, 167% for 3 targets, 175% for 4 and etc.
                target.damage(
                    target.pos == cell ? damageRoll() :
                    (int)Math.ceil( damageRoll() / size ),
                    curUser, Element.SHOCK
                );

//                if( cell != target.pos && target != curUser ){
//                    curUser.sprite.parent.add( new Lightning( cell, target.pos ) );
//                }

                if( Dungeon.visible[ target.pos ] ){
                    CellEmitter.center( target.pos ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
                }
            }
        }

	}
	
	@Override
	protected void fx( int cell, Callback callback ) {

        targets = new HashSet<>();

        Char ch = Actor.findChar( cell );

        if( ch != null ){

            //starting cell is always included
            targets.add( ch );
//            ch.damage( damageRoll(), curUser, Element.SHOCK );

        }

        if( Level.water[ cell ] && ( ch == null || !ch.flying ) ){

            // check for other non-flying mobs in the same pool of water

            PathFinder.buildDistanceMap( cell, Level.water, MAX_DISTANCE );

            for( int c = 0 ; c < Level.LENGTH ; c++ ){

                if( PathFinder.distance[ c ] < Integer.MAX_VALUE ){

                    // highlight affected water tiles
                    GameScene.electrify( c );

                    if( Dungeon.visible[ c ] ){
                        CellEmitter.get( c ).burst( SparkParticle.FACTORY, Random.IntRange( 2, 4 ) );
                    }

                    if( ( ch = Actor.findChar( c ) ) != null && !ch.flying && !targets.contains( ch ) ){
                        targets.add( ch );
                    }
                }
            }
        }

        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
        curUser.sprite.parent.add( new Lightning( curUser.pos, cell ) );
        callback.call();

	}

//  sadly, forking was a nifty mechanic, but ended up as either too OP or too unpredictable

//    private static void fork( HashSet<Mob> targets, Char forkFrom, Integer damage ) {
//
//        // this wand inflicts more damage when target is in water
//        forkFrom.damage(
//            !forkFrom.flying && Level.water[ forkFrom.pos ] ?
//            damage * 3 / 2 : damage, curUser, Element.SHOCK
//        );
//
//        // we do not want our arcs dealing only 0 damage on forking
//        if( damage > 1 ){
//
//            ArrayList<Char> nearby = new ArrayList<>();
//
//            for( Char forkTo : Dungeon.level.mobs ){
//                if(
//                    // checking valid targets
//                    targets.contains( forkTo )
//                    // forking distance is 2 tiles or less
//                    && Level.distance( forkFrom.pos, forkTo.pos ) < 2
//                    // we do not fork to targets which are closer to the source than the current target
//                    && Level.distance( curUser.pos, forkFrom.pos ) <= Level.distance( curUser.pos, forkTo.pos )
//                    // checking whether we can hit that target or not
//                    && Ballistica.cast( forkFrom.pos, forkTo.pos, false, true ) == forkTo.pos
//                ){
//
//                    // we are moving recursive part from here so our lightnings would fork more often
//                    targets.remove( forkTo );
//                    nearby.add( forkTo );
//
//                }
//            }
//
//            int iteration = 0;
//
//            for( Char mob : nearby ){
//
//                // here we can control amount of targets the lightning can fork to
//                if( iteration <= Random.Int( 3 ) ){
//
//                    iteration++;
//
//                    fork( targets, mob, damage * 100 / Random.IntRange( 100, 200 ) );
//                    curUser.sprite.parent.add( new Lightning( forkFrom.pos, mob.pos, null ) );
//                    mob.sprite.centerEmitter().burst( SparkParticle.FACTORY, Random.Int( 3, 5 ) );
//
//                } else {
//                    break;
//                }
//            }
//        }
//
//    }
	
	@Override
	public String desc() {
		return
			"This wand conjures forth deadly arcs of electricity, roasting its target with a high " +
            "voltage zap. Effects of this wand can be transmitted by water, so it should be used " +
            "with a certain amount of care - mind to not stand in the same pool as your enemy!";
	}
}
