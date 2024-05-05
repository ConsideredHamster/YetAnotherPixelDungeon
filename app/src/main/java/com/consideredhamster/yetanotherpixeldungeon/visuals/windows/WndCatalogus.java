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

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;

public class WndCatalogus extends WndTabbed {
	
	private static final int WIDTH_P	= 112;
	private static final int HEIGHT_P	= 160;
	
	private static final int WIDTH_L	= 128;
	private static final int HEIGHT_L	= 128;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private static final String TXT_TITLE	= "Journal";
	private static final String TXT_NOTES	= "Notes";
	private static final String TXT_PHARM	= "Potions";
	private static final String TXT_RUNES	= "Scrolls";
	private static final String TXT_WANDS	= "Wands";
	private static final String TXT_RINGS	= "Rings";
	
	private BitmapText txtTitle;
	private ScrollPane list;

    private enum Tabs {

        NOTES,
        PHARM,
        RUNES,
        WANDS,
        RINGS,

    };
	
	private ArrayList<Component> items = new ArrayList<>();
	
	private static Tabs currentTab;

	public WndCatalogus() {
		
		super();
		
		if (YetAnotherPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		add( txtTitle );
		
		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (
                        items.get(i) instanceof ListItem &&
                        ((ListItem)items.get( i )).onClick( x, y )
                    ) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );

		Tab[] tabs = {
            new JournalTab( TXT_NOTES, Icons.get( Icons.NOTES ) ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.NOTES;
                        updateList( title );
                    }
                };
            },
			new JournalTab( TXT_PHARM, Icons.get( Icons.POTIONS ) ) {
                @Override
				protected void select( boolean value ) {
					super.select( value );

                    if( value ) {
                        currentTab = Tabs.PHARM;
                        updateList(title);
                    }
				};
			},
            new JournalTab( TXT_RUNES, Icons.get( Icons.SCROLLS ) ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.RUNES;
                        updateList(title);
                    }
                };
            },
            new JournalTab( TXT_WANDS, Icons.get( Icons.WANDS ) ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.WANDS;
                        updateList(title);
                    }
                };
            },
            new JournalTab( TXT_RINGS, Icons.get( Icons.RINGS ) ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.RINGS;
                        updateList(title);
                    }
                };
            },
		};

        int tabWidth = ( width + 12 ) / tabs.length ;

        for (Tab tab : tabs) {
            tab.setSize( tabWidth, tabHeight() );
            add( tab );
        }
		
		select( 0 );
	}
	
	private void updateList( String title ) {
		
		txtTitle.text( title );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		
		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;

        switch( currentTab ) {

            case NOTES:
                Collections.sort(Journal.records);

                for (Journal.Record rec : Journal.records) {
                    NoteItem item = new NoteItem( rec.feature, rec.depth );
                    item.setRect( 0, pos, width, ITEM_HEIGHT );
                    content.add(item);
                    items.add(item);

                    pos += item.height();
                }
                break;

            case PHARM:
                for (Class<? extends Item> itemClass : Potion.getKnown()) {
                    ListItem item = new ListItem(itemClass);
                    item.setRect(0, pos, width, ITEM_HEIGHT);
                    content.add(item);
                    items.add(item);

                    pos += item.height();
                }
                break;

            case RUNES:
                for (Class<? extends Item> itemClass : Scroll.getKnown()) {
                    ListItem item = new ListItem(itemClass);
                    item.setRect(0, pos, width, ITEM_HEIGHT);
                    content.add(item);
                    items.add(item);

                    pos += item.height();
                }
                break;

            case WANDS:
                for (Class<? extends Item> itemClass : Wand.getKnown()) {
                    ListItem item = new ListItem(itemClass);
                    item.setRect(0, pos, width, ITEM_HEIGHT);
                    content.add(item);
                    items.add(item);

                    pos += item.height();
                }
                break;

            case RINGS:
                for (Class<? extends Item> itemClass : Ring.getKnown()) {
                    ListItem item = new ListItem(itemClass);
                    item.setRect(0, pos, width, ITEM_HEIGHT);
                    content.add(item);
                    items.add(item);

                    pos += item.height();
                }
                break;


        }
		
//		for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {
//			ListItem item = new ListItem( itemClass );
//			item.setRect( 0, pos, width, ITEM_HEIGHT );
//			content.add( item );
//			items.add( item );
//
//			pos += item.height();
//		}
		
		content.setSize( width, pos );
	}

    private class JournalTab extends Tab {

        public String title;
        private Image icon;

        public JournalTab( String t, Image i ) {
            super();

            title = t;
            icon = i;
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

//            icon.copy( icon() );
            icon.x = x + (width - icon.width) / 2;
            icon.y = y + (height - icon.height) / 2 - (selected ? 0 : 1);
            if (!selected && icon.y < y + CUT) {
                RectF frame = icon.frame();
                frame.top += (y + CUT - icon.y) / icon.texture.height;
                icon.frame( frame );
                icon.y = y + CUT;
            }
        }
    }

	private static class ListItem extends Component {

		private Item item;
//		private boolean identified;

		private ItemSprite sprite;
		private BitmapText label;

        public ListItem( Class<? extends Item> cl ) {
            super();

            try {
                item = cl.newInstance();

                if( item instanceof Wand) {
                    ((Wand)item).dud = true;
                } else if( item instanceof Ring) {
                    ((Ring)item).dud = true;
                }

//				if (identified = item.isIdentified()) {
                sprite.view( item.image(), null );
                label.text( item.name() );
//				} else {
//					sprite.view( 127, null );
//					label.text( item.trueName() );
//					label.hardlight( 0xCCCCCC );
//				}
            } catch (Exception e) {
                // Do nothing
            }
        }
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );
			
			label = PixelScene.createText( 8 );
			add( label );
		}
		
		@Override
		protected void layout() {
			sprite.y = PixelScene.align( y + (height - sprite.height) / 2 );
			
			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align( y + (height - label.baseLine()) / 2 );
		}

		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				GameScene.show( new WndInfoItem( item ) );
				return true;
			} else {
				return false;
			}
		}
	}

    private static class NoteItem extends Component {

        private BitmapText feature;
        private BitmapText depth;

        private Image icon;

        public NoteItem( Journal.Feature f, int d ) {
            super();

            feature.text( f.desc );
            feature.measure();

            depth.text( Integer.toString( d ) );
            depth.measure();

            if (d == Dungeon.depth) {
                feature.hardlight( TITLE_COLOR );
                depth.hardlight( TITLE_COLOR );
            }
        }

        @Override
        protected void createChildren() {
            feature = PixelScene.createText( 9 );
            add( feature );

            depth = new BitmapText( PixelScene.font1x );
            add( depth );

            icon = Icons.get( Icons.DEPTH );
            add( icon );
        }

        @Override
        protected void layout() {

            icon.x = width - icon.width;

            depth.x = icon.x - 1 - depth.width();
            depth.y = PixelScene.align( y + (height - depth.height()) / 2 );

            icon.y = depth.y - 1;

            feature.y = PixelScene.align( depth.y + depth.baseLine() - feature.baseLine() );
        }
    }
}
