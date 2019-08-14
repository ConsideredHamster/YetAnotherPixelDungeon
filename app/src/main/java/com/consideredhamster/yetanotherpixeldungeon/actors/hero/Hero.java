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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.MindVision;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.AcidResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.BodyResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.ColdResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.ElementResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.FireResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.MagicalResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.MindResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.PhysicalResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.ShockResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Banished;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Controlled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Disrupted;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Focus;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Knuckles;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Bookshelf;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.Trap;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndAlchemy;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.GamesInProgress;
import com.consideredhamster.yetanotherpixeldungeon.ResultDescriptions;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Shielding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Combo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Statue;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CheckedCell;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Amulet;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap.Type;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.BodyArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.HuntressArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.RogueArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Revival;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Tenacity;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.Shield;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.GoldenKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.Key;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.SkeletonKey;
import com.consideredhamster.yetanotherpixeldungeon.items.keys.IronKey;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfAccuracy;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfAwareness;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfProtection;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEvasion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfWillpower;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponFlintlock;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponMissile;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Sign;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.InterlevelScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.SurfaceScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.TagAttack;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndMessage;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndTradeItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hero extends Char {

    private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";
    private static final String TXT_LEAVE_WARRIOR = "There is no honor in running away. I must push on.";
    private static final String TXT_LEAVE_SCHOLAR = "It doesn't matter how foolish my decisions were, surrendering now would be even worse.";
    private static final String TXT_LEAVE_BRIGAND = "That is not the stairs I am looking for.\nI must go down, not up.";
    private static final String TXT_LEAVE_ACOLYTE = "I can't go back now. Everything is at stake. Retreating will only delay the inevitable!";

    private static final String TXT_EXP = "%+dEXP";
    private static final String TXT_LEVEL_UP = "level up!";
    private static final String TXT_NEW_LEVEL =
            "Welcome to level %d! You receive %s.";

    public static final String TXT_YOU_NOW_HAVE = "You pick up %s";

    private static final String TXT_SOMETHING_ELSE = "There is something else here";
    private static final String TXT_LOCKED_CHEST = "This chest is locked and you don't have matching key";
    private static final String TXT_LOCKED_DOOR = "You don't have a matching key";
    private static final String TXT_NOTICED_SMTH = "You noticed something";

    private static final String TXT_WOKEN_UP = "You were woken up by someone's presence!";

    private static final String TXT_BREAK_FREE_FAILED = "ensnared";
    private static final String TXT_BREAK_FREE_WORKED = "escaped!";

    private static final String TXT_WAIT = "...";
    private static final String TXT_SEARCH = "search";

    public static final int STARTING_STR = 10;

    private static final float TIME_TO_REST = 1f;
    private static final float TIME_TO_PICKUP = 1f;
    private static final float TIME_TO_SEARCH = 4f;

    public HeroClass heroClass = HeroClass.WARRIOR;
    public HeroSubClass subClass = HeroSubClass.NONE;

    public int magicPower = 15;
    public int attackSkill = 10;
    public int defenseSkill = 5;

    public int strBonus = 0;
    public int lvlBonus = 0;

    public boolean ready = false;

    public HeroAction curAction = null;
    public HeroAction lastAction = null;

    private Char enemy;

    public Armour.Glyph killerGlyph = null;

    private Item theKey;

    public boolean restoreHealth = false;

    public Weapon currentWeapon = null;
    public Armour currentArmour = null;
    public Weapon rangedWeapon = null;
    public Belongings belongings;

    public int STR;

    public int lvl = 1;
    public int exp = 0;

    private ArrayList<Mob> visibleEnemies;

    public Hero(){
        super();
        name = "you";

        HP = HT = 25;
        STR = STARTING_STR;

        belongings = new Belongings( this );

        visibleEnemies = new ArrayList<Mob>();
    }

    @Override
    public int STR(){
        return STR;
//		return Math.max( 1, STR - weakened );
    }

    private static final String ATTACK = "accuracy";
    private static final String DEFENSE = "dexterity";
    private static final String MAGIC = "magicSkill";
    private static final String STRENGTH = "STR";
    private static final String STR_BONUS = "strBonus";
    private static final String LVL_BONUS = "lvlBonus";
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";

    @Override
    public void storeInBundle( Bundle bundle ){
        super.storeInBundle( bundle );

        heroClass.storeInBundle( bundle );
        subClass.storeInBundle( bundle );

        bundle.put( ATTACK, attackSkill );
        bundle.put( DEFENSE, defenseSkill );
        bundle.put( MAGIC, magicPower );

        bundle.put( STRENGTH, STR );

        bundle.put( STR_BONUS, strBonus );
        bundle.put( LVL_BONUS, lvlBonus );

        bundle.put( LEVEL, lvl );
        bundle.put( EXPERIENCE, exp );

        belongings.storeInBundle( bundle );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ){
        super.restoreFromBundle( bundle );

        heroClass = HeroClass.restoreInBundle( bundle );
        subClass = HeroSubClass.restoreInBundle( bundle );

        attackSkill = bundle.getInt( ATTACK );
        defenseSkill = bundle.getInt( DEFENSE );
        magicPower = bundle.getInt( MAGIC );

        STR = bundle.getInt( STRENGTH );
        strBonus = bundle.getInt( STR_BONUS );
        lvlBonus = bundle.getInt( LVL_BONUS );

        lvl = bundle.getInt( LEVEL );
        exp = bundle.getInt( EXPERIENCE );

//        updateAwareness();

//		belongings.restoreFromBundle(bundle);
    }

    @Override
    public int actingPriority(){
        return 5;
    }

    public static void preview( GamesInProgress.Info info, Bundle bundle ){
        info.level = bundle.getInt( LEVEL );
    }

    public String className(){
        return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
    }

    public int viewDistance(){

        if( restoreHealth )
            return 0;

        int distance = buff( Light.class ) != null ? super.viewDistance() : super.viewDistance() / 2 ;

        return GameMath.gate( 1, distance, 8 );
    }

    public int appearance(){
        return belongings.armor == null ? 0 : belongings.armor.appearance;
    }

    public boolean shoot( Char enemy, Weapon wep ){

        rangedWeapon = wep;
        boolean result = attack( enemy );
        rangedWeapon = null;

        return result;
    }

    @Override
    public boolean isRanged(){
        return rangedWeapon != null;
    }


    @Override
    public boolean ignoresDistancePenalty(){
        return rangedWeapon != null && rangedWeapon instanceof RangedWeaponFlintlock;
    }

    @Override
    public boolean ignoresAC(){
        return rangedWeapon != null && rangedWeapon instanceof RangedWeaponFlintlock;
    }

    @Override
    public boolean blocksRanged(){
        return belongings.weap2 instanceof Shield;
    }

    @Override
    public int accuracy(){
        return baseAcc( rangedWeapon != null ? rangedWeapon : currentWeapon, true );
    }

    public int baseAcc( Weapon wep, boolean identified ){

        float modifier = ringBuffsHalved( RingOfAccuracy.Accuracy.class );

        if( buff( Enraged.class ) != null )
            modifier *= 1.25f;

        if( buff( Tormented.class ) != null )
            modifier *= 0.5f;

        if( buff( Charmed.class ) != null )
            modifier *= 0.5f;

        if( buff( Banished.class ) != null )
            modifier *= 0.5f;

        if( buff( Controlled.class ) != null )
            modifier *= 0.5f;

        if( buff( Frozen.class ) != null )
            modifier *= 0.5f;

        if( wep != null ){

            modifier *= wep.penaltyFactor( this, identified || wep.isIdentified() );

        }

        return (int) ( attackSkill * modifier );
    }

    @Override
    public int dexterity(){

        return baseDex( true );

    }

    public int baseDex( boolean identified ){

        if( restoreHealth || morphed )
            return 0;

        float modifier = moving ? ringBuffs( RingOfEvasion.Evasion.class ) : ringBuffsHalved( RingOfEvasion.Evasion.class );

        if( belongings.armor != null ){
            modifier *= belongings.armor.penaltyFactor( this, identified || belongings.armor.isIdentified() );
        }

        if( belongings.weap2 instanceof Shield ){
            modifier *= this.belongings.weap2.penaltyFactor( this, identified || belongings.weap2.isIdentified() );
        }

        if( buff( Crippled.class ) != null && !flying )
            modifier *= 0.5f;

        if( buff( Vertigo.class ) != null )
            modifier *= 0.5f;

        if( buff( Disrupted.class ) != null )
            modifier *= 0.5f;

        if( buff( Frozen.class ) != null )
            modifier *= 0.5f;

        if( buff( Ensnared.class ) != null )
            modifier *= 0.5f;

        if( buff( Levitation.class ) != null )
            modifier *= 1.25f;

        return (int) ( defenseSkill * modifier );
    }

    @Override
    public int magicPower(){
        float modifier = ringBuffsHalved( RingOfWillpower.Willpower.class );

        if( belongings.weap1 instanceof Quarterstaff && belongings.weap1.bonus >= 0 ){
            modifier *= ( 1.1f + 0.05f * belongings.weap1.bonus );
        }

        return (int) ( magicPower * modifier );
    }

    @Override
    public int armourAC(){

        int dr = Random.IntRange( 0, Math.max( 0, STR() - 5 ) );

        if( belongings.armor != null ){

            dr = Math.max( belongings.armor.dr(), 0 );

            int exStr = STR() - belongings.armor.str();

            if( exStr > 0 ){
                dr += Random.IntRange( 0, exStr );
            }

            if( belongings.armor.glyph instanceof Tenacity ){
                dr += belongings.armor.bonus >= 0
                    ? dr * ( HT - HP ) * ( belongings.armor.bonus + 1 ) / HT / 8
                        : dr * ( HT - HP ) * ( belongings.armor.bonus ) / HT / 6;
            }

        }

//        if( buff( Shielding.class ) != null ){
//            dr += HT / 5;
//        }

        dr += (int)(STR() * ringBuffsBaseZero( RingOfProtection.Protection.class ));

        return dr;
    }

    public float guardChance(){
        return !restoreHealth && !morphed ? super.guardChance() : 0.0f;
    }

    @Override
    public int guardStrength(){

        int result = Math.max( 0, STR() - 10 );

        if( belongings.weap2 instanceof Shield ){

            Shield shd = ( (Shield) belongings.weap2 );

            result += shd.dr();

            if ( shd.glyph instanceof Tenacity ){

                result += ( shd.bonus < 0 ? result * ( HT - HP ) * ( shd.bonus ) / ( HT * 3 ) :
                        result * ( HT - HP ) * ( shd.bonus + 1 ) / ( HT * 4 ) );

            }

        } else {

            if( belongings.weap1 != null ){
                result += belongings.weap1.min();
            }

            if( belongings.weap2 instanceof MeleeWeapon ){
                result += ( (MeleeWeapon) belongings.weap2 ).min();
            }

        }

        return result;

    }

    @Override
    public int damageRoll(){
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        int dmg;

        Combo combo = buff( Combo.class );
//        Guard guard = buff( Guard.class );

        if( wep != null ){

            dmg = wep.damageRoll( this );

            if( buff( Enraged.class ) != null ){
                dmg += wep.damageRoll( this ) / 2;
            }

            if( combo != null ){
                dmg += (int) ( wep.damageRoll( this ) * combo.modifier() * ringBuffs( RingOfAccuracy.Accuracy.class ) );
            }

        } else {

            int strMod = Math.max( 0, STR() - 5 );

            dmg = Random.IntRange( 0, strMod );

            if( buff( Enraged.class ) != null ){
                dmg += Math.max( 0, strMod );
            }

            if( combo != null ){
                dmg += (int) ( Random.IntRange( 0, strMod ) * combo.modifier() * ringBuffs( RingOfAccuracy.Accuracy.class ) );
            }

        }

        if( buff( Poisoned.class ) != null )
            dmg /= 2;

        if( buff( Withered.class ) != null )
            dmg /= 2;

        if( buff( Charmed.class ) != null )
            dmg /= 2;

        if( buff( Controlled.class ) != null )
            dmg /= 2;

        return dmg;
    }

    @Override
    public float moveSpeed(){

        float modifier = belongings.armor != null ? belongings.armor.speedFactor( this ) : 1.0f;

        if( belongings.weap2 instanceof Shield ){
            modifier *= belongings.weap2.speedFactor( this );
        }

        return super.moveSpeed() * modifier;
    }

    @Override
    public float attackSpeed(){
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        float value = super.attackSpeed();

        if( wep != null ){

             value *= wep.speedFactor( this ) ;

            if( isDualWielding() ){

                value *= 1.5f;
            }

        } else if( belongings.weap1 == null && belongings.weap2 == null ){

            value *= 2.0f;

        } else if( belongings.weap1 == null || belongings.weap2 == null ) {

            value *= 1.333f;

        }

        return value;
    }

    @Override
    protected float healthValueModifier() {
        return 0.5f;
    }

//	@Override
//	public void spend( float time ) {
//
//        super.spend(time / ringBuffsHalved(RingOfHaste.Haste.class));
//
//	};

    public void spendAndNext( float time ){
        busy();
        spend( time );
        next();
    }

    @Override
    public boolean act(){

        super.act();

        if( morphed ){

            curAction = null;

            spendAndNext( TICK );
            return false;
        }

        Dungeon.observe();
        checkVisibleMobs();
        TagAttack.updateState();
        BuffIndicator.refreshHero();

        if( curAction == null ){

            if( restoreHealth ){

                boolean wakeUp = false;

                for( Mob mob : Dungeon.level.mobs ){
                    if( mob.hostile && !mob.friendly && Level.adjacent( pos, mob.pos ) && detected( mob ) ){
                        wakeUp = true;
                        break;
                    }
                }

                if( wakeUp ){
//					restoreHealth = false;
                    interrupt( TXT_WOKEN_UP );
                } else {
                    spend( TIME_TO_REST );
                    next();
                    return false;
                }
            }

            ready();
            return false;

        } else {

            restoreHealth = false;

            ready = false;

            if( curAction instanceof HeroAction.Move ){

                return actMove( (HeroAction.Move) curAction );

            } else if( curAction instanceof HeroAction.Talk ){

                return actTalk( (HeroAction.Talk) curAction );

            } else if( curAction instanceof HeroAction.Buy ){

                return actBuy( (HeroAction.Buy) curAction );

            } else if( curAction instanceof HeroAction.PickUp ){

                return actPickUp( (HeroAction.PickUp) curAction );

            } else if( curAction instanceof HeroAction.OpenChest ){

                return actOpenChest( (HeroAction.OpenChest) curAction );

            } else if( curAction instanceof HeroAction.Unlock ){

                return actUnlock( (HeroAction.Unlock) curAction );

            } else if( curAction instanceof HeroAction.Examine ){

                return actExamine( (HeroAction.Examine) curAction );

            } else if( curAction instanceof HeroAction.Read ){

                return actRead( (HeroAction.Read) curAction );

            } else if( curAction instanceof HeroAction.Descend ){

                return actDescend( (HeroAction.Descend) curAction );

            } else if( curAction instanceof HeroAction.Ascend ){

                return actAscend( (HeroAction.Ascend) curAction );

            } else if( curAction instanceof HeroAction.Attack ){

                return actAttack( (HeroAction.Attack) curAction );

            } else if( curAction instanceof HeroAction.Cook ){

                return actCook( (HeroAction.Cook) curAction );

            }
        }

        return false;
    }

    public void busy(){
        ready = false;
    }

    public void ready(){

        if( sprite != null ){
            sprite.idle();
        }

        curAction = null;
        ready = true;

        Dungeon.observe();

        GameScene.ready();
    }

    public void interrupt( String awakening ){

        interrupt( awakening, false );

    }

    public void interrupt( String awakening, boolean positive ){

        if( restoreHealth ){

            if( positive ){
                GLog.i( awakening );
            } else {
                GLog.w( awakening );
            }

        }

        interrupt();
    }

    public void interrupt(){

        restoreHealth = false;

        OilLantern lantern = belongings.getItem( OilLantern.class );

        if( isAlive() && lantern != null && lantern.isActivated() && buff( Light.class ) == null ) {
            lantern.activate( this, false );
        } else {
            Dungeon.observe();
        }

        if( isAlive() && curAction != null && !( curAction instanceof HeroAction.Attack ) && curAction.dst != pos ){
            lastAction = curAction;
        }

        curAction = null;
    }

    public void resume(){
        if( isAlive() ){
            curAction = lastAction;
            lastAction = null;
            act();
        }
    }

    private boolean actMove( HeroAction.Move action ){

        if( getCloser( action.dst ) ){

            return true;

        } else {

            if( Dungeon.level.map[ pos ] == Terrain.SIGN ){
//				Sign.read( pos );
                Sign.read();
            }

            ready();

            return false;
        }
    }

    private boolean actTalk( HeroAction.Talk action ){

        NPC npc = action.npc;

        if( Level.adjacent( pos, npc.pos ) ){

            ready();
            sprite.turnTo( pos, npc.pos );
            npc.interact();
            return false;

        } else {

            if( Level.fieldOfView[ npc.pos ] && getCloser( npc.pos ) ){

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    private boolean actBuy( HeroAction.Buy action ){
        int dst = action.dst;
        if( pos == dst || Level.adjacent( pos, dst ) ){

            ready();

            Heap heap = Dungeon.level.heaps.get( dst );
            if( heap != null && heap.type == Type.FOR_SALE && heap.size() == 1 ){

                Shopkeeper shopkeeper = null;

                for( Mob mob : Dungeon.level.mobs ){
                    if( mob instanceof Shopkeeper ){
                        shopkeeper = (Shopkeeper)mob;
                        break;
                    }
                }

                GameScene.show( new WndTradeItem( heap, shopkeeper ) );
            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actCook( HeroAction.Cook action ){

        int dst = action.dst;
        if( Level.distance(dst, pos) <= 1 ) {

            ready();

            YetAnotherPixelDungeon.scene().add(
                    new WndOptions("Alchemy Pot", "Do you want to brew potions or cook meat? ",
                            Utils.capitalize( "Brew potions" ),
                            Utils.capitalize( "Cook meat" ) ) {

                        @Override
                        protected void onSelect( int index ) {

                            if (index == 0) {
                                GameScene.show(new WndAlchemy(WndAlchemy.MODE_BREW));
                            } else if (index == 1) {
                                GameScene.show(new WndAlchemy(WndAlchemy.MODE_COOK));
                            }
                        }
                    } );

            return false;


        } else if (getCloser( dst )) {

            return true;

        } else {
            ready();
            return false;
        }

        /*int dst = action.dst;
        if( Dungeon.visible[ dst ] && Level.adjacent( pos, dst ) ){

            ready();
            AlchemyPot.operate( this, dst );
            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }*/
    }

    private boolean actPickUp( HeroAction.PickUp action ){

        int dst = action.dst;

        if( pos == dst || Level.adjacent( pos, dst ) && ( Level.solid[ dst ] || Level.avoid[ dst ] ) ){

            Heap heap = Dungeon.level.heaps.get( dst );

            if( heap != null ){

                Item item = heap.pickUp();
                if( item.doPickUp( this ) ){

//                    if( item instanceof Waterskin ){
                        // Do nothing
//                    } else {
                        boolean important =
                                ( ( item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment ) && ( item ).isTypeKnown() ) ||
                                        ( ( item instanceof PotionOfStrength || item instanceof PotionOfWisdom ) && ( item ).isTypeKnown() );
                        if( important ){
                            GLog.p( TXT_YOU_NOW_HAVE, item.toString() );
                        } else {
                            GLog.i( TXT_YOU_NOW_HAVE, item.toString() );
                        }
//                    }

//					if (!heap.isEmpty()) {
//						GLog.i( TXT_SOMETHING_ELSE );
//					}

                    spend( TIME_TO_PICKUP );
                    Sample.INSTANCE.play( Assets.SND_ITEM );
                    sprite.pickup( dst );
//					curAction = null;

                    if( invisible <= 0 ){
                        for( int n : Level.NEIGHBOURS8 ){

                            Char ch = Actor.findChar( dst + n );

                            if( ch instanceof Statue ){
                                ( (Statue) ch ).activate();
                            }
                        }
                    }

                } else {
                    Dungeon.level.drop( item, dst ).sprite.drop();
                    ready();
                }
            } else {
                ready();
            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actOpenChest( HeroAction.OpenChest action ){
        int dst = action.dst;
        if( Level.adjacent( pos, dst ) || pos == dst ){

            Heap heap = Dungeon.level.heaps.get( dst );
            if( heap != null && ( heap.type != Type.HEAP && heap.type != Type.FOR_SALE ) ){

                theKey = null;

                if( heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST ){

                    theKey = belongings.getKey( GoldenKey.class, Dungeon.depth );

                    if( theKey == null ){
                        GLog.w( TXT_LOCKED_CHEST );
                        ready();
                        return false;
                    }
                }

                switch( heap.type ){
                    case TOMB:
                        Sample.INSTANCE.play( Assets.SND_TOMB );
                        Camera.main.shake( 1, 0.5f );
                        break;
                    case BONES:
                    case BONES_CURSED:
                        break;
                    default:
                        Sample.INSTANCE.play( Assets.SND_UNLOCK );
                }

                spend( Key.TIME_TO_UNLOCK );
                sprite.operate( dst );

            } else {
                ready();
            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actExamine( HeroAction.Examine action ){

        int dest = action.dst;
        if( Level.adjacent( pos, dest ) ){

            spend( Hero.TIME_TO_PICKUP );
            sprite.operate( dest );

            return false;

        } else if( getCloser( dest ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actRead( HeroAction.Read action ){

        int dest = action.dst;
        if( pos == dest + Level.WIDTH ){

            Sign.read();
            ready();
            return false;

        } else if( getCloser( dest + Level.WIDTH ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock( HeroAction.Unlock action ){
        int doorCell = action.dst;
        if( Level.adjacent( pos, doorCell ) ){

            theKey = null;
            int door = Dungeon.level.map[ doorCell ];

            if( door == Terrain.LOCKED_DOOR ){

                theKey = belongings.getKey( IronKey.class, Dungeon.depth );

            } else if( door == Terrain.LOCKED_EXIT ){

                theKey = belongings.getKey( SkeletonKey.class, Dungeon.depth );

            }

            if( theKey != null ){

                spend( Key.TIME_TO_UNLOCK );
                sprite.operate( doorCell );

                Sample.INSTANCE.play( Assets.SND_UNLOCK );

            } else {
                GLog.w( TXT_LOCKED_DOOR );
                ready();
            }

            return false;

        } else if( getCloser( doorCell ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actDescend( HeroAction.Descend action ){
        int stairs = action.dst;
        if( pos == stairs && pos == Dungeon.level.exit ){

            curAction = null;

//			Hunger hunger = buff( Hunger.class );
//			if (hunger != null && !hunger.isStarving()) {
//				hunger.satisfy( -Hunger.STARVING / 20 );
//			}

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class );

            return false;

        } else if( getCloser( stairs ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAscend( HeroAction.Ascend action ){
        int stairs = action.dst;
        if( pos == stairs && pos == Dungeon.level.entrance ){

            if( Dungeon.depth == 1 ){

                if( belongings.getItem( Amulet.class ) == null ){

                    switch( Dungeon.hero.heroClass ){
                        case WARRIOR:
                            GameScene.show( new WndMessage( TXT_LEAVE_WARRIOR ) );
                            break;
                        case SCHOLAR:
                            GameScene.show( new WndMessage( TXT_LEAVE_SCHOLAR ) );
                            break;
                        case BRIGAND:
                            GameScene.show( new WndMessage( TXT_LEAVE_BRIGAND ) );
                            break;
                        case ACOLYTE:
                            GameScene.show( new WndMessage( TXT_LEAVE_ACOLYTE ) );
                            break;
                        default:
                            GameScene.show( new WndMessage( TXT_LEAVE ) );
                            break;
                    }

                    ready();
                } else {
                    Dungeon.win( ResultDescriptions.WIN );
                    Dungeon.deleteGame( Dungeon.hero.heroClass, true );
                    Game.switchScene( SurfaceScene.class );
                }

            } else {

                curAction = null;

//				Hunger hunger = buff( Hunger.class );
//				if (hunger != null && !hunger.isStarving()) {
//					hunger.satisfy( -Hunger.STARVING / 20 );
//				}

                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene( InterlevelScene.class );
            }

            return false;

        } else if( getCloser( stairs ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAttack( HeroAction.Attack action ){

        enemy = action.target;

        if( enemy.isAlive() ){

            if( belongings.weap1 instanceof RangedWeaponMissile && ( (RangedWeaponMissile) belongings.weap1 ).checkAmmo( this, false ) ){

                RangedWeaponMissile weap = (RangedWeaponMissile) belongings.weap1;

                Item.curUser = this;
                Item.curItem = weap;

                RangedWeaponMissile.shooter.onSelect( enemy.pos );
                curAction = null;
                busy();

                return false;

            } else if( belongings.weap1 instanceof RangedWeaponFlintlock && ( (RangedWeaponFlintlock) belongings.weap1 ).ammunition().isInstance( belongings.weap2 ) ){

                RangedWeaponFlintlock weap = (RangedWeaponFlintlock) belongings.weap1;

                Item.curUser = this;
                Item.curItem = weap;

                if( !weap.loaded ){

                    weap.execute( this, RangedWeaponFlintlock.AC_RELOAD );

                } else if( weap.checkAmmo( this, false ) ){

                    busy();

                    RangedWeaponFlintlock.shooter.onSelect( enemy.pos );

                } else {

                    ready();

                }

                curAction = null;

                return false;

            } else if( belongings.weap2 instanceof ThrowingWeapon && !Level.adjacent( pos, enemy.pos ) ){

                ThrowingWeapon weap = (ThrowingWeapon) belongings.weap2;

                Item.curUser = this;
                Item.curItem = weap;

                busy();

                ThrowingWeapon.shooter.onSelect( enemy.pos );
                curAction = null;

                return false;

            } else if( Level.adjacent( pos, enemy.pos ) ){

                if( buff( Tormented.class ) == null && buff( Banished.class ) == null ){

                    currentWeapon = isDualWielding() ? Random.oneOf( belongings.weap1, (Weapon) belongings.weap2 ) :
                            belongings.weap1 instanceof MeleeWeapon ? belongings.weap1 :
                                    belongings.weap2 instanceof MeleeWeapon ? (Weapon) belongings.weap2 :
                                            null;

                    buff( Satiety.class ).decrease(
                        Satiety.POINT *
                        ( currentWeapon != null ?
                        currentWeapon.str() : 5.0f )
                        / STR() / attackSpeed()
                    );

                    sprite.attack( enemy.pos );
                    spend( attackDelay() );

                } else {

                    GLog.n( Tormented.TXT_CANNOT_ATTACK );
                    ready();

                }

                return false;

            } else {

                if( Level.fieldOfView[ enemy.pos ] && getCloser( enemy.pos ) ){

                    return true;

                } else {

                    ready();
                    return false;

                }
            }

        } else {

            if( Level.fieldOfView[ enemy.pos ] && getCloser( enemy.pos ) ){

                return true;

            } else {
                ready();
                return false;
            }
        }
    }

    public void rest( boolean sleep ){

        spendAndNext( TIME_TO_REST );

        if( !sleep ){

            sprite.showStatus( CharSprite.DEFAULT, TXT_WAIT );
            search( false );

            if( buff( Vertigo.class ) == null ){
                Buff.affect( this, Focus.class ).reset( 1 );
            }

        } else {

            if( !isStarving() ){
                Buff.detach( this, Light.class );
                restoreHealth = true;
            } else {
                GLog.n( "You are too hungry too sleep right now." );
            }
        }

    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ){

        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        Buff.affect( this, Combo.class ).hit();

        if( wep != null ){

            wep.proc( this, enemy, damage );

            if( wep instanceof MeleeWeapon ){
                wep.use( 1 );
            }
        }

        return damage;
    }

    @Override
    public int defenseProc( Char enemy, int damage, boolean blocked ){

        if( blocked ){
            if( belongings.weap2 instanceof Shield ){
                currentArmour = (Shield) belongings.weap2;
                currentArmour.use( 2 );
            } else {
                currentArmour = belongings.armor;

                if( currentWeapon instanceof MeleeWeapon ){
                    currentWeapon.use( 2 );
                }
            }
        } else {
            currentArmour = belongings.armor;

            if( currentArmour != null ) {
                currentArmour.use( 2 );
            }
        }

        return super.defenseProc( enemy, currentArmour != null ? currentArmour.proc( enemy, this, damage ) : damage, blocked );
    }

    @Override
    public void heal( int value ) {

        if( restoreHealth && HP < HT && ( HP + value >= HT ) ){
            interrupt();
        }

        super.heal( value );

    }

    @Override
    public void damage( int dmg, Object src, Element type ){

        interrupt();

        super.damage( dmg, src, type );

    }

    private void checkVisibleMobs(){
        ArrayList<Mob> visible = new ArrayList<>();

        boolean newMob = false;

        for( Mob m : Dungeon.level.mobs ){
            if( Level.fieldOfView[ m.pos ] && m.hostile ){
                visible.add( m );
                if( !visibleEnemies.contains( m ) ){
                    newMob = true;
                }
            }
        }

        Collections.sort( visible, new Comparator<Mob>() {
            @Override
            public int compare( Mob mob1, Mob mob2 ){

                int d1 = Level.distance( Dungeon.hero.pos, mob1.pos );
                int d2 = Level.distance( Dungeon.hero.pos, mob2.pos );

                return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
            }
        } );

        if( newMob && !restoreHealth ){
            interrupt();
        }

        visibleEnemies = visible;
    }

    public int visibleEnemies(){
        return visibleEnemies.size();
    }

    public Mob visibleEnemy( int index ){
        return visibleEnemies.size() > 0 ? visibleEnemies.get( index % visibleEnemies.size() ) : null;
    }

    private boolean getCloser( final int target ){

        if( rooted ){

            if( Random.Float() > 0.01f * STR() ){

                this.sprite.showStatus( CharSprite.WARNING, TXT_BREAK_FREE_FAILED );

                Camera.main.shake( 1, 1f );

                sprite.move( pos, pos );

                spendAndNext( TICK );

                return false;

            } else {

                this.sprite.showStatus( CharSprite.POSITIVE, TXT_BREAK_FREE_WORKED );

                Buff.detach( this, Ensnared.class );

            }
        }

        if( Level.adjacent( pos, target ) ){

            if( Actor.findChar( target ) == null ){

                if( Level.chasm[ target ] && !flying && !Chasm.jumpConfirmed ){

                    Chasm.heroJump( this );
                    interrupt();
                    return false;

                }

                if( Level.passable[ target ] || Level.avoid[ target ] && !Level.illusory[ target ] ){

                    if(
                        Trap.itsATrap( Dungeon.level.map[ target ] )
                        && buff( Vertigo.class) == null
                        && !flying && !Trap.stepConfirmed
                    ) {

                        Trap.askForConfirmation( this );
                        interrupt();

                    } else {

                        return makeStep( target );

                    }
                }
            }

        } else {

            int len = Level.LENGTH;
            boolean[] p = Level.passable;
            boolean[] v = Dungeon.level.visited;
            boolean[] m = Dungeon.level.mapped;
            boolean[] w = Dungeon.level.illusory;
            boolean[] passable = new boolean[ len ];
            for( int i = 0 ; i < len ; i++ ){
                passable[ i ] = p[ i ] && ( v[ i ] || m[ i ] ) && !w[ i ];
            }

            return makeStep( Dungeon.findPath( this, pos, target, passable, Level.fieldOfView ) );
        }

        return false;
    }

    private boolean makeStep( int step ) {

        if( step < 0 ) return false;

        Satiety satiety=buff( Satiety.class );
        if (satiety!=null)
            buff( Satiety.class ).decrease(
                Satiety.POINT *
                ( belongings.armor != null ?
                (float)belongings.armor.str()
                : 5.0f ) / STR()
            );


        int oldPos = pos;
        move( step );
        sprite.move( oldPos, pos );

        if( belongings.weap1 instanceof RangedWeaponFlintlock && belongings.weap2 instanceof Bullets ) {

            RangedWeaponFlintlock weap = (RangedWeaponFlintlock)belongings.weap1;

            if( !weap.loaded ) {
                weap.reload( this );
            }

        }

        if( belongings.weap2 instanceof ThrowingWeapon ) {

            Heap heap = Dungeon.level.heaps.get( step );

            if(
                heap != null && heap.type == Type.HEAP && heap.peek() != null
                && heap.peek().getClass().equals( belongings.weap2.getClass() )
            ){
                Item item = heap.pickUp();
                item.doPickUp( this );
                Sample.INSTANCE.play( Assets.SND_ITEM );
                GLog.i( TXT_YOU_NOW_HAVE, item.toString() );
            }

        }

        spend( 1 / moveSpeed() );

        return true;
    }

    public boolean handle( int cell ){

        if( cell == -1 ){
            return false;
        }

        Char ch;
        Heap heap;

        if( Level.fieldOfView[ cell ] && ( ch = Actor.findChar( cell ) ) instanceof Mob ){

            if( ch instanceof NPC ){
                curAction = new HeroAction.Talk( (NPC) ch );
            } else {
                curAction = new HeroAction.Attack( ch );
            }

        } else if( Level.fieldOfView[ cell ] && ( heap = Dungeon.level.heaps.get( cell ) ) != null ) {

            if( heap.type != Type.HEAP && heap.type != Type.FOR_SALE ) {

                curAction = new HeroAction.OpenChest( cell );

            } else if( cell == pos || visibleEnemies.size() == 0 || Level.solid[ cell ] || Level.avoid[ cell ] ) {

                if( heap.type == Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0 ) {
                    curAction = new HeroAction.Buy( cell );
                } else {
                    curAction = new HeroAction.PickUp( cell );
                }

            } else {

                curAction = new HeroAction.Move( cell );
                lastAction = null;

            }

        } else if( Dungeon.level.map[ cell ] == Terrain.ALCHEMY && cell != pos ){

            curAction = new HeroAction.Cook( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.LOCKED_DOOR || Dungeon.level.map[ cell ] == Terrain.LOCKED_EXIT ){

            curAction = new HeroAction.Unlock( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.BOOKSHELF ){

            curAction = new HeroAction.Examine( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.WALL_SIGN ){

            curAction = new HeroAction.Read( cell );

        } else if( cell == Dungeon.level.exit ){

            curAction = new HeroAction.Descend( cell );

        } else if( cell == Dungeon.level.entrance ){

            curAction = new HeroAction.Ascend( cell );

        } else {

            curAction = new HeroAction.Move( cell );
            lastAction = null;

        }

        return act();
    }

    public void earnExp( int exp ){

        this.exp += exp;

        if( sprite != null )
            sprite.showStatus( CharSprite.POSITIVE, TXT_EXP, exp );

        while( this.exp >= maxExp() ){
            this.exp -= maxExp();

            lvl++;

            int hpBonus = 1;
            int attBonus = 0;
            int defBonus = 0;
            int magBonus = 0;
            int strBonus = 0;
            int stlBonus = 0;
            int detBonus = 0;
            int wilBonus = 0;

            if( heroClass != HeroClass.ACOLYTE || lvl % 6 != 1 )
                hpBonus++;

            if( heroClass != HeroClass.SCHOLAR || lvl % 3 != 1 )
                attBonus++;

            if( heroClass != HeroClass.WARRIOR || lvl % 3 != 1 )
                defBonus++;

//            if( heroClass != HeroClass.BRIGAND || lvl % 3 != 1 )
//                magBonus++;


            if( heroClass == HeroClass.WARRIOR && lvl % 6 == 1 )
                hpBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 3 == 1 )
                attBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 3 == 1 )
                defBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 3 == 1 )
                magBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 6 == 1 )
                magBonus++;


            if( heroClass == HeroClass.WARRIOR && lvl % 10 == 1 )
                strBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 2 == 1 )
                detBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 2 == 1 )
                stlBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 2 == 1 )
                wilBonus++;

            STR += strBonus;

            HT += hpBonus;
            HP += hpBonus;

            attackSkill += attBonus;
            defenseSkill += defBonus;
            magicPower += magBonus;

            ArrayList bonusList = new ArrayList();

            if( hpBonus > 0 )
                bonusList.add( Utils.format( "+%d hp", hpBonus ) );
            if( attBonus > 0 )
                bonusList.add( Utils.format( "+%d acc", attBonus ) );
            if( defBonus > 0 )
                bonusList.add( Utils.format( "+%d dex", defBonus ) );
            if( magBonus > 0 )
                bonusList.add( Utils.format( "+%d mag", magBonus ) );

            if( strBonus > 0 )
                bonusList.add( Utils.format( "+%d strength", strBonus ) );
            if( detBonus > 0 )
                bonusList.add( Utils.format( "+%d%% perception", detBonus ) );
            if( stlBonus > 0 )
                bonusList.add( Utils.format( "+%d%% stealth", stlBonus ) );
            if( wilBonus > 0 )
                bonusList.add( Utils.format( "+%d%% attunement", wilBonus ) );

            if( sprite != null ){
                GLog.p( TXT_NEW_LEVEL, lvl, TextUtils.join( ", ", bonusList ) );
                sprite.showStatus( CharSprite.POSITIVE, TXT_LEVEL_UP );

                sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );

                Sample.INSTANCE.play( Assets.SND_LEVELUP );

                QuickSlot.refresh();
            }

            Badges.validateLevelReached();
        }
    }

    public int maxExp(){
        return ( lvl + 4 ) * ( lvl + 3 ) / 2;
    }


    public boolean isStarving(){
        return ( buff( Satiety.class ) ).isStarving();
    }

    public boolean isDualWielding(){
        return belongings.weap1 instanceof MeleeWeapon && belongings.weap2 instanceof MeleeWeapon ||
                belongings.weap1 instanceof Knuckles && belongings.weap2 == null ||
                belongings.weap2 instanceof Knuckles && belongings.weap1 == null;
    }

//    public boolean isOneHandEmpty(){
//        return belongings.weap1 instanceof MeleeWeapon && belongings.weap2 == null ||
//                belongings.weap2 instanceof MeleeWeapon && belongings.weap1 == null;
//    }

    @Override
    public boolean add( Buff buff ){

        boolean result = super.add( buff );

        interrupt();

        BuffIndicator.refreshHero();

        return result;
    }

    @Override
    public void remove( Buff buff ){

        super.remove( buff );

        BuffIndicator.refreshHero();

    }


    @Override
    public float awareness(){

        float result = super.awareness() * ringBuffsThirded( RingOfAwareness.Awareness.class );

        if( heroClass == HeroClass.SCHOLAR ){
            result *= 0.75f;
        } else if( heroClass == HeroClass.ACOLYTE ){
            result *= 1.1f + 0.01f * (int) ( ( lvl - 1 ) / 2 );
        } else {
            result *= 1.0f;
        }

        if( belongings.armor instanceof HuntressArmor && belongings.armor.bonus >= 0 ){
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( buff( MindVision.class ) != null  ){
            result *= 1.25f;
        }

        if( restoreHealth ){
            result *= 0.5f;
        }

        return result;
    }

    @Override
    public float stealth(){

        return baseStealth( true );

    }

    public float baseStealth( boolean identified ){

        float result = super.stealth() * ringBuffsThirded( RingOfShadows.Shadows.class );

        if( heroClass == HeroClass.WARRIOR ){
            result *= 0.75f;
        } else if( heroClass == HeroClass.BRIGAND ){
            result *= 1.1f + 0.01f * ( ( lvl - 1 ) / 2 );
        }

        if( belongings.armor instanceof RogueArmor && belongings.armor.bonus >= 0 ){
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( buff( Invisibility.class ) != null  ){
            result *= 1.25f;
        }

        if( !restoreHealth ){

            if( belongings.armor != null ){
                result *= belongings.armor.penaltyFactor( this, identified || belongings.armor.isIdentified() );
            }

            if( belongings.weap1 != null ){
                result *= belongings.weap1.penaltyFactor( this, identified || belongings.weap1.isIdentified() );
            }

            if( belongings.weap2 != null ){
                result *= belongings.weap2.penaltyFactor( this, identified || belongings.weap2.isIdentified() );
            }

        }

        return result;
    }

    public float attunement(){

        float result = baseAttunement();

        result *= ringBuffsThirded( RingOfMysticism.Mysticism.class );;

        if( belongings.armor instanceof MageArmor && belongings.armor.bonus >= 0 ){
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( restoreHealth ){
            result *= 0.5f;
        }

        return result;
    }

    public float baseAttunement(){
        if( heroClass == HeroClass.BRIGAND ){
            return  0.75f;
        } else if( heroClass == HeroClass.SCHOLAR ){
            return 1.1f + 0.01f * ( ( lvl - 1 ) / 2 );
        } else {
            return 1.0f;
        }
    }

    @Override
    public void die( Object cause, Element dmg ){

        curAction = null;

        boolean rezzed = false;
        BodyArmor armor = belongings.armor;
        Shield shield = belongings.weap2 instanceof Shield ? (Shield) belongings.weap2 : null;

        if( armor != null && armor.glyph instanceof Revival && Armour.Glyph.procced( armor.bonus ) && Random.Int( 2 ) == 0 ){

            if( armor.bonus >= 0 ){

                armor.identify( Item.ENCHANT_KNOWN );
                Revival.resurrect( this );
                rezzed = true;

            }

        } else if( shield != null && shield.glyph instanceof Revival && Armour.Glyph.procced( shield.bonus ) && Random.Int( 2 ) == 0 ){

            if( shield.bonus >= 0 ){

                shield.identify( Item.ENCHANT_KNOWN );
                Revival.resurrect( this );
                rezzed = true;

            }

        } else {

            Ankh ankh = belongings.getItem( Ankh.class );

            if( ankh != null ){

                Ankh.resurrect( this );
                Statistics.ankhsUsed++;
                ankh.detach( Dungeon.hero.belongings.backpack );
                rezzed = true;

            }
        }

        if( !rezzed ){
            super.die( cause, dmg );
            reallyDie( cause, dmg );
        }
    }

    public static void reallyDie( Object cause, Element dmg ){

        Camera.main.shake( 4, 0.3f );
        GameScene.flash( 0xBB0000 );

        int length = Level.LENGTH;
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Level.discoverable;

        for( int i = 0 ; i < length ; i++ ){

            int terr = map[ i ];

            if( discoverable[ i ] ){

                visited[ i ] = true;
                if( ( Terrain.flags[ terr ] & Terrain.TRAPPED ) != 0 ){
                    Level.set( i, Terrain.discover( terr ) );
                    GameScene.updateMap( i );
                }
            }
        }

        Bones.leave();

        Dungeon.observe();

        Dungeon.hero.belongings.identify();

        int pos = Dungeon.hero.pos;

        ArrayList<Integer> passable = new ArrayList<Integer>();
        for( Integer ofs : Level.NEIGHBOURS8 ){
            int cell = pos + ofs;
            if( ( Level.passable[ cell ] || Level.avoid[ cell ] ) && Dungeon.level.heaps.get( cell ) == null ){
                passable.add( cell );
            }
        }
        Collections.shuffle( passable );

        ArrayList<Item> items = new ArrayList<Item>( Dungeon.hero.belongings.backpack.items );
        for( Integer cell : passable ){
            if( items.isEmpty() ){
                break;
            }

            Item item = Random.element( items );
            Dungeon.level.drop( item, cell ).sprite.drop( pos );
            items.remove( item );
        }

        GameScene.gameOver();

        Dungeon.fail( ResultDescriptions.generateResult( cause, dmg ) );

        GLog.n( ResultDescriptions.generateMessage( cause, dmg ) );

        Dungeon.deleteGame( Dungeon.hero.heroClass, true );
    }

//	@Override
//	public void move( int step ) {
//		super.move(step);
//
//		if (!flying) {
//
//			if (Level.water[pos]) {
//				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
//			} else {
//				Sample.INSTANCE.play( Assets.SND_STEP );
//			}
//		}
//
//        Dungeon.energy.press(pos, this);
//	}

    @Override
    public void onMotionComplete(){
        Dungeon.observe();
        search( false );

        super.onMotionComplete();
    }

    @Override
    public void onAttackComplete(){

        // For a good measure...
        if( enemy instanceof Mob ){
            TagAttack.target( (Mob) enemy );
        }

        attack( enemy );

        curAction = null;

        Invisibility.dispel();

        super.onAttackComplete();
    }

    @Override
    public void onOperateComplete(){

        if( curAction instanceof HeroAction.Unlock ){

            if( theKey != null ){
                theKey.detach( belongings.backpack );
                theKey = null;
            }

            int doorCell = ( (HeroAction.Unlock) curAction ).dst;
            int door = Dungeon.level.map[ doorCell ];

            Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR_CLOSED : Terrain.UNLOCKED_EXIT );
            GameScene.updateMap( doorCell );

        } else if( curAction instanceof HeroAction.Examine ){

            int cell = ( (HeroAction.Examine) curAction ).dst;

            Bookshelf.examine( cell );

        } else if( curAction instanceof HeroAction.OpenChest ){

            if( theKey != null ){
                theKey.detach( belongings.backpack );
                theKey = null;
            }

            Heap heap = Dungeon.level.heaps.get( ( (HeroAction.OpenChest) curAction ).dst );
//			if (heap.type == Type.BONES || heap.type == Type.BONES_CURSED) {
//				Sample.INSTANCE.play( Assets.SND_BONES );
//			}

            heap.open();
        }
        curAction = null;

        super.onOperateComplete();
    }

    public void search( boolean intentional ){

        boolean smthFound = false;

        for( Integer ofs : Level.NEIGHBOURS8 ){

            int p = pos + ofs;

            if( p >= 0 && p < Level.LENGTH && Dungeon.visible[ p ] ){

                if( intentional ){
                    sprite.parent.addToBack( new CheckedCell( p ) );
                }

                if( ( Level.trapped[ p ] || Level.illusory[ p ] )
                    && ( intentional || buff( Light.class ) != null
                    || Random.Float() < ( ( 0.45f - Dungeon.chapter() * 0.05f ) * awareness() ) )
                ) {

                    int oldValue = Dungeon.level.map[ p ];

                    GameScene.discoverTile( p, oldValue );

                    Level.set( p, Terrain.discover( oldValue ) );

                    GameScene.updateMap( p );

                    ScrollOfClairvoyance.discover( p );

                    smthFound = true;
                }
            }
        }

        if( intentional ){
            sprite.showStatus( CharSprite.DEFAULT, TXT_SEARCH );
            spendAndNext( TIME_TO_SEARCH / ( 1.0f + awareness() ) );
            sprite.search();
        }

        if( smthFound ){
            GLog.w( TXT_NOTICED_SMTH );
            Sample.INSTANCE.play( Assets.SND_SECRET );
            interrupt();
        }
    }

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();

        if( buff( Shielding.class ) != null ){
            for( Class<? extends Element> type : Shielding.RESISTS) {
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + Shielding.RESISTANCE );
            }
        }

        if( buff( RingOfProtection.Protection.class ) != null ){

            float value = this.ringBuffsBaseZero( RingOfProtection.Protection.class ) / 2 ;

            for( Class<? extends Element> type : RingOfProtection.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        if( buff( RingOfWillpower.Willpower.class ) != null ){

            float value = this.ringBuffsBaseZero( RingOfWillpower.Willpower.class ) ;

            for( Class<? extends Element> type : RingOfWillpower.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        if( buff( RingOfVitality.Vitality.class ) != null ){

            float value = this.ringBuffsBaseZero( RingOfVitality.Vitality.class ) ;

            for( Class<? extends Element> type : RingOfVitality.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        BodyArmor a = this.belongings.armor;
        if( a != null && a.glyph != null && a.glyph.resistance() != null ){

            Class<? extends Element> type = a.glyph.resistance();

            if( a.bonus >= 0 ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.1f + a.bonus * 0.05f );
            } else {
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f) - 0.05f - a.bonus * 0.05f );
            }
        }

        Shield s = this.belongings.weap2 instanceof Shield ? (Shield) this.belongings.weap2 : null;
        if( s != null && s.glyph != null && s.glyph.resistance() != null ) {

            Class<? extends Element> type = s.glyph.resistance();

            if( s.bonus >= 0 ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.1f + s.bonus * 0.05f );
            } else {
                resistances.put( type, (resistances.containsKey( type ) ? resistances.get( type ) : 0.0f) - 0.05f - s.bonus * 0.05f );
            }
        }

        ElementResistance[] buffResistances = {
            buff(FireResistance.class),
            buff(ColdResistance.class),
            buff(AcidResistance.class),
            buff(ShockResistance.class),
            buff(MindResistance.class),
            buff(BodyResistance.class),
            buff(MagicalResistance.class),
            buff(PhysicalResistance.class),
        };

        for ( ElementResistance res : buffResistances ) {
            if ( res != null ){
                resistances.put( res.resistance(), ( resistances.containsKey( res.resistance() ) ?
                    resistances.get( res.resistance() ) : 0.0f ) + res.resistanceValue()
                );
            }
        }

        return resistances;
    }

    @Override
    public void next(){
        super.next();
    }


}
