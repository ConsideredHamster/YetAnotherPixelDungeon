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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Controlled;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.DrainWill;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.DrainLife;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfLifeDrain extends WandUtility {

	{
		name = "Wand of Life Drain";
        image = ItemSpriteSheet.WAND_LIFEDRAIN;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.85f;
    }

    @Override
    protected void onZap( int cell ){

        Char ch = Actor.findChar( cell );

        if (ch != null ) {

            int damage = damageRoll();

            // zero dexterity usually means that target is asleep or wandering
            if( ch.dexterity() == 0 ) {
                damage += damage / 2 + Random.Int( damage % 2 + 1 );
//                ch.sprite.showStatus( CharSprite.DEFAULT, "sneak zap!" );
            }

            int healing = ch.HP;

            ch.damage( damage, curUser, Element.ENERGY );

            healing -= ch.HP;

            healing = Element.Resist.modifyValue( healing, ch, Element.BODY );

            if( healing > 0 && ch != curUser ){
                curUser.heal( healing );
            }

        } else {

            GLog.i( "nothing happened" );

        }
    }

    @Override
    protected void fx( int cell, Callback callback ) {

        Char target = Actor.findChar( cell );

        if (target != null && target != curUser ) {

            if( !target.isMagical() ) {

                curUser.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 5 );
                curUser.sprite.parent.add( new DrainLife( curUser.pos, cell, null ) );

                if( target.sprite.visible ){
                    new Flare( 6, 20 ).color( SpellSprite.COLOUR_DARK, true ).show( target.sprite, 0.5f );
                }

            } else {

//                target.sprite.emitter().start( Speck.factory( Speck.CONTROL ), 0.5f, 5 );
                curUser.sprite.parent.add( new DrainWill( curUser.pos, cell, null ) );

                if( target.sprite.visible ){
                    new Flare( 6, 20 ).color( SpellSprite.COLOUR_WILD, true ).show( target.sprite, 0.5f );
                }
            }

        } else {

            curUser.sprite.parent.add( new DrainLife( curUser.pos, cell, null ) );

        }

        Sample.INSTANCE.play(Assets.SND_ZAP);
        callback.call();

    }

	@Override
	public String desc() {
		return
			"This wand will allow you to steal life energy from living creatures to restore your " +
            "own health. Using it against non-living creatures will just harm them, but it is " +
            "especially effective against targets which are sleeping or otherwise unaware of danger.";
	}
}
