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
package com.consideredhamster.yetanotherpixeldungeon.actors.blobs;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Blinded;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;

public class Darkness extends Blob {
	
	@Override
	protected void evolve() {
		super.evolve();

        Blob blob = Dungeon.level.blobs.get( Sunlight.class );

        if (blob != null) {

            int par[] = blob.cur;

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {
                    blob.volume -= par[i];
                    par[i] = 0;
                }
            }
        }

		Char ch;
		for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                if (cur[i] > 0) {
                    BuffActive.add( ch, Blinded.class, TICK );
                }
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour( Speck.factory( Speck.DARKNESS ), 0.2f );
	}
	
	@Override
	public String tileDesc() {
		return "A cloud of impenetrable darkness is swirling here, obstructing your vision.";
	}
}
