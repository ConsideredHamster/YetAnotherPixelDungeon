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

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "CHANGELOG";

    private static final String TXT_DESCR =

            "Hello everyone! Sorry for this update taking so long, especially given how prone to " +
            "crashes the previous version was. But now at least the nastiest crashes should've been fixed.\n" +
            "\n" +
            "Also, I took some time and implemented a feature which I wanted to implement long ago - the ability " +
            "to steal items from shops. Every character class can do it, but it requires high enough stealth " +
            "to be at least somewhat reliable, so obviously Brigand is a natural shoplifter. Be careful though, " +
            "as even successful stealing still causes shopkeeper to grow suspicious - it is just a small " +
            "private shop, after all.\n" +
            "\n" +
            "Another feature worth of a special mention is that pouring water from waterskins now allows to grow " +
            "grass and herbs for you (instead of flooding tiles). Maybe this feature would sounds awfully " +
            "familiar to those who played SproutedPD, but I can assure you that this similarity was not " +
            "intentional in any way. The longer any mod is updated, the more likely it is that some feature in it will " +
            "happen to be similar to a feature from some other mod.\n" +
            "\n" +
            "Okay, I guess it is all for now. Here is a detailed changelog for you, and stay tuned for " +
            "the upcoming updates. Good hunting!\n" +
            "\n" +
            "_GENERAL_\n" +
            "" +
            "\n" +
            "- added ability to steal items from shops\n" +
            "\n" +
            "- shopkeepers do not leave any items behind when fleeing anymore\n" +
            "\n" +
            "- boomerangs and chakrams now always return when thrown (just like harpoons)\n" +
            "\n" +
            "- sleeping in water doesn't affects health regeneration rate anymore\n" +
            "\n" +
            "- slightly adjusted various debuff durations\n" +
            "\n" +
            "_MOBS & BOSSES_\n" +
            "\n" +
            "- gnoll brutes now become enraged on being hit while having less than 50% health\n" +
            "\n" +
            "- DM-300 now becomes slightly faster after every enrage (up to 95%)\n" +
            "\n" +
            "- DM-300 now receives halved damage when enraged\n" +
            "\n" +
            "- Tengu now receives halved damage when enraged\n" +
            "\n" +
            "_HERBS & WATERSKINS_\n" +
            "\n" +
            "- brought back the Earthroot herb\n" +
            "\n" +
            "- changed the sprite of the Whirlvine herb\n" +
            "\n" +
            "- added a rare Wyrmflower herb which can be used to brew potions of Strength\n" +
            "\n" +
            "- pouring water from waterskins does not flood the targeted tile anymore\n" +
            "\n" +
            "- pouring water from waterskin now turns ember tiles into grass tiles\n" +
            "\n" +
            "- pouring water from waterskin now turns grass tiles into high grass tiles\n" +
            "\n" +
            "- pouring water from waterskin now grows random herbs on a high grass tiles\n" +
            "\n" +
            "_BUGS & ISSUES_\n" +
            "\n" +
            "- fixed game crashing when attacking burning Goo or its spawns\n" +
            "\n" +
            "- fixed game crashing sometimes when Ankh tries to resurrect you\n" +
            "\n" +
            "- fixed game crashing when player character is killed by a trap\n" +
            "\n" +
            "- fixed Levitation visual effect glitch when using potion of Levitation to fall down\n" +
            "\n" +
            "- fixed Ensnared duration from webs and regrowth clouds\n" +
            "\n" +
            "- fixed guard lasting for a more than a single turn\n" +
            "\n" +
            "- fixed some descriptions in the tutorial\n" +
            "\n" +
            "- fixed some typos and grammar mistakes\n" +
            "\n" +
            "- fixed some other minor issues";

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
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
        add( txtTitle );

        list = new ScrollPane( new ChangelogItem( TXT_DESCR, width, txtTitle.height() ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );

	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        private BitmapTextMultiline normal;
        private BitmapTextMultiline highlighted;

        public ChangelogItem( String text, int width, float offset ) {
            super();

//            label.text( text );
//            label.maxWidth = width;
//            label.measure();

            Highlighter hl = new Highlighter( text );

//            normal = PixelScene.createMultiline( hl.text, 6 );
            normal.text( hl.text );
            normal.maxWidth = width;
            normal.measure();
//            normal.x = 0;
//            normal.y = offset;
//            add( normal );

            if (hl.isHighlighted()) {
                normal.mask = hl.inverted();

//                highlighted = PixelScene.createMultiline( hl.text, 6 );
                highlighted.text( hl.text );
                highlighted.maxWidth = normal.maxWidth;
                highlighted.measure();
//                highlighted.x = normal.x;
//                highlighted.y = normal.y;
//                add( highlighted );

                highlighted.mask = hl.mask;
                highlighted.hardlight( TITLE_COLOR );
            }

            height = normal.height() + GAP;
        }

        @Override
        protected void createChildren() {
            normal = PixelScene.createMultiline( 6 );
            add( normal );
            highlighted = PixelScene.createMultiline( 6 );
            add( highlighted );
        }

        @Override
        protected void layout() {
            normal.y = PixelScene.align( y + GAP );
            highlighted.y = PixelScene.align( y + GAP );
        }
    }
}
