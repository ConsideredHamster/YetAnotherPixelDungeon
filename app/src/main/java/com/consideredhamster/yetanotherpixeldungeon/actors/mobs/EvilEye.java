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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.DeathRay;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.PurpleParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.EyeSprite;
import com.watabou.utils.Random;

public class EvilEye extends MobRanged {
	
	private static final String TXT_DEATHGAZE_KILLED = "%s's deathgaze killed you...";

    public EvilEye() {

        super( 11 );

		name = "evil eye";
		spriteClass = EyeSprite.class;
		
		flying = true;
		
//		loot = new Dewdrop();
//		lootChance = 0.5f;

	}
	
//	private int hitCell;
	
//	@Override
//	protected boolean canAttack( Char enemy ) {
//
//        if( isCharmedBy( enemy ) )
//            return false;
//
//		hitCell = Ballistica.cast( pos, enemy.pos, true, false );
//
//		for (int i=1; i < Ballistica.distance; i++) {
//			if (Ballistica.trace[i] == enemy.pos) {
//				return true;
//			}
//		}
//		return false;
//	}

    @Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && (enemySeen || enemy != null && detected( enemy ))) {
			return getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return !Level.adjacent( pos, enemy.pos ) && !isCharmedBy( enemy ) &&
                Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        Sample.INSTANCE.play(Assets.SND_RAY);

        sprite.parent.add( new DeathRay( sprite.center(), DungeonTilemap.tileCenterToWorld( cell ) ) );

        onCastComplete();

        super.onRangedAttack( cell );

    }

    @Override
    public boolean cast( Char enemy ) {

        boolean terrainAffected = false;

        for (int i=1; i < Ballistica.distance ; i++) {

            int pos = Ballistica.trace[i];

            int terr = Dungeon.level.map[pos];

            if (terr == Terrain.DOOR_CLOSED) {

                Level.set(pos, Terrain.EMBERS);
                GameScene.updateMap(pos);
                terrainAffected = true;

            } else if (terr == Terrain.HIGH_GRASS) {

                Level.set( pos, Terrain.GRASS );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

//            Heap heap = Dungeon.level.heaps.get( pos );
//            if (heap != null) {
//                heap.disintegrate( true );
//            }

            Char ch = Actor.findChar( pos );

            if (ch != null) {

                if (hit(this, ch, false, true)) {

                    ch.damage(damageRoll(), this, DamageType.ENERGY);

                    if (Dungeon.visible[pos]) {
                        ch.sprite.flash();
                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                    }

//                    if (!ch.isAlive() && ch == Dungeon.hero) {
//                        Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name), Dungeon.depth));
//                        GLog.n(TXT_DEATHGAZE_KILLED, name);
//                    }
                } else {
                    ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        return true;
    }

//	@Override
//	public float attackSpeed() {
//		return 1f;
//	}
	
//	@Override
//	protected boolean doAttack( Char enemy ) {
//
//		spend( attackDelay() );
//
//		boolean rayVisible = false;
//
//		for (int i=0; i < Ballistica.distance; i++) {
//			if (Dungeon.visible[Ballistica.trace[i]]) {
//				rayVisible = true;
//                hitCell = Ballistica.trace[i];
//			}
//		}
//
//		if (rayVisible) {
//			sprite.attack( hitCell );
//			return false;
//		} else {
//			attack( enemy );
//			return true;
//		}
//	}
	
//	@Override
//	public boolean attack( Char enemy ) {
//
//        boolean terrainAffected = false;
//
//		for (int i=1; i < Ballistica.distance ; i++) {
//
//			int pos = Ballistica.trace[i];
//
//            int terr = Dungeon.level.map[pos];
//            if (terr == Terrain.DOOR_CLOSED ) {
//
//                Level.set(pos, Terrain.EMBERS);
//                GameScene.updateMap(pos);
//                terrainAffected = true;
//
//            } else if (terr == Terrain.HIGH_GRASS) {
//
//                Level.set( pos, Terrain.GRASS );
//                GameScene.updateMap( pos );
//                terrainAffected = true;
//
//            }
//
////            Heap heap = Dungeon.level.heaps.get( pos );
////            if (heap != null) {
////                heap.disintegrate( true );
////            }
//
//			Char ch = Actor.findChar( pos );
//
//			if (ch != null) {
//
//                if (hit(this, ch, false, true)) {
//                    ch.damage(damageRoll(), this, DamageType.ENERGY);
//
//                    if (Dungeon.visible[pos]) {
//                        ch.sprite.flash();
//                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
//                    }
//
////                    if (!ch.isAlive() && ch == Dungeon.hero) {
////                        Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name), Dungeon.depth));
////                        GLog.n(TXT_DEATHGAZE_KILLED, name);
////                    }
//                } else {
//                    ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
//                }
//            }
//		}
//
//        if (terrainAffected) {
//            Dungeon.observe();
//        }
//
//		return true;
//	}

//    @Override
//    public void add( Buff buff ) {
//        if (buff instanceof Blindness) {
//            damage( Random.NormalIntRange( HT / 2 , HT ), null, null );
//        }
//        super.add( buff );
//    }
	
	@Override
	public String description() {
		return
			"One of this creature's other names is \"orb of hatred\", because when it sees an enemy, " +
			"it uses its deathgaze recklessly, often ignoring its allies and wounding them.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Energy.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }
}
