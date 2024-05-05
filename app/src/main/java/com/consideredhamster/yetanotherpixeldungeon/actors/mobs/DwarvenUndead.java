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

  import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
  import com.consideredhamster.yetanotherpixeldungeon.Element;
  import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
  import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
  import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
  import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
  import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
  import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.UndeadSprite;
  import com.watabou.noosa.audio.Sample;
  import com.watabou.utils.Bundle;
  import com.watabou.utils.Random;

  public class DwarvenUndead extends MobPrecise {

	  public DwarvenKing king = null;
	  private static final String WELL = "well";
	  public int well = 0;

	  public DwarvenUndead() {

		  super( 5, 12, false );

		  name = "undead dwarf";
		  info = "Magical, Weakening attack";
		  spriteClass = UndeadSprite.class;

		  armorClass /= 2;

		  resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
		  resistances.put( Element.Acid.class, Element.Resist.PARTIAL );

		  resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
		  resistances.put( Element.Body.class, Element.Resist.IMMUNE );

	  }

	  public boolean isMagical() {
		  return true;
	  }

	  @Override
	  public void storeInBundle( Bundle bundle ) {
		  super.storeInBundle( bundle );
		  bundle.put( WELL, well );
	  }

	  @Override
	  public void restoreFromBundle( Bundle bundle ) {
		  super.restoreFromBundle( bundle );
		  well = bundle.getInt( WELL );
	  }

	  @Override
	  public int dexterity() {
		  return buff( Enraged.class ) == null ? super.dexterity() : 0;
	  }

	  @Override
	  public int armourAC() {
		  return buff( Enraged.class ) == null ? super.armourAC() : 0;
	  }

	  @Override
	  public boolean act() {

		  if ( king == null ) {
			  king = DwarvenKing.king();
		  }

		  if ( buff( Enraged.class ) != null && king != null && king.isAlive() && Level.adjacent( pos, king.pos ) ) {

			  king.sacrificeMinion( this );
			  return true;

		  }

		  return super.act();
	  }


	  @Override
	  protected boolean getCloser( int target ) {
		  return buff( Enraged.class ) != null && king != null && king.isAlive() ?
				  super.getCloser( king.pos ) :
				  super.getCloser( target );
	  }

	  @Override
	  protected boolean canAttack( Char enemy ) {
		  return super.canAttack( enemy ) && buff( Enraged.class ) == null;
	  }

	  @Override
	  public int attackProc( Char enemy, int damage, boolean blocked ) {

		  if ( Random.Int( 10 ) < 5 ) {
			  BuffActive.addFromDamage( enemy, Withered.class, damage );
		  }

		  return damage;
	  }

	  @Override
	  public void die( Object cause, Element dmg ) {

		  if ( well > 0 && king != null ) {
			  GameScene.add( Blob.seed( well, DwarvenKing.spawnDelay( king.breaks ), DwarvenKing.Spawner.class ) );
		  }

		  if ( Dungeon.visible[ pos ] ) {
			  Sample.INSTANCE.play( Assets.SND_BONES );
			  sprite.emitter().burst( Speck.factory( Speck.BONE ), 6 );
		  }

		  super.die( cause, dmg );
	  }

	  @Override
	  public String description() {
		  return
				  "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
						  "They appear as stocky skeletons with unusually tiny skulls.";
	  }

  }
