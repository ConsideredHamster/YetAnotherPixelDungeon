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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndBag;

public class ScrollOfEnchantment extends InventoryScroll {

	private static final String TXT_ITEM_ENCHANT	= "Your %s glows in the dark.";
	private static final String TXT_ITEM_RESISTS	= "Your cursed %s resists being enchanted!";
	private static final String TXT_ITEM_UNCURSE	= "Malicious enchantment is lifted from your %s!";
	private static final String TXT_ITEM_UNKNOWN	= "%s cannot be enchanted!";

	{
		name = "Scroll of Enchantment";
        shortName = "En";

		inventoryTitle = "Select an enchantable item";
		mode = WndBag.Mode.ENCHANTABLE;

        spellSprite = SpellSprite.SCROLL_ENCHANT;
        spellColour = SpellSprite.COLOUR_RUNE;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

//		ScrollOfBanishment.uncurse(Dungeon.hero, item);
//        if( item.bonus < 0)
//            item.bonus = bonus * (-1);

        if (item instanceof Weapon) {

            Weapon weapon = (Weapon)item;
            
            if( weapon.bonus >= 0 ) {

                weapon.enchant();
                GLog.w(TXT_ITEM_ENCHANT, weapon.name());

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
                Badges.validateItemLevelAcquired(item);

            } else {

                if( weapon.enchantment != null ) {
                    weapon.enchant( null );
                    GLog.w(TXT_ITEM_UNCURSE, weapon.name());
                    weapon.identify(CURSED_KNOWN);
                    curUser.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);

                } else {
                    GLog.w(TXT_ITEM_RESISTS, weapon.name());
                    weapon.identify( CURSED_KNOWN );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                }
            }

        } else if (item instanceof Armour) {

            Armour armour = (Armour)item;

            if( armour.bonus >= 0 ) {

                armour.inscribe();
                GLog.w(TXT_ITEM_ENCHANT, armour.name());

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

                Badges.validateItemLevelAcquired(item);

            } else {

                if( armour.glyph != null ) {
                    armour.inscribe(null);
                    GLog.w(TXT_ITEM_UNCURSE, armour.name());
                    curUser.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                } else {
                    GLog.w(TXT_ITEM_RESISTS, armour.name());
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                }
            }

        } else {

            GLog.w( TXT_ITEM_UNKNOWN, item.name() );
		
		}
	}
	
	@Override
	public String desc() {
		return
			"This scroll is able to imbue a weapon or an armor with a random enchantment, " +
            "granting it some special powers.";
	}



    @Override
    public int price() {
        return isTypeKnown() ? 150 * quantity : super.price();
    }
}
