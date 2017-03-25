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

	private static final String TXT_TITLE	= "PLEASE READ";

    private static final String TXT_DESCR =
        "This window is supposed to tell you about the changes made to the game in the " +
        "last updates, but due to some of the suggestions being more frequent than others, I want to " +
        "address them once and for all here, where everyone can see them.\n" +
        "\n" +
        "First, I will definitely implement international localization in this mod, but not right " +
        "now. Thing is, implementing localization would require a huge amount of effort, and I " +
        "would rather start working on things like this AFTER I implement the things I deem to " +
        "be more important. Which means that it will happen somewhere AFTER the 0.4.0 update.\n" +
        "\n" +
        "Second, I do not plan to include multiplayer in this mod at all. That's because I don't " +
        "see any reasonable way to make it work with the current game mechanics. I am aware that there " +
        "may be another way to include at least some amount of interaction between players, but for now " +
        "I am fine with the game being single player only (like most roguelikes out there).\n" +
        "\n" +
        "Finally, I already have my own plans about the classes I want to add in this mod, " +
        "thank you all for your suggestions. However, all of this is irrelevant right now anyway " +
        "because I need to bring back subclasses before it becomes a good idea to introduce " +
        "new classes to the game. Which is planned exactly for the 0.4.0 update.\n" +
        "\n" +
        "Of course, there is a long road between updates 0.3.0 and 0.4.0. There is still a lots of " +
        "things to rework first - torches, traps, bosses, story, music... Version 0.3.1, for example, " +
        "will be about wands, herbs and debuffs. Please, be understanding and patient. Thank you ;)\n" +
        "\n" +
        "P.S. Also, I want to bring to your attention that you can bring up this window any time by " +
        "tapping the version number in the bottom right of the title screen. Okay, all said and " +
        "done, here comes the actual changelog...\n" +
        "\n" +
        "CHANGES IN THE LAST UPDATES:\n" +
        "\n" +
        "v0.3.0c\n" +
        "\n" +
        "- the \"search button\" switch in the settings now works like this:\n" +
        "- \"default behaviour\" - tap to examine, press or tap again to search\n" +
        "- \"reversed behaviour\" - tap to search, press to examine\n" +
        "\n" +
        "- removed the \"Enchanted item with +X bonus acquired\" badge\n" +
        "- added the \"X scrolls of Upgrade used\" badge\n" +
        "\n" +
        "- Warrior now gets 2 bonus HP from potions of Strength\n" +
        "- Acolyte now gets 2 bonus HP from potions of Strength\n" +
        "\n" +
        "- scrolls of Banishment now affect wands in the wand holder\n" +
        "- potions of Blessing now affect wands in the wand holder\n" +
        "\n" +
        "- decreased skeleton respawn rate in the fourth boss fight\n" +
        "- fixed various crashes and other issues\n" +
        "\n" +
        "v0.3.0b\n" +
        "\n" +
        "- added an option to change the \"Search\" button behaviour\n" +
        "- some loading tips were reworded (thanks to Dustin Jacobsen)\n" +
        "- wandmaker reward now shows proper wand names\n" +
        "- fixed the sad ghost quest crashing the game\n" +
        "- fixed some of the descriptions and issues\n" +
        "\n" +
        "v0.3.0a\n" +
        "\n" +
        "- you can hit targets out of your field of view again...\n" +
        "- ...but you get 50% accuracy penalty in this case\n" +
        "- rebalanced rewards of sad ghost and ambitious imp quests\n" +
        "- rewards for the sad ghost's quest are revealed to the player now\n" +
        "- rewards for the wandmaker's quest are revealed to the player now\n" +
        "- player now receives 50% less damage on Easy difficulty\n" +
        "- player now receives 50% more damage on Impossible difficulty\n" +
        "\n" +
        "- all basic shops now sell arrows or quarrels\n" +
        "- all basic shops now sell bullets or gunpowder\n" +
        "- decreased base price of ammunitions in shops\n" +
        "- partially identified non-cursed items now cost less\n" +
        "- rebalanced bomb damage to be less OP against bosses\n" +
        "- bomb sticks are less powerful than bundles now\n" +
        "\n" +
        "- scrolls of Enchantment now can be used on wands and rings\n" +
        "- scrolls of Enchantment now upgrade enchanted weapons/armors\n" +
        "- scrolls of Enchantment now re-enchant only if item is already at +3\n" +
        "- scrolls of Sunlight now heal more if you stay in their area of effect\n" +
        "- effect of scrolls of Sunlight now lasts less longer\n" +
        "- thunderstorm clouds now deal low damage to the fire elementals\n" +
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
