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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GolemSprite;

public class Golem extends MobHealthy {

    public Golem() {

        super( 16 );

		name = "stone golem";
		spriteClass = GolemSprite.class;

        dexterity /= 2;

	}

    @Override
    public int armourAC() {
        return buffs( Burning.class ) == null ? super.armourAC() : 0 ;
    }
	
	@Override
	public float attackDelay() {
		return 1.5f;
	}

    @Override
    public float moveSpeed() {
        return 0.75f;
    }
	
	@Override
	public void die( Object cause, DamageType dmg ) {
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

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Flame.class);
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Shock.class);
        RESISTANCES.add(DamageType.Acid.class);
        RESISTANCES.add(DamageType.Energy.class);
        RESISTANCES.add(DamageType.Unholy.class);

        IMMUNITIES.add(DamageType.Mind.class);
        IMMUNITIES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }
}
