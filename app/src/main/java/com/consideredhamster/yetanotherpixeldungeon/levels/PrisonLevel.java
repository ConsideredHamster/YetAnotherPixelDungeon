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
package com.consideredhamster.yetanotherpixeldungeon.levels;

import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Ghost;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Halo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room.Type;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class PrisonLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;

//        viewDistance = 7;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

    @Override
    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
    }
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}
	}
	
	@Override
	protected void createMobs() {
		super.createMobs();

        Ghost.Quest.spawn(this);
	}
	
	@Override
	protected void decorate() {

        for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map[i] == Terrain.EMPTY) {
                if (Random.Int( 10 ) == 0 ) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < LENGTH ; i++) {
            if (
                    i + WIDTH < LENGTH && map[i] == Terrain.WALL &&
                    !Arrays.asList( Terrain.WALLS ).contains( map[i + WIDTH] ) &&
                    Random.Int( 15 ) == 0
            ) {

                map[i] = Random.oneOf(
                    Terrain.WALL_DECO, Terrain.WALL_DECO1, Terrain.WALL_DECO2
                );

            } else if(
                    map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                    Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }

		for (Room r : rooms) {
            if (r.type == Type.STANDARD) {
                for (Room n : r.neighbours) {
                    if (n.type == Type.STANDARD && !r.connected.containsKey( n )) {
                        Rect w = r.intersect( n );
                        if (w.left == w.right && w.bottom - w.top >= 4) {

                            w.top += 2;
                            w.bottom -= 1;

                            w.right++;

                            Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.GRATE );

                        } else if (w.top == w.bottom && w.right - w.left >= 4) {

                            w.left += 2;
                            w.right -= 1;

                            w.bottom++;

                            Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.GRATE );
                        }
                    }
                }
            }
        }

        for (Room room : rooms) {
            if (room.type != Room.Type.STANDARD) {
                continue;
            }

            if (room.width() <= 3 || room.height() <= 3) {
                continue;
            }


        }
	}

    @Override
    public String tileName( int tile ) {
        return PrisonLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return PrisonLevel.tileDescs(tile);
    }
	
//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Dark cold water.";
		default:
			return Level.tileNames(tile);
		}
	}
	
//	@Override
	public static String tileDescs(int tile) {
		switch (tile) {
            case Terrain.EMPTY_DECO:
                return "There are old blood stains on the floor.";
            case Terrain.WALL_DECO:
                return "A torch hangs on the wall, burning dimly. Who even keeps these alight?";
            case Terrain.WALL_DECO1:
                return "There is an old iron grate built into this wall. You can't see what is there in the darkness.";
            case Terrain.WALL_DECO2:
                return "Seems like someone's remains are stored here. Why?";
            case Terrain.WALL_DECO3:
            case Terrain.WALL_DECO4:
            case Terrain.WALL_DECO5:
                return "There is a dried up bloodstain here. Creepy.";
            case Terrain.BOOKSHELF:
                return "This is probably a vestige of a prison library. Maybe there would be something useful in here?";
            case Terrain.SHELF_EMPTY:
                return "This is probably a vestige of a prison library.";
            default:
                return Level.tileDescs(tile);
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Torch( i ) );
			}
		}
	}
	
	private static class Torch extends Emitter {
		
		private int pos;
		
		public Torch( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 1, p.y + 3, 2, 0 );
			
			pour( FlameParticle.FACTORY, 0.15f );
			
			add( new Halo( 16, 0xFFFFCC, 0.2f ).point( p.x, p.y ) );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
}