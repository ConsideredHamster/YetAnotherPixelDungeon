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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.CorrosiveGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ScorpionSprite;

public class CaveScorpion extends MobHealthy {

    public CaveScorpion() {

        super( 12 );

		name = "cave scorpion";
		spriteClass = ScorpionSprite.class;

//        baseSpeed = 0.75f;
//		loot = new PotionOfHealing();
//		lootChance = 0.125f;

//        loot = new MysteryMeat();
//        lootChance = 0.35f;
	}
	
//	@Override
//	protected boolean canAttack( Char enemy ) {
//		return !Level.adjacent( pos, enemy.pos ) && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
//        return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos && !isCharmedBy( enemy );
//	}

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( damage < enemy.HP && damage > 0 ) {
            Buff.affect(enemy, Ooze.class);
        }

        enemy.sprite.burst( 0x007044, 5 );

        return damage;
    }

    @Override
	public void die( Object cause, DamageType dmg ) {

        GameScene.add(Blob.seed(pos, 50, CorrosiveGas.class));

        super.die(cause, dmg);
    }
	
//	@Override
//	protected boolean getCloser( int target ) {
//		if (state == HUNTING) {
//			return enemySeen && getFurther( target );
//		} else {
//			return super.getCloser( target );
//		}
//	}
	
	@Override
	public String description() {
		return
			"These huge arachnid-like creatures pose a significant threat to any adventurer " +
            "due to a ability to inject acid with their tails.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
