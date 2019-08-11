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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class Trap {

    private static final String TXT_HIDDEN_PLATE_CLICKS = "A hidden pressure plate clicks!";
    private static final String TXT_TRAPPED = "This tile is trapped!";

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
                new WndOptions( TXT_TRAPPED, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
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


    public static void trigger( int cell ) {

        Char ch = Actor.findChar( cell  );

        if (ch == Dungeon.hero) {
            Dungeon.hero.interrupt();
        }

        if( Dungeon.visible[cell] ) {

            if( ( Terrain.flags[ Dungeon.level.map[ cell ] ] & Terrain.TRAPPED ) != 0 ) {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS);
            }

            Sample.INSTANCE.play( Assets.SND_TRAP);
        }


        int trap = Dungeon.level.map[cell];
        Level.set( cell, Terrain.INACTIVE_TRAP);
        GameScene.updateMap( cell );

        switch ( trap ) {

            case Terrain.SECRET_TOXIC_TRAP:
            case Terrain.TOXIC_TRAP:
                ToxicTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_FIRE_TRAP:
            case Terrain.FIRE_TRAP:
                FireTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_BOULDER_TRAP:
            case Terrain.BOULDER_TRAP:
                BoulderTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_POISON_TRAP:
            case Terrain.POISON_TRAP:
                ConfusionTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_ALARM_TRAP:
            case Terrain.ALARM_TRAP:
                AlarmTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_LIGHTNING_TRAP:
            case Terrain.LIGHTNING_TRAP:
                LightningTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_BLADE_TRAP:
            case Terrain.BLADE_TRAP:
                BladeTrap.trigger( cell, ch );
                break;

            case Terrain.SECRET_SUMMONING_TRAP:
            case Terrain.SUMMONING_TRAP:
                SummoningTrap.trigger( cell, ch );
                break;
        }

    }
}
