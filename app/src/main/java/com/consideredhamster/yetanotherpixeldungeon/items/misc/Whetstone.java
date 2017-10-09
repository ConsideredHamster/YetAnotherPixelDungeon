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
package com.consideredhamster.yetanotherpixeldungeon.items.misc;

import java.util.ArrayList;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class Whetstone extends Item {
	
	private static final String TXT_SELECT_WEAPON	= "Select a melee weapon to repair";
	private static final String TXT_REPAIR_WEAPON	= "Your %s looks much better now!";
	private static final String TXT_CHARGE_KEEPED	= "Your ring helped you with repair!";
	private static final String TXT_CHARGE_WASTED	= "Your ring prevented proper repair.";

	private static final float TIME_TO_APPLY = 2f;
	
	private static final String AC_APPLY = "APPLY";

    private static final String TXT_STATUS	= "%d/%d";
	
	{
		name = "whetstone";
		image = ItemSpriteSheet.WHETSTONE;
	}

    private static final String VALUE = "value";

    private int value = 3;
    private final int limit = 3;

    @Override
    public String quickAction() {
        return AC_APPLY;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(VALUE, value);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        value = bundle.getInt(VALUE);
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_APPLY) {

			curUser = hero;
            curItem = this;
			GameScene.selectItem( itemSelector, WndBag.Mode.WHETSTONE, TXT_SELECT_WEAPON );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}

    @Override
    public Item random() {
        value = Random.IntRange( 1, 3 );
        return this;
    }
	
	private void apply( Weapon weapon ) {

        float bonus = Dungeon.hero.ringBuffsBaseZero( RingOfDurability.Durability.class ) * 0.5f;

        if( bonus > 0.0f && Random.Float() < bonus ) {
            GLog.p(TXT_CHARGE_KEEPED);
        } else {
            if( --value <= 0 ) {
                detach(curUser.belongings.backpack);
            }
        }

        if( bonus < 0.0f && Random.Float() > -bonus ) {
            GLog.n(TXT_CHARGE_WASTED);
        } else {
            weapon.repair(1);
            GLog.p(TXT_REPAIR_WEAPON, weapon.name());
        }

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_MISS);

        curUser.sprite.centerEmitter().start(Speck.factory(Speck.KIT), 0.05f, 10);
		curUser.spend( TIME_TO_APPLY );
		curUser.busy();
	}
	
	@Override
	public int price() {
		return 20 + 10 * value;
	}
	
	@Override
	public String info() {
		return
			"Using a whetstone, you can repair your melee weapons, bringing them back to their former glory." +
            "\nThis whetstone can be used " + ( value > 2 ? "three times" : value < 2 ? "only one time" : "two times" ) + " more.";
	}

    @Override
    public String status() {
        return Utils.format( TXT_STATUS, value, limit );
    }

    @Override
    public String toString() {
        return super.toString() + " (" + status() +  ")" ;
    }
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
//				GameScene.show( new WndBalance( (Weapon)item ) );
                Whetstone.this.apply( (Weapon)item );
			}
		}
	};
	
//	public class WndBalance extends Window {
//
//		private static final String TXT_CHOICE = "How would you like to balance your %s?";
//
//		private static final String TXT_SPEED		= "For speed";
//		private static final String TXT_ACCURACY	= "For accuracy";
//		private static final String TXT_CANCEL		= "Never mind";
//
//		private static final int WIDTH			= 120;
//		private static final int MARGIN 		= 2;
//		private static final int BUTTON_WIDTH	= WIDTH - MARGIN * 2;
//		private static final int BUTTON_HEIGHT	= 20;
//
//		public WndBalance( final Weapon weapon ) {
//			super();
//
//			IconTitle titlebar = new IconTitle( weapon );
//			titlebar.setRect( 0, 0, WIDTH, 0 );
//			add( titlebar );
//
//			BitmapTextMultiline tfMesage = PixelScene.createMultiline( Utils.format( TXT_CHOICE, weapon.name() ), 8 );
//			tfMesage.maxWidth = WIDTH - MARGIN * 2;
//			tfMesage.measure();
//			tfMesage.x = MARGIN;
//			tfMesage.y = titlebar.bottom() + MARGIN;
//			add( tfMesage );
//
//			float pos = tfMesage.y + tfMesage.height();
//
//			if (weapon.imbue != Weapon.Imbue.SPEED) {
//				RedButton btnSpeed = new RedButton( TXT_SPEED ) {
//					@Override
//					protected void onClick() {
//						hide();
//						Whetstone.this.apply( weapon, true );
//					}
//				};
//				btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
//				add( btnSpeed );
//
//				pos = btnSpeed.bottom();
//			}
//
//			if (weapon.imbue != Weapon.Imbue.ACCURACY) {
//				RedButton btnAccuracy = new RedButton( TXT_ACCURACY ) {
//					@Override
//					protected void onClick() {
//						hide();
//						Whetstone.this.apply( weapon, false );
//					}
//				};
//				btnAccuracy.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
//				add( btnAccuracy );
//
//				pos = btnAccuracy.bottom();
//			}
//
//			RedButton btnCancel = new RedButton( TXT_CANCEL ) {
//				@Override
//				protected void onClick() {
//					hide();
//				}
//			};
//			btnCancel.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
//			add( btnCancel );
//
//			resize( WIDTH, (int)btnCancel.bottom() + MARGIN );
//		}
//
//		protected void onSelect( int index ) {};
//	}
}
