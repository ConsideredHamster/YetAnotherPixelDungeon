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

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Halo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class HallsLevel extends RegularLevel {

	{
//		minRoomSize = 6;
		
//		viewDistance = 4;
		
		color1 = 0x801500;
		color2 = 0xa68521;
	}
	
//	@Override
//	public void create() {
//		addItemToSpawn( new Torch() );
//		super.create();
//	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

    @Override
    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.55f : 0.40f, 6 );
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.55f : 0.30f, 3 );
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
                    Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );

            } else if(
                    map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                    Terrain.WALL_DECO, Terrain.WALL_DECO1, Terrain.WALL_DECO2
                );
            }
        }

        for( Room room : rooms ){
            if( room.type == Room.Type.TUNNEL ){
                for( Room.Door door : room.connected.values() ){
                    if( door.type == Room.Door.Type.TUNNEL && Random.Int( 3 ) == 0 ){

                        int pos = door.y * Level.WIDTH + door.x;

                        for( int i : Level.NEIGHBOURS4 ){
                            if( map[ pos + i ] == Terrain.CHASM ){
                                map[ pos + i ] = Terrain.STATUE_SP;
                            }
                        }
                    }
                }
            }
        }

        for (int i=WIDTH + 1; i < LENGTH - WIDTH; i++){

            if( map[ i ] == Terrain.EMPTY ){
                int n = 0;
                if( map[ i + 1 ] == Terrain.WALL ){
                    n++;
                }
                if( map[ i - 1 ] == Terrain.WALL ){
                    n++;
                }
                if( map[ i + WIDTH ] == Terrain.WALL ){
                    n++;
                }
                if( map[ i - WIDTH ] == Terrain.WALL ){
                    n++;
                }
                if( Random.Int( 6 ) <= n ){
                    map[ i ] = Terrain.EMPTY_DECO;
                }
            }
        }
		
//		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
//			if (map[i] == Terrain.EMPTY) {
//
//				int count = 0;
//				for (int j=0; j < NEIGHBOURS8.length; j++) {
//					if ((Terrain.flags[map[i + NEIGHBOURS8[j]]] & Terrain.PASSABLE) > 0) {
//						count++;
//					}
//				}
//
//				if (Random.Int( 80 ) < count) {
//					map[i] = Terrain.EMPTY_DECO;
//				}
//
//			} else if (map[i] == Terrain.WALL &&
//				map[i-1] != Terrain.WALL_DECO && map[i-WIDTH] != Terrain.WALL_DECO &&
//				map[i-1] != Terrain.WALL_DECO2 && map[i-WIDTH] != Terrain.WALL_DECO2 &&
//				Random.Int( 20 ) == 0) {
//
//				map[i] = Random.oneOf( Terrain.WALL_DECO, Terrain.WALL_DECO2 );
//
//			}
//		}

		for (Room r : rooms) {
            if (r.type == Room.Type.STANDARD) {
                for (Room n : r.neighbours) {
                    if (n.type == Room.Type.STANDARD && !r.connected.containsKey( n )) {
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
	}

    @Override
    public String tileName( int tile ) {
        return HallsLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return HallsLevel.tileDescs(tile);
    }

	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Suspiciously colored liquid";
		case Terrain.GRASS:
			return "Embermoss";
		case Terrain.HIGH_GRASS:
			return "Emberfungi";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Pillar";
		default:
			return Level.tileNames(tile);
		}
	}

	public static String tileDescs(int tile) {
		switch (tile) {
            case Terrain.WATER:
                return "Something tells you that it isn't actually water, but it works the same.";
            case Terrain.WALL_DECO:
                return "There is an candle hanging on this wall. It burns with unnatural green flame.";
            case Terrain.WALL_DECO1:
                return "A menacing skull made from stone decorates this wall panel. Spooky!";
            case Terrain.WALL_DECO2:
                return "A some kind of weird eye is engraved on this wall panel. Weird.";
            case Terrain.WALL_DECO3:
            case Terrain.WALL_DECO4:
            case Terrain.WALL_DECO5:
                return "There is a window of stained glass in this wall. It doesn't looks breakable";
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return "The pillar is made of real humanoid skulls. Awesome.";
            case Terrain.BOOKSHELF:
                return "Books in ancient languages smoulder in the bookshelf. May it contain something useful?";
            case Terrain.SHELF_EMPTY:
                return "Books in ancient languages smoulder in the bookshelf.";
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
			if ( level.map[i] == 63 ) {
				scene.add( new Stream( i ) );
			} else if (level.map[i] == Terrain.WALL_DECO) {
                scene.add( new Torch( i ) );
            }
		}
	}
	
	private static class Stream extends Group {
		
		private int pos;
		
		private float delay;
		
		public Stream( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( 2 );
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((FireParticle)recycle( FireParticle.class )).reset( 
						p.x + Random.Float( DungeonTilemap.SIZE ), 
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
		
		@Override
		public void draw() {
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
			super.draw();
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		}
	}
	
	public static class FireParticle extends PixelParticle.Shrinking {
		
		public FireParticle() {
			super();
			
			color( 0xEE7722 );
			lifespan = 1f;
			
			acc.set( 0, +80 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			speed.set( 0, -40 );
			size = 4;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}

	private static class Torch extends Emitter {

        private int pos;

        public Torch( int pos ){
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 1, p.y - 4, 2, 0 );

            pour( ElmoParticle.FACTORY, 0.15f );

            add( new Halo( 16, 0xFFFFCC, 0.2f ).point( p.x, p.y ) );
        }

        @Override
        public void update(){
            if( visible = Dungeon.visible[ pos ] ){
                super.update();
            }
        }
    }

	public static void pregenerate( Level level ) {

        int square = 6;

        for( int x = 1 ; x < Level.WIDTH - 1 ; x += square ) {
            for( int y = 1 ; y < Level.HEIGHT - 1; y += square ) {

                int xx = x + Random.Int( square ) + 1 ;
                int yy = y + Random.Int( square ) + 1 ;

                level.map[ xx * Level.WIDTH + yy ] = Random.oneOf(
//                    Terrain.WALL_DECO
                    Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );

            }
        }
    }
}
