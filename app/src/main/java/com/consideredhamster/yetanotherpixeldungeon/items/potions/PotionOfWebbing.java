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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.SpiderWeb;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;

public class PotionOfWebbing extends Potion {

    public static final int BASE_DURATION = 10;

	{
		name = "Potion of Webbing";
        shortName = "We";
        harmful = true;
	}

    @Override
    public void shatter( int cell ) {

        for (int n : Level.NEIGHBOURS9) {

            Char ch;
            int pos = cell + n;
            int duration = Random.IntRange( BASE_DURATION, BASE_DURATION * 2 );

            if( ( ch = Char.findChar( pos ) ) != null ) {

                // if tile is occuppied, then just apply debuff directly
                BuffActive.add( ch, Ensnared.class, duration );

            } else if( n == 0 && !Level.solid[ pos ] && !Level.chasm[ pos ] ) {

                // otherwise, create a patch of webs on the targeted tile
                SpiderWeb.spawn( pos, duration );

            }

            CellEmitter.get( pos ).burst( Speck.factory( Speck.COBWEB ), 3 );
        }

        super.shatter( cell );
    }
	
	@Override
	public String desc() {
		return
			"Upon exposure to open air, this wondrous concoction will splash its surroundings " +
			"with sticky filaments of thick web. Anyone caught in this web will be ensnared " +
            "for a short period of time.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 75 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.60f;
    }
}
