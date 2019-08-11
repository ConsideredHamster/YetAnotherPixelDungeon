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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.UnholyArmor;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Bag;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Random;

public class ScrollOfBanishment extends Scroll {

	private static final String TXT_PROCCED	=
		"You are engulfed in a cleansing light, and all malevolent magic in your proximity is weakened.";
	private static final String TXT_NOT_PROCCED	= 
		"You are engulfed in a cleansing light, but nothing happens.";
	
	{
		name = "Scroll of Banishment";
        shortName = "Ba";

        spellSprite = SpellSprite.SCROLL_EXORCISM;
        spellColour = SpellSprite.COLOUR_HOLY;
	}
	
	@Override
	protected void doRead() {
		
		boolean procced = uncurse( curUser, curUser.belongings.backpack.items.toArray( new Item[0] ) );

		procced = procced | uncurse( curUser,
			curUser.belongings.weap1,
			curUser.belongings.weap2,
			curUser.belongings.armor,
			curUser.belongings.ring1, 
			curUser.belongings.ring2 );

        boolean affected = false;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Level.fieldOfView[mob.pos] ) {

                if( mob.isMagical() ) {
                    new Flare(6, 24).color(SpellSprite.COLOUR_HOLY, true).show(mob.sprite, 2f);
                    BuffActive.addFromDamage( mob, Tormented.class, (10 + curUser.magicPower()) );
                    affected = true;
                }

                if( mob.buff( UnholyArmor.class ) != null ) {
                    new Flare(6, 24).color(SpellSprite.COLOUR_HOLY, true).show(mob.sprite, 2f);
                    Buff.detach( mob, UnholyArmor.class );
                    affected = true;
                }
            }
        }
		
		if (procced || affected) {
			GLog.p( TXT_PROCCED );
		} else {		
			GLog.i( TXT_NOT_PROCCED );		
		}

        GameScene.flash(SpellSprite.COLOUR_HOLY - 0x555555);

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
			"The incantation on this scroll will attempt to banish any evil magics that might " +
            "happen to exist near the reader, weakening curses on carried items, banishing " +
            "nearby creatures of magical origin and even dispelling some malicious effects." +
            "\n\nDuration of effect inflicted by this scroll depends on magic skill of the reader.";
	}
	
	public static boolean uncurse( Hero hero, Item... items ) {
		
		boolean procced = false;

		for(Item item : items) {

			if (item != null) {

                if( item instanceof Bag ) {

                    uncurse( hero, ((Bag)item).items.toArray( new Item[0] ) );

                } else if( item.isUpgradeable() ) {

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
    public int price() {
        return isTypeKnown() ? 85 * quantity : super.price();
    }
}
