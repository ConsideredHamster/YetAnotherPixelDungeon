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

import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Tomahawks;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.sprites.BruteSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.MissileSprite;

public class GnollBrute extends MobPrecise {

//	private static final String TXT_ENRAGED = "%s becomes enraged!";

    public GnollBrute() {

        super( 10 );

		name = "gnoll brute";
		spriteClass = BruteSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;
	}

//	private boolean enraged = false;
	
//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle( bundle );
//		enraged = HP < HT / 3;
//	}
	
//	@Override
//	public int damageRoll() {
//		return super.damageRoll() * ( HT * 2 - HP ) / HT ;
//	}

//    @Override
//    public int guardEffectiveness() {
//
//        return super.shieldAC() * 3;
//
//    }

    @Override
    public int shieldAC() {
        return enemySeen && !stunned ? armourAC() * 2 / 3 : 0 ;
    }

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() / 2 : super.damageRoll();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance(pos, enemy.pos) <= 2
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Tomahawks(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack( cell );
    }
	
//	@Override
//	public void damage( int dmg, Object src ) {
//        super.damage(dmg, src);
//
//        if (buff(Regeneration.class) == null) {
//            Buff.affect(this, Regeneration.class);
//        }

//		if ( isAlive() && !enraged && HP < HT / 3 ) {
//
//			enraged = true;
//			spend( TICK );
//			if (Dungeon.visible[pos]) {
//				GLog.w( TXT_ENRAGED, name );
//				sprite.showStatus( CharSprite.NEGATIVE, "enraged" );
//			}
//		}
//	}

//    @Override
//    public boolean act() {

//        if( enraged && HP >= HT ) {
//            enraged = false;
//        }

//        return super.act();
//    }
	
	@Override
	public String description() {
		return
			"Brutes are the largest, strongest and toughest of all gnolls. They are a clumsy, " +
            "but very ferocious fighters. Their blows grow stronger the closer they are to death.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    static {
        RESISTANCES.add(DamageType.Mind.class);
        RESISTANCES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
