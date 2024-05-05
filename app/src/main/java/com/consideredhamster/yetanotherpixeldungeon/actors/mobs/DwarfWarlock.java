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

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Thunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.WarlockSprite;
import com.watabou.utils.Random;

public class DwarfWarlock extends MobRanged {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public DwarfWarlock() {

        super( 15 );

        /*

            base maxHP  = 30
            armor class = 8

            damage roll = 7-14

            accuracy    = 35
            dexterity   = 20

            perception  = 140%
            stealth     = 100%

         */

		name = "dwarf warlock";
		info = "Lightning bolt";
		spriteClass = WarlockSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;

        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

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
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_BLUE, 10);
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
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}

    @Override
    protected void onRangedAttack( int cell ) {

        sprite.parent.add( new Lightning( pos, cell ) );
        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );

        onCastComplete();

        super.onRangedAttack(cell);

    }

    @Override
	public boolean cast( Char enemy ) {

        HashSet<Char> affected = Thunderstorm.spreadFrom( enemy.pos );

        if( affected != null && !affected.isEmpty() ) {
            for( Char ch : affected ) {

                int power = damageRoll() + ( ch == enemy ? damageRoll() : 0 ) ;

                ch.damage( power, this, Element.SHOCK );

                if( Dungeon.hero == ch ) {
                    Camera.main.shake( 1, 0.1f );
                }
            }
        }

        return true;
	}

	@Override
	public String description() {
		return
			"When dwarves' interests have shifted from engineering to arcane arts, " +
			"warlocks have come to power in the city. They started with elemental magic, " +
			"but soon switched to demonology and necromancy.";
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
