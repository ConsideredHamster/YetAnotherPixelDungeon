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

package com.consideredhamster.yetanotherpixeldungeon.actors.buffs;

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class Mending extends Buff {
    private static final float STEP = 1f;

    private int duration = 0;

    private static final String DURATION = "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( DURATION, duration);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        duration = bundle.getInt( DURATION );
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            return true;
        } else {
            return false;
        }
    }

    public void setDuration( int value ) {

        duration = value;

    }

    @Override
    public boolean act() {

        if ( duration <= 0 ) {

            detach();

            spend(STEP);

        } else {

            int effect = 10;

            if( target instanceof Hero ) {

                Hero hero = ((Hero) target);

                effect += (hero.lvl - 1) + hero.strBonus;

                effect *= target.ringBuffs(RingOfVitality.Vitality.class );

            }

            int healthRestored = Math.min(

                ( effect / 10 + ( effect % 10 > Random.Int(10) ? 1 : 0 ) ),

                target.HT - target.HP

            ) ;

            if( healthRestored > 0 ) {

                // FIXME

                if( target instanceof Hero && ((Hero)target).restoreHealth && ( target.HP + healthRestored >= target.HT ) ) {
                    ((Hero)target).interrupt();
                }

                target.HP += healthRestored ;

                target.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));

//                if (duration % 2 == 0)
                target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            }

            duration--;

            spend( STEP );

        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.MENDING;
    }

    @Override
    public String toString() {
        return Utils.format("Mending (%d)", duration);
    }
}
