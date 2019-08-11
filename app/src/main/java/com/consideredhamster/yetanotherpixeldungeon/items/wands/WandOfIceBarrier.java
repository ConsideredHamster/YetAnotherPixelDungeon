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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.scenes.CellSelector;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.IceBlockSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfIceBarrier extends WandUtility {

	{
		name = "Wand of Ice Barrier";
        image = ItemSpriteSheet.WAND_ICEBARRIER;

        hitChars = false;
        goThrough = false;

	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.80f;
    }

    protected static ArrayList<Integer> cells = new ArrayList<Integer>();

	@Override
	protected void onZap( int cell ) {

        CellEmitter.get( cell ).burst( Speck.factory( Speck.ICESHARD ), 12 );

        if( cells.size() > 0 ){

            for( int c : cells ){

                int power = ( Level.water[ c ] ? damageRoll() * 3 / 2 : damageRoll() ) / cells.size();

                Char ch = Actor.findChar( c );

                if( ch != null ){

                    if( ch instanceof IceBlock ) {
                        ch.HT += power;
                        ch.heal( power );
                    } else {
                        power = Char.absorb( power, ch.armorClass(), false );
                        ch.damage( power, curUser, Element.PHYSICAL );

                        if( ch.isAlive() ){
                            BuffActive.add( ch, Frozen.class, power );
                        }
                    }

                } else if( power > 0 ) {

                    IceBlock.spawnAt( power, c );

                }
            }

        } else {
            GLog.i( "nothing happened" );
        }
    }

    @Override
	protected void fx( final int cell, final Callback callback ) {

        cells = new ArrayList<>();
        cells.add( cell );

        MagicMissile.frost( curUser.sprite.parent, curUser.pos, cell, new Callback() {
            @Override
            public void call(){
            curUser.ready();
            GameScene.selectCell( new SecondaryListener( cell, callback ) );
            }
        } );

	}
	
	@Override
	public String desc() {
		return 
			"This rime-covered wand allows its user to create a wall of ice between any two points. " +
            "Durability of this wall is heavily dependent on amount of tiles affected, but it can be " +
            "used on the same spot if its user desires. Ice shards created by this wand also may harm " +
            "and chill your enemies.";
	}

    private static class SecondaryListener implements CellSelector.Listener {

        public Callback callback;
        public Integer source;

        public SecondaryListener( Integer source, Callback callback ) {
            this.source = source;
            this.callback = callback;
        }

        @Override
        public void onSelect( Integer target ){

            if( target != null && !source.equals( target ) ){

                target = Ballistica.cast( source, target, false, false );
                MagicMissile.shards( curUser.sprite.parent, source, target, null );

                if( Ballistica.distance > 0 ){

                    for( int i = 1 ; i <= Ballistica.distance ; i++ ){
                        cells.add( Ballistica.trace[ i ] );
                    }
                }

//                target = Ballistica.cast( source, source + source - target, false, false );
//                MagicMissile.shards( curUser.sprite.parent, source, target, null );
//
//                if( Ballistica.distance > 0 ){
//
//                    for( int i = 1 ; i <= Ballistica.distance ; i++ ){
//                        cells.add( Ballistica.trace[ i ] );
//                    }
//                }
            }

            Sample.INSTANCE.play( Assets.SND_ZAP );
            callback.call();

        }

        @Override
        public String prompt(){
            return "Choose another tile";
        }
    };

    public static class IceBlock extends NPC {

        public IceBlock(){

            name = "ice block";
            spriteClass = IceBlockSprite.class;

            resistances.put( Element.Flame.class, Element.Resist.VULNERABLE );

            resistances.put( Element.Shock.class, Element.Resist.PARTIAL);
            resistances.put( Element.Acid.class, Element.Resist.PARTIAL);

            resistances.put( Element.Frost.class, Element.Resist.IMMUNE );
            resistances.put( Element.Body.class, Element.Resist.IMMUNE );
            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

            resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );
            resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );

            hostile = false;
            friendly = true;
        }

        @Override
        public boolean isMagical() {
            return false;
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        @Override
        public int viewDistance() {
            return 0;
        };

        @Override
        protected boolean act() {

            if( --HP <= 0 ){
                die( null );
                return true;
            }

            state = PASSIVE;

            return super.act();
        }

        @Override
        public void interact(){
            GLog.i( "You dispel the ice block." );
            Dungeon.hero.sprite.operate( pos );

            Dungeon.hero.spend( TICK );
            Dungeon.hero.busy();

            die( null );
        }

        @Override
        public Char chooseEnemy() {
            return null;
        }

        private void adjustStats( int level ) {
            HT = level * 2;
            armorClass = 0;

            minDamage = 0;
            maxDamage = 0;

            accuracy = 0;
            dexterity = 0;
        }

        public static IceBlock spawnAt( int level, int pos ){

            // cannot spawn on walls, chasms or already ocupied tiles
            if ( !Level.solid[pos] && !Level.chasm[pos] && Actor.findChar( pos ) == null ){

                IceBlock block = new IceBlock();

                block.adjustStats( level );
                block.HP = block.HT;

                block.pos = pos;
                block.enemySeen = true;
                block.state = block.PASSIVE;

                GameScene.add( block, TICK );
                Dungeon.level.press( block.pos, block );
                block.sprite.spawn();

                return block;

            } else {

                return null;

            }
        }

        @Override
        public void die( Object cause, Element dmg ) {
            super.die( cause, dmg );

            if ( cause != null && Dungeon.visible[pos] ) {
                CellEmitter.get( pos ).burst( Speck.factory( Speck.ICESHARD ), 16 );
            }
        }

        @Override
        public String description() {
            return "";
        }
    }
}
