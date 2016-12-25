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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ShopkeeperHumanSprite;
import com.consideredhamster.yetanotherpixeldungeon.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndBag;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndTradeItem;

import java.util.ArrayList;

public class Shopkeeper extends NPC {

    private static final String TXT_GREETINGS = "Good day! Are you interested in my wares?";

    private static String[][] LINES = {

            {
                    "Hey, cut it off.",
                    "Don't do that.",
                    "That's not allowed in my shop.",
                    "Stop it, please.",
                    "Ouch! Stop it!",
            },
            {
                    "One more time and I'll call for help!",
                    "Stop that! Or I will call someone!",
                    "I advise you to leave me alone.",
            },
            {
                    "GUARDS! GUARDS!",
                    "SOMEONE, HELP ME!",
                    "I AM UNDER ASSAULT!",
            },
            {
                    "Ah, screw it. I am leaving.",
                    "That's it. I am outta here.",
                    "Why don't you leave me alone!",
            },
    };

    private int threatened = 0;
    private boolean seenBefore = false;

	{
		name = "shopkeeper";
		spriteClass = ShopkeeperHumanSprite.class;
	}


	
	@Override
	protected boolean act() {

        if( noticed ) {

            noticed = false;

        }

        if (!seenBefore && Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.SHOP );
            seenBefore = true;
            greetings();
        }

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
    public void damage( int dmg, Object src, DamageType type ) {
        react();
	}

	@Override
    public boolean add( Buff buff ) {
        react();
        return false;
    }

    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }
	
	protected void react() {

        if( threatened < LINES.length ) {
            yell(LINES[threatened][Random.Int(LINES[threatened].length)]);
        }

        if( threatened >= 3 ) {
            runAway();
        } else if( threatened >= 2 ) {
            callForHelp();
        }

        threatened++;
    }

    protected void callForHelp() {

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos != pos) {
                mob.beckon( pos );
            }
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center( pos ).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
        }

        Sample.INSTANCE.play( Assets.SND_CHALLENGE );
    }

	protected void runAway() {

        ArrayList<Heap> forSale = new ArrayList<>();

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.FOR_SALE) {

                forSale.add( heap );

            }
        }

        int amount = forSale.size();

		for ( Heap heap : forSale ) {

            if( Random.Int( amount + 1 ) > 0 ) {

                CellEmitter.get(heap.pos).burst( Speck.factory( Speck.WOOL ), 5 );
                heap.destroy();

            } else {

                heap.type = Heap.Type.HEAP;

            }
		}
		
		destroy();
		sprite.killAndErase();
		
		Journal.remove( Journal.Feature.SHOP );
		CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 10 );
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"This stout guy looks more appropriate for a trade district in some large city " +
			"than this little black market down here. Better for you, anyway.";
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, "Select an item to sell" );
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public void interact() {
		sell();
	}

    private static final String SEENBEFORE		= "seenbefore";
    private static final String THREATENED		= "threatened";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( SEENBEFORE, seenBefore );
        bundle.put( THREATENED, threatened );
    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean( SEENBEFORE );
        threatened = bundle.getInt( THREATENED );
    }
}
