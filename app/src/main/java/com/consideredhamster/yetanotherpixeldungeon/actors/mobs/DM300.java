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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.DM300Sprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.Camera;
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

        resistances.put(Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put(Element.Body.class, Element.Resist.IMMUNE );
        resistances.put(Element.Dispel.class, Element.Resist.IMMUNE );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
    }

    @Override
    public boolean isMagical() {
        return false;
    }

    @Override
    public float awareness(){
        return 1.0f;
    }

    @Override
    public float moveSpeed() {
        return buff( Enraged.class ) != null ? 1.0f : 0.75f + breaks * 0.05f;
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if( buff( Enraged.class ) != null ) {
            dmg /= 2;
        }

        super.damage( dmg, src, type );
    }
	
	@Override
	public void move( int step ) {
		super.move( step );

        if( buff( Enraged.class ) != null ) {

            dropBoulders( step + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ], damageRoll() / 2 );

            dropBoulders( step + Level.NEIGHBOURS12[ Random.Int( Level.NEIGHBOURS12.length ) ], damageRoll() / 3 );

            Camera.main.shake( 2, 0.1f );

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

            BuffActive.add(this, Enraged.class, breaks * Random.Float(8.0f, 12.0f));

            if (Dungeon.visible[pos]) {
//                sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                GLog.n( "DM-300 is enraged!" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        }

        return super.act();
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

        yell( "Mission failed. Shutting down." );

        super.die( cause, dmg );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        Badges.validateBossSlain();

	}

	@Override
	public void notice() {
		super.notice();
        if( enemySeen && HP == HT && breaks == 0 ) {
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

    public void dropBoulders( int pos, int power ) {

        if( pos < 0 || pos > Level.LENGTH )
            return;

        if( Level.solid[pos] )
            return;

        Char ch = Actor.findChar(pos);
        if (ch != null) {

            int dmg = Char.absorb( Random.IntRange( power / 2 , power ), ch.armorClass() );
//                    int dmg = Math.max(0, Random.IntRange(Dungeon.depth, Dungeon.depth + 10) - Random.NormalIntRange(0, ch.armorClass()));

            ch.damage(dmg, this, Element.PHYSICAL);

            if ( ch.isAlive() ) {
                BuffActive.addFromDamage(ch, Vertigo.class, dmg);
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.shatter();
        }

        if (Dungeon.visible[pos]) {

            CellEmitter.get(pos).start( Speck.factory(Speck.ROCK), 0.1f, 4 );

        }
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
