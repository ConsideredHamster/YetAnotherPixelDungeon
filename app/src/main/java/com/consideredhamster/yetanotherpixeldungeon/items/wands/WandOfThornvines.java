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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.AcidParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.LeafParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ThornvineSprite;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.MagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfThornvines extends WandUtility {

	{
		name = "Wand of Thornvines";
        image = ItemSpriteSheet.WAND_THORNVINE;
        goThrough = false;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.90f;
    }
	
	@Override
	protected void onZap( int cell ) {

        // stats of thornvine depend on magic stats and wand stats
        int stats = damageRoll();
        int level = getCharges();

        // first we check the targeted tile
        if( Thornvine.spawnAt( stats, level, cell ) == null ) {

            // then we check the previous tile
            int prevCell = Ballistica.trace[ Ballistica.distance - 1 ];

            if( Thornvine.spawnAt( stats, level, prevCell ) == null ) {

                // and THEN we check all tiles around the targeted
                ArrayList<Integer> candidates = new ArrayList<Integer>();

                for (int n : Level.NEIGHBOURS8) {
                    int pos = cell + n;
                    if( Level.adjacent( pos, prevCell ) && !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ){
                    Thornvine.spawnAt( stats, level, candidates.get( Random.Int( candidates.size() ) ) );
                } else {
                    GLog.i( "nothing happened" );
                }
            }
        }

        CellEmitter.center( cell ).burst( LeafParticle.GENERAL, 5 );
        Sample.INSTANCE.play( Assets.SND_PLANT );
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.foliage( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"This wand possesses the mystical force of summoning forces of the very earth to the " +
            "wielder's command, allowing him or her to spawn thornvines from the floor. These " +
            "plants will fiercely lash out at any enemy passing through, and are especially strong " +
            "when created on grass-covered tiles.";
	}

    public static class Thornvine extends NPC {

        private int stats;
        private int charges;

        public Thornvine(){

            name = "thornvine";
            spriteClass = ThornvineSprite.class;

            resistances.put( Element.Flame.class, Element.Resist.VULNERABLE );

            resistances.put( Element.Shock.class, Element.Resist.PARTIAL);
//            resistances.put( Element.Body.class, Element.Resist.PARTIAL );

            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
//            resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

            resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );

            PASSIVE = new Guarding();
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
            return 1;
        };

        @Override
        protected boolean act() {

            if( --HP <= 0 ){
                die( this );
                return true;
            }

            return super.act();
        }


        @Override
        public void interact(){

            Dungeon.hero.sprite.operate( pos );

            if( Dungeon.hero.belongings.weap2 instanceof WandOfThornvines ) {

                // we restore at least one charge less than what was spent on the vine
                ((WandOfThornvines)Dungeon.hero.belongings.weap2).addCharges(
                        ( ( HP - 1 ) * charges / HT )
                );

                GLog.i( "You recall the thornvine into the wand." );
                Sample.INSTANCE.play( Assets.SND_PLANT );

            } else {

                GLog.i( "You unsummon the thornvine." );
                Sample.INSTANCE.play( Assets.SND_PLANT );

            }

            Dungeon.hero.spend( TICK );
            Dungeon.hero.busy();

            die( this );
        }

        @Override
        public int attackProc( Char enemy, int damage, boolean blocked ) {

            // thornvines apply crippled on hit
            if( !blocked ) {
                BuffActive.addFromDamage( enemy, Crippled.class, damage );
            }

            return damage;
        }

        @Override
        protected Char chooseEnemy() {

            // thornvines attack your enemies by default
            if ( enemy == null || !enemy.isAlive() ) {

                HashSet<Mob> enemies = new HashSet<Mob>();

                for ( Mob mob: Dungeon.level.mobs ) {
                    if ( mob.hostile && !mob.friendly && Level.fieldOfView[mob.pos] ) {
                        enemies.add( mob );
                    }
                }

                return enemies.size() > 0 ? Random.element( enemies ) : null;

            } else {

                return enemy;

            }
        }

        private void adjustStats( int stats, int charges ) {

            HT = stats + charges * 2 + 2;
            armorClass = 0;

            maxDamage = stats;
            minDamage = stats / 2;

            accuracy = stats;
            dexterity = stats / 2;

            this.stats = stats;
            this.charges = charges;
        }

        public static Thornvine spawnAt( int stats, int charges, int pos ){

            // cannot spawn on walls, chasms or already occupied tiles
            if ( !Level.solid[pos] && !Level.chasm[pos] && Actor.findChar( pos ) == null ){

                Thornvine vine = new Thornvine();

                if( Dungeon.level.map[ pos ] == Terrain.GRASS || Dungeon.level.map[ pos ] == Terrain.HIGH_GRASS ){
                    stats += ( stats / 2 + Random.Int( stats % 2 + 1 ) );
                }

                vine.adjustStats( stats, charges );
                vine.HP = vine.HT;

                vine.pos = pos;
                vine.enemySeen = true;
                vine.state = vine.PASSIVE;

                GameScene.add( vine, 0f );
                Dungeon.level.press( vine.pos, vine );

                vine.sprite.emitter().burst( LeafParticle.LEVEL_SPECIFIC, 5 );
                vine.sprite.spawn();

                return vine;

            } else {

                return null;

            }
        }

        private class Guarding extends Mob.Passive {

            @Override
            public boolean act( boolean enemyInFOV, boolean justAlerted ){

                if (enemyInFOV && canAttack( enemy ) && enemy != Dungeon.hero ) {

                    return doAttack( enemy );

                } else {

                    spend( TICK );
                    return true;

                }
            }

            @Override
            public String status(){
                return "guarding";
            }
        }

        @Override
        public String description() {
            return
                "Thornvines are kind of semisentient plants which are very territorial and will " +
                "attack anything which comes near. Their sharp thorns can inflict grievous wounds, " +
                "but they are very vulnerable to fire and will quickly wither as time passes.";
        }

        private static final String STATS	= "stats";
        private static final String CHARGES	= "charges";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( STATS, stats );
            bundle.put( CHARGES, charges );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            adjustStats( bundle.getInt( STATS ), bundle.getInt( CHARGES ) );
        }
    }
}
