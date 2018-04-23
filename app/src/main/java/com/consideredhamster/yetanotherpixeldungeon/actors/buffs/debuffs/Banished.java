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
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Banished extends Debuff {
	
//	public int object = 0;

    @Override
    public Element buffType() {
        return Element.DISPEL;
    }

    @Override
    public String toString() {
        return "Banished";
    }

    @Override
    public String statusMessage() { return "banished"; }

//    @Override
//    public String playerMessage() { return ""; }

    @Override
    public int icon() {
        return BuffIndicator.BANISH;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.add( CharSprite.State.PROTECTION );
//    }

//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.PROTECTION );
//    }


    @Override
    public String description() {
        return "You are not supposed to be able to see description of this debuff, but if you " +
                "somehow do, then it is just the same as Tormented, but for magical creatures.";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.totalHealthValue() * 1.5f ) ) + 1, this, Element.DISPEL );

        return super.act();

    }

//    private static final String OBJECT	= "object";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle( bundle );
//        bundle.put( OBJECT, object );
//
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        object = bundle.getInt( OBJECT );
//    }
}
