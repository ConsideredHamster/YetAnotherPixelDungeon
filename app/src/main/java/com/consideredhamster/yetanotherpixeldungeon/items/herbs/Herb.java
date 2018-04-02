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
package com.consideredhamster.yetanotherpixeldungeon.items.herbs;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.food.Food;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;

public abstract class Herb extends Food {
		
//    public static final String AC_PLANT	= "PLANT";

//    private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";

//    private static final float TIME_TO_PLANT = 1f;

    {
        energy = Satiety.MAXIMUM * 0.05f;
//        defaultAction = AC_THROW;
    }

//    protected Class<? extends Plant> plantClass;
//    protected String plantName;

    public Class<? extends Potion> alchemyClass;

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.addFromDamage( AC_PLANT );
//        return actions;
//    }

//    @Override
//    protected void onThrow( int cell ) {
//        if (Dungeon.bonus.map[cell] == Terrain.ALCHEMY || Level.chasm[cell]) {
//            super.onThrow( cell );
//        } else {
//            Dungeon.bonus.plant( this, cell );
//        }
//    }

//    @Override
//    public void execute( Hero hero, String action ) {
//        if (action.equals( AC_PLANT )) {
//
//            hero.spend( TIME_TO_PLANT );
//            hero.busy();
//            ((Seed)detach( hero.belongings.backpack )).onThrow( hero.pos );
//
//            hero.sprite.operate( hero.pos );
//
//        } else {
//
//            super.execute (hero, action );
//
//        }
//    }

//    public Plant couch( int pos ) {
//        try {
//            if (Dungeon.visible[pos]) {
//                Sample.INSTANCE.play( Assets.SND_PLANT );
//            }
//            Plant plant = plantClass.newInstance();
//            plant.pos = pos;
//            return plant;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @Override
    public int price() {
        return 10 * quantity;
    }

//    @Override
//    public String info() {
//        return String.format( TXT_INFO, Utils.indefinite( plantName ), desc() );
//    }
}

