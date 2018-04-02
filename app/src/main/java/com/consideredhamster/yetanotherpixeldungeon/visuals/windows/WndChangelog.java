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

	private static final String TXT_TITLE	= "Update 0.3.1e!";

    private static final String TXT_DESCR =

        "Finally, I can say that version 0.3.1 has been completed! Thank you for waiting!\n" +
        "\n" +
        "Well, I had to rework a huge chunk of the game code for all these changes, so you better like " +
        "them. Reworking debuffs was necessary for the next update (in which I plan to finally " +
        "rework the wands and enchantments as well as add new potions and scrolls), which will " +
        "serve as the basis for the return of the subclasses.\n" +
        "\n" +
        "WARNING! This update will make your older save games unplayable. Sorry about that, but given " +
        "the scope of work done, it would've been unnecessarily difficult to make this update " +
        "backwards compatible. I try to avoid issues like these where it is possible, but now it wasn't.\n" +
        "\n" +
        "Here you'll find main features that were introduced in this version:\n" +
        "\n" +
        "_GENERAL_\n" +
        "\n" +
        "- new soundtracks, kudos to _Jivvy_! (music in the first chapter remained the same)\n" +
        "\n" +
        "- when you skip a turn, the character gains a significant bonus to the chance to hit\n" +
        "\n" +
        "- waterskins now occupy a separate button on the left of the game screen\n" +
        "\n" +
        "- added a digital indicator of the character's health\n" +
        "\n" +
        "- added the indicator of current difficulty in the upper right corner\n" +
        "\n" +
        "_BUFFS & DEBUFFS_\n" +
        "\n" +
        "- all status effects were standardized, many were partially or completely reworked\n" +
        "\n" +
        "- some overly powerful or redundant debuffs were removed from the game (stun, for example)\n" +
        "\n" +
        "- some debuffs were brought back to the game or implemented from scratch\n" +
        "\n" +
        "- duration of debuffs from attacks now scales from damage received vs target's total health\n" +
        "\n" +
        "- duration of most debuffs from blobs now depends on amount of time target spent in the blob\n" +
        "\n" +
        "- added descriptions of effects in the \"Buffs\" window\n" +
        "\n" +
        "- added effect duration indicator at the top of the game screen\n" +
        "\n" +
        "- many debuffs got new shiny visual effects\n" +
        "\n" +
        "_ILLUMINATION & SECRETS_\n" +
        "\n" +
        "- torches have been removed from the game\n" +
        "\n" +
        "- characters start with an oil lantern that can be turned on and off at any time instead\n" +
        "\n" +
        "- lantern increases view distance and makes it guaranteed to find traps/secret doors, but makes it guaranteed to be noticed in return\n" +
        "\n" +
        "- lantern needs oil flasks, there are two of them per chapter (one among random drops and one in a shop)\n" +
        "\n" +
        "- player character's view distance is now limited to 4 cells regardless of the chapter\n" +
        "\n" +
        "- rebalanced chance to detect traps (mostly decreased it)\n" +
        "\n" +
        "_FOOD & SATIETY_\n" +
        "\n" +
        "- satiety drain rate now depends on strength requirement of your current equipment vs your current strength\n" +
        "\n" +
        "- starving now gives you some time before it starts to inflict damage (and it also deals decreased damage at first)\n" +
        "\n" +
        "- in general, starvation now inflicts less damage than before (4% HP per turn at max instead of 6.67% HP per turn)\n" +
        "\n" +
        "- herbs can now be eaten to slightly satisfy hunger and/or to remove certain debuffs\n" +
        "\n" +
        "- some herbs, on the contrary, inflict short-term debuffs (but satisfy hunger anyway)\n" +
        "\n" +
        "- it's better to eat harmful food when you are not that hungry\n" +
        "\n" +
        "_BOSSES & MOBS_\n" +
        "\n" +
        "- the fifth boss now has immunity to fire/acid, as long as the corresponding fist is alive\n" +
        "\n" +
        "- on knockback into something, the fourth boss now inflicts Vertigo instead of dealing additional damage\n" +
        "\n" +
        "- wraiths are now resistant to physical damage\n" +
        "\n" +
        "- reduced the frequency of wraith spawn on the haunted floors\n" +
        "\n" +
        "- some mobs have been slightly tweaked\n" +
        "\n" +
        "_EQUIPMENT & CONSUMABLES_\n" +
        "\n" +
        "- resists from buffs, rings and enchantments now stack and can be brought to 100% immunity\n" +
        "\n" +
        "- bonuses from rings and the chance of triggering enchantments have become slightly higher at the lower upgrade levels\n" +
        "\n" +
        "- scrolls of Torment now inflict a \"Torment\" debuff, frightening off mobs in addition to inflicting damage\n" +
        "\n" +
        "- scroll of Challenge now enrages your character instead of mobs on the current floor\n" +
        "\n" +
        "- scrolls of Banishment now banish magical mobs in addition to inflicting damage, forcing them to flee\n" +
        "\n" +
        "- scrolls of Sunlight now disrupt (confuse) magic mobs instead of ignoring them\n" +
        "\n" +
        "-----\n" +
        "\n" +
        "Thank you for waiting for this update, and good hunting!";

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
