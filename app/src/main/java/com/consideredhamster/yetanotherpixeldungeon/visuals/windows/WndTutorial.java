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

import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndTutorial extends WndTabbed {

	private static final int WIDTH_P	= 112;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 128;
	private static final int HEIGHT_L	= 128;

    private static SmartTexture icons;
    private static TextureFilm film;

	private static final String TXT_TITLE	= "Tutorial";

	private static final String[] TXT_LABELS = {
        "I", "II", "III", "IV", "V",
    };

    private static final String[] TXT_TITLES = {
        "Tutorial - Interface",
		"Tutorial - Mechanics",
		"Tutorial - Equipment",
		"Tutorial - Consumables",
		"Tutorial - Denizens",
    };

    private static final String[][] TXT_POINTS = {
        {
            "Almost every action in the game are performed via tapping a desired tile. You tap to move, tap to attack, tap to pick up items and tap to interact with NPCs and dungeon features.",
			"If you tap the character avatar on the top left of the screen, you'll be able to see the stats and buffs your character currently has. If one of the stats is affected by an unidentified item, its value will be displayed as \"??\".",
            "You can skip a turn by tapping this button on the bottom left of the screen. Press and hold this button to rest - this allows to skip time much faster and significantly increases your health regeneration.",
            "Depending on your settings, this button can be tapped or pressed to make your character search nearby tiles, revealing traps and secret doors. Alternatively, it allows you to examine anything you meet in the dungeon to read its description.",
            "On the bottom right of the screen you can see this button. Tap it to open your inventory (you have probably guessed it by now). Long press it to show your keys available at the moment.",
            "To the left of the inventory button you can see three quickslots. You can use some items from there without need to open your inventory. You can add items for the quickslots by pressing and holding them.",
            "There is an offhand quickslot right above the inventory button. Its effect depends on the combination of weapons you have equipped at the moment. For example, it will shoot if wands or ranged weapons are equipped.",
            "Above the offhand quickslot there are several buttons which are probably hidden at the moment. These are danger indicator, attack button, pickup button and resume button. Danger indicator shows the number of currently visible enemies.",
			"Selecting the enemy by clicking the danger indicator and tapping the attack button allows you to attack an enemy without tapping the tile. Also, pressing and holding the attack button will show target's description.",
			"Pickup button is shown only when there are items on the tile on which your character is standing right now. Tapping the pickup button allows you to pick up these items without the need to click on the character.",
            "Below your character avatar you can see a button which allows you to drink from one of your waterskins without having to open your inventory. Or you can long press this button to pour water from your waterskin instead.",
            "Also there are another button which allows you to interact with your lantern. It is kinda smart one - tap it to light, snuff or refill your lantern. Long pressing it allows you to use your spare oil flasks to ignite nearby tiles.",
        },
		{
			"Most actions in the game spend one turn when performed, which means that one attack spend just as much time as one tile travelled, and that almost everyone moves with the same speed. Keep in mind that actions take their effect the moment they are performed and make the character wait after that.",
            "Enemies have a certain chance to notice you. It depends on their Perception value and your Stealth value. It also depends on the distance between you and the mob, but the mobs who are already on the hunt will have 100% chance to notice you if they can see you at all.",
            "However, as enemy loses sight of you, they can lose track of you and become open to a sneak attack. The chance of this to happen mostly depends on your Stealth. You can (and should) utilize corners, doors or high grass for this tactic. Don't forget that flying creatures can see over high grass.",
			"When you are attacking or being attacked, the first thing to be determined is whether this attack was a hit or miss. In general, the chance to hit depends on the attacker's Accuracy value and the defender's Dexterity value. Different characters and even mobs have different growth rates for these values.",
            "Dexterity value of the defender is decreased by 1/16 of its value for every occupied or impassable tile near it. This means that you should lure your enemies to narrow corridors if you want to miss less often, and you'll want to stick to the open areas if you want to dodge more reliably.",
            "All ranged attacks consider attacker's Accuracy to be decreased by 1/8 of its value for every tile of distance. The only exception for this rule are flintlock weapons. Only wands of Magic Missile can miss, but they use your magic power instead of accuracy to determine your chance to hit.",
            "For the player character (and some late-game mobs), every consecutive hit after the second one slightly increases damage done by the attack. This is shown by the \"combo\" message over your character and can significantly improve your effectiveness against packs of mobs.",
            "Your Perception attribute affects your chances to find a trap or a secret door by walking near it, as well as your chance to hear an enemy through the wall. Mind that secrets become more difficult to find as you descend further into the dungeon.",
			"High grass is extremely useful for setting up ambushes, as it both blocks field of view and makes you stealthier. However, water is noisy and will decrease your stealth when you are standing in it.",
            "Sleeping is the most readily available source of recovery. While you are sleeping, your regeneration rate is tripled. However, sleeping in the water denies this advantage, but you still can do that to skip turns quickly if needed.",
            "Searching or lighting your lantern guarantees that all secrets on nearby tiles (such as traps or hidden doors) will be revealed to you. Keep in mind that a lit lantern also makes it guaranteed for enemies to notice you, however.",
            "Your Attunement very significantly affects wands recharge rate, and also determines chance to prevent equipping cursed item when it is unidentified. Magic skill determines strength of your wands and also affects effectiveness of certain offensive scrolls.",
        },
        {
			"Melee weapons are separated into different categories. The most basic of them is heavy one-handed weapons which do not have any special abilities or penalties and can be used in any combination of weapons without strength penalties.",
			"Light one-handed weapons can be equipped in your off-hand, but will require additional strength to use them in such way. Dual-wielding increases your attack speed by 50% and allows you to parry as well.",
			"Light two-handed weapons are basically different kinds of polearms. They are not intended to be dual-wielded and will require additional strength if you want to use them in this way. However, they can be used with shields just fine.",
			"Heavy two-handed weapons are not intended to be used with shields or other melee  weapons. Throwing weapons and wands do not count as weapons here. Also, mind that if your second hand is empty, you can use your main weapon to parry.",
            "Throwing weapons can be equipped in your offhand slot. They can't be upgraded, but they allow you to attack from distance while keeping your melee weapon equipped. They never degrade, but they have a chance to break on use (same applies for ammunition as well).",
            "Ranged weapons require both hands to use - one to hold the weapon and second to hold its ammunition. Without ammunition, you will attack as if you had no weapon at all. Every kind of ranged weapon requires specific kind of ammunition.",
            "Flintlock weapons require bullets in your offhand to shoot and gunpowder in your inventory to reload. Also, loud noises tend to draw unnecessary attention. However, firearms are equally accurate on any distance and they penetrate target's armor.",
            "Always have at least some kind of armor equipped. Proper armor will greatly increase your chances of survival, decreasing damage from most sources. It will not protect from non-physical damage though, such as fire, lightning or disintegration.",
			"Cloth armors offer very little protection but they can enhance one of your secondary attributes - Stealth, Detection or Willpower. This bonus can be increased by upgrading the armor, and can lead to some powerful (but risky) character builds.",
            "Shields occupy your offhand slot, but they increase your armor class with a 50% chance and are more effective at blocking enemy blows. When blocking or parrying enemy blows, you have a chance to leave your enemy open to a counter attack, which will be a guaranteed hit.",
            "Wands can be very powerful, but you need to equip them and they have a limited number of charges. Unidentified and cursed wands can miscast occasionally. Some wands spend all of their charges on use, and their effect depends on amount of charges used.",
            "Rings are rare trinkets which can greatly help you when equipped. They are not really powerful by themselves, but effects of similar rings stack with each other. Cursed rings will hinder your abilities instead.",
        },
        {
			"Most equipment can be upgraded. Upgraded items are much more powerful than common ones, they deal more damage, offer better protection, require less strength, have higher amount of charges, recharge faster and are more durable. However, items cannot be upgraded more than three times. Keep that in mind.",
			"Weapons and armors can be enchanted. Enchantments provide some unique effects such as bonus fire damage or resistance to acid, but chance of them working significantly depends on the upgrade level of your weapon. Also, cursed items reverse the effects of their enchantments, turning them against you.",
            "Some items may happen to be cursed, which means that you will be unable to unequip them until curse is removed (with help of certain scrolls and potions). Cursed items offer the same damage/protection as non-cursed ones, but they require more strength to be used and break faster. Items which are too good for the current chapter have a much higher chance of being cursed.",
            "Most equipment has condition level. It slowly decreases as the item is used, but can be restored with the corresponding repair tools or scrolls of Upgrade. Every condition level affects item performance just as much as upgrade level does, but it doesn't affects the strength requirement of the weapon or the number of charges of the wand.",
			"Your character needs food to survive. There is always at least one ration at every floor; you can also find some food in shops or dropped from certain mobs. Your regeneration rate is increased when your satiety is over 75% and halved when it is 25% or lower. When your satiety hits 0%, it will cause periodical damage from starvation.",
            "Your most readily available source of healing are your waterskins. When used, waterskin recovers part of your missing health, and can be refilled in occasional water fountains. There is always at least one fountain per chapter.",
            "In the darkness of the dungeon, ambient lighting is barely noticeable, limiting your field of vision. To counter this, you can use the lantern you start with. It basically nullifies your Stealth though, so use it wisely.",
            "If you have some spare gunpowder, you can use it to craft makeshift bombs. These bombs can be dismantled back to give you some of the gunpowder spent on them, and they can be combined into even more powerful bomb bundles.",
            "Scrolls can be very powerful when used correctly. Some of them can lead to your quick demise if used incorrectly. There is no way to know which scroll is which, unless you try to read it or find one in a shop.",
            "On your quest, you can find randomly colored potions. Some of them are beneficial and some of them are harmful. Beneficial potions buff you when used, and harmful potions are better to be thrown into your enemies.",
            "Sometimes in the high grass you can find alchemical herbs. You can either eat them to cleanse and prevent certain debuffs, or use them in alchemy pot for brewing potions or cooking raw meat.",
            "If you find inventory to be too limiting, consider buying bags in the shops. A bag unlocks separate inventories for herbs, potions, scrolls or wands. Additionally, it will protect these items from harmful effects (like fire).",			
        },
        {
			"As you explore this dungeon, you'll meet many adversaries on your path. Defeating your enemies is the main source of experience to level up your character, but you'll need the level of threat of your opponents to be appropriate to see any improvements.",
			"Dungeon is filled with monsters to the brim, and even as you kill them, it will always spawn more to get you. Some mobs can even drop something useful on death. Be careful not to rely on that too much though. Each time the dungeon spawns another creature, respawn time on the current floor is increased a bit.",
			"Every denizen of this dungeon possesses some special abilities, but in general all of them can be separated into several categories. Most common enemies like rats and flies usually have higher dexterity and stealth, but their attacks are weaker and they can't take a good hit without, you know, dying horribly.",
			"A bit less common enemies like muggers, skeletons or brutes usually do not have any significant drawbacks or advantages. Some of them can even try to attack you from short distance, but these attacks are usually weaker and they will always switch to melee when damaged.",
			"Some enemies possess proper ranged attacks though, and they will use them whenever possible. Even worse is that most of these enemies also deal non-physical damage which ignores your armor class. On the other hand, some of them also need to spend a turn to charge their attack before that.",
			"There are also some mobs which are a much greater threat than others, being strong, durable, and pretty accurate as well. Their only weakness is their very low dexterity. They are also more susceptible to be ambushed and are much easier to be heard.",
			"While in general, most enemies belong to certain chapter and will never spawn out of it, some enemies can be encountered in any part of the dungeon. They grow in strength to always represent adequate threat for the current floor. Most of them also have some kind of weakness which makes dealing with them much easier.",
			"However, bosses take the cake for being the greatest threat in this dungeon. All of them are very powerful, durable and possess unique abilities. Worst of all, you can't evade the fight with them and you have to defeat them to descend deeper into the dungeon. They require preparation and attention to be defeated, but some of them also possess certain tricks to make fight with them easier.",
			"But keep in mind that not everything in this dungeon wants you dead. Some denizens of this dungeon are quite friendly and can even give you a short quest to complete. Obviously, doing what they ask will net you a proper reward, but they can be simply ignored if you want, it will have almost no effect on your future progress.",
			"Some of these NPCs do not want anything from you... except for your gold. There will be small shop on every fifth floor where you can sell your surplus items and buy something in return. Assortment and quality of items in these shops depends on the current chapter, but some of the items are guaranteed to be sold.",
			"Finally, keep in mind that some of the enemies in this dungeon are of magical, unnatural origin, and thus can be immune to some effects which require living flesh and thinking mind to be affected. But this also makes them susceptible to some other effects which do not affect natural, living creatures.",	
			"Well, that's it for now. If you read this tutorial from the beginning to the end, then you now know everything what you need to start playing this game. Some of details are gonna be explained in loading tips (pay attention to them) and you can learn more about inner workings of the game by reading the YAPD article on the Pixel Dungeon wikia. Good luck, and watch out for mimics!",
		},
    };

	private BitmapText txtTitle;
	private ScrollPane list;

    private enum Tabs {

        INTERFACE,
        MECHANICS,
        CONSUMABLES,
        EQUIPMENT,
        DENIZENS,

    };

//	private ArrayList<Component> items = new ArrayList<>();

	private static Tabs currentTab;

	public WndTutorial() {
		
		super();

        icons = TextureCache.get( Assets.HELP );
        film = new TextureFilm( icons, 24, 24 );
		
		if (YetAnotherPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		add( txtTitle );
		
		list = new ScrollPane( new Component() );
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );

		Tab[] tabs = {
            new LabeledTab( TXT_LABELS[0] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.INTERFACE;
                        updateList( TXT_TITLES[0] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[1] ) {
                @Override
				protected void select( boolean value ) {
					super.select( value );

                    if( value ) {
                        currentTab = Tabs.MECHANICS;
                        updateList( TXT_TITLES[1] );
                    }
				};
			},
            new LabeledTab( TXT_LABELS[2] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.EQUIPMENT;
                        updateList( TXT_TITLES[2] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[3] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.CONSUMABLES;
                        updateList( TXT_TITLES[3] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[4] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.DENIZENS;
                        updateList( TXT_TITLES[4] );
                    }
                };
            },
		};

        int tabWidth = ( width + 12 ) / tabs.length ;

		for (Tab tab : tabs) {
			tab.setSize( tabWidth, tabHeight() );
			add( tab );
		}
		
		select( 0 );
	}
	
	private void updateList( String title ) {

		txtTitle.text( title );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		
//		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		int index = 0;
		float pos = 0;

        switch( currentTab ) {

            case INTERFACE:
                for (String text : TXT_POINTS[0]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case MECHANICS:

                index += 12;

                for (String text : TXT_POINTS[1]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case EQUIPMENT:

                index += 24;

                for (String text : TXT_POINTS[2]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case CONSUMABLES:

                index += 36;

                for (String text : TXT_POINTS[3]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case DENIZENS:

                index += 48;

                for (String text : TXT_POINTS[4]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

        }
		
		content.setSize( width, pos );
	}
	
	private static class TutorialItem extends Component {

        private final int GAP = 4;
        private Image icon;
		private BitmapTextMultiline label;
		
		public TutorialItem( String text, int index, int width ) {
			super();

            icon.frame( film.get( index ) );

            label.text( text );
            label.maxWidth = width - (int)icon.width() - GAP;
            label.measure();

            height = Math.max( icon.height(), label.height() ) + GAP;
		}
		
		@Override
		protected void createChildren() {

            icon = new Image( icons );
            add( icon );
			
			label = PixelScene.createMultiline( 6 );
			add( label );
		}
		
		@Override
		protected void layout() {
			icon.y = PixelScene.align( y );
			
			label.x = icon.x + icon.width;
			label.y = PixelScene.align( y );
		}
	}
}
