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

import java.util.HashSet;

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.sprites.StatueSprite;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class Statue extends MobPrecise {

    public Statue() {

        super( Dungeon.depth + 1 );

        name = "animated statue";
        spriteClass = StatueSprite.class;

        minDamage += tier;
        maxDamage += tier;

        HP = HT += Random.IntRange(4, 8);

        armorClass += tier * 2;

//        EXP = 0;
        state = PASSIVE;

//        do {
//			weapon = (Weapon)Generator.random( Generator.Category.WEAPON );
//		} while (!(weapon instanceof MeleeWeapon) || weapon.bonus < 0);
//
//		weapon.identify();
//		weapon.enchant();
    }
	
//	private Weapon weapon;
//
//	public Statue() {
//		super();
//
//		do {
//			weapon = (Weapon)Generator.random( Generator.Category.WEAPON );
//		} while (!(weapon instanceof MeleeWeapon) || weapon.bonus < 0);
//
//		weapon.identify();
//		weapon.enchant();
//
//		HP = HT = 15 + Dungeon.depth * 5;
//		dexterity = 4 + Dungeon.depth;
//	}
	
//	private static final String WEAPON	= "weapon";

//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//		bundle.put( WEAPON, weapon );
//	}

//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle(bundle);
//		weapon = (Weapon)bundle.get( WEAPON );
//	}
	
//	@Override
//	protected boolean act() {
//		if (Dungeon.visible[pos]) {
//			Journal.add( Journal.Feature.STATUE );
//		}
//		return super.act();
//	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange(weapon.MIN, weapon.MAX);
//	}
//
//	@Override
//	public int accuracy( Char target ) {
//		return (int)(( super.accuracy( target ) ) * weapon.accuracyFactor( null ));
//	}
//
//	@Override
//	public float attackDelay() {
//		return weapon.speedFactor( null );
//	}
//
//	@Override
//	public int armorClass() {
//		return Dungeon.depth;
//	}

//    @Override
//    public float awareness(){
//        return 0.5f;
//    }
	
	@Override
	public void damage( int dmg, Object src, DamageType type ) {

		if (state == PASSIVE) {
            notice();
			state = HUNTING;
		}
		
		super.damage( dmg, src, type );
	}
	
//	@Override
//	public int attackProc( Char enemy, int damage ) {
//		weapon.proc( this, enemy, damage );
//		return damage;
//	}
	
	@Override
	public void beckon( int cell ) {
//        if (state == PASSIVE) {
//            state = HUNTING;
//        }
        // do nothing
	}


    @Override
    protected boolean act() {

        if( state == PASSIVE ) {
            if ( enemy != null && Level.adjacent( pos, enemy.pos ) && enemy.invisible <= 0) {
                activate();
                return true;
            }
        }

        return super.act();
    }


    public void activate() {

        state = HUNTING;
        enemySeen = true;

        GLog.w( "The statue activates!" );

        spend( TICK );
    }


//	@Override
//	public void die( Object cause ) {
//		Dungeon.bonus.drop( weapon, pos ).sprite.drop();
//		super.die( cause );
//	}
	
//	@Override
//	public void destroy() {
//		Journal.remove( Journal.Feature.STATUE );
//		super.destroy();
//	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return
			"You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
            "Usually passive, these stony juggernauts are almost unstoppable once provoked, being very resistant to both " +
            "physical and magical damage. Besides being extremely reliable guardians, these automatons also may serve as a " +
            "pretty cool garden decorations.";
//			"You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
//			"While the statue itself is made of stone, the _" + weapon.name() + "_, it's wielding, looks real.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Flame.class);
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Shock.class);
        RESISTANCES.add(DamageType.Acid.class);
        RESISTANCES.add(DamageType.Energy.class);
        RESISTANCES.add(DamageType.Unholy.class);
        RESISTANCES.add(DamageType.Dispel.class);

        IMMUNITIES.add(DamageType.Mind.class);
        IMMUNITIES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }
}
