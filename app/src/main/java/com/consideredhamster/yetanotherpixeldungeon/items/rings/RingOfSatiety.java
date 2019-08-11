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

public class RingOfSatiety extends Ring {

	{
		name = "Ring of Satiety";
        shortName = "Sa";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Satiety();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 3 );
        }

        StringBuilder desc = new StringBuilder(
            "Rings of satiety optimize digestive mechanisms of wearer's body, making it possible " +
            "to go without food longer and increasing nutriety of consumed meals, both helping in " +
            "the times of hunger and helping to prolong times of excess."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will decrease the rate at which your _satiety drops by " + mainEffect +
            "%_ and increasing _effectiveness of eating by " + sideEffect + "%_."
        );

        return desc.toString();
	}
	
	public class Satiety extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel comfortable warmth in your stomach." :
                "You feel your hunger growing faster." ;
        }
	}
}
