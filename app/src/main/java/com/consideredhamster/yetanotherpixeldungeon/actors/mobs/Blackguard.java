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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Harpoons;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Chains;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.BlackguardSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Blackguard extends MobHealthy {

    public Blackguard() {

        super( 20 );

        /*
            base maxHP  = 50
            armor class = 20

            damage roll = 8-27

            accuracy    = 23
            dexterity   = 20

            perception  = 75%
            stealth     = 75%

         */

        name = "blackguard";
        info = "Magical, Harpoon throwing";
        spriteClass = BlackguardSprite.class;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
//        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );

//        resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.PARTIAL );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }
	@Override
	protected boolean canAttack( Char enemy ) {
		return super.canAttack( enemy ) || Level.distance(pos, enemy.pos) <= 2
				&& Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos
				&& Element.Resist.getResistance( enemy, Element.KNOCKBACK ) < Element.Resist.IMMUNE
				;
	}
	
	@Override
	protected void onRangedAttack( int cell ) {
		((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
				reset(pos, cell, new Harpoons(), new Callback() {
					@Override
					public void call() {
						onCastComplete();
					}
				});
		
		sprite.parent.add( new Chains( pos, cell, false ) );
		
		super.onRangedAttack( cell );
	}
	
	@Override
	public boolean cast( final Char enemy ) {

		sprite.parent.add( new Chains( pos, enemy.pos, true ) );
		
		if ( hit( this, enemy, true, false ) ) {
			
			int distance = 1;
			
			distance = Element.Resist.modifyValue( distance, enemy, Element.KNOCKBACK );
			
			if( distance > 0 ) {
				
				final int newPos = Ballistica.trace[ Math.max( 1, Ballistica.distance - distance ) ];
				
				Pushing.move( enemy, newPos, new Callback() {
					@Override
					public void call() {
						Actor.moveToCell( enemy, newPos );
						Dungeon.level.press( newPos, enemy );
					}
				} );
				
				spend( -1 / attackSpeed() );
			}
			
			if ( Dungeon.visible[ enemy.pos ] ) {
				Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			}
			
		} else {
			
			enemy.missed();
			
		}
		
		return true;
	}

	@Override
	public String description() {
		return
			"These demonic juggernauts are a pure embodiment of unrelenting rage. Their skin harder " +
			"than any armor, they instead bind their bodies with heavy chains, which can also be " +
			"used to pull their victims closer before pounding them to death with their bare fists.";
	}

}
