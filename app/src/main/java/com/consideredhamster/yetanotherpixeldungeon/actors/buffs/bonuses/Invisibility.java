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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Invisibility extends Bonus {

    private static final String TXT_DISPEL		=
            "Invisibility is dispelled!";

    @Override
    public String toString() {
        return "Invisible";
    }

    @Override
    public String statusMessage() { return "invisible"; }

    @Override
    public String playerMessage() { return "You see your hands turn invisible!"; }

    @Override
    public int icon() {
        return BuffIndicator.INVISIBLE;
    }

    @Override
    public void applyVisual(){

        if( target.sprite.visible ){
            Sample.INSTANCE.play( Assets.SND_MELD );
        }

        target.sprite.add( CharSprite.State.INVISIBLE );
    }

    @Override
    public void removeVisual() {
        if( target.invisible <= 0 ){
            target.sprite.remove( CharSprite.State.INVISIBLE );
        }
    }

    @Override
    public String description() {
        return "Your body is almost transparent, so enemies are less likely to notice you (but they " +
                "still can try to find you) and stealing from shops becomes easier. Attacking, " +
                "stealing or being bumped into will dispel this effect.";
    }

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.invisible++;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		if(target.invisible > 0) {
            target.invisible--;
        }
		super.detach();
	}


    public static void dispel() {

        Invisibility.dispel( Dungeon.hero );

    }

	public static void dispel( Char ch ) {

        Invisibility buff = ch.buff( Invisibility.class );
        if ( buff != null ) {
            GLog.w(TXT_DISPEL);
            buff.detach();
		}
	}
}
