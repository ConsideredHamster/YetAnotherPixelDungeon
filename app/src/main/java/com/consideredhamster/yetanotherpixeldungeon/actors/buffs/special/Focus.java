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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffPassive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffReactive;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class Focus extends BuffReactive {

    @Override
    public int icon() {
        return BuffIndicator.FOCUSED;
    }

    @Override
    public String toString() {
        return "Focused";
    }

//    @Override
//    public String statusMessage() { return "focused"; }

    @Override
    public boolean attachTo( Char target ) {

        Buff.detach( target, Combo.class);
        Buff.detach( target, Guard.class );

        return super.attachTo( target );
    }

    @Override
    public String description() {
        return "You spent a turn to take aim at present enemies. Your next attack or wand zap will " +
                "be twice more precise than usual.";
    }


}
