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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfPhaseWarp;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfPhasing extends WandUtility {

	{
		name = "Wand of Phasing";
        shortName = "Ph";
	}

	@Override
	protected void onZap( int cell ) {
		
		Char ch = Actor.findChar( cell );
        
//		if (ch == curUser) {
//
//			setKnown();
//			ScrollOfPhasing.teleportHero(curUser);
//
//		} else
        if (ch != null ) {

//            if (Char.hit(curUser, ch, true, true)) {

                int pos;

                int count = curCharges;

                do {
                    pos = Dungeon.level.randomRespawnCell();
                    if (count-- <= 0) {
                        break;
                    }
                } while (pos == -1 && Level.distance(cell, pos) <= 6 + curCharges);

                if (pos == -1) {

                    GLog.w(ScrollOfPhaseWarp.TXT_NO_TELEPORT);

                } else {

                    ch.pos = pos;
                    ch.sprite.place(ch.pos);
                    ch.sprite.visible = ch == curUser || Dungeon.visible[pos];

                    Buff.affect(ch, Confusion.class, curCharges );

                    GLog.i("%s teleported %s to somewhere", curUser.name, ch.name);

                }

//            } else {
//
//                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
//                Sample.INSTANCE.play(Assets.SND_MISS);
//
//            }
        } else {

            GLog.i( "nothing happened" );

        }
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.coldLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"A blast from this wand will teleport a creature against " +
			"its will to a random place on the current level.";
	}
}
