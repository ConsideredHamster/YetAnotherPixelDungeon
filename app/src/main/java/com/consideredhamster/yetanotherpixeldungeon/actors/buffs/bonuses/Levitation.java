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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Levitation extends Bonus {

    @Override
    public String toString() {
        return "Levitating";
    }

    @Override
    public String statusMessage() { return "levitating"; }

    @Override
    public String playerMessage() { return "You start floating in the air!"; }

    @Override
    public int icon() {
        return BuffIndicator.LEVITATION;
    }

    @Override
    public void applyVisual(){
        target.sprite.add( CharSprite.State.LEVITATING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.LEVITATING );
    }

    @Override
    public String description() {
        return "The whole body feels like it is lighter than air! While levitating, traps and " +
            "terrain effects do not affect you, and your movement speed and dexterity are increased.";
    }
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.flying = true;
			Ensnared.detach(target, Ensnared.class);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		target.flying = false;
		Dungeon.level.press( target.pos, target );
		super.detach();
	}
}
