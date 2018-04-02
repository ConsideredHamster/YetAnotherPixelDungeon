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
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Tormented extends Debuff {
	
	public final static String TXT_CANNOT_ATTACK = "You are in panic! You cannot attack!";

    @Override
    public Element buffType() {
        return Element.MIND;
    }

    @Override
    public String toString() {
        return "Tormented";
    }

    @Override
    public String statusMessage() { return "tormented"; }

    @Override
    public String playerMessage() { return "Your mind is seized with fear and pain!"; }

    @Override
    public int icon() {
        return BuffIndicator.TERROR;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.BLEEDING );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.BLEEDING );
//    }

    @Override
    public String description() {
        return "Terrifying magic fills your mind with pain. Your ranged attacks become very " +
                "unreliable, and attacking in melee is not even an option. RUN!";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.currentHealthValue() * 1.5f ) ) + 1, this, Element.MIND );

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
