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
package com.consideredhamster.yetanotherpixeldungeon.actors.blobs;

import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.Journal.Feature;
import com.consideredhamster.yetanotherpixeldungeon.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator.Category;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.*;

public class WaterOfTransmutation extends WellWater {
	
	@Override
	protected Item affectItem( Item item ) {
		
		if (item instanceof MeleeWeapon) {			
			item = changeWeapon( (MeleeWeapon)item );		
		} else if (item instanceof Scroll) {	
			item = changeScroll( (Scroll)item );	
		} else if (item instanceof Potion) {
			item = changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			item = changeRing( (Ring)item );
		} else if (item instanceof Wand) {	
			item = changeWand( (Wand)item );
		} else if (item instanceof Herb) {
			item = changeSeed( (Herb)item );
		} else {
			item = null;
		}
		
		if (item != null) {
			Journal.remove( Feature.WELL_OF_TRANSMUTATION );
		}
		
		return item;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );	
		emitter.start( Speck.factory( Speck.CHANGE ), 0.2f, 0 );
	}
	
	private MeleeWeapon changeWeapon( MeleeWeapon w ) {
		
		MeleeWeapon n = null;
		
		if (w instanceof Knuckles) {
			n = new Dagger();
		} else if (w instanceof Dagger) {
			n = new Knuckles();
		}
		
		else if (w instanceof Spear) {
			n = new Quarterstaff();
		} else if (w instanceof Quarterstaff) {
			n = new Spear();
		}
		
		else if (w instanceof Broadsword) {
			n = new Mace();
		} else if (w instanceof Mace) {
			n = new Broadsword();
		}
		
		else if (w instanceof Greatsword) {
			n = new Battleaxe();
		} else if (w instanceof Battleaxe) {
			n = new Greatsword();
		}
		
		else if (w instanceof Glaive) {
			n = new Warhammer();
		} else if (w instanceof Warhammer) {
			n = new Glaive();
		}
		
		if (n != null) {
			
			int level = w.bonus;
			if (level > 0) {
				n.upgrade( level );
			} else if (level < 0) {
				n.curse( -level );
			}
			
			if (w.isEnchanted()) {
				n.enchant();
			}
			
			n.known = w.known;
			
			Journal.remove( Feature.WELL_OF_TRANSMUTATION );
			
			return n;
		} else {
			return null;
		}
	}
	
	private Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.random( Category.RING );
		} while (n.getClass() == r.getClass());
		
		n.bonus = 0;
		
		int level = r.bonus;
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.curse( -level );
		}
		
		n.known = r.known;
		
		return n;
	}
	
	private Wand changeWand( Wand w ) {
		
		Wand n;
		do {
			n = (Wand)Generator.random( Category.WAND );
		} while (n.getClass() == w.getClass());
		
		n.bonus = 0;
		n.upgrade( w.bonus);
		
		n.known = w.known;
//		n.cursedKnown = w.cursedKnown;
//		n.cursed = w.cursed;
		
		return n;
	}
	
	private Herb changeSeed( Herb s ) {
		
		Herb n;
		
		do {
			n = (Herb)Generator.random( Category.HERB);
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private Scroll changeScroll( Scroll s ) {
		if (s instanceof ScrollOfUpgrade) {
			
			return new ScrollOfEnchantment();
			
		} else if (s instanceof ScrollOfEnchantment) {
			
			return new ScrollOfUpgrade();
			
		} else {
			
			Scroll n;
			do {
				n = (Scroll)Generator.random( Category.SCROLL );
			} while (n.getClass() == s.getClass());
			return n;
		}
	}
	
	private Potion changePotion( Potion p ) {
		if (p instanceof PotionOfStrength) {
			
			return new PotionOfWisdom();
			
		} else if (p instanceof PotionOfWisdom) {
			
			return new PotionOfStrength();
			
		} else {
			
			Potion n;
			do {
				n = (Potion)Generator.random( Category.POTION );
			} while (n.getClass() == p.getClass());
			return n;
		}
	}
	
	@Override
	public String tileDesc() {
		return 
			"Power of change radiates from the water of this well. " +
			"Throw an item into the well to turn it into something else.";
	}
}
