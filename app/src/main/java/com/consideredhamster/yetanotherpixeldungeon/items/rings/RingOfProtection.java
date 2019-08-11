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

import java.util.HashSet;
import java.util.Locale;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;

public class RingOfProtection extends Ring {

	{
		name = "Ring of Protection";
        shortName = "Pr";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Protection();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", Dungeon.hero.STR * Ring.effect( bonus ) );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
            "This ring makes the wearer's own body to become sturdier, effectively channeling his " +
            "or her physical strength into additional armor. It also greatly increases resistances " +
            "to various magical and elemental threats."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "Wearing this ring will increase your _armor class by " + mainEffect + "_ and " +
            "increase your _resistance_ to fire, cold, shock, acid and energy by " +
            "_" + sideEffect + "%_."
        );

        return desc.toString();
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Flame.class);
        RESISTS.add(Element.Flame.Periodic.class);
        RESISTS.add(Element.Shock.class);
        RESISTS.add(Element.Shock.Periodic.class);
        RESISTS.add(Element.Acid.class);
        RESISTS.add(Element.Acid.Periodic.class);
        RESISTS.add(Element.Frost.class);
        RESISTS.add(Element.Energy.class);
        RESISTS.add(Element.Unholy.class);
    }
	
	public class Protection extends RingBuff {

        @Override
        public String desc() {
            return bonus >= 0 ?
                "You feel more protected." :
                "You feel more vulnerable." ;
        }
	}
}
