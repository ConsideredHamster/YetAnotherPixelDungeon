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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.sprites.BatSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;

public class VampireBat extends MobEvasive {

    public static boolean swarmer = true;

    public VampireBat() {

        super( 9 );

        name = "vampire bat";
        spriteClass = BatSprite.class;

        flying = true;

//		loot = new PotionOfHealing();
//		lootChance = 0.125f;

        baseSpeed = 2f;
	}

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
            && Level.distance( pos, Dungeon.hero.pos ) <= 2
            && detected( Dungeon.hero ) && detected(Dungeon.hero)
        ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() && !enemy.isMagical() ) {

            int reg = Math.min(Random.Int(damage + 1), HT - HP);

            if (reg > 0) {
                HP += reg;

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                }
            }
        }
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by " +
			"replenishing their health with each successful attack.";
	}
}
