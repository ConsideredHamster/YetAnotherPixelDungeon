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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.food.ChargrilledMeat;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Buff {

	private static final String TXT_BURNS_UP		= "%s burns up!";
//	private static final String TXT_BURNED_TO_DEATH	= "You burned to death...";
	
	private static final float DURATION = 6f;
	
	private float left;
	
	private static final String LEFT	= "left";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
	}
	
	@Override
	public boolean act() {

        target.damage(
                Random.IntRange( 1, (int) Math.sqrt(
                    !Bestiary.isBoss(target) ? target.HT : target.HT / 4
                ) ), this, DamageType.FLAME
        );

        Blob blob = Dungeon.level.blobs.get( Burning.class );
//            Blob blob2 = Dungeon.level.blobs.get( Miasma.class );

        if (Level.flammable[target.pos] && ( blob == null || blob.cur[ target.pos ] <= 0 )) {
//            if (Level.flammable[target.pos] || blob1 != null && blob1.cur[target.pos] > 0 || blob2 != null && blob2.cur[target.pos] > 0) {
            GameScene.add(Blob.seed(target.pos, 4, Fire.class));
        }

        if (target instanceof Hero) {

            Item item = ((Hero) target).belongings.randomUnequipped();

            if (item instanceof Scroll || item instanceof Herb) {

                item = item.detach(((Hero) target).belongings.backpack);
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            } else if (item instanceof MysteryMeat) {

                item = item.detach(((Hero) target).belongings.backpack);
                ChargrilledMeat steak = new ChargrilledMeat();
                if (!steak.collect(((Hero) target).belongings.backpack)) {
                    Dungeon.level.drop(steak, target.pos).sprite.drop();
                }
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            }
        }

        left -= Random.Float( TICK / 2, TICK * 2 );

        if(left <= 0 || !target.isAlive() || Level.water[target.pos] && !target.flying ) {

            detach();

        } else {

            spend(TICK);

        }

		return true;
	}


    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {

            Buff.detach( target, Ensnared.class );
            Buff.detach( target, Frozen.class );
            Buff.detach( target, Bleeding.class );
            Buff.detach( target, Invisibility.class);

            return true;
        } else {
            return false;
        }
    }

    public void reignite( Char ch ) {

//        if( left == 0 ){
//            spend( TICK );
//        }

		left = duration( ch );
	}

	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}
	
	@Override
	public String toString() {
		return "Burning";
	}

	public static float duration( Char ch ) {
//		Protection r = ch.buff( Protection.class );
//		return r != null ? r.durationFactor() * DURATION : DURATION;
		return DURATION;
	}

//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromFire();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.BURNING, Dungeon.depth ) );
//		GLog.n( TXT_BURNED_TO_DEATH );
//	}

    @Override
    public Class<? extends DamageType> buffType() {
        return DamageType.Flame.class;
    }

}
