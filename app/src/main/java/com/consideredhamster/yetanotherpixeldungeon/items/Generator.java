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
package com.consideredhamster.yetanotherpixeldungeon.items;

import java.util.HashMap;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
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
import com.consideredhamster.yetanotherpixeldungeon.items.food.Food;
import com.consideredhamster.yetanotherpixeldungeon.items.food.OverpricedRation;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.DreamweedHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.WhirlvineHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.FirebloomHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.IcecapHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.SorrowmossHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.SungrassHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.ArmorerKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Battery;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.CraftingKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Explosives;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Torch;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Whetstone;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.*;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.*;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.*;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.*;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.*;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.*;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Arbalest;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Arquebuse;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Bow;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Handcannon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Pistole;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Sling;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.*;
import com.watabou.utils.Random;

public class Generator {

	public static enum Category {

        WEAPON	( 60,	Weapon.class ),
        ARMOR	( 35,	Armour.class ),
        WAND	( 3,	Wand.class ),
        RING	( 2,	Ring.class ),

        POTION	( 40,	Potion.class ),
        SCROLL	( 30,	Scroll.class ),
        THROWING( 20,   Item.class ),
        MISC	( 10,	Item.class ),

        GOLD	( 0,	Gold.class ),
        HERB    ( 0,	Herb.class ),
        FOOD	( 0,	Food.class ),
        AMMO    ( 0,	Item.class ),

        ;

		public Class<?>[] classes;
		public float[] probs;

		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	};
	
	private static HashMap<Category,Float> categoryProbs = new HashMap<Generator.Category, Float>();
	private static HashMap<Category,Float> equipmentProbs = new HashMap<Generator.Category, Float>();
	private static HashMap<Category,Float> comestibleProbs = new HashMap<Generator.Category, Float>();

	static {

        Category.WEAPON.probs = null;
		Category.WEAPON.classes = new Class<?>[]{
                Dagger.class,
                Knuckles.class,
                Quarterstaff.class,

                Spear.class,
                Shortsword.class,
                Mace.class,

                Glaive.class,
                Broadsword.class,
                Battleaxe.class,

                Halberd.class,
                Greatsword.class,
                Warhammer.class,

                Sling.class,
                Bow.class,
                Arbalest.class,

				Pistole.class,
				Arquebuse.class,
				Handcannon.class,
        };

        Category.ARMOR.probs = null;
		Category.ARMOR.classes = new Class<?>[]{
                MageArmor.class,
                RogueArmor.class,
                HuntressArmor.class,

                StuddedArmor.class,
                MailArmor.class,
                ScaleArmor.class,

                DiscArmor.class,
                SplintArmor.class,
                PlateArmor.class,

                RoundShield.class,
                KiteShield.class,
                TowerShield.class,
        };

        Category.WAND.probs = null;
        Category.WAND.classes = new Class<?>[]{
                WandOfPhasing.class,
                WandOfFreezing.class,
                WandOfFirebolt.class,
                WandOfHarm.class,
                WandOfBlink.class,
                WandOfLightning.class,
                WandOfCharm.class,
                WandOfEntanglement.class,
                WandOfFlock.class,
                WandOfMagicMissile.class,
                WandOfDisintegration.class,
                WandOfAvalanche.class,
        };

        Category.RING.probs = null;
        Category.RING.classes = new Class<?>[]{
                RingOfShadows.class,
                RingOfAccuracy.class,
                RingOfEvasion.class,
                RingOfPerception.class,
                RingOfVitality.class,
                RingOfSatiety.class,
                RingOfEnergy.class,
                RingOfProtection.class,
                RingOfFortune.class,
                RingOfKnowledge.class,
                RingOfHaste.class,
                RingOfSorcery.class,
        };

        Category.SCROLL.probs = new float[]{ 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 3, 2 };
        Category.SCROLL.classes = new Class<?>[]{
                ScrollOfIdentify.class,
                ScrollOfTransmutation.class,

                ScrollOfSunlight.class,
                ScrollOfDarkness.class,

                ScrollOfClairvoyance.class,
                ScrollOfPhaseWarp.class,

                ScrollOfBanishment.class,
                ScrollOfChallenge.class,

                ScrollOfTorment.class,
                ScrollOfRaiseDead.class,

                ScrollOfUpgrade.class,
                ScrollOfEnchantment.class,
        };

        Category.POTION.probs = new float[]{ 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 3, 2 };
        Category.POTION.classes = new Class<?>[]{
                PotionOfMending.class,
                PotionOfLiquidFlame.class,

                PotionOfMindVision.class,
                PotionOfFrigidVapours.class,

                PotionOfLevitation.class,
                PotionOfCorrosiveGas.class,

                PotionOfInvisibility.class,
                PotionOfOvergrowth.class,

                PotionOfBlessing.class,
                PotionOfThunderstorm.class,

                PotionOfStrength.class,
                PotionOfWisdom.class,
        };

        Category.THROWING.probs = null;
        Category.THROWING.classes = new Class<?>[]{
                Bullets.class,
                Arrows.class,
                Quarrels.class,

                PoisonDarts.class,
                Knives.class,
                Bolas.class,

                Shurikens.class,
                Javelins.class,
                Tomahawks.class,

                Boomerangs.class,
                Chakrams.class,
                Harpoons.class,
        };

        Category.MISC.probs = new float[]{ 5, 4, 4, 4, 4, 3, 3, 3, 3, 2 };
        Category.MISC.classes = new Class<?>[]{
	            Gold.class,

                Explosives.Gunpowder.class,
                Explosives.BombStick.class,

                OverpricedRation.class,
                Torch.class,

                Whetstone.class,
                ArmorerKit.class,

                CraftingKit.class,
                Battery.class,

                Ankh.class,
        };

        Category.HERB.probs = null;
        Category.HERB.classes = new Class<?>[]{
                FirebloomHerb.class,
                IcecapHerb.class,
                SorrowmossHerb.class,
                DreamweedHerb.class,
                SungrassHerb.class,
                WhirlvineHerb.class
        };

        Category.AMMO.probs = null;
        Category.AMMO.classes = new Class<?>[]{
                Bullets.class,
                Arrows.class,
                Quarrels.class,
                Explosives.Gunpowder.class,
                Explosives.BombStick.class,
                Explosives.BombBundle.class,
        };

        Category.GOLD.probs = null;
        Category.GOLD.classes = new Class<?>[]{
                Gold.class };

        Category.FOOD.probs = null;
        Category.FOOD.classes = new Class<?>[]{
                Food.class };
	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );

            if(
                    cat.superClass == Weapon.class
                    || cat.superClass == Armour.class
                    || cat.superClass == Wand.class
                    || cat.superClass == Ring.class
            ) {
                equipmentProbs.put( cat, cat.prob );
            } else {
                comestibleProbs.put( cat, cat.prob );
            }
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}

    public static Item randomEquipment() {
        return random( Random.chances( equipmentProbs ) );
    }

    public static Item randomComestible() {
        return random( Random.chances( comestibleProbs ) );
    }
	
	public static Item random( Category cat ) {
		try {
			
//			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
                case ARMOR:
                    return randomArmor();
                case WEAPON:
                    return randomWeapon();
                case THROWING:
                    return randomThrowing();
                default:
                    return ( cat.probs != null ?
                        ((Item) cat.classes[Random.chances(cat.probs)].newInstance()).random()
                    :
                        ((Item) Random.element( cat.classes ).newInstance()).random()
                    );
            }
			
		} catch (Exception e) {

			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			return null;
			
		}
	}
	
	public static Armour randomArmor() throws Exception {

        Category cat = Category.ARMOR;

        Armour a = (Armour)Random.element( cat.classes ).newInstance();
        a.random();
        int delta = 0;

        while ( ( Dungeon.depth - a.lootLevel() > delta ) || ( a.lootLevel() - Dungeon.depth > delta ) ) {
            a = (Armour)Random.element( cat.classes ).newInstance();
            a.random();
            delta++;
        }

        return a;

	}
	
	public static Weapon randomWeapon() throws Exception {
		
		Category cat = Category.WEAPON;

        Weapon w = (Weapon)Random.element( cat.classes ).newInstance();
        w.random();
        int delta = 0;

        while ( ( Dungeon.depth - w.lootLevel() > delta ) || ( w.lootLevel() - Dungeon.depth > delta ) ) {
            w = (Weapon)Random.element( cat.classes ).newInstance();
            w.random();
            delta++;
        }

        return w;
	}

    public static Weapon randomThrowing() throws Exception {

        Category cat = Category.THROWING;

        Weapon w = (Weapon)Random.element( cat.classes ).newInstance();
        w.random();
        int delta = 0;

        while ( ( Dungeon.depth - w.lootLevel() > delta ) || ( w.lootLevel() - Dungeon.depth > delta ) ) {
            w = (Weapon)Random.element( cat.classes ).newInstance();
            w.random();
            delta++;
        }

        return w;
    }

//    public static Wand randomWand() throws Exception {
//
//        Category cat = Category.WAND;
//
//        Wand w = (Wand)cat.classes[Random.chances( cat.probs )].newInstance();
//        w.random();
//        int delta = 0;
//
//        while ( ( Dungeon.depth - w.lootLevel() > delta ) || ( w.lootLevel() - Dungeon.depth > delta ) ) {
//            w = (Wand)cat.classes[Random.chances( cat.probs )].newInstance();
//            w.random();
//            delta++;
//        }
//
//        return w;
//    }
}
