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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.sprites.BlackguardSprite;

import java.util.HashSet;

public class Blackguard extends MobHealthy {

    public Blackguard() {

        super( 20 );

		name = "blackguard";
		spriteClass = BlackguardSprite.class;

	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Flame.class);
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Acid.class);
        RESISTANCES.add(DamageType.Energy.class);
        RESISTANCES.add(DamageType.Unholy.class);
        RESISTANCES.add(DamageType.Dispel.class);
        RESISTANCES.add(DamageType.Mind.class);

        IMMUNITIES.add(DamageType.Body.class);
    }
    
    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }

	@Override
	public String description() {
		return
			"This metallic juggernaut once was a hero like you. Now, his tormented soul is bound within these halls forever.";
	}

}
