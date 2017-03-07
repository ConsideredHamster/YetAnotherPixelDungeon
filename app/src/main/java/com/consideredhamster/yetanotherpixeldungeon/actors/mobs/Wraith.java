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

import java.util.ArrayList;
import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Wraith extends MobRanged {

	private static final float SPAWN_DELAY	= 2.0f;

    private int timeToJump;

    public Wraith() {

        super( Dungeon.depth + 1 );

        name = "wraith";
        spriteClass = WraithSprite.class;

        minDamage += tier;
        maxDamage += tier;

        HP = HT += Random.IntRange( 3, 6 );

//        EXP = 0;

        timeToJump = jumpDelay();
        flying = true;
    }

    @Override
    public int damageRoll() {
        return super.damageRoll() / 2;
    }

    @Override
    public boolean ignoresAC() {
        return true;
    }

    private int jumpDelay() {

        return viewDistance();

    }

    private void blink() {

        timeToJump = jumpDelay();

        int newPos = pos;

        if( buff(Blindness.class) == null ) {
            do {

                newPos = Random.Int( Level.LENGTH );

            } while ( Level.solid[newPos] || !Level.fieldOfView[newPos] ||
                     Actor.findChar(newPos) != null && pos != newPos ||
                    Ballistica.cast(pos, newPos, false, false) != newPos );
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
        }

        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
        }

        ((WraithSprite)sprite).blink(pos, newPos);

        move( newPos );

        spend( 1 / moveSpeed() );

    }

	private static final String DELAY = "delay";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(DELAY, timeToJump);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
        timeToJump = bundle.getInt( DELAY );
	}

    @Override
    public int armorClass() {
        return 0;
    }

	@Override
	public String description() {
		return
			"A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. " +
			"Being an ethereal entity, it can teleport at will to shower you with a bolts " +
            "of unholy energy. The touch of a wraith drains life from its victims, bypassing " +
            "any armor and restoring it's own foul strength.";
	}
	


    @Override
    protected boolean canAttack( Char enemy ) {
        return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.shadow(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        if (hit( this, enemy, true, true )) {

            enemy.damage( damageRoll(), this, DamageType.UNHOLY );

        } else {

            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );

        }

        return true;
    }

    protected boolean doAttack( Char enemy ) {

        timeToJump--;

        if ( !rooted && timeToJump < 0 ) {

            blink();
            return true;

        } else {

            return super.doAttack( enemy );

        }
    }

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
                && Level.distance( pos, Dungeon.hero.pos ) <= 2
                && detected( Dungeon.hero ) && detected( Dungeon.hero )
                ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if ( distance( enemy ) <= 1 && !enemy.isMagical() && isAlive() ) {
            int reg = Math.min( Random.Int( damage + 1 ), HT - HP );

            if (reg > 0) {
                HP += reg;

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                }
            }
        }
        return super.attackProc( enemy, damage );
    }

    @Override
    public boolean reset() {
        state = HUNTING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

//    @Override
//    public void call() {
//        next();
//    }

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Unholy.class);

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

    public static ArrayList<Wraith> spawnAround( int pos, int amount ) {

//		for (int n : Level.NEIGHBOURS8) {
//			int cell = pos + n;
//			if (Level.passable[cell] && Actor.findChar( cell ) == null) {
//				spawnAt( cell );
//			}
//		}

        ArrayList<Wraith> wraiths = new ArrayList<>();

        if( amount > 0 ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            for (int i = 0; i < amount; i++) {
                if (candidates.size() > 0) {
                    int o = Random.Int( candidates.size() );
                    wraiths.add( spawnAt( candidates.get(o) ) );
                    candidates.remove( o );
                } else {
                    break;
                }
            }
        }

        return wraiths;
    }

    public static Wraith spawnAt( int pos ) {

        if (!Level.solid[pos] && Actor.findChar( pos ) == null) {

            Wraith w = new Wraith();
//			w.adjustStats( Dungeon.depth );
            w.pos = pos;
            w.special = true;
            w.enemySeen = true;
            w.state = w.HUNTING;
            GameScene.add( w, SPAWN_DELAY );

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );

            return w;
        } else {
            return null;
        }
    }
}
