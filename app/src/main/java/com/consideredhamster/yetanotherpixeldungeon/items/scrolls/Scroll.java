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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import java.util.ArrayList;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Blinded;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.ItemStatusHandler;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundle;

public abstract class Scroll extends Item {

	private static final String TXT_BLINDED	= "You can't read a scroll while blinded";
	private static final String TXT_CONFUSED	= "You can't read a scroll while confused";

	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;

    private static final Class<?>[] scrolls = {
            ScrollOfIdentify.class,
            ScrollOfTransmutation.class,
            ScrollOfBanishment.class,
            ScrollOfClairvoyance.class,
            ScrollOfSunlight.class,
            ScrollOfDarkness.class,
            ScrollOfChallenge.class,
            ScrollOfPhaseWarp.class,
            ScrollOfTorment.class,
            ScrollOfRaiseDead.class,
            ScrollOfUpgrade.class,
            ScrollOfEnchantment.class,
//            ScrollOfPsionicBlast.class,
//		ScrollOfRecharging.class,
//		ScrollOfLullaby.class,
//		ScrollOfMirrorImage.class,
    };
	private static final String[] runes = 
		{"KAUNAN", "SOWILO", "LAGUZ", "YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ", "NAUDIZ", "BERKANAN", "ODAL", "TIWAZ"};
	private static final Integer[] images = {
		ItemSpriteSheet.SCROLL_KAUNAN, 
		ItemSpriteSheet.SCROLL_SOWILO, 
		ItemSpriteSheet.SCROLL_LAGUZ, 
		ItemSpriteSheet.SCROLL_YNGVI, 
		ItemSpriteSheet.SCROLL_GYFU, 
		ItemSpriteSheet.SCROLL_RAIDO, 
		ItemSpriteSheet.SCROLL_ISAZ, 
		ItemSpriteSheet.SCROLL_MANNAZ, 
		ItemSpriteSheet.SCROLL_NAUDIZ, 
		ItemSpriteSheet.SCROLL_BERKANAN, 
		ItemSpriteSheet.SCROLL_ODAL, 
		ItemSpriteSheet.SCROLL_TIWAZ};
	
	private static ItemStatusHandler<Scroll> handler;

	private String rune;

	protected int spellSprite;
	protected int spellColour;

	{
		stackable = true;		
//		defaultAction = AC_READ;
        shortName = "??";
	}

    @Override
    public String quickAction() {
        return AC_READ;
    }
	
	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images, bundle );
	}
	
	public Scroll() {
		super();
		image = handler.image( this );
		rune = handler.label( this );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {

            if (hero.buff( Blinded.class ) != null) {

                GLog.w( Blinded.TXT_CANNOT_READ );

            } else {

				curUser = hero;
				curItem = detach( hero.belongings.backpack );
				doRead();

			}
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	protected void doRead() {

        isUsed( curUser, (Scroll)curItem );

    };

    @Override
    public boolean isIdentified() {
        return isTypeKnown();
    }

    @Override
	public boolean isTypeKnown() {
		return handler.isKnown(this);
	}
	
	public void setKnown() {
		if (!isTypeKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllScrollsIdentified();
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public String name() {
		return isTypeKnown() ? name : "scroll \"" + rune + "\"";
	}
	
	@Override
	public String info() {
		return isTypeKnown() ?
			desc() :
			"This parchment is covered with indecipherable writing, and bears a title " +
			"of rune " + rune + ". Who knows what it will do when read aloud?";
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}

    protected static void isUsed( Hero user, Scroll scroll ) {

        scroll.setKnown();
        Invisibility.dispel(user);
        user.spendAndNext(TIME_TO_READ);
        Sample.INSTANCE.play(Assets.SND_READ);
        QuickSlot.refresh();

        SpellSprite.show(user, scroll.spellSprite, scroll.spellColour);
        new Flare( 6, 32 ).color(scroll.spellColour, true).show(curUser.sprite, 2f);

    };
	
	@Override
	public int price() {
		return 50 * quantity;
	}
}
