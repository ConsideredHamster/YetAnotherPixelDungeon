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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Golem;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarfMonk;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.quest.DwarfToken;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfAccuracy;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEvasion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfFortune;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfAwareness;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfProtection;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSatiety;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfWillpower;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.AmbitiousImpSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndImp;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AmbitiousImp extends NPC {

	{
		name = "ambitious imp";
		spriteClass = AmbitiousImpSprite.class;
	}
	
	private static final String TXT_GOLEMS1	=
		"Are you an adventurer? I love adventurers! You can always rely on them " +
		"if something needs to be killed. Am I right? For a bounty, of course ;)\n" +
		"In my case this is _golems_ who need to be killed. You see, I'm going to start a " +
		"little business here, but these stupid golems are bad for business! " +
		"It's very hard to negotiate with wandering lumps of granite, damn them! " +
		"So please, kill... let's say _6 of them_ and a reward is yours.";
	
	private static final String TXT_MONKS1	=
		"Are you an adventurer? I love adventurers! You can always rely on them " +
		"if something needs to be killed. Am I right? For a bounty, of course ;)\n" +
		"In my case this is _monks_ who need to be killed. You see, I'm going to start a " +
		"little business here, but these lunatics don't buy anything themselves and " +
		"will scare away other customers. " +
		"So please, kill... let's say _8 of them_ and a reward is yours.";
	
	private static final String TXT_GOLEMS2	=
		"How is your golem safari going?";	
	
	private static final String TXT_MONKS2	=
		"Oh, you are still alive! I knew that your kung-fu is stronger ;) " +
		"Just don't forget to grab these monks' tokens.";	
	
	private static final String TXT_CYA	= "See you, %s!";
	private static final String TXT_HEY	= "Psst, %s!";
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell( Utils.format( TXT_HEY, Dungeon.hero.className() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 1000;
//	}
	
//	@Override
//	public String defenseVerb() {
//		return "evaded";
//	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
	}
	
	@Override
    public boolean add( Buff buff ) {
        return false;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {

			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6))) {
				GameScene.show( new WndImp( this, tokens ) );
			} else {
				tell( Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1 );
			Quest.given = true;
			Quest.completed = false;
			
			Journal.add( Journal.Feature.IMP );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( 
			new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	public void flee() {
		
		yell(Utils.format(TXT_CYA, Dungeon.hero.className()));
		
		destroy();
//		sprite.die();

//        sprite.emitter().burst(Speck.factory(Speck.WOOL), 15);
        sprite.emitter().start(ElmoParticle.FACTORY, 0.03f, 60);
        sprite.killAndErase();
	}
	
	@Override
	public String description() {
		return 
			"Imps are lesser demons. They are notable neither for their strength nor their magic talent, but for their cruelty " +
            "and greed. However, some of them are actually quite smart and sociable. This one looks quite friendly, for example.";
	}
	
	public static class Quest {
		
		private static boolean alternative;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	= node.getBoolean( ALTERNATIVE );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			}
		}
		
		public static void spawn( Level level, Room room ) {

			if (!spawned && Dungeon.depth > 19 && Random.Int( 24 - Dungeon.depth ) == 0) {

				AmbitiousImp npc = new AmbitiousImp();

				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1 || level.heaps.get( npc.pos ) != null);

				level.mobs.add(npc);
				Actor.occupyCell( npc );
				
				spawned = true;	
				alternative = true;
				
				given = false;

                int random = Random.Int( 3 );

                if( Dungeon.hero.heroClass == HeroClass.WARRIOR ) {
                    reward = random == 2 ? new RingOfSatiety() : random == 1 ? new RingOfVitality() : new RingOfProtection();
                } else if( Dungeon.hero.heroClass == HeroClass.BRIGAND ) {
                    reward = random == 2 ? new RingOfFortune() : random == 1 ? new RingOfShadows() : new RingOfEvasion();
                } else if( Dungeon.hero.heroClass == HeroClass.SCHOLAR ) {
                    reward = random == 2 ? new RingOfKnowledge() : random == 1 ? new RingOfMysticism() : new RingOfWillpower();
                } else if( Dungeon.hero.heroClass == HeroClass.ACOLYTE ) {
                    reward = random == 2 ? new RingOfDurability() : random == 1 ? new RingOfAwareness() : new RingOfAccuracy();
                }

				reward.bonus = random + 1;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof DwarfMonk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Journal.remove(Journal.Feature.IMP);
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
