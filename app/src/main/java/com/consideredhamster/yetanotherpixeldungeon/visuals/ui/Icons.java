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
package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import android.graphics.RectF;

import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;

public enum Icons {

//    CHALLENGE_OFF,
//    CHALLENGE_ON,

    DIFFICULTY_0,
    DIFFICULTY_1,
    DIFFICULTY_2,
    DIFFICULTY_3,

    YAPD,
    PERSONAL,
    TARGET,
    STAIRS,
    WARRIOR,
    MAGE,
    ROGUE,
    HUNTRESS,

    INFO,
    MASTERY,
    PREFS,
    EXIT,
    DONATE_DISABLED,
    DONATE_ENABLED,
    MIMIC_DAMAGED,
    MIMIC_HEALTHY,
    BILLING_WAIT,

    CHECKED,
    UNCHECKED,
    WARNING,
    CLOSE,
    RESUME,
    WATA,

    BACKPACK,
    KEYRING,
    HERB_POUCH,
    POTION_BELT,
    SCROLL_HOLDER,
    WAND_HOLSTER,

    SKULL,
    BUSY,
    ALERT,
    INSPECT,
    SLEEP,
    DEPTH,
    NOTES,
	POTIONS,
	SCROLLS,
	WANDS,
	RINGS,

    COMPASS,

    DOT_GRN,
    DOT_RED

    ;


	
	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {

        Image icon = new Image( Assets.ICONS );
        
		switch (type) {
//            case CHALLENGE_OFF:
//                icon.frame( rect( icon, 0, 0, 24, 24) );
//                break;
//            case CHALLENGE_ON:
//                icon.frame( rect( icon, 24, 0, 24, 24) );
//                break;
            case DIFFICULTY_0:
                icon.frame( rect( icon, 0, 0, 24, 24) );
                break;
            case DIFFICULTY_1:
                icon.frame( rect( icon, 24, 0, 24, 24) );
                break;
            case DIFFICULTY_2:
                icon.frame( rect( icon, 48, 0, 24, 24) );
                break;
            case DIFFICULTY_3:
                icon.frame( rect( icon, 72, 0, 24, 24) );
                break;

            case YAPD:
                icon.frame( rect( icon, 0, 24, 16, 16) );
                break;
            case PERSONAL:
                icon.frame( rect( icon, 16, 24, 16, 16) );
                break;
//            case SUPPORTED:
//                icon.frame( rect( icon, 32, 24, 16, 16) );
//                break;
            case TARGET:
                icon.frame( rect( icon, 32, 24, 16, 16) );
                break;
            case STAIRS:
                icon.frame( rect( icon, 48, 24, 16, 16) );
                break;
            case WARRIOR:
                icon.frame( rect( icon, 64, 24, 16, 16) );
                break;
            case MAGE:
                icon.frame( rect( icon, 80, 24, 16, 16) );
                break;
            case ROGUE:
                icon.frame( rect( icon, 96, 24, 16, 16) );
                break;
            case HUNTRESS:
                icon.frame( rect( icon, 112, 24, 16, 16) );
                break;

            case INFO:
                icon.frame( rect( icon, 0, 40, 14, 14) );
                break;
            case MASTERY:
                icon.frame( rect( icon, 14, 40, 14, 14) );
                break;
            case PREFS:
                icon.frame( rect( icon, 28, 40, 14, 14) );
                break;
            case EXIT:
                icon.frame( rect( icon, 42, 40, 14, 14) );
                break;

            case DONATE_DISABLED:
                icon.frame( rect( icon, 56, 40, 14, 14) );
                break;
            case DONATE_ENABLED:
                icon.frame( rect( icon, 70, 40, 14, 14) );
                break;
            case MIMIC_DAMAGED:
                icon.frame( rect( icon, 84, 40, 14, 14) );
                break;
            case MIMIC_HEALTHY:
                icon.frame( rect( icon, 98, 40, 14, 14) );
                break;
            case BILLING_WAIT:
                icon.frame( rect( icon, 112, 40, 14, 14) );
                break;

            case CHECKED:
                icon.frame( rect( icon, 0, 54, 12, 12) );
                break;
            case UNCHECKED:
                icon.frame( rect( icon, 12, 54, 12, 12) );
                break;
            case WARNING:
                icon.frame( rect( icon, 24, 54, 12, 12) );
                break;
            case CLOSE:
                icon.frame( rect( icon, 36, 54, 11, 11) );
                break;
            case RESUME:
                icon.frame( rect( icon, 48, 54, 11, 11 ) );
                break;
            case WATA:
                icon.frame( rect( icon, 60, 54, 15, 12) );
                break;

            case BACKPACK:
                icon.frame( rect( icon, 0, 66, 10, 10) );
                break;
            case KEYRING:
                icon.frame( rect( icon, 11, 66, 10, 10) );
                break;
            case HERB_POUCH:
                icon.frame( rect( icon, 22, 66, 10, 10) );
                break;
            case POTION_BELT:
                icon.frame( rect( icon, 33, 66, 10, 10) );
                break;
            case SCROLL_HOLDER:
                icon.frame( rect( icon, 44, 66, 10, 10) );
                break;
            case WAND_HOLSTER:
                icon.frame( rect( icon, 55, 66, 10, 10) );
                break;

            case SKULL:
                icon.frame( rect( icon, 0, 76, 8, 8) );
                break;
            case BUSY:
                icon.frame( rect( icon, 8, 76, 8, 8) );
                break;
            case ALERT:
                icon.frame( rect( icon, 16, 76, 8, 8) );
                break;
            case INSPECT:
                icon.frame( rect( icon, 24, 76, 8, 8) );
                break;
            case SLEEP:
                icon.frame( rect( icon, 32, 76, 8, 8) );
                break;
            case DEPTH:
                icon.frame( rect( icon, 40, 76, 8, 8) );
                break;
            case NOTES:
                icon.frame( rect( icon, 48, 76, 8, 8) );
                break;
            case POTIONS:
                icon.frame( rect( icon, 56, 76, 8, 8) );
                break;
            case SCROLLS:
                icon.frame( rect( icon, 64, 76, 8, 8) );
                break;
            case WANDS:
                icon.frame( rect( icon, 72, 76, 8, 8) );
                break;
            case RINGS:
                icon.frame( rect( icon, 80, 76, 8, 8) );
                break;

            case COMPASS:
                icon.frame( rect( icon, 0, 84, 7, 7) );
                break;

            case DOT_GRN:
                icon.frame( rect( icon, 0, 91, 5, 5) );
                break;
            case DOT_RED:
                icon.frame( rect( icon, 5, 91, 5, 5) );
                break;

		}
		return icon;
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return get( WARRIOR );
		case SCHOLAR:
			return get( MAGE );
		case BRIGAND:
			return get( ROGUE );
		case ACOLYTE:
			return get( HUNTRESS );
		default:
			return null;
		}
	}

    public static Image get( int diff ) {
        switch (diff) {
            case 0:
                return get( DIFFICULTY_0 );
            case 1:
                return get( DIFFICULTY_1 );
            case 2:
                return get( DIFFICULTY_2 );
            case 3:
                return get( DIFFICULTY_3 );
            default:
                return null;
        }
    }

    private static RectF rect(Image icon, int x, int y, int w, int h ) {
        return icon.texture.uvRect( x, y, x + w, y + h);
    }
}
