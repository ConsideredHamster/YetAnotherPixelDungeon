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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.sprites.SwarmSprite;

import java.util.HashSet;

public class CarrionSwarm extends MobEvasive {

    public static boolean swarmer = true;

    public CarrionSwarm() {

        super( 5 );

        name = "carrion eater";
        spriteClass = SwarmSprite.class;

        flying = true;

	}

//    @Override
//    public float attackDelay() {
//        return 0.5f;
//    }

    @Override
    public String description() {
        return
                "The deadly swarm of flies buzzes angrily. These unclean foes " +
                "have uncanny sense of smell when it comes to anything edible.";
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked  ) {

        if( !blocked && damage > 0 ){

            Hunger hunger = enemy.buff( Hunger.class );

            if( hunger != null ){

                hunger.satisfy( Hunger.STARVING / 100f * ( -1 ) );

            }

        }

        return damage;
    }

//	@Override
//	public void die( Object cause ) {
//
//		super.die( cause );
//
//        GameScene.add(Blob.seed(pos, 10, CorrosiveGas.class));
//	}

//	private static final float SPLIT_DELAY	= 1f;

//	int generation	= 0;

//	private static final String GENERATION	= "generation";

//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//		bundle.put( GENERATION, generation );
//	}

//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle( bundle );
//		generation = bundle.getInt( GENERATION );
//	}

//	@Override
//	public int defenseProc( Char enemy, int damage ) {
//
//		if (HP >= damage + 2) {
//			ArrayList<Integer> candidates = new ArrayList<Integer>();
//			boolean[] passable = Level.passable;
//
//			int[] neighbours = {pos + 1, pos - 1, pos + Level.WIDTH, pos - Level.WIDTH};
//			for (int n : neighbours) {
//				if (passable[n] && Actor.findChar( n ) == null) {
//					candidates.add( n );
//				}
//			}
//
//			if (candidates.size() > 0) {
//
//				Swarm clone = split();
//				clone.HP = (HP - damage) / 2;
//				clone.pos = Random.element( candidates );
//				clone.state = clone.HUNTING;
//
//				if (Dungeon.bonus.map[clone.pos] == Terrain.DOOR_CLOSED) {
//					Door.enter( clone.pos );
//				}
//
//				GameScene.add( clone, SPLIT_DELAY );
//				Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );
//
//				HP -= clone.HP;
//			}
//		}
//
//		return damage;
//	}

//	@Override
//	public String defenseVerb() {
//		return "evaded";
//	}

//	private Swarm split() {
//		Swarm clone = new Swarm();
//		clone.generation = generation + 1;
//		if (buff( Burning.class ) != null) {
//			Buff.affect( clone, Burning.class ).reignite( clone );
//		}
//		if (buff( Poison.class ) != null) {
//			Buff.affect( clone, Poison.class ).set( 2 );
//		}
//		return clone;
//	}

//	@Override
//	protected void dropLoot() {
//		if (Random.Int( 5 * (generation + 1) ) == 0) {
//			Dungeon.bonus.drop( new PotionOfHealing(), pos ).sprite.drop();
//		}
//	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Body.class);
        RESISTANCES.add(DamageType.Acid.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
