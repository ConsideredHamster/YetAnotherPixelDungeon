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

package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Mending extends Bonus {

    @Override
    public String toString() {
        return "Mending";
    }

    @Override
    public String statusMessage() { return "mending"; }

    @Override
    public int icon() {
        return BuffIndicator.MENDING;
    }

    @Override
    public void applyVisual(){
        target.sprite.add( CharSprite.State.MENDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.MENDING );
    }

    @Override
    public String description() {
        return "Warm, tingly sensation flows under your skin, and you can feel your wounds closing " +
                "at an accelerated rate.";
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            Buff.detach( target, Poisoned.class );
            Buff.detach( target, Crippled.class );
            Buff.detach( target, Withered.class );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean act() {

        int effect = 10;

        if( target instanceof Hero ) {

            Hero hero = ((Hero) target);

            effect += (hero.lvl - 1) + hero.strBonus;

        }

        int healthRestored = ( effect / 10 + ( effect % 10 > Random.Int(10) ? 1 : 0 ) );

        target.heal( healthRestored > 0 ? healthRestored : 1 );

        return super.act();
    }
}
