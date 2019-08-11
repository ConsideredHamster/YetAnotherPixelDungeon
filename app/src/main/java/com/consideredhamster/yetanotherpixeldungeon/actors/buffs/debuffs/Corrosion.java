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

import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.CausticOoze;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Corrosion extends Debuff {

    @Override
    public Element buffType() {
        return Element.ACID;
    }

    @Override
    public String toString() { return "Corrosion"; }

    @Override
    public String statusMessage() { return "corrosion"; }

    @Override
    public String playerMessage() { return "Caustic ooze is eating your flesh!"; }

    @Override
    public int icon() {
        return BuffIndicator.CAUSTIC;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BLIGHTED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLIGHTED );
    }

    @Override
    public String description() {
        return "Caustic ooze corrodes your armor and skin. This is not only painful, but also " +
                "slowly damages your armor and makes it less effective for the duration of the " +
                "effect. Use water to remove it as soon as possible!";
    }

    @Override
    public boolean act() {

        target.damage(
            Random.Int( (int)Math.sqrt(
                target.totalHealthValue() * 0.5f
            ) ) + 1, this, Element.ACID_PERIODIC
        );

        if( target instanceof Hero ) {
            Hero hero = (Hero)target;
            if( hero.belongings.armor != null ) {
                hero.belongings.armor.use( 1 );
            }
        }

        if ( !target.isAlive() || Level.water[ target.pos ] && !target.flying ) {
            detach();
            return true;
        }

        return super.act();
    }

    @Override
    public void detach() {

        if( !target.isAlive() && duration > 0 ) {
            CausticOoze.spawn( target.pos, duration );
        }

        super.detach();
    }

}
