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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Explosives;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class Elemental extends MobPrecise {

    private static String TXT_ENSNARED = "Elemental burns its snares!";
    private static String TXT_FROZEN = "Freezing hurts elemental!";

    public Elemental() {

        super( 14 );

        name = "fire elemental";
        spriteClass = ElementalSprite.class;

        flying = true;
        armorClass = 0;

		loot = Explosives.Gunpowder.class;
		lootChance = 0.25f;
    }

    @Override
    public DamageType damageType() {
        return DamageType.FLAME;
    }

    @Override
    public int damageRoll() {
        return super.damageRoll() / 2 ;
    }

//	@Override
//	public int attackProc( Char enemy, int damage ) {
//		if (Random.Int( 2 ) == 0) {
//			Buff.affect( enemy, Burning.class ).reignite( enemy );
//		}
//
//		return damage;
//	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.fire(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }



    @Override
    public boolean cast( Char enemy ) {

        if (hit( this, enemy, true, true )) {

            enemy.damage( damageRoll(), this, DamageType.FLAME );

        } else {

            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );

        }

        return true;
    }

    @Override
    public void damage( int dmg, Object src, DamageType type ) {

        if ( type == DamageType.FLAME ) {

            if (HP < HT) {
                int reg = Math.min( dmg / 2, HT - HP );

                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                }
            }

        } else {

            super.damage(dmg, src, type);

        }
    }
	
	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Burning) {
			if (HP < HT) {
                int reg = Math.max( 1, (HT - HP) / 5 );

                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), (int)Math.sqrt( reg ) );
                }
			}

            return false;

		} else if (buff instanceof Frozen) {

            damage( Random.NormalIntRange( HT / 2 , HT ), null, null );

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_FROZEN );
            }

            return false;

        } else if (buff instanceof Ensnared) {

            GameScene.add(Blob.seed(pos, 1, Fire.class));

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_ENSNARED );
            }

            return false;

        } else {

            return super.add( buff );

        }
	}

//    protected boolean doAttack( Char enemy ) {
//
//        if (Level.adjacent( pos, enemy.pos )) {
//
//            return super.doAttack( enemy );
//
//        } else {
//
//            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
//            if (visible) {
//                sprite.cast(enemy.pos);
//            } else {
//                zap();
//            }
//
//            return !visible;
//        }
//    }
//
//    private void zap() {
//
//        spend( attackDelay() );
//
////        for (int i=1; i < Ballistica.distance - 1; i++) {
////            int c = Ballistica.trace[i];
////            if (Level.flammable[c]) {
////                GameScene.add( Blob.seed( c, 1, Fire.class ) );
////            }
////        }
////
////        if (!Level.water[enemy.pos]) {
////            GameScene.add(Blob.seed(enemy.pos, 1, Fire.class));
////        }
//
//        if (hit( this, enemy, true, true )) {
//
//            int dmg = damageRoll() / 2;
//            enemy.damage( dmg, this, DamageType.FLAME );
//
//        } else {
//            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
//        }
//    }

//    public void onZapComplete() {
//        zap();
//        next();
//    }

    @Override
    public void die( Object cause, DamageType dmg ) {

        if (Level.flammable[pos]) {
            GameScene.add(Blob.seed(pos, 1, Fire.class));
        }

        super.die( cause, dmg );
    }
	
	@Override
	public String description() {
		return
			"Wandering fire elementals are a byproduct of summoning greater entities. " +
			"They are too chaotic in their nature to be controlled by even the most powerful demonologist.";
	}
	
//	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
//
//	static {
//        IMMUNITIES.add( Fire.class );
//        IMMUNITIES.add( Leech.class );
//        IMMUNITIES.add( Poison.class );
//        IMMUNITIES.add( Burning.class );
//        IMMUNITIES.add( WandOfFirebolt.class );
//        IMMUNITIES.add( ScrollOfPsionicBlast.class );
//	}
//
//	@Override
//	public HashSet<Class<?>> immunities() {
//		return IMMUNITIES;
//	}
public static final HashSet<Class<? extends DamageType>> RESISTANCES = new HashSet<>();
    public static final HashSet<Class<? extends DamageType>> IMMUNITIES = new HashSet<>();

    static {
//        RESISTANCES.add(DamageType.Energy.class);

//        IMMUNITIES.add(DamageType.Flame.class);
        IMMUNITIES.add(DamageType.Acid.class);
        IMMUNITIES.add(DamageType.Body.class);
    }

    @Override
    public HashSet<Class<? extends DamageType>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<? extends DamageType>> immunities() {
        return IMMUNITIES;
    }
}
