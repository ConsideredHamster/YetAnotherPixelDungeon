package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Chrome;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBlacksmith;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

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
public class ItemButton extends Component {

    protected NinePatch bg;
    protected ItemSlot slot;

    public Item item = null;

    @Override
    protected void createChildren() {
        super.createChildren();

        bg = Chrome.get( Chrome.Type.BUTTON );
        add( bg );

        slot = new ItemSlot() {
            @Override
            protected void onTouchDown() {
                bg.brightness( 1.2f );
                Sample.INSTANCE.play( Assets.SND_CLICK );
            };
            @Override
            protected void onTouchUp() {
                bg.resetColorAlpha();
            }
            @Override
            protected void onClick() {
                ItemButton.this.onClick();
            }
        };
        add( slot );
    }

    protected void onClick() {};

    @Override
    protected void layout() {
        super.layout();

        bg.x = x;
        bg.y = y;
        bg.size( width, height );

        slot.setRect( x + 2, y + 2, width - 4, height - 4 );
    };

    public void item( Item item ) {
        slot.item( this.item = item );
    }

    public void clear() {
        slot.clearIcon();
        slot.item = null;
    }

}
