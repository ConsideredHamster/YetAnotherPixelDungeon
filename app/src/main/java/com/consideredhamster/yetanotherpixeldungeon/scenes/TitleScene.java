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
package com.consideredhamster.yetanotherpixeldungeon.scenes;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndChangelog;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.BannerSprites;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Fireball;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Archs;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ExitButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.PrefsButton;

public class TitleScene extends PixelScene {

    private static final String TXT_PLAY		= "Play";
    private static final String TXT_HIGHSCORES	= "Rankings";
    private static final String TXT_BADGES		= "Badges";
    private static final String TXT_ABOUT		= "About";

    private static final int COLOR_NORMAL		= 0x444444;
    private static final int COLOR_BRIGHT	    = 0xCACFC2;

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.play( Assets.TRACK_MAIN_THEME, true );
        Music.INSTANCE.volume(1f);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        Image title = BannerSprites.get( BannerSprites.Type.TITLE_LOGO);

        float height = title.height +
                (YetAnotherPixelDungeon.landscape() ? DashboardItem.SIZE : DashboardItem.SIZE * 2 );

        title.x = (w - title.width()) / 2;
        title.y = (h - height) / 2;

        add( title );

        Image signs = new Image( BannerSprites.get( BannerSprites.Type.TITLE_LOGO_RUNES) ) {
            private float time = 0;
            @Override
            public void update() {
                super.update();
                am = (float)Math.sin( -(time += Game.elapsed / 2) );
            }
            @Override
            public void draw() {
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
                super.draw();
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
            }
        };
        signs.x = title.x;
        signs.y = title.y;
        add( signs );

        Image title2 = BannerSprites.get( BannerSprites.Type.YET_ANOTHER);
        add( title2 );

        title2.x = title.x + ( title.width() - title2.width() ) / 2;
        title2.y = title.y + ( title.height() - title2.height() ) / 2;

        Image signs2 = new Image( BannerSprites.get( BannerSprites.Type.YET_ANOTHER_RUNES) ) {
            private float time = 0;
            @Override
            public void update() {
                super.update();
                am = (float)Math.sin( -(time += Game.elapsed / 2) );
            }
            @Override
            public void draw() {
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
                super.draw();
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
            }
        };
        signs2.x = title2.x;
        signs2.y = title2.y;
        add( signs2 );

        placeTorch( title.x + 24, title.y + 12 );
        placeTorch( title.x + title.width - 24, title.y + 12 );

        DashboardItem btnBadges = new DashboardItem( TXT_BADGES, 3 ) {
            @Override
            protected void onClick() {
                YetAnotherPixelDungeon.switchNoFade(BadgesScene.class);
            }
        };
        add( btnBadges );

        DashboardItem btnAbout = new DashboardItem( TXT_ABOUT, 1 ) {
            @Override
            protected void onClick() {
                YetAnotherPixelDungeon.switchNoFade(AboutScene.class);
            }
        };
        add( btnAbout );

        DashboardItem btnPlay = new DashboardItem( TXT_PLAY, 0 ) {
            @Override
            protected void onClick() {
                YetAnotherPixelDungeon.switchNoFade(StartScene.class);
            }
        };
        add( btnPlay );

        DashboardItem btnHighscores = new DashboardItem( TXT_HIGHSCORES, 2 ) {
            @Override
            protected void onClick() {
                YetAnotherPixelDungeon.switchNoFade(RankingsScene.class);
            }
        };
        add( btnHighscores );

        if (YetAnotherPixelDungeon.landscape()) {
            float y = (h + height) / 2 - DashboardItem.SIZE / 2;
            btnHighscores	.setPos( w / 2 - btnHighscores.width(), y );
            btnBadges		.setPos( w / 2, y );
            btnPlay			.setPos( btnHighscores.left() - btnPlay.width(), y );
            btnAbout		.setPos( btnBadges.right(), y );
        } else {
            btnBadges.setPos( w / 2 - btnBadges.width(), (h + height) / 2 - DashboardItem.SIZE / 2 );
            btnAbout.setPos( w / 2, (h + height) / 2 - DashboardItem.SIZE / 2 );
            btnPlay.setPos( w / 2 - btnPlay.width(), btnAbout.top() - DashboardItem.SIZE );
            btnHighscores.setPos( w / 2, btnPlay.top() );
        }

        final BitmapText version = new BitmapText( "v " + Game.version, font1x );
        version.measure();
        version.hardlight(COLOR_NORMAL);
        version.x = w - version.width();
        version.y = h - version.height();
        add( version );

        final Emitter emitter = new Emitter();
        emitter.pos( version.x, version.y, version.width(), version.height() );
        add(emitter);

        TouchArea changelog = new TouchArea( version ) {
            @Override
            protected void onClick( Touchscreen.Touch touch ) {

                YetAnotherPixelDungeon.lastVersion( Game.versionNum );

                emitter.on = false;
                version.hardlight( COLOR_NORMAL );
                parent.add( new WndChangelog() );

            }
        };
        add(changelog);

        PrefsButton btnPrefs = new PrefsButton();
        btnPrefs.setPos( 0, 0 );
        add(btnPrefs);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        fadeIn();

        if( YetAnotherPixelDungeon.lastVersion() < Game.versionNum ) {
            YetAnotherPixelDungeon.lastVersion( Game.versionNum );
            add(new WndChangelog());
        }
    }

    private void placeTorch( float x, float y ) {
        Fireball fb = new Fireball();
        fb.setPos( x, y );
        add( fb );
    }

    private static class DashboardItem extends Button {

        public static final float SIZE	= 48;

        private static final int IMAGE_SIZE	= 32;

        private Image image;
        private BitmapText label;

        public DashboardItem( String text, int index ) {
            super();

            image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
            this.label.text( text );
            this.label.measure();

            setSize( SIZE, SIZE );
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image( Assets.DASHBOARD );
            add( image );

            label = createText( 9 );
            add( label );
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = align( x + (width - image.width()) / 2 );
            image.y = align( y );

            label.x = align( x + (width - label.width()) / 2 );
            label.y = align( image.y + image.height() +2 );
        }

        @Override
        protected void onTouchDown() {
            image.brightness( 1.5f );
            Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
        }

        @Override
        protected void onTouchUp() {
            image.resetColorAlpha();
        }
    }
}
