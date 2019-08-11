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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.watabou.noosa.BitmapTextMultiline;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.utils.Random;

public class WndTradeItem extends Window {
	
	private static final float GAP		= 2;
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 16;
	
	private static final String TXT_SALE		= "FOR SALE: %s - %dg";
	private static final String TXT_BUY			= "Buy for %dg";
	private static final String TXT_SELL		= "Sell for %dg";
	private static final String TXT_SELL_1		= "Sell 1 for %dg";
	private static final String TXT_SELL_ALL	= "Sell all for %dg";
	private static final String TXT_CANCEL		= "Never mind";

    private static final String TXT_BOUGHT	= "You've bought %s for %dg";
    private static final String TXT_SOLD	= "You've sold your %s for %dg";

    private static final String TXT_STEAL	    = "Steal (%d%% chance)";
    private static final String TXT_STOLEN	    = "You successfully steal %s!";
    private static final String TXT_CAUGHT	    = "You fail to steal %s.";

	private WndBag owner;

    private BitmapTextMultiline normal;
    private BitmapTextMultiline highlighted;
	
	public WndTradeItem( final Item item, WndBag owner ) {
		
		super();
		
		this.owner = owner; 
		
		float pos = createDescription( item, false );
		
		if (item.quantity() == 1) {
			
			RedButton btnSell = new RedButton( Utils.format( TXT_SELL, item.price() ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell );
			
			pos = btnSell.bottom();
			
		} else {
			
			int priceAll= item.price();
			RedButton btnSell1 = new RedButton( Utils.format( TXT_SELL_1, priceAll / item.quantity() ) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Utils.format( TXT_SELL_ALL, priceAll ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnSellAll );
			
			pos = btnSellAll.bottom();
			
		}
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	public WndTradeItem( final Heap heap, final Shopkeeper shopkeeper ) {
		
		super();
		
		Item item = heap.peek();

        if( !item.isIdentified() )
            item.identify();
		
		float pos = createDescription( item, true );


        if ( shopkeeper != null ) {

            int price = price( item );

            RedButton btnBuy = new RedButton( Utils.format( TXT_BUY, price ) ) {
                @Override
                protected void onClick() {
                    hide();
                    buy( heap );
                }
            };
            btnBuy.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
            btnBuy.enable( price <= Dungeon.gold );
            add( btnBuy );

            float chance = shopkeeper.stealingChance( item );

            RedButton btnSteal = new RedButton( Utils.format( TXT_STEAL, (int)( chance * 100 ) ) ) {
                @Override
                protected void onClick() {
                    hide();
                    steal( heap, shopkeeper );
                }
            };
            btnSteal.setRect( 0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT );
            btnSteal.enable( chance > 0.0f );
            add( btnSteal );
			
			RedButton btnCancel = new RedButton( TXT_CANCEL ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect( 0, btnSteal.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnCancel );
			
			resize( WIDTH, (int)btnCancel.bottom() );
			
		} else {
			
			resize( WIDTH, (int)pos );
			
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		if (owner != null) {
			owner.hide();
			Shopkeeper.sell();
		}
	}
	
	private float createDescription( Item item, boolean forSale ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), item.glowing() ) );
		titlebar.label( forSale ? 
			Utils.format( TXT_SALE, item.toString(), price( item ) ) : 
			Utils.capitalize( item.toString() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		if (item.isIdentified() && item.bonus > 0) {
			titlebar.color( ItemSlot.UPGRADED );	
		} else if (item.isIdentified() && item.bonus < 0) {
			titlebar.color( ItemSlot.DEGRADED );	
		}

        Highlighter hl = new Highlighter( item.info() );

        normal = PixelScene.createMultiline( hl.text, 6 );
        normal.maxWidth = WIDTH;
        normal.measure();
        normal.x = titlebar.left();
        normal.y = titlebar.bottom() + GAP;
        add( normal );

        if (hl.isHighlighted()) {
            normal.mask = hl.inverted();

            highlighted = PixelScene.createMultiline( hl.text, 6 );
            highlighted.maxWidth = normal.maxWidth;
            highlighted.measure();
            highlighted.x = normal.x;
            highlighted.y = normal.y;
            add( highlighted );

            highlighted.mask = hl.mask;
            highlighted.hardlight( TITLE_COLOR );
        }
		
//		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
//		info.maxWidth = WIDTH;
//		info.measure();
//		info.x = titlebar.left();
//		info.y = titlebar.bottom() + GAP;
//		add( info );
//
//		return info.y + info.height();
		return normal.y + normal.height();
	}
	
	private void sell( Item item ) {
		
		Hero hero = Dungeon.hero;
		
		if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false, false )) {
			return;
		}

		item.detachAll( hero.belongings.backpack );
		
		int price = item.price();
		
		new Gold( price ).doPickUp( hero );
		GLog.i( TXT_SOLD, item.name(), price );
	}
	
	private void sellOne( Item item ) {
		
		if (item.quantity() <= 1) {
			sell( item );
		} else {
			
			Hero hero = Dungeon.hero;
			
			item = item.detach( hero.belongings.backpack );
			int price = item.price();
			
			new Gold( price ).doPickUp( hero );
			GLog.i( TXT_SOLD, item.name(), price );
		}
	}
	
	private int price( Item item ) {

        int price = item.price() * item.priceModifier() * Dungeon.chapter();

		return price;
	}

    private void buy( Heap heap ) {

        Hero hero = Dungeon.hero;
        Item item = heap.pickUp();

        int price = price( item );
        Dungeon.gold -= price;

        GLog.i( TXT_BOUGHT, item.name(), price );

        if (!item.doPickUp( hero )) {
            Dungeon.level.drop( item, heap.pos ).sprite.drop();
        }
    }

    private void steal( Heap heap, Shopkeeper shopkeeper ) {

        Hero hero = Dungeon.hero;

        Item item = heap.peek();

        if( Random.Float() < shopkeeper.stealingChance( item ) ){

            item = heap.pickUp();

            if (!item.doPickUp( hero )) {
                Dungeon.level.drop( item, heap.pos ).sprite.drop();
            }

            GLog.i( TXT_STOLEN, item.name() );
            Invisibility.dispel( hero );
            shopkeeper.onStealing();

        } else {

            GLog.w( TXT_CAUGHT, item.name() );
            Invisibility.dispel( hero );
            shopkeeper.onCaught();

        }
    }
}
