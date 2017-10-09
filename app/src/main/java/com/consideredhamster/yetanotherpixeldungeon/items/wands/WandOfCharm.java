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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfCharm extends WandUtility {

	{
		name = "Wand of Charm";
        shortName = "Ch";
	}

	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );
		if (ch != null) {

//            if( Char.hit( curUser, ch, true, true ) ) {

                if (ch == Dungeon.hero) {
                    Buff.affect(ch, Confusion.class, curCharges );
                    ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                } else {
                    Charm buff = Buff.affect(ch, Charm.class, curCharges );

                    if( buff != null ) {
                        buff.object = curUser.id();
                        ch.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
                    }
                }

//            } else {
//                ch.sprite.showStatus( CharSprite.NEUTRAL, ch.defenseVerb() );
//                Sample.INSTANCE.play(Assets.SND_MISS);
//            }

		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.purpleLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"The purple light from this wand will hypnotize the target, forcing it " +
			"to violently protect you against other enemies for a while.";
	}
}
