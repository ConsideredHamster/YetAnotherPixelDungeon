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
package com.consideredhamster.yetanotherpixeldungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Blinded;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Recharging;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.ShopPainter;
import com.watabou.noosa.Game;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Piranha;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.levels.CavesBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.CavesLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.CityBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.CityLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.DeadEndLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.HallsBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.HallsLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.LastLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.LastShopLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.PrisonBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.PrisonLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerLevel;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.StartScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndResurrect;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public class Dungeon {
	
	public static int potionOfStrength;
	public static int potionOfExperience;
	public static int scrollsOfUpgrade;
	public static int scrollsOfEnchantment;

	public static int ankhs;
	public static int wands;
	public static int rings;
    public static int ammos;

    public static int bottles;
    public static int scrolls;
    public static int torches;
	
	public static int challenges;
	public static int difficulty;

	public static Hero hero;
	public static Level level;
	
	public static int depth;
    public static int gold;
	// Reason of death
	public static String resultDescription;
	
	public static HashSet<Integer> chapters;
	
	// Hero's field of view
	public static boolean[] visible = new boolean[Level.LENGTH];
	
	public static boolean nightMode;
	
	public static SparseArray<ArrayList<Item>> droppedItems;
	
	public static void init() {

		challenges = YetAnotherPixelDungeon.challenges();
		
		Actor.clear();
		
		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		
		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();
		
		Statistics.reset();
		Journal.reset();
		
		depth = 0;
		gold = 0;

		droppedItems = new SparseArray<ArrayList<Item>>();
		
		potionOfStrength = 0;
		potionOfExperience = 0;
		scrollsOfUpgrade = 0;
		scrollsOfEnchantment = 0;

		ankhs = 0;
		wands = 0;
		rings = 0;
		ammos = 0;

		bottles = 0;
		scrolls = 0;
		torches = 0;
		
		chapters = new HashSet<Integer>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		AmbitiousImp.Quest.reset();
		
		Room.shuffleTypes();

        ShopPainter.initAssortment();

		QuickSlot.quickslotValue_1 = null;
		QuickSlot.quickslotValue_2 = null;
		QuickSlot.quickslotValue_3 = null;

		hero = new Hero();

        Buff.affect( hero, Satiety.class ).setValue( Satiety.MAXIMUM );
        Buff.affect( hero, Recharging.class );

		Badges.reset();
		
		StartScene.curClass.initHero( hero );
	}
	
	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		depth++;

		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		
		Arrays.fill( visible, false );
		
		Level level;
		switch (depth) {
		case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            level = new SewerLevel();
            break;
        case 6:
            level = new SewerBossLevel();
            break;
		case 7:
		case 8:
        case 9:
        case 10:
        case 11:
            level = new PrisonLevel();
            break;
        case 12:
            level = new PrisonBossLevel();
            break;
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
            level = new CavesLevel();
            break;
		case 18:
            level = new CavesBossLevel();
            break;
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
            level = new CityLevel();
            break;
		case 24:
            level = new CityBossLevel();
            break;
		case 25:
            level = new LastShopLevel();
            break;
		case 26:
		case 27:
		case 28:
		case 29:
            level = new HallsLevel();
            break;
		case 30:
            level = new HallsBossLevel();
            break;
		case 31:
			level = new LastLevel();
			break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
//	public static void resetLevel( int pos ) {
//
//		Actor.clear();
//
//		Arrays.fill( visible, false );
//
//        bonus.reset();
//        switchLevel( bonus, pos );
//		switchLevel( bonus, bonus.exit );
//	}
	
	public static boolean shopOnLevel() {
		return depth % 5 == 0;
	}

    public static int chapter() {
        return ( depth - 1 ) / 6 + 1;
    }
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth % 6 == 0;
	}

	public static int questsCompleted() {

        int res = 0;

        if(Ghost.Quest.isCompleted())
            res++;

        if(Wandmaker.Quest.isCompleted())
            res++;

        if(Blacksmith.Quest.isCompleted())
            res++;

        if(AmbitiousImp.Quest.isCompleted())
            res++;

		return res;
	}

	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		
//		nightMode = new Date().getHours() < 7;
        nightMode = false;
		
		Dungeon.level = level;
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add( level.respawner() );
		}
		
		hero.pos = pos != -1 ? pos : level.exit;
		
		observe();
	}
	
	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() ); 
		}
		dropped.add(item);
	}
	
	public static boolean posNeeded() {
		int[] quota = {1, 0, 3, 1, 5, 2, 11, 4, 17, 6, 23, 8, 29, 10};
		return chance( quota, potionOfStrength );
	}

    public static boolean poeNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};
        return chance( quota, potionOfExperience );
    }
	
	public static boolean souNeeded() {
        int[] quota = {1, 0, 3, 1, 5, 2, 11, 4, 17, 6, 23, 8, 29, 10};
		return chance( quota, scrollsOfUpgrade );
	}
	
	public static boolean soeNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};
        return chance( quota, scrollsOfEnchantment );
	}

    public static boolean ankhsNeeded() {
        int[] quota = {5, 0, 11, 1, 17, 2, 23, 3, 29, 3};

        return chance( quota, ankhs );
    }

//    public static boolean vialsNeeded() {
//        int[] quota = {5, 1, 11, 2, 17, 3, 23, 4, 29, 5};
//
//        return chance( quota, vials );
//    }

    public static boolean wandsNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};

        return chance( quota, wands );
    }

    public static boolean ringsNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};

        return chance( quota, rings );
    }

    public static boolean ammosNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};

        return chance( quota, ammos );
    }

    public static boolean bottlesNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};

        return chance( quota, bottles );
    }

    public static boolean scrollsNeeded() {
        int[] quota = {1, 0, 5, 1, 11, 2, 17, 3, 23, 4, 29, 5};

        return chance( quota, scrolls );
    }

    public static boolean torchesNeeded() {
        int[] quota = {5, 2, 11, 4, 17, 6, 23, 8, 29, 10};

        return chance( quota, torches );
    }
	
	private static boolean chance( int[] quota, int number ) {
		
		for (int i=0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float)(qNumber - number) / (qDepth - depth + 1);
			}
		}
		
		return false;
	}
	
	private static final String RG_GAME_FILE	= "game.dat";
	private static final String RG_DEPTH_FILE	= "depth%d.dat";
	
	private static final String WR_GAME_FILE	= "warrior.dat";
	private static final String WR_DEPTH_FILE	= "warrior%d.dat";
	
	private static final String MG_GAME_FILE	= "mage.dat";
	private static final String MG_DEPTH_FILE	= "mage%d.dat";
	
	private static final String RN_GAME_FILE	= "ranger.dat";
	private static final String RN_DEPTH_FILE	= "ranger%d.dat";
	
	private static final String VERSION		= "version";
	private static final String DIFFICULTY	= "difficulty";
	private static final String CHALLENGES	= "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String LEVEL		= "level";
	private static final String DROPPED		= "dropped%d";
	private static final String POS			= "potionsOfStrength";
	private static final String POE			= "potionsOfExperience";
	private static final String SOU			= "scrollsOfEnhancement";
	private static final String SOE			= "scrollsOfEnchantment";
	private static final String ANKHS		= "ankhs";
	private static final String VIALS		= "vials";
	private static final String WANDS		= "wands";
	private static final String RINGS		= "rings";
	private static final String AMMOS		= "ammos";
	private static final String BOTTLES		= "bottles";
	private static final String SCROLLS		= "scrolls";
	private static final String TORCHES		= "torches";
	private static final String WT			= "transmutation";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";

	public static String gameFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_GAME_FILE;
		case SCHOLAR:
			return MG_GAME_FILE;
		case ACOLYTE:
			return RN_GAME_FILE;
		default:
			return RG_GAME_FILE;
		}
	}
	
	private static String depthFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_DEPTH_FILE;
		case SCHOLAR:
			return MG_DEPTH_FILE;
		case ACOLYTE:
			return RN_DEPTH_FILE;
		default:
			return RG_DEPTH_FILE;
		}
	}
	
	public static void saveGame( String fileName ) throws IOException {
		try {
			Bundle bundle = new Bundle();
			
			bundle.put( VERSION, Game.version );
			bundle.put( DIFFICULTY, difficulty );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			
			for (int d : droppedItems.keyArray()) {
				bundle.put( String.format( DROPPED, d ), droppedItems.get( d ) );
			}
			
			bundle.put( POS, potionOfStrength );
			bundle.put( POE, potionOfExperience );
			bundle.put( SOU, scrollsOfUpgrade );
			bundle.put( SOE, scrollsOfEnchantment );

			bundle.put( ANKHS, ankhs );
			bundle.put( WANDS, wands );
			bundle.put( RINGS, rings );
			bundle.put( AMMOS, ammos );

			bundle.put( BOTTLES, bottles );
			bundle.put( SCROLLS, scrolls );
			bundle.put( TORCHES, torches );

			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			AmbitiousImp.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			Room.storeRoomsInBundle( bundle );

            ShopPainter.saveAssortment( bundle );
			
			Statistics.storeInBundle( bundle );
			Journal.storeInBundle( bundle );
			
			QuickSlot.save( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Wand.save( bundle );
			Ring.save( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
			
		} catch (Exception e) {

			GamesInProgress.setUnknown( hero.heroClass );
		}
	}
	
	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		OutputStream output = Game.instance.openFileOutput( Utils.format( depthFile( hero.heroClass ), depth ), Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	
	public static void saveAll() throws IOException {

		if (hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( gameFile( hero.heroClass ) );
			saveLevel();
			
			GamesInProgress.set( hero.heroClass, depth, hero.lvl, difficulty, challenges != 0 );
			
		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.killedBy, WndResurrect.killedWith );
			
		}

	}

    public static boolean loaded() {
        return level != null;
    }
	
	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame( gameFile( cl ), true );
	}
	
	public static void loadGame( String fileName ) throws IOException {
		loadGame( fileName, false );
	}
	
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {
		
		Bundle bundle = gameBundle( fileName );
		
		Dungeon.challenges = bundle.getInt( CHALLENGES );
		Dungeon.difficulty = bundle.getInt( DIFFICULTY );

		Dungeon.level = null;
		Dungeon.depth = -1;
		
		if (fullLoad) {
			PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		}
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Wand.restore( bundle );
		Ring.restore( bundle );
		
		potionOfStrength = bundle.getInt( POS );
		potionOfExperience = bundle.getInt( POE );
		scrollsOfUpgrade = bundle.getInt( SOU );
		scrollsOfEnchantment = bundle.getInt( SOE );

		ankhs = bundle.getInt( ANKHS );
		wands = bundle.getInt( WANDS );
		rings = bundle.getInt( RINGS );
		ammos = bundle.getInt( AMMOS );

		bottles = bundle.getInt( BOTTLES );
		scrolls = bundle.getInt( SCROLLS );
		torches = bundle.getInt( TORCHES );

		if (fullLoad) {
			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				AmbitiousImp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				AmbitiousImp.Quest.reset();
			}
			
			Room.restoreRoomsFromBundle( bundle );

            ShopPainter.loadAssortment( bundle );
		}
		
		Bundle badges = bundle.getBundle( BADGES );
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}

//        QuickSlot.quickslotValue_0 = null;
//        QuickSlot.quickslotValue_1 = null;
//        QuickSlot.quickslotValue_2 = null;

		QuickSlot.restore( bundle );
		
		@SuppressWarnings("unused")
		String version = bundle.getString( VERSION );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );

        try {
            hero.belongings.restoreFromBundle(bundle.getBundle(HERO));
        } catch ( Exception e ) {
            YetAnotherPixelDungeon.reportException(e);
        }

//		QuickSlot.compress();

        if( hero.buff( Satiety.class ) == null ) {
            Buff.affect( hero, Satiety.class ).setValue( Satiety.MAXIMUM );
        }

        if( hero.buff( Recharging.class ) == null ) {
            Buff.affect( hero, Recharging.class );
        }
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );
		
		Statistics.restoreFromBundle( bundle );
		Journal.restoreFromBundle( bundle );
		
		droppedItems = new SparseArray<ArrayList<Item>>();
		for (int i=2; i <= Statistics.deepestFloor + 1; i++) {
			ArrayList<Item> dropped = new ArrayList<Item>();
			for (Bundlable b : bundle.getCollection( String.format( DROPPED, i ) ) ) {
				dropped.add( (Item)b );
			}
			if (!dropped.isEmpty()) {
				droppedItems.put( i, dropped );
			}
		}
	}
	
	public static Level loadLevel( HeroClass cl ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), depth ) ) ;
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return (Level)bundle.get( "level" );
	}
	
	public static void deleteGame( HeroClass cl, boolean deleteLevels ) {
		
		Game.instance.deleteFile( gameFile( cl ) );
		
		if (deleteLevels) {
			int depth = 1;
			while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}
		
		GamesInProgress.delete( cl );
	}
	
	public static Bundle gameBundle( String fileName ) throws IOException {
		
		InputStream input = Game.instance.openFileInput( fileName );
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return bundle;
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.challenges = (bundle.getInt( CHALLENGES ) != 0);
		if (info.depth == -1) {
			info.depth = bundle.getInt( "maxDepth" );	// FIXME
		}
		Hero.preview( info, bundle.getBundle( HERO ) );
	}
	
	public static void fail( String desc ) {
		resultDescription = desc;
		if (hero.belongings.getItem( Ankh.class ) == null) { 
			Rankings.INSTANCE.submit( false );
		}
	}
	
	public static void win( String desc ) {
		
		hero.belongings.identify();
		
		if (challenges != 0) {
			Badges.validateChampion();
		}
		
		resultDescription = desc;
		Rankings.INSTANCE.submit( true );
	}
	
	public static void observe() {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero);
        System.arraycopy(Level.fieldOfView, 0, visible, 0, visible.length);

        BArray.or(level.visited, visible, level.visited);
		
		GameScene.afterObserve();
	}
	
	private static boolean[] passable = new boolean[Level.LENGTH];
	
	public static int findPath( Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		if (Level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Level.avoid[to] && !(ch instanceof Piranha)) ? to : -1;
		}

		if (ch.flying || ch.buff( Tormented.class ) != null ||
            ch.buff( Blinded.class ) != null || ch.buff( Vertigo.class ) != null
        ) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}

        if (ch instanceof Mob && !(ch instanceof Piranha)) {
            BArray.or( passable, Level.illusory, passable );
        }

		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos] && ((Char) actor).invisible == 0) {
					passable[pos] = false;
				}
			}
		}
		
		return PathFinder.getStep( from, to, passable );
		
	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {
		
		if (ch.flying || ch.buff( Tormented.class ) != null || ch.buff( Vertigo.class ) != null || ch.buff( Blinded.class ) != null) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}

        if (ch instanceof Mob && !(ch instanceof Piranha)) {
            BArray.or( passable, Level.illusory, passable );
        }
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
                int pos = ((Char)actor).pos;
                if (visible[pos] && ((Char) actor).invisible == 0) {
                    passable[pos] = false;
                }
			}
		}

		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
