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
package com.consideredhamster.yetanotherpixeldungeon;

public class Difficulties {

	public static final int EASY			= 0;
	public static final int NORMAL			= 1;
	public static final int HARDCORE		= 2;
	public static final int IMPOSSIBLE		= 3;
	
	public static final String[] NAMES = {
		"Easy",
		"Normal",
		"Hardcore",
		"Impossible"
	};

    public static final String[] ABOUT = {

            "- Player character receives less damage\n" +
            "- Bosses have 20% less health\n" +
            "- Mobs have their health minimized\n" +
            "- Can't earn any badges on this difficulty!\n",

            "- Player character receives normal damage\n" +
            "- Bosses have normal health\n"+
            "- Mobs have their health randomized\n" +
            "- This difficulty has no special features\n",

            "- Player character receives normal damage\n" +
            "- Bosses have 20% more health\n"+
            "- Mobs have their health maximized\n" +
            "- Beat the game on Normal to unlock!\n",

            "- Player character receives more damage\n" +
            "- Bosses have 50% more health\n"+
            "- Mobs have their health maximized\n" +
            "- Beat the game on Hardcore to unlock!\n",
    };
}
