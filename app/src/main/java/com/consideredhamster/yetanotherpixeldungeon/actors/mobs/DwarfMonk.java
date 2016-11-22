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

import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Combo;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yetanotherpixeldungeon.items.food.OverpricedRation;
import com.consideredhamster.yetanotherpixeldungeon.sprites.MonkSprite;

public class DwarfMonk extends MobEvasive {

//    public static final String TXT_DISARMED = "Monk's attack has knocked your %s out of your hands!";

    public static boolean swarmer = true;

    public DwarfMonk() {

        super( 13 );

        name = "dwarf monk";

        spriteClass = MonkSprite.class;

		loot = new OverpricedRation();
		lootChance = 0.05f;
	}

    @Override
    public float attackDelay() {
        return 0.5f;
    }

//	@Override
//	public String defenseVerb() {
//		return "parried";
//	}
	
	@Override
	public void die( Object cause, DamageType dmg ) {
		AmbitiousImp.Quest.process( this );
		
		super.die( cause, dmg );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {

//		if (  Random.Int( enemy.HT ) < damage && enemy == Dungeon.hero) {

//            Hero hero = (Hero)enemy;

//            EquipableItem weapon = Random.oneOf( hero.belongings.weap1, hero.belongings.weap2 );
//
//            if( weapon != null && weapon.disarmable() ) {
//
//                GLog.w(TXT_DISARMED, weapon.name());
//                weapon.doDrop(hero);
//
//            }
//		}

        damage += Buff.affect(this, Combo.class).hit( damage );
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. " +
			"They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Body.class);
        RESISTANCES.add(DamageType.Mind.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
