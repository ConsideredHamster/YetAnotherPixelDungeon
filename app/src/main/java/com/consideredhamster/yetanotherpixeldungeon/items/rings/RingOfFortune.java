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

public class RingOfFortune extends Ring {

	{
		name = "Ring of Fortune";
        shortName = "Fo";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Fortune();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
            "First impression is that this ring affects nothing at all, but when using it for an " +
            "extended amount of time you may find your wealth to be increased substantially due to " +
            "being blessing with greater luck."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase _amount of gold_ spawned and _amount of loot_ " +
            "dropped by defeated enemies _by " + mainEffect + "%_."
        );

        return desc.toString();
	}
	
	public class Fortune extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You don't feel anything special on equipping this ring. Is that good?" :
                "You don't feel anything special on equipping this ring. Is that bad?" ;
        }
	}
}
