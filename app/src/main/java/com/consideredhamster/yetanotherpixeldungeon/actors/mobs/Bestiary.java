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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.watabou.utils.Random;

public abstract class Bestiary {

	public static Mob mob( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );

		if ( Dungeon.depth > 1 && Random.Int( 20 ) == 0) {
			if ( Dungeon.chapter() == 1 && Statistics.deepestFloor < 6 ) {
				cl = GooSpawn.class;
			} else if ( Dungeon.chapter() == 2 && Statistics.deepestFloor < 12 ) {
				cl = TenguShadow.class;
			} else if ( Dungeon.chapter() == 3 && Statistics.deepestFloor < 18 ) {
				cl = DM100.class;
			} else if ( Dungeon.chapter() == 4 && Statistics.deepestFloor < 24 ) {
				cl = DwarvenUndead.class;
			}
		}

		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Mob mutable( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		
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
				chances = new float[]{ 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class, CarrionSwarm.class };
				break;
			case 5:
				chances = new float[]{ 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class, CarrionSwarm.class, Skeleton.class };
				break;

			case 6:
				chances = new float[]{ 1 };
				classes = new Class<?>[]{ Goo.class };
				break;

			case 7:
				chances = new float[]{ 3, 2, 1 };
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GnollHunter.class };
				break;
			case 8:
				chances = new float[]{ 4, 3, 2, 1 };
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollHunter.class  };
				break;
			case 9:
				chances = new float[]{ 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class };
				break;
			case 10:
				chances = new float[]{ 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class, VampireBat.class };
				break;
			case 11:
				chances = new float[]{ 7, 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GiantSpider.class, GnollShaman.class, GnollHunter.class, VampireBat.class, GnollBrute.class };
				break;

			case 12:
				chances = new float[]{ 1 };
				classes = new Class<?>[]{ Tengu.class };
				break;

			case 13:
				chances = new float[]{ 4, 3, 2, 1 };
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, GnollShaman.class, GnollHunter.class };
				break;
			case 14:
				chances = new float[]{ 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, GnollShaman.class, GnollHunter.class };
				break;
			case 15:
				chances = new float[]{ 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class };
				break;
			case 16:
				chances = new float[]{ 7, 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class, FireDrake.class };
				break;
			case 17:
				chances = new float[]{ 8, 7, 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, CaveScorpion.class, GnollShaman.class, GnollHunter.class, FireDrake.class, Golem.class };
				break;

			case 18:
				chances = new float[]{ 1 };
				classes = new Class<?>[]{ DM300.class };
				break;

			case 19:
				chances = new float[]{ 2, 1 };
				classes = new Class<?>[]{ DwarfMonk.class, FireDrake.class };
				break;
			case 20:
				chances = new float[]{ 3, 2, 1 };
				classes = new Class<?>[]{ DwarfMonk.class, FireDrake.class, DwarfWarlock.class };
				break;
			case 21:
				chances = new float[]{ 4, 3, 2, 1 };
				classes = new Class<?>[]{ DwarfMonk.class, FireDrake.class, DwarfWarlock.class, Golem.class };
				break;
			case 22:
				chances = new float[]{ 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ DwarfMonk.class, FireDrake.class, DwarfWarlock.class, Golem.class, Imp.class };
				break;
			case 23:
				chances = new float[]{ 6, 5, 4, 3, 2, 1 };
				classes = new Class<?>[]{ DwarfMonk.class, FireDrake.class, DwarfWarlock.class, Golem.class, Imp.class, Succubus.class };
				break;

			case 24:
				chances = new float[]{ 1 };
				classes = new Class<?>[]{ DwarvenKing.class };
				break;

			case 26:
				chances = new float[]{ 1, 1 };
				classes = new Class<?>[]{ Imp.class, Succubus.class };
				break;
			case 27:
				chances = new float[]{ 1, 1, 1 };
				classes = new Class<?>[]{ Imp.class, Succubus.class, Magus.class };
				break;
			case 28:
				chances = new float[]{ 1, 1, 1, 1 };
				classes = new Class<?>[]{ Imp.class, Succubus.class, Magus.class, Blackguard.class };
				break;
			case 29:
				chances = new float[]{ 1, 1, 1, 1 };
				classes = new Class<?>[]{ Imp.class, Succubus.class, Magus.class, Blackguard.class };
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
