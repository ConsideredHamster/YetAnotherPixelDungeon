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

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.DeadEndLevel;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END = 
		"What are you doing here?!";
	
	private static final String[] TIPS = {



//            "Don't overestimate your strength, use weapons and armor you can handle.",
//            "Dungeon is filled with nasty traps and hidden doors. Watch your step. Look around. Search often.",
//            "Always mind your position. Dodging in narrow tunnels is much harder than dodging in open spaces - use it to your advantage.",
//            "Sometimes it is better to just run away. Enemies can lose your track and open themselves to an ambush, giving you a certain hit.",
//            "You can spend your gold in shops on deeper levels of the dungeon. The first one is in the next room, by the way.",
            "", "", "", "", "", "",

//            "Flying has it's benefits. You can see over obstacles, ignore traps, move silently, and be much less affected by terrain.",
//            "Water can be your friend or enemy. It amplifies lightning and frost effects, but extinguishes fire and washes off acid.",
//            "Equipping the best weapon and armor possible is very helpful down here. Good equipment saves lives.",
//            "It is possible to remove cursed items, if you try enough. It is still quite painful, though.",
//            "Knowledge is born from patience and experience. If you want to identify something, go on and use it.",

            "", "", "", "", "", "",

//            "Try to keep your level as high level as possible. Go back and train a little bit, if you are not sure in yourself.",
//            "Do not underestimate anything what can save you from a dangerous situation. There is nothing worse than getting cornered.",
//            "Smart hunter knows that water is noisy. Smart hunter prefers hiding in high grass. Except when he is on fire.",
//            "Cooking raw meat with fire makes it more edible. Eating herbs can hold off starvation for a while, too.",
//            "Sometimes, it is better to starve for a while, but save food for later. Especially, if you've got other ways to stay alive.",

            "", "", "", "", "", "",

//            "Even if you are ensnared, you can still use your hands or simply keep struggling. Also, you can put yourself on fire.",
//            "Potions of Strength and scrolls of Recharging can dispel magical weakness, do not forget about it.",
//            "Creatures of the magical origins are vulnerable to banishment and vorpal weapons.",
//            "You can keep all these potions of Wisdom until it becomes to hard to increase your level in combat, instead of drinking them immediately.",
//            "Sometimes, it is wiser to skip a level entirely - especially if you've got scrolls of Magic Mapping and potions of Mind Vision.",

            "", "", "", "", "", "",

            "",

            "greetings, mortal" +
                "\n\nare you ready to die?",
            "my servants can smell your blood, human",
            "worship me, and i may yet be merciful" +
                "\n\nthen again, maybe not",
            "You have played this game for too long, mortal" +
                "\n\ni think i shall remove you from the board"

//		"Don't overestimate your strength, use weapons and armor you can handle.",
//		"Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
//		"Remember, that raising your strength is not the only way to access better equipment. You can go " +
//			"the other way, lowering its strength requirement with Scrolls of Upgrade.",
//		"You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 7th bonus.",
//		"You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 7th bonus.",
//
//		"Beware of Goo!",
//
//		"Pixel-Mart - all you need for successful adventure!",
//		"Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
//			"when you actually need them.",
//		"Being hungry doesn't hurt, but starving does hurt.",
//		"Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
//            "a closed door when you know it is approaching.",
//        "Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
//           "a closed door when you know it is approaching.",
//
//		"Don't let The Tengu out!",
//
//		"Pixel-Mart. Spend money. Live longer.",
//		"When you're attacked by several monsters at the same time, try to retreat behind a door.",
//		"If you are burning, you can't put out the fire in the water while levitating.",
//		"There is no sense in possessing more than one Ankh at the same time, because you will lose them upon resurrecting.",
//		"There is no sense in possessing more than one Ankh at the same time, because you will lose them upon resurrecting.",
//
//		"DANGER! Heavy machinery can cause injury, loss of limbs or death!",
//
//		"Pixel-Mart. A safer life in dungeon.",
//		"When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
//		"With a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
//		"The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",
//		"The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",
//
//		"No weapons allowed in the presence of His Majesty!",
//
//		"Pixel-Mart. Special prices for demon hunters!",
//        "Greetings, mortal. Are you ready to die?",
//        "My servants can smell your blood, human.",
//        "Worship me, and I may yet be merciful." +
//            "\n\nThen again, maybe not.",
//        "You have played this game for too long, mortal..." +
//            "\n\nI think I shall remove you from the board.",
//        "Pray to your gods for the last time, mortal." +
//            "\n\nYour service to them ends here.",
	};
	
	private static final String TXT_NOMESSAGE =
		"Whatever was written here is incomprehensible.";
	
//	public static void read( int pos ) {
	public static void read() {

		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;
			
			if (index < TIPS.length && TIPS[index] != "" ) {
				GameScene.show( new WndMessage( TIPS[index] ) );
			} else {
                GameScene.show( new WndMessage( TXT_NOMESSAGE ) );
//				Level.set( pos, Terrain.EMBERS );
//				GameScene.updateMap( pos );
//				GameScene.discoverTile( pos, Terrain.SIGN );
				
//				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
//				Sample.INSTANCE.play( Assets.SND_BURNING );
				
//				GLog.w( TXT_BURN );
				
			}
		}
	}
}
