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

package com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing;

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.UnholyArmor;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.mechanics.Ballistica;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MissileSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Chakrams extends ThrowingWeaponLight {

    {
        name = "chakrams";
        image = ItemSpriteSheet.THROWING_ANUS;
    }

    public Chakrams() {
        this( 1 );
    }

    public Chakrams(int number) {
        super( 3 );
        quantity = number;
    }

    @Override
    public boolean returnsWhenThrown() {
        return true;
    }

    @Override
    public boolean canBackstab() {
        return true;
    }

    @Override
    public int str( int bonus ) {
        return super.str( bonus ) + 2 ;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 2 ;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    private boolean canBounceTo(Char enemy){
        return (enemy != null && enemy != curUser && !(enemy instanceof NPC)
                && Level.fieldOfView[enemy.pos]);
    }

    protected boolean bounce(int cell){
        HashSet<Char> ns = new HashSet<Char>();

        //check possible targets at one range
        for (int i : Level.NEIGHBOURS8) {
            Char n = Actor.findChar(cell + i);
            if (canBounceTo(n)) {
                ns.add(n);
            }
        }
        //check possible targets at two range
        for (int i : Level.NEIGHBOURS16) {
            try {
                Char n = Actor.findChar(cell + i);
                //has path to the enemy (no wall or other mob in between)
                if (canBounceTo(n) && n.pos== Ballistica.cast(cell, n.pos, false, true))
                    ns.add(n);

            }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
        }

        if (ns.size() > 0) {
            final Char newEnemy = Random.element(ns);
            final Weapon weap=this;
            final int enemyPos=newEnemy.pos;
            ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                    reset(cell, newEnemy.pos, curItem.imageAlt(),0.75f, null,new Callback() {
                        @Override
                        public void call() {
                            curUser.shoot(newEnemy, weap);

                            curUser.spendAndNext( 1/weap.speedFactor( curUser ) );
                            QuickSlot.refresh();

                            ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                                    reset(enemyPos, curUser.pos, curItem.imageAlt(), null);
                        }
                    });

            return true;
        }
        return false;

    }

    @Override
    public String desc() {
        return "When used by a skilled user, this razor-edged disc can hit several targets at " +
                "once and then return back to the hand from which it was thrown.";
    }
}
