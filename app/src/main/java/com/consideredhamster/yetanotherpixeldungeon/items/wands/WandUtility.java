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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfWillpower;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.watabou.utils.Random;

import java.util.Locale;

public abstract class WandUtility extends Wand {

    @Override
	public int maxCharges( int bonus ) {
		return 3 + state + ( bonus > 0 ? bonus : 0 ) ;
	}

    @Override
    public float effectiveness( int bonus ) {
        return effect( getCharges() );
    }

    private float effect( int charges ) {
        return 0.10f + 0.10f * charges;
    }

    @Override
    public float rechargeRate( int bonus ) {
        return 0.05f + 0.02f * state + 0.03f * ( bonus > 0 ? bonus : 0 );
    }

    @Override
    public int damageRoll() {

        if( curUser != null ) {

            float eff = effectiveness();

            if( curUser.buff( Withered.class ) != null )
                eff *= 0.5f;

            if( curUser.buff( Charmed.class ) != null )
                eff *= 0.5f;

            int max = (int)( curUser.magicPower() * eff );
            int min = max * 4 / 5;

            return Random.IntRange( min, max );

        } else {

            return 0;

        }
    }

    @Override
    protected float miscastChance( int bonus ) {

        if( !isIdentified() || bonus < 0 ){
            return 0.35f - 0.05f * state - 0.05f * bonus ;
        } else {
            return 0f;
        }

    }

    @Override
    protected float squeezeChance( int bonus ) {

        if( isIdentified() && bonus >= 0 ){
            return 0.2f + 0.05f * state + 0.05f * bonus;
        } else {
            return 0f;
        }

    }

    @Override
    protected void spendCharges() {

        curCharges = 0;

    }

    @Override
    protected String wandInfo() {

        Hero hero = Dungeon.hero;

        final String p = "\n\n";
        StringBuilder info = new StringBuilder();

        // if we are not sure what stats we currently have due to some of the relevant equipment
        // being unidentified, then use base values of these stats. not the best way to do it,
        // but it should work for now. maybe when i'll get to reworking weapons, i'll expand this
        float magicPower =
                hero.belongings.ring1 instanceof RingOfWillpower && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfWillpower && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1 instanceof Quarterstaff && !hero.belongings.weap1.isIdentified() ?
                        hero.magicPower : hero.magicPower();

        float attunement =
                hero.belongings.ring1 instanceof RingOfMysticism && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfMysticism && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.armor instanceof MageArmor && !hero.belongings.armor.isIdentified() ?
                        hero.baseAttunement() : hero.attunement();

        // again, if the wand is not identified yet, then values are displayed as if it was unupgraded
        int max = (int)( magicPower * effect( maxCharges( isIdentified() ? bonus : 0 ) ) );
        int min = max * 4 / 5;

        String recharge = String.format( Locale.getDefault(), "%.1f",
                1.0f / attunement / rechargeRate( isIdentified() ? bonus : 0 )
        );

        String chance = String.format( Locale.getDefault(), "%.0f",
                100 * ( !isIdentified() || bonus < 0
                        ? miscastChance( isIdentified() ? bonus : 0 )
                        : squeezeChance( isIdentified() ? bonus : 0 ) )
        );

        if ( !isIdentified() ){

            info.append(
                    "This wand is _" + ( isCursedKnown() && bonus < 0 ? "cursed" : "unidentified" ) +
                    "_, but is in a _" + stateToString( state ) + " condition_. Most likely, it " +
                    "holds only _up to " + maxCharges( 0 ) + " charges_ and will probably " +
                    "have _" + chance + "% chance_ to miscast when used."
            );

            info.append( p );

            info.append(
                "With your current magic power and attunement values, power of this wand will " +
                "(probably) be _" + min + "-" + max + " points_ when fully charged and it will " +
                "recover one charge _per " + recharge + " turns_."
            );

        } else {

            info.append(
                "This wand is _" + ( bonus < 0 ? "cursed" : "not cursed" ) + "_ and is " +
                "in a _" + stateToString( state ) + " condition_. It currently holds _" +
                getCharges() + "/" + maxCharges() + " charges_ and will have _" + chance +"% " +
                "chance_ to " + ( bonus < 0 ? "miscast when used." : "squeeze an additional charge." )
            );

            info.append( p );

            info.append(
                "With your current magic power and attunement values, power of this wand will " +
                "be  _" + min + "-" + max + " points_ when fully charged and it will recover " +
                "one charge _per " + recharge  + " turns_."
            );

        }

        return info.toString();
    }
}
