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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffReactive;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;

public class Combo extends BuffReactive {
	
	private static String TXT_COMBO = "combo %dx!";
	
	public int count = 0;

    @Override
    public int icon() {
        return count > 2 ? BuffIndicator.COMBO : BuffIndicator.NONE;
    }

    @Override
    public String toString() {
        return "Combo x" + count;
    }

    @Override
    public String description() {
        return "Every consecutive attack increases your damage slightly. Performing any actions " +
                "except attacking again will reset this counter, though.";
    }

    @Override
    public boolean attachTo( Char target ) {

        Buff.detach( target, Guard.class);
        Buff.detach( target, Focus.class );

        return super.attachTo( target );
    }

	public void hit( ) {

		count++;

        reset( 1 );

        if ( target.sprite.visible && count >= 3 ) {
            target.sprite.showStatus( CharSprite.DEFAULT, TXT_COMBO, count );
        }
	}

    public float modifier() {

        if ( count > 2 ) {

            return ( count - 2 ) * 0.125f;

        } else {

            return 0.0f;

        }
    }

//	@Override
//	public boolean act() {
//		detach();
//		return true;
//	}

    private static final String COUNT	= "count";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, count );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );
    }
}
