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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.DeathRay;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.PurpleParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;

public class WandOfDisintegration extends Wand {

	{
		name = "Wand of Disintegration";
        shortName = "Di";
		hitChars = false;
	}

    @Override
    public int max() {
        return 25;
    }

    @Override
    public int min() {
        return 15;
    }
	
	@Override
	protected void onZap( int cell ) {
		
		boolean terrainAffected = false;

		Ballistica.distance = Math.min( Ballistica.distance - 1, distance() );
		
		ArrayList<Char> chars = new ArrayList<Char>();
		
		for (int i=1; i <= Ballistica.distance; i++) {
			
			int c = Ballistica.trace[i];
			
			Char ch = Actor.findChar( c );
			if ( ch != null ) {

                if( Char.hit( curUser, ch, false, true ) ) {

                    chars.add( ch );

                } else {

                    ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());

                }
			}

//            Heap heap = Dungeon.level.heaps.get( c );
//            if (heap != null) {
//                heap.disintegrate( true );
//            }

//			if (Level.flammable[c]) {
//
//				Level.set( c, Terrain.EMBERS );
//				GameScene.updateMap( c );
//				terrainAffected = true;
//
//			}

            if (Dungeon.level.map[c] == Terrain.DOOR_CLOSED) {

                Level.set( c, Terrain.EMBERS );
                GameScene.updateMap( c );
                terrainAffected = true;

            } else if (Dungeon.level.map[c] == Terrain.HIGH_GRASS ) {

                Level.set( c, Terrain.GRASS );
                GameScene.updateMap( c );
                terrainAffected = true;

            }

            CellEmitter.center( c ).burst( PurpleParticle.BURST, terrainAffected ? Random.IntRange( 6, 8 ) : Random.IntRange( 3, 5 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}

//		int lvl = bonus + chars.size();
//		int dmgMin = bonus / 2;
//		int dmgMax = 10 + bonus;

		for (Char ch : chars) {

            ch.damage( damageRoll(), curUser, DamageType.ENERGY );

		}
	}

	private int distance() {
//		return 4 + bonus / 5 ;
		return 8 ;
	}

	@Override
	protected void fx( int cell, Callback callback ) {
		
		cell = Ballistica.trace[Math.min( Ballistica.distance - 1, distance() )];
		curUser.sprite.parent.add( new DeathRay( curUser.sprite.center(), DungeonTilemap.tileCenterToWorld( cell ) ) );
		callback.call();
	}
	
	@Override
	public String desc() {
		return
			"This wand emits a beam of destructive energy, which pierces all creatures in its way. " +
			"The more targets it hits, the less damage it inflicts to each of them. The distance of wand " +
            "is limited, however.";
	}
}
