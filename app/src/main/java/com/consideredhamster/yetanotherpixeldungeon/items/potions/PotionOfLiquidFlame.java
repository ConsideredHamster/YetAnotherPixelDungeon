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

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class PotionOfLiquidFlame extends Potion {

    public static final float BASE_VAL	= 0.5f;

	{
		name = "Potion of Liquid Flame";
        shortName = "Li";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

		GameScene.add( Blob.seed( cell, 2, Fire.class ) );

        for (int n : Level.NEIGHBOURS8) {
            if( Level.flammable[ cell + n ] || !Level.water[ cell + n ] &&
                    Level.passable[ cell + n ] && BASE_VAL > Random.Float() ) {
                GameScene.add( Blob.seed( cell + n, 2, Fire.class ) );
            }
        }

        super.shatter( cell );
	}
	
	@Override
	public String desc() {
		return
			"This flask contains an unstable compound which will burst " +
			"violently into flame upon exposure to open air.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 40 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.95f;
    }
}
