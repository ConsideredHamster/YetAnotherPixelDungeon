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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Web;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Terror;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.SpinnerSprite;
import com.watabou.utils.Random;

public class GiantSpider extends MobHealthy {

    public GiantSpider() {

        super( 7 );

		name = "giant spider";
		spriteClass = SpinnerSprite.class;
		
		loot = new MysteryMeat();
		lootChance = 0.3f;
		
		FLEEING = new Fleeing();
	}
	
	@Override
	protected boolean act() {
		boolean result = super.act();

		if (state == FLEEING && buff( Terror.class ) == null) {
			if (enemy != null && enemySeen && enemy.buff( Poison.class ) == null) {
				state = HUNTING;
			}
		}
		return result;
	}
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {
        if ( !blocked && Random.Int( enemy.HT ) < damage ) {

            Poison buff = Buff.affect( enemy, Poison.class );

            if( buff != null ) {
                buff.addDuration(Random.IntRange( damage / 3, damage / 2 ));
            }

            state = FLEEING;
		}

        GameScene.add( Blob.seed( enemy.pos, Random.IntRange( 5, 7 ), Web.class ) );
		
		return damage;
	}


	
//	@Override
//	public void move( int step ) {
//		if (state == FLEEING) {
//			GameScene.add( Blob.seed( pos, Random.IntRange( 5, 7 ), Web.class ) );
//		}
//		super.move( step );
//	}
	
	@Override
	public String description() {		
		return 
			"These greenish furry cave spiders try to avoid direct combat, preferring to wait in the distance " +
			"while their victim, entangled in the spinner's excreted cobweb, slowly dies from their poisonous bite.";
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null) {
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
