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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.DeathRay;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.PurpleParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class WandOfDisintegration extends WandCombat {

	{
		name = "Wand of Disintegration";
        image = ItemSpriteSheet.WAND_DISINTEGRATION;

        hitChars = false;
    }

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.15f;
    }

    private ArrayList<Integer> cells = new ArrayList<Integer>();

	@Override
	protected void onZap( int cell ) {
		
		boolean terrainAffected = false;
		
		for ( int c : cells ) {
			
			Char ch = Actor.findChar( c );

			if ( ch != null ) {
                ch.damage( Char.absorb( damageRoll(), ch.armorClass(), true ), curUser, Element.ENERGY );
                CellEmitter.center( c ).burst( PurpleParticle.BURST, 3 );
			}

            if ( Dungeon.level.map[c] == Terrain.DOOR_CLOSED ) {

                Level.set( c, Terrain.EMBERS );
                GameScene.updateMap( c );
                terrainAffected = true;

                if( Dungeon.visible[ c ] ){
                    CellEmitter.center( c ).burst( PurpleParticle.BURST, 6 );
                }

            } else if ( Dungeon.level.map[c] == Terrain.HIGH_GRASS ) {

                Level.set( c, Terrain.GRASS );
                GameScene.updateMap( c );
                terrainAffected = true;

                if( Dungeon.visible[ c ] ){
                    CellEmitter.center( c ).burst( PurpleParticle.BURST, 4 );
                }

            } else {

                if( Dungeon.visible[ c ] ){
                    CellEmitter.center( c ).burst( PurpleParticle.BURST, 3 );
                }

            }
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}
	}

	@Override
	protected void fx( int cell, Callback callback ) {

        cells = new ArrayList<>( );

        int reflectFrom = Ballistica.trace[ Ballistica.distance ] ;

        curUser.sprite.parent.add( new DeathRay( curUser.pos, reflectFrom ) );

        cells = getCellsFromTrace( cells );

        if( Level.solid[ reflectFrom ] ){

            int reflectTo = getReflectTo( curUser.pos, reflectFrom );

            if( reflectFrom != reflectTo ){

                Ballistica.cast( reflectFrom, reflectTo, true, false );

                reflectTo = Ballistica.trace[ Ballistica.distance ] ;

                curUser.sprite.parent.add( new DeathRay( reflectFrom, reflectTo ) );

                cells = getCellsFromTrace( cells );

            }
        }

		callback.call();
	}

	private int getReflectTo( int sourcePos, int targetPos ) {

        int sourceX = sourcePos % Level.WIDTH;
        int sourceY = sourcePos / Level.WIDTH;

        int targetX = targetPos % Level.WIDTH;
        int targetY = targetPos / Level.WIDTH;

        int reflectX = targetX;
        int reflectY = targetY;

        int deltaX = targetX - sourceX;
        int deltaY = targetY - sourceY;

        // right angles would reflect everything right back at ya so they are ignored
        if( deltaX != 0 && deltaY != 0 ){

            boolean horizontWall = Level.solid[ targetPos - ( deltaX > 0 ? 1 : -1 ) ];
            boolean verticalWall = Level.solid[ targetPos - ( deltaY > 0 ? Level.WIDTH : -Level.WIDTH ) ];

            if( !horizontWall || !verticalWall ) {

                // convex corners reflect in random direction
                boolean reflectHorizontally = horizontWall || ( !verticalWall && Random.Int( 2 ) == 0 );

                if( reflectHorizontally ) {
                    // perform horizontal reflection
                    reflectX += deltaX;
                    reflectY -= deltaY;
                } else {
                    // perform vertical reflection
                    reflectX -= deltaX;
                    reflectY += deltaY;
                }
            } else {

                // concave corners reflect everything by both axes, unless hit from 45 degrees angle
                if( Math.abs( deltaX ) != Math.abs( deltaY ) ){

                    if( deltaX > 0 == deltaY > 0 ){
                        reflectX -= deltaY;
                        reflectY -= deltaX;
                    } else {
                        reflectX += deltaY;
                        reflectY += deltaX;
                    }
                }
            }
        }

        reflectX = GameMath.gate( 0, reflectX, Level.WIDTH );
        reflectY = GameMath.gate( 0, reflectY, Level.HEIGHT );

        return reflectX + reflectY * Level.WIDTH;

    }

	private ArrayList<Integer> getCellsFromTrace( ArrayList<Integer> cells ){

        if( Ballistica.distance > 0 ){

            for( int i = 1 ; i <= Ballistica.distance ; i++ ){

                int cell = Ballistica.trace[ i ];

                cells.add( cell );

            }
        }

        return cells;

    }
	
	@Override
	public String desc() {
		return
			"This wand emits a beam of destructive energy, which pierces all creatures in its way " +
            "and bounce from solid obstacles, allowing its user to sohot them around the corners.";
	}
}
