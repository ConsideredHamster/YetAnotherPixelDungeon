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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import java.util.ArrayList;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.watabou.utils.Random;

public class WandOfMagicMissile extends Wand {

//	public static final String AC_DISENCHANT	= "DISENCHANT";
//
//	private static final String TXT_SELECT_WAND	= "Select a wand to upgrade";
//
//	private static final String TXT_DISENCHANTED =
//		"you disenchanted the Wand of Magic Missile and used its essence to upgrade your %s";
//
//	private static final float TIME_TO_DISENCHANT	= 2f;
	
//	private boolean disenchantEquipped;

    @Override
    public int max() {
        return 20 + curUser.magicSkill() / 3;
    }

    @Override
    public int min() {
        return 10 + curUser.magicSkill() / 6;
    }

	{
		name = "Wand of Magic Missile";
        shortName = "Ma";
//		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
//		if (bonus > 0) {
//			actions.add( AC_DISENCHANT );
//		}
		return actions;
	}
	
	@Override
	protected void onZap( int cell ) {

        int power = curUser.magicSkill() / 3;

        Splash.at( cell, 0x33FFFFFF, (int) Math.sqrt(power) + 2 );

        Char ch = Actor.findChar( cell );

        if (ch != null) {

            if( Char.hit( curUser, ch, true, true ) ) {

                ch.damage(
                        Char.absorb(
                                damageRoll(), ch.armorClass()
                        ), curUser, null
                );

                ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, Random.IntRange( 2 + power / 10 , 4 + power / 5 ) );
//                ch.sprite.burst( 0xFF99CCFF, (int)Math.sqrt( power ) );

//                if (ch == curUser && !ch.isAlive()) {
//                    Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
//                    GLog.n( "You killed yourself with your own Wand of Magic Missile..." );
//                }

            } else {

                Sample.INSTANCE.play(Assets.SND_MISS);
                ch.missed();

            }
		}
	}
	
//	@Override
//	public void execute( Hero hero, String action ) {
//		if (action.equals( AC_DISENCHANT )) {
			
//			if (hero.belongings.weapon == this) {
//				disenchantEquipped = true;
//				hero.belongings.weapon = null;
//				updateQuickslot();
//			} else {
//				disenchantEquipped = false;
//				detach( hero.belongings.backpack );
//			}
			
//			curUser = hero;
//			GameScene.selectItem( itemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND );
//
//		} else {
//
//			super.execute( hero, action );
//
//		}
//	}

//	@Override
//	protected boolean isTypeKnown() {
//		return false;
//	}
//
//
//	protected int initialCharges() {
//		return 3;
//	}

	@Override
	public String desc() {
		return
			"This wand's effect is quite simple, as it just launches bolts of pure magical energy. " +
            "However, its main caveat is that it's power heavily depends on arcane skills of the user, " +
            "making this wand a quite fearsome weapon in the hands of a sufficiently skilled spellcaster.";
	}
	
//	private final WndBag.Listener itemSelector = new WndBag.Listener() {
//		@Override
//		public void onSelect( Item item ) {
//			if (item != null) {
//
//				Sample.INSTANCE.play( Assets.SND_EVOKE );
//				ScrollOfUpgrade.upgrade( curUser );
//				evoke( curUser );
//
//				GLog.w( TXT_DISENCHANTED, item.name() );
//
//				item.upgrade();
//				curUser.spendAndNext( TIME_TO_DISENCHANT );
//
//				Badges.validateItemLevelAcquired(item);
//
//			} else {
//				if (disenchantEquipped) {
//					curUser.belongings.weapon = WandOfMagicMissile.this;
//					WandOfMagicMissile.this.updateQuickslot();
//				} else {
//					collect( curUser.belongings.backpack );
//				}
//			}
//		}
//	};
}
