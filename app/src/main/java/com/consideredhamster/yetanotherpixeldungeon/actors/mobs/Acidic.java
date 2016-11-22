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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.sprites.AcidicSprite;
import com.watabou.utils.Random;

public class Acidic extends CaveScorpion {

	{
		name = "acidic scorpio";
		spriteClass = AcidicSprite.class;
	}

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst( 0x006600, 5 );
        }

        return damage;
    }
	
	@Override
	public int defenseProc( Char enemy, int damage,  boolean blocked ) {

//		int dmg = Random.IntRange( 0, damage );
//		if (dmg > 0) {
//			enemy.damage( dmg, this );
//		}

        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Ooze.class);
            this.sprite.burst( 0x006600, 5 );
        }


        return super.defenseProc( enemy, damage, blocked );
	}
	
//	@Override
//	public void die( Object cause ) {
//		super.die( cause );
//		Badges.validateRare( this );
//	}
}
