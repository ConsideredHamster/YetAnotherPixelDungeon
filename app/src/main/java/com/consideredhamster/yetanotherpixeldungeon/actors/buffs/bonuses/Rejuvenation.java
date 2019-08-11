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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Sunlight;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Rejuvenation extends Bonus {

    @Override
    public int icon() {
        return BuffIndicator.SUNLIGHT;
    }

    @Override
    public String toString() {
        return "Consecrated";
    }

    @Override
    public String statusMessage() { return "consecrated"; }

    @Override
    public String description() {
        return "Being bathed in holy light restores your health. The longer you are affected by " +
                "this effect, the more health it restores.";
    }
    @Override
    public boolean act() {

        Blob blob = Dungeon.level.blobs.get( Sunlight.class );

        if (blob != null && blob.cur[ target.pos ] > 0 ){

            double effect =  Math.sqrt( target.totalHealthValue() ) + duration;

            target.heal( Random.Int( (int)effect ) + 1 );

            spend( TICK );

        } else {

            detach();

        }

        return true;
    }

}
