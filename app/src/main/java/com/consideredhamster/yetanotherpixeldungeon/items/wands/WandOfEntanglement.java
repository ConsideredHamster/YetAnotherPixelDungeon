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
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Web;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;

public class WandOfEntanglement extends WandUtility {

	{
		name = "Wand of Entanglement";
        shortName = "En";
	}
	
	@Override
	protected void onZap( int cell ) {
		
//		for (int i=1; i < Ballistica.distance-1; i++) {
//			int p = Ballistica.trace[i];
//			int c = Dungeon.level.map[p];
//			if (c == Terrain.EMPTY ||
//				c == Terrain.EMBERS ||
//				c == Terrain.EMPTY_DECO) {
//
//				Level.set( p, Terrain.GRASS );
//
//			}
//		}
//
//		int c = Dungeon.level.map[cell];
//		if (c == Terrain.EMPTY ||
//			c == Terrain.EMBERS ||
//			c == Terrain.EMPTY_DECO ||
//			c == Terrain.GRASS ||
//			c == Terrain.HIGH_GRASS) {
//
//			GameScene.add( Blob.seed( cell, (power() + 2) * 20, Regrowth.class ) );
//
//
//
//		} else {
//
//			GLog.i( "nothing happened" );
//
//		}

        Char ch = Actor.findChar(cell);
        if (ch != null) {

//            if (Char.hit(curUser, ch, true, true)) {

                Buff.affect(ch, Ensnared.class, curCharges + 1 );

                GameScene.add( Blob.seed( cell, curCharges + 1, Web.class ) );

//            GLog.w("%s charges", curCharges);

//                ch.sprite.burst(0xFFFFFFFF, (int) Math.sqrt(power) + 1);

//            } else {
//
//                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
//                Sample.INSTANCE.play(Assets.SND_MISS);
//
//            }
        }



//        for (int i : Level.NEIGHBOURS8) {
//            if( !Dungeon.level.solid[cell + i] && !Dungeon.level.chasm[cell + i] && Random.Int( power / 5 ) > 0 ) {
//                GameScene.add( Blob.seed( cell + i, Random.IntRange( 5 + power / 5, 5 + power / 3 ), Web.class ) );
//            }
//        }
	}
	
	protected void fx( int cell, Callback callback ) {
//		MagicMissile.foliage( curUser.sprite.parent, curUser.pos, cell, callback );
		MagicMissile.web( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"This wand possesses the mystical force of shooting a missiles of a very thick cobweb, trapping it's target in place. " +
            "It is used mainly by city guards to catch criminals... Or by criminals to catch their victims.";
	}
}
