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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Thunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class PotionOfThunderstorm extends Potion {

    public static final int BASE_VAL	= 300;

	{
		name = "Potion of Thunderstorm";
        shortName = "Th";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

        Blob blob = Blob.seed( cell, BASE_VAL, Thunderstorm.class );

        GameScene.add( blob );

        Thunderstorm.thunderstrike(cell, blob);

        if( Dungeon.hero.isAlive() ) {
            if (Dungeon.visible[cell]) {
                Thunderstorm.viewed();
            } else if ( Level.distance(cell, Dungeon.hero.pos) <= 4) {
                Thunderstorm.listen();
            }
        }

        super.shatter( cell );
	}
	
	@Override
	public String desc() {
		return
			"Upon exposure to open air, the liquid in this flask will start a localized rainfall, " +
            "complete with storm clouds and lightning. While the main use of this potion is to " +
            "water crops, nothing stops you from using it to fry your enemies with occasional " +
            "thunderbolts. Be careful to not get hit yourself!";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 95 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.40f;
    }
}
