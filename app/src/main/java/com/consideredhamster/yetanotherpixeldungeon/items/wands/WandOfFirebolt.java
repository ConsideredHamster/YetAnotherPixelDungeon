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
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.CorrosiveGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Callback;

public class WandOfFirebolt extends Wand {

	{
		name = "Wand of Firebolt";
        shortName = "Fi";
	}

    @Override
    public int max() {
        return 30;
    }

    @Override
    public int min() {
        return 15;
    }

	@Override
	protected void onZap( int cell ) {

        Blob blob1 = Dungeon.level.blobs.get(CorrosiveGas.class);
        Blob blob2 = Dungeon.level.blobs.get(Miasma.class);

		for (int i=1; i < Ballistica.distance - 1; i++) {
			int c = Ballistica.trace[i];
			if (Level.flammable[c] || blob1 != null && blob1.cur[c] > 0 || blob2 != null && blob2.cur[c] > 0 ) {
				GameScene.add( Blob.seed( c, 1, Fire.class ) );
			}
		}

        if ( Level.flammable[ cell ] ) {
           GameScene.add(Blob.seed(cell, 2, Fire.class));
        }
					
		Char ch = Actor.findChar(cell);
		if (ch != null) {

            if( Char.hit( curUser, ch, true, true ) ) {

                ch.damage( Char.absorb(damageRoll(), ch.armorClass(), true), curUser, DamageType.FLAME );

//                Burning buff = Buff.affect( ch, Burning.class );
//
//                if( buff != null ) {
//                    buff.reignite(ch);
//                }

//                if (ch == curUser && !ch.isAlive()) {
//                    Dungeon.fail(Utils.format(ResultDescriptions.WAND, name, Dungeon.depth));
//                    GLog.n("You killed yourself with your own Wand of Firebolt...");
//                }

            } else {

                Sample.INSTANCE.play(Assets.SND_MISS);
                ch.missed();

            }
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.fire( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"This wand unleashes bursts of magical fire. It will ignite " +
			"flammable terrain, and will damage and burn a creature it hits.";
	}
}
