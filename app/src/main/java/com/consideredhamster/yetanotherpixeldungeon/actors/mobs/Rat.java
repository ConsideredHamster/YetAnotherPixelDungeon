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
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.RatSprite;

public class Rat extends MobEvasive {

    public static boolean swarmer = true;

    public Rat() {

        super( 1 );

        name = "marsupial rat";
        spriteClass = RatSprite.class;

        minDamage += 1;

    }

	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		
		super.die( cause, dmg );
	}

//    @Override
//    protected boolean act() {

//        if ( enemySeen && HP >= HT ) {
//
//            for ( Mob mob : Dungeon.bonus.mobs ) {
//                if ( mob instanceof Rat && mob.enemySeen && mob != this ) {
//                    state = HUNTING;
//                    return super.act();
//                }
//            }
//
//            state = FLEEING;
//
//        }

//        return super.act();
//    }

//    @Override
//    protected boolean getCloser( int target ) {
//        if ( state == HUNTING && HP >= HT ) {
//
//            for ( Mob mob : Dungeon.bonus.mobs ) {
//                if ( mob instanceof Rat && mob.enemySeen && mob.HP >= mob.HT && mob != this ) {
//                    return super.getCloser( target );
//                }
//            }
//
//            state = FLEEING;
//            return super.getFurther( target );
//
//        }
//
//        return super.getCloser( target );
//    }
	
	@Override
	public String description() {
		return "This vermin inhabited the City for almost as long as these sewers had existed. " +
                "But recently there appeared rumours about these rats attacking pets, toddlers and " +
                "even adults sometimes. "

                    + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                    "They are hardly can be considered a worthy opponent for you, but they can be dangerous in large numbers." : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                    "Undoubtedly, these creatures are not the main threat down there, but their unnatural aggressiveness can be unnerving." : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                    "Why, just why it did have to be rats?.." : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                    "The malicious intent which twisted minds of these little creatures is just a sign of the things to come." : "" )

                ;
	}
}
