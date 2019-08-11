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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Blinded;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.HolyLight;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShaftParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfSmiting extends WandCombat {

	{
		name = "Wand of Smiting";
        image = ItemSpriteSheet.WAND_SMITING;

        goThrough = false;
        hitChars = false;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.05f;
    }

	@Override
	protected void onZap( int cell ) {

        if( Level.solid[ cell ] ) {
            cell = Ballistica.trace[ Ballistica.distance - 1 ];
        }

		for ( int n : Level.NEIGHBOURS9 ){

            Char ch = Actor.findChar( cell + n );

            if( ch != null && ch != curUser ){

                int power = n == 0 ? damageRoll() : damageRoll() / 2 ;

                ch.damage( power, curUser, Element.ENERGY );

                if( ch.isMagical() ) {
                    power += ( power / 2 + Random.Int( power % 2 + 1 ) );
                }

                if( Random.Int( 4 ) == 0 ){
                    BuffActive.add( ch, Blinded.class, power );
                }

                if( ch.sprite.visible ){
                    ch.sprite.emitter().start( Speck.factory( Speck.HOLY ), 0.05f, n == 0 ? 6 : 3 );
                }
            }
        }

	}

    @Override
	protected void fx( int cell, Callback callback ) {

        if( Level.solid[ cell ] ) {
            cell = Ballistica.trace[ Ballistica.distance - 1 ];
        }

        if( Dungeon.visible[ cell ] ){

            Sample.INSTANCE.play( Assets.SND_LEVELUP, 1.0f, 1.0f, 0.5f );

            new Flare( 6, 24 ).color( SpellSprite.COLOUR_HOLY, true ).show( cell, 1f );
            CellEmitter.top( cell ).burst( ShaftParticle.FACTORY, 4 );
//            GameScene.flash( SpellSprite.COLOUR_HOLY - 0x555555 );
            HolyLight.createAtPos( cell );

        }

        Sample.INSTANCE.play( Assets.SND_ZAP );

        callback.call();
    }
	
	@Override
	public String desc() {
		return
			"This gilded piece of wood allows its user to channel and release bursts of hallowed " +
            "energy, harming and sometimes even blinding any wrongdoer caught in its area of " +
            "effect. Its effects are even stronger against undead or magical foes.";
	}
}
