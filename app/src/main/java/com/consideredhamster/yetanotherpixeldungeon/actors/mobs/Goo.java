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

import java.util.ArrayList;
import java.util.HashMap;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GooSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Goo extends MobEvasive {

	private static final float PUMP_UP_DELAY	= 2f;

    private static final float SPLIT_DELAY	= 1f;

    private static final int SPAWN_HEALTH = 15;

    public boolean phase = false;

    protected int breaks = 0;

    public Goo() {

        super(2, 10, true);

        name = "Goo";
        info = "Boss enemy!";

        spriteClass = GooSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        dexterity /= 2;
        armorClass = 0;

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );

        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );

    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public float awareness(){
        return state != SLEEPING ? super.awareness() : 0.0f ;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

	@Override
	public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if( buff( Enraged.class ) != null ) {

            dmg /= 2;

        } else if ( type == Element.PHYSICAL && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH ) ) {

            GooSpawn clone = GooSpawn.split( this, dmg );
            clone.mother = this;

        }

        super.damage( dmg, src, type );
	}

    @Override
    public boolean act() {

        if (( state == SLEEPING || Level.water[pos] ) && HP < HT && !phase ) {

            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
            HP++;

            if( HP >= HT ) {
                beckon( Dungeon.hero.pos );
                Dungeon.hero.interrupt( "You were awoken by a bad feeling." );
                GLog.i("Goo awakens!");
            }
        }

        if( phase ) {

            GameScene.add( Blob.seed(pos, 150 + breaks * 50, Miasma.class) );

            if( buff( Enraged.class ) == null ) {

                phase = false;

                state = SLEEPING;

                Blob blob = Dungeon.level.blobs.get( Miasma.class );

                if (blob != null) {
                    blob.remove();
                }

                for (int i = Random.Int(2) ; i < breaks + 1 ; i++) {

                    int pos = ((SewerBossLevel) Dungeon.level).getRandomSpawnPoint();

                    if( pos > 0 ) {

                        GooSpawn clone = new GooSpawn();

                        clone.HP = clone.HT;
                        clone.pos = pos;
                        clone.mother = this;
                        clone.state = clone.HUNTING;
                        clone.phase = true;
                        clone.EXP = 0;

                        clone.beckon(pos);

                        if (Dungeon.level.map[clone.pos] == Terrain.DOOR_CLOSED) {
                            Door.enter(clone.pos);
                        }

                        Dungeon.level.press(clone.pos, clone);

                        GameScene.add(clone, SPLIT_DELAY);

                        if (Dungeon.visible[clone.pos]) {
                            clone.sprite.alpha(0);
                            clone.sprite.parent.add(new AlphaTweener(clone.sprite, 1, 0.5f));
                        }

                        clone.sprite.idle();
                    }
                }

                if (Dungeon.visible[pos]) {
                    sprite.showStatus(CharSprite.DEFAULT, "sleeping...");
                    GLog.i("Goo is exhausted!");
                }

                sprite.idle();
                spend( PUMP_UP_DELAY );

            } else {

                spend( TICK );

            }

            return true;

        } else if( state != SLEEPING ) {

            if ( 3 - breaks > 4 * HP / HT ) {

                breaks++;

                phase = true;

                GameScene.add( Blob.seed( pos, 100, Miasma.class ) );

                BuffActive.add( this, Enraged.class, breaks * Random.Float( 5.0f, 6.0f ) );

                for ( Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone() ) {
                    if ( mob instanceof GooSpawn ) {
                        ( (GooSpawn) mob ).phase = true;
                        mob.sprite.idle();
                    }
                }

                if ( Dungeon.visible[ pos ] ) {
//                    sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                    GLog.n( "Goo starts releasing deadly miasma!" );
                }

                sprite.idle();

                spend( TICK );
                return true;

            }
        }

        return super.act();
    }

    @Override
    public void die( Object cause, Element dmg ) {

        yell( "glurp... glurp..." );

        super.die(cause, dmg);

        ((SewerBossLevel)Dungeon.level).unseal();

        GameScene.bossSlain();

        Badges.validateBossSlain();

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof GooSpawn) {
                mob.die( cause, null );
            }
        }

        Blob blob = Dungeon.level.blobs.get( Miasma.class );

        if (blob != null) {
            blob.remove();
        }
    }

    @Override
    public void notice() {
        super.notice();
        if( enemySeen ) {
            yell("GLURP-GLURP!");
        }
    }

    @Override
    public String description() {
        return  "Little is known about The Goo. It's quite possible that it is not even a creature, but rather a " +
                "conglomerate of substances from the sewers that gained some kind of rudimentary, but very evil " +
                "sentience.";
    }

    private static final String PHASE	= "phase";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
        breaks = bundle.getInt( BREAKS );
    }
}
