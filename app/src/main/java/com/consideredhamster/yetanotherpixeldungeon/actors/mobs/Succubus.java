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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfPhaseWarp;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfLifeDrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SuccubusSprite;

import java.util.ArrayList;

public class Succubus extends MobPrecise {
	
	private static final int BLINK_DELAY = 16;

	private int delay = 0;

    public Succubus() {

        super( 18 );

        /*

            base maxHP  = 43
            armor class = 5

            damage roll = 6-21

            accuracy    = 36
            dexterity   = 30

            perception  = 125%
            stealth     = 125%

         */

        name = "succubus";
        info = "Magical, Life drain, Charm, Teleport";

        spriteClass = SuccubusSprite.class;

        armorClass /= 3;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);
        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return ( super.canAttack( enemy ) ||
            enemy.buff( Charmed.class ) == null ) &&
            Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
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

//        onCastComplete();

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        if ( hit( this, enemy, true, true ) ) {

            Charmed buff = BuffActive.addFromDamage( enemy, Charmed.class, damageRoll() );

            if( buff != null ) {
//                buff.object = this.id();
                enemy.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
            }

        } else {

            enemy.missed();

        }

        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() ) {

            int healed = Element.Resist.modifyValue( damage / 2, enemy, Element.BODY );

            if ( healed > 0 ) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }

        return damage;
    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (delay <= 0 && enemySeen && enemy != null && Level.fieldOfView[target]
            && Level.distance( pos, target ) > 1 && enemy.buff( Charmed.class ) != null
            && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos
            && Level.mob_passable[ Ballistica.trace[ Ballistica.distance - 1 ] ]  ) {

			blink( target );
			spend( -1 / moveSpeed() );
			return true;

		} else {

			delay--;
			return super.getCloser( target );

		}
	}

	private void blink( int target ) {

		int cell = Ballistica.cast( pos, target, false, true );

		if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 1];
		}

        ScrollOfPhaseWarp.appear( this, cell );

		delay = BLINK_DELAY;
	}
	
	@Override
	public String description() {
		return
			"The succubi are demons that look like seductive (in a slightly gothic way) girls. Demonic charms allow " +
			"them to mesmerize mortals, making them unable to inflict any direct harm against their tormentor and " +
            "leaving them vulnerable to succubus' life-draining touch.";
	}
}
