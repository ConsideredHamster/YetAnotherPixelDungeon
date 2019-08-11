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

import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;
import com.watabou.utils.Bundle;

public class QuickSlot extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM_QUICKSLOT = "Select an item for the quickslot";
	private static final String TXT_SELECT_ITEM_OFFHAND = "Select an item for the offhand";

    public static QuickSlot quickslot0;
    public static QuickSlot quickslot1;
    public static QuickSlot quickslot2;
    public static QuickSlot quickslot3;

    public Object value;
    private boolean targeting = false;
    private Char currentTarget = null;

    private Item itemInSlot;
    private ItemSlot slot;

    private Image crossB;
    private Image crossM;
//    private Item lastItem = null;

    public static Object quickslotValue_1;
    public static Object quickslotValue_2;
    public static Object quickslotValue_3;

    public void setAsQuickSlot0() {

        this.value = select();
        quickslot0 = this;
        item( select() );
    }

	public void setAsQuickSlot1() {
        this.value = quickslotValue_1;
		quickslot1 = this;
		item( select() );
	}
	
	public void setAsQuickSlot2() {
        this.value = quickslotValue_2;
		quickslot2 = this;
		item( select() );
	}

    public void setAsQuickSlot3() {
        this.value = quickslotValue_3;
        quickslot3 = this;
        item( select() );
    }
	
	@Override
	public void destroy() {
		super.destroy();

        if (this == quickslot1) {
            quickslot1 = null;
        } else if (this == quickslot2) {
            quickslot2 = null;
        } else  if (this == quickslot3) {
            quickslot3 = null;
        } else {
			quickslot0 = null;
		}
		
//		lastItem = null;
		currentTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					GameScene.handleCell( currentTarget.pos );
				} else {
					Item item = select();

                    cancel();

//					if (item == lastItem) {
//						useTargeting();
//					} else {
//						lastItem = item;
//					}
                    if ( item != null ) {
                        item.execute( Dungeon.hero, QuickSlot.this == quickslot0 && item.equipAction() != null ? item.equipAction() : item.quickAction() );

                        if( !GameScene.checkListener() ) {
                            useTargeting();
                        }
                    }
				}
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlot.this.onLongClick();
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColorAlpha();
			}
		};

        slot.setScale(0.8f);

		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add(crossB);

		crossM = new Image();
		crossM.copy(crossB);
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill(this);
		
		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
	}
	
	@Override
	protected void onClick() {
        if( this == quickslot0 ) {
            GameScene.selectItem(this, WndBag.Mode.OFFHAND, TXT_SELECT_ITEM_OFFHAND);
        } else {
            GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM_QUICKSLOT);
        }
	}
	
	@Override
	protected boolean onLongClick() {
        if( this == quickslot0 ) {
            GameScene.selectItem(this, WndBag.Mode.OFFHAND, TXT_SELECT_ITEM_OFFHAND);
        } else {
            GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM_QUICKSLOT);
        }
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Item select() {

        if( this == quickslot0 ) {

            value = (Dungeon.hero.belongings.weap2 == null || Dungeon.hero.belongings.weap1 instanceof RangedWeapon &&
                    ((RangedWeapon) Dungeon.hero.belongings.weap1).ammunition().isInstance(Dungeon.hero.belongings.weap2) ?
                    Dungeon.hero.belongings.weap1 : Dungeon.hero.belongings.weap2);

        } else if ( this == quickslot1 ) {

            quickslotValue_1 = value;

        } else if ( this == quickslot2 ) {

            quickslotValue_2 = value;

        } else if ( this == quickslot3 ) {

            quickslotValue_3 = value;

        }

		if (value instanceof Item) {
			
			return (Item)value;
			
		} else if (value != null) {
			
			Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)value );
			return item != null ? item : Item.virtual( (Class<? extends Item>)value );
			
		} else {
			
			return null;
			
		}
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {

            if( this == quickslot0 && item instanceof EquipableItem ) {

                EquipableItem equipable = ((EquipableItem)item);

                if( !equipable.isEquipped( Dungeon.hero) ) {

                    if (equipable instanceof MeleeWeaponLightOH) {
                        ((MeleeWeaponLightOH) item).doEquipSecondary(Dungeon.hero);
                    } else {
                        equipable.execute(Dungeon.hero, EquipableItem.AC_EQUIP );
                    }

                } else {

                    equipable.execute(Dungeon.hero, EquipableItem.AC_UNEQUIP );

                }
            } else {
                value = (item.stackable ? item.getClass() : item);
            }

//			if (this == setAsQuickSlot1) {
//				quickslotValue_1 = (item.stackable ? item.getClass() : item);
//			} else if (this == quickslot2) {
//				quickslotValue_2 = (item.stackable ? item.getClass() : item);
//			}

			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
        slot.showStatus( item != null );
		itemInSlot = item;
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable( 
			itemInSlot != null && 
			itemInSlot.quantity() > 0 && 
			(Dungeon.hero.belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.hero )));
	}
	
	private void useTargeting() {
		
//		targeting = currentTarget != null && currentTarget.isAlive() && Dungeon.visible[currentTarget.pos];

        if ( currentTarget != null && ( !currentTarget.isAlive() || !Dungeon.visible[currentTarget.pos] ) ) {
            currentTarget = null;
        }

        if ( currentTarget == null ) {

            int distance = 0;

            for (Mob mob : Dungeon.level.mobs) {
                if ( mob.hostile && Dungeon.visible[mob.pos]
                        && ( Ballistica.cast( Dungeon.hero.pos, mob.pos, false, true ) == mob.pos )
                        && ( distance == 0 || Level.distance(Dungeon.hero.pos, mob.pos) < distance )
                        ) {
                    currentTarget = mob;
                    distance = Level.distance(Dungeon.hero.pos, currentTarget.pos);
                }
            }
        }

		if ( currentTarget != null ) {
			if (Actor.all().contains(currentTarget) ) {
				currentTarget.sprite.parent.add(crossM);
                crossM.point(DungeonTilemap.tileToWorld(currentTarget.pos));
				crossB.visible = true;
                targeting = true;
			} else {
				currentTarget = null;
			}
		}
	}


	public static void refresh() {

        if (quickslot0 != null) {
            quickslot0.item( quickslot0.select() );
        }
		if (quickslot1 != null) {
			quickslot1.item( quickslot1.select() );
		}
        if (quickslot2 != null) {
            quickslot2.item( quickslot2.select() );
        }
        if (quickslot3 != null) {
            quickslot3.item( quickslot3.select() );
        }
	}
	
	public static void target( Item item, Char target ) {
		if (target != Dungeon.hero) {

            quickslot0.currentTarget = target;
            quickslot1.currentTarget = target;
            quickslot2.currentTarget = target;
            quickslot3.currentTarget = target;
            HealthIndicator.instance.target(target);

        }
	}
	
	public static void cancel() {
        if (quickslot0 != null && quickslot0.targeting) {
            quickslot0.crossB.visible = false;
            quickslot0.crossM.remove();
            quickslot0.targeting = false;
        }
		if (quickslot1 != null && quickslot1.targeting) {
			quickslot1.crossB.visible = false;
			quickslot1.crossM.remove();
            quickslot1.targeting = false;
		}
        if (quickslot2 != null && quickslot2.targeting) {
            quickslot2.crossB.visible = false;
            quickslot2.crossM.remove();
            quickslot2.targeting = false;
        }
        if (quickslot3 != null && quickslot3.targeting) {
            quickslot3.crossB.visible = false;
            quickslot3.crossM.remove();
            quickslot3.targeting = false;
        }
	}

	private static final String QUICKSLOT1	= "quickslot1";
	private static final String QUICKSLOT2	= "quickslot2";
	private static final String QUICKSLOT3	= "quickslot3";

	@SuppressWarnings("unchecked")
	public static void save( Bundle bundle ) {
//		Belongings stuff = Dungeon.hero.belongings;

		if (quickslot1 != null && quickslot1.value instanceof Class) {
            quickslotValue_1 = quickslot1.value;
			bundle.put( QUICKSLOT1, ((Class<?>) quickslot1.value).getName() );
		}

        if (quickslot2 != null && QuickSlot.quickslot2.value instanceof Class) {
            quickslotValue_2 = quickslot2.value;
            bundle.put( QUICKSLOT2, ((Class<?>) quickslot2.value).getName() );
        }

        if (quickslot3 != null && QuickSlot.quickslot3.value instanceof Class) {
            quickslotValue_3 = quickslot3.value;
            bundle.put( QUICKSLOT3, ((Class<?>) quickslot3.value).getName() );
        }
	}
	
	public static void save( Bundle bundle, Item item ) {
		if (quickslot1 != null && item == quickslot1.value) {
            quickslotValue_1 = quickslot1.value;
			bundle.put( QuickSlot.QUICKSLOT1, true );
		}

        if (quickslot2 != null && item == quickslot2.value) {
            quickslotValue_2 = quickslot2.value;
            bundle.put( QuickSlot.QUICKSLOT2, true );
        }

        if (quickslot3 != null && item == quickslot3.value) {
            quickslotValue_3 = quickslot3.value;
            bundle.put( QuickSlot.QUICKSLOT3, true );
        }
	}
	
	public static void restore( Bundle bundle ) {

        quickslotValue_1 = null;
        quickslotValue_2 = null;
        quickslotValue_3 = null;

		String qsClass = bundle.getString( QUICKSLOT1 );
		if (qsClass != null) {
			try {
                quickslotValue_1 = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		}

        qsClass = bundle.getString( QUICKSLOT2 );
        if (qsClass != null) {
            try {
                quickslotValue_2 = Class.forName( qsClass );
            } catch (ClassNotFoundException e) {
            }
        }

        qsClass = bundle.getString( QUICKSLOT3 );
        if (qsClass != null) {
            try {
                quickslotValue_3 = Class.forName( qsClass );
            } catch (ClassNotFoundException e) {
            }
        }
	}
	
	public static void restore( Bundle bundle, Item item ) {
		if (bundle.getBoolean( QUICKSLOT1 )) {
            quickslotValue_1 = item;
		}
        if (bundle.getBoolean( QUICKSLOT2 )) {
            quickslotValue_2 = item;
        }
        if (bundle.getBoolean( QUICKSLOT3 )) {
            quickslotValue_3 = item;
        }
	}
}
