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

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.CarrionSwarm;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class Food extends Item {

	private static final float TIME_TO_EAT	= 3f;
    private static final String TXT_STUFFED		= "You are overfed... Can't eat anymore.";
	
	public static final String AC_EAT	= "EAT";

	public float energy = Hunger.HUNGRY;
	public String message = "That food tasted delicious!";

	{
		stackable = true;
		name = "ration of food";
		image = ItemSpriteSheet.RATION;
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
	public void execute( Hero hero, String action ) {
		if ( action.equals( AC_EAT ) && hero != null ) {

            Hunger hunger = hero.buff(Hunger.class);

            if( hunger != null && !hunger.isOverfed() ) {

                hunger.satisfy(energy);
                detach( hero.belongings.backpack );
                GLog.i( message );

                hero.sprite.operate( hero.pos );
                hero.busy();
                SpellSprite.show( hero, SpellSprite.FOOD );
//                SpellSprite.show( hero, SpellSprite.MAP );
                Sample.INSTANCE.play( Assets.SND_EAT );

                hero.spend(TIME_TO_EAT);

                for (Mob mob : Dungeon.level.mobs) {
                    if ( mob instanceof CarrionSwarm ) {
                        mob.beckon( hero.pos );
                    }
                }

                Statistics.foodEaten++;
                Badges.validateFoodEaten();
                updateQuickslot();

            } else {

                GLog.i( TXT_STUFFED );

            }

//			switch (hero.heroClass) {
//			case WARRIOR:
//				if (hero.HP < hero.HT) {
//					hero.HP = Math.min( hero.HP + 5, hero.HT );
//					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
//				}
//				break;
//			case SCHOLAR:
//				hero.belongings.charge( false );
//				ScrollOfRecharging.charge( hero );
//				break;
//			case BRIGAND:
//			case ACOLYTE:
//				break;
//			}
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public String info() {
		return 
			"Nothing fancy here: dried meat, " +
			"some biscuits - things like that.";
	}
	
	@Override
	public int price() {
		return 30 * quantity;
	}
}
