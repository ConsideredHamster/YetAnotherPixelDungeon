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

import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.WarlockSprite;

public class DwarfWarlock extends MobRanged {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public DwarfWarlock() {

        super( 15 );

		name = "dwarf warlock";
		spriteClass = WarlockSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;
	}

    @Override
    public boolean act() {

        if( !enemySeen )
            charged = false;

        return super.act();

    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !Level.adjacent( pos, enemy.pos ) && !charged ) {

            charged = true;

            if( Dungeon.visible[ pos ] ) {
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_BLUE, 20);
            }

            spend( attackDelay() );

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }

	@Override
	protected boolean canAttack( Char enemy ) {
        return !isCharmedBy( enemy ) && ( Level.adjacent( pos, enemy.pos )
            || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos );
	}

    @Override
    protected void onRangedAttack( int cell ) {

        int[] points = new int[2];

        points[0] = pos;
        points[1] = cell;

        sprite.parent.add( new Lightning( points, 2, null ) );

        onCastComplete();

        super.onRangedAttack(cell);

    }

    @Override
	public boolean cast( Char enemy ) {

        if ( hit( this, enemy, false, true ) ) {

				enemy.damage(damageRoll(), this, DamageType.SHOCK);

				if (enemy == Dungeon.hero) {

					Camera.main.shake( 2, 0.3f );

//					if (!enemy.isAlive()) {
//						Dungeon.fail( Utils.format( ResultDescriptions.MOB,
//							Utils.indefinite( name ), Dungeon.depth ) );
//						GLog.n( TXT_LIGHTNING_KILLED, name );
//					}
				}

                return true;

			} else {
                enemy.missed();
			}

        return false;
	}
	
//	public void onZapComplete() {
//		cast();
//		next();
//	}
	
//	@Override
//	public void call() {
//		next();
//	}
	
	@Override
	public String description() {
		return
			"When dwarves' interests have shifted from engineering to arcane arts, " +
			"warlocks have come to power in the city. They started with elemental magic, " +
			"but soon switched to demonology and necromancy.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Unholy.class);
        RESISTANCES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
    }
}
