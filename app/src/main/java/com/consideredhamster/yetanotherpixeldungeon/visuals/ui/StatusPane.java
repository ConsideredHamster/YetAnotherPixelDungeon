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
package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BloodParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndGame;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndHero;

public class StatusPane extends Component {
	
	private NinePatch shield;
	private Image difficulty;
	private Image avatar;
//	private Emitter blood;
	
	private int lastTier = 0;
	private int bottom = 0;

	private Image hp;
	private Image exp;
	
	private int lastLvl = -1;
	private int lastKeys = -1;

	private BitmapText health;
	private BitmapText level;
	private BitmapText depth;
	private BitmapText keys;

	private TagDanger danger;
    private TagAttack attack;
    private TagPickup pickup;
	private TagResume resume;
	private BuffIndicator buffs;
	private Compass compass;

    private TagWaterskin btnWaterskin;
    private TagOilLantern btnOilLantern;

	private MenuButton btnMenu;

    public StatusPane( int bottom ) {
        super();
        this.bottom = bottom;
    }
	
	@Override
	protected void createChildren() {
		
		shield = new NinePatch( Assets.STATUS, 80, 0, 48, 0 );
		add( shield );
		
		add( new TouchArea( 0, 1, 30, 30 ) {
			@Override
			protected void onClick( Touch touch ) {
				Image sprite = Dungeon.hero.sprite;
				if (!sprite.isVisible()) {
					Camera.main.focusOn( sprite );
				}
				GameScene.show( new WndHero() );
			};			
		} );
		
		btnMenu = new MenuButton();
		add( btnMenu );

        difficulty = new IconDifficulty();
        add(difficulty);

        avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
        add(avatar);
		
//		blood = new Emitter();
//		blood.pos(avatar);
//		blood.pour(BloodParticle.FACTORY, 0.3f);
//		blood.autoKill = false;
//		blood.on = false;
//		addFromDamage(blood);
		
		compass = new Compass( Dungeon.level.exit );
		add( compass );
		
		hp = new Image( Assets.HP_BAR );	
		add( hp );
		
		exp = new Image( Assets.XP_BAR );
		add(exp);

        level = new BitmapText( PixelScene.font1x );
        level.hardlight(0xFFEBA4);
        add(level);

        health = new BitmapText( PixelScene.font1x );
        health.hardlight(0xCACFC2);
        add(health);
		
		depth = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.font1x );
		depth.hardlight(0xCACFC2);
		depth.measure();
		add( depth );
		
		Dungeon.hero.belongings.countIronKeys();
		keys = new BitmapText( PixelScene.font1x );
		keys.hardlight(0xCACFC2);
		add(keys);
		
		danger = new TagDanger();
		add(danger);

        attack = new TagAttack();
        add( attack );

        pickup = new TagPickup();
        add(pickup);
		
		resume = new TagResume();
		add( resume );



        btnWaterskin = new TagWaterskin();
        add(btnWaterskin);

        btnOilLantern = new TagOilLantern();
        add(btnOilLantern);
		
		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );

	}
	
	@Override
	protected void layout() {
		
		height = 32;
		
		shield.size( width, shield.height );

		avatar.x = PixelScene.align( camera(), shield.x + 15 - avatar.width / 2 );
		avatar.y = PixelScene.align( camera(), shield.y + 16 - avatar.height / 2 );
		
		compass.x = avatar.x + avatar.width / 2 - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2 - compass.origin.y;
		
		hp.x = 30;
		hp.y = 3;

		depth.x = width - 24 - depth.width() - 18;
		depth.y = 6;
		
		keys.y = 6;
		
		layoutTags( bottom );

//        if( YetAnotherPixelDungeon.buttons() ){
            btnWaterskin.setPos( 0, height + 2 );
            btnOilLantern.setPos( 0, btnWaterskin.bottom() + 2 );
//        } else {
//            btnWaterskin.setPos( width - btnWaterskin.width(), height + 10 );
//            btnOilLantern.setPos( width - btnOilLantern.width(), btnWaterskin.bottom() + 3 );
//        }

		buffs.setPos( 32, 11 );
		
		btnMenu.setPos( width - btnMenu.width(), 1 );

        difficulty.x = btnMenu.left() + btnMenu.width() / 2 - difficulty.width() / 2;
        difficulty.y = btnMenu.bottom() + 2;
	}
	
	private void layoutTags( int bottom ) {
		
		float pos = bottom - 1;
		
		if (tagDanger) {
			danger.setPos( width - danger.width(), pos - danger.height() );
			pos = danger.top() - 1;
		}

        if (tagAttack) {
            attack.setPos( width - attack.width(), pos - attack.height() );
            pos = attack.top() - 1;
        }
		
		if (tagPickup) {
			pickup.setPos( width - pickup.width(), pos - pickup.height() );
			pos = pickup.top() - 1;
		}
		
		if (tagResume) {
			resume.setPos( width - resume.width(), pos - resume.height() );
		}
	}
	
	private boolean tagDanger	= false;
    private boolean tagAttack	= false;
    private boolean tagPickup   = false;
	private boolean tagResume	= false;
	
	@Override
	public void update() {
		super.update();
		
		if (
            tagDanger != danger.visible ||
            tagAttack != attack.visible ||
            tagPickup != pickup.visible ||
            tagResume != resume.visible
        ) {

            tagDanger = danger.visible;
            tagAttack = attack.visible;
            tagPickup = pickup.visible;
            tagResume = resume.visible;
			
			layoutTags( bottom );
		}
		
		float health_percent = (float)Dungeon.hero.HP / Dungeon.hero.HT;
		
		if (health_percent == 0) {
			avatar.tint( 0x000000, 0.6f );
//			blood.on = false;
//		} else if (health_percent < 0.25f) {
//			avatar.tint( 0xcc0000, 0.4f );
//			blood.on = true;
		} else {
			avatar.resetColorAlpha();
//			blood.on = false;
		}
		
		hp.scale.x = health_percent;
		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();
		
		if (Dungeon.hero.lvl != lastLvl) {
			
			if (lastLvl != -1) {
				Emitter emitter = (Emitter)recycle( Emitter.class );
				emitter.revive();
				emitter.pos( 26, 26 );
				emitter.burst( Speck.factory( Speck.STAR ), 12 );
			}
			
			lastLvl = Dungeon.hero.lvl;
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = PixelScene.align( 26.0f - level.width() / 2 );
			level.y = PixelScene.align( 26.5f - level.baseLine() / 2 );

		}

        health.text( String.format( "%d/%d", Dungeon.hero.HP, Dungeon.hero.HT ) );
        health.measure();
        health.x = PixelScene.align( 53.0f - health.width() / 2 );
        health.y = PixelScene.align( 4.0f - health.baseLine() / 2 );
        health.alpha( 0.5f );

		int k = IronKey.curDepthQuantity;
		if (k != lastKeys) {
			lastKeys = k;
			keys.text(Integer.toString(lastKeys));
			keys.measure();
			keys.x = width - 8 - keys.width()    - 18;
		}

		
		int tier = Dungeon.hero.appearance();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}
	
	private static class MenuButton extends Button {
		
		private Image image;
		
		public MenuButton() {
			super();
			
			width = image.width + 4;
			height = image.height + 4;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			image = new Image( Assets.STATUS, 114, 3, 12, 11 );
			add( image );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			image.x = x + 2;
			image.y = y + 2;
		}
		
		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}
		
		@Override
		protected void onTouchUp() {
			image.resetColorAlpha();
		}
		
		@Override
		protected void onClick() {
			GameScene.show( new WndGame() );
		}
	}
}
