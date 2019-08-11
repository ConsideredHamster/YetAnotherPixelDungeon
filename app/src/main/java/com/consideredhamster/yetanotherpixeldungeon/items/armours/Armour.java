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
package com.consideredhamster.yetanotherpixeldungeon.items.armours;

import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.AcidWard;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Deflection;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Durability;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Featherfall;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.FlameWard;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.FrostWard;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Retribution;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Revival;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.StormWard;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Tenacity;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class Armour extends EquipableItem {

	private static final int HITS_TO_KNOW	= 15;

	private static final String TXT_EQUIP_CURSED	= "your %s constricts around you painfully";

	private static final String TXT_IDENTIFY	= "you are now familiar enough with your %s to identify it. It is %s.";

	public int tier;
	public int appearance;

	private int hitsToKnow = Random.IntRange(HITS_TO_KNOW, HITS_TO_KNOW * 2);

	public Glyph glyph;

	public Armour(int tier) {

		this.tier = tier;

	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(UNFAMILIRIARITY, hitsToKnow);
		bundle.put(GLYPH, glyph);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		inscribe((Glyph) bundle.get(GLYPH));
	}

//	@Override
//	public ArrayList<String> actions( Hero hero ) {
//		ArrayList<String> actions = super.actions( hero );
//		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
//		return actions;
//	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isEnchantKnown() {
        return known >= ENCHANT_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.armor == this;
    }

    public boolean isInscribed() {
        return glyph != null;
    }

    public int dr( int bonus ) {
        return 0;
    }

    public int dr() {
        return dr(bonus);
    }

    @Override
    public int lootChapter() {
        return tier;
    }

    @Override
    public int lootLevel() {
        return ( lootChapter() - 1 ) * 6 + state * 2 + bonus * 2 + ( isInscribed() ? 3 + bonus : 0 );
    }

    @Override
    public int priceModifier() { return 3; }

    @Override
    public float stealingDifficulty() { return 0.75f; }

    public String descType() {
        return "";
    }

	public int proc( Char attacker, Char defender, int damage ) {

		if (glyph != null) {
			glyph.proc( this, attacker, defender, damage );
		}

		if (!isIdentified()) {

            float effect = defender.ringBuffs(RingOfKnowledge.Knowledge.class);

            hitsToKnow -= (int) effect;
            hitsToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

			if (hitsToKnow <= 0) {
				identify();
				GLog.w( TXT_IDENTIFY, name(), toString() );
			}
		}

		return damage;
	}

	@Override
	public String name() {
		return ( glyph != null && isEnchantKnown() ? glyph.name( this ) : super.name() );
	}

    public String simpleName() {
        return name;
    }

    @Override
    public Item uncurse() {

        if(bonus == -1) {
            inscribe(null);
        }

        return super.uncurse();
    }

//	@Override
//	public Item random() {

//        bonus = Random.NormalIntRange( -3, +3 );
//
//        if (Random.Int( 7 + bonus ) == 0) {
//			inscribe();
//		}
//
//        randomize_state();

//		return this;
//	}

	public Armour inscribe( Glyph glyph ) {

        this.glyph = glyph;

		return this;

	}

	public Armour inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random();
		while (gl.getClass() == oldGlyphClass) {
			gl = Armour.Glyph.random();
		}
		
		return inscribe( gl );
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null && isEnchantKnown() ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{ 
			FlameWard.class, FrostWard.class, StormWard.class, AcidWard.class,
			Tenacity.class, Featherfall.class, Durability.class, Retribution.class,
			Deflection.class, Revival.class };
		
		private static final float[] chances= new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

        protected static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF5511 );
        protected static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
        protected static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x009900 );
        protected static ItemSprite.Glowing MUSTARD = new ItemSprite.Glowing( 0xBBBB33 );
        protected static ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0x00AAFF );
        protected static ItemSprite.Glowing GRAY = new ItemSprite.Glowing( 0x888888 );
        protected static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );
        protected static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x2244FF );
        protected static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
        protected static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0xAA00AA );
        protected static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF44 );

        protected abstract String name_p();
        protected abstract String name_n();
        protected abstract String desc_p();
        protected abstract String desc_n();

        protected abstract boolean proc_p( Char attacker, Char defender, int damage );
        protected abstract boolean proc_n( Char attacker, Char defender, int damage );

        public Class<? extends Element> resistance() {
            return null;
        }

        public static boolean procced( int bonus ) {

            return Random.Float() < ( 0.1f + 0.1f * ( 1 + Math.abs( bonus ) )* ( bonus >= 0 ?
                Dungeon.hero.ringBuffs( RingOfMysticism.Mysticism.class ) : 1.0f ) );

        }

        public boolean proc( Armour armor, Char attacker, Char defender, int damage ) {
            boolean result = procced( armor.bonus ) && ( armor.bonus >= 0 ?
                    proc_p(attacker, defender, damage) : proc_n(attacker, defender, damage) );

            if( result ) {
                armor.identify( ENCHANT_KNOWN );
            }

            return result;
        }

        public String name( Armour armor ) {
            return String.format( armor.bonus >= 0 ? name_p() : name_n(), armor.name );
        }

        public String desc( Armour armor ) {
            return armor.bonus >= 0 ? desc_p() : desc_n();
        }

		@Override
		public void restoreFromBundle( Bundle bundle ) {	
		}

		@Override
		public void storeInBundle( Bundle bundle ) {	
		}
		
		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}
		
		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner instanceof Hero) {
				
				((Hero)owner).killerGlyph = this;
				Badges.validateDeathFromGlyph();
				return true;
				
			} else {
				return false;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
	}
}
