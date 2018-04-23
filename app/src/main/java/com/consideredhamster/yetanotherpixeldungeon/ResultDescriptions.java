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
package com.consideredhamster.yetanotherpixeldungeon;

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarfMonk;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarvenKing;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.GnollBrute;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Golem;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mimic;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Piranha;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Rat;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.BoulderTrap;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.Trap;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public abstract class ResultDescriptions {

//    public static final String FAIL	= "%s";
    public static final String WIN	= "Obtained the Amulet of Yendor";

    public static String generateResult( Object killedBy, Element killedWith ) {

        return Utils.capitalize( killedBy == Dungeon.hero ? killedWith( killedBy, killedWith ) +
                ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ? " herself" : " himself" ) :
                killedWith( killedBy, killedWith ) + " by " + killedBy( killedBy ) );
    }

    public static String generateMessage( Object killedBy, Element killedWith ) {

        return ( killedBy == Dungeon.hero ? "You " + killedWith( killedBy, killedWith ) + " yourself" :
                "You were " + killedWith( killedBy, killedWith ) + " by " + killedBy( killedBy ) ) + "...";
    }

    private static String killedWith( Object killedBy, Element killedWith ) {

        String result = "killed";

        if( killedWith == null ) {

            if( killedBy instanceof Mob) {

                Mob mob = (Mob)killedBy;

                if( Bestiary.isBoss( mob ) || mob instanceof Rat ) {

                    result = "defeated";

                } else if ( mob instanceof GnollBrute ) {

                    result = "murderized";

                } else if ( mob instanceof DwarfMonk ) {

                    result = "facefisted";

                } else if ( mob instanceof Golem ) {

                    result = "squashed flat";

                } else if ( mob instanceof Piranha ) {

                    result = "eaten";

                } else if ( mob instanceof Mimic ) {

                    result = "ambushed";

                }

            } else if( killedBy instanceof DwarvenKing.BoneExplosion ) {

                result = "boned";

            } if( killedBy instanceof DwarvenKing.KnockBack ) {

                result = "crushed";

            } if( killedBy instanceof BoulderTrap ) {

                result = "crushed";

            }


//        } else if( killedWith instanceof DamageType.Flame) {
//            result = "burned to crisp";
//        } else if( killedWith instanceof DamageType.Frost) {
//            result = "chilled to death";
        } else if( killedWith instanceof Element.Shock) {
            result = "electrocuted";
        } else if( killedWith instanceof Element.Acid) {
            result = "dissolved";
//        } else if( killedWith instanceof DamageType.Mind) {
//            result = "";
//        } else if( killedWith instanceof DamageType.Body) {
//            result = "drained";
//        } else if( killedWith instanceof DamageType.Unholy) {
//            result = "withered";
//        } else if( killedWith instanceof DamageType.Energy) {
//            result = "disintegrated";
        }

        return result;
    }

    private static String killedBy( Object killedBy ) {

        String result = "something";

        if( killedBy instanceof Mob ) {
            Mob mob = ((Mob)killedBy);
            result = ( !Bestiary.isBoss( mob ) ? Utils.indefinite( mob.name ) : mob.name );
        } else if( killedBy instanceof Blob ) {
            Blob blob = ((Blob)killedBy);
            result = Utils.indefinite( blob.name );
        } else if( killedBy instanceof Weapon.Enchantment ) {
            result = "cursed weapon";
        } else if( killedBy instanceof Armour.Glyph ) {
            result = "cursed armor";
        } else if( killedBy instanceof Buff ) {
            if( killedBy instanceof Crippled ) {
                result = "excessive bleeding";
            } else if( killedBy instanceof Poisoned ) {
                result = "poison";
            } else if( killedBy instanceof Satiety ) {
                result = "starvation";
            } else if( killedBy instanceof Burning ) {
                result = "being burned alive";
            } else if( killedBy instanceof Corrosion ) {
                result = "caustic ooze";
            }
        } else if( killedBy instanceof Trap ) {
            result = "a trap";
        } else if( killedBy instanceof Chasm ) {
            result = "gravity";
        } else if( killedBy instanceof DwarvenKing.BoneExplosion ) {
            result = "an explosion";
        } else if( killedBy instanceof DwarvenKing.KnockBack ) {
            result = "knockback";
        }

        return result;
    }

}
