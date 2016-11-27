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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.WebParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.BoulderTrap;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.DM300Sprite;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM300 extends MobHealthy {

    protected int breaks = 0;

    public DM300() {

        super( 4, 20, true );

        // 450

        dexterity /= 2; // yes, we divide it again
        armorClass *= 2; // and yes, we multiply it back

        name = Dungeon.depth == Statistics.deepestFloor ? "DM-300" : "DM-400";
        spriteClass = DM300Sprite.class;

        loot = Gold.class;
        lootChance = 4f;
    }

    @Override
    public float awareness(){
        return 1.0f;
    }

    @Override
    public float moveSpeed() {
        return buff( Enraged.class ) != null ? 1.0f : 0.75f;
    }
	
	@Override
	public void move( int step ) {
		super.move( step );

        if( buff( Enraged.class ) != null ) {

            BoulderTrap.boulders( step + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ], damageRoll() / 2 );

            BoulderTrap.boulders( step + Level.NEIGHBOURS12[ Random.Int( Level.NEIGHBOURS12.length ) ], damageRoll() / 3 );

        } else if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += ( HT - HP ) / 5;
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n( "DM-300 repairs itself!" );
			}
		}



	}

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i("DM-300 is not enraged anymore.");
        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            breaks++;

            Buff.affect(this, Enraged.class, breaks * Random.Float(8.0f, 12.0f));

            if (Dungeon.visible[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                GLog.n( "DM-300 is enraged!" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        }

        return super.act();
    }
	
	@Override
	public void die( Object cause, DamageType dmg ) {
		
		super.die( cause, dmg );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		
		Badges.validateBossSlain();
		
		yell( "Mission failed. Shutting down." );
	}
	
	@Override
	public void notice() {
		super.notice();
        if( enemySeen ) {
            yell("Unauthorised personnel detected.");
        }
	}
	
	@Override
	public String description() {
		return
			"This machine was created by the Dwarves several centuries ago. Later, Dwarves started to replace machines with " +
			"golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-300 and similar " +
			"machines were typically used for construction and mining, and in some cases, for city defense.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Flame.class);
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Unholy.class);

        IMMUNITIES.add(DamageType.Mind.class);
        IMMUNITIES.add(DamageType.Body.class);
        IMMUNITIES.add(DamageType.Dispel.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }

    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
    }
}
