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

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MirrorSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MirrorImage extends NPC {
	
	{
		name = "mirror image";
		spriteClass = MirrorSprite.class;

		state = HUNTING;
	}
	
	public int gear;
	
	private int attack;
	private int damage;
	
	private static final String GEAR	= "gear";
	private static final String ATTACK	= "attack";
	private static final String DAMAGE	= "damage";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GEAR, gear );
		bundle.put( ATTACK, attack );
		bundle.put( DAMAGE, damage );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		gear = bundle.getInt( GEAR );
		attack = bundle.getInt( ATTACK );
		damage = bundle.getInt( DAMAGE );
	}
	
	public void duplicate( Hero hero ) {
		gear = hero.appearance();
		attack = hero.accuracy();
		damage = hero.damageRoll();
	}
	
	@Override
	public int accuracy() {
		return attack;
	}
	
	@Override
	public int damageRoll() {
		return damage;
	}

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {
		int dmg = super.attackProc( enemy, damage, blocked );

		destroy();
		sprite.die();
		
		return dmg;
	}
	
	public Char chooseEnemy() {
		
		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob:Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add( mob );
				}
			}
			
			return enemies.size() > 0 ? Random.element( enemies ) : null;
		}
		
		return enemy;
	}
	
	@Override
	public String description() {
		return
			"This illusion bears a close resemblance to you, " +
			"but it's paler and twitches a little.";
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		((MirrorSprite)s).updateArmor( gear );
		return s;
	}

	@Override
	public void interact() {
		
		int curPos = pos;
		
		moveSprite( pos, Dungeon.hero.pos );
		move( Dungeon.hero.pos );
		
		Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
		Dungeon.hero.move( curPos );
		
		Dungeon.hero.spend( 1 / Dungeon.hero.moveSpeed() );
		Dungeon.hero.busy();
	}
}