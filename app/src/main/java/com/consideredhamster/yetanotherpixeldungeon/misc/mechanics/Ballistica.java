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
package com.consideredhamster.yetanotherpixeldungeon.misc.mechanics;

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public final class Ballistica {

	public static int[] trace = new int[Math.max( Level.WIDTH, Level.HEIGHT )];
	public static int distance;

    private static int stepA;
    private static int stepB;
    private static int dA;
    private static int dB;

    private static boolean hit;

    public static int cast( int from, int to, boolean goThrough, boolean hitChars ) {

//        GLog.i( "from=" + from);
//        GLog.i( "to=" + to);

        if( from == to ){
            distance = 0;
            trace[0] = from;
            return to;
        }

        int w = Level.WIDTH;

        int x0 = from % w;
        int x1 = to % w;
        int y0 = from / w;
        int y1 = to / w;

        int dx = x1 - x0;
        int dy = y1 - y0;

        int stepX = dx > 0 ? +1 : -1;
        int stepY = dy > 0 ? +1 : -1;

        dx = Math.abs( dx );
        dy = Math.abs( dy );

        if (dx > dy) {

            stepA = stepX;
            stepB = stepY * w;
            dA = dx;
            dB = dy;

        } else {

            stepA = stepY * w;
            stepB = stepX;
            dA = dy;
            dB = dx;

        }

        int cell = Ballistica.calc( from, to, hitChars, goThrough, dA / 2 );

        if ( !hit ) {

            for (int err = 0; err <= dA; err++) {

                int calc = Ballistica.calc( from, to, hitChars, goThrough, err );

                if ( hit ) {
                    return calc;
                }
            }

            // to avoid distance being calculated incorrectly when throwing behind a wall
            // not the best way to do it, gotta fix that sometimes later (I hope)
            if( !hit ) {
                return Ballistica.calc( from, to, hitChars, goThrough, dA / 2 );
            }
        }

        return cell;

    }

    private static int calc( int from, int to, boolean hitChars, boolean goThrough, int err ) {

        hit = false;
        distance = 0;
        trace[0] = from;

        int cell = from;

        while ( ( !hit || goThrough ) && distance < trace.length - 1 ) {

            cell += stepA;

            err += dB;

            if (err >= dA) {
                err = err - dA;
                cell = cell + stepB;
            }

            if (cell == to) {
                hit = true;
            }

            distance++;
            trace[distance] = cell;
//            trace[ distance + 1 ] = 0;

            if ( cell > 0 && cell < Level.solid.length ) {
                if (Level.solid[cell] || ( hitChars && Actor.findChar(cell) != null ) ) {
                    return trace[distance];
                }
//            } else {
//                return trace[ distance - 1 ];
            }

            /*// basically if current cell is not a wall
            if ( Level.passable[ cell ] || Level.illusory[ cell ] || Level.avoid[ cell ] ) {

                distance++;
                trace[ distance ] = cell;
                trace[ distance + 1 ] = 0;

                // doors are also solid yet do not count as walls
                if ( Level.solid[ cell ] || ( hitChars && Actor.findChar( cell ) != null ) ) {
                    return trace[ distance ];
                }

            } else {

                // we need to keep the next cell for beam reflection logic
                trace[ distance + 1 ] = cell;
                return trace[ distance ];

            } */
        }

        // we need to reset the next cell for beam reflection logic
//        trace[ distance + 1 ] = 0;

        return to;
    }

}
