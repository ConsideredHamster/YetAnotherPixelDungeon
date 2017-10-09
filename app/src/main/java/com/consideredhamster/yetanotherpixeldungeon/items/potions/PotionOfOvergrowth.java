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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Overgrowth;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class PotionOfOvergrowth extends Potion {

    public static final int BASE_VAL	= 250;
    public static final int MODIFIER	= 25;

	{
		name = "Potion of Overgrowth";
        shortName = "Ov";
        harmful = true;
	}

    @Override
    public void shatter( int cell ) {

        GameScene.add(Blob.seed(cell, BASE_VAL + MODIFIER * alchemySkill(), Overgrowth.class));

        boolean mapUpdated = false;

        for (int n : Level.NEIGHBOURS5) {

            if( n == 0 || Random.Float() < 0.75f ) {

                int i = cell + n;
                int c = Dungeon.level.map[i];

                switch (c) {
                    case Terrain.EMPTY:
                    case Terrain.EMPTY_DECO:
                    case Terrain.EMBERS:

                        Level.set(i, Terrain.GRASS);
                        mapUpdated = true;
                        break;

                    case Terrain.GRASS:

                        Level.set(i, Terrain.HIGH_GRASS);
                        mapUpdated = true;
                        break;

                }
            }
        }

        if (mapUpdated) {
            GameScene.updateMap();
            Dungeon.observe();
        }

        if (Dungeon.visible[cell]) {
            setKnown();
            splash(cell);
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }
    }
	
	@Override
	public String desc() {
		return
			"This wondrous concoction fills the air around it with magical fumes, " +
			"forcing roots, grass and plants in its area of effect to grow at a greatly accelerated rate. " +
            "Apart from creating an instant cover, it can be thrown at your enemies to " +
            "stop them in their tracks or to re-decorate your garden.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 45 * quantity : super.price();
	}
}
