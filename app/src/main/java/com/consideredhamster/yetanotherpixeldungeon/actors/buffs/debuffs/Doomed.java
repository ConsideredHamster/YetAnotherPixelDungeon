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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Miasma;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.DoomSkull;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;

public class Doomed extends Debuff {

    @Override
    public String toString() {
        return "Doomed";
    }

    @Override
    public String statusMessage() { return "doomed"; }

    @Override
    public int icon() {
        return BuffIndicator.DOOMED;
    }

    @Override
    public void applyVisual() {
        Sample.INSTANCE.play( Assets.SND_GHOST, 1.25f, 1.25f, 1.25f );
    }

    @Override
    public void removeVisual() {

        if( target.sprite.visible ){
            DoomSkull.createAtChar( target );
        }

        Sample.INSTANCE.play( Assets.SND_DESCEND, 0.75f, 0.75f, 1.25f );
    }

    @Override
    public String playerMessage() { return "You are doomed!"; }

    @Override
    public String description() {
        return "You're doomed. Simple as that. When the duration of this effect will end, you'll " +
                "die on the spot. Have a fun death!";
    }

    @Override
    public boolean act() {

        if( target.sprite.visible && duration > 1 ){
            target.sprite.showStatus( CharSprite.BLACK, Integer.toString( duration - 1 ) );
        }

        return super.act();
    }

//    @Override
//    public boolean attachTo( Char target ) {
//        return super.attachTo( target );
//    }

    @Override
    public void detach() {

        target.damage( target.currentHealthValue(), this, Element.DOOM );

        if( !target.isAlive() ){
            GameScene.add( Blob.seed( target.pos, target.totalHealthValue(), Miasma.class ) );
        }

        super.detach();
    }

}
