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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Guard;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Ethereal;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Tempered;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class RangedWeaponMissile extends RangedWeapon {

	public RangedWeaponMissile(int tier) {

		super( tier );

	}

    @Override
    public int maxDurability() {
        return 150 ;
    }

    @Override
    public int min( int bonus ) {
        return tier + state + bonus + ( enchantment instanceof Tempered ? bonus : 0 ) - 1;
    }

    @Override
    public int max( int bonus ) {
        return tier * 6 + state * dmgMod() - 4
                + ( enchantment instanceof Tempered || bonus >= 0 ? bonus * dmgMod() : 0 )
                + ( enchantment instanceof Tempered && bonus >= 0 ? 1 + bonus : 0 ) ;
    }

    public int dmgMod() {
        return tier + 1 ;
    }

    @Override
    public int str(int bonus) {
        return 4 + tier * 4 - bonus * ( enchantment instanceof Ethereal ? 2 : 1 );
    }

    @Override
    public int penaltyBase(Hero hero, int str) {

        return super.penaltyBase(hero, str) + tier * 4 - 4;

    }

    @Override
    public float breakingRateWhenShot() {
        return 0.1f / Dungeon.hero.ringBuffs( RingOfDurability.Durability.class );
    }

//    @Override
//    public int lootChapter() {
//        return super.lootChapter() + 1;
//    }

    @Override
    public boolean checkAmmo( Hero hero, boolean showMessage ) {

        if (!isEquipped(hero)) {

            if( showMessage ) {
                GLog.n(TXT_NOTEQUIPPED);
            }

        } else if (ammunition() == null || !ammunition().isInstance( hero.belongings.weap2 )) {

            if( showMessage ) {
                GLog.n(TXT_AMMO_NEEDED);
            }

        } else {

            return true;

        }

        return false;
    }


    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_SHOOT) {

            curUser = hero;
            curItem = this;

            if ( checkAmmo( hero, true ) ) {

                GameScene.selectCell(shooter);

            }

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public String descType() {
        return "missile";
    }

    public static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                if (target == curUser.pos) {
                    GLog.i( TXT_SELF_TARGET );
                    return;
                }

                final RangedWeapon curWeap = (RangedWeapon)RangedWeapon.curItem;

                if( curUser.buff( Vertigo.class ) != null ) {
                    target += Level.NEIGHBOURS8[Random.Int( 8 )];
                }

                final int cell = Ballistica.cast(curUser.pos, target, false, true);

                Char ch = Actor.findChar( cell );

                if( ch != null && curUser != ch && Dungeon.visible[ cell ] ) {

//                    if ( curUser.isCharmedBy( ch ) ) {
//                        GLog.i( TXT_TARGET_CHARMED );
//                        return;
//                    }

                    QuickSlot.target(curItem, ch);
                    TagAttack.target( (Mob)ch );
                }

                curUser.sprite.cast(cell, new Callback() {
                    @Override
                    public void call() {
                        curUser.busy();
                        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                            reset(curUser.pos, cell, curUser.belongings.weap2, 2.0f, new Callback() {
                                @Override
                                public void call() {
                                    ((ThrowingWeaponAmmo) curUser.belongings.weap2).onShoot(cell, curWeap);
                                }
                            });

                        if( curWeap != null ){
                            curUser.buff( Satiety.class ).decrease( Satiety.POINT * curWeap.str() / curUser.STR() );
                            curWeap.use( 2 );
                        }
                    }
                });



                Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
                Invisibility.dispel();

            }
        }

        @Override
        public String prompt() {
            return "Choose direction to shoot at";
        }
    };

}
