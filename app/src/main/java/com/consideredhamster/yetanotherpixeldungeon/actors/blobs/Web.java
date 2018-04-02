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

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.watabou.utils.Random;

public class Web extends Blob {
	
	@Override
	protected void evolve() {
		
		for (int i=0; i < LENGTH; i++) {
			
			int offv = cur[i] > 0 ? cur[i] - 1 : 0;
			off[i] = offv;
			
			if (offv > 0) {
				
				volume += offv;
				
				Char ch = Actor.findChar( i );

				if ( ch != null ) {
                    if( ch.buff( Ensnared.class ) != null ){
                        BuffActive.add( ch, Ensnared.class, TICK * 2 );
                    } else {
                        BuffActive.add( ch, Ensnared.class, TICK );
                    }
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
//		emitter.pour(WebParticle.FACTORY, 0.4f);
        emitter.pour( Speck.factory( Speck.COBWEB ), 0.3f );
//		emitter.pour( WebParticle.FACTORY, 0.4f );
	}
	
	public void seed( int cell, int amount ) {
		int diff = amount - cur[cell];
		if (diff > 0) {
			cur[cell] = amount;
			volume += diff;
		}
	}
	
	@Override
	public String tileDesc() {
		return "Everything is covered with a thick web here.";
	}
}
