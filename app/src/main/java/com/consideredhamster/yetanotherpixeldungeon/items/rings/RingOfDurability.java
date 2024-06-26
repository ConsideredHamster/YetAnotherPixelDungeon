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

public class RingOfDurability extends Ring {

	{
		name = "Ring of Durability";
        shortName = "Du";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Durability();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
        }

        StringBuilder desc = new StringBuilder(
            "Rings of Durability are valued by men of crafts and warfare alike, due to their ability " +
            "to make tools of their trade to serve longer and be repaired with greater ease."
        );

        if( !dud ) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    "Wearing this ring will increase overall _durability of your items by " + mainEffect + "%_ " +
                            "and give _" + sideEffect + "% chance to repair your items for free_."
            );
        }

        return desc.toString();
	}
	
	public class Durability extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel how some kind of protective aura surrounds your equipment." :
                "You feel how some kind of disruptive aura surrounds your equipment." ;
        }
	}
}
