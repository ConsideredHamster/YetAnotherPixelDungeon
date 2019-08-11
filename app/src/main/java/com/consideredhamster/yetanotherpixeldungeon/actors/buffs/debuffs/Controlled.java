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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Controlled extends Debuff {

    @Override
    public Element buffType() {
        return Element.DISPEL;
    }

    @Override
    public String toString() {
        return "Controlled";
    }

    @Override
    public String statusMessage() { return "controlled"; }

    @Override
    public String playerMessage() { return "You are controlled!"; }

    @Override
    public int icon() {
        return BuffIndicator.CONTROL;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.CONTROLLED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.CONTROLLED );
    }

    @Override
    public String description() {
        return "You are not supposed to be able to see description of this debuff, but if you " +
                "somehow do, then it is just the same as Charmed, but for magical creatures.";
    }

    @Override
    public boolean attachOnLoad( Char target ) {
        if (super.attachOnLoad( target )) {

            if( target instanceof Mob ) {
                Mob mob =(Mob)target;
                if( mob.hostile && !Bestiary.isBoss( mob ) ) {
                    mob.friendly = true;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {

            if( target instanceof Mob ) {
                Mob mob =(Mob)target;
                if( mob.hostile && !Bestiary.isBoss( mob ) ) {
                    mob.friendly = true;
                    mob.resetEnemy();
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {

        if( target instanceof Mob ) {
            Mob mob =(Mob)target;
            if( mob.hostile && !Bestiary.isBoss( mob ) ) {
                mob.friendly = false;
                mob.resetEnemy();
            }
        }

        super.detach();
    }
}
