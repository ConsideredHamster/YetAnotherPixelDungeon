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
package com.consideredhamster.yetanotherpixeldungeon.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.food.Food;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Dewdrop;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mimic;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Wraith;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatBurned;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.Key;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Heap implements Bundlable {

	private static final String TXT_MIMIC = "This is a mimic!";
	private static final String TXT_BURNED = "Fire burns %s lying on the floor!";

	private static final String TXT_BLOWNUP = "%s is destroyed!";
	private static final String TXT_DAMAGED = "%s is damaged!";
	private static final String TXT_SHATTER = "%s is shattered!";

	private static final String TXT_KNOWN_BY_BREWING = "You now know that this is %s!";

	private static final int HERBS_TO_POTION = 3;
	
	public enum Type {
		HEAP, 
		FOR_SALE, 
		CHEST, 
		LOCKED_CHEST, 
		CRYSTAL_CHEST,
		TOMB,
        BONES,
        BONES_CURSED,
        CHEST_MIMIC
	}
	public Type type = Type.HEAP;
	
	public int pos = 0;
	public int hp = 0;

	public ItemSprite sprite;
	
	public LinkedList<Item> items = new LinkedList<Item>();
	
	public int image() {
		switch (type) {
		case HEAP:
		case FOR_SALE:
			return items.size() > 0 ? items.peek().image() : 0;
		case CHEST:
		case CHEST_MIMIC:
			return ItemSpriteSheet.CHEST;
		case LOCKED_CHEST:
			return ItemSpriteSheet.LOCKED_CHEST;
		case CRYSTAL_CHEST:
			return ItemSpriteSheet.CRYSTAL_CHEST;
		case TOMB:
			return ItemSpriteSheet.TOMB;
		case BONES:
		case BONES_CURSED:
			return ItemSpriteSheet.BONES;
		default:
			return 0;
		}
	}
	
	public ItemSprite.Glowing glowing() {
		return (type == Type.HEAP || type == Type.FOR_SALE) && items.size() > 0 ? items.peek().glowing() : null;
	}
	
	public void open() {
		switch (type) {
		case CHEST_MIMIC:
			if (Mimic.spawnAt( hp, pos, items ) != null) {
				GLog.n( TXT_MIMIC );
				destroy();
			} else {
				type = Type.CHEST;
			}
			break;
		case TOMB:
            CellEmitter.center( pos ).start( Speck.factory( Speck.RATTLE ), 0.1f, 3 );
            Sample.INSTANCE.play(Assets.SND_TOMB);
            if (Wraith.spawnAt(pos) == null) {
                Wraith.spawnAround(pos, 1);
            }
			break;

		case BONES:
            CellEmitter.center( pos ).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
            Sample.INSTANCE.play(Assets.SND_BONES, 1, 1, 1.0f);
            break;

        case BONES_CURSED:
            CellEmitter.center( pos ).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
            Sample.INSTANCE.play(Assets.SND_CURSED, 1, 1, 0.5f);
            if (Wraith.spawnAt(pos) == null) {
                Wraith.spawnAround(pos, 1);
            }

//			for (Item item : items) {
//				if (item.bonus < 0) {
//                    if (Wraith.spawnAt(pos) == null) {
//                        Wraith.spawnAround(pos, 1);
//					}
//					Sample.INSTANCE.play(Assets.SND_CURSED);
//					break;
//				}
//			}
			break;
		default:
		}
		
		if (type != Type.CHEST_MIMIC) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();

            Dungeon.level.press( pos, null );
		}
	}
	
	public int size() {
		return items.size();
	}
	
	public Item pickUp() {
		
		Item item = items.removeFirst();
		if (items.isEmpty()) {
			destroy();
		} else if (sprite != null) {
			sprite.view( image(), glowing() );
		}
		
		return item;
	}
	
	public Item peek() {
		return items.peek();
	}
	
	public void drop( Item item ) {
		
		if (item.stackable) {
			
			Class<?> c = item.getClass();
			for (Item i : items) {
				if (i.getClass() == c) {
					i.quantity += item.quantity;
					item = i;
					break;
				}
			}
			items.remove( item );
			
		}
		
		if (item instanceof Dewdrop) {
			items.add( item );
		} else {
			items.addFirst( item );
		}
		
		if (sprite != null) {
			sprite.view(image(), glowing());
		}
	}
	
	public void replace( Item a, Item b ) {

        boolean found = false;

        if (b.stackable) {
            Class<?> c = b.getClass();

            for (Item i : items) {
                if (i.getClass() == c) {
                    i.quantity += a.quantity;
                    found = true;
                    break;
                }
            }
        }

        if( found ) {

            items.remove( a );

        } else {
            int index = items.indexOf( a );

            if (index != -1) {
                items.remove( index );
                items.add(index, b);
            }
        }

//        int index = items.indexOf( a );
//
//        if (index != -1) {
//            items.remove( index );
//            items.add(index, b);
//        }
	}
	
	public void burn() {
		
		if (type == Type.CHEST_MIMIC) {
			Mimic m = Mimic.spawnAt(hp, pos, items);
			if (m != null) {

                Burning buff = Buff.affect( m, Burning.class );
                buff.add( Actor.TICK * 2 );
				destroy();

			}
		}
		if (type != Type.HEAP) {
			return;
		}
		
		boolean heapBurnt = false;
//		boolean evaporated = false;
		
		for (Item item : items.toArray( new Item[0] )) {

            boolean burnt = false;

            if (item instanceof Scroll) {

                items.remove( item );
                burnt = true;

            } else if (item instanceof Herb) {

                items.remove( item );
                burnt = true;

			} else if ( item instanceof MeatRaw || item instanceof MeatStewed ) {

                MeatBurned result = new MeatBurned();
                result.quantity = item.quantity();

				replace( item, result );
				burnt = true;

			}

            if( burnt && Dungeon.visible[ pos ] ) {
                GLog.w(TXT_BURNED, item.toString());
                heapBurnt = true;
            }
		}
		
		if (
                heapBurnt
//				|| evaporated
		) {
			if (Dungeon.visible[pos]) {
//				if (burnt) {
					burnFX( pos );
//				} else {
//					evaporateFX( pos );
//				}
			}
		}

        if (isEmpty()) {
            destroy();
        } else if (sprite != null) {
            sprite.view( image(), glowing() );
        }
	}
	
	public void freeze( float duration ) {
		
		if (type == Type.CHEST_MIMIC) {
			Mimic m = Mimic.spawnAt( hp, pos, items );
			if (m != null) {
                Frozen buff = Buff.affect( m, Frozen.class );
                buff.add( duration );
				destroy();
			}
		}
		if (type != Type.HEAP) {
			return;
		}
		
//		boolean frozen = false;
//		for (Item item : items.toArray( new Item[0] )) {
//			if (item instanceof RawMeat ) {
//				replace(item, FrozenCarpaccio.cook((RawMeat) item));
//				frozen = true;
//			}
//		}
//
//		if (frozen) {
//			if (isEmpty()) {
//				destroy();
//			} else if (sprite != null) {
//				sprite.view( image(), glowing() );
//			}
//		}
	}
	
	public boolean shatter() {

        if (type != Type.HEAP) {
            return false;
        }

        for (Item item : items.toArray( new Item[0] )) {
            if (item instanceof Potion) {

                ((Potion) item).shatter(pos);

                item.quantity -= Random.Int( item.quantity ) + 1;

                if( item.quantity < 1 )
                    items.remove( item );

                if (isEmpty()) {
                    destroy();
                }

                if( Dungeon.visible[ pos ] ) {
                    GLog.w( TXT_SHATTER, item.name() );
                }

                return true;
            }
        }

        return false;
    }

    public boolean blast() {

        if (type != Type.HEAP) {

            if (type == Type.CRYSTAL_CHEST || type == Type.LOCKED_CHEST || type == Type.TOMB || type == Type.FOR_SALE) {
                sprite.drop();
            } else {
                open();
            }

            return false;
        }

        for ( Item item : items.toArray( new Item[0] ) ) {

            if( !( item instanceof Key) && !item.unique ) {

                boolean success = false;

                if (item.stackable) {

                    item.quantity -= Random.IntRange(1, item.quantity);

                    if (item.quantity < 1) {
                        items.remove(item);
                        success = true;
                    }

                } else if (item.maxDurability() > 0) {

                    item.use(item.maxDurability(), false);

                    if (item.state < 0) {
                        items.remove(item);
                        success = true;
                    }

                } else {

                    if (Random.Int(2) == 0) {
                        items.remove(item);
                        success = true;
                    }

                }

                if( Dungeon.visible[ pos ] ) {
                    GLog.w( success ? TXT_BLOWNUP : TXT_DAMAGED, item.name() );
                    blastFX(pos);
                }
            }
        }

		if (isEmpty()) {
			destroy();
		} else {
			sprite.link();
			sprite.drop();
		}

        return true;
    }

//	public Item brew() {
//
//		CellEmitter.get( pos ).burst( Speck.factory( Speck.BUBBLE ), 3 );
//		Splash.at( pos, 0xFFFFFF, 3 );
//
//        ArrayList<Class<? extends Potion>> variants = new ArrayList<>();
//        HashSet<Class<? extends Potion>> known = Potion.getKnown();
//
//		float chances[] = new float[items.size()];
//		int count = 0;
//
//		int index = 0;
//		for (Item item : items) {
//			if (item instanceof Herb) {
//				count += item.quantity;
//				chances[index++] = item.quantity;
//
//                Herb herb = ((Herb)item);
//
//                if( herb.alchemyClass != null && !known.contains( herb.alchemyClass ) ) {
//                    variants.add( herb.alchemyClass );
//                }
//
//			} else {
//				count = 0;
//				break;
//			}
//		}
//
//		if (count >= HERBS_TO_POTION) {
//
//			CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
//			Sample.INSTANCE.play( Assets.SND_PUFF );
//
//			Herb proto = (Herb)items.get( Random.chances( chances ) );
//			Class<? extends Potion> itemClass = proto.alchemyClass;
//
//			destroy();
//
//			Statistics.potionsCooked++;
//			Badges.validatePotionsCooked();
//
//			if (itemClass == null) {
//				return Generator.random( Generator.Category.POTION );
//			} else {
//				try {
//
//                    Potion result = itemClass.newInstance();
//
//                    if( variants.size() == 1 && itemClass == variants.get(0)) {
//                        result.setKnown();
//                        GLog.i( TXT_KNOWN_BY_BREWING, result.name() );
//                    }
//
//					return result;
//				} catch (Exception e) {
//					return null;
//				}
//			}
//
//		} else {
//			return null;
//		}
//	}
	
	public static void burnFX( int pos ) {
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
		Sample.INSTANCE.play( Assets.SND_BURNING );
	}
	
//	public static void evaporateFX( int pos ) {
//		CellEmitter.get( pos ).burst(Speck.factory(Speck.STEAM), 5);
//        Sample.INSTANCE.play(Assets.SND_PUFF);
//	}

    public static void blastFX(int pos ) {
        CellEmitter.get( pos ).burst( BlastParticle.FACTORY, 15 );
    }
	
	public boolean isEmpty() {
		return items == null || items.size() == 0;
	}
	
	public void destroy() {
		Dungeon.level.heaps.remove( this.pos );
		if (sprite != null) {
			sprite.kill();
		}
		items.clear();
		items = null;
	}

    protected Heap.Type randomHeapType() {

        Heap.Type type = null;

        switch (Random.Int( 20 )) {
            case 0:
            case 1:
            case 2:
            case 3:
                type = Type.BONES;
                break;

            case 4:
                type = Dungeon.depth > 1 && Dungeon.chapter() > Random.Int( 10 ) ? Type.BONES_CURSED : Type.BONES;
                break;

            case 5:
            case 6:
            case 7:
            case 8:
                type = Type.CHEST;
                break;

            case 9:
                type = Dungeon.depth > 1 && Dungeon.chapter() > Random.Int( 10 ) ? Type.CHEST_MIMIC : Type.CHEST;
                break;

            default:
                type = Type.HEAP;
        }

        return type;
    }

    public Heap randomizeType() {

        if( type == Type.HEAP ) {
            type = randomHeapType();
        }

        return this;
    }

    private static final String HP	    = "hp";
    private static final String POS		= "pos";
    private static final String TYPE	= "type";
    private static final String ITEMS	= "items";

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		hp = bundle.getInt( HP );
		pos = bundle.getInt( POS );
		type = Type.valueOf( bundle.getString( TYPE ) );
		items = new LinkedList<Item>( (Collection<? extends Item>)(Object) bundle.getCollection( ITEMS ) );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( HP, hp);
		bundle.put( POS, pos );
		bundle.put( TYPE, type.toString() );
		bundle.put( ITEMS, items );
	}
	
}
