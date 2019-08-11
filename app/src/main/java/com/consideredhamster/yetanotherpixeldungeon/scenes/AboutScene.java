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
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Archs;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ExitButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.utils.PointF;

public class AboutScene extends PixelScene {

    private static final String TITLE =
            "Yet Another Pixel Dungeon";

    private static final String TXT_PART1 =
            "Mod author:\n" +
            "\u007F ConsideredHamster\n" +
            "\n" +
            "Additional code:\n" +
            "\u007F RavenWolf\n" +
            "\n" +
            "Additional music:\n" +
            "\u007F Jivvy\n" +
            "\n" +
            "Main editor:\n" +
            "\u007F Inevielle"
            ;

    private static final String TXT_PART2 =

            "Additional sprites:\n" +
            "\n" +
            "\u007F ConsideredHamster\n" +
            "\u007F Bgnu-Thun\n" +
            "\u007F PavelProvotorov\n" +
            "\u007F JleHuBbluKoT\n" +
            "\u007F RavenWolf\n" +
            "\n" +
            "Original game made by:\n" +
            "\u007F Watabou & Cube_Code"
        ;

	@Override
	public void create() {
		super.create();

        boolean landscape = YetAnotherPixelDungeon.landscape();

        Image yapd = Icons.YAPD.get();
        yapd.scale = new PointF( 1.5f, 1.5f );
        yapd.x = align( ( Camera.main.width - yapd.width()) / 2 );
        yapd.y = align( (Camera.main.height / 3 - yapd.height()) / 2 );
        add( yapd );

        new Flare( 7, 64 ).color( 0x332211, true ).show( yapd, 0 ).angularSpeed = +20;

        BitmapTextMultiline title1 = createMultiline( TITLE, landscape ? 10 : 8 );
        title1.maxWidth = Math.min(Camera.main.width, 120);
        title1.hardlight(Window.TITLE_COLOR);
        title1.measure();
        title1.x = align( ( Camera.main.width - title1.width() ) / 2 );
        title1.y = align( landscape ? ( ( Camera.main.height / 3 - title1.height() ) / 2 ) : ((Camera.main.height / 3 - title1.height()) / 2 ) );
        add(title1);

        BitmapTextMultiline text1 = createMultiline( TXT_PART1, landscape ? 8 : 6 );
        text1.measure();
        text1.width = Math.min( Camera.main.width, landscape ? 120 : 200 );
        text1.x = align( landscape ? (Camera.main.width / 2 - text1.width()) / 2 : (Camera.main.width - text1.width()) / 2 );
        text1.y = align( landscape ? (Camera.main.height - text1.height()) / 2 : Camera.main.height * 2 / 7 );
        add(text1);

        BitmapTextMultiline text2 = createMultiline( TXT_PART2, landscape ? 8 : 6 );
        text2.measure();
        text2.width = Math.min( Camera.main.width, landscape ? 120 : 200 );
        text2.x = align( landscape ? (text1.x + Camera.main.width / 2) : (Camera.main.width - text2.width()) / 2 );
        text2.y = align( landscape ? (Camera.main.height - text2.height()) / 2 : (text1.y + text1.height() + 8 ) );
        add(text2);

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
