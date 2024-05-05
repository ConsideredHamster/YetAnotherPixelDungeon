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

import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Arrows;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Javelins;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Quarrels;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GnollSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;

public class GnollHunter extends MobRanged {

    public GnollHunter() {

        super( Dungeon.chapter() * 5 - 2 );

        /*

            base maxHP  = 9/12/15
            armor class = 2/4/6

            damage roll = 2-5/4-8/6-11

            accuracy    = 11/15/19
            dexterity   = 5/6/7

            perception  = 110%/120%/130%
            stealth     = 100%/100%/100%

         */

		name = "gnoll hunter";
		info = "Spear throw";

		spriteClass = GnollSprite.class;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

        switch( Dungeon.chapter() ) {
            case 1:
                loot = Bullets.class;
                lootChance = 0.25f;
                break;
            case 2:
                loot = Arrows.class;
                lootChance = 0.375f;
                break;
            case 3:
                loot = Quarrels.class;
                lootChance = 0.5f;
                break;
            default:
                loot = Bullets.class;
                lootChance = 0.5f;
                break;
        }
	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
            reset(pos, cell, new Javelins(), new Callback() {
                @Override
                public void call() {
                    onAttackComplete();
                }
            });

        super.onRangedAttack( cell );
    }

	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {

        return "Gnolls are hyena-like humanoids. Curiously, they are very rarely observed so " +
                "close to a human settlements, preferring to dwell somewhere in wilderness.";
	}

}
