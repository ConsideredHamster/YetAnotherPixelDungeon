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
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Bookshelf;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Bones;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.GamesInProgress;
import com.consideredhamster.yetanotherpixeldungeon.ResultDescriptions;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.ForceField;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Bleeding;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Blindness;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Combo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Cripple;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Hunger;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.MindVision;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ooze;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Stun;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Poison;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Regeneration;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Charm;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Confusion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Statue;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs.NPC;
import com.consideredhamster.yetanotherpixeldungeon.effects.CheckedCell;
import com.consideredhamster.yetanotherpixeldungeon.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Amulet;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Ankh;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
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
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfPerception;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfProtection;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEvasion;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfHaste;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfEnergy;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfSorcery;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.Heroic;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponFlintlock;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.RangedWeaponMissile;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.AlchemyPot;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Sign;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.InterlevelScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.SurfaceScene;
import com.consideredhamster.yetanotherpixeldungeon.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.ui.AttackIndicator;
import com.consideredhamster.yetanotherpixeldungeon.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndMessage;
import com.consideredhamster.yetanotherpixeldungeon.windows.WndTradeItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hero extends Char {
	
	private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";
	private static final String TXT_LEAVE_WARRIOR = "There is no honor in running away. I must push on.";
	private static final String TXT_LEAVE_SCHOLAR = "It doesn't matter how foolish my decisions were, surrendering now would be even worse.";
	private static final String TXT_LEAVE_BRIGAND = "That is not the stairs I am looking for.\nI must go down, not up.";
    private static final String TXT_LEAVE_ACOLYTE = "I can't go back now. Everything is at stake. Retreating will only delay the inevitable!";

    private static final String TXT_EXP	= "%+dEXP";
	private static final String TXT_LEVEL_UP = "level up!";
	private static final String TXT_NEW_LEVEL = 
		"Welcome to level %d! You receive %s.";
	
	public static final String TXT_YOU_NOW_HAVE	= "You now have %s";
	
	private static final String TXT_SOMETHING_ELSE	= "There is something else here";
	private static final String TXT_LOCKED_CHEST	= "This chest is locked and you don't have matching key";
	private static final String TXT_LOCKED_DOOR		= "You don't have a matching key";
	private static final String TXT_NOTICED_SMTH	= "You noticed something";

	private static final String TXT_WOKEN_UP	    = "You were woken up by someone's presence!";

	private static final String TXT_BREAK_FREE_FAILED	= "ensnared";
	private static final String TXT_BREAK_FREE_WORKED	= "escaped!";

	private static final String TXT_WAIT	= "...";
	private static final String TXT_SEARCH	= "search";

	public static final int STARTING_STR = 10;
	
	private static final float TIME_TO_REST		= 1f;
	private static final float TIME_TO_PICKUP		= 1f;
	private static final float UNARMED_ATTACK_SPEED = 2.0f;
	private static final float TIME_TO_SEARCH	= 4f;

	public HeroClass heroClass = HeroClass.WARRIOR;
	public HeroSubClass subClass = HeroSubClass.NONE;

    public int magicSkill = 15;
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
	
	public Hero() {
		super();
		name = "you";
		
		HP = HT = 20;
		STR = STARTING_STR;

		belongings = new Belongings( this );
		
		visibleEnemies = new ArrayList<Mob>();
	}

    @Override
	public int STR() {
		return STR;
//		return Math.max( 1, STR - weakened );
	}

	private static final String ATTACK		= "accuracy";
	private static final String DEFENSE		= "dexterity";
	private static final String MAGIC		= "magicSkill";
	private static final String STRENGTH	= "STR";
	private static final String STR_BONUS	= "strBonus";
	private static final String LVL_BONUS	= "lvlBonus";
	private static final String PIE_BONUS	= "pieBonus";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		
		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		bundle.put( MAGIC, magicSkill );

		bundle.put( STRENGTH, STR );

		bundle.put( STR_BONUS, strBonus );
		bundle.put( LVL_BONUS, lvlBonus );

		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		
		belongings.storeInBundle(bundle);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		
		heroClass = HeroClass.restoreInBundle( bundle );
		subClass = HeroSubClass.restoreInBundle(bundle);
		
		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt( DEFENSE );
		magicSkill = bundle.getInt( MAGIC );

		STR = bundle.getInt( STRENGTH );
        strBonus = bundle.getInt( STR_BONUS );
        lvlBonus = bundle.getInt( LVL_BONUS );

        lvl = bundle.getInt( LEVEL );
        exp = bundle.getInt( EXPERIENCE );

//        updateAwareness();

//		belongings.restoreFromBundle(bundle);
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

    public int viewDistance() {

        if( restoreHealth )
            return 0;

        int distance = super.viewDistance();

        distance += (int)( 4 * ringBuffsBaseZero( RingOfPerception.Perception.class ) );

        if( buff( Light.class ) == null )
            distance -= Dungeon.chapter() - 1;

        return GameMath.gate( 1, distance, 8 );
    };
	
	public void live() {
		Buff.affect(this, Regeneration.class);
		Buff.affect(this, Hunger.class);
	}
	
	public int appearance() {
		return belongings.armor == null ? 0 : belongings.armor.appearance;
	}
	
	public boolean shoot( Char enemy, Weapon wep ) {
		
		rangedWeapon = wep;
		boolean result = attack( enemy );
		rangedWeapon = null;
		
		return result;
	}

    @Override
    public boolean isRanged() {
        return rangedWeapon != null && !(rangedWeapon instanceof RangedWeaponFlintlock);
    }

    @Override
    public boolean ignoresAC() {
        return rangedWeapon != null && rangedWeapon instanceof RangedWeaponFlintlock;
    }

	@Override
	public int accuracy() {

        return baseAcc( rangedWeapon != null ? rangedWeapon : currentWeapon, true );

	}

    public int baseAcc( Weapon wep, boolean identified ) {

        float modifier = ringBuffsHalved(RingOfAccuracy.Accuracy.class);

        if ( buff(Confusion.class) != null ) {
            modifier *= 0.5f;
        }

        if ( buff(Blindness.class) != null && buff(MindVision.class) == null ) {
            modifier *= 0.5f;
        }

        if( wep != null ) {

            modifier *= wep.penaltyFactor(this, identified || wep.isIdentified());

        }

        return (int)( attackSkill * modifier );
    }

    @Override
    public int dexterity() {

        return baseDex( true );

    }

    public int baseDex( boolean identified ) {

        if( restoreHealth || stunned || guarded )
            return 0;

        float modifier = moving ? ringBuffs(RingOfEvasion.Evasion.class) : ringBuffsHalved(RingOfEvasion.Evasion.class);

        if( belongings.armor != null ) {
            modifier *= belongings.armor.penaltyFactor( this, identified || belongings.armor.isIdentified() );
        }

        if( belongings.weap2 instanceof Shield ) {
            modifier *= this.belongings.weap2.penaltyFactor( this, identified || belongings.weap2.isIdentified() );
        }

        if ( buff(Ensnared.class) != null ) {
            modifier *= 0.5f;
        }

        if ( buff(Cripple.class) != null && buff(Levitation.class) == null ) {
            modifier *= 0.5f;
        }

        return (int) (defenseSkill * modifier);
    }

    @Override
    public int magicSkill() {
        float modifier = ringBuffsHalved(RingOfSorcery.Sorcery.class);

        return (int) ( magicSkill * modifier );
    }

    @Override
    public int guardEffectiveness() {

        if (guarded) {

            int result = shieldAC();

            if (belongings.weap1 instanceof MeleeWeapon) {
                result += belongings.weap1.min();
            }

            if (belongings.weap2 instanceof MeleeWeapon) {
                result += ((MeleeWeapon) belongings.weap2).min();
            }

            if (belongings.weap2 == null) {
                result *= 2;
            }

            return result;

        } else {

            return 0;

        }
    }

    @Override
    public int armorClass() {

        int result = super.armorClass();

        if (buff( ForceField.class ) != null) {
            result += HT / 5;
        }

        result += STR() * ringBuffsBaseZero(RingOfProtection.Protection.class);

        return result;
    }

    @Override
	public int armourAC() {

        if( belongings.armor != null ) {

            int dr = Math.max( belongings.armor.dr(), 0);

            int exStr = STR() - belongings.armor.str();

            if (exStr > 0) {
                dr += Random.IntRange( 0, exStr );
            }

            if (belongings.armor.glyph instanceof Tenacity) {
                dr += belongings.armor.bonus >= 0
                        ? dr * (HT - HP) * (belongings.armor.bonus + 1) / HT / 8
                        : dr * (HT - HP) * (belongings.armor.bonus) / HT / 6;
            }

            return dr;

        } else {

            return Random.IntRange(0, Math.max( 0, STR() - 5 ) );

        }
	}

    @Override
    public int shieldAC() {

        if ( restoreHealth || stunned ) {
            return 0;
        }

        if( belongings.weap2 instanceof Shield ) {

            Shield shd = ( (Shield)belongings.weap2 );

            int dr = Math.max( shd.dr(), 0);

            int exStr = STR() - shd.str();

            if (exStr > 0) {
                dr += Random.IntRange( 0, exStr );
            }

            if (shd.glyph instanceof Tenacity) {
                dr += shd.bonus >= 0
                        ? dr * (HT - HP) * (shd.bonus + 1) / HT / 8
                        : dr * (HT - HP) * (shd.bonus) / HT / 6;
            }

            return dr;
        }

        return 0;
    }
	
	@Override
	public int damageRoll() {
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

		int dmg;

		if (wep != null) {

			dmg = wep.damageRoll();

            if( wep.enchantment instanceof Heroic) {
                dmg += wep.bonus >= 0
                    ? dmg * (HT - HP) * (wep.bonus + 1) / HT / 8
                    : dmg * (HT - HP) * (wep.bonus) / HT / 6;
            }

            int exStr = STR() - wep.strShown( true );

            if (exStr > 0) {
                dmg += Random.IntRange( 0, exStr );
            }

		} else {

			dmg = Random.IntRange(0, Math.max(0, STR() - 5));

		}

        if( buff( Withered.class ) != null )
            dmg = (int)( dmg * buff( Withered.class ).modifier() );

		return dmg;
	}
	
	@Override
	public float moveSpeed() {

        float modifier = belongings.armor != null ? belongings.armor.speedFactor(this) : 1.0f ;

        if( belongings.weap2 instanceof Shield ) {
            modifier *= belongings.weap2.speedFactor(this);
        }

        return super.moveSpeed() * modifier;
	}

    @Override
	public float attackSpeed() {
		Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

		if (wep != null) {
			
			return wep.speedFactor( this ) * ( isDualWielding() ? 1.5f : 1.0f ) ;
						
		} else {

			return ( belongings.weap1 == null && belongings.weap2 == null ? 2.0f
                    : (belongings.weap1 == null || belongings.weap2 == null ? 1.333f
                    : 1.0f ) );

		}
	}
	
	@Override
	public void spend( float time ) {

        super.spend(time / ringBuffsHalved(RingOfHaste.Haste.class));

	};
	
	public void spendAndNext( float time ) {
		busy();
		spend(time);
		next();
	}
	
	@Override
	public boolean act() {
		
		super.act();
		
		if (stunned) {
			
			curAction = null;
			
			spendAndNext( TICK );
			return false;
		}

        Dungeon.observe();
		checkVisibleMobs();
		AttackIndicator.updateState();
		
		if (curAction == null) {
			
			if (restoreHealth) {

                boolean wakeUp = false;

                for ( Mob mob : Dungeon.level.mobs ) {
                    if ( mob.hostile && Level.adjacent( pos, mob.pos ) && detected( mob ) ) {
                        GLog.w( TXT_WOKEN_UP );
                        wakeUp = true;
                        break;
                    }
                }

				if ( wakeUp ) {
//					restoreHealth = false;
                    interrupt();
				} else {
					spend( TIME_TO_REST ); next();
					return false;
				}
			}
			
			ready();
			return false;
			
		} else {
			
			restoreHealth = false;
			
			ready = false;
			
			if (curAction instanceof HeroAction.Move) {
				
				return actMove( (HeroAction.Move)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Talk) {
				
				return actTalk((HeroAction.Talk) curAction);
				
			} else 
			if (curAction instanceof HeroAction.Buy) {
				
				return actBuy( (HeroAction.Buy)curAction );
				
			}else 
			if (curAction instanceof HeroAction.PickUp) {
				
				return actPickUp( (HeroAction.PickUp)curAction );
				
			} else 
			if (curAction instanceof HeroAction.OpenChest) {
				
				return actOpenChest( (HeroAction.OpenChest)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Unlock) {

                return actUnlock( (HeroAction.Unlock)curAction );

            } else
            if (curAction instanceof HeroAction.Examine) {

                return actExamine( (HeroAction.Examine)curAction );

            } else
            if (curAction instanceof HeroAction.Read) {

                return actRead( (HeroAction.Read)curAction );

            } else
            if (curAction instanceof HeroAction.Descend) {
				
				return actDescend( (HeroAction.Descend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Ascend) {
				
				return actAscend( (HeroAction.Ascend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Attack) {

				return actAttack( (HeroAction.Attack)curAction );
				
			} else
			if (curAction instanceof HeroAction.Cook) {

				return actCook( (HeroAction.Cook)curAction );
				
			}
		}

		return false;
	}
	
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		sprite.idle();
		curAction = null;
		ready = true;

        Dungeon.observe();
		
		GameScene.ready();
	}
	
	public void interrupt() {

        restoreHealth = false;

        Dungeon.observe();

		if (isAlive() && curAction != null && curAction.dst != pos) {
			lastAction = curAction;
		}

		curAction = null;
	}
	
	public void resume() {
        if( isAlive() ) {
            curAction = lastAction;
            lastAction = null;
            act();
        }
	}
	
	private boolean actMove( HeroAction.Move action ) {

		if (getCloser(action.dst)) {
			
			return true;
			
		} else {

			if (Dungeon.level.map[pos] == Terrain.SIGN) {
//				Sign.read( pos );
				Sign.read();
			}

			ready();
			
			return false;
		}
	}
	
	private boolean actTalk(HeroAction.Talk action) {
		
		NPC npc = action.npc;

		if (Level.adjacent(pos, npc.pos)) {
			
			ready();
			sprite.turnTo( pos, npc.pos );
			npc.interact();
			return false;
			
		} else {
			
			if (Level.fieldOfView[npc.pos] && getCloser( npc.pos )) {
				
				return true;
				
			} else {
				ready();
				return false;
			}
			
		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst || Level.adjacent( pos, dst )) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show( new WndTradeItem( heap, true ) );
			}
			
			return false;
			
		} else if (getCloser( dst )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actCook( HeroAction.Cook action ) {
		int dst = action.dst;
		if ( Dungeon.visible[dst] && Level.adjacent( pos, dst ) ) {

			ready();
			AlchemyPot.operate(this, dst);
			return false;
			
		} else if (getCloser( dst )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (
            pos == dst
            || ( ( Dungeon.level.map[dst] == Terrain.PEDESTAL || Level.solid[dst] ) && Level.adjacent( pos, dst ) )
        ) {

			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null) {

				Item item = heap.pickUp();
				if (item.doPickUp( this )) {
					
					if (item instanceof Waterskin) {
						// Do nothing
					} else {
						boolean important = 
							((item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment) && (item).isTypeKnown()) ||
							((item instanceof PotionOfStrength || item instanceof PotionOfWisdom) && (item).isTypeKnown());
						if (important) {
							GLog.p( TXT_YOU_NOW_HAVE, item.toString() );
						} else {
							GLog.i( TXT_YOU_NOW_HAVE, item.toString() );
						}
					}
					
//					if (!heap.isEmpty()) {
//						GLog.i( TXT_SOMETHING_ELSE );
//					}

                    spend( TIME_TO_PICKUP );
                    Sample.INSTANCE.play(Assets.SND_ITEM);
                    sprite.pickup( dst );
//					curAction = null;

                    if( invisible <= 0 ) {
                        for (int n : Level.NEIGHBOURS8) {

                            Char ch = Actor.findChar(dst + n);

                            if (ch instanceof Statue) {
                                ((Statue) ch).activate();
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
			
		} else if (getCloser( dst )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Level.adjacent( pos, dst ) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {
				
				theKey = null;
				
				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey( GoldenKey.class, Dungeon.depth );
					
					if (theKey == null) {
						GLog.w( TXT_LOCKED_CHEST );
						ready();
						return false;
					}
				}
				
				switch (heap.type) {
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
			
		} else if (getCloser( dst )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}

    private boolean actExamine( HeroAction.Examine action ) {

        int dest = action.dst;
        if (Level.adjacent( pos, dest )) {

            spend(Hero.TIME_TO_PICKUP);
            sprite.operate(dest);

            return false;

        } else if (getCloser( dest )) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actRead( HeroAction.Read action ) {

        int dest = action.dst;
        if (pos == dest + Level.WIDTH ) {

            Sign.read();
            ready();
            return false;

        } else if (getCloser( dest + Level.WIDTH )) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock( HeroAction.Unlock action ) {
        int doorCell = action.dst;
        if (Level.adjacent( pos, doorCell )) {

            theKey = null;
            int door = Dungeon.level.map[doorCell];

            if (door == Terrain.LOCKED_DOOR) {

                theKey = belongings.getKey( IronKey.class, Dungeon.depth );

            } else if (door == Terrain.LOCKED_EXIT) {

                theKey = belongings.getKey( SkeletonKey.class, Dungeon.depth );

            }

            if (theKey != null) {

                spend( Key.TIME_TO_UNLOCK );
                sprite.operate(doorCell);

                Sample.INSTANCE.play(Assets.SND_UNLOCK);

            } else {
                GLog.w( TXT_LOCKED_DOOR );
                ready();
            }

            return false;

        } else if (getCloser( doorCell )) {

            return true;

        } else {
            ready();
            return false;
        }
    }
	
	private boolean actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.exit) {
			
			curAction = null;
			
//			Hunger hunger = buff( Hunger.class );
//			if (hunger != null && !hunger.isStarving()) {
//				hunger.satisfy( -Hunger.STARVING / 20 );
//			}

			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);
			
			return false;
			
		} else if (getCloser( stairs )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.entrance) {
			
			if (Dungeon.depth == 1) {
				
				if (belongings.getItem( Amulet.class ) == null) {

                    switch( Dungeon.hero.heroClass ) {
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
			
		} else if (getCloser( stairs )) {
			
			return true;
			
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

        if( enemy.isAlive() && !isCharmedBy( enemy ) ) {

            if ( belongings.weap1 instanceof RangedWeaponMissile && ((RangedWeaponMissile)belongings.weap1).checkAmmo( this, false ) ) {

                RangedWeaponMissile weap = (RangedWeaponMissile)belongings.weap1;

                Item.curUser = this;
                Item.curItem = weap;

                RangedWeaponMissile.shooter.onSelect(enemy.pos);
                curAction = null;

                return false;

            } else if ( belongings.weap1 instanceof RangedWeaponFlintlock && ((RangedWeaponFlintlock)belongings.weap1).ammunition().isInstance( belongings.weap2 ) ) {

                RangedWeaponFlintlock weap = (RangedWeaponFlintlock)belongings.weap1;

                Item.curUser = this;
                Item.curItem = weap;

                if( weap.checkAmmo( this, true ) ) {

                    RangedWeaponFlintlock.shooter.onSelect(enemy.pos);

                } else {

                    ready();

                }

                curAction = null;

                return false;

            } else if ( belongings.weap2 instanceof ThrowingWeapon && !Level.adjacent( pos, enemy.pos ) ) {

                ThrowingWeapon weap = (ThrowingWeapon)belongings.weap2;

                Item.curUser = this;
                Item.curItem = weap;

                ThrowingWeapon.shooter.onSelect( enemy.pos );
                curAction = null;

                return false;

            } else if (Level.adjacent( pos, enemy.pos ) ) {

                spend(attackDelay());
                sprite.attack(enemy.pos);

                return false;

            } else {

                if (Level.fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

                    return true;

                } else {
                    ready();
                    return false;
                }
            }

        } else {

            if (Level.fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

                return true;

            } else {
                ready();
                return false;
            }
        }
	}
	
	public void rest( boolean sleep ) {
		spendAndNext( TIME_TO_REST );
		if (!sleep) {
			sprite.showStatus(CharSprite.DEFAULT, TXT_WAIT);
            search( false );
		} else {
            restoreHealth = true;
        }
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;
		if (wep != null) {

            damage += Buff.affect( this, Combo.class ).hit( (int)(damage * ringBuffs( RingOfAccuracy.Accuracy.class ) ) );

            wep.proc(this, enemy, damage);

//            wep.use();
		}
		
		return damage;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage, boolean blocked ) {



//		RingOfThorns.Thorns thorns = buff( RingOfThorns.Thorns.class );
//		if (thorns != null) {
//			int dmg = Random.IntRange( 0, damage );
//			if (dmg > 0) {
//				enemy.damage( dmg, thorns );
//			}
//		}

//        float thorns = ringBuffs( RingOfThorns.Thorns.class );
		
//		Earthroot.Armor armor = buff( Earthroot.Armor.class );
//		if (armor != null) {
//			damage = armor.absorb( damage );
//		}

        if( blocked ) {
            currentArmour = belongings.weap2 instanceof Shield ? (Shield)belongings.weap2 : null;
        } else {
            currentArmour = belongings.weap2 instanceof Shield ? Random.oneOf( belongings.armor, (Shield)belongings.weap2 ) : belongings.armor;
        }
		
		if (currentArmour != null) {
			damage = currentArmour.proc( enemy, this, damage );

//            belongings.armor.use();
		}
		
		return super.defenseProc( enemy, damage, blocked);
	}
	
	@Override
	public void damage( int dmg, Object src, DamageType type ) {
        restoreHealth = false;

//        Armor armor = belongings.armor;
//
//        if( armor != null && armor.glyph instanceof Deflection && armor.glyph.procced( armor.bonus ) ) {
//            if (src != null && type != null) {
//                src.damage(dmg * 2, null, type);
//                dmg = 0;
//            }
//        }

        super.damage(dmg, src, type);
    }


	private void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<Mob>();
		
		boolean newMob = false;
		
		for (Mob m : Dungeon.level.mobs) {
			if (Level.fieldOfView[ m.pos ] && m.hostile) {
				visible.add( m );
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}
			}
		}
		
		if (newMob) {
            restoreHealth = false;
            interrupt();
		}
		
		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get( index % visibleEnemies.size() );
	}
	
	private boolean getCloser( final int target ) {

		if (rooted) {

            if( Random.Float() > 0.01f * STR() ) {

                this.sprite.showStatus(CharSprite.WARNING, TXT_BREAK_FREE_FAILED);

                Camera.main.shake(1, 1f);

                sprite.move(pos, pos);

                spendAndNext(TICK);

                return false;

            } else {

                this.sprite.showStatus(CharSprite.POSITIVE, TXT_BREAK_FREE_WORKED);

                Buff.detach( this, Ensnared.class );

            }
		}
		
		int step = -1;
		
		if (Level.adjacent( pos, target )) {
			
			if (Actor.findChar( target ) == null) {
				if (Level.chasm[target] && !flying && !Chasm.jumpConfirmed) {
					Chasm.heroJump( this );
					interrupt();
					return false;
				}
				if (Level.passable[target] || Level.avoid[target] && !Level.illusory[target]) {
					step = target;
				}
			}
			
		} else {
		
			int len = Level.LENGTH;
			boolean[] p = Level.passable;
			boolean[] v = Dungeon.level.visited;
			boolean[] m = Dungeon.level.mapped;
            boolean[] w = Dungeon.level.illusory;
            boolean[] passable = new boolean[len];
			for (int i=0; i < len; i++) {
				passable[i] = p[i] && (v[i] || m[i]) && !w[i];
			}
			
			step = Dungeon.findPath( this, pos, target, passable, Level.fieldOfView );
		}
		
		if (step != -1) {
			
			int oldPos = pos;
			move( step );
			sprite.move( oldPos, pos );

//            Dungeon.level.press(pos, this);

			spend( 1 / moveSpeed() );
			
			return true;

		} else {

			return false;
			
		}
	}
	
	public boolean handle( int cell ) {
		
		if (cell == -1) {
			return false;
		}
		
		Char ch;
		Heap heap;
		
		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Cook( cell );
			
		} else if (Level.fieldOfView[cell] && (ch = Actor.findChar( cell )) instanceof Mob) {
			
			if (ch instanceof NPC) {
				curAction = new HeroAction.Talk( (NPC)ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}
			
		} else if (Level.fieldOfView[cell] && (heap = Dungeon.level.heaps.get( cell )) != null) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().price() > 0 ? 
					new HeroAction.Buy( cell ) : 
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

            curAction = new HeroAction.Unlock( cell );

        } else if (Dungeon.level.map[cell] == Terrain.BOOKSHELF) {

            curAction = new HeroAction.Examine( cell );

        } else if (Dungeon.level.map[cell] == Terrain.WALL_SIGN) {

            curAction = new HeroAction.Read( cell );

        } else if (cell == Dungeon.level.exit) {
			
			curAction = new HeroAction.Descend( cell );
			
		} else if (cell == Dungeon.level.entrance) {
			
			curAction = new HeroAction.Ascend( cell );
			
		} else  {
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
			
		}

		return act();
	}
	
	public void earnExp( int exp ) {

		this.exp += exp;

        if( sprite != null )
            sprite.showStatus(CharSprite.POSITIVE, TXT_EXP, exp);

		while (this.exp >= maxExp()) {
			this.exp -= maxExp();

			lvl++;

            int hpBonus = 1;
            int attBonus = 0;
            int defBonus = 0;
            int magBonus = 0;
            int strBonus = 0;
            int stlBonus = 0;
            int detBonus = 0;
            int powBonus = 0;

            if( heroClass != HeroClass.ACOLYTE || lvl % 3 != 1 )
                hpBonus++;

            if( heroClass != HeroClass.SCHOLAR || lvl % 3 != 1 )
                attBonus++;

            if( heroClass != HeroClass.WARRIOR || lvl % 3 != 1 )
                defBonus++;

            if( heroClass != HeroClass.BRIGAND || lvl % 3 != 1 )
                magBonus++;


			if( heroClass == HeroClass.WARRIOR && lvl % 3 == 1 )
				hpBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 3 == 1 )
                attBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 3 == 1 )
                defBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 3 == 1 )
                magBonus++;


            if( heroClass == HeroClass.WARRIOR && lvl % 10 == 1 )
                strBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 2 == 1 )
                detBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 2 == 1 )
                stlBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 2 == 1 )
                powBonus++;

            STR += strBonus;

            HT += hpBonus;
            HP += hpBonus;

            attackSkill += attBonus;
            defenseSkill += defBonus;
            magicSkill += magBonus;

//			if (lvl < 10) {
//				updateAwareness();
//			}

            ArrayList bonusList = new ArrayList();

            if ( hpBonus > 0 )
                bonusList.add( Utils.format("+%d hp", hpBonus ) );
            if ( attBonus > 0 )
                bonusList.add( Utils.format("+%d acc", attBonus ) );
            if ( defBonus > 0 )
                bonusList.add( Utils.format("+%d dex", defBonus ) );
            if ( magBonus > 0 )
                bonusList.add( Utils.format("+%d wnd", magBonus ) );

            if ( strBonus > 0 )
                bonusList.add( Utils.format("+%d strength", strBonus ) );
            if ( detBonus > 0 )
                bonusList.add( Utils.format("+%d%% detection", detBonus ) );
            if ( stlBonus > 0 )
                bonusList.add( Utils.format("+%d%% stealth", stlBonus ) );
            if ( powBonus > 0 )
                bonusList.add( Utils.format("+%d%% m.power", powBonus ) );

            if( sprite != null ) {
                GLog.p(TXT_NEW_LEVEL, lvl, TextUtils.join(", ", bonusList));
                sprite.showStatus(CharSprite.POSITIVE, TXT_LEVEL_UP);

                sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);

                Sample.INSTANCE.play(Assets.SND_LEVELUP);

                QuickSlot.refresh();
            }

			Badges.validateLevelReached();
		}
		
//		if (subClass == HeroSubClass.WARLOCK) {
//
//			int value = Math.min( HT - HP, 1 + (Dungeon.depth - 1) / 5 );
//			if (value > 0) {
//				HP += value;
//				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
//			}
//
//			((Hunger)buff( Hunger.class )).satisfy( 10 );
//		}
	}
	
	public int maxExp() {
		return ( lvl + 4 ) * ( lvl + 3 ) / 2;
	}


//    void updateAwareness() {
//		awareness = (float)(1 - Math.pow(
//			(subClass == HeroSubClass.SNIPER ? 0.85 : 0.90),
//			(1 + Math.min( lvl,  9 )) * 0.5
//		));
//    }

	public boolean isStarving() {
		return (buff( Hunger.class )).isStarving();
	}

    public boolean isDualWielding() {
        return belongings.weap1 instanceof MeleeWeapon && belongings.weap2 instanceof MeleeWeapon;
    }
	
	@Override
	public boolean add( Buff buff ) {

		if (sprite != null) {
			if (buff instanceof Burning) {
				GLog.w("You catch fire!");
			} else if (buff instanceof Stun) {
				GLog.w("You are stunned!");
			} else if (buff instanceof Poison) {
				GLog.w("You are poisoned!");
			} else if (buff instanceof Ooze) {
                GLog.w( "Caustic ooze is eating your flesh!" );
            } else if (buff instanceof Ensnared) {
                GLog.w("You can't move!");
            } else if (buff instanceof Withered) {
				GLog.w( "You feel weakened!" );
//			} else if (buff instanceof Blindness) {
//				GLog.w( "You are blinded!" );
//			} else if (buff instanceof Enraged) {
//				GLog.w( "You become enraged!" );
//				sprite.showStatus(CharSprite.POSITIVE, "enraged");
			} else if (buff instanceof Charm) {
				GLog.w("You are charmed!");
			}  else if (buff instanceof Cripple) {
				GLog.w( "You are crippled!" );
			} else if (buff instanceof Bleeding) {
				GLog.w( "You are bleeding!" );
			} else if (buff instanceof Confusion) {
				GLog.w("Everything is spinning around you!");
//			} else if (buff instanceof Light) {
//                sprite.add(CharSprite.State.ILLUMINATED);
            }
		}

        interrupt();

        BuffIndicator.refreshHero();

        return super.add(buff);
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove(buff);

		BuffIndicator.refreshHero();
	}


    @Override
    public float awareness() {

        float result = super.awareness() * ringBuffsThirded(RingOfPerception.Perception.class);

//        if ( buff( MindVision.class ) != null ) {
//            result *= 1.5f;
//        }

        if (heroClass == HeroClass.SCHOLAR) {
            result *= 0.75f;
        } else if ( heroClass == HeroClass.ACOLYTE) {
            result *= 1.1f + 0.01f * (int)( ( lvl - 1 ) / 2 );
        } else {
            result *= 1.0f;
        }

        if( belongings.armor instanceof HuntressArmor && belongings.armor.bonus >= 0 ) {
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( restoreHealth ) {
            result *= 0.5f;
        }

        return result;
    }

	@Override
	public float stealth() {

		return baseStealth( true );

    }

    public float baseStealth( boolean identified ) {

        float result = super.stealth() * ringBuffsThirded(RingOfShadows.Shadows.class);

        if (heroClass == HeroClass.WARRIOR ) {
            result *= 0.75f;
        } else if ( heroClass == HeroClass.BRIGAND) {
            result *= 1.1f + 0.01f * (int)( ( lvl - 1 ) / 2 );
        }

        if( belongings.armor instanceof RogueArmor && belongings.armor.bonus >= 0 ) {
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( !restoreHealth ) {

            if( belongings.armor != null ) {
                result *= belongings.armor.penaltyFactor(this, identified || belongings.armor.isIdentified());
            }

            if( belongings.weap1 != null ) {
                result *= belongings.weap1.penaltyFactor(this, identified || belongings.weap1.isIdentified());
            }

            if( belongings.weap2 != null ) {
                result *= belongings.weap2.penaltyFactor(this, identified || belongings.weap2.isIdentified());
            }

        }

        return result;
    }

    public float magicPower() {

        float result = ringBuffsThirded(RingOfEnergy.Energy.class);

        if (heroClass == HeroClass.BRIGAND) {
            result *= 0.75f;
        } else if ( heroClass == HeroClass.SCHOLAR) {
            result *= 1.1f + 0.01f * (int)( ( lvl - 1 ) / 2 );
        } else {
            result *= 1.0f;
        }

        if( belongings.armor instanceof MageArmor && belongings.armor.bonus >= 0 ) {
            result *= ( 1.05f + 0.05f * belongings.armor.bonus );
        }

        if( restoreHealth ) {
            result *= 0.5f;
        }

        return result;
    }
	
	@Override
	public void die( Object cause, DamageType dmg ) {

		curAction = null;

//		DewVial.autoDrink( this );
//		if (isAlive()) {
//			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( sprite, 2f ) ;
//			return;
//		}

//		super.die( cause );
//        Actor.fixTime();

        boolean rezzed = false;
        BodyArmor armor = belongings.armor;
        Shield shield = belongings.weap2 instanceof Shield ? (Shield)belongings.weap2 : null ;

        if( armor != null && armor.glyph instanceof Revival && Armour.Glyph.procced(armor.bonus) && Random.Int(5) > 0 ) {

            if( armor.bonus >= 0 ) {

                armor.identify( Item.ENCHANT_KNOWN );
                Revival.resurrect(this);
                rezzed = true;

            }

        } else if( shield != null && shield.glyph instanceof Revival && Armour.Glyph.procced(shield.bonus) && Random.Int(5) > 0 ) {

            if( shield.bonus >= 0 ) {

                shield.identify( Item.ENCHANT_KNOWN );
                Revival.resurrect(this);
                rezzed = true;

            }

        } else {

            Ankh ankh = belongings.getItem(Ankh.class);

            if (ankh != null) {



//            } else {

//			Dungeon.deleteGame( Dungeon.hero.heroClass, false );
//			GameScene.show(new WndResurrect(ankh, cause));

                Ankh.resurrect(this);
                Statistics.ankhsUsed++;
                ankh.detach(Dungeon.hero.belongings.backpack);
                rezzed = true;

//            InterlevelScene.mode = InterlevelScene.Mode.RESURRECT ;


//            Game.switchScene( InterlevelScene.class );

            }
        }

        if( !rezzed ) {
            super.die(cause, dmg);
            reallyDie(cause, dmg);
        }
	}
	
	public static void reallyDie( Object cause, DamageType dmg ) {

        Camera.main.shake(4, 0.3f);
        GameScene.flash(0xBB0000);

        Dungeon.fail( ResultDescriptions.generateResult( cause, dmg ) );

        GLog.n( ResultDescriptions.generateMessage( cause, dmg ) );

		int length = Level.LENGTH;
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.TRAPPED) != 0) {
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
		for (Integer ofs : Level.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Level.passable[cell] || Level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );
		
		ArrayList<Item> items = new ArrayList<Item>( Dungeon.hero.belongings.backpack.items );
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}
			
			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}
		
		GameScene.gameOver();
		
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
//        Dungeon.level.press(pos, this);
//	}
	
	@Override
	public void onMotionComplete() {
		Dungeon.observe();
		search(false);
			
		super.onMotionComplete();
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target( enemy );

        currentWeapon = isDualWielding() ? Random.oneOf( belongings.weap1, (Weapon)belongings.weap2 ) :
            belongings.weap1 instanceof MeleeWeapon ? belongings.weap1 :
                belongings.weap2 instanceof MeleeWeapon ? (Weapon)belongings.weap2 :
                    null ;

		attack( enemy );
		curAction = null;
		
		Invisibility.dispel();

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {

        if (curAction instanceof HeroAction.Unlock) {

            if (theKey != null) {
                theKey.detach( belongings.backpack );
                theKey = null;
            }

            int doorCell = ((HeroAction.Unlock)curAction).dst;
            int door = Dungeon.level.map[doorCell];

            Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR_CLOSED : Terrain.UNLOCKED_EXIT );
            GameScene.updateMap( doorCell );

        } else if (curAction instanceof HeroAction.Examine) {

            int cell = ((HeroAction.Examine)curAction).dst;

            Bookshelf.examine( cell );

        } else if (curAction instanceof HeroAction.OpenChest) {
			
			if (theKey != null) {
				theKey.detach( belongings.backpack );
				theKey = null;
			}
			
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst ); 
//			if (heap.type == Type.BONES || heap.type == Type.BONES_CURSED) {
//				Sample.INSTANCE.play( Assets.SND_BONES );
//			}

			heap.open();
		}
		curAction = null;

		super.onOperateComplete();
	}
	
	public void search( boolean intentional ) {
		
		boolean smthFound = false;

        for (Integer ofs : Level.NEIGHBOURS8) {
            int p = pos + ofs;

            if (Dungeon.visible[p]) {

                if (intentional) {
                    sprite.parent.addToBack( new CheckedCell( p ) );
                }

                if ((Level.trapped[p] || Level.illusory[p]) && (intentional ||
                    Random.Float( 1.0f + 0.2f * Dungeon.chapter() ) < Random.Float( awareness() ) ) ) {

                    int oldValue = Dungeon.level.map[p];

                    GameScene.discoverTile( p, oldValue );

                    Level.set( p, Terrain.discover( oldValue ) );

                    GameScene.updateMap( p );

                    ScrollOfClairvoyance.discover(p);

                    smthFound = true;
                }
            }
        }
		
		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, TXT_SEARCH );
			sprite.operate( pos );
			spendAndNext( TIME_TO_SEARCH / ( 1.0f + awareness() ) );
		}
		
		if (smthFound) {
			GLog.w( TXT_NOTICED_SMTH );
			Sample.INSTANCE.play( Assets.SND_SECRET );
			interrupt();
		}

//		return smthFound;
	}

	public void resurrect( int resetLevel ) {
		
		HP = HT;
//		Dungeon.gold = 0;
		exp = exp;
		
//		belongings.resurrect(resetLevel);
		
		live();
        interrupt();
	}
	
//	@Override
//	public HashSet<Class<?>> resistances() {
//
//        HashSet<Class<?>> resistances = (HashSet<Class<?>>)super.resistances().clone();
//
//        float chance = Dungeon.hero.ringBuffs(RingOfProtection.Resistance.class);
//
//        if( chance >= 1.0f && Random.Float() < chance - 1.0f )
//            resistances.addAll( RingOfProtection.FULL );
//        else if( chance < 1.0f && Random.Float() < 1.0f - chance )
//            return RingOfProtection.EMPTY;
//
//        Shield b = buff( Shield.class );
//        if (b != null )
//            resistances.addAll( Shield.RESISTS );
//
//        return resistances;
//
////		RingOfElements.Resistance r = buff( RingOfElements.Resistance.class );
////		return r == null ? super.resistances() : r.resistances();
//	}
	
//	@Override



    public HashSet<Class<? extends DamageType>> resistances() {
        HashSet<Class<? extends DamageType>> resistances = new HashSet<>();

        float chance = Dungeon.hero.ringBuffs(RingOfProtection.Protection.class);

        if( chance >= 1.0f && Random.Float() < chance - 1.0f )
            resistances.addAll( RingOfProtection.RESISTS );
        else if( chance < 1.0f && Random.Float() < 1.0f - chance )
            return resistances();

        ForceField b = buff(ForceField.class);
        if ( b != null )
            resistances.addAll(ForceField.RESISTS);

        BodyArmor a = this.belongings.armor;
        if ( a != null && a.bonus >= 0 && a.glyph != null && a.glyph.resistance() != null )
            resistances.add(a.glyph.resistance());

        Shield s = this.belongings.weap2 instanceof Shield ? (Shield)this.belongings.weap2 : null ;
        if ( s != null && s.bonus >= 0 && s.glyph != null && s.glyph.resistance() != null )
            resistances.add(s.glyph.resistance());

        return resistances;
    }
	
	@Override
	public void next() {
		super.next();
	}
	

}
