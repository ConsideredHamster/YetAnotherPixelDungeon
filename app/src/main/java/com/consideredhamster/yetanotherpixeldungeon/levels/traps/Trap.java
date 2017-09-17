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
package com.consideredhamster.yetanotherpixeldungeon.levels.traps;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndOptions;

public abstract class Trap {

    private static final String TXT_TILE_IS_TRAPPED = "This tile is trapped!";

    private static final String TXT_R_U_SURE =
            "You are aware of a trap on this tile. Once you step on it, the trap would be " +
            "activated, which would most likely be quite a painful experience. Are you " +
            "REALLY sure you want to step here?";

    private static final String TXT_YES			= "Yes, I know what I'm doing";
    private static final String TXT_NO			= "No, I changed my mind";

    public static boolean stepConfirmed = false;

    public static boolean itsATrap( int terrain ){
        switch( terrain ) {

            case Terrain.TOXIC_TRAP:
            case Terrain.FIRE_TRAP:
            case Terrain.BOULDER_TRAP:
            case Terrain.POISON_TRAP:
            case Terrain.ALARM_TRAP:
            case Terrain.LIGHTNING_TRAP:
            case Terrain.BLADE_TRAP:
            case Terrain.SUMMONING_TRAP:
                return true;

            default:
                return false;

        }
    }

    public static void askForConfirmation( final Hero hero ) {
        GameScene.show(
                new WndOptions( TXT_TILE_IS_TRAPPED, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            stepConfirmed = true;
                            hero.resume();
                            stepConfirmed = false;
                        }
                    }
                }
        );
    }

//    public static void heroPressed(){
//        stepConfirmed = false;
//    }

}
