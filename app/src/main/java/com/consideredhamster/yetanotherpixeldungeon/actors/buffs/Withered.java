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

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class Withered extends PassiveBuff {

    public static final float DELAY = 10f;

    protected int effect;

    private static final String EFFECT	= "effect";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( EFFECT, effect );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        effect = bundle.getInt(EFFECT);
    }

	@Override
	public int icon() {
		return BuffIndicator.WITHERED;
	}
	
	@Override
	public String toString() {
        return Utils.format("Withered (%d)", effect);
	}

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {

            effect = 0;

            spend( DELAY );
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {

//        target.weakened = 0;

        QuickSlot.refresh();

        super.detach();
    }

    public void prolong() {

        if( effect == 0 || Random.Float() < 1.0f / Math.sqrt( effect + 1 ) ) {
//        if( Random.Int( effect + 1) == 0 ) {

            target.sprite.showStatus(CharSprite.NEGATIVE, "withered");

//            target.weakened = ++effect;
            effect++;

//            QuickSlot.refresh();

        }
    };

    @Override
    public boolean act() {

//        target.weakened = --effect;
        effect--;

        if (effect <= 0) {
            detach();
        } else {
            QuickSlot.refresh();
            spend( DELAY );
        }

        return true;
    }
	
	public static float duration( Char ch ) {
//		Protection r = ch.buff( Protection.class );
//		return r != null ? r.durationFactor() * DELAY : DELAY;
        return DELAY;
	}

    public float modifier() {
        return (float)Math.pow( 0.9f, effect );
    }

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Unholy.class;
    }
}
