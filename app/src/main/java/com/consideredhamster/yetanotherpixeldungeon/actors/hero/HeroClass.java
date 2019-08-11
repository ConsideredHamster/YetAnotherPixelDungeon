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
package com.consideredhamster.yetanotherpixeldungeon.actors.hero;

import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.items.food.RationMedium;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.DreamfoilHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.EarthrootHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.FeyleafHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.FirebloomHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.SungrassHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.WhirlvineHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.WyrmflowerHerb;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.EmptyBottle;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfMindVision;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfThunderstorm;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfToxicGas;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfCharm;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfFirebrand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfMagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.ArmorerKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Battery;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.CraftingKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Whetstone;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.HuntressArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.StuddedArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.RogueArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.RoundShield;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Keyring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfRaiseDead;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Sling;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Dagger;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Shortsword;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Knives;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ), BRIGAND( "brigand" ), SCHOLAR( "scholar" ), ACOLYTE( "acolyte" );

	private String title;
	
	private HeroClass( String title ) {
		this.title = title;
	}

    public static final String[] WAR_ABOUT = {
//            "Your ancestors were many a man. Mad warlords and ruthless mercenaries, some. Noble knights and pious crusaders, others. Cowards? None of them.",
//            "Now, your family is broken and ruined, but blood of your fathers still flows strong in your veins. There is a way to remind all the world about your clan's former glory!",
//            "A way as simple as retrieving a single lost treasure hidden down there, below this city. How hard can that be, after all?",

            "Warrior's main advantage is his greater physique - his amount of health is greater than anyone else's and he gets bonus strength with levels.",
            "However, because of huge size and brash attitude, warrior is unreliable when it comes to stealth and evasion, making him somewhat vulnerable to ranged attacks and spells.",
            "The improved survivability and ability to use heavy equipment much earlier make this class the most fitting for beginners.",
    };

    public static final String[] ROG_ABOUT = {
//            "King's decree was simple - swift death at the hands of executioner, or a chance to redeem yourself by stealing one valuable trinket - a familiar work for someone like you.",
//            "Should've been a simple choice, normally. But... this one is in the Dungeon! There is a lots of rumors about this place - evil magics and stuff. You know how it goes with all these spellcasting bastards and their experiments.",
//            "Argh, to hell with that! You have always been a gambler. Nobody would say that you have let the Reaper claim you the easy way. And, who knows... After all of this is done, maybe you'll keep this trinket for yourself?",

            "Nobody can compete with Brigand in dexterity and overall sneakiness, even if he has to limit himself to light armor if he wants to utilize these talents to their fullest.",
            "And he is not lacking in physical fitness or weapon skills either. He would've been the strongest character if not for his disdain of everything magical, especially wands.",
            "Some knowledge of the game mechanics is required to play this class properly, so it can be considered a class for advanced players.",
    };

    public static final String[] MAG_ABOUT = {
//            "Your weary eyes stare down into the abyss. Fingers nervously clutching quarterstaff, searching for calmness in the familiar touch of its wood. This is it.",
//            "All threads lead here. All these years you've spent on seeking the Amulet weren't in vain. The key to all power imaginable, to all knowledge obtainable is hidden in this darkness.",
//            "You only need to brace yourself and make your first step. Your search has ended here. And here, it has only began.",

            "Scholar is the expert wand user. His greater attunement allows him to recharge wands much faster, and he is the most skilled in the arcane arts than other character classes.",
            "However, decades of study have dulled his senses, decreasing his accuracy and perception, forcing him to rely on wands to progress. He is still somewhat strong and agile, though.",
            "Inability to properly use weapons and reliance on wands make him a bit tricky to play as, and therefore this class is only recommended for veteran players.",
    };

    public static final String[] HUN_ABOUT = {
//            "Holy mothers say that fey blood in your veins is a curse for your body but a blessing for your spirit. Frail in build, you always relied on your senses, intuition and faith in the Goddesses.",
//            "Beautiful Artemis, proud Athena and wise Gaia - they have always guided you, sending you insights and prophetic dreams. But, as time went, predictions started to became dark and foreboding.",
//            "They are crystal-clear now - something grows down there, under this City. Something wicked. And it must be nipped in the bud as soon as possible, or else... No gods would save us.",

            "The Acolyte is blessed with extremely sensitive intuition. Her awareness of surroundings and precision of her strikes are unparalleled, allowing for more reliable counterattacks and ranged attacks.",
            "Alas, the elven heritage made her body frail and sickly. Her starting strength is lower and her vitality grows slightly slower with levels when compared with others.",
            "This vulnerability makes Acolyte a fairly challenging class, better fit for an expert player rather than someone who still learns how to play.",
    };

    public static final String[] WAR_DETAILS = {
            "\u007F shortsword",
            "\u007F round shield",
            "\u007F studded armor",
            "\u007F armorer's kit",
            "",
            "+ health",
            "+ strength",
            "",
            "- dexterity",
            "- stealth",
    };

    public static final String[] ROG_DETAILS = {
            "\u007F dagger",
            "\u007F throwing knives x10",
            "\u007F rogue garb",
            "\u007F whetstone",
            "\u007F ring of Shadows",
            "",
            "+ dexterity",
            "+ stealth",
            "",
            "- magic power",
            "- attunement",
    };

    public static final String[] MAG_DETAILS = {
            "\u007F quarterstaff",
            "\u007F wand of Magic Missile",
            "\u007F mystic robe",
            "\u007F arcane battery",
            "\u007F scroll of Raise Dead",
            "",
            "+ magic power",
            "+ attunement",
            "",
            "- accuracy",
            "- perception",
    };

    public static final String[] HUN_DETAILS = {
            "\u007F sling",
            "\u007F bullets x30",
            "\u007F elven cloak",
            "\u007F crafting kit",
            "\u007F empty bottle x3",
            "",
            "+ accuracy",
            "+ perception",
            "",
            "- health",
            "- strength",
    };
	
	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		
		initCommon(hero);
		
		switch (this) {
            case WARRIOR:
                initWarrior( hero );
                break;

            case BRIGAND:
                initRogue( hero );
                break;

            case SCHOLAR:
                initMage( hero );
                break;

            case ACOLYTE:
                initHuntress( hero );
                break;
		}
	}
	
	private static void initCommon( Hero hero ) {

		new Keyring().collect();
        new RationMedium().collect();

        new Waterskin().setLimit( 5 ).fill().collect();
        new OilLantern().collect();

    }
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
        case BRIGAND:
            return Badges.Badge.MASTERY_BRIGAND;
		case SCHOLAR:
			return Badges.Badge.MASTERY_SCHOLAR;
		case ACOLYTE:
			return Badges.Badge.MASTERY_ACOLYTE;
		}
		return null;
	}



    public Badges.Badge victoryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.VICTORY_WARRIOR;
            case BRIGAND:
                return Badges.Badge.VICTORY_BRIGAND;
            case SCHOLAR:
                return Badges.Badge.VICTORY_SCHOLAR;
            case ACOLYTE:
                return Badges.Badge.VICTORY_ACOLYTE;

        }
        return null;
    }
	
	private static void initWarrior( Hero hero ) {

		hero.STR++;

        hero.HP = (hero.HT += 5);
        hero.defenseSkill -= 5;

		(hero.belongings.weap1 = new Shortsword()).identify().repair().fix();
        (hero.belongings.weap2 = new RoundShield()).identify().repair().fix();
        (hero.belongings.armor = new StuddedArmor()).identify().repair().fix();

        new ArmorerKit().collect();

    }

    private static void initRogue( Hero hero ) {

        hero.defenseSkill += 5;
        hero.magicPower -= 5;

        (hero.belongings.weap1 = new Dagger()).identify().repair().fix();
        (hero.belongings.weap2 = new Knives()).quantity(10);
        (hero.belongings.armor = new RogueArmor()).identify().repair().fix();
        (hero.belongings.ring1 = new RingOfShadows()).identify();

        hero.belongings.ring1.activate( hero );

        new Whetstone().collect();
    }
	
	private static void initMage( Hero hero ) {

        hero.magicPower += 5;
        hero.attackSkill -= 5;

		(hero.belongings.weap1 = new Quarterstaff()).identify().repair().fix();
		(hero.belongings.weap2 = new WandOfMagicMissile()).identify().repair().fix();
        (hero.belongings.armor = new MageArmor()).identify().repair().fix();

        ((Wand)hero.belongings.weap2).recharge();

        new ScrollOfRaiseDead().identify().collect();
        new Battery().collect();

	}
	
	private static void initHuntress( Hero hero ) {

        hero.STR--;

        hero.HP = (hero.HT -= 5);
        hero.attackSkill += 5;

        (hero.belongings.weap1 = new Sling()).repair().identify().fix();
        (hero.belongings.weap2 = new Bullets()).quantity( 30 );
        (hero.belongings.armor = new HuntressArmor()).identify().repair().fix();

        new EmptyBottle().quantity(3).collect();
        new CraftingKit().collect();
    }
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case BRIGAND:
                return Assets.BRIGAND;
            case SCHOLAR:
                return Assets.SCHOLAR;
            case ACOLYTE:
                return Assets.ACOLYTE;
		}
		
		return null;
	}

    public String[] history() {

        switch (this) {
            case WARRIOR:
                return WAR_ABOUT;
            case BRIGAND:
                return ROG_ABOUT;
            case SCHOLAR:
                return MAG_ABOUT;
            case ACOLYTE:
                return HUN_ABOUT;
        }

        return null;
    }

	public String[] details() {
		
		switch (this) {
            case WARRIOR:
                return WAR_DETAILS;
            case BRIGAND:
                return ROG_DETAILS;
            case SCHOLAR:
                return MAG_DETAILS;
            case ACOLYTE:
                return HUN_DETAILS;
        }
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : WARRIOR;
	}
}
