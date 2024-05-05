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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.DM100Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DM100 extends MobHealthy {

    public DM100() {

        super( 4, 9, false );

		name = "DM-100";
		info = "Fast movement, Repair machine";
		spriteClass = DM100Sprite.class;

		maxDamage /= 2;
		dexterity /= 2;
		EXP += EXP / 2;

		resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
		resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
		resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
		resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
		resistances.put( Element.Physical.class, Element.Resist.PARTIAL );
	
		resistances.put( Element.Doom.class, Element.Resist.IMMUNE );
		resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
		resistances.put( Element.Body.class, Element.Resist.IMMUNE );
		resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
		resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );

	}

    @Override
    public float moveSpeed() {
        return 2.0f;
    }
    
    @Override
	public boolean act() {
	
		if ( state == SLEEPING ) {
		
			spend( TICK );
			return true;
		
		}

		Char ally = null;

		for ( int n : Level.NEIGHBOURS8 ) {

			Char ch = Actor.findChar(pos + n );

			if ( ch instanceof DM100 || ch instanceof DM300 ) {
				// checking who is more damaged
				if ( ally == null || ( ch.HT - ch.HP ) > ( ally.HT - ally.HP ) ) {
					ally = ch;
				}
			}
		}

		if ( ally != null && ally.HP < ally.HT ) {

			final int allyPos = ally.pos;

			sprite.cast( allyPos, new Callback() {
				@Override
				public void call() { repair( allyPos ); }
			}  );
			sprite.parent.add( new Lightning( pos, allyPos ) );

			spend( TICK );
			return false;

		} else {
			
			return super.act();
			
		}
	}

	public void repair( int pos ) {

		Char ch = Actor.findChar(pos );
		ch.heal( damageRoll() / 2 );

		Sample.INSTANCE.play(Assets.SND_LIGHTNING);
		sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

		next();
	}
	
	@Override
	public String description() {
		return
			"These machines were created by Dwarves several centuries ago. Later, Dwarves started to replace machines with " +
			"golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-100 and larger " +
			"machines were typically used for construction and mining, and in some cases, for city defense.";
	}
}