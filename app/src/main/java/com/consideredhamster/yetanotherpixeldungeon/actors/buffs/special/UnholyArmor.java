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

package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffPassive;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

import java.util.HashSet;

public class UnholyArmor extends BuffPassive {

    public static final HashSet<Class<? extends Element>> RESISTS;

    static {

        // yes, this buff gives immunity to *everything*
        RESISTS = new HashSet<>();
        RESISTS.add(Element.Physical.class);
        RESISTS.add(Element.Knockback.class);

        RESISTS.add(Element.Flame.class);
        RESISTS.add(Element.Flame.Periodic.class);
        RESISTS.add(Element.Shock.class);
        RESISTS.add(Element.Shock.Periodic.class);
        RESISTS.add(Element.Acid.class);
        RESISTS.add(Element.Acid.Periodic.class);
        RESISTS.add(Element.Frost.class);

        RESISTS.add(Element.Unholy.class);
        RESISTS.add(Element.Doom.class);

        RESISTS.add(Element.Body.class);
        RESISTS.add(Element.Mind.class);

        RESISTS.add(Element.Energy.class);
        RESISTS.add(Element.Dispel.class);
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.UNHOLYARMOR );
    }

    @Override
    public void removeVisual() { target.sprite.remove( CharSprite.State.UNHOLYARMOR ); }

    private int consumed;

    public int consumed() {
        return consumed;
    }

    public void consumed( int value ) {
        consumed += value;
    }

    @Override
    public int icon() {
        return BuffIndicator.SHIELDED;
    }

    @Override
    public String toString() {
        return "Unholy Armor";
    }

    private static final String CONSUMED	= "consumed";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( CONSUMED, consumed );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        consumed = bundle.getInt( CONSUMED );
    }
}