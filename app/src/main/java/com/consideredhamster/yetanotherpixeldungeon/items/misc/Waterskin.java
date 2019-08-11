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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Elemental;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Waterskin extends Item {

	public static final String AC_DRINK	= "DRINK";
	public static final String AC_POUR = "POUR";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_VALUE	= "%+dHP";
	private static final String TXT_STATUS	= "%d/%d";

	private static final String TXT_FULL		= "Your waterskins are full!";
	private static final String TXT_EMPTY		= "Your waterskins are empty!";

	private static final String TXT_POUR_SELF	= "You pour water from one of your waterskins on yourself.";
	private static final String TXT_POUR_TILE	= "You pour water from one of your waterskins on nearby tile.";

    private static final String TXT_HEALTH_FULL = "Your health is already full.";

    private static final String TXT_HEALTH_HALF = "Your health is not that low yet!";

    private static final String TXT_R_U_SURE =
            "Drinking from a waterskin only restores part of your missing health, so it is recommended " +
            "to use it only when you are significantly injured. Are you sure you want to drink it now?";

    private static final String TXT_YES			= "Yes, I know what I'm doing";
    private static final String TXT_NO			= "No, I changed my mind";

	{
		name = "waterskins";
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
		super.restoreFromBundle( bundle );
		value = bundle.getInt(VALUE);
        limit = bundle.getInt(LIMIT);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

        actions.add( AC_DRINK );
        actions.add( AC_POUR );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {
        if (action.equals( AC_DRINK )) {

            if( value > 0 ){

                if( hero.HT > hero.HP ){

                    if ( hero.HT < hero.HP * 2 ) {

                        GameScene.show(
                            new WndOptions( TXT_HEALTH_HALF, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        drink( hero );
                                    }
                                };
                            }
                        );

                    } else {
                        drink( hero );
                    }

                } else {
                    GLog.w( TXT_HEALTH_FULL);
                }
            } else {
                GLog.w( TXT_EMPTY );
            }

        } else if( action.equals( AC_POUR ) ){

            if( value > 0 ){

                curUser = hero;
                curItem = this;

                GameScene.selectCell( pourer );

            } else {
                GLog.w( TXT_EMPTY );
            }

        } else {

			super.execute(hero, action);
			
		}
	}

    private void drink( Hero hero ) {

        int healed = hero.HT - hero.HP;

        hero.heal( Random.IntRange( healed / 2, healed * 2 / 3 ) );

        this.value--;

        hero.spend( TIME_TO_DRINK );
        hero.busy();

        Sample.INSTANCE.play( Assets.SND_DRINK );
        hero.sprite.operate( hero.pos );

        updateQuickslot();

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
			"These are a receptacles made for storing water. Quaffing from one recovers " +
            "part of the lost health. They can be refilled in wells. Any additional " +
            "waterskins obtained will increase the amount of water you can carry with you.";
	}
	
	@Override
	public String toString() {
		return super.toString() + " (" + status() +  ")" ;
	}


    protected static CellSelector.Listener pourer = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                Ballistica.cast( curUser.pos, target, false, true );

                int cell = Ballistica.trace[ 0 ];

                if( Ballistica.distance > 0 ){
                    cell = Ballistica.trace[ 1 ];
                }

                Blob blob = Dungeon.level.blobs.get( Fire.class );

                if (blob != null) {

                    int cur[] = blob.cur;

                    if ( cur[ cell ] > 0) {
                        blob.volume -= cur[ cell ];
                        cur[ cell ] = 0;
                    }
                }

                boolean mapUpdated = false;
                int oldTile = Dungeon.level.map[cell];

                if (oldTile == Terrain.EMBERS) {

                    Level.set(cell, Terrain.GRASS);
                    mapUpdated = true;

                } else if (oldTile == Terrain.GRASS) {

                    Level.set(cell, Terrain.HIGH_GRASS);
                    mapUpdated = true;

                } else if (oldTile == Terrain.HIGH_GRASS ) {

                    Dungeon.level.drop( Generator.random(Generator.Category.HERB), cell, true).type = Heap.Type.HEAP;

                }

                if ( mapUpdated ) {

                    GameScene.discoverTile( cell, oldTile );

                    GameScene.updateMap();
                    Dungeon.observe();

                }

                Char ch = Actor.findChar( cell );

                if (ch != null) {
                    if( ch instanceof Elemental ) {
                        ch.damage( Random.IntRange( 1, (int)Math.sqrt( ch.HT / 2 + 1 ) ), this, null );
                    } else {
                        Buff.detach(ch, Burning.class);
                        Buff.detach(ch, Corrosion.class);
                    }
                }

                ((Waterskin)curItem).value--;
                Invisibility.dispel();

                if( curUser.pos == cell ) {
                    GLog.i( TXT_POUR_SELF );
                } else {
                    GLog.i( TXT_POUR_TILE );
                }

                Splash.at( cell, 0xFFFFFF, 5 );
                Sample.INSTANCE.play(Assets.SND_WATER, 0.6f, 0.6f, 1.5f);

                curUser.spend( TIME_TO_DRINK );
                curUser.sprite.operate(cell);
                curUser.busy();


            }
        }
        @Override
        public String prompt() {
            return "Select nearby tile to splash";
        }
    };
}
