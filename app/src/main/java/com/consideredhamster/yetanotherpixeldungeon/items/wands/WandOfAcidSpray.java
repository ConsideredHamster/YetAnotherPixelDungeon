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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.AcidParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfAcidSpray extends WandCombat {

    public static final int MAX_DISTANCE = 6;

	{
		name = "Wand of Acid Spray";
        image = ItemSpriteSheet.WAND_ACIDSPRAY;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.10f;
    }

	@Override
	protected void onZap( int cell ) {
        // alas, everything is done in the fx() method
        // maybe it is not the best algorithm, but it works
	}

    @Override
	protected void fx( int cell, Callback callback ) {

        // limit casting distance to avoid having to wait for too long
        if( Ballistica.distance > MAX_DISTANCE ) {
            cell = Ballistica.trace[ Ballistica.distance = MAX_DISTANCE ];
        }

        // always shoot to the initial cell
        HashSet<Integer> targets = new HashSet<>();
        targets.add( Ballistica.cast( curUser.pos, cell, true, true ) );

        // add two missiles when not in the melee range
        if( Ballistica.distance > 1 ) {
            for( int n : Level.NEIGHBOURS8 ) {
                int pos = cell + n;
                if(
                    Ballistica.distance == Level.distance( curUser.pos, pos ) || pos != curUser.pos &&
                    Actor.findChar( pos ) != null && Ballistica.distance > Level.distance( curUser.pos, pos )
                ) {
                    targets.add( Ballistica.cast( curUser.pos, pos, Actor.findChar( pos ) == null, true ) );
                }
            }
        }

        // and two more missiles if distance is large enough
        if( Ballistica.distance > 3 ) {
            for( int n : Level.NEIGHBOURS16 ) {
                int pos = cell + n;

               if( pos >= 0 && pos < Level.LENGTH ){

                   if(  Ballistica.distance == Level.distance( curUser.pos, pos ) ||
                        pos != curUser.pos && Actor.findChar( pos ) != null &&
                        Ballistica.distance > Level.distance( curUser.pos, pos )
                   ){
                       targets.add( Ballistica.cast( curUser.pos, pos, Actor.findChar( pos ) == null, true ) );
                   }
               }
            }
        }

        // calculate trajectories to all of the desired tiles
        onCast( targets, callback );

        // ZAP!
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	private void onCast( final HashSet<Integer> targets, final Callback callback ) {

        // start missile animations
        if( !targets.isEmpty() ){
            for( final int target : targets ){

                // damage is increased by 50% in melee and decreased by 50% at 4-6 tiles of distance

                int distance = Level.distance( curUser.pos, target );

                final int damage = distance < 2 ? damageRoll() * 3 / 2 : distance > 3 ? damageRoll() / 2 : damageRoll() ;

                MagicMissile.acid( curUser.sprite.parent, curUser.pos, target, new Callback() {
                    @Override
                    public void call(){

                        // damage targets and play splash animation on hit
                        onHit( damage, target );

                        // remove missiles which finished their animation
                        targets.remove( target );

                        // if there are no more missiles, resume the game
                        if( targets.isEmpty() ){
                            callback.call();
                        }
                    }
                } );
            }

        } else {

            // just in case, we make sure that the game doesn't hangs up
            callback.call();

        }
    }

    private void onHit( int damage, int cell ) {

        Char ch = Actor.findChar( cell );

        if( ch != null ){
            ch.damage( damage, curUser, Element.ACID );
        }

        if( Dungeon.visible[ cell ] && ( Dungeon.level.solid[ cell ] || ch != null ) ){
            CellEmitter.center( cell ).burst( AcidParticle.BURST, 3 );
//            CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_ACID, false ), 0.05f, 3 );
            Sample.INSTANCE.play( Assets.SND_PUFF, 0.5f, 0.5f, 1.0f );
        }

    }
	
	@Override
	public String desc() {
		return
			"The vile power of this twisted bit of wood will release a torrent of a deadly blight. " +
            "Due to its unfocused nature, this wand is most effective when used at point blank range, " +
            "but can also be used to cover entire groups of enemies in this corrosive sludge.";
	}
}
