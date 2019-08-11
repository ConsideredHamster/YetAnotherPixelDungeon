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
package com.consideredhamster.yetanotherpixeldungeon.actors;

import java.util.HashMap;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Banished;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Blinded;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Disrupted;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Exposed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Focus;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Guard;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfVitality;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Deflection;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.Ring;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Door;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public abstract class Char extends Actor {

//	protected static final String TXT_HIT		= "%s hit %s";
//	protected static final String TXT_KILL		= "You %s...";
	protected static final String TXT_DEFEAT	= "%s is defeated!";

	protected static final String TXT_DODGED	= "dodged";
	protected static final String TXT_MISSED	= "missed";

	protected static final String TXT_GUARD 	= "guard";
	protected static final String TXT_AMBUSH	= "sneak attack!";
	protected static final String TXT_COUNTER	= "counter attack!";
	protected static final String TXT_EXPOSED	= "exposed!";

//	private static final String TXT_YOU_MISSED	= "%s %s your attack";
//	private static final String TXT_SMB_MISSED	= "%s %s %s's attack";

    protected static final int VIEW_DISTANCE	= 8;

	public int pos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";
	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;

	public boolean morphed      = false;
	public boolean rooted		= false;
	public boolean flying		= false;
    public boolean moving		= false;

	public int invisible		= 0;

	private HashSet<Buff> buffs = new HashSet<Buff>();
	
	@Override
	protected boolean act() {

		Dungeon.level.updateFieldOfView( this );

        moving = false;

		return false;
	}

    public int viewDistance() {
        return buff( Blinded.class ) == null ? VIEW_DISTANCE : 1 ;
    };

    private static final String POS			= "pos";
	private static final String TAG_HP		= "HP";
	private static final String TAG_HT		= "HT";
	private static final String BUFFS		= "buffs";

    @Override
    public int actingPriority(){
        return 4;
    }

	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );

		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );

		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachOnLoad( this );
			}
		}
	}
	
	public boolean attack( Char enemy ){

        boolean visibleFight = Dungeon.visible[ pos ] || Dungeon.visible[ enemy.pos ];

        int damageRoll = damageRoll();

        Guard guarded = enemy.buff( Guard.class );

        if( guarded != null && !ignoresAC() && ( !isRanged() || enemy.blocksRanged() ) &&
            Random.Float() < enemy.guardChance() && guard( damageRoll, enemy.guardStrength() )
        ) {

            guarded.proc( enemy.blocksRanged() );

            attackProc( enemy, damageRoll, true );

            enemy.defenseProc( this, damageRoll, true );

            if( !isRanged() && Random.Float() < counterChance() ){

                Exposed exposed = Buff.affect( this, Exposed.class );

                if( exposed != null ) {
                    exposed.object = enemy.id();
                    exposed.reset(1);
                }

            }

            return true;

        } else if( hit( this, enemy, !isRanged() || ignoresDistancePenalty(), false ) ) {

            boolean blocked = !ignoresAC() && ( guarded != null || Random.Float() < enemy.guardChance() * 0.5f );

            damageRoll = enemy.defenseProc( this, damageRoll, false );

            if( !ignoresAC() ) {

                int dr = enemy.armorClass( blocked );

                damageRoll = absorb( damageRoll, dr, !( damageType() instanceof Element.Physical ) );

            }

            damageRoll = attackProc( enemy, damageRoll, false );

            enemy.damage( damageRoll, this, damageType());

            if( guarded != null ) guarded.reset( enemy.blocksRanged() );

//            Shocked buff1 = buff( Shocked.class );
//
//            if( buff1 != null )
//                buff1.discharge();
//
//            Shocked buff2 = enemy.buff( Shocked.class );
//
//            if( buff2 != null )
//                buff2.discharge();

            if (enemy == Dungeon.hero) {

                if (damageRoll >= enemy.HP) {
                    Camera.main.shake(GameMath.gate(1, damageRoll / (enemy.HT / 4), 5), 0.3f);
                    GameScene.flash(0x330000);
                }

                Dungeon.hero.interrupt( "You were awoken by an attack!" );
            }

            if (visibleFight) {
                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
                enemy.sprite.bloodBurstA(sprite.center(), damageRoll );
            }

            return true;

        } else {

            enemy.missed();

            return false;

        }
	}
	
	public static boolean hit( Char attacker, Char defender, boolean ranged, boolean magic ) {

        if( defender.buff( Guard.class ) != null )
            return true;

        if( defender.isExposedTo(attacker) )
            return true;

//        if( defender.isCharmedBy(attacker) )
//            return true;

        int attValue = ( magic ? attacker.magicPower() : attacker.accuracy() );

        if( Level.fieldOfView[ defender.pos ] )
            attValue *= 2;

        if( attacker.buff( Focus.class ) != null ){
            attValue *= 2;
        }

        if( ranged ) {

            int distance = Math.min( 9, Level.distance(attacker.pos, defender.pos) );

            if( distance > 1 ) {
                attValue = attValue * (9 - distance)/8;
            }
        }

        int defValue = defender.dexterity();
        int impassable = 16;

        for (int n : Level.NEIGHBOURS8) {
            if( Actor.findChar( defender.pos + n ) != null || Level.solid[defender.pos + n] || Level.chasm[defender.pos + n] && !defender.flying ) {
                impassable--;
            }
        }

        defValue = defValue * impassable / 16;

        int roll = Random.Int( attValue + defValue );

//        float buff = attacker.ringBuffsBaseZero( RingOfFortune.Fortune.class ) - defender.ringBuffsBaseZero( RingOfFortune.Fortune.class );
//
//        if( buff > 0.0f && Random.Float() < buff ) {
//            roll = Math.min( roll, Random.Int( attValue + defValue ) );
//        } else if( buff < 0.0f && Random.Float() < -buff ) {
//            roll = Math.max( roll, Random.Int( attValue + defValue ) );
//        }

		return attValue > roll;
	}

    public static int absorb( int damage, int armorClass ) {
        return absorb( damage, armorClass, false );
    }

    public static int absorb( int damage, int armorClass, boolean penetrate ) {
        return armorClass > 0 && damage > 0 ? damage * damage / ( damage + Random.Int(
                ( penetrate ? armorClass : armorClass * 2 ) + 1 ) ) : damage;
    }

//    public static boolean guard( int damage, int guardStrength, boolean penetrate ) {
//        return guardStrength > 0 && damage > 0 ? damage *  ) : damage;
//    }

    public static boolean guard( int damage, int guard ) {
        return damage < Random.Int( guard * 3 + 1 );
    }



    public void missed() {

        if ( sprite.visible ) {

            Sample.INSTANCE.play(Assets.SND_MISS);
            sprite.showStatus( CharSprite.NEUTRAL, dexterity() > 0 ? TXT_DODGED : TXT_MISSED );

        }

        if ( this == Dungeon.hero ) {
            Dungeon.hero.interrupt();
        }
    }

    protected float healthValueModifier() {
        return 1.0f;
    }

    final public int currentHealthValue() {
        return (int)( HP * healthValueModifier() );
    }

    final public int totalHealthValue() {
        return (int)( HT * healthValueModifier() );
    }

	public int accuracy() {
        return 0;
    }

	public int dexterity() {
		return 0;
	}

	public int magicPower() {
		return 0;
	}

    public float attackDelay() {
        return TICK / attackSpeed();
    }

    public float attackSpeed(){

        return ( buff( Enraged.class ) == null ? ( buff( Poisoned.class ) == null ? TICK : TICK * 0.75f ) : TICK );

    }


    public int damageRoll() {
        return 0;
    }

    public int armourAC() {
        return 0;
    }

    public int shieldAC() {
        return 0;
    }

    public int guardStrength() {
        return 0;
    }

    public int armorClass() {

        return armorClass( false );

    }

	public int armorClass( boolean withShield ) {

        if( morphed )
            return 0;

        float armourMod = 1.0f;

        if ( buff(Withered.class) != null ) {
            armourMod *= 0.5f;
        }

        if ( buff(Corrosion.class) != null ) {
            armourMod *= 0.5f;
        }

		return Math.round( armourAC() + ( withShield ? shieldAC() : 0 ) * armourMod );

	}

    public float guardChance() {

        float guardChance = 1.0f;

        if ( buff( Poisoned.class ) != null ) {
            guardChance *= 0.5f;
        }

        if ( buff( Vertigo.class ) != null ) {
            guardChance *= 0.5f;
        }

        if ( buff( Blinded.class ) != null ) {
            guardChance *= 0.5f;
        }

        if ( buff( Disrupted.class ) != null ) {
            guardChance *= 0.5f;
        }

        return guardChance;
    }

    public float counterChance() {
        return awareness() * 0.5f;
    }
	
	public int attackProc( Char enemy, int damage, boolean blocked ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage, boolean blocked ) {
		return damage;
	}

    public int STR() {
        return 0;
    }

	public Element damageType() {
		return Element.PHYSICAL;
	}

    public boolean ignoresAC() { return false; }

    public boolean blocksRanged() {
        return false;
    }

	public float moveSpeed() {
		return ( buff( Levitation.class ) == null ? ( buff( Crippled.class ) == null ? baseSpeed : baseSpeed * 0.5f ) : baseSpeed * 1.5f );
	}

    public float awareness() {
        return buff( Vertigo.class ) == null && buff( Blinded.class ) == null ? 1.0f : 0.5f ;
    }

    public float stealth() {
        return buff( Burning.class ) == null && buff( Ensnared.class ) == null ? 1.0f : 0.5f ;
    }

    public boolean isRanged() {
        return false;
    }

    public boolean ignoresDistancePenalty(){
        return false;
    }

    public boolean isMagical() {
        return false;
    }

    public boolean isHeavy() {
        return STR() > Dungeon.hero.STR();
    }



	public void heal( int value ) {

        if (HP <= 0 || value <= 0) {
            return;
        }

        if( buff( Withered.class ) != null ) {
            value = ( value / 2 + ( Random.Int( 2 ) < value % 2 ? 1 : 0 ) );
        }

        if( buff( RingOfVitality.Vitality.class ) != null ){
            value *= ringBuffsHalved( RingOfVitality.Vitality.class );
        }

        HP = Math.min( HP + value, HT );

        sprite.showStatus( CharSprite.POSITIVE, Integer.toString( value ) );

    }

	public void damage( int dmg, Object src, Element type ) {

		if (HP <= 0) {
			return;
		}

        if( this instanceof Hero ){
            if( Dungeon.difficulty == Difficulties.EASY ) {
                dmg -= ( dmg / 2 + ( Random.Int(2) < dmg % 2 ? 1 : 0 ) );
            } else if( Dungeon.difficulty == Difficulties.IMPOSSIBLE ) {
                dmg += ( dmg / 2 + ( Random.Int(2) < dmg % 2 ? 1 : 0 ) );
            }

            Dungeon.hero.interrupt( "You were awoken by an attack!" );
        }

        boolean amplified = false;
        int textColor = CharSprite.NEGATIVE;

        if( type != null ) {

            float resist = Element.Resist.getResistance( this, type );

            if( !Element.Resist.checkIfDefault( resist ) ) {

                if ( Element.Resist.checkIfNegated( resist ) ) {

                    dmg = 0;
                    textColor = CharSprite.NEUTRAL;

                } else if ( Element.Resist.checkIfPartial( resist ) ) {

                    dmg = dmg / 2 + Random.Int(dmg % 2 + 1);
                    textColor = CharSprite.WARNING;

                } else if ( Element.Resist.checkIfAmplified( resist ) ) {

                    dmg += ( dmg / 2 + Random.Int(dmg % 2 + 1) );
                    amplified = true;

                }

            }

            dmg = type.proc( this, dmg );
            //Damage type effect could have killed the target (like lighting), so return...
            if (HP <= 0) {
                return;
            }
        }

        if( type != null && !( type instanceof Element.Physical ) && src instanceof Char ) {
            if (src instanceof Hero) {

                Hero hero = (Hero) src;

                Armour armor = hero.currentArmour;

                if (armor != null && armor.glyph instanceof Deflection && armor.bonus < 0 && Armour.Glyph.procced(armor.bonus)) {
                    ((Char)src).damage(Random.IntRange(dmg, dmg * 2), null, type);
                }

            } else if ( this instanceof Hero ) {
                Hero hero = (Hero)this;

                Armour armor = hero.currentArmour;

                if (armor != null && armor.glyph instanceof Deflection && armor.bonus > 0 && Armour.Glyph.procced(armor.bonus)) {
                    ((Char)src).damage( Random.IntRange(dmg, dmg * 2), null, type);
                }
            }
        }

		sprite.showStatus( textColor, Integer.toString( dmg ) + ( amplified ? "!" : "" ) );

        sprite.flash();

        HP -= dmg;

//        if( this instanceof Mob && (Mob) && src == Dungeon.hero ) {
//            Buff.detach(this, Charmed.class);
//        }

		if ( !isAlive() ) {

			die(src, type);

		}
	}
	
	public void destroy() {
		HP = 0;

		Actor.remove( this );
        Actor.freeCell( pos );
    }

    public void die( Object src) {

        die(src, null);

    }

	public void die( Object src, Element dmg ) {
		destroy();

		sprite.die();
	}

    public boolean detected( Char ch ) {

        return ch.buff( Light.class) != null || Random.Float( ch.stealth() ) * ( !ch.flying ? Dungeon.level.stealthModifier( ch.pos ) : 1.5f )
                < Random.Float( awareness() * 2.0f ) / Math.sqrt( distance(ch) + 1 );

    }
	
	public boolean isAlive() {
		return HP > 0;
	}

    public boolean isDamagedOverTime() {
        for (Buff b : buffs) {
            if (b instanceof Burning
                || b instanceof Poisoned
                || b instanceof Corrosion
                || b instanceof Crippled
            ) {
                return true;
            }
        }
        return false;
    }

	@Override
	public void spend( float time ) {

		float timeScale = 1f;

		if (buff( Frozen.class ) != null) {
			timeScale *= 0.667f;
		}

//		if (buff( Speed.class ) != null) {
//			timeScale *= 1.5f;
//		}

		super.spend( time / timeScale );
	}
	
	public HashSet<Buff> buffs() {
		return buffs;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<T>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}


    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffs( Class<T> c ) {

        float bonus = 1.0f;

        for (Buff b : buffs) {
            if ( c.isInstance( b )) {
                bonus += ((Ring.RingBuff)b).effect();
            }
        }

        return bonus;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsHalved( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 1.0f + bonus ) / 2.0f;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsThirded( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 2.0f + bonus ) / 3.0f;

    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsBaseZero( Class<T> c ) {

        return ringBuffs( c ) - 1.0f;

    }

	@SuppressWarnings("unchecked")
	public <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

    public boolean isBlocking() {
        for (Buff b : buffs) {
            if (b instanceof Guard) {
                return true;
            }
        }
        return false;
    }

    public boolean isExposedTo( Char ch ) {
        int chID = ch.id();
        for (Buff b : buffs) {
            if (b instanceof Exposed && ((Exposed)b).object == chID) {
                return true;
            }
        }
        return false;
    }
	
	public boolean add( Buff buff ) {

		buffs.add(buff);
		Actor.add(buff);

        return true;
	}
	
	public void remove( Buff buff ) {
		
		buffs.remove(buff);
		Actor.remove(buff);

    }
	
	public void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs(buffClass)) {
			remove(buff);
		}
	}
	
	@Override
	protected void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[0])) {
			buff.detach();
		}
	}

	public void updateSpriteState() {
		for (Buff buff:buffs) {

            buff.applyVisual();

		}
	}

	public void move( int step ) {
		
		if (Level.adjacent( step, pos ) && Random.Int( 2 ) == 0 && ( ( buff( Vertigo.class ) != null ) ) ) {

			step = pos + Level.NEIGHBOURS8[Random.Int( 8 )];

			if ( Level.solid[step] || Actor.findChar( step ) != null ) {
				return;
			}
		}
		
		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

        Actor.moveToCell( this, step );
        pos = step;
		
		if (Dungeon.level.map[pos] == Terrain.DOOR_CLOSED) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.visible[pos];
		}

        Dungeon.level.press(pos, this);

        moving = true;
	}
	
	public int distance( Char other ) {
		return Level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		next();
	}
	
	public void onAttackComplete() {
		next();
	}

    public void onCastComplete() {
        next();
    }

    public void onComplete() {
        next();
    }
	
	public void onOperateComplete() {
		next();
	}

	public HashMap<Class<? extends Element>, Float> resistances() {
		return new HashMap<>();
	}



}
