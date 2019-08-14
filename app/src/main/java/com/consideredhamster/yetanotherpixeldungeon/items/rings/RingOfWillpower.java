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

import com.consideredhamster.yetanotherpixeldungeon.Element;

import java.util.HashSet;
import java.util.Locale;

public class RingOfWillpower extends Ring {

	{
		name = "Ring of Willpower";
        shortName = "Wi";
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Mind.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Willpower();
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
            "By increasing wearer's willpower, this ring indirectly increases their magical " +
            "abilities. Also, it offers additional benefit of making it easier to " +
            "shrug off all kinds of mental debuffs."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase your _magic power by " + mainEffect + "%_ and " +
            "increase your _resistance to mind effects by " + sideEffect + "%_."
        );

        return desc.toString();
    }

    public class Willpower extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "Your arcane proficiency is improved." :
                "Your arcane proficiency is decreased." ;
        }
    }
}
