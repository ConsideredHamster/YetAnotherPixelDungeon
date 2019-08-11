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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Combo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.TagAttack;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Spark;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SmokeParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Explosives;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
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

public abstract class RangedWeaponFlintlock extends RangedWeapon {

    public boolean loaded = false;

	public RangedWeaponFlintlock(int tier) {

		super( tier );

	}

    public static final String AC_RELOAD = "RELOAD";

    protected static final String TXT_POWDER_NEEDED = "You don't have enough gunpowder to reload this gun.";
    protected static final String TXT_NOT_LOADED = "This gun is not loaded.";
    protected static final String TXT_ALREADY_LOADED = "This gun is already loaded.";
    protected static final String TXT_RELOADING = "reloading...";

    @Override
    public int maxDurability() {
        return 150 ;
    }

    @Override
    public int min( int bonus ) {
        return tier + state + bonus + ( enchantment instanceof Tempered ? bonus : 0 ) + 1;
    }

    @Override
    public int max( int bonus ) {
        return tier * 4 + state * dmgMod() + 8
                + ( enchantment instanceof Tempered || bonus >= 0 ? bonus * dmgMod() : 0 )
                + ( enchantment instanceof Tempered && bonus >= 0 ? 1 + bonus : 0 ) ;
    }

    public int dmgMod() {
        return tier + 2;
    }

    @Override
    public int str(int bonus) {
        return 6 + tier * 4 - bonus * ( enchantment instanceof Ethereal ? 2 : 1 );
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + tier * 4 + 4;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public float breakingRateWhenShot() {
        return 0.2f / Dungeon.hero.ringBuffs( RingOfDurability.Durability.class );
    }

    @Override
    public String descType() {
        return "flintlock";
    }

//    @Override
//    public String status() {
//        return isEquipped( Dungeon.hero ) ? (
//                ( ammunition().isInstance( Dungeon.hero.belongings.weap2 ) ? Integer.toString( Dungeon.hero.belongings.weap2.quantity ) : "-" )
//                + "/" + ( Dungeon.hero.belongings.getItem( Gunpowder.class ) != null ? Dungeon.hero.belongings.getItem( Gunpowder.class ).quantity : "-" )
//        ) : null ;
//    }

    @Override
    public String equipAction() {
        return loaded ? AC_SHOOT : AC_RELOAD ;
    }

    @Override
    public boolean checkAmmo( Hero hero, boolean showMessage ) {

        if (!isEquipped(hero)) {

            if( showMessage ) {
                GLog.n(TXT_NOTEQUIPPED);
            }

        } else if ( !loaded ) {

//            if( showMessage ) {
//                GLog.n(TXT_NOT_LOADED);
//            }

            execute( hero, AC_RELOAD );

        } else  if (ammunition() == null || !ammunition().isInstance( hero.belongings.weap2 )) {

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

            if( !loaded ){

                execute( hero, AC_RELOAD );

            } else if ( checkAmmo( hero, true ) ){

                GameScene.selectCell(shooter);

            }

        } else if (action == AC_RELOAD) {

            curUser = hero;

            if( reload(hero ) ) {

                curUser.sprite.operate( curUser.pos );

                curUser.spend( curUser.attackDelay() * 0.5f );

                hero.buff( Satiety.class ).decrease( Satiety.POINT * str() / hero.STR() * 0.5f );
                hero.busy();

            } else if (!isEquipped(hero)) {

                GLog.n( TXT_NOTEQUIPPED );
                hero.ready();

            } else if ( loaded ) {

                GLog.n( TXT_ALREADY_LOADED );
                hero.ready();

            } else {

                GLog.n( TXT_POWDER_NEEDED );
                hero.ready();

            }

        } else {

            super.execute( hero, action );

        }
    }

    public boolean reload( Hero hero){

        curItem = this;

        Item powder = Dungeon.hero.belongings.getItem( Explosives.Gunpowder.class );

        if ( isEquipped(hero) && !loaded && powder != null && tier <= powder.quantity ) {

            loaded = true;

            if( powder.quantity <= tier ){

                powder.detachAll( Dungeon.hero.belongings.backpack );

            } else {
                powder.quantity -= tier;
            }

            curItem.updateQuickslot();

            Sample.INSTANCE.play( Assets.SND_TRAP, 0.6f, 0.6f, 0.5f );

            hero.sprite.showStatus( CharSprite.DEFAULT, TXT_RELOADING );

            return true;

        } else {

            return false;

        }
    }

    public static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                final RangedWeaponFlintlock curWeap = (RangedWeaponFlintlock)RangedWeaponFlintlock.curItem;

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
                            reset(curUser.pos, cell, curUser.belongings.weap2, 3.0f, new Callback() {
                                @Override
                                public void call() {
                                    ((ThrowingWeaponAmmo) curUser.belongings.weap2).onShoot(cell, curWeap);
                                }
                            });

                    curUser.buff( Satiety.class ).decrease( Satiety.POINT * curWeap.str() / curUser.STR() );
                    curWeap.loaded = false;
                    curWeap.use( 2 );

                    Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + curWeap.tier * 0.2f, 0.4f + curWeap.tier * 0.2f, 1.55f - curWeap.tier * 0.15f);
                    Camera.main.shake(curWeap.tier, 0.1f);

                    PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
                    PointF pt = DungeonTilemap.tileCenterToWorld(cell);

                    curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + curWeap.tier);
                    Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + curWeap.tier);

                    }
                });

                for (Mob mob : Dungeon.level.mobs) {
                    if ( Level.distance( curUser.pos, mob.pos ) <= 3 + curWeap.tier && mob.pos != cell ) {
                        mob.beckon( curUser.pos );
                    }
                }

                Invisibility.dispel();

            }
        }

        @Override
        public String prompt() {
            return "Choose target to shoot at";
        }
    };

    private static final String LOADED	= "loaded";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LOADED, loaded );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        loaded = bundle.getBoolean( LOADED );
    }
}