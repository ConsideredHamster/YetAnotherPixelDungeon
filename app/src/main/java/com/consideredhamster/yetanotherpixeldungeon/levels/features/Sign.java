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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.DeadEndLevel;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END = 
		"What are you doing here?!";
	
	private static final String[] TIPS = {

            "By the orders of the King, the city's sewers are temporarily closed for the general public due to the recent reports. Enter at your own risk!",
            "The sewer personnel are recommended to keep their lantern alight at all times. Otherwise, some passages may be difficult to find.",
            "Due to the increasing numbers of criminal activity and gnoll infestation reports, the personnel is advised to stay armed at all times.",
            "Addressing the recent requests, the sewer administration have installed sources of clean drinkable water in the sewers. Stay hydrated!",
            "The sewer management reminds our personnel that any reports of giant hostile crab sightings are completely absurd and obviously false.",
            "Caution! Further floors are locked by the King's decree. Security systems have been installed to prevent unauthorized access.",

            "hello there! feel free to visit me anytime.",
			"don't you think spiders are cute? and giant spiders are even cuter!",
			"sorry for all the gnolls here, they asked REALLY nicely to let them pass",
            "you may meet some of my friends here. don't be afraid, they just want to play!",
            "you're almost there! just a little further... hahaha!",
            "welcome to my playground! ready to have some fun?",

            "HOOMANZ GO AWAY OR BE FOOD",
            "EYE SEE U",
            "HERE BE SKORPOS",
            "GNOLZ STRONK",
            "DWORFS GO AWAY GNOLLZ ROOL HEER NOW",
            "Welcome to the Dwarven Metropolis! Outsiders aren't welcome.",

            "Upper City",
            "Marketplace",
            "Industrial Quarters",
            "Army Quarters",
            "Palace Square",
            "Throne Room",

            "Royal Treasury",

            "Abandon hope ye who enter here.",
            "One day, we shall escape.",
            "Our reign will be glorious.",
            "Nothing can stop us.",

            "",

            "",

//            "greetings, mortal" +
//                "\n\nare you ready to die?",
//            "my servants can smell your blood, human",
//            "worship me, and i may yet be merciful" +
//                "\n\nthen again, maybe not",
//            "you have played this game for too long, mortal" +
//                "\n\ni think i shall remove you from the board"
	};
	
	private static final String TXT_NOMESSAGE =
		"Whatever was written here is incomprehensible.";
	
//	public static void read( int pos ) {
	public static void read() {

		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;
			
			if (index < TIPS.length && TIPS[index] != "" ) {
				GameScene.show( new WndMessage( TIPS[index] ) );
			} else {
                GameScene.show( new WndMessage( TXT_NOMESSAGE ) );
//				Level.set( pos, Terrain.EMBERS );
//				GameScene.updateMap( pos );
//				GameScene.discoverTile( pos, Terrain.SIGN );
				
//				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
//				Sample.INSTANCE.play( Assets.SND_BURNING );
				
//				GLog.w( TXT_BURN );
				
			}
		}
	}
}
