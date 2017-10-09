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

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SkeletonSprite;
import com.watabou.utils.Random;

public class Skeleton extends MobPrecise {

//	private static final String TXT_HERO_KILLED = "You were killed by the explosion of bones...";

    public Skeleton() {

        super( 6 );

        name = "skeleton";
        spriteClass = SkeletonSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;
	}

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {
        if ( Random.Int(enemy.HT) < damage * 2 ) {

            Withered buff = Buff.affect(enemy, Withered.class);

            if( buff != null ) {
                buff.prolong();
                enemy.sprite.burst( 0x000000, 5 );
            }
        }

        return damage;
    }

//    @Override
//    protected void dropLoot() {
//        if (Random.Int( 5 ) == 0) {
//            Item loot = Generator.random( Generator.Category.WEAPON );
//            for (int i=0; i < 2; i++) {
//                Item l = Generator.random( Generator.Category.WEAPON );
//                if (l.price() < loot.price()) {
//                    loot = l;
//                }
//            }
//            Dungeon.bonus.drop( loot, pos ).sprite.drop();
//        }
//    }

    @Override
    public String description() {
        return
                "Skeletons are composed of corpses bones from unlucky adventurers and inhabitants of the dungeon, " +
                "animated by emanations of evil magic from the depths below. Their vile touch is infamous for " +
                "its ability to sapping the lifeforce of the unlucky victim.";
    }

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Frost.class);
        RESISTANCES.add(DamageType.Unholy.class);

        IMMUNITIES.add(DamageType.Mind.class);
        IMMUNITIES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }
	
	@Override
	public void die( Object cause ) {

		super.die( cause );
//
//		boolean heroKilled = false;
//		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
//			Char ch = findChar( pos + Level.NEIGHBOURS8[i] );
//			if (ch != null && ch.isAlive()) {
//				int damage = Math.max( 0,  damageRoll() - Random.IntRange( 0, ch.armorClass() / 2 ) );
//				ch.damage( damage, this );
//				if (ch == Dungeon.hero && !ch.isAlive()) {
//					heroKilled = true;
//				}
//			}
//		}
//
		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
//
//		if (heroKilled) {
//			Dungeon.fail( Utils.format( ResultDescriptions.MOB, Utils.indefinite( name ), Dungeon.depth ) );
//			GLog.n( TXT_HERO_KILLED );
//		}
	}
	
//	@Override
//	public String defenseVerb() {
//		return "blocked";
//	}
}
