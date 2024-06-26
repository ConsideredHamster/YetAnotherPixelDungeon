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
package com.consideredhamster.yetanotherpixeldungeon.actors;

import java.util.Arrays;
import java.util.HashSet;

import android.util.SparseArray;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffReactive;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Actor implements Bundlable {
	
	public static final float TICK	= 1f;

	private float time;
	
	private int id = 0;
	
	protected abstract boolean act();
	
	protected void spend( float time ) {
		this.time += time;
	}

    public int actingPriority(){
        return 0;
    }
	
	protected void postpone( float time ) {
//		if (this.time < now + time) {
			this.time = now + time;
//		}
	}
	
	protected float cooldown() {
		return time - now;
	}
	
	protected void deactivate() {
		time = Float.MAX_VALUE;
	}
	
	protected void onAdd() {}
	
	protected void onRemove() {}
	
	private static final String TIME	= "time";
	private static final String ID		= "id";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( TIME, time );
		bundle.put( ID, id );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		time = bundle.getFloat( TIME );
		id = bundle.getInt( ID );
	}

    public void delay( float duration ) {
        spend( duration );
    }
	
	public int id() {
		if (id > 0) {
			return id;
		} else {
			int max = 0;
			for (Actor a : all) {
				if (a.id > max) {
					max = a.id;
				}
			}
			return (id = max + 1);
		}
	}
	
	// **********************
	// *** Static members ***
	
	private static HashSet<Actor> all = new HashSet<Actor>();
	private static Actor current;
	
	private static SparseArray<Actor> ids = new SparseArray<Actor>();
	
	private static float now = 0;
	
	private static Char[] chars = new Char[Level.LENGTH];
	
	public static void clear() {
		
		now = 0;
		
		Arrays.fill( chars, null );
		all.clear();
		
		ids.clear();
	}
	
	public static void fixTime() {
		
		if (Dungeon.hero != null && all.contains( Dungeon.hero )) {
			Statistics.duration += now;
		}
		
		float min = Float.MAX_VALUE;
		for (Actor a : all) {
			if (a.time < min) {
				min = a.time;
			}
		}
		for (Actor a : all) {
			a.time -= min;
		}
		now = 0;
	}
	
	public static void init() {
		
		addDelayed( Dungeon.hero, -Float.MIN_VALUE );

        for ( Mob mob : Dungeon.level.mobs ) {
            add( mob );
        }

        for ( Hazard hazard : Dungeon.level.hazards ) {
            add( hazard );
        }
		
		for ( Blob blob : Dungeon.level.blobs.values() ) {
			add( blob );
		}
		
		current = null;
	}
	
	public static void occupyCell( Char ch ) {
		chars[ch.pos] = ch;
	}

    public static void moveToCell( Char ch, int newPos ) {

        if( chars[ ch.pos ] == ch && chars[ newPos ] == null ){

            chars[ ch.pos ] = null;
            chars[ newPos ] = ch;

        }

    }

    public static void freeCell( int pos ) {
        chars[pos] = null;
    }
	
	/*protected*/public void next() {
		if (current == this) {
			current = null;
		}
	}
	
	public static void process() {
		
		if (current != null || !Dungeon.hero.isAlive()) {
			return;
		}
	
		boolean doNext;

		do {

			now = Float.MAX_VALUE;

			current = null;

			Arrays.fill( chars, null );
			
			for (Actor actor : all) {

				if (actor.time < now || actor.time == now &&
                    ( current == null || actor.actingPriority() > current.actingPriority() )
                ) {

					now = actor.time;

					current = actor;

				}
				
				if (actor instanceof Char) {
					Char ch = (Char)actor;
					chars[ch.pos] = ch;
				}
			}

            if (current != null) {
				
				if (current instanceof Char && ((Char)current).sprite != null && ((Char)current).sprite.isMoving) {
					// If it's character's turn to act, but its sprite 
					// is moving, wait till the movement is over
					current = null;
					break;
				}
				
				doNext = current.act();

				if (doNext && !Dungeon.hero.isAlive()) {
					doNext = false;
					current = null;
				}

			} else {
				doNext = false;
			}
			
		} while (doNext);
	}
	
	public static void add( Actor actor ) {
		add( actor, now );
	}
	
	public static void addDelayed( Actor actor, float delay ) {
		add( actor, now + delay );
	}
	
	private static void add( Actor actor, float time ) {
		
		if (all.contains( actor )) {
			return;
		}
		
		if (actor.id > 0) {
			ids.put( actor.id,  actor );
		}
		
		all.add( actor );
		actor.time += time;
		actor.onAdd();
		
		if (actor instanceof Char) {
			Char ch = (Char)actor;
			chars[ch.pos] = ch;
			for (Buff buff : ch.buffs()) {
				all.add( buff );
				buff.onAdd();
			}
		}
	}
	
	public static void remove( Actor actor ) {
		
		if (actor != null) {
			all.remove( actor );
			actor.onRemove();
			
			if (actor.id > 0) {
				ids.remove( actor.id );
			}
		}
	}
	
	public static Char findChar( int pos ) {
		return pos >= 0 && pos < chars.length ? chars[pos] : null ;
	}
	
	public static Actor findById( int id ) {
		return ids.get( id );
	}
	
	public static HashSet<Actor> all() {
		return all;
	}
}
