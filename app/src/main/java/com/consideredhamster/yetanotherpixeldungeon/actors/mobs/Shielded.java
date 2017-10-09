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

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShieldedSprite;

public class Shielded extends GnollBrute {

	{
		name = "shielded brute";
		spriteClass = ShieldedSprite.class;

        HP = HT = 60;
        dexterity = 30;
        maxLvl = 25;
        EXP = 16;

    }
	
	@Override
	public int armourAC() {
		return 10;
	}
	
//	@Override
//	public String defenseVerb() {
//		return "blocked";
//	}
	
//	@Override
//	public void die( Object cause ) {
//		super.die( cause );
//		Badges.validateRare( this );
//	}
}
