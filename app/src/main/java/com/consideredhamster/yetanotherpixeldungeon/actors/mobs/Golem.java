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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GolemSprite;

public class Golem extends MobHealthy {

    public Golem() {

        super( 16 );

        /*

            base maxHP  = 41
            armor class = 16

            damage roll = 7-22

            accuracy    = 20
            dexterity   = 8

            perception  = 80%
            stealth     = 80%

         */

		name = "stone golem";
		spriteClass = GolemSprite.class;

        dexterity /= 2;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public int armourAC() {
        return buffs( Burning.class ) == null ? super.armourAC() : 0 ;
    }
	
	@Override
	public float attackSpeed() {
		return 0.75f;
	}

    @Override
    public float moveSpeed() {
        return 0.75f;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		AmbitiousImp.Quest.process( this );
		
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {
		return
			"The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. " +
			"They used spirits of earth as the \"soul\" for the mechanical bodies of golems, which were believed to be " +
			"most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak. Their " +
            "metallic bodies can become less durable under high temperatures.";
	}

}
