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

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Enraged extends Bonus {

    public static final int DURATION = 20;

    @Override
    public String toString() {
        return "Enraged";
    }

    @Override
    public String statusMessage() { return "enraged"; }

    @Override
    public String playerMessage() { return "You. Feel. REALLY. ANGRY!"; }

    @Override
    public int icon() {
        return BuffIndicator.ENRAGED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.ENRAGED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.ENRAGED );
    }

    @Override
    public String description() {
        return "Murderous rage boils in your veins. Your damage is increased, and every kill will " +
                "renew duration of this buff.";
    }

    public void reset( int dur ) {

        if( duration < dur ){
            duration = dur;
        }

        delay( TICK );
    }
}