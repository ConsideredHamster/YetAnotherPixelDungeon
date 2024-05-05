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
  package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

  import java.util.ArrayList;
  import java.util.HashMap;

  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
  import com.watabou.noosa.Camera;
  import com.watabou.noosa.audio.Sample;
  import com.watabou.noosa.tweeners.AlphaTweener;
  import com.watabou.utils.Random;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
  import com.consideredhamster.yetanotherpixeldungeon.Badges;
  import com.consideredhamster.yetanotherpixeldungeon.Element;
  import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
  import com.consideredhamster.yetanotherpixeldungeon.Statistics;
  import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
  import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
  import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.UnholyArmor;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BlobEmitter;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.DrainLife;
  import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
  import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
  import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
  import com.consideredhamster.yetanotherpixeldungeon.levels.CityBossLevel;
  import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
  import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.KingSprite;
  import com.watabou.utils.Bundle;
  import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

  public class DwarvenKing extends MobPrecise {
	
	  private final static int LINE_GREETINGS = 0;
	  private final static int LINE_AWAKENING = 1;
	  private final static int LINE_THREATENED = 2;
	  private final static int LINE_CHANNELING = 3;
	  private final static int LINE_ANNOYANCE = 4;
	  private final static int LINE_EMPOWERED = 5;
	  //    private final static int LINE_MOVEASIDE = 6;
	  private final static int LINE_NEARDEATH = 7;
	
	  private final static String TXT_ENRAGED = "Dwarven King is enraged!";
	  private final static String TXT_STOPPED = "Dwarven King stopped the ritual.";
	  private final static String TXT_CALMDWN = "Dwarven King is not enraged anymore.";
	  private final static String TXT_SUMMONS = "Dwarven King casts some powerful spell!";
	  private final static String TXT_CHANNEL = "Dwarven King starts some kind of ritual!";
	  
	  private static final float SPAWN_DELAY = 1f;
	  private static final float BASE_ENRAGE = 5f;
	  
	  //    private static final String PHASE	= "phase";
	  private static final String BREAKS = "breaks";
	  private static final String CONSUMED = "consumed";
	  
	  private static String[][] LINES = {
			
			  {
					  "How dare you!",
					  "Who dares to disturb my slumber?",
					  "Human? In MY throne room?",
			  },
			  {
					  "Arise, slaves!",
					  "I command you to fight!",
					  "Servants! It is time to feast!",
			  },
			  {
					  "You'll pay for that, maggot!",
					  "Your death will be VERY painful...",
					  "Time to teach you a lesson, mortal.",
					  "You are going to be a very powerful slave.",
			  },
			  {
					  "Come to me, my minions!",
					  "Behold my true power!",
					  "To me, my vassals!",
					  "Your king commands you!",
					  "Raise from your graves!",
			  },
			  {
					  "They are as useless in death as they were in life.",
					  "These sycophants are worthless pile of trash.",
					  "I always have to do everything by myself.",
			  },
			  {
					  "Time to remove you from the board, mortal.",
					  "You played this game for too long, mortal.",
					  "Do you not know your death when you see it? Die now!",
					  "Your screams will be a symphony for my ears.",
					  "You'll pay for everything you've done, human.",
			  },
			  {
					  "Move aside, worm!",
					  "Don't make me angry, little pest.",
					  "You will not stop me this way.",
			  },
			  {
					  "You cannot kill me, " + Dungeon.hero.heroClass.title() + "... I am... immortal... ",
					  "I will return, " + Dungeon.hero.heroClass.title() + "... I will... return...",
					  "No. NO! How it can be? Killed... by... a mortal...",
			  },
	  };
	
	  public DwarvenKing() {
		
		  super( 5, 25, true );
		
		  name = Dungeon.depth == Statistics.deepestFloor ? "King of Dwarves" : "undead King of Dwarves";
		  info = "Boss enemy!";
		  spriteClass = KingSprite.class;
		
		  loot = Gold.class;
		  lootChance = 4f;
		
		  resistances.put( Element.Body.class, Element.Resist.PARTIAL );
		  resistances.put( Element.Mind.class, Element.Resist.PARTIAL );
		  resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
		  resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
		
		  resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
		  resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
	  }
	
	  private static boolean spawnSkeleton( int pos ) {
		
		  if ( Actor.findChar( pos ) != null ) {
			  ArrayList<Integer> candidates = new ArrayList<Integer>();
			
			  for ( int n : Level.NEIGHBOURS8 ) {
				  int cell = pos + n;
				  if ( !Level.solid[ cell ] && Actor.findChar( cell ) == null ) {
					  candidates.add( cell );
				  }
			  }
			
			  if ( candidates.size() > 0 ) {
				  pos = candidates.get( Random.Int( candidates.size() ) );
			  } else {
				  return false;
			  }
		  }
		
		  DwarvenUndead clone = new DwarvenUndead();
		
		  clone.pos = pos;
		  clone.well = pos;
		
		  clone.king = king();
		  clone.state = clone.HUNTING;

		  clone.EXP = 0;

		  clone.minDamage /= 2;
		  clone.maxDamage /= 2;
		
		  if ( clone.king != null )
			  clone.HT = clone.HP = 15 + clone.king.breaks * 5;
		
		  Dungeon.level.press( clone.pos, clone );
		  Sample.INSTANCE.play( Assets.SND_CURSED );
		
		  GameScene.add( clone, SPAWN_DELAY );
		
		  if ( Dungeon.visible[ clone.pos ] ) {
			
			  clone.sprite.alpha( 0 );
			  clone.sprite.parent.add( new AlphaTweener( clone.sprite, 1, 0.5f ) );
			  clone.sprite.emitter().start( Speck.factory( Speck.RAISE_DEAD ), 0.01f, 15 );
			
		  }
		
		  clone.sprite.idle();
		
		  return true;
	  }
	
	  public int breaks = 0;
	  public int consumed = 0;
//    public boolean phase = false;
	
	  @Override
	  public void storeInBundle( Bundle bundle ) {
		  super.storeInBundle( bundle );
//        bundle.put( PHASE, phase );
		  bundle.put( BREAKS, breaks );
		  bundle.put( CONSUMED, consumed );
	  }

//    @Override
//    public int dexterity() {
//        return buff( UnholyArmor.class ) == null ? super.dexterity() : 0 ;
//    }

//    @Override
//    public int armourAC() {
//        return buff( UnholyArmor.class ) == null ? super.armourAC() : 0;
//    }

//    @Override
//    protected boolean getCloser( int target ) {
//        return phase ?
//                super.getCloser( CityBossLevel.pedestal() ) :
//                super.getCloser( target );
//    }
//
//    @Override
//    protected boolean canAttack( Char enemy ) {
//        return super.canAttack( enemy ) && ( !phase || ( buff( Enraged.class ) != null ) );
//    }
	
	  @Override
	  public void restoreFromBundle( Bundle bundle ) {
		  super.restoreFromBundle( bundle );
//        phase = bundle.getBoolean( PHASE );
		  breaks = bundle.getInt( BREAKS );
		  consumed = bundle.getInt( CONSUMED );
	  }
	
	  @Override
	  protected float healthValueModifier() {
		  return 0.25f;
	  }
	
	  @Override
	  public float awareness() {
		  return 2.0f;
	  }
	
	  @Override
	  public boolean act() {
		
		  int throne = Dungeon.level instanceof CityBossLevel ? CityBossLevel.pedestal() : 0;

//        if( Dungeon.level instanceof CityBossLevel ) {
//            GameScene.add(Blob.seed(CityBossLevel.throne( true ), 1, Fire.class));
//            GameScene.add(Blob.seed(CityBossLevel.throne( false ), 1, Fire.class));
//        }
		
		  if ( throne > 0 && Dungeon.hero.isAlive() ) {
			
			  if ( buff( UnholyArmor.class ) != null ) {
				
				  sprite.cast( throne, null );
				  spend( TICK );
				  return true;
				
			  } else if ( 4 * HP / HT < 4 - breaks ) {
				
				  if ( pos == throne ) {
					
					  sprite.cast( throne, null );
					  sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
					  Sample.INSTANCE.play( Assets.SND_CHALLENGE );
					
					  awakenWells();
					
					  if ( breaks > 0 ) {
						
						  Debuff.removeAll( this );
						  Buff.affect( this, UnholyArmor.class, 5 + breaks * 5 + Random.Int( 6 ) );
						
						  yell( LINE_CHANNELING );
						  GLog.w( TXT_CHANNEL );
						
						
					  } else {
						
						  yell( LINE_AWAKENING );
						  GLog.w( TXT_SUMMONS );
						
					  }
					  
					  breaks++;
					
				  } else {
					
					  yell( LINE_THREATENED );
					  Buff.detach( this, Enraged.class );
					  teleportTo( throne );
					
				  }
				
				  spend( TICK );
				  return true;
				
			  }
		  }
		
		  return super.act();
	  }
	
	  @Override
	  public boolean cast( Char enemy ) {
		
		  Blob blob = Dungeon.level.blobs.get( Spawner.class );
		
		  if ( blob != null ) {
			  for ( int well : CityBossLevel.wells() ) {
				  if ( blob.cur[ well ] > 1 ) {
					  GameScene.add( Blob.seed( well, -1, Spawner.class ) );
				  }
			  }
		  }
		
		  for ( Mob mob : Dungeon.level.mobs ) {
			  if ( mob instanceof DwarvenUndead) {
				  mob.target = pos;
				  BuffActive.add( mob, Enraged.class, TICK );
			  }
		  }
		
		  return true;
	  }
	
	  @Override
	  public void damage( int dmg, Object src, Element type ) {
		
		  if ( buff( Enraged.class ) != null ) {
			
			  dmg = dmg / 2 + Random.Int( 1 + dmg % 2 );
			
		  }
		
		  super.damage( dmg, src, type );
	  }
	
	  @Override
	  public HashMap<Class<? extends Element>, Float> resistances() {
		
		  HashMap<Class<? extends Element>, Float> result = new HashMap<>();
		  ;
		  result.putAll( super.resistances() );
		
		  if ( buff( UnholyArmor.class ) != null ) {
			  for ( Class<? extends Element> type : UnholyArmor.RESISTS ) {
				  result.put( type, Element.Resist.IMMUNE );
			  }
		  }
		
		  return result;
	  }
	
	  @Override
	  public int attackProc( Char enemy, int damage, boolean blocked ) {
		
		  if ( enemy != null && buff( Enraged.class ) != null ) {
			  Pushing.knockback( enemy, pos, 1, damage );
		  }
		
		  return damage;
	  }
	
	  @Override
	  public void remove( Buff buff ) {
		
		  if ( buff instanceof UnholyArmor ) {
			
			  int duration = consumed;
			  consumed = 0;
			
			  if ( duration > 0 ) {
				  BuffActive.add( this, Enraged.class, BASE_ENRAGE + TICK * duration );
				  yell( LINE_EMPOWERED );
				  GLog.w( TXT_ENRAGED );
				  spend( TICK );
			  } else {
				  new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true ).show( sprite, 2f );
				  yell( LINE_ANNOYANCE );
				  GLog.w( TXT_STOPPED );
				  spend( TICK );
			  }
			
		  } else if ( buff instanceof Enraged ) {
			  GLog.w( TXT_CALMDWN );
		  }
		
		  super.remove( buff );
	  }
	
	  @Override
	  public void die( Object cause, Element dmg ) {
		
		  yell( LINE_NEARDEATH );
		  Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		
		  Camera.main.shake( 3, 0.5f );
		  new Flare( 6, 48 ).color( SpellSprite.COLOUR_DARK, true ).show( sprite, 3f );
		
		  for ( Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone() ) {
			  if ( mob instanceof DwarvenUndead) {
				  mob.die( cause, null );
			  }
		  }
		
		  Blob blob = Dungeon.level.blobs.get( Spawner.class );
		
		  if ( blob != null ) {
			  blob.remove();
		  }
		
		  super.die( cause, dmg );
		
		  Badges.validateBossSlain();
		  GameScene.bossSlain();
		
		
	  }
	
	  @Override
	  public void notice() {
		  super.notice();
		
		  if ( enemySeen && HP == HT && breaks == 0 ) {
			  yell( LINE_GREETINGS );
		  }
	  }
	
	  @Override
	  public String description() {
		  return
				  "The last king of dwarves was known for a deep understanding of the processes of life " +
						  "and death. He had persuaded the members of his court to participate in a ritual that " +
						  "should have granted them eternal youth. In the end, he was the only one who got it, " +
						  "with an army of undead as a bonus.";
	  }
	
	  public void sacrificeMinion( Mob mob ) {
		
		  UnholyArmor buff = buff( UnholyArmor.class );
		
		  if ( buff != null ) {
			
			  heal( Random.IntRange( mob.HP / 3, mob.HP / 2 ) );
			  consumed += breaks;
			
			  if ( sprite.visible || mob.sprite.visible ) {
				
				  mob.sprite.showStatus( CharSprite.NEGATIVE, "sacrificed" );
				  mob.sprite.parent.add( new DrainLife( mob.pos, pos, null ) );
				  new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true ).show( mob.sprite, 2f );
				
			  }
		  }

		  mob.die( this );
	  }
	
	  private void awakenWells() {
		  int dormant = 3 - breaks;
		  ArrayList<Integer> wells = new ArrayList<>();
		  Blob blob = Dungeon.level.blobs.get( Spawner.class );
		
		  for ( int well : CityBossLevel.wells() ) {
			  if ( blob == null || blob.cur[ well ] <= 0 ) {
				  wells.add( well );
			  } else if ( blob.cur[ well ] > 0 ) {
				  GameScene.add( Blob.seed( well, spawnDelay( breaks ), Spawner.class ) );
			  }
		  }
		
		  while ( wells.size() > dormant ) {
			
			  Integer w = Random.element( wells );
			
			  GameScene.add( Blob.seed( w, spawnDelay( breaks ), Spawner.class ) );
			  wells.remove( w );
			
		  }
	  }
	
	  private void yell( int line ) {
		  yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
	  }
	
	  private void teleportTo( int newPos ) {
		
		  int oldPos = pos;
		  Char target = findChar( newPos );
		
		  Actor.freeCell( oldPos );
		
		  if ( target != null ) {
			
			  target.pos = newPos;
			  Actor.moveToCell( target, oldPos );
			
			  target.sprite.place( newPos );
			  target.sprite.visible = Dungeon.visible[ newPos ];
			
			  target.sprite.alpha( 0 );
			  target.sprite.parent.add( new AlphaTweener( target.sprite, 1, 0.4f ) );
			
		  }
		
		  pos = newPos;
		  Actor.occupyCell( this );
		
		  sprite.place( newPos );
		  sprite.visible = Dungeon.visible[ newPos ];
		
		  sprite.alpha( 0 );
		  sprite.parent.add( new AlphaTweener( sprite, 1, 0.4f ) );
		
		  CellEmitter.get( oldPos ).start( ElmoParticle.FACTORY, 0.03f, 2 );
		  CellEmitter.get( newPos ).start( ElmoParticle.FACTORY, 0.03f, 2 );
		
	  }

	  public static DwarvenKing king() {
		  for ( Mob mob : Dungeon.level.mobs ) {
			  if ( mob instanceof DwarvenKing ) {
				  return (DwarvenKing) mob;
			  }
		  }

		  return null;
	  }

	  public static int spawnDelay( int breaks ) {
		  return Random.IntRange( 10, 15 ) - Random.IntRange( breaks, breaks * 2 ); // 10-15 8-12 6-9 4-6
	  }
	
	  public static class Spawner extends Blob {
		
		  public Spawner() {
			  super();
			
			  name = "bone pit";
		  }
		
		  @Override
		  protected void evolve() {
			
			  int from = WIDTH + 1;
			  int to = Level.LENGTH - WIDTH - 1;
			
			  for ( int pos = from ; pos < to ; pos++ ) {
				
				  if ( cur[ pos ] > 0 ) {
					
					  cur[ pos ]--;
					
					  if ( cur[ pos ] <= 0 ) {
						
						  if ( !spawnSkeleton( pos ) ) {
							  cur[ pos ] = 1;
						  }
						  ;
					  }
				  }
				
				  volume += ( off[ pos ] = cur[ pos ] );
				
			  }
		  }
		
		  public void seed( int cell, int amount ) {
			  if ( cur[ cell ] == 0 ) {
				  volume += amount;
				  cur[ cell ] = amount;
			  }
		  }
		
		  @Override
		  public void use( BlobEmitter emitter ) {
			  super.use( emitter );
			  emitter.start( Speck.factory( Speck.RATTLE ), 0.1f, 0 );
		  }
		
		  @Override
		  public String tileDesc() {
			  return "Bones on the bottom of this well are moving. Creepy.";
		  }
	  }
  }
