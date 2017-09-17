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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.sprites.PiranhaSprite;

public class Piranha extends MobEvasive {
	
	public Piranha() {

		super( Dungeon.depth + 1 );

        name = "giant piranha";
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        minDamage += tier;
        maxDamage += tier;

        HP = HT += Random.IntRange(2, 4);

//        EXP = 0;

        loot = new MysteryMeat();
        lootChance = 0.5f;

	}

//    @Override
//    public float attackDelay() {
//        return 0.5f;
//    }
	
	@Override
	protected boolean act() {
		if (!Level.water[pos]) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( Dungeon.depth, 5 + Dungeon.depth );
//	}
//
//	@Override
//	public int accuracy( Char target ) {
//		return 10 + Dungeon.depth * 3;
//	}
//
//	@Override
//	public int armorClass() {
//		return 0;
//	}

//    @Override
//    public float awareness(){
//        return 2.0f;
//    }
	
//	@Override
//	public void die( Object cause, DamageType dmg ) {
////		Dungeon.bonus.drop( new MysteryMeat(), pos ).sprite.drop();
//		super.die( cause, dmg );
//
//		Statistics.piranhasKilled++;
//		Badges.validatePiranhasKilled();
//	}
	
	@Override
	public boolean reset() {
        state = SLEEPING;
        return true;
	}

	@Override
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

    @Override
    protected int nextStepTo( Char enemy ) {
        return Dungeon.findPath( this, pos, enemy.pos,
                Level.water,
                Level.fieldOfView );
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && Random.Int( enemy.HT ) < damage ) {

            Buff.affect(this, Enraged.class, Random.IntRange(2, 3));

        }

        return damage;
    }

	@Override
	public String description() {
		return
			"These carnivorous fish are sometimes born in these underground pools. " +
			"Other times, they are bred specifically to protect flooded treasure vaults. " +
            "Regardless of origin, they all share the same ferocity and thirst for blood.";
	}

//    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
//    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

//    static {
//        IMMUNITIES.add(DamageType.Flame.class);
//    }

//    @Override
//    public HashSet<Class<? extends DamageType>> resistances() {
//        return RESISTANCES;
//    }
//
//    @Override
//    public HashSet<Class<? extends DamageType>> immunities() {
//        return IMMUNITIES;
//    }
}
