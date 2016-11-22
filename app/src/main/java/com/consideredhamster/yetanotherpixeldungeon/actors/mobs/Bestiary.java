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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.watabou.utils.Random;

public abstract class Bestiary {

	public static Mob mob( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Mob mutable( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		
//		if (Random.Int( 30 ) == 0) {
//			if (cl == Rat.class) {
//				cl = Albino.class;
//			} else if (cl == Thief.class) {
//				cl = Bandit.class;
//			} else if (cl == Brute.class) {
//				cl = Shielded.class;
//			} else if (cl == Monk.class) {
//				cl = Senior.class;
//			} else if (cl == Scorpio.class) {
//				cl = Acidic.class;
//			}
//		}
		
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Class<?> mobClass( int depth ) {
		
		float[] chances;
		Class<?>[] classes;
		
		switch (depth) {
		case 1:
			chances = new float[]{ 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class };
			break;
		case 2:
			chances = new float[]{ 3, 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class };
			break;
		case 3:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class };
			break;
		case 4:
			chances = new float[]{ 3, 2, 1, 1, 0.02f };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class, CarrionSwarm.class };
			break;
        case 5:
            chances = new float[]{ 2, 1, 1, 1, 0.05f, 0.02f };
            classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class, CarrionSwarm.class, Skeleton.class };
            break;

//		case 1:
//		case 2:
//		case 3:
//		case 4:
//		case 5:
		case 6:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Goo.Mother.class };
			break;

		case 7:
			chances = new float[]{ 2, 1, 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GnollHunter.class };
			break;
		case 8:
            chances = new float[]{ 3, 2, 1, 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollHunter.class  };
			break;
		case 9:
			chances = new float[]{ 4, 3, 2, 1, 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class };
			break;
		case 10:
			chances = new float[]{ 3, 2, 1, 1, 1, 0.02f };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class, VampireBat.class };
			break;
        case 11:
            chances = new float[]{ 2, 1, 1, 1, 1, 0.05f, 0.02f };
            classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class, VampireBat.class, GnollBrute.class };
            break;

		case 12:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Tengu.class };
			break;

		case 13:
			chances = new float[]{ 2, 1, 1, 1 };
			classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, GnollShaman.class, GnollHunter.class };
			break;
		case 14:
			chances = new float[]{ 3, 2, 1, 1, 1 };
			classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollHunter.class };
			break;
		case 15:
			chances = new float[]{ 4, 3, 2, 1, 1, 1 };
			classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class };
			break;
		case 16:
            chances = new float[]{ 3, 2, 1, 1, 1, 1, 0.02f };
            classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class, DwarfMonk.class };
            break;
        case 17:
            chances = new float[]{ 2, 1, 1, 1, 1, 1, 0.05f, 0.02f };
            classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class, DwarfMonk.class, Elemental.class };
            break;

		case 18:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ DM300.class };
			break;

		case 19:
			chances = new float[]{ 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class };
			break;
		case 20:
			chances = new float[]{ 3, 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class };
			break;
		case 21:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class };
			break;
		case 22:
			chances = new float[]{ 3, 2, 1, 1, 0.02f };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class, Imp.class };
			break;
        case 23:
            chances = new float[]{ 2, 1, 1, 1, 0.05f, 0.02f };
            classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class, Imp.class, Succubus.class };
            break;

		case 24:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ DwarvenKing.class };
			break;

		case 26:
			chances = new float[]{ 2, 1 };
			classes = new Class<?>[]{ Imp.class, Succubus.class };
			break;
		case 27:
			chances = new float[]{ 3, 2, 1 };
			classes = new Class<?>[]{ Imp.class, Succubus.class, Fiend.class };
			break;
		case 28:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ Imp.class, Succubus.class, Fiend.class, Blackguard.class };
			break;
        case 29:
            chances = new float[]{ 1, 1, 1, 1 };
            classes = new Class<?>[]{ Imp.class, Succubus.class, Fiend.class, Blackguard.class };
            break;

		case 30:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Yog.class };
			break;
			
		default:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Rat.class };
		}
		
		return classes[ Random.chances( chances )];
	}
	
	public static boolean isBoss( Char mob ) {
		return mob instanceof Goo || mob instanceof Tengu || mob instanceof DM300 || mob instanceof DwarvenKing
                || mob instanceof Yog || mob instanceof Yog.RottingFist || mob instanceof Yog.BurningFist;
	}
}
