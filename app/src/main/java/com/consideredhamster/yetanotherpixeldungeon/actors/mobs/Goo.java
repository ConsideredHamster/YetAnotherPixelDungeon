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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.SewerBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GooSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class Goo extends MobEvasive {

	private static final float PUMP_UP_DELAY	= 2f;

    private static final float SPLIT_DELAY	= 1f;

    private static final int SPAWN_HEALTH = 8;

    public boolean phase = false;

    public Goo() {

        super(2, 10, true);

        armorClass = 0;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
        resistances.put(Element.Body.class, Element.Resist.IMMUNE);

    }

    public boolean isMagical() {
        return true;
    }

    private static Goo mother() {
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Mother) {
                return (Goo)mob;
            }
        }

        return null;
    }

    private static final String PHASE	= "phase";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
    }

//    @Override
//    public DamageType damageType() {
//        return DamageType.ACID;
//    }

	@Override
	public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if (
                type == Element.ENERGY || type == Element.SHOCK
                || type == null && buff( Frozen.class ) != null
        ) {

            dmg += Random.IntRange( 0, dmg );

        }

        if( buff( Enraged.class ) != null ) {

            dmg /= 2;

        } else if ( type == null && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH * 3 ) ) {

            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;

            for (int n : Level.NEIGHBOURS8) {
                if (passable[pos + n] && Actor.findChar(pos + n) == null) {
                    candidates.add(pos + n);
                }
            }

            if (candidates.size() > 0) {

                Spawn clone = new Spawn();

                Burning buff1 = buff( Burning.class );

                if ( buff1 != null) {
                    BuffActive.addFromDamage( clone, Burning.class, buff1.getDuration() );
                }

                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                clone.HT = dmg;

                if( Dungeon.difficulty == Difficulties.NORMAL ) {
                    clone.HP = Random.NormalIntRange( 1, clone.HT / 2 );
                } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
                    clone.HP = clone.HT / 2;
                } else {
                    clone.HP = 1;
                }

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR_CLOSED) {
                    Door.enter(clone.pos);
                }

                Dungeon.level.press(clone.pos, clone);

                GameScene.add(clone, SPLIT_DELAY);

                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );
            }
        }

        super.damage( dmg, src, type );
	}
	
	@Override
	public String description() {
		return
			"Little is known about The Goo. It's quite possible that it is not even a creature, but rather a " +
			"conglomerate of substances from the sewers that gained some kind of rudimentary, but very evil " +
            "sentience.";
	}

    public static class Mother extends Goo {

        public Mother() {

            super();

            name = "Goo";
            spriteClass = GooSprite.class;

            loot = Gold.class;
            lootChance = 4f;

            dexterity /= 2;

        }

        protected int breaks = 0;

        private static final String BREAKS	= "breaks";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( BREAKS, breaks );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            breaks = bundle.getInt( BREAKS );
        }

        @Override
        public float awareness(){
            return state != SLEEPING ? super.awareness() : 0.0f ;
        }

        @Override
        protected float healthValueModifier() { return 0.25f; }

        @Override
        public boolean act() {

            if (( state == SLEEPING || Level.water[pos] ) && HP < HT && !phase && buff( Frozen.class ) == null ) {
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                HP++;

                if( HP >= HT ) {
                    beckon( Dungeon.hero.pos );
                    Dungeon.hero.interrupt( "You were awoken by a bad feeling." );
                    GLog.i("Goo awakens!");
                }
            }

            if( phase ) {

                GameScene.add( Blob.seed(pos, 100, Miasma.class) );

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

                            Spawn clone = new Spawn();

                            clone.HT = SPAWN_HEALTH;

                            if( Dungeon.difficulty == Difficulties.NORMAL ) {
                                clone.HT = Random.NormalIntRange( clone.HT, clone.HT * 2);
                            } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
                                clone.HT = clone.HT * 2;
                            }

                            clone.HP = clone.HT;
                            clone.pos = pos;
                            clone.state = clone.HUNTING;
                            clone.phase = true;

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

                    spend(PUMP_UP_DELAY);
                    return true;
                }

            } else if( state != SLEEPING && 3 - breaks > 4 * HP / HT ) {

                breaks++;

                phase = true;

                GameScene.add(Blob.seed(pos, 100, Miasma.class));

                BuffActive.add(this, Enraged.class, breaks * Random.Float( 5.0f, 10.0f ) );

                for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                    if (mob instanceof Spawn) {
                        ((Spawn)mob).phase = true;
                        mob.sprite.idle();
                    }
                }

                if (Dungeon.visible[pos]) {
                    sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                    GLog.n("Goo is enraged!");
                }

//                Camera.main.shake( 2 + breaks, 0.3f );

                sprite.idle();

                spend( PUMP_UP_DELAY );
                return true;
            }

            return super.act();
        }

        @Override
        public void die( Object cause, Element dmg ) {

            super.die(cause, dmg);

	    	((SewerBossLevel)Dungeon.level).unseal();

            GameScene.bossSlain();

            Badges.validateBossSlain();

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if (mob instanceof Spawn) {
                    mob.die( cause, null );
                }
            }

            Blob blob = Dungeon.level.blobs.get( Miasma.class );

            if (blob != null) {
                blob.remove();
            }

            yell( "glurp... glurp..." );
        }

        @Override
        public void notice() {
            super.notice();
            if( enemySeen ) {
                yell("GLURP-GLURP!");
            }
        }

    }

    public static class Spawn extends Goo {

        public Spawn() {

            super();

            name = "spawn of Goo";
            spriteClass = GooSprite.SpawnSprite.class;

            minDamage /= 2;
            maxDamage /= 2;

            EXP = 0;

        }

        private Goo mother;

        @Override
        public int dexterity() {
            return !phase ? super.dexterity() : 0 ;
        }

        @Override
        public boolean act() {

            if( mother == null ) {
                mother = mother();
            }

            if ( phase && mother != null && mother != this && Level.adjacent( pos, mother.pos ) ) {

                Burning buff1 = buff( Burning.class );

                if ( buff1 != null) {
                    BuffActive.addFromDamage( mother, Burning.class, buff1.getDuration() );
                }

                int heal = Math.min( HP, mother.HT - mother.HP );

                if( heal > 0 ) {
                    mother.sprite.showStatus(CharSprite.POSITIVE, "%d", heal);
                    mother.sprite.emitter().burst( Speck.factory( Speck.HEALING ), (int)Math.sqrt( heal ) );
                    mother.HP += heal;
                }

                Actor.addDelayed(new Pushing(this, pos, mother.pos), -1);

//                sprite.alpha(1);
                sprite.parent.add(new AlphaTweener(sprite, 0.0f, 0.1f));

                sprite.showStatus(CharSprite.NEGATIVE, "absorbed");

                GLog.n("An entranced spawn was absorbed by the Goo, restoring its health!");

                die( this );

                return true;

            }

            if ( Level.water[pos] && HP < HT && buff( Frozen.class ) == null ) {
//                sprite.showStatus(CharSprite.POSITIVE, "%d", heal);
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                HP ++;
            }

            if( !phase && HP == HT ) {
                phase = true;
                sprite.idle();

                sprite.showStatus(CharSprite.NEGATIVE, "entranced");
                GLog.n("A spawn of Goo has fully recovered and became entranced!");

                spend( TICK );
                return true;
            }

            return super.act();
        }

        @Override
        protected boolean getCloser( int target ) {
            return phase && mother != null ?
                    super.getCloser( mother.pos ) :
                    super.getCloser( target );
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return !phase && super.canAttack( enemy );
        }
    }
}
