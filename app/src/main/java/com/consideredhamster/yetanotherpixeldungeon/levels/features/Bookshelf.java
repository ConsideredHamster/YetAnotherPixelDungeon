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

import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

    private static final String TXT_FOUND_SCROLL	= "You found a scroll!";
    private static final String TXT_FOUND_NOTHING	= "You found nothing interesting.";
    private static final String TXT_FOUND_READING	= "You found %s";

    private static final String[] BOOKS = {

            // LORE 

			"some religious scripture which tells that this world was created by an almighty and mysterious entity named Watabou, but was then abandoned by Him.", 

			"a legend which tells how Watabou used a miraculous Cube to fill this world with wonderful music of the spheres, but this artifact was completely lost long ago.", 

			"some scripture which tells that this world was torn asunder into a dozens of different realities, and every single one of them was different and existed on its own.", 

			"some ancient scripture which tells that this world was corrupted by a cruel and mischievous entity, which for some reason was considered to be a hamster.", 

			"a mouldy book which tells about how the powerful Wizard of Yendor bravely fought in a great war against a dark god and won it, but only at the cost of his own life.", 

			// DENIZENS 

			"an old book which describes gnolls and their savage habits and rituals. Apparently, gnolls believe that they can absorb qualities of those who they are eating.",

			"a half-torn book which describes various encounters with trolls. These creatures seem to be as reclusive in their daily life as they are ferocious when threatened.", 

			"some old booklet about the glorious technological achievements of dwarven blacksmiths and artificers. There is a blot of something what looks like dried blood on it.", 

			"a grimoire which describes the horrible demonic creatures inhabiting the bowels of this world, one of the most powerful of which is called Yog-Dzewa.", 

			"a mouldy tome which, it seems, told about the rituals to summon different creatures of magical origin - undead, golems, elementals... It is too worn out to be of any use.",

			// CHARACTERS 

			"a history book which tells stories of the ill-tempered, but brave and honourable people of the Thule. No other land is as famous for its warriors and sea traders.", 

			"a geography book which contains description of somewhat wild and barbaric, but cunning and mischievous tribes inhabiting vast southern deserts.",

			"a philosophy book which was written by one of the many sorcerers of the Eastern Empire, which is famous for valuing knowledge above everything else.", 

			"an autobiography book which describes lives of the people of the West - simple, but capable folk who live by the will and blessing of their druidic leaders.",

			// CREDITS 

			"an old book about Inevielle, a flame sorceress. She was also renowned for her linguistic prowess and a knack for funny animals.", 

			"the biography of Logodum, a famous artist whose masterful paintings made this world a much more beautiful place. His works are known even until these days.", 

			"some storybook which tells a fragment of the tale of a weary adventurer. With his trusty knuckleduster and net, he was able to hack through any problem.", 

			"old notes about B'gnu-Thun, a hunter and a craftsman. Beautiful belts, mighty shields and lot of other wondrous things were created thanks to his talent.", 

			"a story about R'byj, a cunning troll warlord who, with his knowledge of tactics and strategies, made a huge impact on the current state of this world.",

			"music sheets written by Jivvy, a talented bard from eastern lands whose songs deserved much greater praise and popularity.",

			"a tome which was written by Evan the Shattered, a great sage who worked tirelessly to improve his world. His deeds inspired many, and were adored by even more.",

            // CONTEST WINNERS

            "a book on the adventures of the legendary Connor Johnson, a persistent fellow who have routinely beaten truly impossible odds due to his numerous talents.",

            "a legend of High Priest Heimdazell, the First of a Hundred. He was wise as an owl and tough as a walnut. New worlds were shaped with his stories.",

            "a myth about Nero, a mad dwarven warrior-king known for his short temper and a terrifying long-handled axe. No challenge was too difficult for him.",

            "an epic story of Krautwich the Brigand who, with his trusty dagger and arbalest at his side, had managed to triumph where all others had failed before.",

            "old tales about the perilous Maze which was created by a sadistic demon god. Sounds interesting, but it is located somewhere on the other side of the world.",

            "a fable telling about great Illion the Three Thousandth and his neverending quest to obtain the almighty Ultimate Mace.",


            // MISC

			"scribbles of some ancient philosopher which are filled with thoughts about how this world is off its balance because it is not in its completed state yet.", 

			"a list of bizarre prophecies, which mentions dead men blowing into a horns, arrival of demons in blood-red raiments, and giant crabs in formal dresses.",

    };

	public static void examine( int cell ) {

        if (Random.Float() < ( 0.05f + 0.05f * Dungeon.chapter() ) ) {

            Dungeon.level.drop( Generator.random( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
            GLog.i( TXT_FOUND_SCROLL );

        } else if (Random.Float() < 0.05f ) {

            GLog.i( TXT_FOUND_READING, BOOKS[ Random.Int( BOOKS.length ) ] );

        } else {

            GLog.i( TXT_FOUND_NOTHING );

        }

        Level.set( cell, Terrain.SHELF_EMPTY );
        Dungeon.observe();
		
		if (Dungeon.visible[cell]) {
            GameScene.updateMap( cell );
			Sample.INSTANCE.play(Assets.SND_OPEN);
		}
	}
}
