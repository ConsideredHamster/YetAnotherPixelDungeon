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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Shurikens;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.TenguSprite;
import com.watabou.utils.Random;

public class Tengu extends MobRanged {

	private static final int JUMP_DELAY = 8;

    private int timeToJump = 0;
    protected int breaks = 0;

    public Tengu() {

        super( 3, 15, true );

        name = Dungeon.depth == Statistics.deepestFloor ? "Tengu" : "memory of Tengu";
        spriteClass = TenguSprite.class;

        loot = Gold.class;
        lootChance = 4f;
    }

    @Override
    public float attackDelay() {
        return buff( Enraged.class ) == null ? 1.0f : 0.5f ;
    }

    @Override
    public int damageRoll() {
        return buff( Enraged.class ) == null ? super.damageRoll() : super.damageRoll() / 2 ;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Shurikens(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack(cell);
    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (!rooted && Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
		timeToJump++;
		if ( !rooted && timeToJump >= JUMP_DELAY ) {
			jump();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

    @Override
    public void damage( int dmg, Object src, DamageType type ) {

        if (HP <= 0) {
            return;
        }

        timeToJump++;

        super.damage( dmg, src, type );
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i( "Tengu is not enraged anymore." );
        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            breaks++;

            Buff.affect(this, Enraged.class, breaks * Random.Float(2.5f, 5.0f));

            if (Dungeon.visible[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                GLog.n( "Tengu is enraged!" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        } else if( buff( Enraged.class ) != null ) {

            timeToJump++;

        }

        return super.act();
    }
	
	private void jump() {
		timeToJump = 0;

        if( buff(Blindness.class) == null ) {
            for (int i = 0; i < 4; i++) {
                int trapPos;
                do {
                    trapPos = Random.Int(Level.LENGTH);
                } while (!Level.fieldOfView[trapPos] || !Level.passable[trapPos] || Actor.findChar( trapPos ) != null);

                if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
                    Level.set(trapPos, Terrain.BLADE_TRAP);
                    GameScene.updateMap(trapPos);
                    ScrollOfClairvoyance.discover(trapPos);
                }
            }
        }
		
		int newPos;

		do {
			newPos = Dungeon.level.randomRespawnCell( false, true );
		} while (Level.adjacent( pos, newPos ) ||
			(enemy != null && Level.adjacent( newPos, enemy.pos )));
		
		sprite.move( pos, newPos );
		move( newPos );
		
		if (Dungeon.visible[newPos]) {
			CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
		}
		
		spend( 1 / moveSpeed() );
	}
	
	@Override
	public void notice() {
		super.notice();
        if( enemySeen ) {
            yell( "Gotcha, " + Dungeon.hero.heroClass.title() + "!" );
        }
	}



    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public void die( Object cause, DamageType dmg ) {

//		Badges.Badge badgeToCheck = null;
//		switch (Dungeon.hero.heroClass) {
//		case WARRIOR:
//			badgeToCheck = Badge.MASTERY_WARRIOR;
//			break;
//		case SCHOLAR:
//			badgeToCheck = Badge.MASTERY_SCHOLAR;
//			break;
//		case BRIGAND:
//			badgeToCheck = Badge.MASTERY_BRIGAND;
//			break;
//		case ACOLYTE:
//			badgeToCheck = Badge.MASTERY_ACOLYTE;
//			break;
//		}
//		if (!Badges.isUnlocked( badgeToCheck )) {
//			Dungeon.bonus.drop( new TomeOfMastery(), pos ).sprite.drop();
//		}

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
        super.die( cause, dmg );

        Badges.validateBossSlain();

        yell( "Free at last..." );
    }
	
	@Override
	public String description() {
		return
			"Tengu are members of the ancient assassins clan, which is also called Tengu. " +
			"These assassins are noted for extensive use of shurikens and traps.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
//        RESISTANCES.add(DamageType.Unholy.class);
        RESISTANCES.add(DamageType.Body.class);

        IMMUNITIES.add(DamageType.Mind.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }

    private static final String TIME_TO_JUMP	= "timeToJump";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( TIME_TO_JUMP, timeToJump );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        timeToJump = bundle.getInt( TIME_TO_JUMP );
    }
}
