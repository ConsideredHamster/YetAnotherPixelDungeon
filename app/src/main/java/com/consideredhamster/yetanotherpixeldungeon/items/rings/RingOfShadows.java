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

public class RingOfShadows extends Ring {

	{
		name = "Ring of Shadows";
        shortName = "Sh";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Shadows();
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
            "The curious enchantment on this ring controls the shadows around its wearer, helping " +
            "him or her to blend with their surroundings. Such rings are indispensable for those " +
            "whose job revolves around espionage or assassinations."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase your _stealth by " + mainEffect + "%_ and increasing " +
            "_sneak attack damage by " + sideEffect + "%_."
        );

        return desc.toString();
	}
	public class Shadows extends RingBuff {

        @Override
        public String desc() {
            return bonus >= 0 ?
                "Suddenly, shadows thicken around you, obfuscating your presence." :
                "Suddenly, shadows bend around you, highlighting your presence." ;
        }
	}
}
