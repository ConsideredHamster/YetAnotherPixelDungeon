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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.watabou.utils.SparseArray;

public class BuffIndicator extends Component {

	public static final int NONE	    = -1;

    public static final int COMBO		= 0;
    public static final int GUARD		= 1;
    public static final int FOCUSED     = 2;
    public static final int EVADE		= 3;

    public static final int STARVATION	= 4;
    public static final int HUNGER		= 5;
    public static final int OVERFED     = 6;
    public static final int LIGHT		= 7;

    public static final int MENDING		= 8;
    public static final int MIND_VISION	= 9;
    public static final int LEVITATION	= 10;
    public static final int INVISIBLE	= 11;

    public static final int SHIELDED    = 12;
    public static final int SUNLIGHT    = 13;
    public static final int ENRAGED     = 14;
    public static final int IMMUNITY	= 15;

    public static final int SHOCKED     = 16;
    public static final int BURNING     = 17;
    public static final int CAUSTIC     = 18;

    public static final int WITHER      = 19;
    public static final int CRIPPLED    = 20;
    public static final int POISONED    = 21;

    public static final int VERTIGO     = 22;
    public static final int TERROR		= 23;
    public static final int CHARMED     = 24;

    public static final int DISRUPT		= 25;
    public static final int BANISH		= 26;
    public static final int CONTROL     = 27;

    public static final int BLINDED 	= 28;
    public static final int ENSNARED	= 29;
    public static final int FROZEN		= 30;
    public static final int DOOMED      = 31;

    public static final int RESIST_ELEC = 32;
    public static final int RESIST_FIRE = 33;
    public static final int RESIST_ACID = 34;
    public static final int RESIST_COLD = 35;
    public static final int RESIST_BODY = 36;
    public static final int RESIST_MIND = 37;
    public static final int RESIST_MAGI = 38;
    public static final int RESIST_PHYS = 39;

	public static final int SIZE	= 8;
	
	private static BuffIndicator heroInstance;
	
	private SmartTexture texture;
	private TextureFilm film;
	
	private SparseArray<Image> icons = new SparseArray<Image>();
	
	private Char ch;
	
	public BuffIndicator( Char ch ) {
		super();
		
		this.ch = ch;
		if (ch == Dungeon.hero) {
			heroInstance = this;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (this == heroInstance) {
			heroInstance = null;
		}
	}
	
	@Override
	protected void createChildren() {
		texture = TextureCache.get( Assets.BUFFS_SMALL );
		film = new TextureFilm( texture, SIZE, SIZE );
	}
	
	@Override
	protected void layout() {

		clear();
		
		SparseArray<Image> newIcons = new SparseArray<Image>();
		
		for (Buff buff : ch.buffs()) {

			int icon = buff.icon();

			if (icon != NONE) {

                Image img = new Image( texture );
                img.frame( film.get( icon ) );
                img.x = x + newIcons.size() * (SIZE + 2);
                img.y = y;
                add( img );

                String status = buff.status();

                if( ch == Dungeon.hero && status != null ){

                    BitmapText text = new BitmapText( PixelScene.font1x );
                    text.hardlight( 0xCACFC2 );

                    text.text( status );
                    text.measure();

                    text.x = img.x + SIZE / 2 - text.width() / 2;
                    text.y = img.y + SIZE + 2;
                    add( text );

                }
				
				newIcons.put( icon, img );
			}
		}
		
		for (Integer key : icons.keyArray()) {
			if (newIcons.get( key ) == null) {
				Image icon = icons.get( key );
				icon.origin.set( SIZE / 2 );
				add( icon );
				add( new AlphaTweener( icon, 0, 0.6f ) {
					@Override
					protected void updateValues( float progress ) {
						super.updateValues( progress );
						image.scale.set( 1 + 5 * progress );
					};
				} );
			}
		}
		
		icons = newIcons;
	}
	
	public static void refreshHero() {
		if (heroInstance != null) {
			heroInstance.layout();
		}
	}
}
