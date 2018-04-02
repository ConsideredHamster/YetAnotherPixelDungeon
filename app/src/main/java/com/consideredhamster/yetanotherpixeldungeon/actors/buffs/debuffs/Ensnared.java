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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Ensnared extends Debuff {

    @Override
    public String toString() {
        return "Ensnared";
    }

    @Override
    public String statusMessage() { return "ensnared"; }

    @Override
    public String playerMessage() { return "You are ensnared! Struggle to escape!"; }

    @Override
    public int icon() {
        return BuffIndicator.ENSNARED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.BURNING );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.BURNING );
//    }

    @Override
    public String description() {
        return "You are ensnared and cannot move. Evading attacks is more difficult, and you are " +
                "more likely to be noticed. You can try to break out, but this can attract unwanted " +
                "attention.";
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
//		if (!target.flying && super.attachTo( target )) {
            target.rooted = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.rooted = false;
        super.detach();
    }

}