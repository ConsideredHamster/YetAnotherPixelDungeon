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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Crippled extends Debuff {

    @Override
    public Element buffType() {
        return Element.BODY;
    }

    @Override
    public String toString() {
        return "Crippled";
    }

    @Override
    public String statusMessage() { return "crippled"; }

    @Override
    public String playerMessage() { return "Your legs are crippled! Don't move too much."; }

    @Override
    public int icon() {
        return BuffIndicator.CRIPPLED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BLEEDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLEEDING );
    }

    @Override
    public String description() {
        return "Your legs are severely wounded, making it harder to dodge and significantly " +
                "slowing your movement. Running around will prolong duration of this effect.";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.totalHealthValue() * 0.5f ) ) + 1, this, Element.BODY );

        if( target.moving && Random.Int( 2 ) == 0 ) duration++;

        return super.act();
    }

}
