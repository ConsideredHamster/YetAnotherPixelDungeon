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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfAvalanche extends Wand {

	{
		name = "Wand of Avalanche";
        shortName = "Av";
//		hitChars = false;
	}

    @Override
    public int max() {
        return 35;
    }

    @Override
    public int min() {
        return 20;
    }

    @Override
	protected void onZap( int cell ) {

        float effectiveness = effectiveness();
        int size = 2;

        Sample.INSTANCE.play( Assets.SND_ROCKS );
        Camera.main.shake(3, 0.07f * (3 + size));

        PathFinder.buildDistanceMap( cell, BArray.not( Level.solid, null ), size );
        Ballistica.distance = Math.min( Ballistica.distance, 2 );

		for (int i=0; i < Level.LENGTH; i++) {
			
			int d = PathFinder.distance[i];
			
			if ( d < Integer.MAX_VALUE ) {

                boolean wall = false;

                for(int n : Level.NEIGHBOURS4) {
                    if( Level.solid[ i + n ] ) {
                        wall = true;
                    }
                }

                if( wall || Random.Int( d * 2 + 1 ) == 0) {

                    Char ch = Actor.findChar(i);
                    if (ch != null ) {

                        if( Char.hit( curUser, ch, false, true ) ) {

                            int dmg = Char.absorb( damageRoll() / ( d + 1 ), ch.armorClass() );
//                            int dmg = Random.NormalIntRange(power + size - d, 5 + power + size * 5 - d * 5) - Random.NormalIntRange(0, ch.armorClass());

                            ch.sprite.flash();

                            ch.damage(dmg, curUser, Element.PHYSICAL);

                            if (ch.isAlive() ) {
                                BuffActive.addFromDamage(ch, Vertigo.class, dmg);
                            }

                        } else {

                            ch.missed();

                        }
                    }

                    Heap heap = Dungeon.level.heaps.get( i );
                    if (heap != null) {
                        heap.shatter( "Your wand" );
                    }

                    Dungeon.level.press(i, null);

                    CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.1f, 3 + (size - d));
                }
			}


		}

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= 6 ) {
                mob.beckon( cell );
            }
        }
		
//		if (!curUser.isAlive()) {
//			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
//			GLog.n( "You killed yourself with your own Wand of Avalanche..." );
//		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.earth( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"When a discharge of this wand hits a wall (or any other solid obstacle) it causes " +
			"an avalanche of stones, damaging and stunning all creatures in the affected area.";
	}
}
