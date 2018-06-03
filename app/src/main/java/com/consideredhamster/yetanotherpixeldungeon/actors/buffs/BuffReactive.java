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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public abstract class BuffReactive extends Buff {

    protected int duration;

    public void reset( int value ) {
        duration = value;
    }

    public void check(){
        if( duration > 0 ) {
            duration--;
        } else {
            detach();
        }
    }

    public static void check( Char ch ){
        for( BuffReactive buff : (HashSet<BuffReactive>)ch.buffs( BuffReactive.class ).clone() ) {
            buff.check();
        }
    }

    @Override
    public boolean act() {
        spend( TICK );
        return true;
    }

    private static final String DURATION	= "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DURATION, duration );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        duration = bundle.getInt( DURATION );
    }
}
