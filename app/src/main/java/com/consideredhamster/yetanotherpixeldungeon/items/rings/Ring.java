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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.ItemStatusHandler;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Ring extends EquipableItem {

	private static final int TICKS_TO_KNOW	= 100;
	
	private static final float TIME_TO_EQUIP = 1f;
	
	private static final String TXT_IDENTIFY_NORMAL = "you are now familiar enough with your %s to identify it. It is %s +%d.";
	private static final String TXT_IDENTIFY_CURSED = "you are now familiar enough with your %s to identify it. It is %s -%d.";

	private static final String TXT_UNEQUIP_TITLE = "Unequip one ring";
	private static final String TXT_UNEQUIP_MESSAGE = 
		"You can only wear two rings at a time. " +
		"Unequip one of your equipped rings.";
	
	protected Buff buff;
	
	private static final Class<?>[] rings = { 
		RingOfVitality.class,
		RingOfAwareness.class,
		RingOfShadows.class,
		RingOfConcentration.class,
		RingOfKnowledge.class,
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfSatiety.class,
		RingOfDurability.class,
		RingOfFortune.class,
		RingOfProtection.class,
		RingOfSorcery.class
	};
	private static final String[] gems = 
		{"diamond", "opal", "garnet", "ruby", "amethyst", "topaz", "onyx", "tourmaline", "emerald", "sapphire", "quartz", "agate"};
	private static final Integer[] images = {
		ItemSpriteSheet.RING_DIAMOND, 
		ItemSpriteSheet.RING_OPAL, 
		ItemSpriteSheet.RING_GARNET, 
		ItemSpriteSheet.RING_RUBY, 
		ItemSpriteSheet.RING_AMETHYST, 
		ItemSpriteSheet.RING_TOPAZ, 
		ItemSpriteSheet.RING_ONYX, 
		ItemSpriteSheet.RING_TOURMALINE, 
		ItemSpriteSheet.RING_EMERALD, 
		ItemSpriteSheet.RING_SAPPHIRE, 
		ItemSpriteSheet.RING_QUARTZ, 
		ItemSpriteSheet.RING_AGATE};
	
	private static ItemStatusHandler<Ring> handler;
	
	private String gem;

	private int ticksToKnow = Random.IntRange(TICKS_TO_KNOW, TICKS_TO_KNOW * 2);
	
	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images, bundle );
	}
	
	public Ring() {
		super();
		syncGem();
        shortName = "??";
    }
	
	public void syncGem() {
		image	= handler.image( this );
		gem		= handler.label( this );
	}
	
//	@Override
//	public ArrayList<String> actions( Hero hero ) {
//		ArrayList<String> actions = super.actions( hero );
//		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
//		return actions;
//	}

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }
	
	@Override
	public boolean doEquip( final Hero hero ) {
		
		if (hero.belongings.ring1 != null && hero.belongings.ring2 != null) {
			
			final Ring r1 = hero.belongings.ring1;
			final Ring r2 = hero.belongings.ring2;
			
			YetAnotherPixelDungeon.scene().add(
				new WndOptions( TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE, 
					Utils.capitalize( r1.toString() ), 
					Utils.capitalize( r2.toString() ) ) {
					
					@Override
					protected void onSelect( int index ) {
						
						detach( hero.belongings.backpack );
						
						Ring equipped = (index == 0 ? r1 : r2);

                        if( QuickSlot.quickslot1.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot1.value = equipped ;

                        if( QuickSlot.quickslot2.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot2.value = equipped ;

                        if( QuickSlot.quickslot3.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot3.value = equipped ;

						if (equipped.doUnequip( hero, true, false )) {

							if( !doEquip( hero ) ) {
                                equipped.doEquip( hero );
                            }

						} else {

							collect( hero.belongings.backpack );

						}
					}
				} );
			
			return false;
			
		} else {

            if( ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                if (hero.belongings.ring1 == null) {
                    hero.belongings.ring1 = this;
                } else {
                    hero.belongings.ring2 = this;
                }

                if (QuickSlot.quickslot1.value == Ring.this)
                    QuickSlot.quickslot1.value = null;

                if (QuickSlot.quickslot2.value == Ring.this)
                    QuickSlot.quickslot2.value = null;

                if (QuickSlot.quickslot3.value == Ring.this)
                    QuickSlot.quickslot3.value = null;

                detach(hero.belongings.backpack);

                onEquip( hero );

                activate(hero);

                hero.spendAndNext( TIME_TO_EQUIP );

                return true;

            } else {

                if ( !collect( hero.belongings.backpack ) ) {
                    Dungeon.level.drop( this, hero.pos ).sprite.drop();
                }

                hero.spendAndNext( TIME_TO_EQUIP * 0.5f );

                return false;

            }
		}
	}

    @Override
	public void activate( Char ch ) {
		buff = buff();
		buff.attachTo( ch );
	}

    public void deactivate( Char ch ) {
        if( buff != null ) {
            buff.target.remove(buff);
            buff = null;
        }
    }

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			
			if (hero.belongings.ring1 == this) {
				hero.belongings.ring1 = null;
			} else {
				hero.belongings.ring2 = null;
			}
			
			hero.remove( buff );
			buff = null;

            QuickSlot.refresh();
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.ring1 == this || hero.belongings.ring2 == this;
	}
	
	@Override
	public Item upgrade() {
		
		super.upgrade();
		
//		if (buff != null) {
//
//			Char owner = buff.target;
//			buff.detach();
//			if ((buff = buff()) != null) {
//				buff.attachTo( owner );
//			}
//		}
		
		return this;
	}
	
	@Override
	public Item curse() {
		
		super.curse();
		
//		if (buff != null) {
//
//			Char owner = buff.target;
//			buff.detach();
//			if ((buff = buff()) != null) {
//				buff.attachTo( owner );
//			}
//		}
		
		return this;
	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
	public boolean isTypeKnown() {
		return handler.isKnown( this );
	}
	
	protected void setKnown() {
		if (!isTypeKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllRingsIdentified();
	}
	
	@Override
	public String name() {
		return isTypeKnown() ? super.name() : gem + " ring";
	}
	
	@Override
	public String desc() {
		return 
			"This metal band is adorned with a large " + gem + " gem " +
			"that glitters in the darkness. Who knows what effect it has when worn?";
	}
	
	@Override
	public String info() {
		if (isEquipped( Dungeon.hero )) {
			
			return desc() + "\n\n" + "The " + name() + " is on your finger" + 
				(bonus < 0 ? ", and because it is cursed, you are powerless to remove it." : "." );
			
		} else if (bonus < 0 && isCursedKnown()) {
			
			return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";
			
		} else {
			
			return desc();
			
		}
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public Item random() {

        Float rand = Random.Float();

        int n = 0;

        if ( rand < Dungeon.depth * 0.03f ) {
            n++;
            if ( rand < Dungeon.depth * 0.02f ) {
                n++;
                if ( rand < Dungeon.depth * 0.01f ) {
                    n++;
                }
            }
        }

        if( n > 0 ) {
            if ( Random.Int( Dungeon.chapter() * 10 + 1 ) < Random.Int( n * 10 + 1 ) ) {
                curse(n);
            } else {
                upgrade(n);
            }
        }

		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == rings.length;
	}

    public static HashSet<Class<? extends Ring>> getKnown() {
        return handler.known();
    }
	
	@Override
	public int price() {

		int price = 100;

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        return price;
	}

    @Override
    public float stealingDifficulty() { return 0.75f; }
	
	protected RingBuff buff() {
		return null;
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, ticksToKnow );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((ticksToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			ticksToKnow = TICKS_TO_KNOW;
		}
	}
	
	public class RingBuff extends Buff {

        protected String desc() {
            return "You don't feel anything special on equipping this ring.";
        }

//		public int energy;
		public int level() {
            return Ring.this.bonus < 0 ? Ring.this.bonus : Ring.this.bonus + 1 ;
        };

        public float effect() {
            return level() >= 0 ? 0.2f + 0.1f * level() : 0.167f * level();
        }
		
		@Override
		public boolean attachTo( Char target ) {

			if (target instanceof Hero && !isTypeKnown()) {
				setKnown();
				GLog.i( desc() );
//				Badges.validateItemLevelAcquired(Ring.this);
			}

			return super.attachTo(target);
		}

		@Override
		public boolean act() {

            if (!isIdentified() && !((Hero)target).restoreHealth) {

                float effect = target.ringBuffs(RingOfKnowledge.Knowledge.class);

                ticksToKnow -= (int) effect;
                ticksToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

                if (ticksToKnow <= 0) {
                    String gemName = name();
                    identify();
                    GLog.w(Ring.this.bonus >= 0 ? TXT_IDENTIFY_NORMAL : TXT_IDENTIFY_CURSED, gemName, Ring.this.name(), Math.abs(Ring.this.bonus));
//                    Badges.validateItemLevelAcquired(Ring.this);
                }
            }

//			use();

			spend( TICK );

			return true;
		}
	}
}
