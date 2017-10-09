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
package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Freezing;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfFrigidVapours;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.watabou.utils.PathFinder;

public class Icecap extends Plant {

	private static final String TXT_DESC = "Upon touching an Icecap excretes a pollen, which freezes everything in its vicinity.";
	
	{
		image = 1;
		plantName = "Icecap";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		PathFinder.buildDistanceMap( pos, BArray.not( Level.losBlockHigh, null ), 1 );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		
		for (int i=0; i < Level.LENGTH; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Freezing.affect( i, 1, fire );
			}
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Icecap";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.HERB_ICECAP;
			
			plantClass = Icecap.class;
			alchemyClass = PotionOfFrigidVapours.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
