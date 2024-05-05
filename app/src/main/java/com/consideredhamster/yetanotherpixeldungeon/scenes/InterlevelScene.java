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

import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndError;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndStory;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.5f;
	
	private static final String TXT_DESCENDING	= "Descending...";
	private static final String TXT_ASCENDING	= "Ascending...";
	private static final String TXT_LOADING		= "Loading...";
	private static final String TXT_RESURRECTING= "Resurrecting...";
	private static final String TXT_RETURNING	= "Returning...";
	private static final String TXT_FALLING		= "Falling...";
	private static final String TXT_CONTINUE	= "Tap to continue!";

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

            "There is a shop on every fifth level of the dungeon; you can spend your gold there",
            "There are only 3 ankhs in the dungeon but there is a low chance to find more",

            "Trapped and flooded vaults are less likely to have a cursed item in them",
            "Special rooms with tombs or animated statues will never have their prize cursed",

            "Perception determines the time it takes to search for traps and secret doors",
            "Perception affects your chances to notice a trap or a secret door by walking near it",
            "Perception affects your chances to hear nearby enemies while exploring or sleeping",
            "Your chance to expose your attacker depends on your Perception attribute",

            "Stealth determines success chance and amount of attempts when stealing from shops",
            "Stealth affects your chances to ambush an enemy to deal a sneak attack",

            "Attunement affects the recharge rate of all of your wands",
            "Your chance to prevent equipping a cursed item depends on your Attunement",

//            "Magic power affects effectiveness of enchanted equipment",
            "Your magic power determines damage/effect of your wands",
            "High Strength increases your chances to break free when ensnared",

            "Amount of loot and special rooms increases as you descend deeper into the dungeon",
            "Try to keep your character level higher or equal to the current depth",

            "Item upgrade levels are capped at +3 - don't worry, that's enough",
            "Upgraded items are more durable, but cursed items break much faster",

            "Remember that you always can turn these tooltips off in the game settings!",
            "More tips will be added later!",

            // WEAPONS & ARMOURS

            "Using a weapon which is too heavy for you decreases your attack speed",
            "Stronger weapons usually decrease your accuracy and stealth",
//            "Being electrified can force you to drop your current weapon on the ground",

            "Using an armor or shield which is too heavy for you decreases your movement speed",
            "Stronger shields and body armors usually decrease your dexterity and stealth",
            "Upgraded cloth armor will increase its corresponding attribute even more",

            "Your chance to block an attack depends on armor class of your shield or damage of your weapon",
            "A successful block can expose your attacker, leaving it open to a counterattack",

            "Excess strength decreases penalties from heavy equipment",
            "You can identify weapons, armors, wands and rings by using them long enough",
            "Stealth penalty from your equipment is not applied while you are asleep",

//            "Stronger flintlock weapons require more gunpowder to reload",
            "Flintlock weapons ignore distance penalties and the target's armor",

//            "You can craft makeshift bombs from excess gunpowder",
//            "You can combine bomb sticks into bomb bundles which pack some extra punch",
            "You can dismantle bomb bundles or sticks to obtain some of their components",

            // WANDS & RINGS

            "Chance to miscast depends on the wand's condition and is increased for cursed wands",
            "Chance to squeeze additional charge depends on the wand's condition and upgrade level",

            "Cursed and unidentified wands can miscast, but cannot squeeze additional charges",
            "You can can squeeze additional charges from empty wands if they are identified",

            "Some rings can be kept only to equip them for certain occasions",
            "Bonuses from two equipped rings of a similar type stack additively",

            "There is usually only 1 wand per chapter but there is a low chance to find more",
            "There is usually only 1 ring per chapter but there is a low chance to find more",

            "Sometimes, disintegration rays can be angled to hit the target twice",
            "Wands of Smiting are especially useful against enemies of magical origin",

            "Wands of Lightning do not conduct through the water if their target is flying",
            "Wands of Acid Spray deal 150% damage in melee range, but only 50% against targets afar",
            "Creating runes with wand of Firebrand is more effective than zapping with it directly",

            "Avalanches created by wand of Force Blast deal more damage than direct zaps",
            "Thornvines are stronger and tougher when created on grassy tiles",
            "Walls created by wand of Ice Barrier are more durable when placed on water tiles",

            "Wands of Charm can be used to harm and disorient enemies of magical origin",
            "Wands of Life Drain are more powerful when used against sleeping or wandering enemies",
            "Efficiency of wand of Damnation depends mostly on the target's current health",

            // POTIONS

            "There is only 1 potion of Wisdom per chapter but there is a low chance to find more",
            "Potions of Wisdom also increase your level cap, allowing you to reach higher levels",

            "There are only 2 potions of Strength per chapter but there is a low chance to find more",
            "Drinking a potion of Strength is the most effective way to heal yourself",

            "There is always at least one potion of Mending in every shop",
            "Potions of Mending also cure most physical debuffs such as poison or bleeding",

            "Potions of Mind Vision allow you to ignore most of disadvantages of being blind",
            "Potions of Mind Vision also increase your Perception for the duration of effect",

            "Potions of Levitation give you a bonus to your movement speed and evasion",
            "Potions of Levitation can be used to descend safely when jumping into a chasm",

            "Being invisible also increases your stealth, making it easier to steal from shops",
            "Enemies can dispel the effect of a potion of Invisibility by stumbling into you",

            "Drinking a potion of Shield increases your physical resistance as well",
            "Effects of a potion of Shield can be stacked with bonus resistances from equipment",

            "Potions of Blessing will inflict heavy damage to the enemies of magical origin",
            "Throw a potion of Blessing under yourself to weaken curses on items in your inventory",

            "Potions of Liquid Flame never spread on nearby water tiles",
            "Potions of Liquid Flame always affect nearby flammable tiles",

            "You can quickly put out fire in a room with a help of a potion of Frigid Vapours",
            "Potion of Frigid Vapours are more useful against targets standing in the water",

            "Some gases are highly flammable - be careful when using potions of Toxic Gas",
            "Potions of Toxic Gas are very effective against crowds of non-magical enemies",

            "Using a potion of Thunderstorm can attract wandering monsters",
            "Potions of Thunderstorm can be used to flood the dungeon floor or to extinguish fires",

            "Potions of Webbing can be very helpful if you prefer to keep your enemies away from you",
            "Potions of Confusion Gas are especially dangerous in combination with chasms",
            "Potions of Rage have all of the benefits of scrolls of Challenge but without any drawbacks",
            "Potions of Caustic Ooze leave temporary acid puddles when thrown on dry land tiles",

            // SCROLLS

            "There is only 1 scroll of Enchantment per chapter but there is a low chance to find more",
            "Using a scroll of Enchantment on a cursed item will significantly weaken its curse",

            "There are only 2 scrolls of Upgrade per chapter but there is a low chance to find more",
            "Uncursing an enchanted item with scroll of Upgrade allows you to keep the enchantment",

            "Using your scrolls of Detect Magic wisely can save you a lot of time",
            "There is always at least one scroll of Detect Magic in every shop",

            "Scrolls of Transmutation will never change an item into the same item",
            "Scrolls of Transmutation can be used to transmute ammunition and throwing weapons",

            "Scrolls of Sunlight can be used to counteract effect of a potion of Thunderstorm",
            "Never forget that scroll of Sunlight can heal some of your enemies, too",

            "Scrolls of Clairvoyance will not reveal traps or secret doors, only rooms and items",
            "Area revealed by a scroll of Clairvoyance cannot be erased by a scroll of Phase Warp",

            "Scrolls of Banishment can be used to harm undead, elementals and golems",
            "Scrolls of Banishment partially dispel curses from all of the items in your inventory",

            "Enemies blinded by a scroll of Darkness can fall into a chasm or step into a trap",
            "Scrolls of Darkness can be used to counteract effects of scrolls of Sunlight",

            "Scrolls of Phase Warp can save your life as easily as they can end it",
            "Using a scrolls of Phase Warp will confuse you for a short period",

            "Scrolls of Raise Dead can be very deadly against a single creature - including you",
            "Wraiths summoned by using a scroll of Raise Dead will stop being charmed after a while",

            "Using scrolls of Challenge in boss fights is probably the best way to use them",
            "Scroll of Challenge can be used to lure mimics out of their disguise",

            "Scroll of Torment is more harmful to you if there are no more enemies in sight",
            "Scroll of Torment is useless against creatures which have no mind to torture",

            // FOOD

            "There is always at least 1 ration of food per depth, but look out for hidden rooms",
            "Some kinds of monsters can drop a raw meat or even a small ration",

            "A full stomach allows you to recover from wounds faster than normal",
            "Fire can burn raw or cooked meat, making it way less useful and tasty",

            "Burned meat is less nutritious and cannot be properly cooked anymore",
            "Stewed meat removes debuffs just like herbs do, but takes longer to eat",

            "Sometimes you can find additional rations, but they will be smaller",
            "You can buy pastry in shops; more often than not it is well worth its cost",

            "Starvation is not immediately harmful; initially it just prevents regeneration",
            "If you ignore starvation for too long, it will become more dangerous",

            "Satiety will be drained slightly slower if you have more strength than required by your equipment",
            "Moving, attacking, casting and blocking decrease your satiety faster than standing still",

            // BOSSES

            "Most bosses can become enraged, but only three times per fight",
            "Bosses are quite vulnerable to explosives, potions and scrolls.",

            "Mind that miasma released by Goo is highly flammable",
            "Goo have several ways to heal itself, but all of them can be denied",

            "Tengu teleports more often when receiving damage",
            "Tengu cannot teleport when ensnared",

            "DM-300 is neither organic nor magical creature.",
            "DM-300 starts using it's special attacks more often after every enrage.",

            "Dwarven King is completely invulnerable during the ritual",
            "Dwarven King's ritual can be disrupted by a certain spell...",

            // TERRAIN

            "Try to avoid moving in water if you are trying to sneak up on someone",
            "Consider sticking to high grass if you are trying to sneak up on someone",

            "Flying creatures can see over the high grass and are unaffected by terrain effects",
            "Water amplifies shock and frost effects, but extinguishes fire and washes off acid",

            "Traps only appear in standard rooms and never appear in corridors or special rooms",
            "Monsters inhabiting this dungeon are aware of all of its traps and secret doors",

            "Your health regeneration is tripled if you are sleeping on dry land",
            "Sleeping in the water is much less efficient than sleeping anywhere else",
            "Evasion chance is decreased for every adjacent tile which is occupied or impassable",

            // MISC

            "You can pour water from your waterskin to put out fires and wash away caustic ooze",
            "You can grow herbs by watering adjacent high grass tiles with your waterskins",

            "Your lantern makes it much easier to check for traps and secret doors",
            "Oil lantern can be used to start fires on adjacent tiles if you have spare oil flasks",
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
	private boolean pause = false;

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

        if( YetAnotherPixelDungeon.loadingTips() > 0 ) {

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
//					case RESURRECT:
//                        resurrect();
//                        break;
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

//                error = ERR_FILE_NOT_FOUND;
				
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
				if (thread.isAlive() || error != null || YetAnotherPixelDungeon.loadingTips() > 2 ) {
                    phase = Phase.STATIC;

                    if( !thread.isAlive() && error == null) {
                        message.text(TXT_CONTINUE);
                        message.measure();
                        message.x = (Camera.main.width - message.width()) / 2;
                        message.y = (Camera.main.height - message.height()) / 2;

                        TouchArea hotArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height) {
                            @Override
                            protected void onClick(Touchscreen.Touch touch) {
                                phase = Phase.FADE_OUT;
                                timeLeft = TIME_TO_FADE;
                                this.destroy();
                            }
                        };
                        add(hotArea);
                    }

                } else {
                    phase = Phase.FADE_OUT;
                    timeLeft = ( YetAnotherPixelDungeon.loadingTips() > 0 ?
                            TIME_TO_FADE * YetAnotherPixelDungeon.loadingTips() * 3 : TIME_TO_FADE );
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

                add(new WndError(error) {
                    public void onBackPressed() {
                        super.onBackPressed();
                        Game.switchScene(StartScene.class);
                    }
                });

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
			Dungeon.saveAll();
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
		Dungeon.saveAll();
		
		Level level;

		if( Dungeon.depth <= 25 ){

            if( Dungeon.depth >= Statistics.deepestFloor ){
                level = Dungeon.newLevel();
            } else {
                Dungeon.depth++;
                level = Dungeon.loadLevel( Dungeon.hero.heroClass );
            }
        } else {
		    // You hear distant a sound of  malicious laughter.
            level = Dungeon.loadLevel( Dungeon.hero.heroClass );
        }

		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell( true, true ) );
	}
	
	private void ascend() throws Exception {
		Actor.fixTime();
		
		Dungeon.saveAll();
		Dungeon.depth--;
		Level level = Dungeon.
                loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws Exception {
		
		Actor.fixTime();
		
		Dungeon.saveAll();
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

//	private void resurrect() throws Exception {
//
//        Actor.fixTime();
//
//        if (Dungeon.bossLevel()) {
//
//            Dungeon.hero.resurrect( Dungeon.depth );
//            Dungeon.depth--;
//            Level level = Dungeon.newLevel();
//            Dungeon.switchLevel( level, level.entrance );
//
//        } else {
//
//            Dungeon.hero.resurrect(-1);
//            Actor.clear();
//            Arrays.fill(Dungeon.visible, false);
//            Dungeon.level.reset();
//            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
//
//        }
//    }

	@Override
	protected void onBackPressed() {
		// Do nothing
	}
}
