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

package com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;

public abstract class MeleeWeaponLightOH extends MeleeWeapon {

    private static final String TXT_EQUIP_TITLE = "Equipping secondary weapon";
    private static final String TXT_EQUIP_MESSAGE =
            "Because this is a light one-handed weapon, you can equip it as your off-hand weapon. " +
            "It will require additional strength to be properly wielded, and may decrease your " +
            "combat proficiency. Do you want to equip it as a main weapon or as an off-hand weapon?";

    private static final String TXT_EQUIP_PRIMARY = "main hand";
    private static final String TXT_EQUIP_SECONDARY = "offhand";

    public MeleeWeaponLightOH(int tier) {

        super(tier);

    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " _. It can be used as off-hand weapon, " +
//                "but its strength requirement will be increased that way.";
        return "light one-handed";
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) - 8;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() - 1;
    }

    @Override
    public int strShown( boolean identified ) {
        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap2 && incompatibleWith( Dungeon.hero.belongings.weap1 ) ?
                        Dungeon.hero.belongings.weap1.str(
                            Dungeon.hero.belongings.weap1.isIdentified() ?
                            Dungeon.hero.belongings.weap1.bonus : 0
                        ) : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return this == Dungeon.hero.belongings.weap2 && item instanceof MeleeWeapon ; }

    @Override
    public boolean doEquip( final Hero hero ) {

        if ( hero.belongings.weap1 != null ) {

            YetAnotherPixelDungeon.scene().add(
                    new WndOptions(TXT_EQUIP_TITLE, TXT_EQUIP_MESSAGE,
                            Utils.capitalize( TXT_EQUIP_PRIMARY ),
                            Utils.capitalize( TXT_EQUIP_SECONDARY ) ) {

                        @Override
                        protected void onSelect( int index ) {

                            detach( hero.belongings.backpack );

                            if (index == 0) {
//                            if (index == 0 && ( hero.belongings.weap1 == null || hero.belongings.weap1.doUnequip( hero, true, false ) ) ) {

                                doEquipPrimary(hero);

                            } else if (index == 1) {
//                            } else if (index == 1 && ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) ) {

                                doEquipSecondary( hero );

                            } else {

                                collect( hero.belongings.backpack );

                            }
                        }
                    } );

            return false;

        } else {

           return super.doEquip( hero );

        }
    }


    public boolean doEquipPrimary( Hero hero ) {
        return super.doEquip( hero );
    }

    public boolean doEquipSecondary( Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

//            if( QuickSlot.quickslot1.value == this && hero.belongings.weap2 != null )
//                QuickSlot.quickslot1.value = hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;
//
//            if( QuickSlot.quickslot2.value == this && hero.belongings.weap2 != null )
//                QuickSlot.quickslot2.value = hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip(hero, true, false) ) &&
                ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                hero.belongings.weap2 = this;
                activate(hero);

                onEquip( hero );

                QuickSlot.refresh();

                hero.spendAndNext(time2equip(hero));
                return true;

            } else {

                QuickSlot.refresh();
                hero.spendAndNext(time2equip(hero) * 0.5f);

                if ( !collect( hero.belongings.backpack ) ) {
                    Dungeon.level.drop( this, hero.pos ).sprite.drop();
                }

                return false;

            }
        } else {

            GLog.w(TXT_ISEQUIPPED, name());
            return false;

        }
    }

}
