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
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public abstract class Buff extends Actor {

	public Char target;

    public String description() {
        return "";
    }

    public String playerMessage() { return null; }
    public String messagePrefix() { return null; }

    public String statusMessage() { return null; }
    public int statusColor() { return CharSprite.DEFAULT; }

    public String status() { return null; }

    public void applyVisual() {}
    public void removeVisual() {}

    // FIXME
    public boolean attachOnLoad( Char target ) {
        this.target = target;
        return target.add(this);
    }

    public boolean attachTo( Char target ) {

        this.target = target;

        if( target.add(this) ){

            if( statusMessage() != null && target.sprite != null ) {
                target.sprite.showStatus( statusColor(), statusMessage() );
            }

            if( playerMessage() != null && Dungeon.hero == target ) {
                GLog.i( messagePrefix() + playerMessage() );
            }

            applyVisual();

            return true;

        } else {

            return false;

        }
    }
	
	public void detach() {
        target.remove(this);
        removeVisual();
    }
	
	@Override
	public boolean act() {
		deactivate();
		return true;
	}

    @Override
    public int actingPriority(){
        return 1;
    }
	
	public int icon() {
		return BuffIndicator.NONE;
	}

//    public float duration() {
//        return cooldown();
//    }

    public boolean awakensMobs() { return true; }

	public static<T extends Buff> T append( Char target, Class<T> buffClass ) {
		try {

			T buff = buffClass.newInstance();

            return buff.attachTo( target ) ? buff : null;

		} catch (Exception e) {
			return null;
		}
	}
	
	public static<T extends BuffPassive> T append( Char target, Class<T> buffClass, float duration ) {
		T buff = append(target, buffClass);

        if (buff != null) {
            buff.spend(duration);
        }

		return buff;
	}
	
	public static<T extends Buff> T affect( Char target, Class<T> buffClass ) {
		T buff = target.buff(buffClass);
		if (buff != null) {
			return buff;
		} else {
			return append( target, buffClass );
		}
	}
	
	public static<T extends BuffPassive> T affect( Char target, Class<T> buffClass, float duration ) {
		T buff = affect(target, buffClass);

        if (buff != null) {
            buff.spend(duration);
        }

		return buff;
	}

    public static <T extends BuffPassive> T prolong( Char target, Class<T> buffClass, float duration ) {
        T buff = affect( target, buffClass );

        if (buff != null) {
            buff.postpone(duration);
        }

        return buff;
    }

    public static<T extends BuffPassive> T shorten( Char target, Class<T> buffClass, float duration ) {
        T buff = affect( target, buffClass );

        if (buff != null) {
            buff.spend(duration * (-1));
        }

        return buff;
    }

    public static void detach( Buff buff ) {
        if (buff != null) {
            buff.detach();
        }
    }
	
	public static void detach( Char target, Class<? extends Buff> cl ) {
		detach( target.buff( cl ) );
	}
}
