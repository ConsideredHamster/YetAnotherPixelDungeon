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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;

public class WndInfoCell extends Window {
	
	private static final float GAP	= 2;

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 240;

	private static final String TXT_NOTHING	= "There is nothing here.";
	
	public WndInfoCell( int cell ) {
		
		super();

        int width = YetAnotherPixelDungeon.landscape() ? WIDTH_L : WIDTH_P ;
		
		int tile = Dungeon.level.map[cell];
		if (Level.water[cell]) {
			tile = Terrain.WATER;
		} else if (Level.chasm[cell]) {
			tile = Terrain.CHASM;
		}
		
		IconTitle titlebar = new IconTitle();
		if (tile == Terrain.WATER) {
			Image water = new Image( Dungeon.level.waterTex() );
			water.frame( 0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE );
			titlebar.icon( water );
		} else {
			titlebar.icon( DungeonTilemap.tile( tile ) );
		}
		titlebar.label( Dungeon.level.tileName( tile ) );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		BitmapTextMultiline info = PixelScene.createMultiline( 6 );
		add( info );
		
		StringBuilder desc = new StringBuilder( Dungeon.level.tileDesc( tile ) );
		
		final char newLine = '\n';
		for (Blob blob:Dungeon.level.blobs.values()) {
			if (blob.cur[cell] > 0 && blob.tileDesc() != null) {
				if (desc.length() > 0) {
					desc.append( newLine );
					desc.append( newLine );
				}
				desc.append( blob.tileDesc() );
			}
		}
		
		info.text( desc.length() > 0 ? desc.toString() : TXT_NOTHING );
		info.maxWidth = width;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		
		resize( width, (int)(info.y + info.height()) );
	}
}
