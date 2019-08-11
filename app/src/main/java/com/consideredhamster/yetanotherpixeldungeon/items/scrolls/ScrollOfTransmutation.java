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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorCloth;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorHeavy;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorLight;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandUtility;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponHeavy;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponLight;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponSpecial;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfTransmutation extends InventoryScroll {

	private static final String TXT_ITEM_TRANSMUTED	= "your %s is transmuted into %s!";
    private static final String TXT_ITEM_RESISTS	= "Your %s is cursed and resists being transmuted!";
	private static final String TXT_ITEM_UNKNOWN	= "%s cannot be transmuted!";

	{
		name = "Scroll of Transmutation";
        shortName = "Tr";

		inventoryTitle = "Select an transmutable item";
		mode = WndBag.Mode.TRANSMUTABLE;

        spellSprite = SpellSprite.SCROLL_TRANSMUT;
        spellColour = SpellSprite.COLOUR_WILD;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

        if( item.bonus < 0  ) {

            item.identify( CURSED_KNOWN );
            GLog.w(TXT_ITEM_RESISTS, item.name());
            curUser.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

        } else if( !transmute( item ) ) {

            item.identify( CURSED_KNOWN );
            GLog.w( TXT_ITEM_UNKNOWN, item.name() );
//            curUser.sprite.emitter().start( Speck.factory(Speck.CHANGE), 0.1f, 3 );

		} else {

            item.fix();
            item.identify( CURSED_KNOWN );
//            curUser.sprite.emitter().start( Speck.factory(Speck.CHANGE), 0.1f, 5 );

        }
	}

    public static boolean transmute( Item item ) {

        if(item instanceof ThrowingWeapon) {

            Item newItem = changeThrowing((ThrowingWeapon) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap1 == item ) {

                curUser.belongings.weap1 = (Weapon)newItem;

            } else if( curUser.belongings.weap2 == item ) {

                curUser.belongings.weap2 = (Weapon) newItem;

            } else {

                item.detachAll(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if(item instanceof Weapon) {

            Item newItem = changeWeapon((Weapon) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap1 == item ) {

                curUser.belongings.weap1 = (Weapon)newItem;

            } else if( curUser.belongings.weap2 == item ) {

                if( newItem instanceof MeleeWeaponLightOH ) {

                    curUser.belongings.weap2 = (Weapon) newItem;

                } else {

                    curUser.belongings.weap2 = null;

                    if (!newItem.doPickUp(curUser)) {
                        Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                    }

                }

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Armour) {

            Item newItem = changeArmour((Armour) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap2 == item ) {

                curUser.belongings.weap2 = (Shield) newItem;

            } else if( curUser.belongings.armor == item ) {

                curUser.belongings.armor = (BodyArmor) newItem;
                ((HeroSprite)curUser.sprite).updateArmor();

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Ring) {

            Item newItem = changeRing((Ring) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.ring1 == item ) {

                newItem.identify( ENCHANT_KNOWN );
                GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

                curUser.belongings.ring1.deactivate( curUser );
                curUser.belongings.ring1 = (Ring)newItem;
                curUser.belongings.ring1.activate( curUser );

            } else if( curUser.belongings.ring2 == item ) {

                newItem.identify( ENCHANT_KNOWN );
                GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

                curUser.belongings.ring2.deactivate( curUser );
                curUser.belongings.ring2 = (Ring)newItem;
                curUser.belongings.ring2.activate( curUser );

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Wand) {

            Item newItem = changeWand((Wand) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if (curUser.belongings.weap2 == item) {

                curUser.belongings.weap2 = (Wand) newItem;
                curUser.belongings.weap2.activate(curUser);

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }
        } else {
            return false;
        }

        curUser.sprite.emitter().start( Speck.factory( Speck.CHANGE ), 0.2f, 5 );

        return true;
    }


    private static Weapon changeWeapon( Weapon w ) {

        Weapon n;
        do {
            n = (Weapon) Generator.random(Generator.Category.WEAPON, false);
        } while (n == null || n.getClass() == w.getClass() || n.weaponType() != w.weaponType() );

        n.known = w.known;
        n.state = w.state;
        n.bonus = w.bonus;
        n.enchantment = w.enchantment;
        n.durability = w.durability;

        return n;
    }

    private static Weapon changeThrowing( ThrowingWeapon w ) {

        ThrowingWeapon n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING);

        if( w instanceof ThrowingWeaponAmmo ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponAmmo ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        } else if( w instanceof ThrowingWeaponSpecial ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponSpecial ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        } else if( w instanceof ThrowingWeaponLight ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponLight ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            };

        } else if( w instanceof ThrowingWeaponHeavy ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponHeavy ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        }

        n.known = w.known;
        n.state = w.state;
        n.bonus = w.bonus;
        n.enchantment = w.enchantment;
        n.durability = w.durability;

        if( n.tier > w.tier ) {
            n.quantity = Math.max( 1, w.quantity * n.baseAmount() / w.baseAmount() );
        } else {
            n.quantity = w.quantity;
        }

        return n;
    }

    private static Armour changeArmour( Armour a ) {

        Armour n = (Armour) Generator.random(Generator.Category.ARMOR, false);

        if (a instanceof Shield) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof Shield ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorCloth) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorCloth ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorLight) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorLight ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorHeavy) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorHeavy ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        }

        n.known = a.known;
        n.state = a.state;
        n.bonus = a.bonus;
        n.glyph = a.glyph;
        n.durability = a.durability;

        return n;
    }

    private static Ring changeRing( Ring r ) {
        Ring n;
        do {
            n = (Ring) Generator.random(Generator.Category.RING);
        } while (n == null || n.getClass() == r.getClass());

        n.bonus = r.bonus;
        n.known = r.known;



        return n;
    }

    private static Wand changeWand( Wand w ) {

        Wand n;
        do {
            n = (Wand)Generator.random( Generator.Category.WAND );
        } while (n == null || n.getClass() == w.getClass() || n instanceof WandUtility != w instanceof WandUtility);

        n.bonus = w.bonus;
        n.state = w.state;
        n.known = w.known;
        n.durability = w.durability;
        n.setCharges( w.getCharges() );

        return n;
    }
	
	@Override
	public String desc() {
		return
			"This scroll is able to transmute an item into a different one, but of a similar value. It " +
            "works on weapons, armors, wands and rings.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 60 * quantity : super.price();
    }
}
