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
package com.consideredhamster.yetanotherpixeldungeon.items.food;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class FrozenCarpaccio extends Food {

	{
		name = "frozen carpaccio";
		image = ItemSpriteSheet.CARPACCIO;
		energy = Hunger.STARVING / 4;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			
//			switch (Random.Int( 2 )) {
//			case 0:
//				GLog.i( "You see your hands turn invisible!" );
//				Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
//				break;
//			case 1:
//				GLog.i( "You feel your skin hardens!" );
//				Buff.affect( hero, Barkskin.class ).bonus( hero.HT / 4 );
//				break;
//			case 2:
				GLog.i( "Refreshing!" );
//				Buff.detach( hero, Poison.class );
//				Buff.detach( hero, Cripple.class );
//				Buff.detach( hero, Withered.class );
//				Buff.detach( hero, Bleeding.class );
//				break;
//			case 3:
//				GLog.i( "You feel better!" );
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + hero.HT / 10, hero.HT );
					hero.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
				}
//				break;
//			}
		}
	}
	
	@Override
	public String info() {
		return 
			"It's a piece of frozen raw meat. The only way to eat it is " +
			"by cutting thin slices of it. And this way it's suprisingly good.";
	}
	
	public int price() {
		return 15 * quantity;
	};
	
	public static Food cook( MysteryMeat ingredient ) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}
}
