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

        "Hello, fellow rat punchers! April Fools event is over, and this means that mob sprites are reverted back to normal. Also, classy suit armor was removed from the game - sorry about that. I liked it, but it had to go, as it was never intended to be in the game for real.\n" +
        "\n" +
        "Apart from removing stuff from the game, I've also implemented new features for your waterskins and lantern. Now they can be used on adjacent tiles to splash them with water and set them on fire, respectively. Hope that this feature makes for a more fun runs, as it allows you to basically terraform this dungeon to suit your own needs.\n" +
        "\n" +
        "Additionally, this update fixes to most of the reported issues (all of the important ones, at least) and nerfs wraiths back to be a way more manageable threat. They are still resistant to physical damage and possess all the same abilities, but they got less beefy and less annoying.\n" +
        "\n" +
        "Now I can start working on the next update, which will be mostly about making wands more powerful and interesting to use. It will also bring new potions and scrolls into the game, as well as change existing alchemy system and implement new ways to obtain scrolls for your character.\n" +
        "\n" +
        "_IMPORTANT:_ one thing which I want to remind to both rookie and veteran YAPD players is that this mod has its own set of articles on the PD wikia. They can be really useful for a newcomer to this mod, and I really recommend checking them out, they can be very helpful - despite the fact that they are a bit outdated by now...\n" +
        "\n" +
        "And that's where I need help of veteran players. I wrote all of these articles mostly by myself, and while I can update them myself as well, doing this is a very time consuming task for a single person. Wikis are supposed to be community-driven projects, so I just ask for some volunteers to spend just a little bit of their time and show some love to those articles.\n" +
        "\n" +
        "Thank you in advance!\n" +
        "\n" +
        "-----\n" +
        "\n" +
        "Ok, now that's everything is said and done, here is a list of changes brought by this update:\n" +
        "\n" +
        "_GENERAL_\n" +
        "\n" +
        "- lantern can be used to spend your oil flasks to ignite nearby tiles\n" +
        "\n" +
        "- amount of oil flasks spawned is increased\n" +
        "\n" +
        "- waterskins can be used to pour water on adjacent tiles (or yourself)\n" +
        "\n" +
        "- pouring water now creates a patch of water on affected tile\n" +
        "\n" +
        "- wands of Magic Missile now inflict energy damage with their spells (instead physical)\n" +
        "\n" +
        "_ENEMIES_\n" +
        "\n" +
        "- decreased wraith HP\n" +
        "\n" +
        "- decreased wraith blink chance\n" +
        "\n" +
        "- restored wraith spawn rate on haunted floor to the previous value\n" +
        "\n" +
        "- gnoll shamans now inflict energy damage with their spells (instead of physical)\n" +
        "\n" +
        "_MISC_\n" +
        "\n" +
        "- weapons ands armors in shops can be enchanted now\n" +
        "\n" +
        "- increased duration of \"Withered\" caused by unholy damage\n" +
        "\n" +
        "- thunderstorm clouds now can remove Corrosion\n" +
        "\n" +
        "- updated loading tips\n" +
        "\n" +
        "- updated tutorial\n" +
        "\n" +
        "_BUGFIXES_\n" +
        "\n" +
        "- fixed first boss not spawning minions when hit\n" +
        "\n" +
        "- fixed issue with weapons being degraded too fast\n" +
        "\n" +
        "- fixed scroll of Challenge crashing the game\n" +
        "\n" +
        "- fixed scroll of Transmutation and throwing weapons/ammunitions issue\n" +
        "\n" +
        "- fixed issue when using throwing weapons with ranged weapon in your main hand\n" +
        "\n" +
        "- fixed issue with quickslots acting as offhand slots when item in them is equipped\n" +
        "\n" +
        "- fixed scroll of Transmutation not showing a message when transmuting rings\n" +
        "\n" +
        "- fixed some typos and grammar mistakes\n" +
        "\n" +
        "_UPDATE 0.3.1h_:\n" +
        "\n" +
        "- fixed ranged weapons working incorrectly after all of the bugfixes... *sigh*\n" +
        "\n" +
        "-----\n" +
        "\n" +
        "Well, that's all for now. Thank you for playing this mod!";

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
