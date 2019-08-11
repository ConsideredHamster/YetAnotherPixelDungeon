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
package com.consideredhamster.yetanotherpixeldungeon.actors.hazards;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HazardSprite;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public abstract class Hazard extends Actor {

    public Class<? extends HazardSprite> spriteClass;
    public HazardSprite sprite;

    public int pos;
    public int var;

//    public Hazard() {
//        super();
//    }

    @Override
    protected boolean act(){
        return false;
    }

    @Override
    public int actingPriority(){
        return 3;
    }

    public abstract void press( int cell, Char ch );

    public HazardSprite sprite() {
        HazardSprite sprite = null;
        try {
            sprite = spriteClass.newInstance();
        } catch (Exception e) {
            return null;
        }
        return sprite;
    }

    public String desc() {
        return null;
    };

    public void destroy() {

        Dungeon.level.hazards.remove(this);
        Actor.remove(this);

    }

    public static <T extends Hazard> T findHazard( int pos, Class<T> hazardClass ) {

        for( Hazard hazard : Dungeon.level.hazards ) {
            if( pos == hazard.pos && hazardClass.isInstance( hazard ) ){
                return hazardClass.cast( hazard );
            }
        }

        return null;
    }

    public static HashSet<Hazard> findHazards( int pos ) {

        HashSet<Hazard> hazards = new HashSet<>();

        for( Hazard hazard : Dungeon.level.hazards ) {
            if( pos == hazard.pos )
                hazards.add( hazard );
        }

        return hazards;
    }

    private static final String POS	= "pos";
    private static final String VAR	= "var";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( POS, pos );
        bundle.put( VAR, var );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        pos = bundle.getInt( POS );
        var = bundle.getInt( VAR );
    }
}
