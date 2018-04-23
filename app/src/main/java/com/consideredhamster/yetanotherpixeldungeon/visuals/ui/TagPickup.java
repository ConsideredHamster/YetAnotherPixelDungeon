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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;

public class TagPickup extends Tag {

	private ItemSlot slot;
	
	private Item lastItem = null;
	private int lastQuantity = 0;
	
	public TagPickup() {
		super( 0x1F75CC );
		
		setSize( 24, 22 );
		
		visible = false;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {

			protected void onClick() {
				Dungeon.hero.handle( Dungeon.hero.pos );
			};

            protected boolean onLongClick() {
                Toolbar.examineHeap( Dungeon.hero.pos );
                return true;
            };
		};
		slot.showParams( false );
		add( slot );
	}
	
	@Override
	protected void layout() {
		super.layout();
		slot.setRect( x + 2, y + 3, width - 2, height - 6 );
	}
	
	@Override
	public void update() {
		
		if (Dungeon.hero.ready) {
			Heap heap = Dungeon.level.heaps.get( Dungeon.hero.pos );
			if (heap != null) {
				
				Item item = 
					heap.type == Heap.Type.CHEST || heap.type == Heap.Type.CHEST_MIMIC ? ItemSlot.CHEST :
					heap.type == Heap.Type.LOCKED_CHEST ? ItemSlot.LOCKED_CHEST :
					heap.type == Heap.Type.TOMB ? ItemSlot.TOMB :
					heap.type == Heap.Type.BONES || heap.type == Heap.Type.BONES_CURSED ? ItemSlot.SKELETON :
					heap.peek();
				if (item != lastItem || item.quantity() != lastQuantity) {
					lastItem = item;
					lastQuantity = item.quantity();
					
					slot.item( item );
					flash();
				}
				visible = true;
				
			} else {
				
				lastItem = null;
				visible = false;
				
			}
		}
		
		slot.enable( visible && Dungeon.hero.ready );
		
		super.update();
	}
}
