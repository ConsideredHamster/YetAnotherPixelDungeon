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

	private static final String TXT_TITLE	= "YAPD v0.3.4";

    private static final String TXT_DESCR =

                "After a few weeks of receiving feedback and testing the latest update myself, I " +
                "found and fixed quite a bunch of issues. Hopefully, after this update I will be " +
                "able to get to work on the next major one.\n" +
                "\n" +
                "By the way! I am really interested in watching how other people play this mod. " +
                "If you care enough to stream yourself playing the latest version of YAPD or even " +
                "upload it to Youtube, please contact me about it. I promise I'll do my best" +
                "to participate however I can, and will be very grateful regardless of how it goes.\n" +
                "\n" +
                "Anyway, here's the changelog for this update:\n" +
                "\n" +
                "_MINOR CHANGES_\n" +
                "\n" +
                "- added a pop-up about Tutorial on the second floor for new players\n" +
                "\n" +
                "- decreased the amount of healing Goo receives from absorbing its spawn\n" +
                "- decreased the distance between Tengu's shadows spawned in the alternate phase\n" +
                "- DM-300 now spawns immediately aggroed and closer to the starting room\n" +
                "- decreased DM-100's health by half (they deserved it)\n" +
                "\n" +
                "- boss summons no longer grant any experience\n" +
                "- piranhas are now resistant to fire and immune to burning\n" +
                "- changed skeletons spawning on the fifth floor to gnoll shamans\n" +
                "- evil eyes no longer drop meat, but vampire bats do\n" +
                "\n" +
                "- buying, selling, and stealing items from shops now takes one turn\n" +
                "- increased amount of ammo/gunpowder/bombs sold at the last shop\n" +
                "- freezing duration from Wands of Ice Barrier will ignore the target's armor now\n" +
                "- Scrolls of Phase Warp are now more reliably random\n" +
                "\n" +
                "_BUGS & ISSUES_\n" +
                "\n" +
                "- fixed Yog's fists not counting as magical enemies\n" +
                "- fixed the burning fist ability description\n" +
                "- fixed evil eyes being able to hit you twice\n" +
                "\n" +
                "- fixed DM-300 bombs not exploding at the same time\n" +
                "- fixed DM-300 being able to hit you at a distance\n" +
                "- fixed DM-300 being able to use abilities while asleep\n" +
                "- fixed DM-300 always throwing two bombs while enraged\n" +
                "\n" +
                "- fixed dropping/throwing equipped armor taking fewer turns than unequipping it\n" +
                "- fixed Scrolls of Detect Magic failing to identify equipped items in certain cases\n" +
                "- fixed some shelves being inaccessible on warehouse floors\n" +
                "- fixed vampiric weapons draining health from magical enemies\n" +
                "\n" +
                "- fixed the guard buff lasting longer than intended\n" +
                "- fixed some other (even less important) stuff\n" +
                "\n" +
                "That's all for now. _Have fun!_"
    ;

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
