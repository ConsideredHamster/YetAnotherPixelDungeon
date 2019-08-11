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

public class RingOfKnowledge extends Ring {

	{
		name = "Ring of Knowledge";
        shortName = "Kn";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Knowledge();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
            "Both scholars and their pupils appreciate such rings, because they increase cognitive " +
            "capacity of their wearer, allowing to learn more things in a shorter amount of time."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase both the _amount of experience_ earned and " +
            "_identification rate_ of equipped items _by " + mainEffect + "%_."
        );

        return desc.toString();
	}
	
	public class Knowledge extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel that your mind starts working better, your memory improves." :
                "You start feeling stupid. It is like something prevents you from thinking clearly." ;
        }
	}
}
