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

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.GooSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GooSpawn extends MobEvasive {

    private static final float SPLIT_DELAY	= 1f;

    private static final int SPAWN_HEALTH = 15;

    public boolean phase = false;

    public Goo mother = null;

    public GooSpawn() {

        super( 2, 3, false );

        name = "spawn of Goo";
        info = "Magical, Splitting";

        spriteClass = GooSprite.SpawnSprite.class;

        HT = SPAWN_HEALTH;

        if( Dungeon.difficulty == Difficulties.NORMAL ) {
            HT = Random.NormalIntRange( HT, HT * 2);
        } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
            HT = HT * 2;
        }

        minDamage /= 2;
        maxDamage /= 2;

        HP  = HT;
        EXP = 5;

        armorClass = 0;

        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );

    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public int dexterity() {
        return !phase ? super.dexterity() : 0 ;
    }

    @Override
    protected boolean getCloser( int target ) {
        return phase && mother != null ?
                super.getCloser( mother.pos ) :
                super.getCloser( target );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return !phase && super.canAttack( enemy );
    }

	@Override
	public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if ( type == Element.PHYSICAL && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH ) ) {

            split( this, dmg );

        }

        super.damage( dmg, src, type );
	}

    @Override
    public boolean act() {

        if ( phase && mother != null && Level.adjacent( pos, mother.pos ) ){

            Burning buff1 = buff( Burning.class );

            if( buff1 != null ){
                BuffActive.add( mother, Burning.class, (float)buff1.getDuration() );
            }

            mother.heal( HP );
            die( this );

            Pushing.move( this, mother.pos, null );
            sprite.parent.add( new AlphaTweener( sprite, 0.0f, 0.1f ) );

            if( Dungeon.visible[ pos ] ) {
                mother.sprite.showStatus( CharSprite.NEGATIVE, "absorbed" );
                GLog.n( "Goo absorbs entranced spawn, healing itself!" );
            }

            return true;

        }

        if ( Level.water[pos] && HP < HT ) {
            HP++;
        }

        if( !phase && mother != null && HP == HT ) {
            phase = true;
            sprite.idle();

            if( Dungeon.visible[ pos ] ){
                sprite.showStatus( CharSprite.WARNING, "entranced" );
                GLog.n( "A spawn of Goo became entranced - do not let them stand in the water!" );
            }

            spend( TICK );
            return true;
        }

        return super.act();
    }

    public static GooSpawn split( Mob spawner, int dmg ) {

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        boolean[] passable = Level.passable;

        for (int n : Level.NEIGHBOURS8) {
            if (passable[spawner.pos + n] && Actor.findChar(spawner.pos + n) == null) {
                candidates.add(spawner.pos + n);
            }
        }

        if (candidates.size() > 0) {

            final GooSpawn clone = new GooSpawn();

            clone.pos = spawner.pos;
            clone.HT = Math.min( dmg, spawner.HP );

            clone.HP = 1;
            clone.EXP = 0;

            clone.state = clone.HUNTING;

            GameScene.add( clone, SPLIT_DELAY );

            Pushing.move( clone, Random.element( candidates ), new Callback() {
                @Override
                public void call(){
                    Actor.occupyCell( clone );
                    Dungeon.level.press(clone.pos, clone);
                }
            } );

            Burning buff1 = spawner.buff( Burning.class );

            if ( buff1 != null ) {
                BuffActive.addFromDamage( clone, Burning.class, buff1.getDuration() );
            }

            Frozen buff2 = spawner.buff( Frozen.class );

            if ( buff2 != null ) {
                BuffActive.addFromDamage( clone, Frozen.class, buff2.getDuration() );
            }

            return clone;
        }

        return null;
    }

    @Override
    public String description() {
        return  "Little is known about The Goo. It's quite possible that it is not even a creature, but rather a " +
                "conglomerate of substances from the sewers that gained some kind of rudimentary, but very evil " +
                "sentience.";
    }

    private static final String PHASE	= "phase";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
    }
}
