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
package com.consideredhamster.yetanotherpixeldungeon.scenes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndError;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndStory;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.5f;
	
	private static final String TXT_DESCENDING	= "Descending...";
	private static final String TXT_ASCENDING	= "Ascending...";
	private static final String TXT_LOADING		= "Loading...";
	private static final String TXT_RESURRECTING= "Resurrecting...";
	private static final String TXT_RETURNING	= "Returning...";
	private static final String TXT_FALLING		= "Falling...";
	
	private static final String ERR_FILE_NOT_FOUND	= "File not found. For some reason.";
	private static final String ERR_GENERIC			= "Something went wrong..."	;	
	
	public static enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
	};
	public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;
	
	public static boolean noStory = false;
	
	public static boolean fallIntoPit;

    private static final String[] TIPS = {

            // GENERAL

            "There is a shop on every fifth level of the dungeon, you can spend your gold there",
            "There are only 3 ankhs in the dungeon but there is a low chance to find more",

            "Detection determines the time it takes to search for traps and secret doors",
            "Detection affects your chances to notice a trap or a secret door by walking near it",
            "Detection affects your chances to hear nearby enemies through walls and doors",
            "Your chance to wake up when there is a someone nearby depends on your Detection",

            "Stealth determines your chances of being noticed by enemies",
            "Stealth affects your chances to ambush an enemy to deal surprise hit",

            "Magic power affects recharge rate of all of your wands",
            "Magic power affects effectiveness of some of the scrolls",

            "Your wand skill affects your accuracy with most combat wands",
            "Your wand skill improves damage of your wand of Magic Missile",

            "High Strength increases your chances to break free when being ensnared",
            "Your health regeneration rate grows with levels and potions of Strength used",

            "Your health regeneration is tripled while you are asleep",
            "You do not regenerate any health while you are starving, burning or poisoned",

            "Amount of loot and special rooms increases as you descend deeper into the dungeon",
            "Try to keep your character level higher or equal to the current depth",

            "Item upgrade levels are capped at +3 - don't worry, that's enough",
            "Upgraded items are more durable but cursed items break much faster",

            "Remember that you always can turn these tooltips off in the game settings!",
            "More tips will be added later!",

            // WEAPONS & ARMOURS

            "Using a weapon which is too heavy for you decreases your attack speed",
            "Stronger weapons usually decrease your accuracy and stealth",

            "Using an armor or shield which is too heavy for you decreases your movement speed",
            "Stronger shields and body armors usually decrease your dexterity and stealth",

            "Your chance to block or parry an attack depends on armor class of your shield and damage of your weapon",
            "Your chance to counterattack equals to your chance to hit the target",

            "Excessive strength decreases penalties from heavy equipment",
            "Stronger flintlock weapons spend more gunpowder on reload",

            "Being hit by a lightning can force you to drop your current weapon on the ground",
            "You can identify weapons, armors, wands and rings by using them long enough",

            "Flintlock weapons ignore distance penalties and target's armor",
            "You can craft makeshift bombs from excess gunpowder",

            "You can combine bomb sticks into bomb bundles which pack some extra punch",
            "You can dismantle bomb bundles and sticks to obtain some of its components",

            "Upgraded cloth armors increase your stats even further than usual",
            "Stealth penalty from your equipment is not applied while you are asleep",

            // WANDS & RINGS

            "Combat wands always have a chance to miscast, which depends on their quality level",
            "Combat wands have a chance to squeeze additional charge, which depends on their quality level",

            "Recharge rate of utility wands depends on their quality and upgrade levels",
            "Utility wands always spend all of their charges on use",

            "Some rings can be kept only to equip them for certain occasions",
            "Bonuses from two equipped rings of a similar type stack additively",

            "There is usually only 1 wand per chapter but there is a low chance to find more",
            "There is usually only 1 ring per chapter but there is a low chance to find more",

            // POTIONS

            "There is only 1 potion of Wisdom per chapter but there is a low chance to find more",
            "Potions of Wisdom also increase your level cap, allowing you to reach higher levels",

            "There are only 2 potions of Strength per chapter but there is a low chance to find more",
            "Potion of Strength can be used to dispel weakness",

            "Potions of Mending also can dispel Withered status effect",
            "Potion of Mending also cures most physical debuffs such as poison or bleeding",

            "Potions of Mind Vision allow you to ignore most of disadvantages of being blind",
            "Potions of Mind Vision can be very powerful when used under right circumstances",

            "Potion of Levitation gives you a bonus to your movement speed",
            "Potion of Levitation can be used to descend safely when jumping into a chasm",

            "Using a scroll or a wand dispels the effect of potion of Invisibility",
            "Enemies can dispel effect of potion of Invisibility if they stumble into you",

            "Drinking a potion of Blessing increases your armor class by 20% of your max health",
            "Potion of Blessing can be thrown on adjacent tile to uncurse equipped items",

            "Potions of Liquid Flame never spread on nearby water tiles",
            "Potions of Liquid Flame always affect nearby flammable tiles",

            "You can quickly put out fire in a room with a help of a potion of Frigid Vapours",
            "Potion of Frigid Vapours can be used to escape or to land a killing blow",

            "Some gases are highly flammable - be careful when using potions of Corrosive Gas",
            "Potions of Corrosive Gas are very effective against crowds of enemies",

            "Potions of Ovegrowth are more effective when used on an already grassy tiles",
            "You can farm plants for alchemy with help of potions of Overgrowth",

            "Using a potion of Thunderstorm can attract wandering monsters",
            "Potions of Thunderstorm can be used to flood the dungeon floor or extinguish fires",

            // SCROLLS

            "There is only 1 scroll of Enchantment per chapter but there is a low chance to find more",
            "Using a scroll of Enchantment on a cursed item will remove all enchantments on it",

            "There are only 2 scrolls of Upgrade per chapter but there is a low chance to find more",
            "Uncursing an enchanted item with scroll of Upgrade allows you to keep the enchantment",

            "Using your scrolls of Identify wisely can save you a lot of time",
            "There is always at least one scroll of Identify in every shop",

            "Scrolls of Transmutation will never change an item into the same item",
            "Scrolls of Transmutation will always keep the rarity of the item",

            "Scrolls of Sunlight can be used to counteract effect of a potion of Thunderstorm",
            "Never forget that scroll of Sunlight can heal some of your enemies, too",

            "Scrolls of Clairvoyance will not reveal traps or secret doors, only rooms and items",
            "Area revealed by a scroll of Clairvoyance cannot be erased by a scroll of Phase Warp",

            "Scrolls of Banishment can be used to harm undead, elementals and golems",
            "Effectiveness of scroll of Banishment depends on distance between you and your target",

            "Enemies blinded by a scroll of Darkness can fall into a chasm or step into a trap",
            "Scrolls of Darkness can be used to counteract effects of scrolls of Sunlight",

            "Scrolls of Phase Warp can save your life as easily as they can end it",
            "Scrolls of Phase Warp can sometimes erase your memory about current depth",

            "Scrolls of Raise Dead can be very deadly against a single creature - including you",
            // "Wraiths summoned by using a scroll of Raise Dead will disappear after a while",

            "Using scrolls of Challenge is an effective, but risky way to farm experience faster",
            "Scroll of Challenge can be used to lure mimics out of their cover",

            "Scroll of Torment is more harmful to you if there are no more enemies in sight",
            "Scroll of Torment is useless against creatures which have no flesh to torture",

            // FOOD

            "There is always at least 1 ration of food per depth, but look out for hidden rooms",
            "Some kinds of monsters can drop a raw meat or even a small ration",

            "Full stomach allows you to recover from wounds faster than normal",
            "Eating raw meat can poison you - better cook it one way or another",

            "Chargrilled meat doesn't have any additional advantages besides being edible",
            "Frozen carpaccio is so tasty it recovers some of your health when eaten",

            "Sometimes you can find additional rations, but they will be smaller",
            "You can buy pastry in shops; more often than not it is well worth its cost",

            // BOSSES

            "Most bosses can become enraged, but only three times per fight",
            "Bosses are less vulnerable to damage which is based on target's maximum health",

            "Mind that miasma released by Goo is highly flammable",
            "Dwarven King's ritual can be disrupted by certain spell...",

            "Tengu teleports more often when threatened",
            "DM-300 is neither organic nor magical creature.",

            // TERRAIN

            "Try to avoid moving in water if you are trying to sneak up on someone",
            "Consider sticking to high grass if you are trying to sneak up on someone",

            "Flying creatures can see over the high grass and are unaffected by terrain effects",
            "Water amplifies shock and frost effects, but extinguishes fire and washes off acid",

            "Traps only appear in standard rooms and never appear in corridors or special rooms",
            "Monsters inhabiting this dungeon are aware of all of its traps and secret doors",

            "Sleeping in the water is much less efficient than sleeping anywhere else",
            "Your evasion chance is decreased for every occupied or impassable tile near you",

    };
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;
	
    private BitmapText            message;
    private ArrayList<BitmapText> tipBox;

	private Thread thread;
	private String error = null;
	
	@Override
	public void create() {
		super.create();
		
		String text = "";
//        int depth = Dungeon.depth;

		switch (mode) {
            case DESCEND:
                text = TXT_DESCENDING;
//                depth++;
                break;
            case ASCEND:
                text = TXT_ASCENDING;
//                depth--;
                break;
            case CONTINUE:
                text = TXT_LOADING;

//                GamesInProgress.Info info = GamesInProgress.check( StartScene.curClass );

//                if (info != null) {
//
//                    depth = info.depth;
//
//                }

//                depth = depth > 0 ? depth : 0 ;

                break;
            case RESURRECT:
                text = TXT_RESURRECTING;
                break;
            case RETURN:
                text = TXT_RETURNING;
//                depth = returnDepth;
                break;
            case FALL:
                text = TXT_FALLING;
//                depth++;
                break;
		}
		
		message = PixelScene.createText( text, 10 );
		message.measure();
		message.x = (Camera.main.width - message.width()) / 2; 
		message.y = (Camera.main.height - message.height()) / 2;
		add(message);

        tipBox = new ArrayList<>();

        if( YetAnotherPixelDungeon.loadingTips() ) {

            BitmapTextMultiline tip = PixelScene.createMultiline(TIPS[Random.Int(TIPS.length)], 6);
            tip.maxWidth = Camera.main.width * 9 / 10;
            tip.measure();

            for (BitmapText line : tip.new LineSplitter().split()) {
                line.measure();
                line.x = PixelScene.align(Camera.main.width / 2 - line.width() / 2);
                line.y = PixelScene.align(Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * line.height());
                tipBox.add(line);
                add(line);
            }
        }


		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;
		
		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();
					
					switch (mode) {
					case DESCEND:
						descend();
						break;
					case ASCEND:
						ascend();
						break;
					case CONTINUE:
						restore();
						break;
					case RESURRECT:
                        resurrect();
                        break;
					case RETURN:
						returnTo();
						break;
					case FALL:
						fall();
						break;
					}

					if ((Dungeon.depth % 6) == 0 && Dungeon.depth == Statistics.deepestFloor ) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}

                    if( mode != Mode.CONTINUE ) {
                        Dungeon.saveAll();
                        Badges.saveGlobal();
                    }
					
				} catch (FileNotFoundException e) {
					
					error = ERR_FILE_NOT_FOUND;
					
				} catch (Exception e) {

					error = e.toString();
                    YetAnotherPixelDungeon.reportException(e);
					
				}
				
				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE * 2;
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:

			message.alpha( 1 - p );

            for (BitmapText line : tipBox) {
                line.alpha( 1 - p );
            }

			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {



					phase = Phase.FADE_OUT;
                    timeLeft = TIME_TO_FADE * ( YetAnotherPixelDungeon.loadingTips() ? 4 : 1 );
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:

			message.alpha( p );

            for (BitmapText line : tipBox) {
                line.alpha( p );
            }

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p );
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:
			if (error != null) {
				add( new WndError( error ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					}
				} );
				error = null;
			}
			break;
		}
	}
	
	private void descend() throws Exception {
		
		Actor.fixTime();
		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
			Dungeon.saveLevel();
		}
		
		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, level.entrance );
	}
	
	private void fall() throws Exception {
		
		Actor.fixTime();
		Dungeon.saveLevel();
		
		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell() );
	}
	
	private void ascend() throws Exception {
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws Exception {
		
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
	}
	
	private void restore() throws Exception {
		
		Actor.fixTime();
		
		Dungeon.loadGame(StartScene.curClass);
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
		} else {
			Level level = Dungeon.loadLevel( StartScene.curClass );
			Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}

	private void resurrect() throws Exception {

        Actor.fixTime();

        if (Dungeon.bossLevel()) {

            Dungeon.hero.resurrect( Dungeon.depth );
            Dungeon.depth--;
            Level level = Dungeon.newLevel();
            Dungeon.switchLevel( level, level.entrance );
            
        } else {

            Dungeon.hero.resurrect(-1);
            Actor.clear();
            Arrays.fill(Dungeon.visible, false);
            Dungeon.level.reset();
            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);

        }
    }

	@Override
	protected void onBackPressed() {
		// Do nothing
	}
}
