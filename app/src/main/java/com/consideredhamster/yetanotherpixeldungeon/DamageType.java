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

import com.watabou.noosa.Camera;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Cripple;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SnowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class DamageType {

    public abstract int proc( Char target, int damage );

    public static final Flame FLAME = new Flame();
//    public static final Flame.Periodic FLAME_PERIODIC = new Flame.Periodic();

    public static final Acid ACID = new Acid();
//    public static final Acid.Periodic ACID_PERIODIC = new Acid.Periodic();

    public static final Frost FROST = new Frost();
    public static final Shock SHOCK = new Shock();

    public static final Mind MIND = new Mind();
    public static final Body BODY = new Body();

    public static final Dispel DISPEL = new Dispel();
    public static final Energy ENERGY = new Energy();
    public static final Unholy UNHOLY = new Unholy();
    public static final Falling FALLING = new Falling();

    public static class Flame extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

            if (target.sprite.visible) {
                target.sprite.emitter().burst(FlameParticle.FACTORY, (int) Math.sqrt(damage / 2) + 1);
            }

            if ( damage < target.HP && Random.Int( target.HT ) < damage * damage / 2 ) {
                Burning buff = Buff.affect( target, Burning.class );

                if( buff != null ) {
                    buff.reignite(target);
                }
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

    public static class Shock extends DamageType {

        private static final String TXT_DISARMED = "Sudden shock have made you drop your %s on the ground!";

        @Override
        public int proc( Char target, int damage ) {

            if(Level.water[target.pos] && !target.flying ) {
                damage += damage / 2;
            }

            if ( damage < target.HP && Random.Int(target.HT) < damage ) {

                target.sprite.showStatus(CharSprite.NEGATIVE, "shocked");

                if( target instanceof Hero ) {

                    Camera.main.shake( 2, 0.3f );

                    Hero hero = (Hero)target;
                    EquipableItem weapon = Random.oneOf( hero.belongings.weap1, hero.belongings.weap2 );

                    if( weapon != null && weapon.disarmable() ) {

                        GLog.w(TXT_DISARMED, weapon.name());
                        weapon.doDrop(hero);

                    }

                } else {

                    target.delay( Random.IntRange( 1, 2 ) );

                }
            }

            if (target.sprite.visible) {
                target.sprite.centerEmitter().burst( SparkParticle.FACTORY, (int) Math.sqrt(damage / 2) + 1 );
            }

            return damage;
        }
    }

    public static class Acid extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

            if (target.sprite.visible) {
                target.sprite.burst(0x006600, (int) Math.sqrt(damage / 2) + 1);
            }

            if ( damage < target.HP && damage > 0 ) {
                Buff.affect(target, Ooze.class);
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

    public static class Frost extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

            if (target.sprite.visible) {
                CellEmitter.get(target.pos).start(SnowParticle.FACTORY, 0.2f, 6);
            }

            if ( damage < target.HP && Random.Int( target.HT * 2 ) < damage * damage / 2 ) {
                Buff.affect(target, Frozen.class, (Level.water[target.pos] && !target.flying
                        ? Random.Float(2.0f, 3.0f) : Random.Float(1.0f, 1.5f)));
            }

            return damage;
        }
    }

    public static class Energy extends DamageType {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Body extends DamageType {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Mind extends DamageType {

        @Override
        public int proc( Char target, int damage ) {
            return damage;
        }
    }

    public static class Unholy extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

//            if ( Random.Int( target.HT ) < damage * damage / 2 ) {
                Withered buff = Buff.affect(target, Withered.class);

                if( damage < target.HP && buff != null ) {

                    damage /= buff.modifier();

                    buff.prolong();

                }
//            }

            return damage;

        }
    }

    public static class Dispel extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

            return damage;

        }
    }

    public static class Falling extends DamageType {

        @Override
        public int proc( Char target, int damage ) {

            if( damage < target.HP ) {
                Buff.affect(target,
                        Cripple.class, damage * Cripple.DURATION * 2 / target.HT);
            }

            return damage;

        }
    }
}


