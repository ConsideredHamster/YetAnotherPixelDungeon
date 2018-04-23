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

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.SheepSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfFlock extends WandUtility {

	{
		name = "Wand of Flock";
        shortName = "Fl";
	}
	
	@Override
	protected void onZap( int cell ) {

		int n = curCharges;
		
		if (Actor.findChar( cell ) != null && Ballistica.distance > 2) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		boolean[] passable = BArray.or( Level.passable, Level.avoid, null );
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				passable[((Char)actor).pos] = false;
			}
		}
		
		PathFinder.buildDistanceMap( cell, passable, n );
		int dist = 0;
		
		if (Actor.findChar( cell ) != null) {
			PathFinder.distance[cell] = Integer.MAX_VALUE;
			dist = 1;
		}
		
		float lifespan = 6 + curCharges;
		
	sheepLabel:
		for (int i=0; i < n; i++) {
			do {
				for (int j=0; j < Level.LENGTH; j++) {
					if (PathFinder.distance[j] == dist) {
						
						Sheep sheep = new Sheep();
						sheep.lifespan = lifespan;
						sheep.pos = j;
						GameScene.add( sheep );
						Dungeon.level.press( j, sheep );
						
						CellEmitter.get( j ).burst( Speck.factory( Speck.WOOL ), 4 );
						
						PathFinder.distance[j] = Integer.MAX_VALUE;
						
						continue sheepLabel;
					}
				}
				dist++;
			} while (dist < n);
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.wool( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return 
			"A flick of this wand summons a flock of magic sheep, creating temporary impenetrable obstacle.";
	}
	
	public static class Sheep extends NPC {
		
		private static final String[] QUOTES = {"Baa!", "Baa?", "Baa.", "Baa..."};
		
		{
			name = "sheep";
			spriteClass = SheepSprite.class;
		}
		
		public float lifespan;
		
		private boolean initialized = false;
		
		@Override
		protected boolean act() {
			if (initialized) {
				HP = 0;

				destroy();
				sprite.die();
				
			} else {
				initialized = true;
				spend( lifespan + Random.Float( 2 ) );
			}
			return true;
		}
		
		@Override
        public void damage( int dmg, Object src, Element type ) {
		}
		
		@Override
		public String description() {
			return 
				"This is a magic sheep. What's so magical about it? You can't kill it. " +
				"It will stand there until it magically fades away, all the while chewing cud with a blank stare.";
		}

		@Override
		public void interact() {
			yell( Random.element( QUOTES ) );
		}
	}
}
