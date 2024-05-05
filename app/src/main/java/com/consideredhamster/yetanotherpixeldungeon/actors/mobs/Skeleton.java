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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SkeletonSprite;
import com.watabou.utils.Random;

public class Skeleton extends MobPrecise {

    public Skeleton() {

        super( 6 );

        /*

            base maxHP  = 19
            armor class = 6

            damage roll = 3-9

            accuracy    = 15
            dexterity   = 12

            perception  = 110%
            stealth     = 110%

         */

        name = "skeleton";
        info = "Magical, Weakening attack";
        spriteClass = SkeletonSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Withered.class, damage * 2 );
        }

        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }

    @Override
    public String description() {
        return
                "Skeletons are composed of corpses bones from unlucky adventurers and inhabitants of the dungeon, " +
                "animated by emanations of evil magic from the depths below. Their vile touch is infamous for " +
                "its ability to sapping the lifeforce of the unlucky victim.";
    }

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
	}

}
