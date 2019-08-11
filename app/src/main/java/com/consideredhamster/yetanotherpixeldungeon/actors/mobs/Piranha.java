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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.PiranhaSprite;

public class Piranha extends MobEvasive {
	
	public Piranha() {

		super( Dungeon.depth + 1 );

        name = "giant piranha";
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        minDamage += tier * 2;
        maxDamage += tier * 2;

        HP = HT += Random.IntRange(2, 4);

        loot = new MeatRaw();
        lootChance = 0.5f;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

	}
	
	@Override
	protected boolean act() {
		if (!Level.water[pos]) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}
	
	@Override
	public boolean reset() {
        state = SLEEPING;
        return true;
	}

	@Override
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

    @Override
    protected int nextStepTo( Char enemy ) {
        return Dungeon.findPath( this, pos, enemy.pos,
                Level.water,
                Level.fieldOfView );
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Crippled.class, damage * 2 );
        }

        return damage;
    }

	@Override
	public String description() {
		return
			"These carnivorous fish are sometimes born in these underground pools. " +
			"Other times, they are bred specifically to protect flooded treasure vaults. " +
            "Regardless of origin, they all share the same ferocity and thirst for blood.";
	}

}
