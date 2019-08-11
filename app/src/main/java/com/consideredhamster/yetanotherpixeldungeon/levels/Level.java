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
package com.consideredhamster.yetanotherpixeldungeon.levels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.consideredhamster.yetanotherpixeldungeon.items.food.RationMedium;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.EmptyBottle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Challenges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Alchemy;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.WellWater;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.MindVision;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Statue;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Wraith;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.LeafParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.WindParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.ScrollHolder;
import com.consideredhamster.yetanotherpixeldungeon.items.food.Food;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfMending;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.*;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.ShadowCaster;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public abstract class Level implements Bundlable {

	public static enum Feeling {
		NONE,
		CHASM,
		HAUNT,
		TRAPS,
		WATER,
		GRASS
	};

	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int LENGTH = WIDTH * HEIGHT;

	public static final int[] NEIGHBOURS1 = {0};
	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
	public static final int[] NEIGHBOURS5 = {0, -WIDTH, +1, +WIDTH, -1};
	public static final int[] NEIGHBOURSX = {0, -WIDTH-1, -WIDTH+1, +WIDTH-1, +WIDTH+1};
	public static final int[] NEIGHBOURS8 = {+1, -1, +WIDTH, -WIDTH, +1+WIDTH, +1-WIDTH, -1+WIDTH, -1-WIDTH};
	public static final int[] NEIGHBOURS9 = {0, +1, -1, +WIDTH, -WIDTH, +1+WIDTH, +1-WIDTH, -1+WIDTH, -1-WIDTH};

    public static final int[] NEIGHBOURS12 = {

            +2, +2+WIDTH, +2-WIDTH,
            -2, -2+WIDTH, -2-WIDTH,

            +WIDTH*2, +WIDTH*2+1, +WIDTH*2-1,
            -WIDTH*2, -WIDTH*2+1, -WIDTH*2-1,

    };

	public static final int[] NEIGHBOURS16 = {

            +2, +2+WIDTH, +2-WIDTH,
            -2, -2+WIDTH, -2-WIDTH,

            +WIDTH*2, +WIDTH*2+1, +WIDTH*2-1,
            -WIDTH*2, -WIDTH*2+1, -WIDTH*2-1,

            +WIDTH*2+2, -WIDTH*2+2,
            +WIDTH*2-2, -WIDTH*2-2,

    };

	protected static final float TIME_TO_RESPAWN	= 65f;

	public static boolean resizingNeeded;
	public static int loadedMapSize;

	public int[] map;
	public boolean[] visited;
	public boolean[] mapped;

//	public int viewDistance = 8;
	public int mobsSpawned = 0;

	public static boolean[] fieldOfView = new boolean[LENGTH];

	public static boolean[] passable	 = new boolean[LENGTH];
	public static boolean[] mob_passable = new boolean[LENGTH];
	public static boolean[] losBlockHigh = new boolean[LENGTH];
	public static boolean[] losBlockLow	 = new boolean[LENGTH];
	public static boolean[] flammable    = new boolean[LENGTH];
	public static boolean[] trapped      = new boolean[LENGTH];
	public static boolean[] solid		 = new boolean[LENGTH];
	public static boolean[] avoid        = new boolean[LENGTH];
	public static boolean[] water		 = new boolean[LENGTH];
	public static boolean[] chasm = new boolean[LENGTH];
	public static boolean[] quiet		 = new boolean[LENGTH];
	public static boolean[] important    = new boolean[LENGTH];
	public static boolean[] illusory     = new boolean[LENGTH];

	public static boolean[] discoverable	= new boolean[LENGTH];

	public Feeling feeling = Feeling.NONE;

	public int entrance;
	public int exit;

	public HashSet<Mob> mobs;
	public HashSet<Hazard> hazards;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>,Blob> blobs;
//	public SparseArray<Hazard> plants;

	protected ArrayList<Item> itemsToSpawn = new ArrayList<Item>();

	public int color1 = 0x004400;
	public int color2 = 0x88CC44;

	protected static boolean pitRoomNeeded = false;
	protected static boolean weakFloorCreated = false;

	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String MOBS_SPAWNED = "mobs_killed";
	private static final String ENTRANCE	= "entrance";
	private static final String EXIT		= "exit";
	private static final String HEAPS		= "heaps";
	private static final String HAZARDS		= "hazards";
	private static final String MOBS		= "mobs";
	private static final String BLOBS		= "blobs";

	public void create() {

		resizingNeeded = false;

		map = new int[LENGTH];
		visited = new boolean[LENGTH];
		Arrays.fill( visited, false );
		mapped = new boolean[LENGTH];
		Arrays.fill( mapped, false );

        heaps = new SparseArray<Heap>();
        hazards = new HashSet<Hazard>();
        mobs = new HashSet<Mob>();
        blobs = new HashMap<Class<? extends Blob>,Blob>();

        if (!Dungeon.bossLevel()) {

            addItemToSpawn( new RationMedium() );

            if (Dungeon.posNeeded()) {
                addItemToSpawn( new PotionOfStrength() );
                Dungeon.potionOfStrength++;
            }

            if (Dungeon.poeNeeded()) {
                addItemToSpawn( new PotionOfWisdom() );
                Dungeon.potionOfExperience++;
            }

            if (Dungeon.souNeeded()) {
                addItemToSpawn( new ScrollOfUpgrade() );
                Dungeon.scrollsOfUpgrade++;
            }

            if (Dungeon.soeNeeded()) {
                addItemToSpawn( new ScrollOfEnchantment() );
                Dungeon.scrollsOfEnchantment++;
            }

            if (Dungeon.ankhsNeeded()) {
                addItemToSpawn( new Ankh().identify() );
                Dungeon.ankhs++;
            }

//            if (Dungeon.vialsNeeded()) {
//                addItemToSpawn( new Waterskin() );
//                Dungeon.vials++;
//            }

            if (Dungeon.wandsNeeded()) {
                addItemToSpawn( Generator.random( Generator.Category.WAND ) );
                Dungeon.wands++;
            }

            if (Dungeon.ringsNeeded()) {
                addItemToSpawn( Generator.random( Generator.Category.RING ) );
                Dungeon.rings++;
            }

            if (Dungeon.ammosNeeded()) {
                addItemToSpawn( Generator.random( Generator.Category.AMMO ) );
                Dungeon.ammos++;
            }

            if (Dungeon.bottlesNeeded()) {
                addItemToSpawn( new EmptyBottle() );
                Dungeon.bottles++;
            }

//            if (Dungeon.scrollsNeeded()) {
//                addItemToSpawn( new EmptyScroll() );
//                Dungeon.scrolls++;
//            }

            if (Dungeon.torchesNeeded()) {
                addItemToSpawn( new OilLantern.OilFlask() );
                Dungeon.torches++;
            }

            if (Dungeon.depth % 6 != 1 && Dungeon.depth < 24 ) {

                int chapter = Dungeon.chapter();

                switch ( Random.Int( 12 ) ) {
                    case 0:
                        if( chapter == 1 ) {
                            feeling = Feeling.WATER;
                        } else {
                            feeling = Feeling.HAUNT;
                        }

                        break;
                    case 1:
                        if( chapter == 2 ) {
                            feeling = Feeling.HAUNT;
                        } else {
                            feeling = Feeling.GRASS;
                        }

                        break;
                    case 2:
                        if( chapter == 3 ) {
                            feeling = Feeling.GRASS;
                        } else {
                            feeling = Feeling.TRAPS;
                        }

                        break;
                    case 3:
                        if( chapter == 4 ) {
                            feeling = Feeling.TRAPS;
                        } else {
                            feeling = Feeling.WATER;
                        }

                        break;
                }
            }
        }

		boolean pitNeeded = Dungeon.depth > 1 && weakFloorCreated;

		do {
			Arrays.fill( map, Terrain.WALL );
//			Arrays.fill( map, feeling == Feeling.CHASM ? Terrain.CHASM : Terrain.WALL );

			pitRoomNeeded = pitNeeded;
			weakFloorCreated = false;

		} while (!build());
		decorate();

		buildFlagMaps();
		cleanWalls();

		createMobs();
		createItems();
	}

	public void reset() {

		for (Mob mob : mobs.toArray( new Mob[0] )) {
			if (!mob.reset()) {
				mobs.remove( mob );
			}
		}

        for (Class<? extends Blob> blob : blobs.keySet()) {
            if ( !( blob == WellWater.class || blob == Alchemy.class ) ) {
                blobs.remove( blob );
            }
        }

		createMobs();
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

        heaps = new SparseArray<Heap>();
        hazards = new HashSet<Hazard>();
        mobs = new HashSet<Mob>();
        blobs = new HashMap<Class<? extends Blob>, Blob>();

		map		= bundle.getIntArray( MAP );
		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );

		mobsSpawned = bundle.getInt(MOBS_SPAWNED);

		entrance	= bundle.getInt( ENTRANCE );
		exit		= bundle.getInt( EXIT );

		weakFloorCreated = false;

		adjustMapSize();

		Collection<Bundlable> collection = bundle.getCollection( HEAPS );
		for (Bundlable h : collection) {
			Heap heap = (Heap)h;
            if( heap != null ){
                if( resizingNeeded ){
                    heap.pos = adjustPos( heap.pos );
                }
                heaps.put( heap.pos, heap );
            }
		}

		collection = bundle.getCollection( HAZARDS );
		for (Bundlable z : collection) {
            Hazard hazard = (Hazard)z;
            if (hazard != null){
                if( resizingNeeded ){
                    hazard.pos = adjustPos( hazard.pos );
                }
                hazards.add( hazard );
            }
		}

		collection = bundle.getCollection( MOBS );
		for (Bundlable m : collection) {
			Mob mob = (Mob)m;
			if (mob != null) {
				if (resizingNeeded) {
					mob.pos = adjustPos( mob.pos );
				}
				mobs.add( mob );
			}
		}

		collection = bundle.getCollection( BLOBS );
		for (Bundlable b : collection) {
			Blob blob = (Blob)b;
			blobs.put( blob.getClass(), blob );
		}

		buildFlagMaps();
		cleanWalls();
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		bundle.put( ENTRANCE, entrance );
		bundle.put( EXIT, exit );
		bundle.put( HEAPS, heaps.values() );
		bundle.put( HAZARDS, hazards );
		bundle.put( MOBS, mobs );
		bundle.put( BLOBS, blobs.values() );
		bundle.put( MOBS_SPAWNED, mobsSpawned );
	}

	public int tunnelTile() {
		return Terrain.EMPTY;
//		return feeling == Feeling.CHASM ? Terrain.EMPTY_SP : Terrain.EMPTY;
	}

	private void adjustMapSize() {
		// For levels saved before 1.6.3
		if (map.length < LENGTH) {

			resizingNeeded = true;
			loadedMapSize = (int)Math.sqrt( map.length );

			int[] map = new int[LENGTH];
			Arrays.fill( map, Terrain.WALL );

			boolean[] visited = new boolean[LENGTH];
			Arrays.fill( visited, false );

			boolean[] mapped = new boolean[LENGTH];
			Arrays.fill( mapped, false );

			for (int i=0; i < loadedMapSize; i++) {
				System.arraycopy( this.map, i * loadedMapSize, map, i * WIDTH, loadedMapSize );
				System.arraycopy( this.visited, i * loadedMapSize, visited, i * WIDTH, loadedMapSize );
				System.arraycopy( this.mapped, i * loadedMapSize, mapped, i * WIDTH, loadedMapSize );
			}

			this.map = map;
			this.visited = visited;
			this.mapped = mapped;

			entrance = adjustPos( entrance );
			exit = adjustPos( exit );
		} else {
			resizingNeeded = false;
		}
	}

	public int adjustPos( int pos ) {
		return (pos / loadedMapSize) * WIDTH + (pos % loadedMapSize);
	}

	public String tilesTex() {
		return null;
	}

	public String waterTex() {
		return null;
	}

	abstract protected boolean build();

	abstract protected void decorate();

	abstract protected void createMobs();

	abstract protected void createItems();


    public String currentTrack() {

        switch( Dungeon.chapter() ){
            case 1:
                return Assets.TRACK_CHAPTER_1;
            case 2:
                return Assets.TRACK_CHAPTER_2;
            case 3:
                return Assets.TRACK_CHAPTER_3;
            case 4:
                return Assets.TRACK_CHAPTER_4;
            case 5:
                return Dungeon.depth > 25 ? Assets.TRACK_CHAPTER_5 : Assets.TRACK_CHAPTER_4;
            default:
                return Assets.TRACK_CHAPTER_1;
        }

    };

	public void addVisuals( Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (chasm[i]) {
				scene.add( new WindParticle.Wind( i ) );
				if (i >= WIDTH && water[i-WIDTH]) {
					scene.add( new FlowParticle.Flow( i - WIDTH ) );
				}
			} else if( map[i] == Terrain.WELL ) {
                scene.add( new Fountain( i ) );
            }
		}
	}



    private static class Fountain extends Emitter {

        private int pos;
        private float rippleDelay = 0;

        public Fountain( int pos ) {
            super();

            this.pos = pos;

            on = true;
        }

        @Override
        public void update() {
            if (visible = Dungeon.visible[pos]) {

                if ((rippleDelay -= Game.elapsed) <= 0) {
                    GameScene.ripple( pos );
                    rippleDelay = 1f;
                }
            }
        }
    }

	public int nMobs() {
		return 0;
	}

	public Actor respawner() {
		return new Actor() {
			@Override
			protected boolean act() {

				if (mobs.size() < nMobs()) {

                    Mob mob  = (feeling == Feeling.HAUNT && Random.Int(5) == 0 ?
                                new Wraith() : Bestiary.mob(Dungeon.depth));

                    mob.state = mob.WANDERING;

                    mob.pos = randomRespawnCell( mob.flying, false );

                    if ( Dungeon.hero.isAlive() && mob.pos != -1 ) {
                        mobsSpawned++;
                        GameScene.add( mob );
						if (Statistics.amuletObtained) {
							mob.beckon( Dungeon.hero.pos );
						}
					}
				}

				spend( ( TIME_TO_RESPAWN - Dungeon.chapter() * 5 - ( feeling == Feeling.TRAPS ? 10 : 0 ) + mobsSpawned) );
				return true;
			}
		};
	}

    public int randomRespawnCell() {
        return randomRespawnCell( false, false );
    }

    public int randomRespawnCell( boolean ignoreTraps, boolean ignoreView ) {

        ArrayList<Integer> cells = getPassableCellsList();

        if( !ignoreTraps )
            cells = filterTrappedCells( cells );

        if( !ignoreView )
            cells = filterVisibleCells( cells );

        return !cells.isEmpty() ? Random.element( cells ) : -1 ;
    }

    public ArrayList<Integer> getPassableCellsList() {

        ArrayList<Integer> result = new ArrayList<>();

        for( int cell = 0 ; cell < Level.LENGTH ; cell++ ){

            if( !solid[cell] && passable[cell] && Actor.findChar(cell) == null ) {
                result.add( cell );
            }
        }

        return result;
    }

    public ArrayList<Integer> filterTrappedCells( ArrayList<Integer> cells ) {

        ArrayList<Integer> result = new ArrayList<>();

        for( Integer cell : cells ){

            if( mob_passable[cell] ) {
                result.add( cell );
            }

        }

        return result;
    }

    public ArrayList<Integer> filterVisibleCells( ArrayList<Integer> cells ) {

        ArrayList<Integer> result = new ArrayList<>();

        for( Integer cell : cells ){

            if( !Dungeon.visible[cell] && distance(Dungeon.hero.pos, cell) > 8 ) {
                result.add( cell );
            }

        }

        return result;
    }

    public Integer getRandomCell( ArrayList<Integer> cells ) {

        return Random.element( cells );

    }

	public int randomDestination() {
		int cell;
		do {
			cell = Random.Int( LENGTH );
		} while (!mob_passable[cell]);
		return cell;
	}

	public void addItemToSpawn( Item item ) {
		if (item != null) {
			itemsToSpawn.add( item );
		}
	}

	public Item itemToSpawnAsPrize() {
		if (Random.Int( itemsToSpawn.size() + 1 ) > 0) {
			Item item = Random.element( itemsToSpawn );
			itemsToSpawn.remove( item );
			return item;
		} else {
			return null;
		}
	}

    public Item itemToSpawnAsPrize( Class<? extends Item> category ) {
        for (Item item : itemsToSpawn) {
            if( category.isInstance(item) ) {
                itemsToSpawn.remove( item );
                return item;
            }
        }

        return null;
    }

	private void buildFlagMaps() {

		for (int i=0; i < LENGTH; i++) {
			int flags = Terrain.flags[map[i]];
			passable[i]		= (flags & Terrain.PASSABLE) != 0;
			mob_passable[i]	= passable[i] && (flags & Terrain.TRAPPED) == 0 || (flags & Terrain.ILLUSORY) != 0;
            losBlockLow[i]	= (flags & Terrain.LOS_BLOCKING) != 0;
            losBlockHigh[i]	= losBlockLow[i] && (flags & Terrain.SOLID) != 0 ;
			flammable[i]	= (flags & Terrain.FLAMMABLE) != 0;
			trapped[i]		= (flags & Terrain.TRAPPED) != 0;
			solid[i]		= (flags & Terrain.SOLID) != 0;
			avoid[i]		= (flags & Terrain.AVOID) != 0;
			water[i]		= (flags & Terrain.LIQUID) != 0;
			chasm[i]		= (flags & Terrain.PIT) != 0;
			important[i]	= (flags & Terrain.IMPORTANT) != 0;
			illusory[i]	    = (flags & Terrain.ILLUSORY) != 0;
            quiet[i]		= map[i] == Terrain.HIGH_GRASS ;
		}

		int lastRow = LENGTH - WIDTH;

		for (int i=0; i < WIDTH; i++) {
            passable[i] = mob_passable[i] = avoid[i] = false;
			passable[lastRow + i] = mob_passable[lastRow + i] = avoid[lastRow + i] = false;
		}
		for (int i=WIDTH; i < lastRow; i += WIDTH) {
			passable[i] = mob_passable[i] = avoid[i] = false;
			passable[i + WIDTH-1] = mob_passable[i + WIDTH-1] = avoid[i + WIDTH-1] = false;
		}

		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {

			if (water[i]) {
				int t = Terrain.WATER_TILES;
				for (int j=0; j < NEIGHBOURS4.length; j++) {
					if ((Terrain.flags[map[i + NEIGHBOURS4[j]]] & Terrain.UNSTITCHABLE) != 0) {
						t += 1 << j;
					}
				}
				map[i] = t;
			}

			if (chasm[i]) {
				if (!chasm[i - WIDTH]) {
					int c = map[i - WIDTH];
					if (c == Terrain.EMPTY_SP || c == Terrain.STATUE_SP) {
						map[i] = Terrain.CHASM_FLOOR_SP;
					} else if (water[i - WIDTH]) {
						map[i] = Terrain.CHASM_WATER;
					} else if ((Terrain.flags[c] & Terrain.UNSTITCHABLE) != 0) {
						map[i] = Terrain.CHASM_WALL;
					} else {
						map[i] = Terrain.CHASM_FLOOR;
					}
				}
			}
		}
	}

	private void cleanWalls() {
		for (int i=0; i < LENGTH; i++) {

			boolean d = false;

			for (int j=0; j < NEIGHBOURS9.length; j++) {
				int n = i + NEIGHBOURS9[j];
				if (n >= 0 && n < LENGTH && map[n] != Terrain.WALL && map[n] != Terrain.WALL_DECO && map[n] != Terrain.WALL_SIGN) {
					d = true;
					break;
				}
			}

			if (d) {
				d = false;

				for (int j=0; j < NEIGHBOURS9.length; j++) {
					int n = i + NEIGHBOURS9[j];
					if (n >= 0 && n < LENGTH && !chasm[n]) {
						d = true;
						break;
					}
				}
			}

			discoverable[i] = d;
		}
	}

	public static void set( int cell, int terrain ) {

		int flags = Terrain.flags[terrain];
		passable[cell]		= (flags & Terrain.PASSABLE) != 0;
        mob_passable[cell]	= passable[cell] && (flags & Terrain.TRAPPED) == 0 || (flags & Terrain.ILLUSORY) != 0;
		losBlockLow[cell]	= (flags & Terrain.LOS_BLOCKING) != 0;
		losBlockHigh[cell]	= losBlockLow[cell] && (flags & Terrain.SOLID) != 0;
		flammable[cell]		= (flags & Terrain.FLAMMABLE) != 0;
		trapped[cell]		= (flags & Terrain.TRAPPED) != 0;
		solid[cell]			= (flags & Terrain.SOLID) != 0;
		avoid[cell]			= (flags & Terrain.AVOID) != 0;
		chasm[cell]			= (flags & Terrain.PIT) != 0;
		water[cell]			= (flags & Terrain.LIQUID) != 0;
		important[cell]		= (flags & Terrain.IMPORTANT) != 0;
		illusory[cell]		= (flags & Terrain.ILLUSORY) != 0;
        quiet[cell]			= terrain == Terrain.HIGH_GRASS;

        Painter.set( Dungeon.level, cell, terrain );
	}

	public Heap drop( Item item, int cell ) {
        return drop( item, cell, false );
    }

	public Heap drop( Item item, int cell, boolean stackHeaps ) {

		if (Dungeon.isChallenged( Challenges.NO_FOOD ) && item instanceof Food) {
			item = new Gold( item.price() );
		} else
		if (Dungeon.isChallenged( Challenges.NO_ARMOR ) && item instanceof BodyArmor) {
			item = new Gold( item.price() );
		} else
		if (Dungeon.isChallenged( Challenges.NO_HEALING ) && item instanceof PotionOfMending) {
			item = new Gold( item.price() );
		} else
		if (Dungeon.isChallenged( Challenges.NO_HERBALISM ) && item instanceof Herb) {
			item = new Gold( item.price() );
		} else
		if (Dungeon.isChallenged( Challenges.NO_SCROLLS ) && (item instanceof Scroll || item instanceof ScrollHolder)) {
//			if (item instanceof ScrollOfUpgrade) {
				// These scrolls still can be found
//			} else {
				item = new Gold( item.price() );
//			}
		}

		if ((map[cell] == Terrain.ALCHEMY) && !(item instanceof Herb)) {
			int n;
			do {
				n = cell + NEIGHBOURS8[Random.Int( 8 )];
			} while ( !Level.passable[n] && !Level.avoid[n] );
			cell = n;
		}

		Heap heap = heaps.get( cell );

		if (heap == null) {

			heap = new Heap();
			heap.pos = cell;
			if (map[cell] == Terrain.CHASM || (Dungeon.level != null && chasm[cell])) {
				Dungeon.dropToChasm( item );
				GameScene.discard( heap );
			} else {
				heaps.put( cell, heap );
				GameScene.add( heap );
			}

		} else if (!stackHeaps && heap.type != Heap.Type.HEAP) {

			int n;
			do {
				n = cell + Level.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Level.passable[n] && !Level.avoid[n]);
			return drop( item, n );

		}

		heap.drop(item);

		if (Dungeon.level != null) {
			press( cell, null );
		}

		return heap;
	}

	public int pitCell() {
		return randomRespawnCell();
	}

    public float stealthModifier( int pos ) {
		return ( Level.water[pos] ? 0.75f : ( Level.quiet[pos] ? 1.25f : 1.0f ) );
	}

	public void press( int cell, Char ch ) {

        switch (map[cell]) {

            case Terrain.WELL:

                WellWater.affectCell(cell);
                break;

            case Terrain.DOOR_CLOSED:
                Door.enter(cell);
                break;

            case Terrain.DOOR_ILLUSORY:
                Door.discover(cell);
                break;

        }

        for( Hazard hazard : Hazard.findHazards( cell ) ){
            hazard.press( cell, ch );
        }


        if( ch == null || !ch.flying ) {

            if ( chasm[cell] ) {

                if (ch == Dungeon.hero) {
                    Chasm.heroFall(cell);
                } else if (ch instanceof Mob ) {
                    Chasm.mobFall( (Mob)ch );
                }

                return;
            }

            switch (map[cell]) {

                case Terrain.SECRET_TOXIC_TRAP:
                case Terrain.TOXIC_TRAP:
                case Terrain.SECRET_FIRE_TRAP:
                case Terrain.FIRE_TRAP:
                case Terrain.SECRET_BOULDER_TRAP:
                case Terrain.BOULDER_TRAP:
                case Terrain.SECRET_POISON_TRAP:
                case Terrain.POISON_TRAP:
                case Terrain.SECRET_ALARM_TRAP:
                case Terrain.ALARM_TRAP:
                case Terrain.SECRET_LIGHTNING_TRAP:
                case Terrain.LIGHTNING_TRAP:
                case Terrain.SECRET_BLADE_TRAP:
                case Terrain.BLADE_TRAP:
                case Terrain.SECRET_SUMMONING_TRAP:
                case Terrain.SUMMONING_TRAP:
                    Trap.trigger(cell);
                    break;

                case Terrain.HIGH_GRASS:

                    if( Dungeon.visible[cell] ) {
                        CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, Random.IntRange(2, 4));
                    }

                    break;

                case Terrain.GRASS:
                    if( Dungeon.visible[cell] ) {
                        CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, Random.IntRange(1, 3));
                    }

                    break;
            }

            if (Dungeon.visible[cell]) {
                if (Level.water[cell]) {

                    GameScene.ripple(cell);
                    Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );

                } else {
                    Sample.INSTANCE.play( Assets.SND_STEP );
                }
            }
        }
	}

	public boolean[] updateFieldOfView( Char c ) {

		int cx = c.pos % WIDTH;
		int cy = c.pos / WIDTH;

//		boolean sighted = c.buff( Blindness.class ) == null && c.isAlive();

//		if (sighted) {
			ShadowCaster.castShadow( cx, cy, fieldOfView, c.viewDistance(), c.flying );
//		} else {
//			Arrays.fill(fieldOfView, false);
//		}

//		int sense = 1;
//		if (c.isAlive()) {
//			for (Buff b : c.buffs( MindVision.class )) {
//				sense = Math.max( ((MindVision)b).distance, sense );
//			}
//		}

//		if (!sighted) {
//
//			int ax = Math.max( 0, cx - 1 );
//			int bx = Math.min( cx + 1, WIDTH - 1 );
//			int ay = Math.max( 0, cy - 1 );
//			int by = Math.min( cy + 1, HEIGHT - 1 );
//
//			int len = bx - ax + 1;
//			int pos = ax + ay * WIDTH;
//			for (int y = ay; y <= by; y++, pos+=WIDTH) {
//				Arrays.fill( fieldOfView, pos, pos + len, true );
//			}
//
//			for (int i=0; i < LENGTH; i++) {
//				fieldOfView[i] &= discoverable[i];
//			}
//		}

		if (c.isAlive()) {
			if (c.buff( MindVision.class ) != null) {
				for (Mob mob : mobs) {
                    if( !( mob instanceof Statue) ) {
                        int p = mob.pos;
                        fieldOfView[p] = true;
                        fieldOfView[p + 1] = true;
                        fieldOfView[p - 1] = true;
                        fieldOfView[p + WIDTH + 1] = true;
                        fieldOfView[p + WIDTH - 1] = true;
                        fieldOfView[p - WIDTH + 1] = true;
                        fieldOfView[p - WIDTH - 1] = true;
                        fieldOfView[p + WIDTH] = true;
                        fieldOfView[p - WIDTH] = true;
                    }
				}

                for (Heap heap : heaps.values()) {
                    if( heap.type == Heap.Type.CHEST_MIMIC) {
                        int p = heap.pos;
                        fieldOfView[p] = true;
                        fieldOfView[p + 1] = true;
                        fieldOfView[p - 1] = true;
                        fieldOfView[p + WIDTH + 1] = true;
                        fieldOfView[p + WIDTH - 1] = true;
                        fieldOfView[p - WIDTH + 1] = true;
                        fieldOfView[p - WIDTH - 1] = true;
                        fieldOfView[p + WIDTH] = true;
                        fieldOfView[p - WIDTH] = true;
                    }
				}
//			} else if( Dungeon.hero == c ) {
//
//                // FIXME
//
//                for (Mob mob : mobs) {
//                    if( mob.noticed ) {
//                        int p = mob.pos;
//
//                        for (int n : Level.NEIGHBOURS8) {
//
//                            fieldOfView[p + n] = true;
//
//                            Char ch = Actor.findChar( p + n );
//
//                            if( ch instanceof Mob && !((Mob)ch).noticed ) {
//                                ((Mob)ch).noticed = true;
//                            }
//                        }
//
//
//                        fieldOfView[mob.pos] = true;
//
//                    } else {
//
//                        for (int n : Level.NEIGHBOURS8) {
//                            Char ch = Actor.findChar( mob.pos + n );
//
//                            if( ch instanceof Mob && ((Mob)ch).noticed ) {
//                                mob.noticed = true;
//                            }
//                        }
//                    }
//                }
            }
//			else if (c.buff( MindVision.class ) != null) {
//				for (Heap heap : heaps.values()) {
//					int p = heap.pos;
//					fieldOfView[p] = true;
//					fieldOfView[p + 1] = true;
//					fieldOfView[p - 1] = true;
//					fieldOfView[p + WIDTH + 1] = true;
//					fieldOfView[p + WIDTH - 1] = true;
//					fieldOfView[p - WIDTH + 1] = true;
//					fieldOfView[p - WIDTH - 1] = true;
//					fieldOfView[p + WIDTH] = true;
//					fieldOfView[p - WIDTH] = true;
//				}
//			}
		}

		return fieldOfView;
	}

	public static int distance( int a, int b ) {
		int ax = a % WIDTH;
		int ay = a / WIDTH;
		int bx = b % WIDTH;
		int by = b / WIDTH;
		return Math.max( Math.abs( ax - bx ), Math.abs( ay - by ) );
	}

    public static boolean adjacent( int a, int b, boolean diagonal ) {
        int diff = Math.abs( a - b );
        return diff == 1 || diff == WIDTH || ( diagonal && diff == WIDTH + 1 ) || ( diagonal && diff == WIDTH - 1 );
    }

    public static boolean adjacent( int a, int b ) {
        return adjacent( a, b, true );
    }

    public String tileName( int tile ) {
        return Level.tileNames(tile);
    }

    public String tileDesc( int tile ) {
        return Level.tileDescs(tile);
    }

	public static String tileNames( int tile ) {

		if (tile >= Terrain.WATER_TILES) {
			return tileNames( Terrain.WATER );
		}

		if (tile != Terrain.CHASM && (Terrain.flags[tile] & Terrain.PIT) != 0) {
			return tileNames( Terrain.CHASM );
		}

		switch (tile) {
		case Terrain.CHASM:
			return "Chasm";
		case Terrain.EMPTY:
		case Terrain.EMPTY_SP:
		case Terrain.EMPTY_DECO:
		case Terrain.SECRET_TOXIC_TRAP:
		case Terrain.SECRET_FIRE_TRAP:
		case Terrain.SECRET_BOULDER_TRAP:
		case Terrain.SECRET_POISON_TRAP:
		case Terrain.SECRET_ALARM_TRAP:
		case Terrain.SECRET_LIGHTNING_TRAP:
			return "Floor";
		case Terrain.GRASS:
			return "Grass";
		case Terrain.WATER:
			return "Water";
		case Terrain.WALL:
		case Terrain.WALL_DECO:
		case Terrain.DOOR_ILLUSORY:
			return "Wall";
		case Terrain.DOOR_CLOSED:
			return "Closed door";
		case Terrain.OPEN_DOOR:
			return "Open door";
		case Terrain.ENTRANCE:
			return "Depth entrance";
		case Terrain.EXIT:
			return "Depth exit";
		case Terrain.EMBERS:
			return "Embers";
		case Terrain.LOCKED_DOOR:
			return "Locked door";
		case Terrain.PEDESTAL:
			return "Pedestal";
		case Terrain.BARRICADE:
			return "Barricade";
		case Terrain.HIGH_GRASS:
			return "High grass";
		case Terrain.LOCKED_EXIT:
			return "Locked depth exit";
		case Terrain.UNLOCKED_EXIT:
			return "Unlocked depth exit";
		case Terrain.WALL_SIGN:
		case Terrain.SIGN:
			return "Sign";
		case Terrain.WELL:
			return "Well";
		case Terrain.EMPTY_WELL:
			return "Empty well";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Statue";
		case Terrain.TOXIC_TRAP:
			return "Toxic gas trap";
		case Terrain.FIRE_TRAP:
			return "Fire trap";
		case Terrain.BOULDER_TRAP:
			return "Boulder trap";
		case Terrain.POISON_TRAP:
			return "Confusion gas trap";
		case Terrain.ALARM_TRAP:
			return "Alarm trap";
		case Terrain.LIGHTNING_TRAP:
			return "Lightning trap";
		case Terrain.BLADE_TRAP:
			return "Blade trap";
		case Terrain.SUMMONING_TRAP:
			return "Summoning trap";
		case Terrain.INACTIVE_TRAP:
			return "Triggered trap";
		case Terrain.BOOKSHELF:
		case Terrain.SHELF_EMPTY:
			return "Bookshelf";
		case Terrain.ALCHEMY:
			return "Alchemy pot";
		default:
			return "???";
		}
	}

	public static String tileDescs( int tile ) {

		switch (tile) {
        case Terrain.WALL:
        case Terrain.WALL_DECO:
        case Terrain.DOOR_ILLUSORY:
            return "Just a wall, nothing special. Mind that fighting in close spaces restricts ability to dodge.";
		case Terrain.CHASM:
			return "You can't see the bottom. Fighting near chasms limits movement, restricting ability to dodge (unless you are flying).";
		case Terrain.WATER:
			return "Step in the water to extinguish fire. However, don't forget that walking in the water is noisy and may attract unwanted attention!";
		case Terrain.ENTRANCE:
			return "Stairs lead up to the upper depth.";
		case Terrain.EXIT:
		case Terrain.UNLOCKED_EXIT:
			return "Stairs lead down to the lower depth.";
		case Terrain.EMBERS:
			return "Embers cover the floor.";
		case Terrain.HIGH_GRASS:
			return "Dense vegetation blocks the view and hushes your steps, making it easier to move undetected through it.";
		case Terrain.LOCKED_DOOR:
			return "This door is locked, you need a matching key to unlock it.";
		case Terrain.LOCKED_EXIT:
			return "Heavy bars block the stairs leading down.";
		case Terrain.BARRICADE:
			return "The wooden barricade is firmly set but has dried over the years. Might it burn?";
		case Terrain.SIGN:
			return "Somebody placed a sign here..";
		case Terrain.WALL_SIGN:
			return "There is something written on this wall.";
		case Terrain.TOXIC_TRAP:
		case Terrain.FIRE_TRAP:
		case Terrain.BOULDER_TRAP:
		case Terrain.POISON_TRAP:
		case Terrain.ALARM_TRAP:
		case Terrain.LIGHTNING_TRAP:
		case Terrain.BLADE_TRAP:
		case Terrain.SUMMONING_TRAP:
			return "Stepping onto a hidden pressure plate will activate the trap.";
		case Terrain.INACTIVE_TRAP:
			return "The trap has been triggered before and it's not dangerous anymore.";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Someone wanted to adorn this place, but failed, obviously.";
		case Terrain.ALCHEMY:
			return "Drop some herbs here to cook a potion.";
		case Terrain.EMPTY_WELL:
			return "The well has run dry.";
		default:
			if (tile >= Terrain.WATER_TILES) {
				return tileDescs( Terrain.WATER );
			}
			if ((Terrain.flags[tile] & Terrain.PIT) != 0) {
				return tileDescs( Terrain.CHASM );
			}
			return "";
		}
	}
}
