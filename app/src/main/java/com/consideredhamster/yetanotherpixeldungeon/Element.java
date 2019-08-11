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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Shocked;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.watabou.utils.Random;

import java.util.Map;

public abstract class Element {

    public abstract int proc( Char target, int damage );

    public static final Physical PHYSICAL = new Physical();
    public static final Physical.Falling FALLING = new Physical.Falling();

    public static final Knockback KNOCKBACK = new Knockback();
    public static final Ensnaring ENSNARING = new Ensnaring();

    public static final Flame FLAME = new Flame();
    public static final Flame.Periodic FLAME_PERIODIC = new Flame.Periodic();

    public static final Acid ACID = new Acid();
    public static final Acid.Periodic ACID_PERIODIC = new Acid.Periodic();

    public static final Shock SHOCK = new Shock();
    public static final Shock.Periodic SHOCK_PERIODIC = new Shock.Periodic();

    public static final Mind MIND = new Mind();
    public static final Body BODY = new Body();
    public static final Dispel DISPEL = new Dispel();

    public static final Frost FROST = new Frost();
    public static final Energy ENERGY = new Energy();

    public static final Unholy UNHOLY = new Unholy();
    public static final Doom DOOM = new Doom();


    public static class Flame extends Element {

        @Override
        public int proc( Char target, int damage ) {

            if (target.sprite.visible) {
                target.sprite.emitter().burst(FlameParticle.FACTORY, (int) Math.sqrt( damage / 2 ) + 1);
            }

            if( Random.Float() < 0.5f ){
                BuffActive.addFromDamage( target, Burning.class, damage * 2 );
            }

            return damage;
        }

        public static class Periodic extends Flame {

            @Override
            public int proc( Char target, int damage ) {
                return damage;
            }

        }
    }

    public static class Shock extends Element {

        @Override
        public int proc( Char target, int damage ) {

            if( !target.flying && Level.water[ target.pos ] ){
                damage += ( damage / 2 + Random.Int( damage % 2 + 1 ) );
            }

//            Shocked buff = target.buff( Shocked.class );
//
//            if( buff != null ){
//
//                buff.discharge();
//
//            } else {
//
//                if( Random.Float() < 0.25f ){
//                    BuffActive.addFromDamage( target, Shocked.class, damage * 2 );
//                }
//
////                if (target.sprite.visible) {
////                    target.sprite.centerEmitter().burst( SparkParticle.FACTORY, (int)Math.sqrt( damage ) + 1 );
////                }
//
//            }

            return damage;
        }

        public static class Periodic extends Shock {

            @Override
            public int proc( Char target, int damage ) {
                return damage;
            }

        }
    }

    public static class Acid extends Element {

        @Override
        public int proc( Char target, int damage ) {

            if (target.sprite.visible) {
                target.sprite.burst(0x006600, (int) Math.sqrt(damage / 2) + 1);
            }

            if( Random.Float() < 0.75f ){
                BuffActive.addFromDamage( target, Corrosion.class, damage * 2 );
            }

            return damage;
        }

        public static class Periodic extends Acid {

            @Override
            public int proc( Char target, int damage ) {
                return damage;
            }

        }
    }

    public static class Frost extends Element {

        @Override
        public int proc( Char target, int damage ) {

//            if (target.sprite.visible) {
//                CellEmitter.get(target.pos).start(SnowParticle.FACTORY, 0.2f, 6);
//            }
//
//            if ( damage < target.HP && Random.Int( target.HT * 2 ) < damage * damage / 2 ) {
//                Buff.affect(target, Frozen.class, (Level.water[target.pos] && !target.flying
//                        ? Random.Float(2.0f, 3.0f) : Random.Float(1.0f, 1.5f)));
//            }

            return damage;
        }
    }

    public static class Energy extends Element {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Body extends Element {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Mind extends Element {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Unholy extends Element {

        @Override
        public int proc( Char target, int damage ) {

//            if ( Random.Int( target.HT ) < damage * damage / 2 ) {
                BuffActive.addFromDamage(target, Withered.class, damage * 2 );

//                if( damage < target.HP && buff != null ) {
//
//                    damage *= buff.modify();
//
//                }
//            }

            return damage;

        }
    }

    public static class Dispel extends Element {

        @Override
        public int proc( Char target, int damage ) {

            return damage;

        }
    }

    public static class Doom extends Element {

        @Override
        public int proc( Char target, int damage ) {

            return damage;

        }
    }


    public static class Physical extends Element {

        @Override
        public int proc( Char target, int damage ){
            return damage;
        }

        public static class Falling extends Physical {
            @Override
            public int proc( Char target, int damage ){
                BuffActive.addFromDamage( target, Crippled.class, damage );
                return damage;
            }
        }
    }

    public static class Knockback extends Element {
        @Override
        public int proc( Char target, int damage ){
            // not actually needed, because knockback deals physical damage
            // maybe make some separate class from Element for such effects?
            return damage;
        }
    }

    public static class Ensnaring extends Element {
        @Override
        public int proc( Char target, int damage ){
            // yeah, I guess I really should add a separate class for cases like these
            return damage;
        }
    }

    public static class Resist {

        public static final float VULNERABLE = -1.0f;
        public static final float DEFAULT = 0.0f;
        public static final float PARTIAL = 0.5f;
        public static final float IMMUNE = 1.0f;

        public static int modifyValue( int value, Char target, Element type ) {

            float resist = Element.Resist.getResistance( target, type );

            if( !Element.Resist.checkIfDefault( resist ) ) {

                if ( Element.Resist.checkIfNegated( resist ) ) {

                    value = 0;

                } else if ( Element.Resist.checkIfPartial( resist ) ) {

                    value = value / 2 + Random.Int( value % 2 + 1 );

                } else if ( Element.Resist.checkIfAmplified( resist ) ) {

                    value = value * 3 / 2 + Random.Int( value % 2 + 1 );

                }

            }

            return value;
        }

        public static float getResistance( Char target, Element type ) {

            Float resistance = DEFAULT;

            for( Map.Entry<Class<? extends Element>, Float> entry : target.resistances().entrySet() ) {
                if( entry.getKey().isInstance( type ) ) {
                    resistance = entry.getValue();
                    break;
                }
            }

            return resistance;

        }

        public static boolean checkIfDefault( float resistance ) {
            return resistance == DEFAULT;
        }

        public static boolean checkIfNegated( float resistance ) {
            return resistance > IMMUNE || resistance > Random.Float( PARTIAL, IMMUNE );
        }

        public static boolean checkIfPartial( float resistance ) {
            return resistance > PARTIAL || resistance > Random.Float( DEFAULT, PARTIAL );
        }

        public static boolean checkIfAmplified( float resistance ) {
            return resistance < VULNERABLE || resistance < Random.Float( VULNERABLE, DEFAULT );
        }
    }
}


