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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.FiendSprite;
import com.watabou.utils.Random;

public class Fiend extends MobRanged {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public Fiend() {

        super( 19 );

        /*

            base maxHP  = 37
            armor class = 10

            damage roll = 9-17

            accuracy    = 43
            dexterity   = 25

            perception  = 150%
            stealth     = 100%

         */

		name = "fiend";
		spriteClass = FiendSprite.class;

//        resistances.put( Element.Mind.class, Element.Resist.PARTIAL );
        resistances.put( Element.Body.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
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

            sprite.centerEmitter().burst( EnergyParticle.FACTORY_BLACK, 25 );

            spend(attackDelay());

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }

//    @Override
//    public DamageType damageType() {
//        return DamageType.UNHOLY;
//    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Tormented.class, damageRoll() * 2 );
        }

        return damage;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
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

    @Override
    public void die( Object cause, Element dmg ) {

        GameScene.add(Blob.seed(pos, 100, Miasma.class));

        super.die( cause, dmg );
    }

    @Override
    public String description() {
        return
                "Some demons seem to transcend their flesh and wear pure darkness as their form. Shadowy " +
                        "and menacing, these unholy abominations are born of malicious intent and are nothing " +
                        "more than incarnations of distilled evil, revelling only in death and pain.";
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
