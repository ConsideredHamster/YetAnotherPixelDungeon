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

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfBlastWave extends WandUtility {

	{
		name = "Wand of Force Blast";
        image = ItemSpriteSheet.WAND_BLAST_WAVE;
        // I am really not sure how I managed to end up with exact the same name for a wand as in
        // ShPD despite intentionally trying to avoid that. Well, I guess I'll keep it for now.
    }

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.00f;
    }

    @Override
	protected void onZap( int cell ) {

        int damage = damageRoll();

        Char ch = Char.findChar( cell );

        if( ch != null ) {
            Pushing.knockback( ch, curUser.pos, damage * 4 / ch.totalHealthValue() + 1, damage );
        } else if ( Level.solid[ cell ] ) {
            rocksFall( Ballistica.trace[ Ballistica.distance - 1 ], damage * 3 / 2 );
        } else {
            // Do nothing
        }

        if( Dungeon.visible[ cell ] ) {
            CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST, false ), 0.03f, 8 );
        }

	}

	private void rocksFall( int cell, int damage ) {

        int size = 2;

        Sample.INSTANCE.play( Assets.SND_ROCKS );
        Camera.main.shake(3, 0.07f * (3 + size));

        PathFinder.buildDistanceMap( cell, BArray.not( Level.solid, null ), size );
        Ballistica.distance = Math.min( Ballistica.distance, 2 );

        for (int i=0; i < Level.LENGTH; i++) {

            int d = PathFinder.distance[i];

            if ( d < Integer.MAX_VALUE ) {

                boolean wall = false;

                for(int n : Level.NEIGHBOURS4) {
                    if( Level.solid[ i + n ] ) {
                        wall = true;
                    }
                }

                if( wall || Random.Int( d * 2 + 1 ) == 0) {

                    Char ch = Actor.findChar(i);
                    if (ch != null ) {

                        int dmg = Char.absorb( ch == curUser ? damage / 2 : damage , ch.armorClass() );

                        ch.damage(dmg, curUser, Element.PHYSICAL);

                        if ( ch.isAlive() ) {
                            BuffActive.addFromDamage( ch, Vertigo.class, dmg );
                        }

                    }

                    Heap heap = Dungeon.level.heaps.get( i );
                    if (heap != null) {
                        heap.shatter();
                    }

                    Dungeon.level.press(i, null);

                    CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.1f, 3 + (size - d));
                }
            }
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= 6 ) {
                mob.beckon( cell );
            }
        }
    }
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.blast( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"Wand will release a wave of force so strong that it may send your target flying back, " +
            "slamming into walls or someone else. If this wave hits a wall or other solid object, " +
            "it will cause an avalanche of stones, damaging and stunning everyone in the nearby area.";
	}
}
