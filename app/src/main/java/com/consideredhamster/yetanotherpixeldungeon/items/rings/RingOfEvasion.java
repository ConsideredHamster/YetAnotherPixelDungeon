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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

import java.util.Locale;

public class RingOfEvasion extends Ring {

	{
		name = "Ring of Evasion";
        shortName = "Ev";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Evasion();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
            "Rings of this kind serve to improve reflexes and speed of their wearer, making them " +
            "harder to be hit - especially when they are on the move."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase your _evasion by " + mainEffect + "%_ when you " +
            "are standing still and by _" + sideEffect + "% when you are moving_."
        );

        return desc.toString();
	}
	
	public class Evasion extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel that your reflexes are improved." :
                "You feel that your reflexes are dampened." ;
        }
	}
}
