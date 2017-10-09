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

import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Arrows;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Javelins;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Quarrels;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GnollSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;

public class GnollHunter extends MobRanged {

    public GnollHunter() {

        super( 3 );

		name = "gnoll hunter";
		spriteClass = GnollSprite.class;

        switch( Dungeon.chapter() ) {
            case 1:
                loot = Bullets.class;
                lootChance = 0.25f;
                break;
            case 2:
                loot = Arrows.class;
                lootChance = 0.375f;
                break;
            case 3:
                loot = Quarrels.class;
                lootChance = 0.5f;
                break;
            default:
                loot = Bullets.class;
                lootChance = 0.5f;
                break;
        }

        lootChance = 0.25f;


	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos && !isCharmedBy( enemy );
    }

//    @Override
//    public int attackProc( Char enemy, int damage ) {
//
//        if ( distance(enemy) > 1 && Random.Int( enemy.HT ) < damage ) {
//            Buff.affect( enemy, Poison.class ).set(Random.IntRange( damage / 2 , damage ));
//            enemy.sprite.burst( 0x00AAAA, 5 );
//        }
//
//        return damage;
//    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
            reset(pos, cell, new Javelins(), new Callback() {
                @Override
                public void call() {
                    onAttackComplete();
                }
            });

        super.onRangedAttack( cell );
    }

    @Override
    public void damage( int dmg, Object src, DamageType type ) {
        super.damage(dmg, src, type);

        if ( isAlive() && src != null && HP >= HT / 2 && HP + dmg < HT / 2 ) {

            state = FLEEING;

            if (Dungeon.visible[pos]) {
                sprite.showStatus(CharSprite.NEGATIVE, "fleeing");
//                spend( TICK );
            }

        }
    }
	
	@Override
	public void die( Object cause, DamageType dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {
//		return
//			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
//			"Gnoll hunters are regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.";

        return "Gnolls are hyena-like humanoids. "

                + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                "This one seems to be a hunter or something like. It is not like their sharped sticks gonna be a problem to you." : "" )

                + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                "Curiously, they are very rarely observed so close to a human settlements, preferring to dwell somewhere in wilderness." : "" )

                + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                "And that's probably everything there is to know about them. Who cares, anyway?" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                "They seem to be in alliance with wild beasts and other denizens of these depths. Maybe, even... leading them?" : "" )

                ;
	}

//    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
//    static {
//        RESISTANCES.add(DamageType.Body.class);
//    }
//
//    @Override
//    public HashSet<Class<? extends DamageType>> resistances() {
//        return RESISTANCES;
//    }
}
