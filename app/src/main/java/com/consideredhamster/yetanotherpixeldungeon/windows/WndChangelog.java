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

	private static final String TXT_TITLE	= "What's new?";

    private static final String TXT_DESCR =
        "PLEASE READ: due to some of the suggestions being more frequent than others, I want to " +
        "address them once and for all.\n" +
        "\n" +
        "First, I definitely want to implement international localization in this mod, and I will " +
        "definitely implement it... But not right now. Thing is, implementing localization would " +
        "require a huge amount of effort, and I would rather start working on things like this " +
        "AFTER I'll implement the things I deem to be more important. Which means that it will " +
        "happen somewhere after the 0.4.0 update.\n" +
        "\n" +
        "Second, I do not plan to include multiplayer in this mod at all. That's because I don't " +
        "see any reasonable way to make it work with current game mechanics - especially with how " +
        "turns work in the game. I am aware that there may be a way to include at least some " +
        "amount of interaction between players, but for now I am fine with the game being pure single " +
        "player (like most roguelikes out there). \n" +
        "\n" +
        "Finally, I already have (quite vague) plans about which classes I want to add in this mod, " +
        "thank you all foryour suggestions. However, all of this is irrelevant right now anyway " +
        "because I need to bring back subclasses before it will become a good idea to introduce " +
        "new classes to the game. Which is planned exactly for the 0.4.0 update.\n" +
        "\n" +
        "Of course, there is a long road between updates 0.3.0 and 0.4.0. There " +
        "is still a lots of things to rework first - herbs, torches, wands, traps, bosses, story, " +
        "and even the game's music. Please, be understanding and patient. Thank you ;)\n" +
        "\n" +
        "v0.3.0b\n" +
        "\n" +
        "- added an option to change the \"Search\" button behaviour in the game settings\n" +
        "- wandmaker now shows actual wand names even if they were not identified yet\n" +
        "- completing the sad ghost quest will not crash the game anymore (restart the run if he still does)\n" +
        "- some loading tips were reworded (thanks to Dustin Jacobsen)\n" +
        "- fixed some of the descriptions and issues\n" +
        "\n" +
        "v0.3.0a\n" +
        "\n" +
        "General\n" +
        "\n" +
        "- you can hit targets out of your field of view again, but with 50% accuracy penalty\n" +
        "- rebalanced rewards of sad ghost and ambitious imp quests\n" +
        "- rewards of sad ghost and old wandmaker quests are visible to the player now\n" +
        "- player character now receives 50% less damage on Easy difficulty\n" +
        "- player character now receives 50% more damage on Impossible difficulty\n" +
        "\n" +
        "Items & Shops\n" +
        "\n" +
        "- all basic shops now sell arrows or quarrels\n" +
        "- all basic shops now sell bullets or gunpowder\n" +
        "- decreased base price of ammunitions\n" +
        "- partially identified non-cursed items now cost less than fully identified ones\n" +
        "- rebalanced bomb damage, bomb sticks are less powerful than bundles now\n" +
        "\n" +
        "- scrolls of Enchantment now can be used to upgrade/uncurse wands and rings\n" +
        "- scrolls of Enchantment now upgrade already enchanted weapons/armors\n" +
        "- scrolls of Enchantment now re-enchant only if item is already at +3\n" +
        "- scrolls of Sunlight now heal more the longer you stay in their area of effect\n" +
        "- effect of scrolls of Sunlight now lasts less longer\n" +
        "- thunderstorm clouds now deal low damage to the fire elementals\n" +
        "\n" +
        "Mobs & Bosses\n" +
        "\n" +
        "- mob damage is not affected by difficulty anymore\n" +
        "- mob respawn rate is not affected by difficulty anymore\n" +
        "- tweaked last boss battle to be a bit more difficult\n" +
        "- buffed damage growth rate of miasma cloud\n" +
        "- carrion swarms now drain 1% of your satiety per hit\n" +
        "- fire elementals do not drop gunpowder anymore\n" +
        "- golems now become less armored while burning\n" +
        "- wraiths now deal physical damage in melee instead of body damage\n" +
        "- wraiths still ignore target's armor\n" +
        "- increased drop chance of raw meat from mobs\n" +
        "\n" +
        "- lots of minor tweaks and bugfixes";

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
