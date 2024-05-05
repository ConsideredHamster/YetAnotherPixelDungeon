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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.BombHazard;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Gold;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Shurikens;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.TenguSprite;
import com.watabou.utils.Random;

public class Tengu extends MobRanged {

	private static final int JUMP_DELAY = 18;

	private final static int LINE_GREETINGS = 0;
    private final static int LINE_JUMPCLOSER = 1;
    private final static int LINE_JUMPFURTHER = 2;
    private final static int LINE_HIDEAWAY = 3;
    private final static int LINE_REVEALED = 4;
    private final static int LINE_SHOWINGUP = 5;
    private final static int LINE_NEARDEATH = 6;
    private final static int LINE_LOOKINGFOR = 7;
    private final static int LINE_DISCOVERED = 8;

    private final static String[][] LINES = {

            {
                    "Welcome to my humble abode! Haha!",
                    "Welcome! Do you want to play?",
                    "Oh, you're finally here! Let's play!",
            },
            {
                    "Let's get a bit closer...",
                    "Hello there!",
                    "Now you're it!",
                    "Haha! Got you!",
                    "BOO!.. Hahaha!",
            },
            {
                    "Let's put a little distance...",
                    "Here is a little present for you!",
                    "Don't stand there! Come here!",
                    "Oops, didn't catch me! Haha!",
                    "Here, catch! Hahaha!",
            },
            {
                    "Well, I need to rest a little. Have fun!",
                    "I am afraid I'll have to leave you with these guys for now.",
                    "Wow, you're tough! Here, play with these guys for a while.",
                    "Here is your company for now! Enjoy!",
                    "What fun! I just need to rest for a while.",
            },
            {
                    "Oh! You found me! How wonderful!",
                    "Wow! Are you so eager to continue our little game?",
                    "Hey! I haven't finished catching my breath!",
            },
            {
                    "Hey, guess what? I'm back!",
                    "Hello again! Hope you weren't bored!",
                    "Here and back again! Shall we continue?",
                    "I'm here! What took you so long?",
                    "Heeeeere's Tengu! Hahaha!",
            },
            {
                    "Well... That... Was fun...",
                    "I think... Our game has ended...",
                    "Well played, seeker... Well played.",
                    "I think I am done... Thank you, seeker.",
                    "Huh? It seems that I've lost... Oh well.",
            },
            {
                    "Where are you?",
                    "Are you here? Or here?",
                    "Why are you hiding from me?",
                    "Hey, stop hiding! That's my thing!",
                    "Show yourself! Stop ruining the fun!",
            },
            {
                    "Hey! Found you!",
                    "A-ha! Here you are!",
                    "That was a nice try!",
                    "Can't hide forever from me!",
                    "You are not really good at hiding.",
            }
    };

    private int timeToJump = 0;
    protected int breaks = 0;

    public Tengu() {

        super( 3, 15, true );

        name = Dungeon.depth == Statistics.deepestFloor ? "Tengu" : "memory of Tengu";
        info = "Boss enemy!";

        spriteClass = TenguSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
    }

    @Override
    public float attackSpeed() {
        return isRanged() ? super.attackSpeed() : super.attackSpeed() * 2;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Shurikens(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack(cell);
    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (!rooted ) {
            if ( enemy != null ) {
                if ( canSeeTarget( enemy ) ) {
                    yell(LINE_JUMPCLOSER);
                    jumpCloser();
                } else {
                    if( enemy.stealth() <= 0 || Random.Float() < 0.1 / enemy.stealth() ) {

                        if( enemy.invisible == 0 ) {
                            yell( LINE_DISCOVERED );
                        }

                        jumpCloser();

                    } else {

                        if( Random.Int( 10 ) == 0 ) {
                            yell( LINE_LOOKINGFOR );
                        }

                        jumpAway();

                    }
                }
            } else {
                jumpAway();
            }
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		timeToJump++;

		if ( !rooted && timeToJump >= ( JUMP_DELAY - breaks * 2 ) ) {
            if( enemy != null && !Level.adjacent( pos, enemy.pos ) ) {
                jumpCloser();
                yell( LINE_JUMPCLOSER );
            } else {
                yell( LINE_JUMPFURTHER );
                jumpAway();
            }

			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if( buff( Invisibility.class ) != null ) {
            yell( LINE_REVEALED );
        }

        timeToJump++;

        super.damage( dmg, src, type );
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            yell( LINE_HIDEAWAY );
            GLog.i( "Tengu disappears somewhere!" );

            breaks++;
            hideAway();

            return true;

        } else if( buff( Invisibility.class ) != null ) {

            if( shadowCount() > 0 ) {

                heal( 1 );
                sprite.idle();
                spend( TICK );
                next();

                return true;

            } else {

                Invisibility.dispel( this );

                enemy = Dungeon.hero;
                jumpCloser();

                yell( LINE_SHOWINGUP );

                return true;

            }

        } else {

            timeToJump++;

        }

        return super.act();
    }

    private void jumpCloser() {

        timeToJump = 0;
        int newPos = pos;

        ArrayList<Integer> cells = new ArrayList<>();

        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if (pos != cell && Level.adjacent( enemy.pos, cell ) ) {
                cells.add(cell);
            }
        }

        if (!cells.isEmpty()) {
            newPos = Random.element(cells);

            if( shadowCount() < breaks && canSeeTarget( enemy ) ) {
                spawnShadow(pos);
            }
        }

        if( Dungeon.visible[ pos ] ){
            CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            Sample.INSTANCE.play( Assets.SND_PUFF );
        }

        if( enemy != null ) {
            beckon( enemy.pos );
        }

        sprite.move( pos, newPos );
        move( newPos );

        spend( 1 / moveSpeed() );
    }

    private void jumpAway() {

        timeToJump = 0;
        int newPos = pos;

        ArrayList<Integer> cells = new ArrayList<>();

        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if ( pos != cell && !Level.adjacent( pos, cell ) && Level.distance( pos, cell ) <= 6 ) {
                cells.add(cell);
            }
        }

        if (!cells.isEmpty()) {
            newPos = Random.element(cells);

            if( Level.adjacent( pos, enemy.pos ) && canSeeTarget( enemy ) ) {
                spawnBomb(pos);
            }
        }

        if( Dungeon.visible[ pos ] ){
            CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            Sample.INSTANCE.play( Assets.SND_PUFF );
        }

        sprite.move( pos, newPos );
        move( newPos );

        spend( 1 / moveSpeed() );
    }

    private void spawnBomb( int cell ) {

        BombHazard hazard = new BombHazard();
        hazard.setValues( cell, BombHazard.BOMB_NINJA, Random.Int( 1, 3 + breaks ), 0 );
        GameScene.add( hazard );
        ( (BombHazard.BombSprite) hazard.sprite ).appear();

    }

    private void spawnShadow( int cell ) {

        final TenguShadow clone = new TenguShadow();

        clone.pos = cell;
//        clone.HT = clone.HP = 1;
        clone.EXP = 0;

        clone.state = clone.HUNTING;
        clone.target = target;

        clone.beckon( target );

        sprite.turnTo( pos, cell );

        GameScene.add( clone, TICK );
        clone.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

    }

    private void hideAway() {

        Debuff.removeAll( this );
        BuffActive.add( this, Invisibility.class, HT - HP );

        jumpAway();
        clearShadows();

        ArrayList<Integer> cells = new ArrayList<>();
        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if ( pos != cell && !Level.adjacent( pos, cell ) && Level.distance( pos, cell  ) <= 4 ) {
                cells.add(cell);
            }
        }

        for( int i = 0 ; i < breaks + 1 ; i++ ) {
            if (!cells.isEmpty()) {
                Integer cell = Random.element(cells);
                cells.remove(cell);
                spawnShadow(cell);
            }
        }
    }

    private int shadowCount() {
        int result = 0;

        for( Mob mob : Dungeon.level.mobs ) {
            if( mob instanceof TenguShadow ) {
                result++;
            }
        }

        return result;
    }

    private void clearShadows() {

        for( Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone() ) {
            if( mob instanceof TenguShadow ) {
                mob.die( null, null );
            }
        }
    }

    @Override
    public void notice() {
        super.notice();

        if ( enemySeen && HP == HT && breaks == 0 ) {
            yell( LINE_GREETINGS );
        }
    }

    private void yell( int line ) {
        yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
    }

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        yell( LINE_NEARDEATH );
        clearShadows();

        super.die( cause, dmg );

        GameScene.bossSlain();
        Badges.validateBossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

    }
	
	@Override
	public String description() {
		return
			"Tengu are members of the ancient assassins clan, which is also called Tengu. " +
			"These assassins are noted for extensive use of martial arts and shadow magic.";
	}

    private static final String TIME_TO_JUMP	= "timeToJump";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( TIME_TO_JUMP, timeToJump );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        timeToJump = bundle.getInt( TIME_TO_JUMP );
    }
}
