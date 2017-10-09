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
import com.watabou.noosa.tweeners.AlphaTweener;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class PotionOfInvisibility extends Potion {

	private static final float ALPHA	= 0.4f;

    public static final float DURATION	= 10f;
    public static final float MODIFIER	= 1.0f;

	
	{
		name = "Potion of Invisibility";
        shortName = "In";
	}
	
	@Override
	protected void apply( Hero hero ) {
		Buff.affect( hero, Invisibility.class, DURATION + alchemySkill() * MODIFIER );
        GLog.i( "You see your hands turn invisible!" );
        Sample.INSTANCE.play( Assets.SND_MELD );
        setKnown();
    }
	
	@Override
	public String desc() {
		return
			"Drinking this potion will render you temporarily invisible. While invisible, " +
			"enemies will be unable to see you, but they can try to find you nevertheless. " +
            "Attacking an enemy, as well as using a wand or a scroll before enemy's eyes, " +
            "will dispel the effect.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 45 * quantity : super.price();
    }
	
	public static void melt( Char ch ) {
		if (ch.sprite.parent != null) {
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, ALPHA, 0.4f ) );
		} else {
			ch.sprite.alpha( ALPHA );
		}
	}
}
