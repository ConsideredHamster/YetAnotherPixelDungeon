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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs;

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Ooze extends Buff {
	
	public int damage = 1;
	
	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}
	
	@Override
	public String toString() {
		return "Caustic ooze";
	}
	
	@Override
	public boolean act() {
        if (!target.isAlive() || Level.water[target.pos] && !target.flying) {
            detach();
        } else {

//			target.damage( damage, this, DamageType.ACID );

            target.damage( Random.IntRange( 1, (int)Math.sqrt( !Bestiary.isBoss(target) ?
                    target.HT : target.HT / 4 ) / 2 + 1 ), this, DamageType.ACID );

//			if (!target.isAlive() && target == Dungeon.hero) {
//				Dungeon.fail( Utils.format( ResultDescriptions.OOZE, Dungeon.depth ) );
//				GLog.n( TXT_HERO_KILLED, toString() );
//			}

			spend(TICK);
		}

		return true;
	}

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Acid.class;
    }

}
