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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Wraith;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;

import java.util.ArrayList;

public class ScrollOfRaiseDead extends Scroll {
	
	{
		name = "Scroll of Raise Dead";
        shortName = "Ra";

        spellSprite = SpellSprite.SCROLL_RAISEDEAD;
        spellColour = SpellSprite.COLOUR_DARK;
	}
	
	@Override
	protected void doRead() {

//        Char target = curUser;

//        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
//            if ( Level.fieldOfView[mob.pos] && mob.hostile && !( mob instanceof Wraith ) && ( target == curUser || mob.HP > target.HP ) ) {
//
//                target = mob;
//
//            }
//        }

        ArrayList<Wraith> summoned = Wraith.spawnAround( curUser.pos, Random.IntRange( 2, 4 ) );

        for (Wraith w : summoned) {

//            w.EXP = 0;

            float duration = Random.Int( 16, 20 ) * ( 110 + curUser.magicSkill() ) / 100;

//            Buff.affect( w, Summoned.class, duration );

            Charm buff = Buff.affectForced(w, Charm.class, duration );

            if( buff != null ) {
                buff.object = curUser.id();
            }

//            w.aggro( target );

        }

        Sample.INSTANCE.play(Assets.SND_DEATH);
		
		super.doRead();
	}
	
	@Override
	public String desc() {
		return
                "Malicious magics hidden within this scroll allow its reader to commune with unspeakable, " +
                "giving him or her an ability to summon lost souls from the underworld, and even " +
                "control them for a short period. However, these phantasms would happily tear their " +
                "master to pieces once control over them is lost." +
                "\n\nDuration of controlling effect depends on magic skill of the reader.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 100 * quantity : super.price();
    }
}
