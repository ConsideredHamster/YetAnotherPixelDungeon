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
package com.consideredhamster.yetanotherpixeldungeon.actors.blobs;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.Journal.Feature;
import com.consideredhamster.yetanotherpixeldungeon.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class WaterOfHealth extends WellWater {

	private static final String TXT_PROCCED =
		"You refill your waterskin in the well.";

    private static final String TXT_VIAL_IS_FULL =
            "Your waterskin is already full.";

    private static final String TXT_NO_MORE_WATER =
            "The well is empty now.";

    @Override
    protected boolean affect() {

        if (pos == Dungeon.hero.pos ) {

            Dungeon.hero.interrupt();

            Waterskin vial = Dungeon.hero.belongings.getItem(Waterskin.class);

            int space = vial.space();

            if ( space > 0 ) {

                int fill = Math.min( space, cur[ pos ] );

                vial.fill( fill );
                volume = off[pos] = cur[pos] -= fill;

//                Dungeon.hero.sprite.pickup( pos );
//                Dungeon.hero.spend( TICK );

                GLog.i(TXT_PROCCED);

                Sample.INSTANCE.play(Assets.SND_DRINK);

                if( cur[ pos ] <= 0 ) {

                    GLog.i(TXT_NO_MORE_WATER);
                    Journal.remove(Feature.WELL);
                    return true;

                }

            } else {

                GLog.i(TXT_VIAL_IS_FULL);

            }
        }

        return false;
    }
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use(emitter);
		emitter.start( Speck.factory( Speck.DISCOVER ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return
			"The water in this well looks clean and fresh. " +
			"Looks like you can refill your water supplies here.";
	}



//	@Override
//	protected boolean affectHero( Hero hero ) {
//
//		Sample.INSTANCE.play( Assets.SND_DRINK );

//        hero.HP = hero.HT;
//
//        Buff.detach( hero, Burning.class );
//        Buff.detach( hero, Poison.class );
//        Buff.detach( hero, Cripple.class );
//        Buff.detach( hero, Withered.class );
//        Buff.detach( hero, Bleeding.class );
//
//        hero.belongings.uncurseEquipped();
//        hero.buff( Hunger.class ).satisfy(Hunger.STARVING, true);
//
//        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
//        CellEmitter.get(pos).start( ShaftParticle.FACTORY, 0.2f, 3 );

//		Dungeon.hero.interrupt();
//
//		GLog.p(TXT_PROCCED);
//
//		Journal.remove(Feature.WELL_OF_HEALTH);
//
//		return true;
//	}

//	@Override
//	protected Item affectItem( Item item ) {
//		if (item instanceof DewVial && !((DewVial)item).isFull()) {
//			((DewVial)item).fill();
//			return item;
//		}
//
//		return null;
//	}
}
