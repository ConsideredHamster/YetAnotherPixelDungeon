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

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.ResultDescriptions;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.watabou.utils.Random;

public class AmuletScene extends PixelScene {

	private static final String TXT_EXIT	= "Take me back home!";
	private static final String TXT_STAY	= "I am not done yet";
	
	private static final int WIDTH			= 120;
	private static final int BTN_HEIGHT		= 18;
	private static final float SMALL_GAP	= 2;
	private static final float LARGE_GAP	= 8;
	
	private static final String TXT = 
		"The fabled Amulet of Yendor. After all of the hardships you've passed through, you finally hold it in your hands.\n\n" +
        "It makes you feel omnipotent. Using its power you can make any wish come true... However, it will work only once.\n\n" +
        "So, you can use it to bring yourself back to the surface, forever leaving this nightmarish dungeon in the past.\n\n" +
        "Or you can try to get out of the dungeon by yourself, and keep the miraculous powers of the Amulet for later.\n\n" +
        "What will be your decision?";

    private static final String TXT_SHORT =
            "So, what will be your decision?";
	
	public static boolean noText = false;
	
	private Image amulet;
	
	@Override
	public void create() {
		super.create();
		
		BitmapTextMultiline text = null;

        text = createMultiline( !noText ? TXT : TXT_SHORT , 6 );
        text.maxWidth = WIDTH;
        text.measure();
        add( text );
		
		amulet = new Image( Assets.AMULET );
		add( amulet );
		
		RedButton btnExit = new RedButton( TXT_EXIT ) {
			@Override
			protected void onClick() {
				Dungeon.win( ResultDescriptions.WIN );
				Dungeon.deleteGame( Dungeon.hero.heroClass, true );
				Game.switchScene( noText ? TitleScene.class : RankingsScene.class );
			}
		};
		btnExit.setSize( WIDTH, BTN_HEIGHT );
		add( btnExit );
		
		RedButton btnStay = new RedButton( TXT_STAY ) {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		btnStay.setSize( WIDTH, BTN_HEIGHT );
		add( btnStay );
		
		float height;
//		if (noText) {
//			height = amulet.height + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();
//
//			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
//			amulet.y = align( (Camera.main.height - height) / 2 );
//
//			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, amulet.y + amulet.height + LARGE_GAP );
//			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
			
//		} else {
			height = amulet.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();

			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
			amulet.y = align( (Camera.main.height - height) / 2 );

			text.x =  align( (Camera.main.width - text.width()) / 2 );
			text.y = amulet.y + amulet.height + LARGE_GAP;

			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, text.y + text.height() + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
//		}

		new Flare( 8, 48 ).color( 0xFFDDBB, true ).show( amulet, 0 ).angularSpeed = +30;
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}
	
	private float timer = 0;
	
	@Override
	public void update() {
		super.update();
		
		if ((timer -= Game.elapsed) < 0) {
			timer = Random.Float( 0.5f, 5f );
			
			Speck star = (Speck)recycle( Speck.class );
			star.reset( 0, amulet.x + 10.5f, amulet.y + 5.5f, Speck.DISCOVER );
			add( star );
		}
	}
}
