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
package com.consideredhamster.yetanotherpixeldungeon.levels.painters;

import java.util.ArrayList;
import java.util.Arrays;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.ShopkeeperDemon;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.ShopkeeperDwarf;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.ShopkeeperGhost;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.ShopkeeperTroll;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.ArmorerKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Battery;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.EmptyBottle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Explosives;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.CraftingKit;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Whetstone;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.DiscArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.PlateArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.HuntressArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.StuddedArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MailArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.ScaleArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.RogueArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.SplintArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.KiteShield;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.RoundShield;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.TowerShield;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.PotionSash;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.ScrollHolder;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.HerbPouch;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.WandHolster;
import com.consideredhamster.yetanotherpixeldungeon.items.food.RationLarge;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfMending;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.*;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Arbalest;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Arquebuse;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Bow;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Handcannon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Pistole;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Sling;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Arrows;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bolas;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Boomerangs;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Chakrams;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Harpoons;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Javelins;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Knives;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.PoisonDarts;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Quarrels;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Shurikens;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Tomahawks;
import com.consideredhamster.yetanotherpixeldungeon.levels.CavesLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.CityLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.LastShopLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.PrisonLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ShopPainter extends Painter {

	private static int pasWidth;
	private static int pasHeight;

    private static ArrayList<Item> kits;
    private static ArrayList<Item> ammo1;
    private static ArrayList<Item> ammo2;

    private static Item[] defaultKits = { new Whetstone(), new ArmorerKit(), new CraftingKit(), new Battery() };
    private static Item[] defaultAmmo1 = { new Arrows(), new Arrows(), new Quarrels(), new Quarrels() };
    private static Item[] defaultAmmo2 = { new Bullets(), new Bullets(), new Explosives.Gunpowder(), new Explosives.Gunpowder() };

    private static final String KITS		= "shops_kits";
    private static final String AMMO1		= "shops_ammo1";
    private static final String AMMO2		= "shops_ammo2";

    public static void initAssortment() {

        kits = new ArrayList<>();
        kits.addAll( Arrays.asList( defaultKits ) );

        ammo1 = new ArrayList<>();
        ammo1.addAll( Arrays.asList( defaultAmmo1 ) );

        ammo2 = new ArrayList<>();
        ammo2.addAll( Arrays.asList( defaultAmmo2 ) );

    }

    public static void saveAssortment( Bundle bundle) {

        bundle.put( KITS, kits );
        bundle.put( AMMO1, ammo1 );
        bundle.put( AMMO2, ammo2 );

    }

    public static void loadAssortment( Bundle bundle ) {

        kits = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( KITS )) {
            if( item != null ){
                kits.add( (Item)item );
            }
        };

        ammo1 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO1 )) {
            if( item != null ){
                ammo1.add( (Item)item );
            }
        };

        ammo2 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO2 )) {
            if( item != null ){
                ammo2.add( (Item)item );
            }
        };
    }

	public static void paint( Level level, Room room ) {
		
		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY_SP);

		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;
		int per = pasWidth * 2 + pasHeight * 2;
		
		Item[] range = range( level );
		
		int pos = xy2p( room, room.entrance() ) + (per - range.length) / 2;
		for (int i=0; i < range.length; i++) {
			
			Point xy = p2xy( room, (pos + per) % per );
			int cell = xy.x + xy.y * Level.WIDTH;
			
			if (level.heaps.get( cell ) != null) {
				do {
					cell = room.random();
				} while (level.heaps.get( cell ) != null);
			}
			
			level.drop( range[i], cell, true ).type = Heap.Type.FOR_SALE;
			
			pos++;
		}
		
		placeShopkeeper( level, room );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
	}
	
	private static Item[] range( Level level ) {
		
		ArrayList<Item> items = new ArrayList<Item>();

        if ( level instanceof LastShopLevel) {

            int questsCompleted = Dungeon.questsCompleted();

            items.add( Random.oneOf(
                    new Knuckles(), new Dagger(), new Quarterstaff(),
                    new Sling(), new Bow(), new Arbalest(),
                    new Pistole(), new Arquebuse(), new Handcannon()
            ).enchant().repair().fix().uncurse(3).upgrade(3).identify() );

            items.add( Random.oneOf(
                    new Spear(), new Mace(), new Shortsword(),
                    new Glaive(), new Battleaxe(), new Broadsword(),
                    new Halberd(), new Warhammer(), new Greatsword()
            ).enchant().repair().fix().uncurse(3).upgrade(3).identify() );

            items.add( Random.oneOf(
                    new StuddedArmor(), new DiscArmor(), new RoundShield(),
                    new MailArmor(), new SplintArmor(), new KiteShield(),
                    new ScaleArmor(), new PlateArmor(), new TowerShield()
            ).inscribe().repair().fix().uncurse(3).upgrade(3).identify() );

            items.add( Random.oneOf(
                    new PoisonDarts(), new Bolas(), new Boomerangs(),
                    new Knives(), new Shurikens(), new Chakrams(),
                    new Javelins(), new Tomahawks(), new Harpoons()
            ).random());

            items.add(Random.oneOf(
                    new Bullets(), new Arrows(), new Quarrels()
            ).random());

            items.add(Random.oneOf(
                    new Explosives.Gunpowder(), new Explosives.BombStick(), new Explosives.BombBundle()
            ).random());

            items.add(Generator.random(Generator.Category.RING).uncurse(3).upgrade(3));
            items.add(Generator.random(Generator.Category.WAND).repair().fix().uncurse(3).upgrade(3));

            items.add(new PotionOfMending());
            items.add(Random.Int(5 - questsCompleted) == 0 ? new PotionOfStrength() : Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            items.add(Random.Int(5 - questsCompleted) == 0 ? new ScrollOfUpgrade() : Generator.random(Generator.Category.SCROLL));

            items.add(Random.oneOf(new Whetstone(), new ArmorerKit(), new Battery(), new CraftingKit()));
            items.add(new RationLarge());

            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());
            items.add(new EmptyBottle());

        } else {

            Bag bag = null;
            Weapon weapon = null;
            Armour armour = null;
            Item ranged = null;
            ThrowingWeapon thrown = null;

            if (level instanceof SewerLevel) {

                bag = new HerbPouch();

                weapon = Random.oneOf(new Dagger(), new Knuckles(), new Quarterstaff());
                armour = Random.oneOf(new MageArmor(), new RogueArmor(), new HuntressArmor());
                ranged = Random.oneOf(new Sling(), new Bolas());
                thrown = Random.oneOf(new PoisonDarts(), new Knives());

            } else if (level instanceof PrisonLevel) {

                bag = new PotionSash();

                weapon = Random.oneOf(new Spear(), new Mace(), new Shortsword());
                armour = Random.oneOf(new StuddedArmor(), new DiscArmor(), new RoundShield());
                ranged = Random.oneOf(new Bow(), new Pistole());
                thrown = Random.oneOf(new Javelins(), new Shurikens());

            } else if (level instanceof CavesLevel) {

                bag = new ScrollHolder();

                weapon = Random.oneOf(new Glaive(), new Battleaxe(), new Broadsword());
                armour = Random.oneOf(new MailArmor(), new SplintArmor(), new KiteShield());
                ranged = Random.oneOf(new Arbalest(), new Arquebuse());
                thrown = Random.oneOf(new Boomerangs(), new Tomahawks());

            } else if (level instanceof CityLevel) {

                bag = new WandHolster();

                weapon = Random.oneOf(new Halberd(), new Warhammer(), new Greatsword());
                armour = Random.oneOf(new ScaleArmor(), new PlateArmor(), new TowerShield());
                ranged = Random.oneOf(new Handcannon(), new Explosives.BombStick());
                thrown = Random.oneOf(new Harpoons(), new Chakrams());

            }

            if( bag != null ) {
                items.add(bag);
            }

            if( weapon != null ) {
                weapon.repair().fix().identify().upgrade(Random.Int(Dungeon.chapter()));

                if( Random.Int( 5 ) < Dungeon.chapter() ) {
                    weapon.enchant();
                }

                items.add(weapon);
            }

            if( armour != null ) {
                armour.repair().fix().identify().upgrade( Random.Int( Dungeon.chapter() ) );

                if( Random.Int( 5 ) < Dungeon.chapter() ) {
                    armour.inscribe();
                }

                items.add(armour);
            }

            if( ranged instanceof RangedWeapon ) {
                ranged.repair().fix().identify().upgrade(Random.Int(Dungeon.chapter()));

                if( Random.Int( 5 ) < Dungeon.chapter() ) {
                    ((RangedWeapon)ranged).enchant();
                }

                items.add(ranged);
            } else if( ranged instanceof ThrowingWeapon || ranged instanceof Explosives ) {
                ranged.random();
                items.add(ranged);
            }

            if( thrown != null ) {
                thrown.random();
                items.add(thrown);
            }

            items.add( generateAmmo1() );
            items.add( generateAmmo2() );

            Ring ring = (Ring)Generator.random(Generator.Category.RING);
            if( ring != null) {
                ring.bonus = Random.Int( Dungeon.chapter() );
                items.add(ring);
            }

            Wand wand = (Wand)Generator.random(Generator.Category.WAND);
            if( wand != null ) {
                wand.bonus = Random.Int( Dungeon.chapter() );
                wand.repair().fix();
                wand.recharge();
                items.add(wand);
            }

            items.add(new PotionOfMending());
            items.add(Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            items.add(Generator.random(Generator.Category.SCROLL));

            items.add( generateKits() );

            items.add(new RationLarge());
            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());
            items.add(new EmptyBottle());
        }
		
		Item[] range = items.toArray( new Item[0] );
//		Random.shuffle( range );
		
		return range;
	}

    private static Item generateAmmo1() {
        if( !ammo1.isEmpty() ) {
            return ammo1.remove( Random.Int( ammo1.size() ) ).random();
        } else {
            return Random.oneOf( defaultAmmo1 ).random();
        }
    }

    private static Item generateAmmo2() {
        if( !ammo2.isEmpty() ) {
            return ammo2.remove( Random.Int( ammo2.size() ) ).random();
        } else {
            return Random.oneOf( defaultAmmo2 ).random();
        }
    }

    private static Item generateKits() {
        if( !kits.isEmpty() ) {
            return kits.remove( Random.Int( kits.size() ) );
        } else {
            return Random.oneOf( defaultKits );
        }
    }
	
	private static void placeShopkeeper( Level level, Room room ) {

        int pos;
        do {
            pos = room.random(1);
        } while (level.heaps.get(pos) != null);

        Mob shopkeeper;

        if (level instanceof LastShopLevel) {
            shopkeeper = new ShopkeeperDemon();
        } else if( level instanceof CityLevel) {
            shopkeeper = new ShopkeeperDwarf();
        } else if( level instanceof CavesLevel) {
            shopkeeper = new ShopkeeperTroll();
        } else if( level instanceof PrisonLevel) {
            shopkeeper = new ShopkeeperGhost();
        } else {
            shopkeeper = new Shopkeeper();
        }

        shopkeeper.pos = pos;
        level.mobs.add( shopkeeper );

		if (level instanceof LastShopLevel) {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				int p = shopkeeper.pos + Level.NEIGHBOURS9[i];
				if (level.map[p] == Terrain.EMPTY_SP) {
					level.map[p] = Terrain.WATER;
				}
			}
		}
	}
	
	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else /*if (xy.x == room.left)*/ {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {

			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
}
