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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class Invisibility extends PassiveBuff {



    private static final String TXT_DISPEL		=
            "Invisibility is dispelled!";

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.invisible++;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		if(target.invisible > 0) {
            target.invisible--;
        }
		super.detach();
	}
	
	@Override
	public int icon() {
		return BuffIndicator.INVISIBLE;
	}
	
	@Override
	public String toString() {
		return "Invisible";
	}


    public static void dispel() {

        Invisibility.dispel( Dungeon.hero );

    }

	public static void dispel( Char ch ) {

        Invisibility buff = ch.buff( Invisibility.class );
        if ( buff != null ) {
            GLog.w(TXT_DISPEL);
            buff.detach();
		}
	}
}
