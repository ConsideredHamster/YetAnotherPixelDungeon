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

import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.food.FrozenCarpaccio;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Frozen extends PassiveBuff {

	private static final float DURATION	= 5f;

    protected int level;

    private static final String LEVEL	= "level";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVEL, level );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt( LEVEL );
    }

    public void set( int level ) {
        this.level = level;
    };
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {

            level = 1;
			
			target.stunned = true;
            Buff.detach( target, Burning.class );
            Buff.detach( target, Invisibility.class);
			
			if (target instanceof Hero) {
				Hero hero = (Hero)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof MysteryMeat) {
					
					item = item.detach( hero.belongings.backpack );
					FrozenCarpaccio carpaccio = new FrozenCarpaccio(); 
					if (!carpaccio.collect( hero.belongings.backpack )) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {

        level--;

        if( level < 1 ) {

            super.detach();
            Stun.unfreeze(target);

        }
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}
	
	@Override
	public String toString() {
		return "Frozen";
	}
	
	public static float duration( Char ch ) {
//		Protection r = ch.buff( Protection.class );
//		return r != null ? r.durationFactor() * DURATION : DURATION;
		return DURATION;
	}

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Frost.class;
    }
}
