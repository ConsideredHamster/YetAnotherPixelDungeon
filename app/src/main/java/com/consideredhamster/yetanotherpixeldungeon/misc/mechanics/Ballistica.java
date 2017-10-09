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

public class Ballistica {

	public static int[] trace = new int[Math.max( Level.WIDTH, Level.HEIGHT )];
	public static int distance;

    public static int[] trace_tmp = new int[Math.max( Level.WIDTH, Level.HEIGHT )];
    public static int distance_tmp;

    public static int cast( int from, int to, boolean goThrough, boolean hitChars ) {

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

        int stepA;
        int stepB;
        int dA;
        int dB;

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

        if( !goThrough ) {

            int cell = Ballistica.calc(from, to, hitChars, stepA, stepB, dA, dB, dA / 2);

            distance = distance_tmp;
            trace = trace_tmp.clone();

            if (cell != to) {

                for (int err = 0; err <= dA; err++) {
                    int calc = Ballistica.calc(from, to, hitChars, stepA, stepB, dA, dB, err);
                    if (calc == to) {
                        cell = calc;
                        distance = distance_tmp;
                        trace = trace_tmp.clone();
                    }
                }
            }

            return cell;

        } else {

            boolean hit = Ballistica.calcThrough(from, to, hitChars, stepA, stepB, dA, dB, dA / 2);

            distance = distance_tmp;
            trace = trace_tmp.clone();

            if ( !hit ) {
                for (int err = 0; err <= dA; err++) {
                    if (Ballistica.calcThrough(from, to, hitChars, stepA, stepB, dA, dB, err)) {
                        distance = distance_tmp;
                        trace = trace_tmp.clone();
                    }
                }
            }

            return trace[ distance ];
        }
    }

    public static int calc( int from, int to, boolean hitChars,
                            int stepA, int stepB, int dA, int dB, int err ) {

        distance_tmp = 1;
        trace_tmp[0] = from;

        int cell = from;

        while (cell != to) {

            cell += stepA;

            err += dB;
            if (err >= dA) {
                err = err - dA;
                cell = cell + stepB;
            }

            trace_tmp[distance_tmp++] = cell;

            if (!Level.passable[cell] && !Level.illusory[cell] && !Level.avoid[cell]) {
                return trace_tmp[--distance_tmp - 1];
            }

            if (Level.solid[cell] || (hitChars && Actor.findChar( cell ) != null)) {
//                break;
                return cell;
            }
        }

//        trace_tmp[distance_tmp++] = cell;

        return to;
    }

    public static boolean calcThrough( int from, int to, boolean hitChars,
                            int stepA, int stepB, int dA, int dB, int err ) {

        distance_tmp = 1;
        trace_tmp[0] = from;

        int cell = from;
        boolean hit = false;

        while (distance_tmp <= 8) {

            cell += stepA;

            err += dB;
            if (err >= dA) {
                err = err - dA;
                cell = cell + stepB;
            }

            trace_tmp[distance_tmp++] = cell;

            if (cell == to) {
                hit = true;
            }

            if (!Level.passable[cell] && !Level.illusory[cell] && !Level.avoid[cell]) {
//                return trace_tmp[--distance_tmp - 1];
                distance_tmp --;
                break;
            }

            if (Level.solid[cell] || (hitChars && Actor.findChar( cell ) != null)) {
//                return cell;
                break;
            }
        }

        distance_tmp --;

//        trace_tmp[distance_tmp++] = cell;

        return hit;
    }

//    public static int cast( int from, int to, boolean goThrough, boolean hitChars ) {
//
//        int w = Level.WIDTH;
//
//        int x0 = from % w;
//        int x1 = to % w;
//        int y0 = from / w;
//        int y1 = to / w;
//
//        int dx = x1 - x0;
//        int dy = y1 - y0;
//
//        int stepX = dx >
// 0 ? +1 : -1;
//        int stepY = dy > 0 ? +1 : -1;
//
//        dx = Math.abs( dx );
//        dy = Math.abs( dy );
//
//        int stepA;
//        int stepB;
//        int dA;
//        int dB;
//
//        if (dx > dy) {
//
//            stepA = stepX;
//            stepB = stepY * w;
//            dA = dx;
//            dB = dy;
//
//        } else {
//
//            stepA = stepY * w;
//            stepB = stepX;
//            dA = dy;
//            dB = dx;
//
//        }
//
//        distance = 1;
//        trace[0] = from;
//
//        int cell = from;
//
//        int err = dA / 2;
//        while (cell != to || goThrough) {
//
//            cell += stepA;
//
//            err += dB;
//            if (err >= dA) {
//                err = err - dA;
//                cell = cell + stepB;
//            }
//
//            trace[distance++] = cell;
//
//            if (!Level.passable[cell] && !Level.avoid[cell]) {
//                return trace[--distance - 1];
//            }
//
//            if (Level.losBlockHigh[cell] || (hitChars && Actor.findChar( cell ) != null)) {
//                return cell;
//            }
//        }
//
//        trace[distance++] = cell;
//
//        return to;
//    }
}
