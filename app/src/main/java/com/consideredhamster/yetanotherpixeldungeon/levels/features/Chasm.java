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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.levels.RegularLevel;
import com.consideredhamster.yetanotherpixeldungeon.levels.Room;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.InterlevelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.MobSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.Random;

public class Chasm {

    // FIXME
    private static Chasm CHASM = new Chasm();

	private static final String TXT_CHASM	= "Chasm";
	private static final String TXT_YES		= "Yes, I know what I'm doing";
	private static final String TXT_NO		= "No, I changed my mind";
    private static final String TXT_POTION	= "Use potion of Levitation";
	private static final String TXT_JUMP 	=
		"Do you really want to jump into the chasm? You can probably die.";

    private static final String TXT_LANDS_SAFELY = "You safely land on the floor!";
    private static final String TXT_SHATTER_PACK = "Your %s has not survived the fall!";

	public static boolean jumpConfirmed = false;
	public static boolean useLevitation = false;

	public static void heroJump( final Hero hero ) {

		GameScene.show(

            ( Potion.getKnown().contains(PotionOfLevitation.class) && hero.belongings.getItem(PotionOfLevitation.class) != null ?

                new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_POTION, TXT_NO) {

                    @Override
                    protected void onSelect(int index) {
                        if (index < 2) {

                            jumpConfirmed = true;

                            if (index == 1) {
                                useLevitation = true;
                            }

                            hero.resume();
                        }
                    }
                }

                : new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO) {

                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {

                            jumpConfirmed = true;

                            hero.resume();

                        }
                    }
                }
            )
        );
	}
	
	public static void heroFall( int pos ) {
		
		jumpConfirmed = false;

        if( !useLevitation ) {
            if( Dungeon.hero.heroClass == HeroClass.ACOLYTE ) {
                Sample.INSTANCE.play(Assets.SND_FALLING, 1.0f, 1.0f, 1.2f);
            } else {
                Sample.INSTANCE.play(Assets.SND_FALLING);
            }
        }
		
		if (Dungeon.hero.isAlive()) {
			Dungeon.hero.interrupt();
			InterlevelScene.mode = InterlevelScene.Mode.FALL;
			if (Dungeon.level instanceof RegularLevel) {
				Room room = ((RegularLevel)Dungeon.level).room( pos );
				InterlevelScene.fallIntoPit = room != null && room.type == Room.Type.WEAK_FLOOR;
			} else {
				InterlevelScene.fallIntoPit = false;
			}
			Game.switchScene( InterlevelScene.class );
		} else {
			Dungeon.hero.sprite.visible = false;
		}
	}
	
	public static void heroLand() {
		
		Hero hero = Dungeon.hero;

        if( useLevitation ) {

            BuffActive.add( hero, Levitation.class, Math.round( Random.Float(PotionOfLevitation.DURATION / 5, PotionOfLevitation.DURATION / 4 ) ));
            GLog.p( TXT_LANDS_SAFELY );
            useLevitation = false;

        } else {

            hero.sprite.burst(hero.sprite.blood(), 10);
            Camera.main.shake(4, 0.2f);

            Item item = hero.belongings.randomUnequipped();
            if (item instanceof Potion) {

                item = item.detach(hero.belongings.backpack);
                GLog.w(TXT_SHATTER_PACK, item.toString());
//            Sample.INSTANCE.play(Assets.SND_SHATTER);
                ((Potion) item).shatter(hero.pos);

            }

            Heap heap = Dungeon.level.heaps.get(hero.pos);
            if (heap != null) {
                heap.shatter();
            }

            int dmg;

            if (hero.belongings.armor != null) {
                dmg = hero.HT * hero.belongings.armor.str() / hero.STR();
            } else {
                dmg = hero.HT * 5 / hero.STR();
            }

            if ( hero.isAlive()) {
                hero.damage(Char.absorb(dmg, hero.armorClass()), CHASM, Element.FALLING);
            } else {
                hero.die(CHASM, Element.FALLING);
            }

        }
	}

	public static void mobFall( Mob mob ) {
		mob.destroy();
		((MobSprite)mob.sprite).fall();
	}
}
