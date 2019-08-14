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

	private static final String TXT_TITLE	= "YAPD v0.3.2: Arcane Art & Crafts!";

    private static final String TXT_DESCR =

        "Hello! I bet you didn't expect to receive an update after waiting for so long, but here it is! " +
        "I know, this update was in the works for far too long, but the work never really stopped... " +
        "Just got delayed often. I hope that next updates would not take as long as this one.\n" +
        "\n" +
        "Anyway, the main theme of this update is about wands and alchemy. Basically _all wands " +
        "were redesigned_ and rebalanced from scratch, and now should allow much more wand builds " +
        "for the more zap-happy players. There are also some _new potions_, and the _alchemy system " +
        "was reworked_ - now it allows brewing any kind of potions (if correct herbs are used), " +
        "and also allows you to _cook pieces of raw meat_ with herbs to provide resistance buffs.\n" +
        "\n" +
        "A _bunch of bugs was fixed_ as well, and there were quite a lot of _balance tweaks_ and " +
        "_interface improvements_. The game should be more accessible and comfortable to play now. " +
        "Most changes are listed below. Hope you will enjoy this update, and good luck!\n" +
        "\n" +
        "_UPD_: version _0.3.2a+_ fixes several problems which were missed during the beta testing " +
        "somehow. The fixed issues are listed below this changelog.\n" +
        "\n" +
        "_GENERAL_\n" +
        "\n" +
        "- \"magic skill\" stat was renamed into \"magic power\" and now affects wand's damage/effect\n" +
        "\n" +
        "- \"willpower\" was renamed into \"attunement\" and now affects recharge rate not as much\n" +
        "\n" +
        "- potions of Strength now increase your magic power in addition to its previous effects\n" +
        "\n" +
        "- Scholar now receives bonus magic power only on every 3rd level up\n" +
        "\n" +
        "- Acolyte now receives bonus magic power only on every 6th level up\n" +
        "\n" +
        "- Warrior and Brigand do not get additional magic power on level ups at all\n" +
        "\n" +
        "- increased starting health for all character classes from by 5 points\n" +
        "\n" +
        "- regeneration rate now depends on the max health amount only\n" +
        "\n" +
        "- nerfed waterskins and oil lantern\n" +
        "\n" +
        "- Acolyte now starts with three empty bottles instead of a potion of Overgrowth\n" +
        "\n" +
        "_WANDS & RINGS_\n" +
        "\n" +
        "- wand damage, recharge rate, and number of charges were significantly rebalanced\n" +
        "\n" +
        "- combat wands now are Magic Missile, Disintegration, Smiting, Lightning, Acid Spray and Firebrand\n" +
        "\n" +
        "- utility wands now are Ice Barrier, Thornvines, Force Blast, Charm, Life Drain and Damnation\n" +
        "\n" +
        "- wands will never miss their target anymore (except Magic Missile)\n" +
        "\n" +
        "- \"utility\" wands can miscast/squeeze charges, just like combat wands\n" +
        "\n" +
        "- only unidentified and/or cursed wands will ever miscast now\n" +
        "\n" +
        "- changed curse chances for wands/rings to be dependent on chapter rather than floor number\n" +
        "\n" +
        "- wands and rings now have much more detailed descriptions\n" +
        "\n" +
        "_ALCHEMY & POTIONS_\n" +
        "\n" +
        "- brewing potions now requires an empty potion bottle and two specific herbs\n" +
        "\n" +
        "- empty bottles can be found in shops, laboratories, and among the loot\n" +
        "\n" +
        "- using incorrect herbs will give you unstable potion which is, well, unstable\n" +
        "\n" +
        "- added potions of Rage, Toxic Gas, and Confusion Gas\n" +
        "\n" +
        "- Potion of Overgrowth was replaced with Potion of Webbing\n" +
        "\n" +
        "- Potion of Blessing was divided into Potion of Shield and Potion of Blessing\n" +
        "\n" +
        "- Potion of Corrosive Gas was replaced with Potion of Caustic Ooze\n" +
        "\n" +
        "- Potion of Caustic Ooze creates temporary acid puddles on the floor\n" +
        "\n" +
        "- rain created by potions of Thunderstorm now can refill wells in its area of effect\n" +
        "\n" +
        "_HERBS & FOOD_\n" +
        "\n" +
        "- raw meat cannot be cooked by burning or freezing it, but is completely safe to eat\n" +
        "\n" +
        "- burning raw meat will turn it into burned meat, which is a worse version of raw meat\n" +
        "\n" +
        "- you can, however, cook raw meat in alchemy pots, making it more nutritional\n" +
        "\n" +
        "- it can also be cooked with herbs to provide a much longer resistance buff when eaten\n" +
        "\n" +
        "- herbs now require only one turn to be eaten (was three turns before)\n" +
        "\n" +
        "- herbs now remove debuffs and apply short resistance buffs when eaten\n" +
        "\n" +
        "- spawn rates of different herbs now depends on the current chapter\n" +
        "\n" +
        "- implemented Feyleaf herb, which increases magical resistance when eaten\n" +
        "\n" +
        "- Dreamweed was renamed into Dreamfoil and had its sprite changed slightly\n" +
        "\n" +
        "- herbs and other types of food now have much more detailed descriptions\n" +
        "\n" +
        "_EQUIPMENT_\n" +
        "\n" +
        "- quarterstaffs now increase your magic power by 10-25% depending on their upgrade level\n" +
        "\n" +
        "- rebalanced prices and quantities of ammunition and throwing weapons\n" +
        "\n" +
        "- missile and flintlock weapons got their damage increased\n" +
        "\n" +
        "- light throwing weapons can now inflict bonus damage on sneak attacks\n" +
        "\n" +
        "- debuffs from special throwing weapons now ignore target's armor\n" +
        "\n" +
        "- javelins and tomahawks can now knock their targets back on hit\n" +
        "\n" +
        "- chakrams can now be used to hit several enemies at once\n" +
        "\n" +
        "- harpoons were reworked and will be more reliable now\n" +
        "\n" +
        "- throwing items at secret doors now also reveals them\n" +
        "\n" +
        "- weapon enchantments do not provide bonus damage for certain wands anymore\n" +
        "\n" +
        "- added a confirmation pop-up when equipping items which are too heavy for you\n" +
        "\n" +
        "_BUFFS & DEBUFFS_\n" +
        "\n" +
        "- the Enraged buff now has decreased effect, but lasts longer\n" +
        "\n" +
        "- Mind Vision now increases Perception by 25%\n" +
        "\n" +
        "- Levitation now increases Dexterity by 25%\n" +
        "\n" +
        "- Invisibility now increases Stealth by 25%\n" +
        "\n" +
        "- Invisibility is now dispelled on stealing\n" +
        "\n" +
        "- Corrosion now causes its target to spawn an acid puddle on death\n" +
        "\n" +
        "- Vertigo now prevents you from receiving \"Focused\" buff from skipping turns\n" +
        "\n" +
        "- being Poisoned now decreases attack speed only by 25% (instead of 50%)\n" +
        "\n" +
        "- electricity damage does not inflict Electrified debuff on its targets anymore\n" +
        "\n" +
        "- electricity damage now just gets increased by 50% if the target is standing in the water\n" +
        "\n" +
        "_MOBS_\n" +
        "\n" +
        "- slightly buffed minimum damage for most of the mobs\n" +
        "\n" +
        "- Goo was tweaked to make its fight easier for new players and less annoying for veterans\n" +
        "\n" +
        "- King of Dwarves was tweaked to make his fight to be less confusing/unfair for everyone\n" +
        "\n" +
        "- carrion eaters, giant spiders and cave scorpions are now vulnerable to mind debuffs\n" +
        "\n" +
        "- warlocks and evil eyes will not miss anymore (same as the corresponding wands)\n" +
        "\n" +
        "- carrion eaters are not resistant to body and acid anymore\n" +
        "\n" +
        "- updated giant spider sprites, and now they only spawn webs on death\n" +
        "\n" +
        "- cave scorpions now spawn acid puddle instead of corrosive gas on death\n" +
        "\n" +
        "- dwarf monks aren't resistant to mind effects anymore\n" +
        "\n" +
        "- dwarf warlocks now deal more damage with their ranged attacks\n" +
        "\n" +
        "- charmed mobs should be a bit more independent now\n" +
        "\n" +
        "- hitting charmed/controlled mobs doesn't remove the effect from them\n" +
        "\n" +
        "_DUNGEON_\n" +
        "\n" +
        "- moved old wandmaker NPC to the Sewers, and sad ghost NPC to the Prison\n" +
        "\n" +
        "- decreased amount of water in the bonus well rooms (only guaranteed wells are on the 4th floor of a chapter)\n" +
        "\n" +
        "- poison dart traps were changed into confusion gas traps\n" +
        "\n" +
        "- toxic gas trap now releases toxic gas instead of corrosive gas\n" +
        "\n" +
        "- trapped vaults now can be filled with summoning traps\n" +
        "\n" +
        "- made item piles on the already visited tiles visible through the fog of war\n" +
        "\n" +
        "- small changes to the layout of the second boss floor and imp's shop floor\n" +
        "\n" +
        "- secret doors on the 2nd floor are now guaranteed only if you have not unlocked at " +
        "least one of the strength, upgrade or boss badges yet\n" +
        "\n" +
        "_v.0.3.2a+_\n" +
        "\n" +
        "- fixed issue with wands not recharging in save games from previous versions\n"+
        "\n" +
        "- fixed wand of Damnation crashing the game when used with low magic power\n"+
        "\n" +
        "- fixed generation of the imp's shop room being botched up sometimes\n"+
        "\n" +
        "- fixed Goo not healing or inheriting burning debuff when absorbing its spawns\n"+
        "\n" +
        "- fixed characters not picking up items from pedestals when mobs are in sight\n"+
        "\n" +
        "- fixed scroll of Challenge possibly freezing the game\n"+
        "\n" +
        "- fixed possible game crashes on knockback"
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
