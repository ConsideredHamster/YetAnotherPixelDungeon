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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Guard;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Ethereal;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Tempered;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class MeleeWeapon extends Weapon {

	public MeleeWeapon(int tier) {

		super();
		
		this.tier = tier;

	}

    private static final String TXT_NOTEQUIPPED = "You have to equip this weapon first!";
    private static final String TXT_CANNOTGUARD = "You can guard only with shields and melee weapons!";
    private static final String TXT_GUARD = "guard";

    private static final String AC_GUARD = "GUARD";

    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add(AC_GUARD);
//        return actions;
//    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {

                GLog.n(TXT_NOTEQUIPPED);

            } if ( hero.belongings.weap2 != null && !(hero.belongings.weap2 instanceof MeleeWeapon) && !(hero.belongings.weap2 instanceof Shield) ) {

                GLog.n(TXT_CANNOTGUARD);

            } else {

                hero.buff( Satiety.class ).decrease( Satiety.POINT * str() / hero.STR() );
                Buff.affect( hero, Guard.class ).reset( 1 );
                hero.spendAndNext( Actor.TICK );

            }

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public int maxDurability() {
        return 100 ;
    }

    @Override
    public int min( int bonus ) {
        return Math.max( 0, tier + state + bonus + ( enchantment instanceof Tempered ? bonus + tier - 1 : 0 ) - 2 );
    }

    @Override
    public int max( int bonus ) {
        return Math.max( 0, tier * 2 + state * dmgMod() - 1
                + ( enchantment instanceof Tempered || bonus >= 0 ? bonus * dmgMod() : 0 )
                + ( enchantment instanceof Tempered && bonus >= 0 ? bonus + tier + 1 : 0 ) ) ;
    }

    public int dmgMod() {
        return tier ;
    }

    @Override
    public int str(int bonus) {
        return 5 + tier * 2 - bonus * ( enchantment instanceof Ethereal ? 2 : 1 );
    }

    @Override
    public int penaltyBase(Hero hero, int str) {

        return super.penaltyBase(hero, str) + tier * 4;

    }

    @Override
    public float stealingDifficulty() { return 0.75f; }

	@Override
	public int price() {

        int price = 20 + state * 10;

        price *= lootChapter();

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if( enchantment != null && isEnchantKnown() ) {
            price += price / 4;
        }

        return price;
	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isEnchantKnown() {
        return known >= ENCHANT_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

}
