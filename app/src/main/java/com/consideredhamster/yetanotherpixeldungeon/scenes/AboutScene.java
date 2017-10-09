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

import android.content.Intent;
import android.net.Uri;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.ui.Archs;
import com.consideredhamster.yetanotherpixeldungeon.ui.ExitButton;
import com.consideredhamster.yetanotherpixeldungeon.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.ui.Window;

public class AboutScene extends PixelScene {

    private static final String CAP_WATA =
            "Original Pixel Dungeon";

    private static final String TXT_WATA =
            "Code & graphics: Watabou\n" +
            "Music: Cube_Code\n\n" +
            "These guys are awesome!\n\n" +
            "Watabou's official website:";

    private static final String LNK_WATA =
            "pixeldungeon.watabou.ru";

    private static final String CAP_YAPD =
            "Yet Another Pixel Dungeon";

    private static final String TXT_YAPD =
            "Author: ConsideredHamster\n" +
            "Artist: PavelProvotorov\n" +
            "Editor: Inevielle\n" +
            "Thanks for support: rbyj93\n\n" +
            "Special thanks to: Evan (ShPD)";

    private static final String LNK_YAPD =
            "reddit.com/user/ConsideredHamster";


	
	@Override
	public void create() {
		super.create();

        boolean landscape = YetAnotherPixelDungeon.landscape();

        BitmapTextMultiline text1 = createMultiline(TXT_WATA, landscape ? 8 : 6 );
        text1.maxWidth = Math.min( Camera.main.width, 120 );
        text1.measure();
        text1.x = align(landscape ? (Camera.main.width / 2 - text1.width()) / 2 : (Camera.main.width - text1.width()) / 2);
        text1.y = align( landscape ? (Camera.main.height - text1.height()) / 2 : (Camera.main.height / 2 - text1.height()) / 2 ) + 8;
        add(text1);

        BitmapTextMultiline title1 = createMultiline(CAP_WATA, landscape ? 8 : 6 );
        title1.maxWidth = Math.min(Camera.main.width, 120);
        title1.hardlight(Window.TITLE_COLOR);
        title1.measure();
        title1.x = align(landscape ? (Camera.main.width / 2 - title1.width()) / 2 : (Camera.main.width - title1.width()) / 2);
        title1.y = text1.y - title1.height;
        add(title1);

        Image wata = Icons.WATA.get();
        wata.x = align( landscape ? (Camera.main.width / 2 - wata.width()) / 2 : (Camera.main.width - wata.width()) / 2);
        wata.y = title1.y - wata.height - 8;
        add( wata );

        new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

		BitmapTextMultiline text2 = createMultiline(TXT_YAPD, landscape ? 8 : 6);
		text2.maxWidth = Math.min(Camera.main.width, 120);
		text2.measure();
        text2.x = align( landscape ? (Camera.main.width / 2 - text2.width()) / 2 + Camera.main.width / 2 : (Camera.main.width - text2.width()) / 2);
        text2.y = align( landscape ? (Camera.main.height - text2.height()) / 2 : (Camera.main.height / 2 - text2.height()) / 2 + Camera.main.height / 2 ) + 8;
		add(text2);

        BitmapTextMultiline title2 = createMultiline(CAP_YAPD, landscape ? 8 : 6 );
        title2.maxWidth = Math.min(Camera.main.width, 120);
        title2.hardlight(Window.TITLE_COLOR);
        title2.measure();
        title2.x = align( landscape ? (Camera.main.width / 2 - title2.width()) / 2 + Camera.main.width / 2 : (Camera.main.width - title2.width()) / 2);
        title2.y = text2.y - title2.height;

        add(title2);

        Image yapd = Icons.YAPD.get();
//        yapd.x = text2.x + (text2.width - yapd.width) / 2;
        yapd.x = align( landscape ? (Camera.main.width / 2 - yapd.width()) / 2 + Camera.main.width / 2 : (Camera.main.width - yapd.width()) / 2);
        yapd.y = title2.y - yapd.height - 8;
        add( yapd );

        new Flare( 7, 64 ).color( 0x332211, true ).show( yapd, 0 ).angularSpeed = +20;
		
		BitmapTextMultiline link1 = createMultiline(LNK_WATA, landscape? 8 : 6 );
		link1.maxWidth = Math.min( Camera.main.width, 120 );
		link1.measure();
		link1.hardlight(Window.TITLE_COLOR);
		add( link1 );

		link1.x = text1.x;
		link1.y = text1.y + text1.height() + 8;

		TouchArea hotArea1 = new TouchArea( link1 ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK_WATA ) );
				Game.instance.startActivity( intent );
			}
		};
		add( hotArea1 );

        BitmapTextMultiline link2 = createMultiline(LNK_YAPD, landscape? 8 : 6 );
        link2.maxWidth = Math.min( Camera.main.width, 120 );
        link2.measure();
        link2.hardlight(Window.TITLE_COLOR);
        add( link2 );

        link2.x = text2.x;
        link2.y = text2.y + text2.height() + 8;

        TouchArea hotArea2 = new TouchArea( link2 ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK_YAPD ) );
                Game.instance.startActivity( intent );
            }
        };
        add( hotArea2 );

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		YetAnotherPixelDungeon.switchNoFade(TitleScene.class);
	}
}
