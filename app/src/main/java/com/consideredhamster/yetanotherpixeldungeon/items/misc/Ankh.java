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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Ankh extends Item {

	{
        visible = false;
		stackable = true;
		name = "ankh";
		image = ItemSpriteSheet.ANKH;
	}

    private static final String TXT_RESURRECT	= "You are resurrected by the powers of Ankh!";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

        return actions;
    }
	
	@Override
	public String info() {
		return 
			"The ancient symbol of immortality grants an ability to return to life after death. ";
	}


	public static void resurrect( Hero hero ) {
        new Flare( 8, 32 ).color(0xFFFF66, true).show(hero.sprite, 2f) ;
        GameScene.flash(0xFFFFAA);

        hero.HP = hero.HT;

        Debuff.removeAll(hero);

        uncurse( hero, hero.belongings.backpack.items.toArray( new Item[0] ) );

        uncurse( hero,
                hero.belongings.weap1,
                hero.belongings.weap2,
                hero.belongings.armor,
                hero.belongings.ring1,
                hero.belongings.ring2
        );

        hero.sprite.showStatus(CharSprite.POSITIVE, "resurrected!");
        GLog.w(TXT_RESURRECT);
	}

    public static boolean uncurse( Hero hero, Item... items ) {

        boolean procced = false;

        for(Item item : items) {

            if (item != null) {

                if( item instanceof Bag ) {

                    uncurse( hero, ((Bag)item).items.toArray( new Item[0] ) );

                } else {

                    item.identify(CURSED_KNOWN);

                    if (item.bonus < 0) {

                        item.bonus = Random.IntRange(item.bonus + 1, 0);

                        if (item.bonus == 0) {
                            if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
                                ((Weapon) item).enchant(null);
                            } else if (item instanceof Armour && ((Armour) item).glyph != null) {
                                ((Armour) item).inscribe(null);
                            }
                        }

                        procced = true;

                    }
                }
            }
        }

        if (procced) {
            hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
        }

        return procced;
    }

	@Override
    public String status() {
        return Integer.toString( quantity );
    }

	@Override
	public int price() {
		return 100 * quantity;
	}
}
