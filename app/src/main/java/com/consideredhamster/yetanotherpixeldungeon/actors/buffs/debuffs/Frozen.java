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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Frozen extends Debuff {

    public final static String TXT_CANNOT_LIGHT = "Your lantern is too cold to be lit again. You'll have to wait a little.";

    @Override
    public Element buffType() {
        return Element.FROST;
    }

    @Override
    public String toString() {
        return "Frozen";
    }

    @Override
    public String statusMessage() { return "frozen"; }

    @Override
    public String playerMessage() { return "Intense cold slows your movement!"; }

    @Override
    public int icon() {
        return BuffIndicator.FROZEN;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.CHILLED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.CHILLED );
    }

    @Override
    public String description() {
        return "Brrr, it is cold! All your actions are slowed, your lantern is not working, and on " +
                "top of that you are less likely to hit and dodge. Oh, and being in water prolongs " +
                "duration of this effect.";
    }



    @Override
    public boolean act() {

        if( Level.water[ target.pos ] && !target.flying && Random.Int( 2 ) == 0 ) {
            duration++;
        }

        return super.act();
    }

    @Override
    public boolean attachTo( Char target ) {

        if (super.attachTo( target )) {

            Buff.detach( target, Burning.class );

            if( target instanceof Hero ){

                Hero hero = (Hero)target;

                OilLantern lantern = hero.belongings.getItem( OilLantern.class );

                if( lantern != null && lantern.isActivated() ){
                    lantern.deactivate( hero, false );
                }

            }

//            if (target instanceof Hero) {
//                Hero hero = (Hero)target;
//                Item item = hero.belongings.randomUnequipped();
//                if (item instanceof RawMeat ) {
//
//                    item.detach( hero.belongings.backpack );
//
//                    FrozenCarpaccio carpaccio = new FrozenCarpaccio();
//                    if (!carpaccio.collect( hero.belongings.backpack )) {
//                        Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
//                    }
//                }
//            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {


        super.detach();
    }
}
