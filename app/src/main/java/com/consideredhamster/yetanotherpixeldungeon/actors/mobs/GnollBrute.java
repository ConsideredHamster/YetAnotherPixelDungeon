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

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Tomahawks;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.BruteSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.watabou.utils.Random;

public class GnollBrute extends MobPrecise {

	private static final String TXT_ENRAGED = "%s becomes enraged!";

    public GnollBrute() {

        super( 10 );

        /*

            base maxHP  = 27
            armor class = 9 + 9

            damage roll = 4-13 (2-6)

            accuracy    = 22
            dexterity   = 18

            perception  = 105%
            stealth     = 105%

         */

		name = "gnoll brute";
		spriteClass = BruteSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;

        resistances.put( Element.Body.class, Element.Resist.PARTIAL );
        resistances.put( Element.Mind.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
	}

    @Override
    public int shieldAC() {

        return armorClass;

    }

    @Override
    public boolean blocksRanged() {
        return true;
    }

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() / 2 : super.damageRoll();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance(pos, enemy.pos) <= 2
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Tomahawks(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack( cell );
    }
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

        super.damage( dmg, src, type );

		if ( isAlive() && buff( Enraged.class ) == null && HP < HT / 2 ) {

            BuffActive.add(this, Enraged.class, Random.Float( 5.0f, 10.0f ) );
            spend( TICK );

            if (Dungeon.visible[pos]) {
                GLog.w( TXT_ENRAGED, name );
//                sprite.showStatus( CharSprite.NEGATIVE, "enraged" );
            }
        }
	}

	@Override
	public String description() {
		return
			"Brutes are the largest, strongest and toughest of all gnolls. They are dumb, " +
            "but very ferocious fighters. They can become temporarily enraged when injured enough.";
	}
}
