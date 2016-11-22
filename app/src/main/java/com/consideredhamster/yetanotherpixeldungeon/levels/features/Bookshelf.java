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

import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

    private static final String TXT_FOUND_SCROLL	= "You found a scroll!";
    private static final String TXT_FOUND_NOTHING	= "You found nothing.";
    private static final String TXT_FOUND_READING	= "You found %s.";

    private static final String[] BOOKS = {

            // LORE

            "some religious scripture which tells that this world was created by an almighty and mysterious entity named Watabou, but was then abandoned by Him.",

            "a legend which tells how Watabou used a miraculous cube to fill this world with wonderful music of the pheres, but this artifact was completely lost long ago.",

            "some scripture which tells that this world was torn asunder into a dozens of different realities, and every one of them was different and existed on its own.",

            "some ancient scripture which tells that this world was corrupted by a cruel and mischievous entity, which for some reason was considered to be a hamster.",

            "scribbles of some ancient philosopher which are filled with thoughts about how this world is off its balance because it is not in its completed state yet.",

            "a mouldy book which tells about how the powerful Wizard of Yendor bravely fought in a great war against a dark god and won it, but only at the cost of his own life.",

            "a grimoire which describes the demonic creatures inhabiting the bowels of this world, the most powerful of which is named Yog-Dzewa and who relentlessly tries to consume this world.",

            "a history book which tells stories of ill-tempered, but brave and honourable people of the Thule. No other land is as famous for its warriors and sea traders.",

            "a geography book which contains decsription of somewhat wild and barbaric, but cunning and mischievous tribes inhabiting Southlands.",

            "an philosophy book which was written by one of the many sorcerers of the Eastern Empire, which is famous for valuing knowledge above everything else.",

            "an autobiography book which describes lives of the people of the West - simple, but capable folk which lives by the will and blessing of their numerous gods.",

            // CREDITS

            "an old book about a flame sorceress Inevielle. She was also renowned for her linguistic prowess. The tux crabs are soon to emerge in the depths of this very dungeon thanks to her whining.",

            "a story about R'byj, a cunning troll warlord who, with his knowledge of tactics and strategies, made a huge impact on the current state of this world.",

            "a biography of Logodum, a famous artist whose masterful paintings made this world a much more beautiful place. His works are known even until these days.",

            "some storybook which tells the tale of a weary adventurer. His main gimmick was that he knew a lots of tricks and always had an idea for almost any situation.",

            "a tome which was written by Evan the Shattered, a great sage who worked tirelessly to improve his world. His deeds inspired many, and were adored by even more.",

            "old notes about B'gnu-Thun, a hunter and a craftsman. Beautiful belts, mighty shields and lot of other wondrous things were created thanks to his talent.",

    };

	public static void examine( int cell ) {

        if (Random.Float() < ( 0.05f + 0.05f * Dungeon.chapter() ) ) {

            Dungeon.level.drop( Generator.random( Generator.Category.SCROLL ), cell ).sprite.drop();
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
