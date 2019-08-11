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
package com.consideredhamster.yetanotherpixeldungeon;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfBlastWave;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.TitleScene;

public class YetAnotherPixelDungeon extends Game {
	
	public YetAnotherPixelDungeon() {
		super( TitleScene.class );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfConcentration" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfWillpower.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSorcery" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfLifeDrain.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfBlink" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfDamnation.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfFlock" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfSmiting.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfHarm" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfThornvines.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfEntanglement" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfIceBarrier.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfFreezing" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfFirebrand.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfFirebolt" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfAcidSpray.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfAvalanche" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfBlastWave.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfPhasing" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfToxicGas.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfCorrosiveGas" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWebbing.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfOvergrowth" );
        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.MysteryMeat" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.FrozenCarpaccio" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.MeatBurned.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.ChargrilledMeat" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.RationSmall.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.OverpricedRation" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.RationMedium.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.Food" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.food.RationLarge.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.food.Pasty" );

        com.watabou.utils.Bundle.addAlias(
                com.consideredhamster.yetanotherpixeldungeon.items.herbs.DreamfoilHerb.class,
                "com.consideredhamster.yetanotherpixeldungeon.items.herbs.DreamweedHerb" );
	}
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		updateImmersiveMode();
		
		DisplayMetrics metrics = new DisplayMetrics();
        instance.getWindowManager().getDefaultDisplay().getMetrics( metrics );
        boolean landscape = metrics.widthPixels > metrics.heightPixels;

        if( Preferences.INSTANCE.getBoolean( Preferences.KEY_LANDSCAPE, false ) != landscape ){
            landscape( !landscape );
        }

        Badges.loadGlobal();

        Music.INSTANCE.enable( music() );
        Sample.INSTANCE.enable( soundFx() );

        Sample.INSTANCE.load(
                Assets.SND_CLICK,
                Assets.SND_BADGE,
                Assets.SND_GOLD,

                Assets.SND_DESCEND,
                Assets.SND_STEP,
                Assets.SND_WATER,
                Assets.SND_OPEN,
                Assets.SND_UNLOCK,
                Assets.SND_ITEM,
                Assets.SND_DEWDROP,
                Assets.SND_HIT,
                Assets.SND_MISS,
                Assets.SND_EAT,
                Assets.SND_READ,
                Assets.SND_LULLABY,
                Assets.SND_DRINK,
                Assets.SND_SHATTER,
                Assets.SND_ZAP,
                Assets.SND_LIGHTNING,
                Assets.SND_LEVELUP,
                Assets.SND_DEATH,
                Assets.SND_CHALLENGE,
                Assets.SND_CURSED,
                Assets.SND_EVOKE,
                Assets.SND_TRAP,
                Assets.SND_TOMB,
                Assets.SND_ALERT,
                Assets.SND_MELD,
                Assets.SND_BOSS,
                Assets.SND_BLAST,
                Assets.SND_PLANT,
                Assets.SND_RAY,
                Assets.SND_BEACON,
                Assets.SND_TELEPORT,
                Assets.SND_CHARMS,
                Assets.SND_MASTERY,
                Assets.SND_PUFF,
                Assets.SND_ROCKS,
                Assets.SND_BURNING,
                Assets.SND_FALLING,
                Assets.SND_GHOST,
                Assets.SND_SECRET,
                Assets.SND_BONES,
                Assets.SND_BEE,
                Assets.SND_DEGRADE,
                Assets.SND_MIMIC);
	}
	
	@Override
	public void onWindowFocusChanged( boolean hasFocus ) {
		
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus) {
			updateImmersiveMode();
		}
	}
	
	public static void switchNoFade( Class<? extends PixelScene> c ) {
		PixelScene.noFade = true;
		switchScene(c);
	}
	
	/*
	 * ---> Preferences
	 */
	
	public static void landscape( boolean value ) {
        if( android.os.Build.VERSION.SDK_INT >= 9 ) {
            Game.instance.setRequestedOrientation( value ?
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
        } else {
            Game.instance.setRequestedOrientation( value ?
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        }

		Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
	}
	
	public static boolean landscape() {
		return width > height;
	}
	
	// *** IMMERSIVE MODE ****
	
	private static boolean immersiveModeChanged = false;
	
	@SuppressLint("NewApi")
	public static void immerse( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_IMMERSIVE, value );
		
		instance.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				updateImmersiveMode();
				immersiveModeChanged = true;
			}
		} );
	}
	
	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height ) {
		super.onSurfaceChanged( gl, width, height );
		
		if (immersiveModeChanged) {
			requestedReset = true;
			immersiveModeChanged = false;
		}
	}
	
	@SuppressLint("NewApi")
	public static void updateImmersiveMode() {
		if (android.os.Build.VERSION.SDK_INT >= 19 && instance != null && instance.getWindow() != null && instance.getWindow().getDecorView() != null) {
			try {
				// Sometime NullPointerException happens here
				instance.getWindow().getDecorView().setSystemUiVisibility( 
					immersed() ?
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | 
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
					View.SYSTEM_UI_FLAG_FULLSCREEN | 
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 
					:
					0 );
			} catch (Exception e) {
				reportException( e );
			}
		}
	}
	
	public static boolean immersed() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_IMMERSIVE, false );
	}
	
	// *****************************
	
	public static void scaleUp( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
		switchScene( TitleScene.class );
	}
	
	public static boolean scaleUp() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
	}

	public static void zoom( int value ) {
		Preferences.INSTANCE.put(Preferences.KEY_ZOOM, value);
	}
	
	public static int zoom() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_ZOOM, 0);
	}
	
	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
	}
	
	public static boolean music() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_MUSIC, true);
	}
	
	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
	}
	
	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
	}
	
	public static void brightness( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
		if (scene() instanceof GameScene) {
			((GameScene)scene()).brightness( value );
		}
	}
	
	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
	}

    public static void loadingTips( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_LOADING_TIPS, value );
    }

    public static boolean searchButton() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_SEARCH_BTN, false);
    }

    public static void searchButton( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_SEARCH_BTN, value );
    }

//    public static void buttons( boolean value ) {
//        Preferences.INSTANCE.put( Preferences.KEY_BUTTONS, value );
//    }
//
//    public static boolean buttons() {
//        return true;
////        return Preferences.INSTANCE.getBoolean(Preferences.KEY_BUTTONS, true);
//    }

    public static int loadingTips() {
        return Preferences.INSTANCE.getInt( Preferences.KEY_LOADING_TIPS, 3 );
    }
	
	public static void donated( String value ) {
		Preferences.INSTANCE.put( Preferences.KEY_DONATED, value );
	}
	
	public static String donated() {
		return Preferences.INSTANCE.getString(Preferences.KEY_DONATED, "");
	}
	
	public static void lastClass( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
	}
	
	public static int lastClass() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_LAST_CLASS, 0);
	}

    public static void lastDifficulty( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_DIFFICULTY, value );
    }

    public static int lastDifficulty() {
        return Preferences.INSTANCE.getInt( Preferences.KEY_DIFFICULTY, 0 );
    }

    public static void lastVersion( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_LAST_VERSION, value );
    }

    public static int lastVersion() {
        return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_VERSION, 0 );
    }
	
	public static void challenges( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
	}
	
	public static int challenges() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
	}
	
	public static void intro( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
	}
	
	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
	}
	
	/*
	 * <--- Preferences
	 */
	
	public static void reportException( Throwable tr ) {
		Log.e( "PD", Log.getStackTraceString( tr ) ); 
	}
}