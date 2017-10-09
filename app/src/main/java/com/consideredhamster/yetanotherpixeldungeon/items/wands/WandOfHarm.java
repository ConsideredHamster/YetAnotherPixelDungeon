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
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfHarm extends Wand {

	{
		name = "Wand of Harm";
        shortName = "Ha";
	}

    @Override
    public int max() {
        return 15;
    }

    @Override
    public int min() {
        return 10;
    }
	
	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );

		if (ch != null) {

            if( Char.hit( curUser, ch, true, true ) ) {

                ch.damage( damageRoll(), curUser, DamageType.UNHOLY );

                Splash.at(cell, 0x000000, 5);

                ch.sprite.emitter().burst(ShadowParticle.CURSE, Random.IntRange( 3, 4 ));

            } else {

                Sample.INSTANCE.play(Assets.SND_MISS);
                ch.missed();

            }
			
		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.shadow(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"The vile blast of this twisted bit of wood will sap the very essence of its target " +
			"with a deadly blight. A creature that is affected by its power will suffer noticeable " +
			"drop in both physical and magical strength.";
	}
}
