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

import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponFlintlock;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Guard extends Buff {

    private static String TXT_PARRIED = "parried";
    private static String TXT_BLOCKED = "blocked";

    private static String TXT_PARRY_BROKEN = "parry failed!";
    private static String TXT_BLOCK_BROKEN = "block failed!";

    @Override
    public String toString() {
        return "Guard";
    }

    public void reset( boolean withShield ) {

        target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCK_BROKEN : TXT_PARRY_BROKEN);

    }

    public void proc( boolean withShield ) {

        if( target.sprite.visible ) {
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCKED : TXT_PARRIED);

            if (target == Dungeon.hero) {
                Camera.main.shake(2, 0.1f);
            }
        }
    }

}
