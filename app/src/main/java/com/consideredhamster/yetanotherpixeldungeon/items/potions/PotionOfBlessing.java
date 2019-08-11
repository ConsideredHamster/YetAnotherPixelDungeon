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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShaftParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class PotionOfBlessing extends Potion {

    public static final float DURATION	= 25f;
    public static final float MODIFIER	= 1.0f;

    private static final String TXT_PROCCED	=
            "A cleansing light shines from above, and all malevolent magic nearby is weakened.";
    private static final String TXT_NOT_PROCCED	=
            "A cleansing light shines from above, but nothing happens.";
	
	{
		name = "Potion of Blessing";
        shortName = "Bl";
        harmful = true;
	}


    @Override
    public void shatter( int cell ) {

        if (Dungeon.visible[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );

            new Flare(6, 32).color(SpellSprite.COLOUR_HOLY, true).show( cell, 2f );
        }

        boolean affected = false;

        for (int n : Level.NEIGHBOURS9) {

            int c = cell + n;

            Char ch = Char.findChar( c );

            if( ch == null ) {

                Heap heap = Dungeon.level.heaps.get( c );

                if (heap != null) {
                    affected = affected | uncurse( c, heap.items.toArray( new Item[0] ) );
                }

            } else if( ch == curUser ) {
                affected = uncurse( c, curUser.belongings.backpack.items.toArray( new Item[0] ) ) |
                        uncurse( c,
                        curUser.belongings.weap1,
                        curUser.belongings.weap2,
                        curUser.belongings.armor,
                        curUser.belongings.ring1,
                        curUser.belongings.ring2 );

            } else if ( ch.isMagical() ) {

                int damage = ch.totalHealthValue();

                ch.damage( ( n == 0 ? Random.IntRange( damage / 2, damage ) : Random.IntRange( damage / 3, damage / 2 ) ), curUser, Element.DISPEL );
                affected = true;

            }

            CellEmitter.get( c ).burst(ShaftParticle.FACTORY, 5 );
        }

        if (affected) {
            GLog.p( TXT_PROCCED );
        } else {
            GLog.i( TXT_NOT_PROCCED );
        }
    }

    @Override
	public String desc() {
		return
			"This potion is imbued with great disruptive energy. Shattering it will bathe " +
            "everything near the point of impact in a cleansing light, removing curses and " +
            "harming creatures of purely magical origin.";
	}

    public static boolean uncurse( int pos, Item... items ) {

        int procced = 0;
        for(Item item : items) {
            if (item != null) {

                if( item instanceof Bag) {

                    uncurse( pos, ((Bag)item).items.toArray( new Item[0] )  );

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

                        procced++;

                    }
                }
            }
        }

        if ( procced > 0 ) {
            CellEmitter.get( pos ).burst(ShadowParticle.UP, procced * 5);
        }

        return procced > 0;
    }
	
	@Override
	public int price() {
		return isTypeKnown() ? 50 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.85f;
    }
}
