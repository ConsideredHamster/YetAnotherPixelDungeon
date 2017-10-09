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

import com.watabou.noosa.BitmapTextMultiline;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.quest.DwarfToken;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class WndImp extends Window {
	
	private static final String TXT_MESSAGE	= 
		"Oh yes! You are my hero!\n" +
		"Regarding your reward, I don't have cash with me right now, but I have something better for you. " +
		"This is my family heirloom ring: my granddad took it off a dead %s's finger.";
	private static final String TXT_REWARD		= "Take the ring";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	public WndImp( final AmbitiousImp imp, final DwarfToken tokens ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tokens.image(), null ) );
		titlebar.label( Utils.capitalize( tokens.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

        String story = TXT_MESSAGE;

        switch( Dungeon.hero.heroClass ) {
            case WARRIOR:
                story = Utils.format( story, "paladin" );
                break;
            case SCHOLAR:
                story = Utils.format( story, "sorcerer" );
                break;
            case BRIGAND:
                story = Utils.format( story, "assassin" );
                break;
            case ACOLYTE:
                story = Utils.format( story, "ranger" );
                break;
        }


		BitmapTextMultiline message = PixelScene.createMultiline( story, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnReward = new RedButton( TXT_REWARD ) {
			@Override
			protected void onClick() {
				takeReward( imp, tokens, AmbitiousImp.Quest.reward );
			}
		};
		btnReward.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnReward );
		
		resize( WIDTH, (int)btnReward.bottom() );
	}
	
	private void takeReward( AmbitiousImp imp, DwarfToken tokens, Item reward ) {
		
		hide();
		
		tokens.detachAll( Dungeon.hero.belongings.backpack );

		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, imp.pos ).sprite.drop();
		}
		
		imp.flee();
		
		AmbitiousImp.Quest.complete();
	}
}
