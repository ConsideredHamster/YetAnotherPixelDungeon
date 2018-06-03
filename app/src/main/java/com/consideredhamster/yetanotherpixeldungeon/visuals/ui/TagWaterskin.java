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
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;

public class TagWaterskin extends Tag {

	private ItemSlot slot;

	private Waterskin item = null;

	public TagWaterskin() {

		super( 0x7C8072 );
		setSize( 24, 22 );
        item = Dungeon.hero.belongings.getItem( Waterskin.class );

	}
	
	@Override
	protected void createChildren() {
		super.createChildren();

		slot = new ItemSlot() {

			protected void onClick() {
                item.execute( Dungeon.hero, Waterskin.AC_DRINK );
                flash();
			};

            protected boolean onLongClick() {
                item.execute( Dungeon.hero, Waterskin.AC_POUR );
                flash();
                return true;
            };
		};


        slot.setScale(0.8f);
		add( slot );
	}
	
	@Override
	protected void layout() {

		super.layout();

//        if( YetAnotherPixelDungeon.buttons() ){

            bg.scale.x = -1.0f;
            bg.x += bg.width;

            slot.setRect( x + 2, y + 2, width - 5, height - 4 );

//        } else {
//
//            slot.setRect( x + 2, y + 3, width - 2, height - 6 );
//
//        }

	}
	
	@Override
	public void update() {

        super.update();

        slot.item( item );
        slot.enable( Dungeon.hero.isAlive() && Dungeon.hero.ready );

	}
}
