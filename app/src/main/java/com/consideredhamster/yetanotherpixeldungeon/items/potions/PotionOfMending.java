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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Bleeding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Cripple;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Mending;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;

public class PotionOfMending extends Potion {

    public static final float DURATION	= 20f;
    public static final float MODIFIER	= 1.0f;

	{
		name = "Potion of Mending";
        shortName = "Me";
	}
	
	@Override
	protected void apply( Hero hero ) {
        hero.sprite.showStatus( CharSprite.POSITIVE, "mending" );;
        heal(Dungeon.hero);
        setKnown();
    }
	
	public static void heal( Hero hero ) {

        Mending buff = Buff.affect( hero, Mending.class );

        if( buff != null ) {
            buff.setDuration( (int)( DURATION + alchemySkill() * MODIFIER ) );
        }

		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach( hero, Bleeding.class );
		Buff.detach( hero, Withered.class );

		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
	}
	
	@Override
	public String desc() {
		return
			"When imbibed, this elixir will vastly improve imbiber's natural regeneration and cure " +
            "any physical ailments as well.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 30 * quantity : super.price();
	}
}
