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

public class RingOfAwareness extends Ring {

	{
		name = "Ring of Awareness";
        shortName = "Aw";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Awareness();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 3 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
            "These rings improve wearer's perception, making him or her to be more aware " +
            "of different threats as well as allowing for a much deadlier counterattacks."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase your _perception by " + mainEffect + "%_ " +
            "and _bonus damage from counter attacks by " + sideEffect + "%_."
        );

        return desc.toString();

	}
	
	public class Awareness extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel that your alertness is improved." :
                "You feel that your alertness is dimmed." ;
        }
	}
}
