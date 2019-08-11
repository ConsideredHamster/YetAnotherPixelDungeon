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
package com.consideredhamster.yetanotherpixeldungeon.items.food;

import java.util.ArrayList;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.SungrassHerb;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.CarrionSwarm;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class Food extends Item {

    private static final String TXT_NOT_THAT_HUNGRY = "Don't waste your food!";

    private static final String TXT_R_U_SURE =
        "Your satiety cannot be greater than 100% anyway, so probably it would be a better idea to " +
        "spend some more time before eating this piece of food. Are you sure you want to eat it now?";

    private static final String TXT_YES			= "Yes, I know what I'm doing";
    private static final String TXT_NO			= "No, I changed my mind";
	
	public static final String AC_EAT	        = "EAT";

	public float time;
	public float energy;
	public String message;

	{
		stackable = true;
        time = 3f;
	}

    @Override
    public String quickAction() {
        return AC_EAT;
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		if ( action.equals( AC_EAT ) && hero != null ) {

            final Satiety hunger = hero.buff(Satiety.class);

            if( hero.buff( Poisoned.class ) == null || this instanceof SungrassHerb || this instanceof SungrassHerb.SavoryMeat ){

                if( hunger.energy() + energy > Satiety.MAXIMUM ){

                    GameScene.show(
                            new WndOptions( TXT_NOT_THAT_HUNGRY, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                                @Override
                                protected void onSelect( int index ){
                                    if( index == 0 ){
                                        consume( hunger, hero );
                                    }
                                }
                            }
                    );

                } else {
                    consume( hunger, hero );
                }

            } else {

                GLog.n( Poisoned.TXT_CANNOT_EAT );

            }
			
		} else {
		
			super.execute( hero, action );
			
		}
	}

    private void consume( Satiety hunger, Hero hero ) {

        hunger.increase( energy );
        detach( hero.belongings.backpack );
        onConsume( hero );

        hero.sprite.operate( hero.pos );
        hero.busy();
        SpellSprite.show( hero, SpellSprite.FOOD );
        Sample.INSTANCE.play( Assets.SND_EAT );

        hero.spend( time );

        for (Mob mob : Dungeon.level.mobs) {
            if ( mob instanceof CarrionSwarm ) {
                mob.beckon( hero.pos );
            }
        }

        Statistics.foodEaten++;
        Badges.validateFoodEaten();
        updateQuickslot();
    }

    public void onConsume( Hero hero ) {
        GLog.i( message );
    }

    @Override
    public String info() {
        return desc() + "\n\n" +
            "Eating this piece of food will take _" + (int)time + "_ turns and " +
            "restore _" + (int)( energy / 10 ) + "%_ of your satiety.";
    }

}
