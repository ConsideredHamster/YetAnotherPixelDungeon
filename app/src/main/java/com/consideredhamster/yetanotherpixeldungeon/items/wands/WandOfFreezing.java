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

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Freezing;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Callback;

public class WandOfFreezing extends WandUtility {

	{
		name = "Wand of Freezing";
        shortName = "Fr";
//        hitChars = true;
	}

	@Override
	protected void onZap( int cell ) {

        int distance = 8;

        Ballistica.distance = Math.min( Ballistica.distance, distance );

        if (Ballistica.distance > distance + 1 ) {
            cell = Ballistica.trace[ distance ];
        } else if (Actor.findChar(cell) != null && Ballistica.distance > 1) {
            cell = Ballistica.trace[Ballistica.distance-1];
        }

        Splash.at( cell, 0xFFFFFF, 10 );

        Fire fire = (Fire) Dungeon.level.blobs.get( Fire.class );

        for (int i=1; i < Ballistica.distance ; i++) {

            Freezing.affect(Ballistica.trace[i], curCharges, fire);

        }

        for (int i : Level.NEIGHBOURS8) {
            if( !Level.solid[cell + i] ) {
                Freezing.affect( cell + i, curCharges, fire );
            }
        }


//		Char ch = Actor.findChar( cell );
//		if (ch != null) {
//
//            if( Char.hit( curUser, ch, true, true ) ) {
//
//                int power = power();
//
//                if( Level.water[ch.pos] && !ch.flying ) {
//
//                    Buff.affect(ch, Frost.class, 5f + 0.5f * power );
//
//                } else {
//
//                    Buff.affect(ch, Frost.class, 3f + 0.3f * power );
//
//                }
//
//                ch.sprite.burst( 0xFFFFFFFF, (int)Math.sqrt( power ) + 1 );
////			    Buff.affect( ch, Slow.class, 5.0f + (float)Math.sqrt( power() + 5 ) );
//
//            } else {
//
//                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
//                Sample.INSTANCE.play(Assets.SND_MISS);
//
//            }
//
//		} else {
//
//			GLog.i( "nothing happened" );
//
//		}
	}
	
	protected void fx( int cell, Callback callback ) {
//        cell = Ballistica.trace[Math.min( Ballistica.distance - 1, distance() )];
		MagicMissile.frost(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return 
			"This wand will freeze everything in its wake, buying its user " +
			"a chance to deliver a shattering blow to a defenseless foe or escape " +
            "a menacing threat. Also, you can make some delicious Carpaccio with it.";
	}
}
