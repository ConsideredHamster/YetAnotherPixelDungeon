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
import com.watabou.noosa.tweeners.AlphaTweener;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.watabou.utils.Callback;

public class WandOfBlink extends WandUtility {

	{
		name = "Wand of Blink";
        shortName = "Bl";
        hitChars = true;
	}
	
	@Override
	protected void onZap( int cell ) {

		int distance = curCharges;
		
		if (Ballistica.distance > distance + 1 ) {
			cell = Ballistica.trace[ distance ];
		} else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		curUser.sprite.visible = true;
		appear( Dungeon.hero, cell );
		Dungeon.observe();
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {

        int distance = curCharges;

        if (Ballistica.distance > distance + 1 ) {
            cell = Ballistica.trace[ distance ];
        } else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
            cell = Ballistica.trace[Ballistica.distance - 2];
        }

		MagicMissile.whiteLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		curUser.sprite.visible = false;
	}
	
	public static void appear( Char ch, int pos ) {
		
		ch.sprite.interruptMotion();
		
		ch.move( pos );
		ch.sprite.place( pos );
		
		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}
		
		ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return
			"This wand will allow you to teleport in the chosen direction. " +
			"Creatures and inanimate obstructions will block the teleportation.";
	}
}
