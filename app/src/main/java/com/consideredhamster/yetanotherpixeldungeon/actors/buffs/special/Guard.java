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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffReactive;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class Guard extends BuffReactive {

//    private static String TXT_PARRIED = "parried";
    private static String TXT_BLOCKED = "blocked";

//    private static String TXT_PARRY_BROKEN = "parry failed!";
    private static String TXT_BLOCK_BROKEN = "block failed!";

    @Override
    public int icon() {
        return BuffIndicator.GUARD;
    }

    @Override
    public String toString() {
        return "Guard";
    }

    @Override
    public String statusMessage() { return "guard"; }

    @Override
    public boolean attachTo( Char target ) {

        Buff.detach( target, Combo.class);
        Buff.detach( target, Focus.class );

        return super.attachTo( target );
    }

    @Override
    public String description() {
        return "You are standing in a defensive position, trying to block physical attacks. Every " +
                "successful block will possibly expose your attacker to a powerful counterattack.";
    }

    public void reset( boolean withShield ) {

        target.sprite.showStatus(CharSprite.WARNING, TXT_BLOCK_BROKEN );
//        target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCK_BROKEN : TXT_PARRY_BROKEN);

    }

    public void proc( boolean withShield ) {

        if( target.sprite.visible ) {
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            target.sprite.showStatus(CharSprite.DEFAULT, TXT_BLOCKED );
//            target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCKED : TXT_PARRIED);

            if (target == Dungeon.hero) {
                Camera.main.shake(2, 0.1f);
            }
        }
    }

}
