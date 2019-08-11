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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.levels.PrisonLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.RegularLevel;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmorCloth;
import com.consideredhamster.yetanotherpixeldungeon.items.quest.DriedRose;
import com.consideredhamster.yetanotherpixeldungeon.items.quest.RatSkull;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerLevel;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GhostSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndQuest;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndSadGhost;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost extends NPC {

	{
		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	private static final String TXT_ROSE1	=
		"Hello adventurer... Once I was like you - strong and confident... " +
		"And now I'm dead... But I can't leave this place... Not until I have my _dried rose_... " +
		"It's very important to me... Some monster stole it from my body...";
	
	private static final String TXT_ROSE2	=
		"Please... Help me... Find the rose...";
	
	private static final String TXT_RAT1	=
		"Hello adventurer... Once I was like you - strong and confident... " +
		"And now I'm dead... But I can't leave this place... Not until I have my revenge... " +
		"Slay the _fetid rat_, that has taken my life...";
		
	private static final String TXT_RAT2	=
		"Please... Help me... Slay the abomination...";

	
	public Ghost() {
		super();

		Sample.INSTANCE.load(Assets.SND_GHOST);
	}

    private void flee() {

        int newPos = Dungeon.level.randomRespawnCell( true, false );

        if (newPos != -1) {

            CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );

            Actor.moveToCell( this, newPos );
            pos = newPos;

            sprite.place(pos);
            sprite.visible = Dungeon.visible[pos];

        }
    }
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 0;
//	}
	
//	@Override
//	public String defenseVerb() {
//		return "evaded";
//	}


    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public float moveSpeed() {
		return 0.5f;
	}
	
	@Override
	public Char chooseEnemy() {
		return null;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
        flee();
    }
	
	@Override
	public boolean add( Buff buff ) {
        flee();
        return false;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			
			Item item = Quest.alternative ?
				Dungeon.hero.belongings.getItem( RatSkull.class ) :
				Dungeon.hero.belongings.getItem( DriedRose.class );	
			if (item != null) {
				GameScene.show( new WndSadGhost( this, item ) );
			} else {
				GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT2 : TXT_ROSE2 ) );

                flee();
			}
			
		} else {
			GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT1 : TXT_ROSE1 ) );
			Quest.given = true;
			
			Journal.add( Journal.Feature.GHOST );
		}
	}


	
	@Override
	public String description() {
		return 
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
//	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
//	static {
//		IMMUNITIES.add( Stun.class );
//		IMMUNITIES.add( Ensnared.class );
//	}
//
//	@Override
//	public HashSet<Class<?>> immunities() {
//		return IMMUNITIES;
//	}
	
	public static class Quest {

		private static boolean spawned;

		private static boolean alternative;

		private static boolean given;

		private static boolean processed;

		private static boolean completed;

		private static int depth;
		
		private static int left2kill;
		
		public static Weapon weapon;
		public static Armour armor;
		
		public static void reset() {
			spawned = false;
            completed = false;
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
        private static final String LEFT2KILL	= "left2kill";
        private static final String GIVEN		= "given";
        private static final String PROCESSED	= "processed";
        private static final String COMPLETED	= "completed";
        private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( ALTERNATIVE, alternative );
				if (!alternative) {
					node.put( LEFT2KILL, left2kill );
				}
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
                node.put( PROCESSED, processed );
                node.put( COMPLETED, completed );

				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	=  node.getBoolean( ALTERNATIVE );
				if (!alternative) {
					left2kill = node.getInt( LEFT2KILL );
				}
				
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
                processed	= node.getBoolean( PROCESSED );
                completed	= node.getBoolean( COMPLETED );
                given	= node.getBoolean( GIVEN );

				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armour)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( RegularLevel level ) {
			if (!spawned && Dungeon.depth > 7 && Random.Int( 12 - Dungeon.depth ) == 0 ) {

				Ghost ghost = new Ghost();
                ghost.pos = level.randomRespawnCell();

				if ( ghost.pos > -1 ){

                    level.mobs.add( ghost );
                    Actor.occupyCell( ghost );

                    spawned = true;
                    alternative = false;
                    if( !alternative ){
                        left2kill = 8;
                    }

                    completed = false;
                    given = false;
                    processed = false;
                    depth = Dungeon.depth;

                    do{
                        weapon = (Weapon) Generator.random( Generator.Category.WEAPON );
                    }
                    while( weapon instanceof ThrowingWeapon || weapon.lootChapter() < 2 || weapon.bonus < 1 );

                    do{
                        armor = (Armour) Generator.random( Generator.Category.ARMOR );
                    }
                    while( armor instanceof BodyArmorCloth || armor.lootChapter() < 2 || armor.bonus < 1 );

                    weapon.identify().repair();
                    armor.identify().repair();

                }
			}
		}

		public static void process( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				if (alternative) {
					
//					FetidRat rat = new FetidRat();
//					rat.pos = Dungeon.level.randomRespawnCell();
//					if (rat.pos != -1) {
//						GameScene.add( rat );
						processed = true;
//					}
//
				} else {
					
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;

            completed = true;
			
			Journal.remove( Journal.Feature.GHOST );
		}

        public static boolean isCompleted() {
            return completed;
        }
	}
	
//	public static class FetidRat extends Mob {
//
//		{
//			name = "fetid rat";
//			spriteClass = FetidRatSprite.class;
//
//			HP = HT = 14;
//			dexterity = 6;
//
//			EXP = 0;
//			maxLvl = 5;
//
//			state = WANDERING;
//
//            HP = HT = Random.NormalIntRange( HT, HT * 2 );
//		}
//
//		@Override
//		public int damageRoll() {
//			return Random.NormalIntRange( 0, 12 );
//		}
//
//		@Override
//		public int accuracy() {
//			return 16;
//		}
//
//		@Override
//		public int armorClass() {
//			return 2;
//		}
//
//		@Override
//		public int defenseProc( Char enemy, int damage ) {
//
//			GameScene.add( Blob.seed( pos, 50, CorrosiveGas.class ) );
//
//			return super.defenseProc(enemy, damage);
//		}
//
//		@Override
//		public void die( Object cause ) {
//			super.die( cause );
//
//			Dungeon.level.drop( new RatSkull(), pos ).sprite.drop();
//		}
//
//		@Override
//		public String description() {
//			return
//				"This marsupial rat is much larger, than a regular one. It is surrounded by a foul cloud.";
//		}
//
//		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
//		static {
//			IMMUNITIES.add( CorrosiveGas.class );
//		}
//
//		@Override
//		public HashSet<Class<?>> immunities() {
//			return IMMUNITIES;
//		}
//	}
}
