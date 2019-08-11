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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.WandHolster;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;

public class Recharging extends Buff {

	@Override
	public boolean act() {
		if (target.isAlive()) {

            if( target.buff( Withered.class ) == null ){

                Hero hero = (Hero) target;

                charge( hero, hero.belongings.weap2 );

                charge( hero, hero.belongings.backpack.items.toArray( new Item[0] ) );
            }

			spend( TICK );
			
		} else {
			
			deactivate();
			
		}

		return true;
	}

    public static void charge( Hero hero, Item... items ) {

        for( Item item : items ){

            if( item instanceof Wand ){

                Wand wand = (Wand) item;

                if( wand.getCharges() < wand.maxCharges() ){

                    wand.charge( hero.attunement() );
                    wand.updateQuickslot();

                }

            } else if( item instanceof WandHolster ) {

                charge( hero, ((WandHolster)item).items.toArray( new Item[0] ) );

            }
        }
    }
}
