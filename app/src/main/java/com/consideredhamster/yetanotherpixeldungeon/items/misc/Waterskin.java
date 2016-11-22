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
package com.consideredhamster.yetanotherpixeldungeon.items.misc;

import java.util.ArrayList;

import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class Waterskin extends Item {

	private static final String AC_DRINK	= "DRINK";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_VALUE	= "%+dHP";
	private static final String TXT_STATUS	= "%d/%d";

	private static final String TXT_FULL		= "Your waterskin is full!";
	private static final String TXT_EMPTY		= "Your waterskin is empty!";
//	private static final String TXT_NEW_SKIN    = "You've found an additional waterskin!";

	{
		name = "waterskin";
		image = ItemSpriteSheet.WATERSKIN;
		
		visible = false;
		unique = true;
	}
	
	private int value = 1;
	private int limit = 1;

	private static final String VALUE = "value";
	private static final String LIMIT = "limit";

    @Override
    public String quickAction() {
        return AC_DRINK;
    }
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(VALUE, value);
		bundle.put(LIMIT, limit);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		value = bundle.getInt(VALUE);
        limit = bundle.getInt(LIMIT);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

        actions.add(AC_DRINK);

        actions.remove( AC_THROW );
        actions.remove(AC_DROP);

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {
        if (action.equals( AC_DRINK )) {

            if (value > 0) {

                int healthLost = hero.HT - hero.HP;

//                int required = 1;
//                int required = (int)( healthLost / hero.ringBuffsHalved( RingOfSatiety.Satiety.class ) );

//                required = Math.max( 1, required / 25 + ( required % 25 < Random.Int( 25 ) ? 1 : 0 ) );

//                int consumed = Math.min( required, value );

                int value = healthLost * 2 / 3 ;
//                int value = Random.IntRange( healthLost / 3, healthLost * 2 / 3 )  ;

                value = (int) ( value * hero.ringBuffsHalved( RingOfVitality.Vitality.class ) );

                int effect = Math.min( healthLost, value );

                if (effect > 0) {
                    hero.HP += effect;
                    hero.sprite.showStatus(CharSprite.POSITIVE, TXT_VALUE, effect);
                    hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), Math.max( 1, (int)Math.sqrt( value ) ) );
                }

                this.value --;
//                this.value -= consumed;

                hero.spend(TIME_TO_DRINK);
                hero.busy();

                Sample.INSTANCE.play(Assets.SND_DRINK);
                hero.sprite.operate(hero.pos);

                updateQuickslot();

            } else {
                GLog.w( TXT_EMPTY );
            }

        } else {
			
			super.execute(hero, action);
			
		}
	}
	
	public boolean isFull() {
		return value >= limit;
	}
	
	public void collectDew( Dewdrop dew ) {

		value += dew.quantity;
		if (value >= limit) {
			value = limit;
			GLog.p( TXT_FULL );
		}

		updateQuickslot();
	}

    public int space() {
        return limit - value;
    }

    public Waterskin setLimit( int quantity ) {
        limit = quantity;
        return this;
    }

    public Waterskin fill( int quantity ) {
        value = Math.min( limit, value + quantity );
        updateQuickslot();

        return this;
    }
	
	public Waterskin fill() {
		value = limit;
		updateQuickslot();

        return this;
	}

    public Waterskin improve( Waterskin waterskin ) {

        limit += waterskin.limit;
        value += waterskin.value;

//        value = limit;

        updateQuickslot();

        return this;
    }

    @Override
    public boolean doPickUp( Hero hero ) {

        Waterskin vial = hero.belongings.getItem( Waterskin.class );

        if (vial != null) {

            vial.improve( this );
            GameScene.pickUp(this);

//            GLog.p(TXT_NEW_SKIN);

            Sample.INSTANCE.play(Assets.SND_ITEM);

            return true;

        }

        return super.doPickUp(hero);
    }


    @Override
    public int price() {
        return 50 * quantity;
    }

	@Override
	public String status() {
		return Utils.format( TXT_STATUS, value, limit );
	}
	
	@Override
	public String info() {
		return 
			"This is a receptacle made for storing water. Quaffing from it recovers " +
            "part of the lost health. It can be refilled in wells. Any additional " +
            "waterskins found will increase the amount of water you can carry with you.";
	}
	
	@Override
	public String toString() {
		return super.toString() + " (" + status() +  ")" ;
	}
}
