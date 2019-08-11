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
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

    private static final String TXT_KNOWN_UPGRADED	=
            "your %s looks much better now!";
    private static final String TXT_KNOWN_REPAIRED  =
            "your %s can't be upgraded any further, but it looks a bit better now.";

	private static final String TXT_UNKNW_REPAIRED  =
            "your %s looks a bit better now. But maybe you should have identified it first?";
	private static final String TXT_UNKNW_WHOKNOWS	=
            "your %s doesn't look different. Maybe you should have identified it first?";

    private static final String TXT_CURSE_WEAKENED =
            "your %s was cursed, but now the curse seems to be weaker.";
    private static final String TXT_CURSE_DISPELLED	=
            "your %s was cursed, but now the curse seems to be removed.";

	{
		name = "Scroll of Upgrade";
        shortName = "Up";

		inventoryTitle = "Select an item to upgrade";
		mode = WndBag.Mode.UPGRADEABLE;

        spellSprite = SpellSprite.SCROLL_UPGRADE;
        spellColour = SpellSprite.COLOUR_HOLY;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

        item.identify( CURSED_KNOWN );

        if( item.bonus >= 0 ) {

            if( item.isIdentified() ) {
                GLog.p( item.bonus < 3 ? TXT_KNOWN_UPGRADED : TXT_KNOWN_REPAIRED, item.name() );
            } else {
                GLog.p( item.state < 3 ? TXT_UNKNW_REPAIRED : TXT_UNKNW_WHOKNOWS, item.name() );
            }

            item.upgrade();
            curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);

        } else {

            item.upgrade();

            if( item.bonus < 0 ) {

                GLog.w( TXT_CURSE_WEAKENED, item.name() );
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

            } else {

                GLog.p( TXT_CURSE_DISPELLED, item.name() );
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

            }
        }


        item.repair(1);

        QuickSlot.refresh();

        Statistics.itemsUpgraded++;
		
		Badges.validateItemsUpgraded();
	}

	
	@Override
	public String desc() {
		return
			"This scroll will upgrade a single item, improving its quality. A wand will " +
			"increase in power and in number of charges; a weapon will inflict more damage " +
			"or find its mark more frequently; a suit of armor will deflect additional blows; " +
			"the effect of a ring on its wearer will intensify. Weapons and armor will also " +
			"require less strength to use.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 125 * quantity : super.price();
    }
}
