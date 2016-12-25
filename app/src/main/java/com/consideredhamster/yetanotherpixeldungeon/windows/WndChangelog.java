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
package com.consideredhamster.yetanotherpixeldungeon.windows;

import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.ui.Window;
import com.consideredhamster.yetanotherpixeldungeon.utils.Utils;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "Changelog";

    private static final String TXT_DESCR =
        "v0.3.0\n" +
        "\n" +
        "- all classes are now unlocked by default\n" +
        "\n" +
        "- amount of cursed items was decreased overall\n" +
        "- scrolls of Enchantment now can weaken curses\n" +
        "- items revealed as non-cursed now cost more\n" +
        "- added visual clue for items revealed as non-cursed\n" +
        "\n" +
        "- Goo now spawns minions less often when hit\n" +
        "- Goo's minions now have less health overall\n" +
        "- Goo now is more susceptible to energy damage\n" +
        "- Goo now is more susceptible to shock damage\n" +
        "\n" +
        "- increased effectiveness of combo attacks\n" +
        "- counter attacks now deal bonus damage\n" +
        "- added ring of Durability\n" +
        "- removed ring of Haste\n" +
        "\n" +
        "- added option to show loading tips until tapped\n" +
        "- journal allows reading item descriptions\n" +
        "- added third quickslot and tutorial section\n" +
        "- examine button now searches on a single tap\n"+
        "\n" +
        "- lots of other improvements";

	private BitmapText txtTitle;
	private ScrollPane list;

	public WndChangelog() {
		
		super();
		
		if (YetAnotherPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		add( txtTitle );

		list = new ScrollPane( new ChangelogItem( TXT_DESCR, width ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );
	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;
        private BitmapTextMultiline label;

        public ChangelogItem( String text, int width ) {
            super();

            label.text( text );
            label.maxWidth = width;
            label.measure();

            height = label.height() + GAP;
        }

        @Override
        protected void createChildren() {
            label = PixelScene.createMultiline( 5 );
            add( label );
        }

        @Override
        protected void layout() {
            label.y = PixelScene.align( y + GAP );
        }
    }
}
