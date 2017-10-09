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

import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Buff extends Actor {

	public Char target;

    public Class<? extends DamageType> buffType() {
        return null;
    }

    public boolean tryAttach( Char target ) {

        if (target.immunities().contains(buffType())) {
            target.sprite.showStatus(CharSprite.NEUTRAL, "immune");
            return false;
        } else if (target.resistances().contains(buffType())) {
            if (Random.Int(2) == 0) {
                target.sprite.showStatus(CharSprite.WARNING, "resisted");
                return false;
            }
        }

        return attachTo(target);
    }

    public boolean attachTo( Char target ) {

        this.target = target;

        return target.add(this);

    }
	
	public void detach() {
		target.remove(this);
	}
	
	@Override
	public boolean act() {
		deactivate();
		return true;
	}
	
	public int icon() {
		return BuffIndicator.NONE;
	}

    public float duration() {
        return cooldown();
    }

    public boolean awakensMobs() { return true; }

    public static<T extends Buff> T appendForced( Char target, Class<T> buffClass ) {
        try {

            T buff = buffClass.newInstance();

            return buff.attachTo( target ) ? buff : null;

        } catch (Exception e) {
            return null;
        }
    }
	
	public static<T extends Buff> T append( Char target, Class<T> buffClass ) {
		try {

			T buff = buffClass.newInstance();

            return buff.tryAttach( target ) ? buff : null;

		} catch (Exception e) {
			return null;
		}
	}
	
	public static<T extends PassiveBuff> T append( Char target, Class<T> buffClass, float duration ) {
		T buff = append(target, buffClass);

        if (buff != null) {
            buff.spend(duration);
        }

		return buff;
	}

    public static<T extends Buff> T affectForced( Char target, Class<T> buffClass ) {
        T buff = target.buff(buffClass);
        if (buff != null) {
            return buff;
        } else {
            return appendForced( target, buffClass );
        }
    }

    public static<T extends PassiveBuff> T affectForced( Char target, Class<T> buffClass, float duration ) {
        T buff = affectForced(target, buffClass);

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
	
	public static<T extends PassiveBuff> T affect( Char target, Class<T> buffClass, float duration ) {
		T buff = affect(target, buffClass);

        if (buff != null) {
            buff.spend(duration);
        }

		return buff;
	}
	
	public static<T extends PassiveBuff> T prolong( Char target, Class<T> buffClass, float duration ) {
		T buff = affect( target, buffClass );

        if (buff != null) {
            buff.postpone(duration);
        }

		return buff;
	}

    public static<T extends PassiveBuff> T shorten( Char target, Class<T> buffClass, float duration ) {
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
