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

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Poisoned extends Debuff {

    public final static String TXT_CANNOT_EAT = "You can't stomach anything right now.";

    @Override
    public Element buffType() {
        return Element.BODY;
    }

    @Override
    public String toString() {
        return "Poisoned";
    }

    @Override
    public String statusMessage() { return "poisoned"; }

    @Override
    public String playerMessage() { return "You are poisoned!"; }

    @Override
    public int icon() {
        return BuffIndicator.POISONED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.POISONED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.POISONED );
    }

    @Override
    public String description() {
        return "You are not feeling well... Seems like there is a poison in your veins. This will " +
                "slowly damage you and make you weaker, decreasing your damage and attack speed.";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.totalHealthValue() ) ) + 1, this, Element.BODY );

        return super.act();

    }

}
