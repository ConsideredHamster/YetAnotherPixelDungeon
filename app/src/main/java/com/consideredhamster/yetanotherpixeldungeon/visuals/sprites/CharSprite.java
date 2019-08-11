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
package com.consideredhamster.yetanotherpixeldungeon.visuals.sprites;

import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.AcidParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BloodParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SnowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.WebParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.EmoIcon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.FloatingText;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.EnragedFX;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.IceBlock;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.UnholyArmor;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Shield;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.TorchHalo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CharSprite extends MovieClip implements Tweener.Listener, MovieClip.Listener {
	
	public static final int DEFAULT		= 0xFFFFFF;
	public static final int POSITIVE	= 0x00FF00;
	public static final int NEGATIVE	= 0xFF0000;
	public static final int WARNING		= 0xFF8800;
	public static final int NEUTRAL		= 0xFFFF00;
	public static final int BLACK		= 0x444444;

	private static final float MOVE_INTERVAL	= 0.1f;
	private static final float FLASH_INTERVAL	= 0.05f;	
	
	public enum State {

        // special effects
        ILLUMINATED, UNHOLYARMOR,

        // buffs
		LEVITATING, MENDING, INVISIBLE, ENRAGED, PROTECTION,

        // elemental debuffs
        BURNING, BLIGHTED,

        // body debuffs
        POISONED, BLEEDING, WITHERED,

        // mental debuffs
        VERTIGO, CHARMED,

        // special debuffs
        CHILLED, ENSNARED,

        // magical debuffs
        DISRUPTED, CONTROLLED, BANISHED,

        // redundant effects
        FROZEN, PARALYSED, CHALLENGE,
    }
	
	protected Animation idle;
	protected Animation run;
	protected Animation attack;
	protected Animation operate;
	protected Animation search;
	protected Animation pickup;
	protected Animation cast;
    protected Animation spawn;
    protected Animation die;

	protected Callback animCallback;
	
	protected Tweener motion;

    protected Emitter mending;
    protected Emitter levitation;

    protected Emitter burning;
    protected Emitter blighted;

	protected Emitter poisoned;
	protected Emitter bleeding;
	protected Emitter withered;

    protected Emitter vertigo;
    protected Emitter charmed;

    protected Emitter chilled;
    protected Emitter ensnared;

    protected Emitter controlled;

	protected EnragedFX enraged;

	protected IceBlock iceBlock;
	protected TorchHalo halo;
	protected Shield ward;
    protected UnholyArmor unholyArmor;

	protected EmoIcon emo;
	
	private Tweener jumpTweener;
	private Callback jumpCallback;
	
	private float flashTime = 0;
	
	protected boolean sleeping = false;
	
	public Char ch;
	
	public boolean isMoving = false;
	
	public CharSprite() {
		super();
		listener = this;
	}
	
	public void link( Char ch ) {
		this.ch = ch;
		ch.sprite = this;
		
		place( ch.pos );
		turnTo(ch.pos, Random.Int(Level.LENGTH));
	}
	
	public PointF worldToCamera( int cell ) {
		
		final int csize = DungeonTilemap.SIZE;
		
		return new PointF(
			((cell % Level.WIDTH) + 0.5f) * csize - width * 0.5f,
			((cell / Level.WIDTH) + 1.0f) * csize - height
		);
	}
	
	public void place( int cell ) {
		point(worldToCamera(cell));
	}
	
	public void showStatus( int color, String text, Object... args ) {
		if (visible) {
			if (args.length > 0) {
				text = Utils.format( text, args );
			}
			if (ch != null) {
				FloatingText.show( x + width * 0.5f, y, ch.pos, text, color );
			} else {
				FloatingText.show( x + width * 0.5f, y, text, color );
			}
		}
	}
	
	public void idle() {
		play(idle);
	}

    @Override
    public void play( Animation anim, boolean force ) {
        if( curAnim != die ) {
            super.play(anim, force);
        }
    }
	
	public void move( int from, int to ) {
		play( run );
		
		motion = new PosTweener( this, worldToCamera( to ), MOVE_INTERVAL );
		motion.listener = this;
		parent.add( motion );

		isMoving = true;
		
		turnTo( from , to );
		
		ch.onMotionComplete();
	}
	
	public void interruptMotion() {
		if (motion != null) {
			onComplete(motion);
		}
	}
	
	public void attack( int cell ) {
		turnTo( ch.pos, cell );
		play(attack);
	}
	
	public void attack( int cell, Callback callback ) {
		animCallback = callback;
		turnTo( ch.pos, cell );
		play( attack );
	}
	
	public void operate( int cell ) {
		turnTo( ch.pos, cell );
		play( operate );
	}

	public void search() {
        play( search );
    }

    public void spawn() { play( spawn ); }

    public void pickup( int cell ) {
        turnTo( ch.pos, cell );
        play( pickup );
    }
	
	public void cast(int cell) {
		turnTo( ch.pos, cell );
		play( cast != null ? cast : attack );
	}

    public void cast(int cell, Callback callback ) {
        animCallback = callback;
        turnTo( ch.pos, cell );
        play( cast != null ? cast : attack );
    }
	
	public void turnTo( int from, int to ) {
		int fx = from % Level.WIDTH;
		int tx = to % Level.WIDTH;
		if (tx > fx) {
			flipHorizontal = false;
		} else if (tx < fx) {
			flipHorizontal = true;
		}
	}
	
	public void jump( int from, int to, Callback callback ) {	
		jumpCallback = callback;
		
		int distance = Level.distance( from, to );
		jumpTweener = new JumpTweener( this, worldToCamera( to ), distance * 4, distance * 0.1f );
		jumpTweener.listener = this;
		parent.add(jumpTweener);
		
		turnTo(from, to);
	}
	
	public void die() {
		sleeping = false;
		play( die );
		
		if (emo != null) {
			emo.killAndErase();
		}
	}
	
	public Emitter emitter() {
		Emitter emitter = GameScene.emitter();
		emitter.pos(this);
		return emitter;
	}
	
	public Emitter centerEmitter() {
		Emitter emitter = GameScene.emitter();
		emitter.pos(center());
		return emitter;
	}

    public Emitter topEmitter() {
        Emitter emitter = GameScene.emitter();
        emitter.pos(this);
        emitter.y = -height / 4;
        emitter.height = -height / 2;
        return emitter;
    }

    public Emitter bottomEmitter() {
        Emitter emitter = GameScene.emitter();
        emitter.pos(this);
        emitter.y = height;
        emitter.height = -height / 2;
        return emitter;
    }
	
	public void burst( final int color, int n ) {
		if (visible) {
			Splash.at( center(), color, n );
		}
	}
	
	public void bloodBurstA( PointF from, int damage ) {
		if (visible) {
			PointF c = center();
			int n = Math.min( 9 * damage / ch.HT, 9 );
			Splash.at( c, PointF.angle( from, c ), 3.1415926f / 2, blood(), n );

		}
	}
	
	public int blood() {
		return 0xFFBB0000;
	}
	
	public void flash() {
		ra = ba = ga = 1f;
		flashTime = FLASH_INTERVAL;
	}
	
	public void add( State state ) {
		switch (state) {

            case MENDING:
                if (mending == null){
                    mending = emitter();
                    mending.pour( Speck.factory( Speck.HEALING ), 0.5f );
                }
                break;

            case LEVITATING:
                if (levitation == null){
                    levitation = bottomEmitter();
                    levitation.pour( Speck.factory( Speck.JET ), 0.1f );
                }
                break;

            case INVISIBLE:

                if (parent != null) {
                    parent.add( new AlphaTweener( ch.sprite, 0.4f, 0.4f ) );
                } else {
                    alpha( 0.4f );
                }

                break;

            case ENRAGED:
                if (enraged == null){
                    enraged = EnragedFX.fury( this );
                }
                break;

            case PROTECTION:
                if (ward == null){
                    GameScene.effect( ward = new Shield( this ) );
                }
                break;


            case BURNING:
                if (burning == null){
                    burning = emitter();
                    burning.pour( FlameParticle.FACTORY, 0.06f );
                }
                break;

            case BLIGHTED:
                if (blighted == null){
                    blighted = emitter();
                    blighted.pour( AcidParticle.FACTORY, 0.3f );
                }
                break;


            case POISONED:
                if (poisoned == null){
                    poisoned = emitter();
                    poisoned.pour( Speck.factory( Speck.POISON ), 0.5f );
                }
                break;
            case BLEEDING:
                if (bleeding == null){
                    bleeding = emitter();
                    bleeding.pour( BloodParticle.FACTORY, 0.5f );
                }
                break;
            case WITHERED:
                if (withered == null){
                    withered = emitter();
                    withered.pour( ShadowParticle.UP, 0.25f );
                }
                break;

            case VERTIGO:
                if (vertigo == null){
                    vertigo = topEmitter();
                    vertigo.pour( Speck.factory( Speck.VERTIGO ), 0.5f );
                }
                break;
            case CHARMED:
                if (charmed == null){
                    charmed = emitter();
                    charmed.pour( Speck.factory( Speck.HEART ), 0.25f );
                }
                break;

            case CHILLED:
                if (chilled == null){
                    chilled = emitter();
                    chilled.pour( SnowParticle.FACTORY, 0.1f );
                }
                break;

            case ENSNARED:
                if (ensnared == null){
                    ensnared = emitter();
                    ensnared.pour( WebParticle.FACTORY, 0.5f );
                }
                break;

            case CONTROLLED:
                if (controlled == null){
                    controlled = emitter();
                    controlled.pour( Speck.factory( Speck.CONTROL ), 0.25f );
                }
                break;

            case ILLUMINATED:
                if (halo == null){
                    GameScene.effect( halo = new TorchHalo( this ) );
                }
                break;

            case UNHOLYARMOR:
                if (unholyArmor == null){
                    GameScene.effect( unholyArmor = new UnholyArmor( this ) );
                }
                break;

            case PARALYSED:
                paused = true;
                break;
            case FROZEN:
                iceBlock = IceBlock.freeze( this );
                paused = true;
                break;
		}
	}
	
	public void remove( State state ) {
		switch (state) {

            case MENDING:
                if (mending != null) {
                    mending.on = false;
                    mending = null;
                }
                break;

            case LEVITATING:
                if (levitation != null) {

                    levitation.on = false;
                    levitation = null;
                }
                break;

            case ENRAGED:
                if (enraged != null) {
                    enraged.calm();
                    enraged = null;
                }
                break;

            case PROTECTION:
                if (ward != null) {
                    ward.putOut();
                    ward = null;
                }
                break;

            case INVISIBLE:
                alpha( 1f );
                break;


            case BURNING:
                if (burning != null) {
                    burning.on = false;
                    burning = null;
                }
                break;
            case BLIGHTED:
                if (blighted != null) {
                    blighted.on = false;
                    blighted = null;
                }
                break;



            case POISONED:
                if (poisoned != null) {
                    poisoned.on = false;
                    poisoned = null;
                }
                break;

            case BLEEDING:
                if (bleeding != null) {
                    bleeding.on = false;
                    bleeding = null;
                }
                break;

            case WITHERED:
                if (withered != null) {
                    withered.on = false;
                    withered = null;
                }
                break;


            case VERTIGO:
                if (vertigo != null) {
                    vertigo.on = false;
                    vertigo = null;
                }
                break;
            case CHARMED:
                if (charmed != null) {
                    charmed.on = false;
                    charmed = null;
                }
                break;


            case CHILLED:
                if (chilled != null) {
                    chilled.on = false;
                    chilled = null;
                }
                break;

            case ENSNARED:
                if (ensnared != null) {
                    ensnared.on = false;
                    ensnared = null;
                }
                break;


            case CONTROLLED:
                if (controlled != null) {
                    controlled.on = false;
                    controlled = null;
                }
                break;

            case ILLUMINATED:
                if (halo != null) {
                    halo.putOut();
                    halo = null;
                }
                break;

            case UNHOLYARMOR:
                if (unholyArmor != null) {
                    unholyArmor.putOut();
                    unholyArmor = null;
                }
                break;

            case PARALYSED:
                paused = false;
                break;

            case FROZEN:
                if (iceBlock != null) {
                    iceBlock.melt();
                    iceBlock = null;
                }
                paused = false;
                break;

        }
	}
	
	@Override
	public void update() {
		
		super.update();
		
		if (paused && listener != null) {
			listener.onComplete( curAnim );
		}
		
		if (flashTime > 0 && (flashTime -= Game.elapsed) <= 0) {
//			resetColorAlpha();
            resetColorOnly();
		}



        if (mending != null) {
            mending.visible = visible;
        }

        if (levitation != null) {
            levitation.visible = visible;
        }

        if (burning != null) {
            burning.visible = visible;
        }

        if (blighted != null) {
            blighted.visible = visible;
        }

        if (poisoned != null) {
            poisoned.visible = visible;
        }
        if (bleeding != null) {
            bleeding.visible = visible;
        }
        if (withered != null) {
            withered.visible = visible;
        }

        if (vertigo != null) {
            vertigo.visible = visible;
        }
        if (charmed != null) {
            charmed.visible = visible;
        }

        if (chilled != null) {
            chilled.visible = visible;
        }

        if (controlled != null) {
            controlled.visible = visible;
        }

        if (unholyArmor != null) {
            unholyArmor.visible = visible;
        }

        if (enraged != null) {
            enraged.visible = visible;
        }
//        if (challenge != null) {
//            challenge.visible = visible;
//        }

		if (sleeping) {
			showSleep();
		} else {
			hideSleep();
		}
		if (emo != null) {
			emo.visible = visible;
		}
	}
	
	public void showSleep() {
		if (emo instanceof EmoIcon.Sleep) {
			
		} else {
			if (emo != null) {
				emo.killAndErase();
			}
			emo = new EmoIcon.Sleep( this );
		}
	}
	
	public void hideSleep() {
		if (emo instanceof EmoIcon.Sleep) {
			emo.killAndErase();
			emo = null;
		}
	}
	
	public void showAlert() {
		if (emo instanceof EmoIcon.Alert) {
			
		} else {
			if (emo != null) {
				emo.killAndErase();
			}
			emo = new EmoIcon.Alert( this );
		}
	}
	
	public void hideAlert() {
		if (emo instanceof EmoIcon.Alert) {
			emo.killAndErase();
			emo = null;
		}
	}

    public void showInspect() {
        if (emo instanceof EmoIcon.Inspect) {

        } else {
            if (emo != null) {
                emo.killAndErase();
            }
            emo = new EmoIcon.Inspect( this );
        }
    }

    public void hideInspect() {
        if (emo instanceof EmoIcon.Inspect) {
            emo.killAndErase();
            emo = null;
        }
    }
	
	@Override
	public void kill() {
		super.kill();
		
		if (emo != null) {
			emo.killAndErase();
			emo = null;
		}
	}
	
	@Override
	public void onComplete( Tweener tweener ) {
		if (tweener == jumpTweener) {
			
//			if (visible && Level.water[ch.pos] && !ch.flying) {
//				GameScene.ripple( ch.pos );
//			}
			if (jumpCallback != null) {
				jumpCallback.call();
			}
			
		} else if (tweener == motion) {
			
			isMoving = false;
			
			motion.killAndErase();
			motion = null;
		}
	}

	@Override
	public void onComplete( Animation anim ) {
		
		if (animCallback != null) {
			animCallback.call();
			animCallback = null;
		} else {

            if (anim == attack) {

                idle();
                ch.onAttackComplete();

            } else if (anim == cast) {

                idle();
                ch.onCastComplete();

            } else if (anim == pickup) {

                idle();
                ch.onOperateComplete();

            } else if (anim == operate) {

                idle();
                ch.onOperateComplete();

            } else if (anim == search) {

                idle();

            } else if (anim == spawn) {

                idle();

            }
			
		}
	}
	
	private static class JumpTweener extends Tweener {

		public Visual visual;
		
		public PointF start;
		public PointF end;
		
		public float height;
		
		public JumpTweener( Visual visual, PointF pos, float height, float time ) {
			super( visual, time );
			
			this.visual = visual;
			start = visual.point();
			end = pos;

			this.height = height;
		}

		@Override
		protected void updateValues( float progress ) {
			visual.point( PointF.inter( start, end, progress ).offset( 0, -height * 4 * progress * (1 - progress) ) );
		}
	}
}
