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
package com.consideredhamster.yetanotherpixeldungeon.items.misc;

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.ToxicGas;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.ConfusionGas;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Explosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.BArray;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

import java.util.ArrayList;

public abstract class Explosives extends Item {

    private static final String AC_COMBINE = "COMBINE";
    private static final String AC_SALVAGE = "SALVAGE";

    private static final float TIME_TO_COMBINE = 3.0f;
    private static final float TIME_TO_SALVAGE = 3.0f;

    private static final String TXT_MORE_POWDER_NEEDED = "You don't have enough gunpowder to combine.";
    private static final String TXT_MORE_BOMBS_NEEDED = "You don't have enough bombs to combine.";

    private static final String TXT_STICK_MADE = "You made a bomb stick.";
    private static final String TXT_BUNDLE_MADE = "You made a bomb bundle.";

    private static final String TXT_POWDER_SALVAGED = "You salvaged %s portions of gunpowder.";
    private static final String TXT_BOMBS_SALVAGED = "You salvaged %s bomb sticks.";

    protected Explosives combineResult( Hero hero ) {
        return null;
    };

    protected Explosives salvageResult( Hero hero ) {
        return null;
    };

    public int damage( int strength ) { return strength / 3; }

    public int radius( int strength ) { return strength < 50 ? 0 : strength < 150 ? 1 : strength < 300 ? 2 : strength < 500 ? 3 : 4 ; }

    @Override
    public int priceModifier() { return 2; }

    @Override
    public String quickAction() {
        return AC_THROW;
    }
	
	@Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        actions.add( AC_COMBINE );
        actions.add( AC_SALVAGE );

        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        if (action == AC_SALVAGE) {

            curUser = hero;
            curItem = this;

            Explosives result = salvageResult( curUser );

            if( result != null ) {

                if ( !result.doPickUp( Dungeon.hero )) {
                    Dungeon.level.drop( result, Dungeon.hero.pos ).sprite.drop();
                }

                QuickSlot.refresh();

                Sample.INSTANCE.play(Assets.SND_OPEN, 0.6f, 0.6f, 0.5f);

                curUser.sprite.operate(curUser.pos);

                curUser.spend(TIME_TO_SALVAGE);

                curUser.busy();

            }

        } else if (action == AC_COMBINE) {

            curUser = hero;
            curItem = this;

            Explosives result = combineResult( curUser );

            if( result != null ) {

                if ( !result.doPickUp( Dungeon.hero )) {
                    Dungeon.level.drop( result, Dungeon.hero.pos ).sprite.drop();
                }

                updateQuickslot();

                Sample.INSTANCE.play(Assets.SND_OPEN, 0.6f, 0.6f, 0.5f);

                curUser.sprite.operate(curUser.pos);

                curUser.spend(TIME_TO_COMBINE);

                curUser.busy();

            }

        } else {

            super.execute( hero, action );

        }
    }

    public static void explode( int cell, int damage, int radius, Object source ) {

        PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlockHigh, null ), radius );

        Blob[] blobs = {
                Dungeon.level.blobs.get( Fire.class ),
                Dungeon.level.blobs.get( ToxicGas.class ),
                Dungeon.level.blobs.get( ConfusionGas.class ),
        };

        Sample.INSTANCE.play( Assets.SND_BLAST, 1 + radius );
        Camera.main.shake( 3 + radius, 0.2f + radius * 0.1f );

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= ( 4 + 2 * radius ) ) {
                mob.beckon( cell );
            }
        }

        boolean terrainAffected = false;

        for (int c = 0; c < Level.LENGTH; c++ ) {

            if (PathFinder.distance[c] < Integer.MAX_VALUE) {

                int r = PathFinder.distance[ c ];

                terrainAffected = Explosion.affect( c, r, radius, damage, source ) || terrainAffected;

                for (Blob blob : blobs) {

                    if (blob == null) {
                        continue;
                    }

                    int value = blob.cur[c];
                    if (value > 0) {

                        blob.cur[c] = 0;
                        blob.volume -= value;
                    }
                }
            }
        }

        for (int n : Level.NEIGHBOURS9) {

            int c = cell + n;

            if (Level.flammable[c]) {
                Level.set(c, Terrain.EMBERS);
                GameScene.updateMap(c);
                terrainAffected = true;
            }

            Char ch = Actor.findChar(c);

            if( ch != null ) {

                Pushing.knockback( ch, cell, radius, damage );

            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }
    }

    public static class Gunpowder extends Explosives {

        {
            name = "gunpowder";
            image = ItemSpriteSheet.GUNPOWDER;
            stackable = true;
        }

        @Override
        public Item random() {
            quantity = Random.IntRange( 10, 20 ) * 5;
            return this;
        }

        @Override
        public int price() {
            return quantity;
        }

        @Override
        protected Explosives combineResult( Hero hero ) {

            Gunpowder powder = curUser.belongings.getItem( Explosives.Gunpowder.class );

            if ( powder != null && powder.quantity >= BombStick.powderMax ) {

                if (powder.quantity <= BombStick.powderMax) {

                    powder.detachAll(Dungeon.hero.belongings.backpack);

                } else {

                    powder.quantity -= BombStick.powderMax;

                }

                GLog.i( TXT_STICK_MADE );

                return new BombStick();

            } else {

                GLog.n( TXT_MORE_POWDER_NEEDED );

                return null;

            }
        };

        @Override
        public String info() {
            return
                    "This is a container of black powder. Gunpowder can be used to reload flintlock " +
                    "weapons or to make some makeshift explosives.\n\n" +
                    "You need " + BombStick.powderMax + " portions of gunpowder to create a bomb.";
        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );

            actions.remove( AC_SALVAGE );
            actions.remove( AC_THROW );

            return actions;
        }

        @Override
        public String quickAction() {
            return AC_COMBINE;
        }
    }

    public static class BombStick extends Explosives {

        final protected static int powderMin = 50;
        final protected static int powderMax = 75;

        {
            name = "bomb stick";
            image = ItemSpriteSheet.BOMB_STICK;
            stackable = true;
        }


        @Override
        public String info() {
            return
                "This is a makeshift pipe bomb, filled with black powder. Conveniently, its fuse is " +
                "lit automatically when the bomb is thrown.\n\n" +
                "You can get " + powderMin + "-" + powderMax + " portions of gunpowder by salvaging this.\n" +
                "You can get a bomb bundle by combining " + BombBundle.sticksMax + " " + name + "s." ;
        }

        @Override
        public Item random() {
            quantity = Random.IntRange( 2, 4 );
            return this;
        }

        @Override
        public int price() {
            return 50 * quantity;
        }

        @Override
        protected Explosives combineResult( Hero hero ) {

            BombStick sticks = curUser.belongings.getItem( BombStick.class );

           if ( sticks != null && sticks.quantity >= BombBundle.sticksMax ) {

               if (sticks.quantity <= BombBundle.sticksMax) {

                   sticks.detachAll(Dungeon.hero.belongings.backpack);

               } else {

                   sticks.quantity -= BombBundle.sticksMax;

               }

               GLog.i( TXT_BUNDLE_MADE );

               return new BombBundle();

            } else {

               GLog.n( TXT_MORE_BOMBS_NEEDED );

               return null;

            }
        };

        @Override
        protected Explosives salvageResult( Hero hero ) {

            Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

            if( bomb != null && bomb.quantity > 0 ) {

                Item powder = new Gunpowder().quantity(Random.IntRange(powderMin, powderMax));

                GLog.i(TXT_POWDER_SALVAGED, powder.quantity);

                return (Gunpowder) powder;
            }

            return null;

        };

        @Override
        protected void onThrow( int cell ) {

            if (Level.chasm[cell]) {

                super.onThrow( cell );

            } else {

                Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

                if( bomb != null ) {

                    int strength = bomb.price();

                    if( Level.solid[ cell ] ) {
                        cell = Ballistica.trace[ Ballistica.distance - 1 ];
                    }

                    explode(cell, bomb.damage(strength), bomb.radius(strength), curUser);

                }
            }
        }
    }

    public static class BombBundle extends Explosives {

        final protected static int sticksMin = 2;
        final protected static int sticksMax = 3;

        {
            name = "bomb bundle";
            image = ItemSpriteSheet.BOMB_BUNDLE;
            stackable = true;
        }

        @Override
        public String info() {
            return
                "This is a huge bomb made of several other bombs. It is a very powerful explosive.\n\n" +
                "You can get " + sticksMin + "-" + sticksMax + " bomb sticks by salvaging this.";
        }

        @Override
        public Item random() {
            quantity = Random.IntRange( 1, 2 );
            return this;
        }

        @Override
        public int price() {
            return 150 * quantity;
        }

        @Override
        protected Explosives salvageResult( Hero hero ) {

            Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

            if( bomb != null && bomb.quantity > 0 ) {

                Item sticks = new BombStick().quantity( Random.IntRange( sticksMin, sticksMax ) );

                GLog.i( TXT_BOMBS_SALVAGED, sticks.quantity );

                return (BombStick)sticks;
            }

            return null;

        };

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );

            actions.remove( AC_COMBINE );

            return actions;
        }

        @Override
        protected void onThrow( int cell ) {

            if( Level.solid[ cell ] ) {
                cell = Ballistica.trace[ Ballistica.distance - 1 ];
            }

            if (Level.chasm[cell]) {
                super.onThrow( cell );
            } else {

                Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

                if( bomb != null ) {

                    int strength = bomb.price();

                    explode(cell, bomb.damage(strength), bomb.radius(strength), curUser);

                }
            }
        }
    }
}
