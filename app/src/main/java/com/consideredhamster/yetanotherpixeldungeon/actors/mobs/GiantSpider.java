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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.SpiderWeb;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SpiderSprite;
import com.watabou.utils.Random;

public class GiantSpider extends MobHealthy {

    public GiantSpider() {

        super( 7 );

        /*

            base maxHP  = 22
            armor class = 8

            damage roll = 5-12

            accuracy    = 9
            dexterity   = 7

            perception  = 90%
            stealth     = 90%

         */

		name = "giant spider";
		info = "Poison bite, Spiderwebs";

		spriteClass = SpiderSprite.class;
		
		loot = new MeatRaw();
		lootChance = 0.150f;
		
		FLEEING = new Fleeing();

        resistances.put( Element.Mind.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
	}
	
	@Override
	protected boolean act() {
		boolean result = super.act();

		if (state == FLEEING && buff( Tormented.class ) == null ) {
			if (enemy != null && enemySeen && enemy.buff( Poisoned.class ) == null) {
				state = HUNTING;
			}
		}

		return result;
	}
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Poisoned.class, damage * 2 );
            state = FLEEING;
		}
		
		return damage;
	}

	@Override
    public void die( Object cause, Element dmg ) {

        Ghost.Quest.process( pos );

        SpiderWeb.spawn( pos, Random.IntRange( 5, 7 ) );

        super.die( cause, dmg );

    }
	
	@Override
	public String description() {		
		return 
			"These overgrown subterranean spiders try to avoid direct combat, preferring to poison " +
            "their target and then run away. Their abdomens store large amounts of web, which is " +
            "usually used to wrap up their prey after it succumbs to their venom.";
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Tormented.class ) == null) {
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
