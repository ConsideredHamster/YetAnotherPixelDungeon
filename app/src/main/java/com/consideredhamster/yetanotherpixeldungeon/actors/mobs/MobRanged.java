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

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;

public abstract class MobRanged extends Mob {

    protected MobRanged( int exp ) {
        this( Dungeon.chapter(), exp, false );
    }

    protected MobRanged( int t, int exp, boolean isBoss ) {

        tier = t;

        HT = 3 + tier * 3 + exp;
        armorClass = tier * 2;

        minDamage = tier + 1;
        maxDamage = tier * 3 + 3;

        accuracy = 4 + tier * 4 + exp;
        dexterity = 1 + tier + exp;

        if( !isBoss ) {

            if( Dungeon.difficulty == Difficulties.NORMAL ) {
                HT = Random.NormalIntRange(HT, HT * 2);
            } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
                HT = HT * 2;
            }

            EXP = exp;
            maxLvl = exp + 5;

        } else {

            if( Dungeon.difficulty > Difficulties.HARDCORE ) {
                HT = HT * 15;
            } else {
                HT = HT * 8 + HT * 2 * Dungeon.difficulty;
            }

            EXP = exp * 5;
            maxLvl = 25;

            minDamage += tier - 1;
            maxDamage += tier - 1;

            dexterity /= 2;
            armorClass /= 2;

        }

        HP = HT;
    }

    @Override
    public float awareness(){
        return super.awareness() * ( 1.0f + tier * 0.1f );
    }

}
