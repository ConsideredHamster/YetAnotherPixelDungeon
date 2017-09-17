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
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Bleeding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.UnholyArmor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.effects.BlobEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.effects.LifeDrain;
import com.consideredhamster.yetanotherpixeldungeon.effects.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.levels.CityBossLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.KingSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.UndeadSprite;
import com.watabou.utils.Bundle;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class DwarvenKing extends MobPrecise {

    private final static int LINE_GREETINGS = 0;
    private final static int LINE_AWAKENING = 1;
    private final static int LINE_THREATENED = 2;
    private final static int LINE_CHANNELING = 3;
    private final static int LINE_ANNOYANCE = 4;
    private final static int LINE_EMPOWERED = 5;
    private final static int LINE_MOVEASIDE = 6;
    private final static int LINE_NEARDEATH = 7;

    private final static String TXT_ENRAGED = "Dwarven King is enraged!";
    private final static String TXT_STOPPED = "Dwarven King stopped the ritual.";
    private final static String TXT_CALMDWN = "Dwarven King is not enraged anymore.";
    private final static String TXT_SUMMONS = "Dwarven King casts some powerful spell!";
    private final static String TXT_CHANNEL = "Dwarven King starts some kind of ritual!";

    private static String[][] LINES = {

            {
                    "How dare you!",
                    "Who dares to disturb my slumber?",
                    "Human? In MY throne room?",
            },
            {
                    "Arise, slaves!",
                    "I command you to fight!",
                    "Servants! It is time to feast!",
            },
            {
                    "You'll pay for that, maggot!",
                    "Your death will be VERY painful...",
                    "Time to teach you a lesson, mortal.",
                    "You are going to be a very powerful slave.",
            },
            {
                    "Come to me, my minions!",
                    "Behold my true power!",
                    "To me, my vassals!",
                    "Your king commands you!",
                    "Raise from your graves!",
            },
            {
                    "They are as useless in death as they were in life.",
                    "These sycophants are worthless pile of trash.",
                    "I always have to do everything by myself.",
            },
            {
                    "Time to remove you from the board, mortal.",
                    "You played this game for too long, mortal.",
                    "Do you not know your death when you see it? Die now!",
                    "Your screams will be a symphony for my ears.",
                    "You'll pay for everything you've done, human.",
            },
            {
                    "Move aside, worm!",
                    "Don't make me angry, little pest.",
                    "You will not stop me this way.",
            },
            {
                    "You cannot kill me, " + Dungeon.hero.heroClass.title() + "... I am... immortal... ",
                    "I will return, " + Dungeon.hero.heroClass.title() + "... I will... return...",
                    "No. NO! How it can be? Killed... by... a mortal...",
            },
    };


    private static final float SPAWN_DELAY	= 1f;
    private static final float BASE_ENRAGE	= 5f;

    //FIXME

    private static final BoneExplosion EXPLOSION  = new BoneExplosion();
    private static final KnockBack KNOCKBACK  = new KnockBack();

    public static class BoneExplosion {}
    public static class KnockBack {}

    public DwarvenKing() {

        super( 5, 25, true );

        // 500

//        minDamage = 0;
//        maxDamage = 0;

        name = Dungeon.depth == Statistics.deepestFloor ? "King of Dwarves" : "undead King of Dwarves";
        spriteClass = KingSprite.class;

        loot = Gold.class;
        lootChance = 4f;
    }

    public int breaks = 0;
    public boolean phase = false;

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

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public int dexterity() {
        return buff( UnholyArmor.class ) == null ? super.dexterity() : 0 ;
    }

    @Override
    public int armourAC() {
        return buff( UnholyArmor.class ) == null ? super.armourAC() : 0;
    }

    @Override
    protected boolean getCloser( int target ) {
        return phase ?
                super.getCloser( CityBossLevel.pedestal() ) :
                super.getCloser( target );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) && ( !phase || ( buff( Enraged.class ) != null ) );
    }

    @Override
    public boolean act() {

        int throne = Dungeon.level instanceof CityBossLevel ? CityBossLevel.pedestal() : 0;

//        if( Dungeon.level instanceof CityBossLevel ) {
//            GameScene.add(Blob.seed(CityBossLevel.throne( true ), 1, Fire.class));
//            GameScene.add(Blob.seed(CityBossLevel.throne( false ), 1, Fire.class));
//        }

        if( Dungeon.hero.isAlive() ) {

            if (buff(UnholyArmor.class) != null) {

                sprite.cast(throne, null);
                spend(TICK);
                return true;

            } else if (throne > 0 && phase) {

                if (Level.adjacent(pos, throne) && findChar(throne) != null) {

                    yell( LINE_MOVEASIDE );
                    GLog.w( TXT_ENRAGED );

                    Buff.affect( this, Enraged.class, TICK );
                    enemy = findChar(throne);

//                    return super.act();

                } else if (pos == throne) {

                    sprite.cast(throne, null);
                    sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
                    Sample.INSTANCE.play(Assets.SND_CHALLENGE);

                    sacrificeAllMinions();

                    awakenWells();

                    if (breaks > 0) {

                        Buff.affect(this, UnholyArmor.class, armorDuration( breaks ) );
                        yell( LINE_CHANNELING );
                        GLog.w( TXT_CHANNEL );

                        Buff.detach(this, Withered.class);
                        Buff.detach(this, Burning.class);
                        Buff.detach(this, Ooze.class);
                        Buff.detach(this, Poison.class);
                        Buff.detach(this, Bleeding.class);
                        Buff.detach(this, Blindness.class);
                        Buff.detach(this, Charm.class);
                        Buff.detach(this, Ensnared.class);
                        Buff.detach(this, Confusion.class);

                    } else {

                        yell( LINE_AWAKENING );
                        GLog.w( TXT_SUMMONS );
                        phase = false;

                    }

                    Buff.detach(this, Enraged.class);
                    spend(TICK);

                    return true;
                }

            } else if (!phase && 3 - breaks > 4 * HP / HT) {

                breaks++;
                phase = true;

                Buff.detach(this, Enraged.class);
                yell( LINE_THREATENED );

                spend(TICK);
                return true;

            } else {

                phase = false;
                return super.act();

            }
        }

        return super.act();
    }

    @Override
    public boolean cast( Char enemy ) {

        Blob blob = Dungeon.level.blobs.get( Spawner.class );

        if( blob != null ) {
            for (int well : CityBossLevel.wells()) {
                if (blob.cur[well] > 1) {
                    GameScene.add(Blob.seed(well, -1, Spawner.class));
                }
            }
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Undead) {
                mob.target = pos;
                Buff.affect( mob, Enraged.class, TICK );
            }
        }

        return true;
    }

    @Override
    public void damage( int dmg, Object src, DamageType type ) {

        UnholyArmor buff = buff(UnholyArmor.class);

        if (buff != null) {
//            if( src instanceof Char ) {

                Char ch = Dungeon.hero;

                ch.sprite.showStatus(CharSprite.NEGATIVE, "reflected");
                ch.damage( dmg, null, DamageType.MIND );

//                if( Level.adjacent( pos, ch.pos) ) {
//                    knockBack(ch, dmg);
//                }

//                new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( ch.sprite, 2f );
//            }

            dmg = dmg / 4 + Random.Int( 1 + dmg % 4 );

        } else if( buff( Enraged.class ) != null ) {

            dmg = dmg / 2 + Random.Int( 1 + dmg % 2 );

        }

        super.damage(dmg, src, type);
    }


    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( enemy != null && !enemy.immovable() && buff( Enraged.class ) != null ) {
            Camera.main.shake(1, 0.1f);
            damage = knockBack( enemy, damage);
        }

        return damage;
    }

    @Override
    public void remove( Buff buff ) {
        if( buff instanceof UnholyArmor ) {
            phase = false;

            int duration = ((UnholyArmor) buff).consumed();

            if( duration > 0 ) {
                Buff.affect( this, Enraged.class, BASE_ENRAGE + TICK * duration );
                yell( LINE_EMPOWERED );
                GLog.w( TXT_ENRAGED );
                spend( TICK );
            } else {
                new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );
                yell( LINE_ANNOYANCE );
                GLog.w( TXT_STOPPED );
                spend( TICK );
            }
        } else if( buff instanceof Enraged ) {
            GLog.w( TXT_CALMDWN );
        }

        super.remove( buff );
    }
	
	@Override
	public void die( Object cause, DamageType dmg ) {

        yell( LINE_NEARDEATH );
        Camera.main.shake(3, 0.5f);
        new Flare( 6, 48 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 3f );

//		Dungeon.bonus.drop( new ArmorKit(), pos ).sprite.drop();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead) {
                mob.die( cause, null );
            }
        }

        Blob blob = Dungeon.level.blobs.get( Spawner.class );

        if (blob != null) {
            blob.remove();
        }

        Badges.validateBossSlain();
        GameScene.bossSlain();

        super.die( cause, dmg );

	}
	
	@Override
	public void notice() {
		super.notice();

        if( enemySeen && HP == HT && breaks == 0 ) {
            yell( LINE_GREETINGS );
        }
	}
	
	@Override
	public String description() {
		return
			"The last king of dwarves was known for his deep understanding of processes of life and death. " +
			"He has persuaded members of his court to participate in a ritual, that should have granted them " +
			"eternal youthfulness. In the end he was the only one, who got it - and an army of undead " +
			"as a bonus.";
	}

    public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(DamageType.Unholy.class);
        RESISTANCES.add(DamageType.Body.class);

        IMMUNITIES.add(DamageType.Mind.class);
        IMMUNITIES.add(DamageType.Frost.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }
	
	public static class Undead extends MobPrecise {

		public DwarvenKing king = null;
		public int well = 0;

        public Undead() {

            super( 5, 0, false );

			name = "undead dwarf";
			spriteClass = UndeadSprite.class;

//            minDamage += tier;
//            maxDamage += tier;

            minDamage /= 2;
            maxDamage /= 2;

//            dexterity /= 2;
            armorClass /= 2;
		}

        private static final String WELL	= "well";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( WELL, well );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            well = bundle.getInt( WELL );
        }

        @Override
        public int dexterity() {
            return buff( Enraged.class ) == null ? super.dexterity() : 0 ;
        }

        @Override
        public int armourAC() {
            return buff( Enraged.class ) == null ? super.armourAC() : 0;
        }

        @Override
        public boolean act() {

            if( king == null ) {
                king = king();
//            } else if (!enemySeen) {
//                beckon(king.pos);
            }

            if( buff( Enraged.class ) != null && king != null && king.isAlive() && Level.adjacent( pos, king.pos ) ) {

                int healthRestored = Math.min( Random.IntRange( HP / 3, HP / 2 ), king.HT - king.HP );

                king.HP += healthRestored;

                if (king.sprite.visible) {

                    sprite.parent.add( new LifeDrain( pos, king.pos, null ) );

                    king.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);

                    king.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));

                }

                UnholyArmor buff = king.buff( UnholyArmor.class );

                if( buff != null ) {

                    buff.consumed( king.breaks );
                    sprite.showStatus(CharSprite.NEGATIVE, "sacrificed");
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );

                }

                die( king );

                return true;
            }
            return super.act();
        }


        @Override
        protected boolean getCloser( int target ) {
            return buff( Enraged.class ) != null && king != null && king.isAlive() ?
                    super.getCloser( king.pos ) :
                    super.getCloser( target );
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return super.canAttack( enemy ) && buff( Enraged.class ) == null;
        }

		@Override
		public int attackProc( Char enemy, int damage, boolean blocked ) {

            if( damage * 2 > Random.Int( enemy.HT ) ) {

                Withered buff = Buff.affect(enemy, Withered.class);

                if (buff != null) {
                    buff.prolong();
                    enemy.sprite.burst(0x000000, 5);
                }
            }

			return damage;
		}

		
		@Override
		public void
        die( Object cause, DamageType dmg ) {

            if( buff( Enraged.class ) != null && king != null && king.isAlive() && cause != king ) {
                for (int n : Level.NEIGHBOURS8) {

                    int p = pos + n;

                    Char ch = findChar(p);

                    if (ch != null && ch.isAlive()) {
                        ch.damage(Char.absorb(damageRoll(), ch.armorClass() / 2), EXPLOSION, null);
                    }
                }

                sprite.emitter().burst( FlameParticle.FACTORY, 10 );
            }

            if( well > 0 && king != null) {
                GameScene.add( Blob.seed( well, spawnDelay( king.breaks ), Spawner.class ) );
            }

			if (Dungeon.visible[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
                sprite.emitter().burst( Speck.factory( Speck.BONE ), 6 );
			}

            super.die( cause, dmg );
		}
		
		@Override
		public String description() {
			return
				"These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
				"They appear as stocky skeletons with unusually tiny skulls.";
		}

        public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
        public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

        static {
            RESISTANCES.add(DamageType.Unholy.class);
            RESISTANCES.add(DamageType.Frost.class);
            RESISTANCES.add(DamageType.Acid.class);

            IMMUNITIES.add(DamageType.Mind.class);
            IMMUNITIES.add(DamageType.Body.class);
        }

        @Override
        public HashSet<Class<? extends DamageType>> resistances() {
            return RESISTANCES;
        }

        @Override
        public HashSet<Class<? extends DamageType>> immunities() {
            return IMMUNITIES;
        }
	}

    public static class Spawner extends Blob {

        public Spawner() {
            super();

            name = "bone pit";
        }

        @Override
        protected void evolve() {

            int from = WIDTH + 1;
            int to = Level.LENGTH - WIDTH - 1;

            for (int pos=from; pos < to; pos++) {

                if (cur[pos] > 0) {

                    cur[pos]--;

                    if( cur[ pos ] <= 0 ) {

                        if( !spawnSkeleton( pos ) ) {
                            cur[ pos ] = 1;
                        };
                    }
				}

                volume += ( off[pos] = cur[pos] );

            }
        }

        public void seed( int cell, int amount ) {
            if (cur[cell] == 0) {
                volume += amount;
                cur[cell] = amount;
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use(emitter);
            emitter.start( Speck.factory(Speck.RATTLE), 0.1f, 0 );
        }

        @Override
        public String tileDesc() {
            return "Bones on the bottom of this well are moving. Creepy.";
        }
    }

    private void sacrificeAllMinions() {
        int healthRestored = 0;

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead ) {

                healthRestored += Random.IntRange( mob.HP / 3, mob.HP / 2 );

                if (sprite.visible || mob.sprite.visible) {
                    sprite.parent.add( new LifeDrain( mob.pos, pos, null ) );
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( mob.sprite, 2f );
                }

                mob.die( this );
            }
        }

        healthRestored = Math.min( healthRestored, HT - HP );

        if( healthRestored > 0 ) {

            HP += healthRestored;

            if (sprite.visible) {
                sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(healthRestored));
                sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));
            }
        }
    }

    private void awakenWells() {
        int dormant = 3 - breaks;
        ArrayList<Integer> wells = new ArrayList<>();
        Blob blob = Dungeon.level.blobs.get(Spawner.class);

        for (int well : CityBossLevel.wells()) {
            if (blob == null || blob.cur[well] <= 0) {
                wells.add(well);
            } else if (blob.cur[well] > 0) {
                GameScene.add(Blob.seed(well, spawnDelay(breaks), Spawner.class));
            }
        }

        while (wells.size() > dormant) {

            Integer w = Random.element(wells);

            GameScene.add(Blob.seed(w, spawnDelay(breaks), Spawner.class));
            wells.remove(w);

        }
    }

    private int knockBack( Char enemy, int damage ) {

        int newPos = enemy.pos + enemy.pos - pos;

        int bonus = damageRoll() / 2;

        if ( Level.solid[newPos] ) {

            damage += bonus;

        } else {

            Char ch = Actor.findChar(newPos);

            if( ch != null ) {

                ch.damage( Char.absorb( bonus, ch.armorClass() ), this, null );
                enemy.damage( Char.absorb( bonus, enemy.armorClass() ), this, null );

            }

            if( ch == null || !ch.isAlive() ) {

                Actor.addDelayed(new Pushing(enemy, enemy.pos, newPos), -1);

                Actor.freeCell(enemy.pos);
                enemy.pos = newPos;
                Actor.occupyCell(enemy);

                Dungeon.level.press(newPos, enemy);

            }
        }

        return damage;
    }

    private static boolean spawnSkeleton( int pos ) {

        if( Actor.findChar(pos) != null ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            if (candidates.size() > 0) {
                pos = candidates.get( Random.Int( candidates.size() ) );
            } else {
                return false;
            }
        }

        Undead clone = new Undead();

        clone.pos = pos;
        clone.well = pos;

        clone.king = king();
        clone.state = clone.HUNTING;

        if( clone.king != null )
            clone.HT = clone.HP = 15 + clone.king.breaks * 5;

        Dungeon.level.press(clone.pos, clone);
        Sample.INSTANCE.play(Assets.SND_CURSED);

        GameScene.add(clone, SPAWN_DELAY);

        if (Dungeon.visible[clone.pos]) {

            clone.sprite.alpha(0);
            clone.sprite.parent.add(new AlphaTweener(clone.sprite, 1, 0.5f));
            clone.sprite.emitter().start(Speck.factory(Speck.RAISE_DEAD), 0.01f, 15);

        }

        clone.sprite.idle();

        return true;
    }

    private static DwarvenKing king() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof DwarvenKing) {
                return (DwarvenKing)mob;
            }
        }

        return null;
    }

    private static int spawnDelay( int breaks ) {
        return Random.IntRange( 10, 15 ) - Random.IntRange( breaks * 1, breaks * 2 ); // 10-15 8-12 6-9 4-6
    }

    private static float armorDuration( int breaks ) {
        return 5 + breaks * 5 + Random.Int( 6 );
    }

    private void yell( int line ) {
        yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
    }
}
