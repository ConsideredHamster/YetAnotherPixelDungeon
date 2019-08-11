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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MimicSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Mimic extends MobHealthy {

    private static final float TIME_TO_DEVOUR	= 1.5f;

    public Mimic() {

        super( Dungeon.depth + 1 );

        name = "mimic";
        spriteClass = MimicSprite.class;

        items = new ArrayList<>();

        minDamage += tier;
        maxDamage += tier;

        HP = HT += Random.IntRange( 5, 10 );

        baseSpeed = 0.75f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
    }
	
	public ArrayList<Item> items;
	
//	private static final String LEVEL	= "bonus";
	private static final String ITEMS	= "items";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( ITEMS, items );
//		bundle.put( LEVEL, bonus );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		items = new ArrayList<Item>( (Collection<? extends Item>) (Object) bundle.getCollection( ITEMS ) );
//		adjustStats( bundle.getInt( LEVEL ) );
	}

//    @Override
//    public float attackDelay() {
//        return 1f;
//    }

//
//	@Override
//	public int accuracy( Char target ) {
//		return 9 + bonus;
//	}
	
//	@Override
//	public int attackProc( Char enemy, int damage ) {
//		if (enemy == Dungeon.hero && Random.Int( 3 ) == 0) {
//			Gold gold = new Gold( Random.Int( Dungeon.gold / 10, Dungeon.gold / 2 ) );
//			if (gold.quantity() > 0) {
//				Dungeon.gold -= gold.quantity();
//				Dungeon.bonus.drop( gold, Dungeon.hero.pos ).sprite.drop();
//			}
//		}
//		return super.attackProc( enemy, damage );
//	}
//
//	public void adjustStats( int bonus ) {
//		this.bonus = bonus;
//
//		HT = (3 + bonus) * 3;
//		EXP = 2 + 2 * (bonus - 1) / 5;
//		dexterity = accuracy( null ) / 2;
//
//		enemySeen = true;
//	}

    @Override
    public boolean cast( Char enemy ) {
        return false;
    }

    @Override
    protected boolean act() {

        Heap heap = Dungeon.level.heaps.get( pos );

        if (heap != null && heap.type == Heap.Type.HEAP && !enemySeen) {

            ((MimicSprite)sprite).devour();

            Item item = heap.pickUp();

            devour(item);

            if (Dungeon.visible[pos]) {
                GLog.w("Mimic swallows %s lying on the floor!", item.toString());
            }

            spend( TIME_TO_DEVOUR );

            return true;

//        } else if( heap == null && items != null && state != HUNTING && !enemySeen && Random.Int( Dungeon.chapter() + 1 ) == 0 ) {
//
//            for( Item item : items) {
//                heap = Dungeon.level.drop(item, pos);
//            }
//
//            if( heap != null ) {
//                heap.type = Heap.Type.CHEST_MIMIC;
//                heap.sprite.link();
//                heap.sprite.drop();
//                heap.hp = HT;
//            }
//
//            HP = 0;
//            sprite.killAndErase();
//            Dungeon.level.mobs.remove(this);
//            Actor.remove(this);
//            Actor.freeCell(pos);
//
//            return true;

        } else {

            return super.act();

        }
    }

    private void devour( Item item ) {

        if ( items.contains( item ) ) {
            return;
        }

        if (item.stackable) {
            Class<?> c = getClass();
            for (Item i : items) {
                if (i.getClass() == c) {
                    i.quantity += item.quantity;
                    return;
                }
            }
        }

        items.add(item);
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

		super.die( cause, dmg );
		
		if (items != null) {
			for (Item item : items) {
				Dungeon.level.drop( item, pos ).sprite.drop();
			}
		}
	}
	
	@Override
	public boolean reset() {
		state = WANDERING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

	@Override
	public String description() {
		return
			"Mimics are magical creatures which can take any shape they wish. In dungeons they almost always " +
			"choose a shape of a treasure chest, because they know how to beckon an adventurer, but are too slow " +
            "to catch them otherwise.";
	}
	
	public static Mimic spawnAt( int hp, int pos, List<Item> items ) {
		final Char ch = Actor.findChar( pos );

		if (ch != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int n : Level.NEIGHBOURS8) {
				int cell = pos + n;
				if ((Level.passable[cell] || Level.avoid[cell]) && Actor.findChar( cell ) == null) {
					candidates.add( cell );
				}
			}
			if (candidates.size() > 0) {

                Pushing.move( ch, Random.element( candidates ), new Callback() {
                    @Override
                    public void call(){

                        Actor.occupyCell( ch );

                        Dungeon.level.press( ch.pos, ch );
                    }
                } );

			} else {
				return null;
			}
		}
		
		Mimic m = new Mimic();
		m.items = new ArrayList<Item>( items );
//		m.adjustStats( Dungeon.depth );
//		m.HP = m.HT;
		m.pos = pos;
        m.enemySeen = true;
        m.special = true;
        m.state = m.HUNTING;

        if( hp > 0 ) {
            m.HT = m.HP = hp;
        }

		GameScene.add( m, 0.5f );
		
		m.sprite.turnTo( pos, Dungeon.hero.pos );
		
		if (Dungeon.visible[m.pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.STAR ), 10 );
			Sample.INSTANCE.play( Assets.SND_MIMIC );
		}
		
		return m;
	}
}
