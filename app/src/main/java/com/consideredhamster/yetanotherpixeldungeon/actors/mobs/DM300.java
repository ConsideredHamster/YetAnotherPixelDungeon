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

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Rockfall;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Thunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.BombHazard;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.BossWarning;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.CavesBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.AlarmTrap;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.LaserRay;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Lightning;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.DM300Sprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DM300 extends MobHealthy {

    protected int breaks = 0;
    protected int lastMark = -1;

    public DM300() {

        super( 4, 20, true );

        // 450

        dexterity /= 2; // yes, we divide it again
        armorClass *= 2; // and yes, we multiply it back

        name = Dungeon.depth == Statistics.deepestFloor ? "DM-300" : "DM-400";
        info = "Boss enemy!";
        spriteClass = DM300Sprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Physical.class, Element.Resist.PARTIAL );

        resistances.put( Element.Doom.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );
    }

    @Override
    public float awareness(){
        return 1.0f;
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if ( buff( Enraged.class ) != null ) {

            dmg = dmg / 2 + Random.Int( 1 + dmg % 2 );

        }

        super.damage( dmg, src, type );
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( enemy != null && buff( Enraged.class ) != null ) {
            Pushing.knockback( enemy, pos, 1, damage );
        }

        return damage;
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged && isAlive() ) {

            yell( "STATUS: REPAIR REQUIRED" );
            yell( "REQUESTING ADDITIONAL SUPPORT" );
            GLog.i("DM-300 is not enraged anymore.");

            summonAdds();
            clearMarks();

            state = SLEEPING;

            ((DM300Sprite)sprite).smokingDefault();
            spend( TICK );

        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT && lastMark == -1 ) {

            breaks++;

            BuffActive.add(this, Enraged.class, Random.Int( 8, 12 ) * 2.0f );

            if (Dungeon.visible[pos]) {
                yell( "TARGET STATUS: STILL ALIVE" );
                yell( "INITIATING DEMOLITION PROTOCOL" );
                GLog.n( "DM-300 is enraged!" );
            }

            sprite.idle();
            ((DM300Sprite)sprite).smokingCharging();

            spend( TICK );
            return true;
        }

        if( lastMark > -1 ){

            ArrayList<BossWarning> marks = new ArrayList<>();

            for( Hazard hazard : Dungeon.level.hazards ){
                if( hazard instanceof BossWarning ){
                    marks.add( (BossWarning) hazard );
                }
            }

            if( !marks.isEmpty() ){

                BossWarning mark = marks.get( 0 );

                switch( lastMark ){

                    case BossWarning.VAR_SHOCK:

                        createNova();

                        for( BossWarning m : marks ){
                            m.destroy();
                        }

                        lastMark = -1;
                        spend( TICK );
                        return true;

                    case BossWarning.VAR_CHARGE:

                        performTrampling( mark.pos );
                        mark.destroy();

                        lastMark = -1;
                        spend( TICK );
                        return true;

                    case BossWarning.VAR_LASER:

                        shootLasers( mark.pos );
                        mark.destroy();

                        if( marks.size() <= 1 ){
                            lastMark = -1;
                            spend( TICK );
                            return true;
                        } else {
                            return false;
                        }

                    case BossWarning.VAR_BOMBS:

                        throwBomb( mark.pos );
                        mark.destroy();

                        if( marks.size() <= 1 ){
                            lastMark = -1;
                            spend( TICK );
                            return true;
                        } else {
                            return false;
                        }


                }
            }

        } else if( enemy != null && Level.fieldOfView[ enemy.pos ] && Ballistica.cast( pos, enemy.pos, false, false) == enemy.pos ) {

            if( buff( Enraged.class ) != null ) {

                if( Level.distance( pos, enemy.pos ) > 1 ){
                    sprite.turnTo( pos, enemy.pos );

                    // one bomb is always spawned, it's the most accurate one
                    BossWarning.spawn( enemy.pos, BossWarning.VAR_BOMBS );

                    // chance for bonus bomb increases from 0% to 50% to 100% with every enrage
                    if( Random.Int( 3 ) < breaks - 1 ) {

                        ArrayList<Integer> candidates = new ArrayList<Integer>();

                        for (int n : Level.NEIGHBOURS8) {
                            int cell = enemy.pos + n;
                            if (!Level.solid[cell] && Level.distance( pos, cell ) > 1 ) {
                                candidates.add(cell);
                            }
                        }

                        if (candidates.size() > 0) {

                            int o = Random.Int( candidates.size() );
                            BossWarning.spawn( candidates.get( o ), BossWarning.VAR_BOMBS );

                        }
                    }

                    lastMark = BossWarning.VAR_BOMBS;
                    Dungeon.hero.interrupt();

                    spend( TICK );
                    return true;

                } else {

                    // if target is up close, then ignore bombs and just hit it
                    super.act();

                }

            } else if( Random.Int( 8 ) <= breaks ) {

                if( Level.distance( pos, enemy.pos ) < 2 ){

                    for( int n : Level.NEIGHBOURS8 ){
                        BossWarning.spawn( pos + n, BossWarning.VAR_SHOCK );
                    }

                    lastMark = BossWarning.VAR_SHOCK;
                    Dungeon.hero.interrupt();

                    yell( "TARGET STATUS: CLOSE RANGE" );
                    yell( "INITIATING DISCHARGE PROTOCOL" );

                    spend( TICK );
                    return true;

            } else if( Level.distance( pos, enemy.pos ) <= 4 && !rooted ) {

                    ( (DM300Sprite) sprite ).smokingCharging();

                    sprite.turnTo( pos, enemy.pos );
                    BossWarning.spawn( enemy.pos, BossWarning.VAR_CHARGE );

                    lastMark = BossWarning.VAR_CHARGE;
                    Dungeon.hero.interrupt();

                    yell( "TARGET STATUS: MEDIUM RANGE" );
                    yell( "INITIATING TRAMPLING PROTOCOL" );

                    spend( TICK );
                    return true;

            } else if( Level.distance( pos, enemy.pos ) <= 8 ) {

                    if( Dungeon.visible[ pos ] ){
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_WHITE, 15 );
                    }

                    sprite.turnTo( pos, enemy.pos );
                    BossWarning.spawn( enemy.pos, BossWarning.VAR_LASER );

                    for( int n : Level.NEIGHBOURS8 ) {
                        if( Random.Int( 4 ) < breaks && Level.distance( pos, enemy.pos ) == Level.distance( pos, enemy.pos + n ) ) {
                            BossWarning.spawn( enemy.pos + n, BossWarning.VAR_LASER );
                        }
                    }

                    lastMark = BossWarning.VAR_LASER;
                    Dungeon.hero.interrupt();

                    yell( "TARGET STATUS: DISTANT RANGE" );
                    yell( "INITIATING BEAMCUTTING PROTOCOL" );

                    spend( TICK );
                    return true;

                }
            }
        }

        return super.act();
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

        yell( "StATUs: CRiTIcAL" );
        yell( "SHuTtiNg downnn..." );

        clearMarks();

        for( Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone() ) {
            if( mob instanceof DM100 ) {
                mob.die( null, null );
            }
        }

        super.die( cause, dmg );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        Badges.validateBossSlain();

	}

	@Override
	public void notice() {
		super.notice();
        if( enemySeen && HP == HT && breaks == 0 ) {
            yell("UNAUTHORIZED PERSONNEL DETECTED");
            yell("INITIATING DEFENSE PROTOCOL");
        }
	}
	
	@Override
	public String description() {
		return
			"This machine was created by the Dwarves several centuries ago. Later, Dwarves started to replace machines with " +
			"golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-300 and similar " +
			"machines were typically used for construction and mining, and in some cases, for city defense.";
	}

    private static final String BREAKS	= "breaks";
    private static final String LAST_MARK	= "mark";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( LAST_MARK, lastMark );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        lastMark = bundle.getInt( LAST_MARK );
    }

    private void performTrampling( int target ) {

        int distance = 4 + Random.Int( 5 - breaks );

        Actor.freeCell( pos );

        Ballistica.cast( pos, target, true, true );
        Ballistica.distance = Math.min( Ballistica.distance, distance );

        final int newPos = Ballistica.trace[ Ballistica.distance ];
        final Char ch = findChar( newPos );

        Pushing.push( this, target, distance, 0, new Callback() {
            @Override
            public void call(){

                if( ch != null ) {

                    int dmg = Char.absorb( damageRoll() + damageRoll(), ch.armorClass() );
                    ch.damage( dmg, DM300.this, Element.PHYSICAL );
                    Camera.main.shake( 3, 0.2f );

                } else if( Level.solid[ newPos ] ) {

                    damage( damageRoll(), null, Element.PHYSICAL );
                    spend( TICK );

                    Camera.main.shake( 4, 0.3f );
                    Sample.INSTANCE.play( Assets.SND_ROCKS, 1.0f, 1.0f, 0.5f );

                    if( Dungeon.level.map[ newPos ] == Terrain.STATUE || Dungeon.level.map[ newPos ] == Terrain.GRATE ){

                        if( Dungeon.level instanceof CavesBossLevel && newPos != ((CavesBossLevel)Dungeon.level).arenaDoor ){

                            Level.set( newPos, Terrain.EMPTY_DECO );
                            GameScene.updateMap( newPos );

                        }
                    }

                    for( int n : Level.NEIGHBOURS9 ) {

                        if( n == 0 || Random.Int( 2 ) == 0 ) {

                            int p = newPos + n;

                            if( p != newPos ){
                                Rockfall.affect( p, damageRoll(), DM300.this );
                            }
                        }
                    }

                    for( int n : Level.NEIGHBOURS12 ) {

                        if( Random.Int( 3 ) == 0 ) {

                            Rockfall.affect( newPos + n, damageRoll(), DM300.this );

                        }
                    }
                }

                ((DM300Sprite)sprite).smokingDefault();
            }
        } );
    }

    private void createNova() {

        if( Level.water[ pos ] ) {

            HashSet<Char> affected = Thunderstorm.spreadFrom( pos );

            if( affected != null && !affected.isEmpty() ) {
                for( Char ch : affected ) {
                    int power = damageRoll() + damageRoll() / 2;
                    ch.damage( this == ch ? power : power / 2, this, Element.SHOCK );
                }
            }

        } else {

            for( int n : Level.NEIGHBOURS8 ) {

                int cell = pos + n;

                Char ch = Actor.findChar( cell );

                if( ch != null ) {
                    ch.damage( damageRoll() + damageRoll() / 2, this, Element.SHOCK );
                }

                if( Dungeon.visible[ cell ] ) {
                    sprite.parent.add( new Lightning( pos, cell ) );
                }
            }
        }
    }

    private void throwBomb( final int target ) {

        sprite.attack( target, new Callback() {
            @Override
            public void call(){

                ( (MissileSprite) sprite.parent.recycle( MissileSprite.class ) ).
                        reset( pos, target, new DM300Bomb(), new Callback() {
                            @Override
                            public void call(){
                                BombHazard hazard = new BombHazard();
                                hazard.setValues( target, BombHazard.BOMB_ROUND, damageRoll(), 1 );
                                GameScene.add( hazard );
                                ( (BombHazard.BombSprite) hazard.sprite ).appear();

                                sprite.idle();
                                next();
                            }
                        } );
            }
        });
    }

    private void shootLasers( final int target ) {

        sprite.attack( target, new Callback() {
            @Override
            public void call() {

                boolean terrainAffected = false;

                int reflectFrom = Ballistica.cast( pos, target, true, false );

                sprite.parent.add( new LaserRay( pos, reflectFrom ) );
                Sample.INSTANCE.play(Assets.SND_RAY);

                for (int i=1; i <= Ballistica.distance ; i++) {

                    int pos = Ballistica.trace[i];

                    int terr = Dungeon.level.map[pos];

                    if (terr == Terrain.HIGH_GRASS) {

                        Level.set( pos, Terrain.GRASS );
                        GameScene.updateMap( pos );
                        terrainAffected = true;

                    }

                    Char ch = Actor.findChar( pos );

                    if (ch != null) {

                        ch.damage( damageRoll() / 2, DM300.this, Element.ENERGY );
                    }

                    if (Dungeon.visible[pos]) {
                        CellEmitter.center(pos).burst( ElmoParticle.BURST, Random.IntRange( 2, 4 ) );
                    }

                }

                if (terrainAffected) {
                    Dungeon.observe();
                }

                sprite.idle();
                next();
            }
        }  );
    }

    private void summonAdds() {

        for( int i = 0; i < breaks ; i++ ) {

            int cellToSpawn = 0;
            int curDistance = 0;

            for( Integer cell : Dungeon.level.getPassableCellsList() ){

                if( Dungeon.level.map[ cell ] == Terrain.INACTIVE_TRAP ) {

                    int distance = Level.distance( pos, cell );

                    if( cellToSpawn == 0 || distance < curDistance ) {
                        cellToSpawn = cell;
                        curDistance = distance;
                    }
                }
            }

            if( cellToSpawn > 0 ) {

                DM100 summon = new DM100();

                summon.pos = cellToSpawn;
                summon.target = pos;
                summon.EXP = 0;

                Dungeon.level.press( summon.pos, summon );

                GameScene.add( summon, TICK );

                if ( Dungeon.visible[ summon.pos ] ) {
                    summon.sprite.alpha( 0 );
                    summon.sprite.parent.add( new AlphaTweener( summon.sprite, 1, 0.5f ) );
                    summon.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.01f, 15 );
                }

                summon.state = summon.HUNTING;
                summon.sprite.idle();
            }
        }

        AlarmTrap.trigger( pos, null );
    }

    private void clearMarks() {

        lastMark = -1;

        for( Hazard hazard : (HashSet<Hazard>)Dungeon.level.hazards.clone() ){
            if( hazard instanceof BossWarning ){
                hazard.destroy();
            }
        }
    }

    public static class DM300Bomb extends Item {
        {
            image = ItemSpriteSheet.BOMB_ROUND;
        }
    }
}