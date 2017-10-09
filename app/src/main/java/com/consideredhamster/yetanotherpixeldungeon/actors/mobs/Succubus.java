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

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfBlink;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.SuccubusSprite;

public class Succubus extends MobPrecise {
	
	private static final int BLINK_DELAY = 6;
	
	private int delay = 0;

    public Succubus() {

        super( 18 );

        name = "succubus";
        spriteClass = SuccubusSprite.class;

        armorClass /= 3;

	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return ( super.canAttack( enemy ) ||
            Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos
            && !enemy.isCharmedBy( this ) ) && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.purpleLight(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        onCastComplete();

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        if ( hit( this, enemy, true, true ) ) {

            Charm buff = Buff.affect(enemy, Charm.class, (float)Random.IntRange( 3, 6 ));

            if( buff != null ) {
                buff.object = this.id();
                enemy.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
            }

        } else {

            enemy.missed();

        }

        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() && !enemy.isMagical() ) {

            int reg = Math.min(Random.Int(damage + 1), HT - HP);

            if (reg > 0) {
                HP += reg;

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                }
            }
        }

        return damage;
    }

//    public void onZapComplete() {
//        zap();
//        next();
//    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (delay <= 0 && enemySeen && enemy != null && Level.fieldOfView[target]
            && Level.distance( pos, target ) > 1 && enemy.isCharmedBy( this )
            && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos ) {

			blink( target );
			spend( -2 / moveSpeed() );
			return true;

		} else {

			delay--;
			return super.getCloser( target );

		}
	}
	
	private void blink( int target ) {
		
		int cell = Ballistica.cast( pos, target, false, true );
		
		if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		WandOfBlink.appear( this, cell );
		
		delay = BLINK_DELAY;
	}

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public String description() {
		return
			"The succubi are demons that look like seductive (in a slightly gothic way) girls. Demonic charms allow " +
			"them to mesmerize mortals, making them unable to inflict any direct harm against their tormentor and " +
            "leaving them vulnerable to succubus' life-draining touch.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Mind.class);
        RESISTANCES.add(DamageType.Unholy.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
