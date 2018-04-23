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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.CorrosiveGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ScorpionSprite;
import com.watabou.utils.Random;

public class CaveScorpion extends MobHealthy {

    public CaveScorpion() {

        super( 12 );

		name = "cave scorpion";
		spriteClass = ScorpionSprite.class;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

	}

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Corrosion.class, damage );
        }

        return damage;
    }

    @Override
	public void die( Object cause, Element dmg ) {

        GameScene.add(Blob.seed(pos, 50, CorrosiveGas.class));

        super.die(cause, dmg);
    }

	@Override
	public String description() {
		return
			"These huge arachnid-like creatures pose a significant threat to any adventurer " +
            "due to a ability to inject acid with their tails.";
	}

}
