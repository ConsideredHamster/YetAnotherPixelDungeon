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

import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.GoldenKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.Painter;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.AlarmTrap;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

import java.util.ArrayList;

public class SewerBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;

//        viewDistance = 8;
	}
	
//	private int stairs = 0;

    private static final int TOP			= 3;
    private static final int ARENA_WIDTH    = 11;
    private static final int ARENA_HEIGHT   = 11;
    private static final int CHAMBER_HEIGHT	= 3;

    private static final int LEFT	= (WIDTH - ARENA_WIDTH) / 2;
    private static final int CENTER_X = LEFT + ARENA_WIDTH / 2;
    private static final int CENTER_Y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT / 2;
    private static final int CENTER = CENTER_Y * WIDTH + CENTER_X;

    private static final int DOOR1 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT ;
    private static final int DOOR2 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT + ARENA_WIDTH - 1;

    private int progress = 0;

    private static final int BOSS_ISHIDDEN = 0;
    private static final int BOSS_APPEARED = 1;
    private static final int BOSS_DEFEATED = 2;


//    private boolean bossDefeated = false;

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

    @Override
    protected boolean build() {

        Painter.fill(this, LEFT, TOP, ARENA_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY);
        Painter.fill(this, LEFT, TOP + CHAMBER_HEIGHT, ARENA_WIDTH, 1, Terrain.WALL_DECO);

//        int left = pedestal( true );
//        int right = pedestal( false );
//        map[left] = map[right] = Terrain.PEDESTAL;
//        for (int i=left+1; i < right; i++) {
//            map[i] = Terrain.EMPTY_SP;
//        }

        entrance = (TOP + 1) * WIDTH + CENTER_X;
        map[entrance] = Terrain.ENTRANCE;

        exit = (TOP - 1) * WIDTH + CENTER_X;
        map[exit] = Terrain.LOCKED_EXIT;

//        int sign = ( TOP - 1 ) * WIDTH + CENTER_X + 1;
//        map[sign] = Terrain.WALL_SIGN;

        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, ARENA_WIDTH, ARENA_HEIGHT, Terrain.WATER );
        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );
        Painter.fill( this, LEFT + ARENA_WIDTH - 1, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );

        int x = LEFT;
        int y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
        while (x < LEFT + ARENA_WIDTH) {
            map[(y - ARENA_HEIGHT) * WIDTH + x] = x % 2 == 0 ? Terrain.WALL : Terrain.WALL_DECO;
            map[y * WIDTH - WIDTH + x] = x % 2 == 0 ? Terrain.STATUE_SP : Terrain.WALL;
            map[y * WIDTH + x] = Terrain.WATER;
            x++;
        }

        map[ DOOR1 ] = Terrain.LOCKED_DOOR;
        map[ DOOR2 ] = Terrain.LOCKED_DOOR;

        Painter.fill(this, 0, TOP + CHAMBER_HEIGHT + ARENA_HEIGHT + 1, WIDTH, HEIGHT - TOP - CHAMBER_HEIGHT - ARENA_HEIGHT - 1, Terrain.CHASM);

        Painter.fill( this, CENTER_X - 1, CENTER_Y - 1, 3, 3, Terrain.EMPTY );

        Painter.fill( this, CENTER_X - 3, CENTER_Y - 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X + 3, CENTER_Y - 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X - 3, CENTER_Y + 3, 1, 1, Terrain.STATUE );
        Painter.fill( this, CENTER_X + 3, CENTER_Y + 3, 1, 1, Terrain.STATUE );

        return true;
    }

    @Override
    protected void decorate() {

//        for (int i=0; i < LENGTH; i++) {
//            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
//                map[i] = Terrain.EMPTY_DECO;
//            } else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
//                map[i] = Terrain.WALL_DECO;
//            }
//        }
    }

    @Override
    protected void createMobs() {
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {

        drop( new IronKey(), TOP * WIDTH + CENTER_X - 1 ).type = Heap.Type.CHEST;

        drop( new SkeletonKey(), CENTER ).type = Heap.Type.LOCKED_CHEST;

        drop( new GoldenKey(), CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;

//        drop( new Gold().random(), CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;

        Item item = Bones.get();
        if (item != null) {
            drop( item, CENTER + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ] ).type = Heap.Type.BONES;
        }
    }

    @Override
    public ArrayList<Integer> getPassableCellsList() {

        ArrayList<Integer> result = new ArrayList<>();

        for( Integer cell : super.getPassableCellsList() ){
            if( progress != BOSS_ISHIDDEN && insideArena( cell ) || progress != BOSS_APPEARED && beforeArena( cell ) ){
                result.add( cell );
            }
        }

        return result;
    }

    private boolean insideArena(int cell) {
        return !beforeArena(cell) && !outOfArena(cell);
    }

    private boolean beforeArena(int cell) {
        return cell / WIDTH < TOP + CHAMBER_HEIGHT;
    }

    private boolean outOfArena(int cell) {
        return cell / WIDTH >= TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
    }

    @Override
    public void press( int cell, Char hero ) {

        super.press(cell, hero);

        if ( progress == BOSS_ISHIDDEN && cell == CENTER ) {

            Heap chest = Dungeon.level.heaps.get( cell );

            if( chest.type != Heap.Type.LOCKED_CHEST ) {

                AlarmTrap.trigger( cell, null );
                set(cell, Terrain.INACTIVE_TRAP);
                GameScene.updateMap(cell);

                progress = BOSS_APPEARED;

                Mob boss = Bestiary.mob(Dungeon.depth);
                GLog.i("The chest was trapped! Security system locks the doors!");
                GLog.i("\nSomething wicked comes this way, attracted by the sound of alarm!");

                boss.pos = getRandomSpawnPoint();
                boss.state = boss.HUNTING;

                GameScene.add(boss, 2f);
                boss.beckon(cell);

                if (Dungeon.visible[boss.pos]) {
                    boss.sprite.alpha(0);
                    boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.5f));
                }

                seal();

                Dungeon.observe();

                Music.INSTANCE.play( currentTrack(), true );
            }
        }
    }

    @Override
    public Heap drop( Item item, int cell ) {

        // FIXME

        if ( progress == BOSS_APPEARED && item instanceof Gold ) {

            progress = BOSS_DEFEATED;

            Music.INSTANCE.play( currentTrack(), true );

            unseal();

            Dungeon.observe();
        }

        return super.drop( item, cell );
    }

    public int getRandomSpawnPoint() {

        ArrayList<Integer> list = new ArrayList<>();

        for (int i=0; i < 5; i++) {
            int pos = (TOP + CHAMBER_HEIGHT + 1) * WIDTH + LEFT + i * 2 + 1;

            if( Actor.findChar( pos ) == null ) {
                list.add( pos );
            }
        }

        return list.size() > 0 ? (int)(Random.oneOf( list.toArray() ) ) : 0;
    }

    public void seal() {

        set( DOOR1, Terrain.LOCKED_DOOR );
        GameScene.updateMap( DOOR1 );

        set( DOOR2, Terrain.LOCKED_DOOR );
        GameScene.updateMap( DOOR2 );

	}

	public void unseal() {

        set( DOOR1, Terrain.DOOR_CLOSED );
        GameScene.updateMap( DOOR1 );

        set( DOOR2, Terrain.DOOR_CLOSED );
        GameScene.updateMap( DOOR2 );

        Dungeon.observe();
	}

	@Override
	public String tileName( int tile ) {
		return SewerLevel.tileNames( tile );
	}

	@Override
	public String tileDesc( int tile ) {
        return SewerLevel.tileDescs(tile);
	}

    @Override
    public void addVisuals( Scene scene ) {
        SewerLevel.addVisuals( this, scene );
    }

    @Override
    public int nMobs() {
        return 0;
    }

    @Override
    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

    private static final String PROGRESS	= "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PROGRESS, progress );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        progress = bundle.getInt( PROGRESS );
    }
}
