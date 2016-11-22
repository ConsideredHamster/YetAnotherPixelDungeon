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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee;

import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;

public class Shortsword extends MeleeWeaponLightOH {
	
//	public static final String AC_REFORGE	= "REFORGE";
//
//	private static final String TXT_SELECT_WEAPON	= "Select a weapon to upgrade";
//
//	private static final String TXT_REFORGED =
//		"you reforged the short sword to upgrade your %s";
//	private static final String TXT_NOT_BOOMERANG =
//		"you can't upgrade a boomerang this way";
//
//	private static final float TIME_TO_REFORGE	= 2f;
//
//	private boolean  equipped;
	
	{
		name = "shortsword";
		image = ItemSpriteSheet.GLADIUS;
	}
	
	public Shortsword() {
		super( 3 );
	}

    @Override
    public int max( int bonus ) {
        return super.max(bonus) - 2;
    }
	
//	@Override
//	public ArrayList<String> actions( Hero hero ) {
//		ArrayList<String> actions = super.actions( hero );
//		if (bonus > 0) {
//			actions.add( AC_REFORGE );
//		}
//		return actions;
//	}
	
//	@Override
//	public void execute( Hero hero, String action ) {
//		if (action == AC_REFORGE) {
//
//			if (hero.belongings.weapon == this) {
//				equipped = true;
//				hero.belongings.weapon = null;
//			} else {
//				equipped = false;
//				detach( hero.belongings.backpack );
//			}
//
//			curUser = hero;
//
//			GameScene.selectItem( itemSelector, WndBag.Mode.WEAPON, TXT_SELECT_WEAPON );
//
//		} else {
//
//			super.execute( hero, action );
//
//		}
//	}
	
	@Override
	public String desc() {
		return 
			"It is indeed quite short, just a few inches longer, than a dagger.";
	}
	
//	private final WndBag.Listener itemSelector = new WndBag.Listener() {
//		@Override
//		public void onSelect( Item item ) {
//			if (item != null && !(item instanceof Boomerang)) {
//
//				Sample.INSTANCE.play( Assets.SND_EVOKE );
//				ScrollOfUpgrade.upgrade( curUser );
//				evoke( curUser );
//
//				GLog.w( TXT_REFORGED, item.name() );
//
//				((MeleeWeapon)item).safeUpgrade();
//				curUser.spendAndNext( TIME_TO_REFORGE );
//
//				Badges.validateItemLevelAcquired( item );
//
//			} else {
//
//				if (item instanceof Boomerang) {
//					GLog.w( TXT_NOT_BOOMERANG );
//				}
//
//				if (equipped) {
//					curUser.belongings.weapon = ShortSword.this;
//				} else {
//					collect( curUser.belongings.backpack );
//				}
//			}
//		}
//	};
}
