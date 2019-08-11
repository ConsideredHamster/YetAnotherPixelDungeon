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
package com.consideredhamster.yetanotherpixeldungeon.visuals.effects;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;

public class HolyLight extends Image {

	private static final float TIME_TO_FADE = 0.8f;

	private float time;

	public HolyLight() {
		super( Effects.get( Effects.Type.CROSS ) );
		origin.set( width / 2, height / 2 );
        hardlight( SpellSprite.COLOUR_HOLY );
	}
	
	public void reset( int p ) {
		revive();
		
		x = (p % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;
		
		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha( p );
            scale.set( 2 - p );
		}
	}
	
	public static void createAtChar( Char ch ) {
		HolyLight w = (HolyLight)ch.sprite.parent.recycle( HolyLight.class );
		ch.sprite.parent.bringToFront( w );
		w.reset( ch.pos );
	}
	
	public static void createAtPos( int pos ) {
		Group parent = Dungeon.hero.sprite.parent;
		HolyLight w = (HolyLight)parent.recycle( HolyLight.class );
		parent.bringToFront( w );
		w.reset( pos );
	}
}
