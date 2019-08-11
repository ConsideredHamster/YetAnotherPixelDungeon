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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfPhaseWarp;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.RippleShock;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.FogOfWar;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BannerSprites;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.EmoIcon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.FloatingText;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Ripple;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.DiscardedItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Banner;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BusyIndicator;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.GameLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.HealthIndicator;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.StatusPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Toast;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Toolbar;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag.Mode;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndGame;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndStory;

public class GameScene extends PixelScene {
	
	private static final String TXT_WELCOME			= "Welcome to level %d of Pixel Dungeon!";
	private static final String TXT_WELCOME_BACK	= "Welcome back to level %d of Pixel Dungeon!";
//	private static final String TXT_NIGHT_MODE		= "Be cautious, since the dungeon is even more dangerous at night!";
	
//	private static final String TXT_CHASM	= "Your steps echo across the dungeon.";
	private static final String TXT_WATER	= "You hear the water splashing around you.";
	private static final String TXT_GRASS	= "The smell of vegetation is thick in the air.";
	private static final String TXT_TRAPS	= "The atmosphere hints that this floor hides many secrets.";
	private static final String TXT_HAUNT	= "Eerie feeling sends shivers down your spine.";

//	private static final String TXT_SECRETS	= "The atmosphere hints that this floor hides many secrets.";
	
	static GameScene scene;
	
	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;
	
	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group ripples;
	private Group hazards;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	
	private Toolbar toolbar;
	private Toast prompt;
	
	@Override
	public void create() {

        Music.INSTANCE.play( Dungeon.level.currentTrack(), true );
		Music.INSTANCE.volume( 1f );
		
		YetAnotherPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( defaultZoom + YetAnotherPixelDungeon.zoom() );
		
		scene = this;
		
		terrain = new Group();
		add( terrain );
		
		water = new SkinnedBlock( 
			Level.WIDTH * DungeonTilemap.SIZE, 
			Level.HEIGHT * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() );
		terrain.add( water );
		
		ripples = new Group();
		terrain.add( ripples );
		
		tiles = new DungeonTilemap();
		terrain.add( tiles );
		
		Dungeon.level.addVisuals( this );

		heaps = new Group();
		add( heaps );

        int size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite(Dungeon.level.heaps.valueAt(i));
		}

        emitters = new Group();

        effects = new Group();

        emoicons = new Group();

        hazards = new Group();
        add( hazards );

        for (Hazard hazard : Dungeon.level.hazards) {
            addHazardSprite( hazard );
        }

        sortHazards();

        mobs = new Group();
        add( mobs );

        for (Mob mob : Dungeon.level.mobs) {
            addMobSprite(mob);
            if (Statistics.amuletObtained) {
                mob.beckon( Dungeon.hero.pos );
            }
        }
		
		gases = new Group();
		add( gases );
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}

        add( emitters );
        add( effects );
        add( emoicons );
		
		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
		add(fog);
		
		brightness( YetAnotherPixelDungeon.brightness() );
		
		spells = new Group();
		add( spells );

		statuses = new Group();
		add(statuses);

		hero = new HeroSprite();
		hero.place(Dungeon.hero.pos);
		hero.updateArmor();
        mobs.add(hero);

		add(new HealthIndicator());
		
		add(cellSelector = new CellSelector(tiles));
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect(0, uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height());
		add( toolbar );

        StatusPane sb = new StatusPane( (int)( toolbar.top() - toolbar.btnQuick0.height() ) );
        sb.camera = uiCamera;
        sb.setSize(uiCamera.width, 0);
        add(sb);

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), uiCamera.width - toolbar.btnQuick0.width(),  0 );
		add( log );
		
		if (Dungeon.depth < Statistics.deepestFloor) {
			GLog.i( TXT_WELCOME_BACK, Dungeon.depth );
		} else {
			GLog.i( TXT_WELCOME, Dungeon.depth );
			Sample.INSTANCE.play( Assets.SND_DESCEND );
		}
		switch (Dungeon.level.feeling) {
//		case CHASM:
//			GLog.w( TXT_CHASM );
//			break;
		case WATER:
			GLog.w( TXT_WATER );
			break;
		case GRASS:
			GLog.w( TXT_GRASS );
			break;
        case TRAPS:
            GLog.w( TXT_TRAPS );
            break;
        case HAUNT:
            GLog.w( TXT_HAUNT );
            break;
		default:
		}

//		if (Dungeon.bonus instanceof RegularLevel &&
//			((RegularLevel)Dungeon.bonus).secretDoors > Random.IntRange( 3, 4 )) {
//			GLog.w( TXT_SECRETS );
//		}

//		if (Dungeon.nightMode && !Dungeon.bossLevel()) {
//			GLog.w( TXT_NIGHT_MODE );
//		}
		
		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 50;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
            ScrollOfPhaseWarp.appear( Dungeon.hero, Dungeon.hero.pos );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
            Sample.INSTANCE.play( Assets.SND_TELEPORT );
			break;
		case RETURN:
            ScrollOfPhaseWarp.appear( Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				break;
			case 7:
				WndStory.showChapter( WndStory.ID_PRISON );
				break;
			case 13:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 19:
				WndStory.showChapter( WndStory.ID_METROPOLIS );
				break;
			case 26:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
//			if (Dungeon.hero.isAlive() && Dungeon.depth != 25) {
//				Badges.validateNoKilling();
//			}
			break;
		default:
		}
		
		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell( false, true );
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
//				} else if (item instanceof Hazard.Seed) {
//					Dungeon.level.plant( (Hazard.Seed)item, pos );
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

        Dungeon.hero.updateSpriteState();
        Camera.main.target = hero;

		fadeIn();
	}
	
	public void destroy() {
		
		scene = null;
		Badges.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}
	
	@Override
	public synchronized void update() {
		if ( Dungeon.hero == null ) {
			return;
		}
			
		super.update();

        if( water != null ) {
            water.offset(0, -5 * Game.elapsed);
        }
		
		Actor.process();
		
		if (Dungeon.hero.ready && !Dungeon.hero.morphed ) {
			log.newLine();
		}

        if( cellSelector != null ) {
            cellSelector.enabled = Dungeon.hero.ready;
        }
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add(new WndGame());
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}
	
	public void brightness( boolean value ) {
		water.rm = water.gm = water.bm = 
		tiles.rm = tiles.gm = tiles.bm = 
			value ? 1.5f : 1.0f;
		if (value) {
			fog.am = +2f;
			fog.aa = -1f;
		} else {
			fog.am = +1f;
			fog.aa =  0f;
		}
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add(heap.sprite);
	}
	
	private void addHazardSprite( Hazard hazard ) {
        HazardSprite sprite = hazard.sprite();
        sprite.visible = Dungeon.visible[hazard.pos];
        hazards.add( sprite );
        sprite.link( hazard );
	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}

    public void addMobSprite( Mob mob ) {
        CharSprite sprite = mob.sprite();
        sprite.visible = Dungeon.visible[mob.pos];
        mobs.add( sprite );
        sprite.link( mob );
        mob.updateSpriteState();
    }
	
	private void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		add( banner );
	}
	
	// -------------------------------------------------------
	
	public static void add( Hazard hazard ) {
        Dungeon.level.hazards.add( hazard );
        Actor.add( hazard );
        scene.addHazardSprite( hazard );
        sortHazards();
	}

	public static void sortHazards() {
        // let's sort hazard sprites according to their priority
        // it could've been done better, but i'd rather not mess with watabou's libraries yet

        HashSet<Hazard> hazards = (HashSet<Hazard>)Dungeon.level.hazards.clone();

        for( int i = 0 ; i < Dungeon.level.hazards.size() ; i++ ){

            Hazard selected = null;

            for( Hazard current : hazards ){
                if( selected == null || selected.sprite.spritePriority() < current.sprite.spritePriority() ) {
                    selected = current;
                }
            }

            scene.hazards.sendToBack( selected.sprite );
            hazards.remove( selected );
        }
    }

    public static void add( Blob gas ) {
        Actor.add( gas );
        if (scene != null) {
            scene.addBlobSprite(gas);
        }
    }
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite(heap);
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite(heap);
		}
	}

    public static void add( Mob mob ) {
        Dungeon.level.mobs.add( mob );
        Actor.add(mob);
        Actor.occupyCell( mob );
        scene.addMobSprite( mob );
    }

    public static void changeMobSprite( Mob mob, CharSprite sprite ) {

        if( mob.sprite.getClass() != sprite.getClass() ){

            mob.sprite.killAndErase();
            scene.mobs.erase( mob.sprite );

            mob.sprite = sprite;
            sprite.visible = Dungeon.visible[ mob.pos ];
            scene.mobs.add( sprite );

            sprite.link( mob );
            mob.updateSpriteState();

        }
    }
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed(mob, delay);
		Actor.occupyCell( mob );
		scene.addMobSprite(mob);
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add(icon);
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add(effect);
	}

    public static Ripple ripple( int pos ) {
        Ripple ripple = (Ripple)scene.ripples.recycle( Ripple.class );
        ripple.reset(pos);
        return ripple;
    }

    public static RippleShock electrify( int pos ) {
        RippleShock ripple = (RippleShock)scene.ripples.recycle( RippleShock.class );
        ripple.reset(pos);
        return ripple;
    }
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item ) {
		scene.toolbar.pickup( item );
	}
	
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updated.set(0, 0, Level.WIDTH, Level.HEIGHT);
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updated.union(cell % Level.WIDTH, cell / Level.WIDTH);
		}
	}

    public static void updateMap( boolean[] visible ) {
        if (scene != null) {
            for( int i = 0 ; i < visible.length ; i++ ) {
                if( visible[i] ) {
                    scene.tiles.updated.union(i % Level.WIDTH, i / Level.WIDTH);
                }
            }
        }
    }
	
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover(pos, oldValue);
		}
	}
	
	public static void show( Window wnd ) {
		cancelCellSelector();
		scene.add( wnd );
	}
	
	public static void afterObserve() {
		if (scene != null && scene.fog != null) {
			scene.fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
			
			for (Mob mob : Dungeon.level.mobs) {
                if( mob.sprite.visible = Dungeon.visible[mob.pos] ){
                    if( Dungeon.level.map[ mob.pos ] == Terrain.DOOR_ILLUSORY ){
                        Door.discover( mob.pos );
                    }
                }
            }


            updateMap(Dungeon.visible);
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play(Assets.SND_DEATH);
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		scene.prompt(listener.prompt());
	}

    public static boolean checkListener( CellSelector.Listener listener ) {
        return cellSelector.listener == listener;
    }

    public static boolean checkListener() {
        return cellSelector.listener == null || cellSelector.listener == defaultCellListener;
    }

    public static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}

	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd = mode == Mode.HERB ?
			WndBag.herbPouch(listener, mode, title) :
			WndBag.lastBag( listener, mode, title );
		scene.add( wnd );
		
		return wnd;
	}

	static boolean cancel() {

		if (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.interrupt();
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlot.cancel();
	}
	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
