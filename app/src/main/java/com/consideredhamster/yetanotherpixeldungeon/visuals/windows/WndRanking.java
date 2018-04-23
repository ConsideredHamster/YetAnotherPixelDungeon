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

import java.util.Locale;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Rankings;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Belongings;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BadgesList;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class WndRanking extends WndTabbed {
	
	private static final String TXT_ERROR		= "Unable to load additional information";
	
	private static final String TXT_STATS	= "Stats";
	private static final String TXT_ITEMS	= "Items";
	private static final String TXT_BADGES	= "Badges";
	
	private static final int WIDTH			= 112;
	private static final int HEIGHT			= 134;
	
	private static final int TAB_WIDTH	= 40;
	
	private Thread thread;
	private String error = null;
	
	private Image busy;
	private Rankings.Record rec;

	public WndRanking( Rankings.Record rec, final String gameFile ) {
		
		super();
		resize(WIDTH, HEIGHT);

        this.rec = rec;
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Dungeon.loadGame( gameFile );

                    if( Dungeon.hero == null ) {
                        throw new Exception();
                    }

				} catch (Exception e ) {
					error = TXT_ERROR;
				}
			}
		};
		thread.start();
		
		busy = Icons.BUSY.get();	
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive()) {
			thread = null;
			if (error == null) {
				remove( busy );
				createControls();
			} else {
				hide();
				Game.scene().add( new WndError( TXT_ERROR ) );
			}
		}
	}
	
	private void createControls() {
		
		String[] labels =
			{
                    TXT_STATS,
                    TXT_ITEMS,
                    TXT_BADGES,
            };
		Group[] pages =
			{
                    new StatsTab(),
                    new ItemsTab(),
                    new BadgesTab(),
            };

		for (int i=0; i < pages.length; i++) {

			add( pages[i] );

			Tab tab = new RankingTab( labels[i], pages[i] );
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}

		select( 0 );
	}

	private class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	private class StatsTab extends Group {
		
		private static final int GAP	= 4;
		
		private static final String TXT_TITLE	= "Level %d %s";
		
		private static final String TXT_CHALLENGES	= "Challenges";
		
		private static final String TXT_HEALTH	= "Health Amount";
		private static final String TXT_STR		= "Strength Achieved";

		private static final String TXT_SCORE	= "Score Points";
		private static final String TXT_DURATION= "Game Duration";
		private static final String TXT_DIFF	= "Difficulty";

		private static final String TXT_VERSION 	= "Mod Version";

		private static final String TXT_DEPTH	= "Maximum Depth";
		private static final String TXT_ENEMIES	= "Mobs Killed";
		private static final String TXT_GOLD	= "Gold Found";
		
		private static final String TXT_FOOD	= "Food Eaten";
		private static final String TXT_ALCHEMY	= "Potions Cooked";
		private static final String TXT_ANKHS	= "Ankhs Used";
		
		public StatsTab() {
			super();
			
			String heroClass = Dungeon.hero.className();
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( Dungeon.hero.heroClass, Dungeon.hero.appearance() ) );
			title.label( Utils.format( TXT_TITLE, Dungeon.hero.lvl, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );
			
			float pos = title.bottom();
			
			if (Dungeon.challenges > 0) {
				RedButton btnCatalogus = new RedButton( TXT_CHALLENGES ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}
				};
				btnCatalogus.setRect( 0, pos + GAP, btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
				add( btnCatalogus );
				
				pos = btnCatalogus.bottom();
			}
			
			pos += GAP + GAP;

            pos = statSlot( this, TXT_HEALTH, Integer.toString( Dungeon.hero.HT ), pos );
            pos = statSlot( this, TXT_STR, Integer.toString( Dungeon.hero.STR ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DEPTH, Integer.toString( Statistics.deepestFloor ), pos );
            pos = statSlot( this, TXT_ENEMIES, Integer.toString( Statistics.enemiesSlain ), pos );
            pos = statSlot( this, TXT_GOLD, Integer.toString( Statistics.goldCollected ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_FOOD, Integer.toString( Statistics.foodEaten ), pos );
            pos = statSlot( this, TXT_ALCHEMY, Integer.toString( Statistics.potionsCooked ), pos );
            pos = statSlot( this, TXT_ANKHS, Integer.toString( Statistics.ankhsUsed ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DURATION, Integer.toString( (int)Statistics.duration ), pos );
            pos = statSlot( this, TXT_SCORE, Integer.toString( rec.score ), pos );
            pos = statSlot( this, TXT_DIFF, Difficulties.NAMES[ rec.diff ], pos );

            pos += GAP;

            pos = statSlot( this, TXT_VERSION, !rec.version.isEmpty() ? rec.version : "too old", pos );

            pos += GAP;

            resize(WIDTH, (int)pos);

        }
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			BitmapText txt = PixelScene.createText( label, 7 );
			txt.y = pos;
			parent.add( txt );
			
			txt = PixelScene.createText( value, 6 );
			txt.measure();
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			parent.add( txt );
			
			return pos + GAP + txt.baseLine();
		}
	}
	
	private class ItemsTab extends Group {
		
		private int count;
		private float pos;

        private static final int GAP = 1;
		
		public ItemsTab() {
			super();
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weap1 != null) {
				addItem( stuff.weap1);
			} else {
                addItem( new Placeholder(ItemSpriteSheet.WEAP1) );
            }

            if (stuff.weap2 != null) {
                addItem( stuff.weap2);
            } else {
                addItem( new Placeholder(ItemSpriteSheet.WEAP2) );
            }

			if (stuff.armor != null) {
				addItem( stuff.armor );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.ARMOR) );
            }

			if (stuff.ring1 != null) {
				addItem( stuff.ring1 );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.RING) );
            }

			if (stuff.ring2 != null) {
				addItem( stuff.ring2 );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.RING) );
            }

//			Item setAsQuickSlot1 = getQuickslot( QuickSlot.quickslotValue_1 );
//			Item quickslot2 = getQuickslot( QuickSlot.quickslotValue_2 );
//
//			if (count >= 4 && setAsQuickSlot1 != null && quickslot2 != null) {
//
//				float size = ItemButton.SIZE;
//
//				ItemButton slot = new ItemButton( setAsQuickSlot1 );
//				slot.setRect( 0, pos, size, size );
//				add( slot );
//
//				slot = new ItemButton( quickslot2 );
//				slot.setRect( size + 1, pos, size, size );
//				add( slot );
//			} else {
//				if (setAsQuickSlot1 != null) {
//					addItem( setAsQuickSlot1 );
//				}
//				if (quickslot2 != null) {
//					addItem( quickslot2 );
//				}
//			}
		}
		
		private void addItem( Item item ) {
			LabelledItemButton slot = new LabelledItemButton( item );
			slot.setRect( 0, pos, width, LabelledItemButton.SIZE );
			add( slot );
			
			pos += slot.height() + GAP;
			count++;
		}
		
		private Item getQuickslot( Object value ) {
			if (value instanceof Item && Dungeon.hero.belongings.backpack.contains( (Item)value )) {
					
					return (Item)value;
					
			} else if (value instanceof Class){
				
				@SuppressWarnings("unchecked")
				Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)value );
				if (item != null) {
					return item;
				}
			}
			
			return null;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super();
			
			camera = WndRanking.this.camera;
			
			ScrollPane list = new BadgesList( false );
			add( list );
			
			list.setSize( WIDTH, WndRanking.this.height );
		}
	}
	
	private class ItemButton extends Button {
		
		public static final int SIZE	= 26;
		
		protected Item item;
		
		protected ItemSlot slot;
		private ColorBlock bg;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.bonus < 0 && item.isCursedKnown()) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}
		
		@Override
		protected void createChildren() {	
			
			bg = new ColorBlock( SIZE, SIZE, 0xFF4A4D44 );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, SIZE, SIZE );
			
			super.layout();
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
			if( !( item instanceof Placeholder )) {
                Game.scene().add(new WndItem(null, item));
            }
		}
	}

	private class LabelledItemButton extends ItemButton {
		private BitmapText name;
		
		public LabelledItemButton( Item item ) {
			super(item);
		}
		
		@Override
		protected void createChildren() {	
			super.createChildren();
			
			name = PixelScene.createText( "?", 7 );
			add(name);
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			name.x = slot.right() + 2;
			name.y = y + (height - name.baseLine()) / 2;
			
			String str = Utils.capitalize( item.name() );
			name.text( str );
			name.measure();
			if (name.width() > width - name.x) {
				do {
					str = str.substring( 0, str.length() - 1 );
					name.text( str + "..." );
					name.measure();
				} while (name.width() > width - name.x);
			}
		}
	}

    private static class Placeholder extends Item {
        {
            name = " ";
        }

        public Placeholder( int image ) {
            this.image = image;
        }

        @Override
        public boolean isEquipped( Hero hero ) {
            return true;
        }
    }
}
