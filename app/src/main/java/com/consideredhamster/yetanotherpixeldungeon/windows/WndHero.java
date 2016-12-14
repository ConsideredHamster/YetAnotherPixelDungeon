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
package com.consideredhamster.yetanotherpixeldungeon.windows;

import java.util.Locale;

import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.HuntressArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.RogueArmor;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfAccuracy;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEnergy;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEvasion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfPerception;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSorcery;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.utils.Utils;

public class WndHero extends WndTabbed {
	
	private static final String TXT_STATS	= "Stats";
	private static final String TXT_BUFFS	= "Buffs";
	
	private static final String TXT_EXP		= "Experience";
	private static final String TXT_STR		= "Strength";
	private static final String TXT_HEALTH	= "Health";
	private static final String TXT_SATIETY	= "Satiety";
	private static final String TXT_AWARNSS	= "Detection";
	private static final String TXT_STEALTH	= "Stealth";
	private static final String TXT_WILLPWR = "Magic power";
	private static final String TXT_OFFENSE = "Accuracy";
	private static final String TXT_DEFENSE	= "Dexterity";
	private static final String TXT_MAGIC	= "Wand skill";
	private static final String TXT_GOLD	= "Gold Collected";
	private static final String TXT_DEPTH	= "Maximum Depth";
	
	private static final int WIDTH		= 100;
	private static final int TAB_WIDTH	= 40;
	
	private StatsTab stats;
	private BuffsTab buffs;
	
	private SmartTexture icons;
	private TextureFilm film;
	
	public WndHero() {
		
		super();
		
		icons = TextureCache.get( Assets.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );
		
		stats = new StatsTab();
		add( stats );
		
		buffs = new BuffsTab();
		add( buffs );
		
		add( new LabeledTab( TXT_STATS ) {
			protected void select( boolean value ) {
				super.select( value );
				stats.visible = stats.active = selected;
			};
		} );
		add( new LabeledTab( TXT_BUFFS ) {
			protected void select( boolean value ) {
				super.select( value );
				buffs.visible = buffs.active = selected;
			};
		} );
		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
		}
		
		resize( WIDTH, (int)Math.max( stats.height(), buffs.height() ) );
		
		select( 0 );
	}
	
	private class StatsTab extends Group {
		
		private static final String TXT_TITLE		= "Level %d %s";
//		private static final String TXT_CATALOGUS	= "Knowledge";
//		private static final String TXT_JOURNAL		= "Journal";
		
		private static final int GAP = 3;
		
		private float pos;
		
		public StatsTab() {
			
			Hero hero = Dungeon.hero; 

			BitmapText title = PixelScene.createText( 
				Utils.format( TXT_TITLE, hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ), 9 );
			title.hardlight( TITLE_COLOR );
			title.measure();
			add(title);
			
//			RedButton btnCatalogus = new RedButton( TXT_CATALOGUS ) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndCatalogus() );
//				}
//			};
//			btnCatalogus.setRect( 0, title.y + title.height(), btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
//			add(btnCatalogus);
//
//			RedButton btnJournal = new RedButton( TXT_JOURNAL ) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndJournal() );
//				}
//			};
//			btnJournal.setRect(
//                    btnCatalogus.right() + 1, btnCatalogus.top(),
//                    btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2);
//			add(btnJournal);
			
			pos = title.y + title.height() + GAP;

            statSlot( TXT_EXP, hero.exp + "/" + hero.maxExp() );
            statSlot( TXT_HEALTH, hero.HP + "/" + hero.HT );

            pos += GAP;

            statSlot( TXT_STR, hero.STR() + " (" + hero.STR + ")" );

            if( hero.isDualWielding() ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        !hero.belongings.weap1.isIdentified() || !hero.belongings.weap2.isIdentified() ?
                    "??" : ( ( hero.baseAcc( hero.belongings.weap1, false ) + hero.baseAcc( (Weapon)hero.belongings.weap2, false ) ) / 2 ) ) + " (" + hero.attackSkill + ")" );

            } else if ( hero.belongings.weap1 != null ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1.incompatibleWith( hero.belongings.weap2 ) && !hero.belongings.weap2.isIdentified() ||
                        !hero.belongings.weap1.isIdentified() ?
                    "??" : hero.baseAcc( hero.belongings.weap1, false ) ) + " (" + hero.attackSkill + ")" );

            } else if ( hero.belongings.weap2 instanceof Weapon ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap2.incompatibleWith( hero.belongings.weap1 ) && !hero.belongings.weap1.isIdentified() ||
                        !hero.belongings.weap2.isIdentified() ?
                    "??" : hero.baseAcc( (Weapon)hero.belongings.weap2, false ) ) + " (" + hero.attackSkill + ")" );

            } else {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ?
                    "??" : hero.baseAcc( null, false ) ) + " (" + hero.attackSkill + ")" );

            }

            statSlot(TXT_DEFENSE, (
                        hero.belongings.ring1 instanceof RingOfEvasion && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfEvasion && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.armor != null && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.weap2 instanceof Shield && ( !hero.belongings.weap2.isIdentified() ||
                        hero.belongings.weap2.incompatibleWith( hero.belongings.weap1 ) && !hero.belongings.weap1.isIdentified() )
                    ? "??" : hero.baseDex(false) ) + " (" + hero.defenseSkill + ")");

			statSlot( TXT_MAGIC, (
                        hero.belongings.ring1 instanceof RingOfSorcery && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfSorcery && !hero.belongings.ring2.isIdentified() ?
                    "??" : hero.magicSkill() ) + " (" + hero.magicSkill + ")" );

//			statSlot( TXT_GOLD, Statistics.goldCollected );
//			statSlot( TXT_DEPTH, Statistics.deepestFloor );
			
			pos += GAP;

            statSlot(TXT_WILLPWR, (
                    hero.belongings.armor instanceof MageArmor && !hero.belongings.armor.isIdentified() ||
                            hero.belongings.ring1 instanceof RingOfEnergy && !hero.belongings.ring1.isIdentified() ||
                            hero.belongings.ring2 instanceof RingOfEnergy && !hero.belongings.ring2.isIdentified() ?
                            "??" : (int)( hero.magicPower() * 100 ) ) + "%" );

            statSlot( TXT_AWARNSS, (
                        hero.belongings.armor instanceof HuntressArmor && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.ring1 instanceof RingOfPerception && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfPerception && !hero.belongings.ring2.isIdentified() ?
                    "??" : (int)( hero.awareness() * 100 ) ) + "%" );

            statSlot( TXT_STEALTH, (
                        hero.belongings.armor instanceof RogueArmor && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.ring1 instanceof RingOfShadows && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfShadows && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1 != null && !hero.belongings.weap1.isIdentified() ||
                        hero.belongings.weap2 != null && !hero.belongings.weap2.isIdentified() && !(hero.belongings.weap2 instanceof Wand) ||
                        hero.belongings.armor != null && !hero.belongings.armor.isIdentified() ?
                    "??" : (int)( hero.baseStealth(false) * 100 ) ) + "%" );

            statSlot( TXT_SATIETY, ( hero.isAlive() ?
                (int)Math.ceil( hero.buff( Hunger.class ).value() * 100 ) : 0 ) + "%" );

            pos += GAP;
		}
		
		private void statSlot( String label, String value ) {
			
			BitmapText txt = PixelScene.createText( label, 6 );
			txt.y = pos;
			add( txt );
			
			txt = PixelScene.createText( value, 6 );
			txt.measure();
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			add( txt );
			
			pos += GAP + txt.baseLine();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}
	
	private class BuffsTab extends Group {
		
		private static final int GAP = 2;
		
		private float pos;
		
		public BuffsTab() {
			for (Buff buff : Dungeon.hero.buffs()) {
				buffSlot( buff );
			}
		}
		
		private void buffSlot( Buff buff ) {
			
			int index = buff.icon();
			
			if (index != BuffIndicator.NONE) {
				
				Image icon = new Image( icons );
				icon.frame( film.get( index ) );
				icon.y = pos;
				add( icon );
				
				BitmapText txt = PixelScene.createText( buff.toString(), 8 );
				txt.x = icon.width + GAP;
				txt.y = pos + (int)(icon.height - txt.baseLine()) / 2;
				add( txt );
				
				pos += GAP + icon.height;
			}
		}
		
		public float height() {
			return pos;
		}
	}
}
