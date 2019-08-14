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

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Doomed;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.watabou.utils.Callback;

public class WandOfDamnation extends WandUtility {

	{
		name = "Wand of Damnation";
        image = ItemSpriteSheet.WAND_DAMNATION;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.75f;
    }
	
	@Override
	protected void onZap( int cell ) {

        Char ch = Actor.findChar( cell );

        if (ch != null) {

            int damage = Math.max( 1, damageRoll() );

            int duration = ch.currentHealthValue() * 2 / damage + 1;

            if( ch.sprite.visible ){
                ch.sprite.showStatus( CharSprite.BLACK, Integer.toString( duration ) );
            }

            BuffActive.add( ch, Doomed.class, (float)duration );
            CellEmitter.center( cell ).burst( ShadowParticle.CURSE, 10 );

        } else {

            if( Level.solid[ cell ] ) {
                cell = Ballistica.trace[ Ballistica.distance - 1 ];
            }

            GameScene.add( Blob.seed( cell, damageRoll() * 3 / 2, Miasma.class ) );
            CellEmitter.center( cell ).burst( ShadowParticle.BURST, 10 );

        }

	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.shadow( curUser.sprite.parent, curUser.pos, cell, callback );
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }
	
	@Override
	public String desc() {
		return 
			"This wand is crafted from bones and obsidian, its shape reminding you of a sickle. " +
            "A creation of the dark arts, it can infuse its victims with unholy miasma, dooming " +
            "them to an inevitable death after a certain amount of time.";
	}
	

}
