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
package com.consideredhamster.yetanotherpixeldungeon.levels;

public class Terrain {

	public static final int CHASM			= 0;
	public static final int EMPTY			= 1;
	public static final int GRASS			= 2;
	public static final int EMPTY_WELL		= 3;
	public static final int WALL			= 4;
	public static final int DOOR_CLOSED     = 5;
	public static final int OPEN_DOOR		= 6;
	public static final int ENTRANCE		= 7;
	public static final int EXIT			= 8;
	public static final int EMBERS			= 9;
	public static final int LOCKED_DOOR		= 10;
	public static final int PEDESTAL		= 11;
	public static final int WALL_DECO		= 12;
	public static final int BARRICADE		= 13;
	public static final int EMPTY_SP		= 14;
	public static final int HIGH_GRASS		= 15;
	public static final int EMPTY_DECO		= 24;
	public static final int LOCKED_EXIT		= 25;
	public static final int UNLOCKED_EXIT	= 26;
	public static final int SIGN			= 29;
	public static final int WELL			= 34;
	public static final int STATUE			= 35;
	public static final int STATUE_SP		= 36;
	public static final int BOOKSHELF		= 41;
    public static final int ALCHEMY			= 42;
    public static final int CHASM_FLOOR		= 43;
    public static final int CHASM_FLOOR_SP	= 44;
    public static final int CHASM_WALL		= 45;
    public static final int CHASM_WATER		= 46;
    public static final int GRATE          	= 48;
    public static final int SHELF_EMPTY 	= 49;
    public static final int WALL_SIGN   	= 50;
    public static final int WALL_DECO1      = 51;
    public static final int WALL_DECO2      = 52;
    public static final int WALL_DECO3      = 53;
    public static final int WALL_DECO4      = 54;
    public static final int WALL_DECO5      = 55;

	public static final int DOOR_ILLUSORY = 16;
	public static final int TOXIC_TRAP				= 17;
	public static final int SECRET_TOXIC_TRAP		= 18;
	public static final int FIRE_TRAP				= 19;
	public static final int SECRET_FIRE_TRAP		= 20;
	public static final int BOULDER_TRAP            = 21;
	public static final int SECRET_BOULDER_TRAP     = 22;
	public static final int INACTIVE_TRAP			= 23;
	public static final int POISON_TRAP				= 27;
	public static final int SECRET_POISON_TRAP		= 28;
	public static final int ALARM_TRAP				= 30;
	public static final int SECRET_ALARM_TRAP		= 31;
	public static final int LIGHTNING_TRAP			= 32;
	public static final int SECRET_LIGHTNING_TRAP	= 33;
	public static final int BLADE_TRAP              = 37;
	public static final int SECRET_BLADE_TRAP       = 38;
	public static final int SUMMONING_TRAP			= 39;
	public static final int SECRET_SUMMONING_TRAP	= 40;
	
	public static final int WATER_TILES         	= 64;
	public static final int WATER		            = 79;
	
	public static final int PASSABLE		= 0x01;
	public static final int LOS_BLOCKING	= 0x02;
	public static final int FLAMMABLE       = 0x04;
    public static final int TRAPPED         = 0x08;
    public static final int SOLID			= 0x10;
    public static final int AVOID			= 0x20;
    public static final int LIQUID			= 0x40;
    public static final int PIT				= 0x80;

    public static final int UNSTITCHABLE	= 0x100;
    public static final int IMPORTANT = 0x200;
    public static final int ILLUSORY = 0x400;

    public static final Integer[] WALLS = new Integer[] {
            WALL, WALL_DECO, WALL_DECO1, WALL_DECO2, WALL_DECO3, WALL_DECO4, WALL_DECO5,
            WALL_SIGN, DOOR_CLOSED, DOOR_ILLUSORY, OPEN_DOOR, LOCKED_DOOR, LOCKED_EXIT
    };

	public static final int[] flags = new int[256];
    static {
		flags[CHASM]		= AVOID	| PIT									| UNSTITCHABLE;
		flags[EMPTY]		= PASSABLE;
		flags[GRASS]		= PASSABLE | FLAMMABLE;
		flags[EMPTY_WELL]	= AVOID | IMPORTANT;
		flags[WATER]		= PASSABLE | LIQUID | UNSTITCHABLE;
		flags[WALL]			= LOS_BLOCKING | SOLID 							| UNSTITCHABLE;
		flags[DOOR_CLOSED]	= PASSABLE | LOS_BLOCKING | FLAMMABLE | SOLID	| UNSTITCHABLE;
		flags[OPEN_DOOR]	= PASSABLE | FLAMMABLE | UNSTITCHABLE | IMPORTANT;
		flags[ENTRANCE]		= PASSABLE | IMPORTANT;
		flags[EXIT]			= PASSABLE | IMPORTANT;
		flags[EMBERS]		= PASSABLE;
		flags[LOCKED_DOOR]	= LOS_BLOCKING | SOLID 							| UNSTITCHABLE;
        flags[PEDESTAL]		= AVOID | IMPORTANT | UNSTITCHABLE;
		flags[WALL_DECO]	= flags[WALL];
		flags[WALL_DECO1]	= flags[WALL];
		flags[WALL_DECO2]	= flags[WALL];
		flags[WALL_DECO3]	= flags[WALL];
		flags[WALL_DECO4]	= flags[WALL];
		flags[WALL_DECO5]	= flags[WALL];
		flags[BARRICADE]	= FLAMMABLE | SOLID | LOS_BLOCKING;
		flags[EMPTY_SP]		= flags[EMPTY]									| UNSTITCHABLE;
		flags[HIGH_GRASS]	= PASSABLE | LOS_BLOCKING | FLAMMABLE;
		flags[EMPTY_DECO]	= flags[EMPTY];
		flags[LOCKED_EXIT]	= SOLID;
		flags[UNLOCKED_EXIT]= PASSABLE | IMPORTANT;
		flags[SIGN]			= PASSABLE | FLAMMABLE | IMPORTANT;
		flags[WALL_SIGN]	= flags[WALL];
		flags[WELL]			= AVOID | IMPORTANT;
		flags[STATUE]		= SOLID;
		flags[GRATE]		= SOLID;
		flags[STATUE_SP]	= flags[STATUE] 								| UNSTITCHABLE;
		flags[BOOKSHELF]	= flags[BARRICADE]								| UNSTITCHABLE;
		flags[SHELF_EMPTY]  = flags[BOOKSHELF];
		flags[ALCHEMY]		= AVOID | PASSABLE | IMPORTANT;
		
		flags[CHASM_WALL]		= flags[CHASM];
		flags[CHASM_FLOOR]		= flags[CHASM];
		flags[CHASM_FLOOR_SP]	= flags[CHASM];
		flags[CHASM_WATER]		= flags[CHASM];
		
//		flags[DOOR_ILLUSORY]			= LOS_BLOCKING | AVOID | ILLUSORY | IMPORTANT | UNSTITCHABLE ;
		flags[DOOR_ILLUSORY]			= LOS_BLOCKING | FLAMMABLE | SOLID | UNSTITCHABLE | ILLUSORY;
		flags[TOXIC_TRAP]				= AVOID;
		flags[SECRET_TOXIC_TRAP]		= flags[EMPTY] | TRAPPED;
		flags[FIRE_TRAP]				= AVOID;
		flags[SECRET_FIRE_TRAP]			= flags[EMPTY] | TRAPPED;
		flags[BOULDER_TRAP]			    = AVOID;
		flags[SECRET_BOULDER_TRAP]	    = flags[EMPTY] | TRAPPED;
		flags[POISON_TRAP]				= AVOID;
		flags[SECRET_POISON_TRAP]		= flags[EMPTY] | TRAPPED;
		flags[ALARM_TRAP]				= AVOID;
		flags[SECRET_ALARM_TRAP]		= flags[EMPTY] | TRAPPED;
		flags[LIGHTNING_TRAP]			= AVOID;
		flags[SECRET_LIGHTNING_TRAP]	= flags[EMPTY] | TRAPPED;
		flags[BLADE_TRAP]			= AVOID;
		flags[SECRET_BLADE_TRAP]		= flags[EMPTY] | TRAPPED;
		flags[SUMMONING_TRAP]			= AVOID;
		flags[SECRET_SUMMONING_TRAP]	= flags[EMPTY] | TRAPPED;
		flags[INACTIVE_TRAP]			= flags[EMPTY];
		
		for (int i=WATER_TILES; i < WATER_TILES + 16; i++) {
			flags[i] = flags[WATER];
		}
	};
	
	public static int discover( int terr ) {
		switch (terr) {
		case DOOR_ILLUSORY:
			return DOOR_CLOSED;
		case SECRET_FIRE_TRAP:
			return FIRE_TRAP;
		case SECRET_BOULDER_TRAP:
			return BOULDER_TRAP;
		case SECRET_TOXIC_TRAP:
			return TOXIC_TRAP;
		case SECRET_POISON_TRAP:
			return POISON_TRAP;
		case SECRET_ALARM_TRAP:
			return ALARM_TRAP;
		case SECRET_LIGHTNING_TRAP:
			return LIGHTNING_TRAP;
		case SECRET_BLADE_TRAP:
			return BLADE_TRAP;
		case SECRET_SUMMONING_TRAP:
			return SUMMONING_TRAP;
		default:
			return terr;
		}
	}
}
