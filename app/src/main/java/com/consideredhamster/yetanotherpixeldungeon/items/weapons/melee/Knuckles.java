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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class Knuckles extends MeleeWeaponLightOH {

	{
		name = "knuckleduster";
		image = ItemSpriteSheet.KNUCKLEDUSTER;
	}
	
	public Knuckles() {
		super( 1 );
	}

    @Override
    public int maxDurability() {

        return 150 ;

    }

    @Override
    public float speedFactor( Hero hero ) {

        return super.speedFactor( hero ) * 1.333f;

    }

    @Override
    public boolean disarmable() {
        return false;
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus) + 1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 3;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + 4;
    }

    @Override
    public Type weaponType() {
        return Type.M_BLUNT;
    }
	
	@Override
	public String desc() {
		return "A piece of iron shaped to fit around the knuckles. This simple design allows " +
                "attacking with this weapon as fast as with fists, while being almost impossible " +
                "to knock it out of them.";
	}
}
