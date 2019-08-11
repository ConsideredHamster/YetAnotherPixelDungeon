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
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ElementalSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Elemental extends MobPrecise {

    private static String TXT_ENSNARED = "Elemental burns its snares!";
    private static String TXT_FROZEN = "Freezing hurts elemental!";

    public Elemental() {

        super( 14 );

        /*

            base maxHP  = 35
            armor class = 0

            damage roll = 5-17 (2-8)

            accuracy    = 29
            dexterity   = 24

            perception  = 120%
            stealth     = 105%

         */

        name = "fire elemental";
        spriteClass = ElementalSprite.class;

        flying = true;
        armorClass = 0;

        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );

        resistances.put( Element.Frost.class, Element.Resist.VULNERABLE );

        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.FLAME;
    }

    @Override
    public int damageRoll() {
        return super.damageRoll() / 2 ;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
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

            enemy.damage( absorb(damageRoll(), enemy.armorClass(), true), this, Element.FLAME );

        } else {

            enemy.missed();
        }

        return true;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if ( type == Element.FLAME ) {

            heal( dmg / 2 );

        } else {

            super.damage(dmg, src, type);

        }
    }
	
	@Override
	public boolean add( Buff buff ) {

		if ( buff instanceof Burning ) {

            heal( ( (Burning) buff ).getDuration() );

            return false;

		} else if ( buff instanceof Frozen ) {

            int dmg = (int)Math.sqrt( totalHealthValue() / 2 ) * ( (Frozen) buff ).getDuration();

            damage( dmg, null, Element.FROST );

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_FROZEN );
            }

            return false;

        } else if ( buff instanceof Ensnared ) {

            GameScene.add( Blob.seed( pos, 1, Fire.class ) );

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_ENSNARED );
            }

            return false;

        } else {

            return super.add( buff );

        }
	}

    @Override
    public void die( Object cause, Element dmg ) {

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

}
