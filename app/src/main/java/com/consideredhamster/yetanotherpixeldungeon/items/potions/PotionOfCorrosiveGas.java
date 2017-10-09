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
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.CorrosiveGas;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class PotionOfCorrosiveGas extends Potion {

    public static final int BASE_VAL	= 300;
    public static final int MODIFIER	= 30;

	{
		name = "Potion of Corrosive Gas";
        shortName = "CG";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

        GameScene.add( Blob.seed( cell, BASE_VAL + MODIFIER * alchemySkill(), CorrosiveGas.class ) );

		if (Dungeon.visible[cell]) {
			setKnown();
			
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}
	}
	
	@Override
	public String desc() {
		return
			"Uncorking or shattering this pressurized glass will cause its contents to explode " +
            "into a deadly cloud of highly flammable toxic gas, which will poison whoever happen to inhale it. " +
            "You might choose to fling this potion at distant enemies instead of uncorking it by hand.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 40 * quantity : super.price();
	}
}
