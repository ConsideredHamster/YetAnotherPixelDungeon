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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Freezing;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class PotionOfFrigidVapours extends Potion {
	
	private static final int DISTANCE	= 2;

    public static final float BASE_VAL	= 1.0f;
    public static final float MODIFIER	= 0.2f;
	
	{
		name = "Potion of Frigid Vapours";
        shortName = "Fr";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlockHigh, null ), DISTANCE );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

		boolean visible = false;
        float chance = BASE_VAL + MODIFIER * alchemySkill();

		for (int i=0; i < Level.LENGTH; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE && chance / ( PathFinder.distance[i] + 1 ) > Random.Float() ) {
				visible = Freezing.affect( i, 10, fire ) || visible;
			}
		}
		
		if (visible) {
			setKnown();
            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }
	}
	
	@Override
	public String desc() {
		return 
			"Upon exposure to open air, this chemical will evaporate into a freezing cloud, causing " +
			"any creature that touches it to be frozen in place, unable to act or move.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 35 * quantity : super.price();
    }
}
