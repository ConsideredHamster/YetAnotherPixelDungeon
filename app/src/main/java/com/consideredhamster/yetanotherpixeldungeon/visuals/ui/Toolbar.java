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
package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Keyring;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndHero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndInfoCell;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndInfoItem;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndInfoMob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndMessage;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndTradeItem;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;

	public Tool btnQuick0;
	private Tool btnQuick1;
	private Tool btnQuick2;
	private Tool btnQuick3;

	private PickedUpItem pickedUp;
	
	private boolean lastEnabled = true;
	
	private static Toolbar instance;
	
	public Toolbar() {
		super();
		
		instance = this;
		
		height = btnInventory.height();
	}
	
	@Override
	protected void createChildren() {




        add( btnWait = new Tool( 0, 0, 20, 24 ) {
            @Override
            protected void onClick() {
                Dungeon.hero.rest( false );
            };

            protected boolean onLongClick() {
                Dungeon.hero.rest( true );
                return true;
            };
        } );

		add( btnWait = new Tool( 0, 0, 20, 24 ) {
			@Override
			protected void onClick() {
				Dungeon.hero.rest( false );
			};

			protected boolean onLongClick() {
				Dungeon.hero.rest( true );
				return true;
			};
		} );

		add( btnSearch = new Tool( 20, 0, 20, 24 ) {
			@Override
			protected void onClick() {

                if( YetAnotherPixelDungeon.searchButton() ) {
                    Dungeon.hero.search( true );
                } else {
                    if( GameScene.checkListener( informer ) ) {
                        Dungeon.hero.search( true );
                    } else {
                        GameScene.selectCell(informer);
                    }
                }

			};
            protected boolean onLongClick() {

                if( !YetAnotherPixelDungeon.searchButton() ) {
                    Dungeon.hero.search( true );
                } else {
                    GameScene.selectCell(informer);
                }

                return true;
            };
		} );

        add( btnSearch = new Tool( 20, 0, 20, 24 ) {
            @Override
            protected void onClick() {

                if( YetAnotherPixelDungeon.searchButton() ) {
                    Dungeon.hero.search( true );
                } else {
                    if( GameScene.checkListener( informer ) ) {
                        Dungeon.hero.search( true );
                    } else {
                        GameScene.selectCell(informer);
                    }
                }

            };
            protected boolean onLongClick() {

                if( !YetAnotherPixelDungeon.searchButton() ) {
                    Dungeon.hero.search( true );
                } else {
                    GameScene.selectCell(informer);
                }

                return true;
            };
        } );

        add( btnQuick0 = new QuickslotTool( 82, 0, 22, 24 ) );
        add( btnQuick1 = new QuickslotTool( 82, 0, 22, 24, 1 ) );
        add( btnQuick2 = new QuickslotTool( 82, 0, 22, 24, 2 ) );
        add( btnQuick3 = new QuickslotTool( 82, 0, 22, 24, 3 ) );

		add( btnInventory = new Tool( 60, 0, 22, 24 ) {
			private GoldIndicator gold;
			@Override
			protected void onClick() {
				GameScene.show( new WndBag( Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null ) );

			}
			protected boolean onLongClick() {
                GameScene.show( new WndBag( Dungeon.hero.belongings.getItem( Keyring.class ), null, WndBag.Mode.ALL, null ) );
				return true;
			};

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add( gold );
			};
			@Override
			protected void layout() {
				super.layout();
				gold.fill( this );
			};
		} );

		add( pickedUp = new PickedUpItem() );
	}
	
	@Override
	protected void layout() {
		btnWait.setPos(x, y);
		btnSearch.setPos( btnWait.right(), y );
        btnInventory.setPos( width - btnInventory.width(), y );

        btnQuick0.setPos( width - btnQuick0.width(), y - btnQuick0.height() );
        btnQuick3.setPos( btnInventory.left() - btnQuick3.width(), y );
        btnQuick2.setPos( btnQuick3.left() - btnQuick2.width(), y );
        btnQuick1.setPos( btnQuick2.left() - btnQuick1.width(), y );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (lastEnabled != Dungeon.hero.ready) {
			lastEnabled = Dungeon.hero.ready;
			
			for (Gizmo tool : members) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}
		
		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable( true );
		}
	}
	
	public void pickup( Item item ) {
		pickedUp.reset( item, 
			btnInventory.centerX(), 
			btnInventory.centerY() );
	}

    public static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {

			if (cell == null)
                return;

            if (cell < 0 || cell > Level.LENGTH || !Level.fieldOfView[cell] && !Dungeon.level.mapped[cell]) {
				GameScene.show( new WndMessage( "You don't know what is there." ) ) ;
				return;
			}
			
			if (Level.fieldOfView[cell] || Dungeon.level.mapped[cell]) {

                if (cell == Dungeon.hero.pos) {
                    GameScene.show(new WndHero());
                    return;
                }

                if( examineMob( cell ) )
                    return;

                if( examineHeap( cell ) )
                    return;

//                Plant plant = Dungeon.level.plants.get(cell);
//                if (plant != null) {
//                    GameScene.show(new WndInfoPlant(plant));
//                    return;
//                }
            }
			
			GameScene.show( new WndInfoCell( cell ) );
		}	
		@Override
		public String prompt() {
			return "Select a cell or tap again";
		}
	};

    public static boolean examineMob( int pos ) {

        Mob mob = (Mob) Actor.findChar( pos );
        if (mob != null && Dungeon.visible[ pos ]) {
            GameScene.show(new WndInfoMob(mob));
            return true;
        }

        return false;
    }

    public static boolean examineHeap( int pos ) {

        Heap heap = Dungeon.level.heaps.get( pos );
        if (heap != null) {
            if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
                GameScene.show( new WndTradeItem( heap, null ) );
            } else {
                GameScene.show( new WndInfoItem( heap ) );
            }
            return true;
        }

        return false;
    }
	
	public static class Tool extends Button {
		
		private static final int BGCOLOR = 0x7B8073;
		
		protected Image base;
		
		public Tool( int x, int y, int width, int height ) {
			super();
			
			base.frame( x, y, width, height );
			
			this.width = width;
			this.height = height;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			base = new Image( Assets.TOOLBAR );
			add( base );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}
		
		@Override
		protected void onTouchDown() {
			base.brightness( 1.4f );
		}
		
		@Override
		protected void onTouchUp() {
			if (active) {
				base.resetColorAlpha();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}
		
		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColorAlpha();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}
	
	private static class QuickslotTool extends Tool {
		
		private QuickSlot slot;

        public QuickslotTool( int x, int y, int width, int height ) {
            super( x, y, width, height );

            slot.setAsQuickSlot0();
        }

		public QuickslotTool( int x, int y, int width, int height, int index ) {
			super( x, y, width, height );

            switch ( index ) {
                case 1:
                    slot.setAsQuickSlot1();
                    break;
                case 2:
                    slot.setAsQuickSlot2();
                    break;
                case 3:
                    slot.setAsQuickSlot3();
                    break;
                default:
                    slot.setAsQuickSlot0();
                    break;
            }
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			slot = new QuickSlot();
			add( slot );
		}
		
		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + 1, y + 2, width - 2, height - 2 );
		}
		
		@Override
		public void enable( boolean value ) {
			slot.enable( value );
			super.enable( value );
		}
	}
	
	private static class PickedUpItem extends ItemSprite {
		
		private static final float DISTANCE = DungeonTilemap.SIZE;
		private static final float DURATION = 0.2f;
		
		private float dstX;
		private float dstY;
		private float left;
		
		public PickedUpItem() {
			super();
			
			originToCenter();
			
			active = 
			visible = 
				false;
		}
		
		public void reset( Item item, float dstX, float dstY ) {
			view( item.image(), item.glowing() );
			
			active = 
			visible = 
				true;
			
			this.dstX = dstX - ItemSprite.SIZE / 2;
			this.dstY = dstY - ItemSprite.SIZE / 2;
			left = DURATION;
			
			x = this.dstX - DISTANCE;
			y = this.dstY - DISTANCE;
			alpha( 1 );
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((left -= Game.elapsed) <= 0) {
				
				visible = 
				active = 
					false;
				
			} else {
				float p = left / DURATION; 
				scale.set( (float)Math.sqrt( p ) );
				float offset = DISTANCE * p;
				x = dstX - offset;
				y = dstY - offset;
			}
		}
	}
}
