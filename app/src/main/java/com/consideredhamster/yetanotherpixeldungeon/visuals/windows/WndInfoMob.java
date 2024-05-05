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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.HealthBar;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class WndInfoMob extends WndTitledMessage {

	public WndInfoMob( Mob mob ) {
		
		super( new MobTitle( mob ), desc( mob ) );
		
	}
	
	private static String desc( Mob mob ) {
		
		StringBuilder builder = new StringBuilder( ( mob.friendly || mob.hostile ? "\n\n" + stats( mob ) : "" ) );
		
		builder.append( "\n" + mob.description() );
		builder.append( "\n\n" + ( Bestiary.isBoss( mob ) ? "The " : "This " ) + mob.name + " is " + mob.state.status() + "." );

		return builder.toString();
	}

	private static String stats( Mob mob ) {

		StringBuilder stats = new StringBuilder();

		int mobAccuracy = mob.accuracy() * 2;
		int hitChance = ( mobAccuracy * 100 / ( mobAccuracy + Dungeon.hero.dexterity() ) );

		int heroAccuracy = Dungeon.hero.accuracy() * 2;
		int dodgeChance = 100 - ( heroAccuracy * 100 / ( heroAccuracy + mob.dexterity() ) );

		stats.append( "Mob health: _" + mob.HP + "/" + mob.HT + " HP (" + mob.armorClass() + " AC)_\n" );
		stats.append( "Base damage: _" + mob.minDamage() + "-" + mob.maxDamage() + " (" +
				( ( mob.minDamage() + mob.maxDamage() ) / 2 ) + " on avg.)_\n" );

//        stats.append( "\n" );

		stats.append( "Accuracy: _" + mob.accuracy() + " ("+ hitChance +"% to hit)_\n" );
		stats.append( "Dexterity: _" + mob.dexterity() + " ("+ dodgeChance +"% to dodge)_\n" );

//        stats.append( "\n" );

//        stats.append( "Att speed: _" + (int)(attackSpeed() * 100) + "%_\n" );
//        stats.append( "Mov speed: _" + (int)(attackSpeed() * 100) + "%_\n" );
//
//        stats.append( "\n" );

		ArrayList<String> immunity = new ArrayList<>();
		ArrayList<String> resistant = new ArrayList<>();
		ArrayList<String> vulnerable = new ArrayList<>();

		for( Map.Entry<Class<? extends Element>, Float> entry : mob.resistances().entrySet() ) {
			if( entry.getValue() == Element.Resist.IMMUNE ) {
				immunity.add( entry.getKey().getSimpleName() );
			} else if( entry.getValue() == Element.Resist.PARTIAL ) {
				resistant.add( entry.getKey().getSimpleName() );
			} else if( entry.getValue() == Element.Resist.VULNERABLE ) {
				vulnerable.add( entry.getKey().getSimpleName() );
			}
		}

		if( !immunity.isEmpty() ){

			StringBuilder imm = new StringBuilder(  );
			imm.append( immunity.remove(0));

			for( String s : immunity ) {
				imm.append( ", ");
				imm.append( s );
			}

			stats.append( "Immune to: _" + imm.toString() + "_\n" );
		}
		if( !resistant.isEmpty() ){

			StringBuilder res = new StringBuilder(  );
			res.append( resistant.remove(0));

			for( String s : resistant ) {
				res.append( ", ");
				res.append( s );
			}

			stats.append( "Resistant to: _" + res.toString() + "_\n" );
		}

		if( !vulnerable.isEmpty() ){

			StringBuilder vul = new StringBuilder(  );
			vul.append( vulnerable.remove(0));

			for( String s : vulnerable ) {
				vul.append( ", ");
				vul.append( s );
			}

			stats.append( "Vulnerable to: _" + vul.toString() + "_\n" );
		}

		stats.append( "Special: _" + mob.info + "_\n" );

		return stats.toString();
	}
	
	private static class MobTitle extends Component {
		
		private static final int GAP	= 2;
		
		private CharSprite image;
		private BitmapText name;
		private HealthBar health;
		private BuffIndicator buffs;
		
		public MobTitle( Mob mob ) {
			
			name = PixelScene.createText(
                Utils.capitalize( mob.name ), 9
            );

			name.hardlight( TITLE_COLOR );
			name.measure();	
			add( name );
			
			image = mob.sprite();
			add( image );
			
			health = new HealthBar();
			health.level( (float)mob.HP / mob.HT );
			add( health );
			
			buffs = new BuffIndicator( mob );
			add( buffs );
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() + GAP + health.height() - image.height );
			
			name.x = image.width + GAP;
			name.y = image.height - health.height() - GAP - name.baseLine();
			
			float w = width - image.width - GAP;
			
			health.setRect( image.width + GAP, image.height - health.height(), w, health.height() ); 
			
			buffs.setPos( 
				name.x + name.width() + GAP, 
				name.y + name.baseLine() - BuffIndicator.SIZE );

			height = health.bottom();
		}
	}
}
