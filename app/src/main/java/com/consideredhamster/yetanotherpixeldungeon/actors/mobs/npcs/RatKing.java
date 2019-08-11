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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.RatKingSprite;

public class RatKing extends NPC {

	{
		name = "rat king";
		spriteClass = RatKingSprite.class;
		
		state = SLEEPING;
	}
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 1000;
//	}
	
	@Override
	public float moveSpeed() {
		return 2f;
	}
	
	@Override
	public Char chooseEnemy() {
		return null;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
	}
	
	@Override
    public boolean add( Buff buff ) {
        return false;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (state == SLEEPING) {
			notice();
			yell( "I'm not sleeping!" );
			state = WANDERING;
		} else {
			yell( "What is it? I have no time for this nonsense. My kingdom won't rule itself!" );
		}
	}
	
	@Override
	public String description() {
		return 
			"This rat is a little bigger than a regular marsupial rat " +
			"and it's wearing a tiny crown on its head.";
	}
}
