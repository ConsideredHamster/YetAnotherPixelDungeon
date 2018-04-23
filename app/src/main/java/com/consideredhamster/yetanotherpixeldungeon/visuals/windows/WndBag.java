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

import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorCloth;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Belongings;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Keyring;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.PotionSash;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.ScrollHolder;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.HerbPouch;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.WandHolster;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.noosa.ui.Button;

import java.util.ArrayList;
import java.util.Iterator;

public class WndBag extends WndTabbed {


    private static final String TXT_SELECT_TITLE = "Select quickslot";
    private static final String TXT_SELECT_CANCEL = "Nevermind";
    private static final String TXT_SELECT_MESSAGE =
            "Which quickslot do you want to set for this item?";

	public static enum Mode {
		ALL,
		UNIDENTIFED,
		UPGRADEABLE,
		REPAIRABLE,
		QUICKSLOT,
		OFFHAND,
		FOR_SALE,
		WEAPON,
        WHETSTONE,
        CRAFTING_KIT,
        ARMORERS_KIT,
        ARCANE_BATTERY,
		ENCHANTABLE,
		TRANSMUTABLE,
		CURSED,
		WAND,
        HERB,
        KEYS
	}

    protected static final int COLS_P	= 5;
    protected static final int ROWS_P	= 6;

	protected static final int COLS_L	= 7;
    protected static final int ROWS_L	= 4;

	protected static final int SLOT_SIZE	= 24;
	protected static final int SLOT_MARGIN	= 1;

	protected static final int TITLE_HEIGHT	= 16;

	protected static final int DURABILITY_COLORS[] = {
            0xFFCC0000,
            0xFFFF5500,
            0xFFFF8800,
            0xFFEEEE00,
            0xFFBBCC00,
            0xFF00EE00,
            0xFF00EE00,
    };

	private Listener listener;
	private WndBag.Mode mode;
	private String title;

	private int nCols;
	private int nRows;

	protected int count;
	protected int col;
	protected int row;

	private static Mode lastMode;
	private static Bag lastBag;

	public WndBag( Bag bag, Listener listener, Mode mode, String title ) {

		super();

		this.listener = listener;
		this.mode = mode;
		this.title = title;

		lastMode = mode;
		lastBag = bag;

		nCols = YetAnotherPixelDungeon.landscape() ? COLS_L : COLS_P;
		nRows = YetAnotherPixelDungeon.landscape() ? ROWS_L : ROWS_P ;
//		nRows = (Belongings.BACKPACK_SIZE + 4 + 1) / nCols + ((Belongings.BACKPACK_SIZE + 4 + 1) % nCols > 0 ? 1 : 0) + ( !YetAnotherPixelDungeon.landscape() ? 1 : 0 );

		int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);

		placeTitle( bag, slotsWidth, mode == Mode.ALL || mode == Mode.FOR_SALE, mode == Mode.ALL );
		placeItems( bag );

		resize( slotsWidth, slotsHeight + TITLE_HEIGHT );

		Belongings stuff = Dungeon.hero.belongings;

		ArrayList<Bag> bags = new ArrayList<>();

        bags.add( stuff.backpack );
        bags.add( stuff.getItem( Keyring.class ) );
        bags.add( stuff.getItem( HerbPouch.class ) );
        bags.add( stuff.getItem( PotionSash.class ) );
        bags.add( stuff.getItem( ScrollHolder.class ) );
        bags.add( stuff.getItem( WandHolster.class ) );

        while(bags.remove(null));

        int tabWidth = ( slotsWidth + 12 ) / bags.size() ;

		for (Bag b : bags) {
            BagTab tab = new BagTab( b );
            tab.setSize( tabWidth, tabHeight() );
            add( tab );

            tab.select( b == bag );
		}
	}
	
	public static WndBag lastBag( Listener listener, Mode mode, String title ) {
		
		if (mode == lastMode && lastBag != null && 
			Dungeon.hero.belongings.backpack.contains( lastBag )) {
			
			return new WndBag( lastBag, listener, mode, title );
			
		} else {
			
			return new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
			
		}
	}
	
	public static WndBag herbPouch(Listener listener, Mode mode, String title) {
		HerbPouch pouch = Dungeon.hero.belongings.getItem( HerbPouch.class );
		return pouch != null ?
			new WndBag( pouch, listener, mode, title ) :
			new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
	}

	protected void placeTitle( Bag bag, int width, boolean showGold, boolean showAnkhs ) {

        float pos = 0;

        BitmapText txtTitle = PixelScene.createText( title != null ? title : Utils.capitalize( bag.name() ), 9 );
        txtTitle.hardlight( TITLE_COLOR );
        txtTitle.measure();
        txtTitle.x = 1;
//        txtTitle.x = (int)(slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int)(TITLE_HEIGHT - txtTitle.height()) / 2;
        add( txtTitle );

         if( showGold ){

             Image goldIcon = Icons.get( Icons.GOLD );
             goldIcon.x = width - goldIcon.width() - 1;
             goldIcon.y = ( TITLE_HEIGHT - goldIcon.height() ) / 2f - 1;
             add( goldIcon );


             BitmapText goldText = new BitmapText( Integer.toString( Dungeon.gold ), PixelScene.font1x );
             goldText.measure();
             goldText.x = width - goldIcon.width() - goldText.width() - 3;
             goldText.y = ( TITLE_HEIGHT - goldText.baseLine() ) / 2f - 1;
             add( goldText );

             TouchArea goldArea = new TouchArea( goldText.x, goldText.y, goldIcon.width() + goldText.width(), goldIcon.height() + goldText.height() ) {
                 @Override
                 protected void onClick( Touchscreen.Touch touch ){
                     WndBag.this.add( new WndItem( WndBag.this, new Gold( Dungeon.gold ) ) );
                 }
             };
             add( goldArea );

             pos += goldIcon.width() + goldText.width();
         }

         if( showAnkhs ){
             final Ankh ankh = Dungeon.hero.belongings.getItem( Ankh.class );
             if( ankh != null ){

                 Image ankhIcon = Icons.get( Icons.ANKH );
                 ankhIcon.x = width - ankhIcon.width() - pos - 8;
                 ankhIcon.y = ( TITLE_HEIGHT - ankhIcon.height() ) / 2f - 1;
                 add( ankhIcon );

                 BitmapText ankhText = new BitmapText( Integer.toString( ankh.quantity ), PixelScene.font1x );
                 ankhText.measure();
                 ankhText.measure();
                 ankhText.x = width - ankhIcon.width() - ankhText.width() - pos - 8;
                 ankhText.y = ( TITLE_HEIGHT - ankhText.baseLine() ) / 2f - 1;
                 add( ankhText );

                 TouchArea ankhArea = new TouchArea( ankhText.x, ankhText.y, ankhIcon.width() + ankhText.width(), ankhIcon.height() + ankhText.height() ) {
                     @Override
                     protected void onClick( Touchscreen.Touch touch ){
                         WndBag.this.add( new WndItem( WndBag.this, ankh ) );
                     }
                 };
                 add( ankhArea );

             }
         }
    }
	
	protected void placeItems( Bag container ) {

        Belongings stuff = Dungeon.hero.belongings;

//        Gold gold = new Gold( Dungeon.gold );

//        Ankh ankh = stuff.getItem( Ankh.class );
        Waterskin vial = stuff.getItem( Waterskin.class );
        OilLantern lamp = stuff.getItem( OilLantern.class );

        if( YetAnotherPixelDungeon.landscape() ) {

            placeItem( container );

        }

        placeItem(stuff.weap1 != null ? stuff.weap1 : new Placeholder(ItemSpriteSheet.WEAP1));
        placeItem( stuff.weap2 != null ? stuff.weap2 : new Placeholder( ItemSpriteSheet.WEAP2 ) );
        placeItem( stuff.armor != null ? stuff.armor : new Placeholder( ItemSpriteSheet.ARMOR ) );
        placeItem( stuff.ring1 != null ? stuff.ring1 : new Placeholder( ItemSpriteSheet.RING ) );
        placeItem( stuff.ring2 != null ? stuff.ring2 : new Placeholder( ItemSpriteSheet.RING ) );

        if( !YetAnotherPixelDungeon.landscape() ) {

            placeItem( container );

        } else {

            row = 1;
            col = 0;

        }

        count = 0;

        Iterator<Item> iterator = container.items.iterator();

        while (count < container.size) {

            if (row == nRows - 1 && col == 0) {
                col++;
            }

            if (iterator.hasNext()){
                Item item = iterator.next();

                if (item == null || item.visible) {

                    placeItem(item);
                    count++;

                }

            } else{
                placeItem(null);
                count++;
            }
        }

        if( YetAnotherPixelDungeon.landscape() ) {

//            if( ankh != null ) {
//                row = 0;
//                col = nCols - 1;
//                placeItem( ankh );
//            }

            if( vial != null ) {
                row = nRows - 1;
                col = 0;
                placeItem( vial );
            }

            if( lamp != null ) {
                row = nRows - 1;
                col = nCols - 1;
                placeItem( lamp );
            }

        } else {

            row = nRows - 1;

//            if( ankh != null ) {
//                col = 2;
//                placeItem( ankh );
//            }

            if( vial != null ) {
                col = 3;
                placeItem( vial );
            }

            if( lamp != null ) {
                col = 4;
                placeItem( lamp );
            }

        }
	}
	
	protected void placeItem( final Item item ) {
		
		int x = col * (SLOT_SIZE + SLOT_MARGIN);
		int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN) + ( row > 0 ? 1 : 0 );
		
		add( new ItemButton( item ).setPos( x, y ) );
		
		if (++col >= nCols) {
			col = 0;
			row++;
		}
	}
	
	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onClick( Tab tab ) {
		hide();
		GameScene.show( new WndBag( ((BagTab)tab).bag, listener, mode, title ) );
	}
	
	@Override
	protected int tabHeight() {
		return 24;
	}
	
	private class BagTab extends Tab {
		
		private Image icon;

		private Bag bag;
		
		public BagTab( Bag bag ) {
			super();
			
			this.bag = bag;
			
			icon = Icons.get( bag.icon()  );
			add( icon );
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			icon.am = selected ? 1.0f : 0.6f;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			icon.copy( Icons.get( bag.icon() ) );
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 + 1 - (selected ? 0 : 1);

//			if (!selected && icon.y < y + CUT) {
//				RectF frame = icon.frame();
//				frame.top += (y + CUT - icon.y) / icon.texture.height;
//				icon.frame( frame );
//				icon.y = y + CUT;
//			}
		}
	}
	
	private static class Placeholder extends Item {		
		{
			name = null;
		}
		
		public Placeholder( int image ) {
			this.image = image;
		}
		
		@Override
		public boolean isEquipped( Hero hero ) {
			return true;
		}
	}
	
	private class ItemButton extends ItemSlot {
		
		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;
		private static final int NBARS	= 3;
		
		private Item item;
		private ColorBlock bg;
		
//		private ColorBlock hp[];

        private Image state[];

        private SmartTexture texture;
        private TextureFilm film;
		
		public ItemButton( Item item ) {
			
			super( item );

			this.item = item;

            if( item !=  null ) {
                bg.visible = item.visible || item instanceof Bag;
            }
			
			width = height = SLOT_SIZE;
		}
		
		@Override
		protected void createChildren() {	
			bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			if (state != null) {
				for (int i=0; i < NBARS; i++) {

					state[i].x = x + 1 + i * 4;
					state[i].y = y + height - 3;
				}
			}
			
			super.layout();
		}
		
		@Override
		public void item( Item item ) {
			
			super.item(item);
			if (item != null) {

				bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.hero ) ? EQUIPPED : NORMAL ) );

                // FIXME

                if (item instanceof Bag) {
                    bg.ra = +0.2f;
                    bg.ga = +0.2f;
                    bg.ba = +0.2f;
                } else if ( !item.isCursedKnown() || !(item instanceof EquipableItem) && !item.isIdentified() ) {
                    bg.ra = +0.15f;
                    bg.ba = +0.15f;
                } else if ( !item.isIdentified() || item.bonus < 0) {

                    if( item.bonus < 0 ) {
                        bg.ra = +0.2f;
                        bg.ga = -0.1f;
                    } else {
                        bg.ba = +0.2f;
                        bg.ra = +0.05f;
//                        bg.ga = -0.1f;
                    }
                }
				
				if ( item.maxDurability() > 0 ) {

					state = new Image[NBARS];

					for (int i=0; i < item.state; i++) {

                        int index = item.durability() * 5 / item.maxDurability() + ( item.durability() % ( item.maxDurability() / 5 )  > 0 ? 1 : 0 ) ;

						state[i] = new ColorBlock( 3, 2, ( i + 1 < item.state || index >= DURABILITY_COLORS.length ? DURABILITY_COLORS[5] : DURABILITY_COLORS[ index ] ) );
						add( state[i] );
					}
					for (int i=item.state; i < NBARS; i++) {
                        state[i] = new ColorBlock( 3, 2, DURABILITY_COLORS[0] );
						add( state[i] );
					}
				}
				
				if (item.name() == null) {
					enable( false );
				} else {
					enable( 
						mode == Mode.QUICKSLOT && (item.quickAction() != null) ||
						mode == Mode.OFFHAND && (item instanceof MeleeWeaponLightOH || item instanceof ThrowingWeapon || item instanceof Shield || item instanceof Wand) ||
						mode == Mode.FOR_SALE && (item.visible && item.price() > 0) && (!item.isEquipped( Dungeon.hero ) || item.bonus >= 0) ||
						mode == Mode.UPGRADEABLE && ( item.isUpgradeable() && ( !item.isIdentified() || item.bonus < 3 ) || item.isRepairable() && item.state < 3 ) ||
						mode == Mode.ENCHANTABLE && item instanceof EquipableItem && !( item instanceof ThrowingWeapon ) ||
						mode == Mode.REPAIRABLE && item.isRepairable() && item.state < 3 ||
						mode == Mode.UNIDENTIFED && !item.isIdentified() ||
						mode == Mode.WEAPON && item instanceof Weapon && item.isRepairable() && item.state < 3 ||
						mode == Mode.WHETSTONE && (item instanceof MeleeWeapon && item.state < 3) ||
						mode == Mode.CRAFTING_KIT && ((item instanceof RangedWeapon || item instanceof BodyArmorCloth)&& item.state < 3) ||
						mode == Mode.ARMORERS_KIT && (item instanceof Armour && !(item instanceof BodyArmorCloth) && item.state < 3) ||
						mode == Mode.ARCANE_BATTERY && (item instanceof Wand && item.state < 3) ||
						mode == Mode.TRANSMUTABLE && item instanceof EquipableItem ||
						mode == Mode.WAND && (item instanceof Wand) ||
						mode == Mode.HERB && (item instanceof Herb) ||
						mode == Mode.ALL
					);
				}
			} else {
				bg.color( NORMAL );
			}
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
			if (listener != null) {
				
				hide();
				listener.onSelect( item );
				
			} else {
				
				WndBag.this.add( new WndItem( WndBag.this, item ) );
				
			}
		}
		
		@Override
		protected boolean onLongClick() {
			if (listener == null && item.quickAction() != null) {

                if( QuickSlot.quickslot1.select() == null ) {
                    QuickSlot.quickslot1.value = item.stackable ? item.getClass() : item;
                    hide();
                } else if( QuickSlot.quickslot2.select() == null ) {
                    QuickSlot.quickslot2.value = item.stackable ? item.getClass() : item;
                    hide();
                } else if( QuickSlot.quickslot3.select() == null ) {
                    QuickSlot.quickslot3.value = item.stackable ? item.getClass() : item;
                    hide();
                } else {
                    YetAnotherPixelDungeon.scene().add(
                        new WndOptions( TXT_SELECT_TITLE, TXT_SELECT_MESSAGE,
                                Utils.capitalize( QuickSlot.quickslot1.value instanceof Item ? QuickSlot.quickslot1.value.toString() :
                                        Item.virtual( (Class<? extends Item>)QuickSlot.quickslot1.value).toString() ),
                                Utils.capitalize( QuickSlot.quickslot2.value instanceof Item ? QuickSlot.quickslot2.value.toString() :
                                        Item.virtual( (Class<? extends Item>)QuickSlot.quickslot2.value).toString() ),
                                Utils.capitalize( QuickSlot.quickslot3.value instanceof Item ? QuickSlot.quickslot3.value.toString() :
                                        Item.virtual( (Class<? extends Item>)QuickSlot.quickslot3.value).toString() ),
                                TXT_SELECT_CANCEL
                        ) {

                            @Override
                            protected void onSelect(int index) {

                                switch (index) {
                                    case 0:
                                        QuickSlot.quickslot1.value = null;
                                        break;
                                    case 1:
                                        QuickSlot.quickslot2.value = null;
                                        break;
                                    case 2:
                                        QuickSlot.quickslot3.value = null;
                                        break;
                                }

                                if( index < 3 ) {
                                    onLongClick();
                                }
                            }
                        }
                    );
                }


				QuickSlot.refresh();
				return true;
			} else {
				return false;
			}
		}
	}
	
	public interface Listener {
		void onSelect( Item item );
	}
}
