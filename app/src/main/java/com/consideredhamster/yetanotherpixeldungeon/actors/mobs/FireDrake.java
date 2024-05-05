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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.FireDrakeSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class FireDrake extends MobPrecise {

    public FireDrake() {

        super( 14 );

        /*

            base maxHP  = 35
            armor class = 0

            damage roll = 5-17 (2-8)

            accuracy    = 29
            dexterity   = 24

            perception  = 120%
            stealth     = 105%

         */

        name = "fire drake";
        info = "Flying, Fire breath, Fiery death";

        spriteClass = FireDrakeSprite.class;

        flying = true;

        resistances.put( Element.Flame.class, Element.Resist.IMMUNE );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

        resistances.put( Element.Frost.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

    }

//    @Override
//    public Element damageType() {
//        return Element.FLAME;
//    }
//
//    @Override
//    public int damageRoll() {
//        return isRanged() ? super.damageRoll() / 2 : super.damageRoll();
//    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.fire(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }



    @Override
    public boolean cast( Char enemy ) {

        if (hit( this, enemy, true, true )) {

            enemy.damage( damageRoll(), this, Element.FLAME );

        } else {

            enemy.missed();
        }

        return true;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        if (Level.flammable[pos]) {
            GameScene.add(Blob.seed(pos, 1, Fire.class));
        }

        super.die( cause, dmg );
    }
	
	@Override
	public String description() {
		return
			"Drakes of different kinds had roamed the deeper bowels of this world long before " +
            "dwarves domesticated them. Fire drakes are the most common variety, able to fly and " +
            "belch fire to protect their bearded masters.";
	}

}
