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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OilLantern extends Item {

	public static final String AC_LIGHT	= "LIGHT";
    public static final String AC_SNUFF    = "SNUFF";
    public static final String AC_REFILL   = "REFILL";
    public static final String AC_BURN 	= "BURN";

	private static final float TIME_TO_USE = 1f;
	private static final int MAX_CHARGE = 100;

	private static final String TXT_STATUS	= "%d%%";

	private static final String TXT_CANT_BURN	= "You need a spare oil flask for this!";
	private static final String TXT_NO_FLASKS	= "You don't have oil to refill the lamp!";

	private static final String TXT_DEACTIVATE = "Your lantern flickers faintly and goes out!";

    private static final String TXT_REFILL = "You refill the lantern.";

    private static final String TXT_LIGHT = "You light the lantern.";

    private static final String TXT_SNUFF = "You snuff out the lantern.";

    private static final String TXT_BURN_SELF = "You pour the oil from an oil flask on yourself and ignite it. Just... Why?";
    private static final String TXT_BURN_TILE = "You pour the oil from an oil flask on a nearby tile and ignite it.";
    private static final String TXT_BURN_FAIL = "You try to ignite a nearby tile, but it doesn't catch fire.";

	{
		name = "oil lantern";
		image = ItemSpriteSheet.LANTERN;

        active = false;
        charge = MAX_CHARGE;
        flasks = 0;

		visible = false;
		unique = true;

        updateSprite();
	}

    private boolean active;
    private int charge;
    private int flasks;

    private static final String ACTIVE = "active";
    private static final String FLASKS = "flasks";
    private static final String CHARGE = "charge";

    public void updateSprite() {
        image = isActivated() ? ItemSpriteSheet.LANTERN_LIT : ItemSpriteSheet.LANTERN ;
    }

    public int getCharge() {
        return charge;
    }

    public int getFlasks() {
        return flasks;
    }

    public void spendCharge() {
        charge--;
        updateQuickslot();
    }

    public boolean isActivated() {
        return active ;
    }

    @Override
    public String quickAction() {
        return charge > 0 ? ( isActivated() ? AC_SNUFF : AC_LIGHT ) : AC_REFILL ;
    }
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( CHARGE, charge );
        bundle.put( FLASKS, flasks );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
        active = bundle.getBoolean( ACTIVE );
        charge = bundle.getInt( CHARGE );
        flasks = bundle.getInt( FLASKS );

        updateSprite();
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

        actions.add( isActivated() ? AC_SNUFF : AC_LIGHT );
        actions.add( AC_REFILL );
        actions.add( AC_BURN );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

        if (action.equals( AC_LIGHT )) {

            if( charge > 0 ){

                if( hero.buff( Frozen.class ) == null ){

                    activate( hero, true );

                } else {

                    GLog.n( Frozen.TXT_CANNOT_LIGHT );

                }

            }

        } else if (action.equals( AC_SNUFF ) ) {

            if( isActivated() ){

                deactivate( hero, true );

            }

        } else if (action.equals( AC_REFILL ) ) {

            if ( flasks > 0 ) {

                refill( hero );

            } else {
                GLog.w( TXT_NO_FLASKS );
            }

        } else if (action.equals( AC_BURN ) ) {

            if ( flasks > 0 ) {

                curUser = hero;
                curItem = this;

                GameScene.selectCell( burner );

            } else {
                GLog.w( TXT_CANT_BURN );
            }

        } else {

			super.execute( hero, action );
			
		}
	}

    public void refill( Hero hero ) {

        flasks--;
        charge = MAX_CHARGE;

        hero.spend( TIME_TO_USE );
        hero.busy();

        Sample.INSTANCE.play( Assets.SND_DRINK, 1.0f, 1.0f, 1.2f );
        hero.sprite.operate( hero.pos );

        GLog.i( TXT_REFILL );
        updateQuickslot();

    }

    public void activate( Hero hero, boolean voluntary ) {

        active = true;
        updateSprite();

        Buff.affect( hero, Light.class );
//        hero.updateSpriteState();

        hero.search( false );

        if( voluntary ){

            hero.spend( TIME_TO_USE );
            hero.busy();

            GLog.i( TXT_LIGHT );
            hero.sprite.operate( hero.pos );

        }

        Sample.INSTANCE.play( Assets.SND_CLICK );
        updateQuickslot();

        Dungeon.observe();

    }

    public void deactivate( Hero hero, boolean voluntary ) {

        active = false;
        updateSprite();

        Buff.detach( hero, Light.class );
//        hero.updateSpriteState();

        if( voluntary ){

            hero.spend( TIME_TO_USE );
            hero.busy();

            hero.sprite.operate( hero.pos );
            GLog.i( TXT_SNUFF );

        } else {

            GLog.w( TXT_DEACTIVATE );

        }

        Sample.INSTANCE.play( Assets.SND_PUFF );
        updateQuickslot();

        Dungeon.observe();

    }
	
	public OilLantern collectFlask( OilFlask oil ) {

		flasks += oil.quantity;

		updateQuickslot();

        return this;

	}

    @Override
    public int price() {
        return 0;
    }

	@Override
	public String status() {
		return Utils.format( TXT_STATUS, charge );
	}
	
	@Override
	public String info() {
		return 
			"This lamp from a hardened glass is an indispensable item in the dungeon, which is " +
            "notorious for its poor ambient lighting. Even in the darkest of dungeons, this simple " +
            "device can illuminate your way, provided that you've got oil flasks to keep it " +
            "alight.\n\n" +
                ( isActivated() ?
                    "This small lantern shines vigorously, brighting your day. " :
                    "This small lantern is snuffed out, waiting for its moment to shine. "
                ) +
            "You have " + ( charge / 10.0 ) + " oz of oil left and " + flasks + " spare flask" +
            ( flasks != 1 ? "s" : "" ) + " remaining.";
	}

	public static class OilFlask extends Item {

        {
            name = "oil flask";
            image = ItemSpriteSheet.OIL_FLASK;

            visible = false;
        }

        @Override
        public boolean doPickUp( Hero hero ) {

            OilLantern lamp = hero.belongings.getItem( OilLantern.class );

            if (lamp != null) {

                lamp.collectFlask( this );
                GameScene.pickUp(this);

                Sample.INSTANCE.play(Assets.SND_ITEM);

                return true;

            }

            return super.doPickUp(hero);
        }


        @Override
        public int price() {
            return quantity * 30;
        }

        @Override
        public String info() {
            return
                "This container holds 10 oz of lantern oil. You can use it to " +
                "refill your lantern or pour on something to burn it.";
        }
    }



    protected static CellSelector.Listener burner = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                Ballistica.cast( curUser.pos, target, false, true );

                int cell = Ballistica.trace[ 0 ];

                if( Ballistica.distance > 0 ){
                    cell = Ballistica.trace[ 1 ];
                }

                if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
                    GameScene.add( Blob.seed( cell, 5, Fire.class ) );
                }

                ((OilLantern)curItem).flasks--;
                Invisibility.dispel();

                if( curUser.pos == cell ) {
                    GLog.i( TXT_BURN_SELF );
                } else if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
                    GLog.i( TXT_BURN_TILE );
                } else {
                    GLog.i( TXT_BURN_FAIL );
                }

                Sample.INSTANCE.play(Assets.SND_BURNING, 0.6f, 0.6f, 1.5f);
                CellEmitter.get( cell ).burst( FlameParticle.FACTORY, 5 );

                curUser.sprite.operate(cell);
                curUser.busy();
                curUser.spend( Actor.TICK );

            }
        }
        @Override
        public String prompt() {
            return "Select nearby tile to burn";
        }
    };
}
