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

public class RingOfSorcery extends Ring {

	{
		name = "Ring of Sorcery";
        shortName = "So";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Sorcery();
	}
	
//	@Override
//	public Item random() {
//		bonus = +1;
//		return this;
//	}
//
//	@Override
//	public boolean doPickUp( Hero hero ) {
//		identify();
//		Badges.validateRingOfThorns();
//		Badges.validateItemLevelAcquired(this);
//		return super.doPickUp(hero);
//	}
	
//	@Override
//	public boolean isUpgradeable() {
//		return false;
//	}
	
//	@Override
//	public void use() {
//		// Do nothing (it can't curse)
//	}
	
//	@Override
//	public String desc() {
//		return isTypeKnown() ?
//			"Though this ring doesn't provide real thorns, an enemy that attacks you " +
//			"will itself be wounded by a fraction of the damage that it inflicts. " +
//			"If cursed, it would heal an enemy instead of damaging it." :
//			super.desc();
//	}
//
//	public class Sorcery extends RingBuff {
//	}


    @Override
    public String desc() {
        return isTypeKnown() ?
               ( bonus < 0 ? "Normally, this ring " : "This ring " ) +
               "improves your magic skills, increasing your accuracy with your offensive wands. Besides, this ring " +
               "also improves reliability of your wands, decreasing chance to miscast and increasing chance to squeeze additional charges." +
               ( bonus < 0 ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
    }

    public class Sorcery extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    "Your arcane knowledge is improved." :
                    "Your arcane knowledge is decreased." ;
        }
    }
}
