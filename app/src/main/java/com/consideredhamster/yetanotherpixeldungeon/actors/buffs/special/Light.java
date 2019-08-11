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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffPassive;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Light extends BuffPassive {

    private final static float DELAY = 4f;

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.ILLUMINATED );
    }

    @Override
    public void removeVisual() { target.sprite.remove( CharSprite.State.ILLUMINATED ); }
	
	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

    @Override
    public String toString() {
        return "Light";
    }

    @Override
    public String description() {
        return "Your lantern shines brightly, increasing your field of view and your " +
                "chance to find traps, but making it easier to notice you as well." ;
    }

    @Override
    public boolean act() {

        OilLantern lantern = Dungeon.hero.belongings.getItem( OilLantern.class );

        if( lantern != null && lantern.isActivated() && lantern.getCharge() > 0 ){

            lantern.spendCharge();
            spend( DELAY );

        } else {

            lantern.deactivate( Dungeon.hero, false );
            detach();

        }

        return true;
    }
}
