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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class BuffActive extends Buff {

    protected int duration;

    @Override
    public String status() {
        return duration > 0 ? Integer.toString( duration ) : null;
    }

    public Element buffType() {
        return null;
    }

    public int getDuration() {

        return duration;

    }

    public void add( float value ) {

        duration += value;

    }

	@Override
	public boolean act() {

        duration--;

        if( duration > 0 ) {
            spend( TICK );
        } else {
            detach();
        }

		return true;
	}

    public static<T extends BuffActive> T add( Char target, Class<T> buffClass, float duration ) {

        boolean newBuff = false;

        T buff = target.buff(buffClass);

        if (buff == null) {
            try{

                buff = buffClass.newInstance();
//                duration += buff.durationModifier();
                buff.delay( TICK );
                newBuff = true;

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        if( buff != null ){

            if( buff.buffType() != null ) {

                float resist = Element.Resist.getResistance( target, buff.buffType() );

                if( !Element.Resist.checkIfDefault( resist ) ) {

                    if ( Element.Resist.checkIfNegated( resist ) ) {

                        duration = 0;

                    } else if ( Element.Resist.checkIfPartial( resist ) ) {

                        duration = duration / 2 + Random.Int( (int)duration % 2 + 1 );

                    } else if ( Element.Resist.checkIfAmplified( resist ) ) {

                        duration *= 2;

                    }

                }
            }

            if( duration > 0 ) {

                buff.duration += duration;

                 if( !newBuff || buff.attachTo( target ) ) {
//                     buff.duration += duration;
                     BuffIndicator.refreshHero();
                 }
             }
        }

        return buff;
    }

    public static<T extends BuffActive> T addFromDamage( Char target, Class<T> buffClass, int value ) {

        return add( target, buffClass, (float)Math.ceil( value / Math.sqrt( target.totalHealthValue() ) ) );

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