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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.WraithSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Wraith extends MobRanged {

	private static final float SPAWN_DELAY	= 2.0f;

	private static final float BLINK_CHANCE	= 0.125f;

    public Wraith() {
        this( Dungeon.depth );;
    }

    public Wraith( int depth ) {

        super( depth / 6 + 1, depth + 1, false );

        name = "wraith";
        spriteClass = WraithSprite.class;

        minDamage += tier;
        maxDamage += tier;

        minDamage /= 2;
        maxDamage /= 2;

        HP = HT /= 2;

        flying = true;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Physical.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean ignoresAC() {
        return true;
    }

    @Override
    public int armorClass() {
        return 0;
    }

    private void blink() {

        int newPos = pos;

        do {

            newPos = Random.Int( Level.LENGTH );

        } while ( Level.solid[newPos] || !Level.fieldOfView[newPos] ||
                 Actor.findChar(newPos) != null && pos != newPos ||
                Ballistica.cast(pos, newPos, false, false) != newPos );

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

//	private static final String DELAY = "delay";
//
//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//		bundle.put(DELAY, timeToJump);
//	}
//
//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle(bundle);
//        timeToJump = bundle.getInt( DELAY );
//	}

	@Override
	public String description() {
		return
			"A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. Being " +
            "an ethereal entity it bypasses any armor with its attacks while being partially " +
            "immune to conventional weapons itself.";
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

            enemy.damage( damageRoll(), this, Element.UNHOLY );

        } else {

            enemy.missed();

        }

        return true;
    }

    protected boolean doAttack( Char enemy ) {

        if ( !rooted && Random.Float() < BLINK_CHANCE ) {

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
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( distance( enemy ) <= 1 && isAlive() ) {

            int healed = damage / 2;

            float resist = Element.Resist.getResistance( enemy, Element.BODY );

            if( !Element.Resist.checkIfDefault( resist ) ) {

                if ( Element.Resist.checkIfNegated( resist ) ) {

                    healed = 0;

                } else if ( Element.Resist.checkIfPartial( resist ) ) {

                    healed = healed / 2 + Random.Int( (int)healed % 2 + 1 );

                } else if ( Element.Resist.checkIfAmplified( resist ) ) {

                    healed *= 2;

                }

            }

            if (healed > 0) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }

        return damage;
    }

    @Override
    public boolean reset() {
        state = HUNTING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

    public static ArrayList<Wraith> spawnAround( int pos, int amount ) {
        return spawnAround( Dungeon.depth, pos, amount );
    }

    public static ArrayList<Wraith> spawnAround( int depth, int pos, int amount ) {

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
                    wraiths.add( spawnAt( depth, candidates.get(o) ) );
                    candidates.remove( o );
                } else {
                    break;
                }
            }
        }

        return wraiths;
    }

    public static Wraith spawnAt( int pos ) {
        return spawnAt( Dungeon.depth, pos );
    }

    public static Wraith spawnAt( int depth, int pos ) {

        if (!Level.solid[pos] && Actor.findChar( pos ) == null) {

            Wraith w = new Wraith( depth );

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
