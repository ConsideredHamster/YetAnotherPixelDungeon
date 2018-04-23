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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Shocked extends Debuff {

    private static final String TXT_DISARMED = "Sudden shock have made you drop your %s on the ground!";

    @Override
    public Element buffType() {
        return Element.SHOCK;
    }

    @Override
    public String toString() {
        return "Electrified";
    }

    @Override
    public String statusMessage() { return "electrified"; }

    @Override
    public String playerMessage() { return "You are electrified!"; }

    @Override
    public int icon() {
        return BuffIndicator.SHOCKED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.POISONED );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.POISONED );
//    }

    @Override
    public String description() {
        return "Lightning coarses through your body, waiting for a moment to be released - being " +
                "zapped again or stepping into water will discharge it, knocking your weapons out " +
                "of your hands. Also, your wands are way less reliable because of the static charge.";
    }

    @Override
    public boolean act(){

        if( target.isAlive() && !target.flying && Level.water[ target.pos ] ){
            discharge();
        }

        return super.act();
    }

    public void discharge() {

        target.damage(
                Random.IntRange( duration, duration * (int)Math.sqrt( target.totalHealthValue() ) ),
                this, Element.SHOCK_PERIODIC
        );

//        target.sprite.showStatus( CharSprite.NEGATIVE, "ZAP!");

        if( target instanceof Hero ) {

            Camera.main.shake( 2, 0.3f );

            Hero hero = (Hero)target;
            EquipableItem weapon = Random.oneOf( hero.belongings.weap1, hero.belongings.weap2 );

            if( weapon != null && weapon.disarmable() ) {

                GLog.w(TXT_DISARMED, weapon.name());
                weapon.doDrop(hero);

            }

        } else {

            target.delay( Random.IntRange( 1, 2 ) );

        }

        if (target.sprite.visible) {
            target.sprite.centerEmitter().burst( SparkParticle.FACTORY, (int)Math.ceil( duration ) + 1 );
        }

        detach();
    };

}
