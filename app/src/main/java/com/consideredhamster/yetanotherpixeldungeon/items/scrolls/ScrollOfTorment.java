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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Assets;
import com.consideredhamster.yetanotherpixeldungeon.DamageType;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Terror;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.utils.GLog;

public class ScrollOfTorment extends Scroll {

	{
		name = "Scroll of Torment";
        shortName = "To";

        spellSprite = SpellSprite.SCROLL_MASSHARM;
        spellColour = SpellSprite.COLOUR_DARK;
	}
	
	@Override
	protected void doRead() {

		int count = 0;

		Mob affected = null;

        int distance = 2 + curUser.magicSkill() / 5;

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {

			if (Level.fieldOfView[mob.pos] && Level.distance( curUser.pos, mob.pos ) <= distance ) {

                new Flare( 6, 32 ).color( SpellSprite.COLOUR_DARK, true ).show(mob.sprite, 2f);

                int baseHP = Math.max( 0, ( !Bestiary.isBoss(mob) ? mob.HP : mob.HP / 4 ) - 1 );

                int damage = Math.min( baseHP, Random.IntRange( baseHP * 4 / 5, baseHP ) );

                mob.damage( damage, curUser, DamageType.MIND );

//                if (!Bestiary.isBoss(mob) && Random.Int( mob.HP ) < damage ) {
//                    Terror buff = Buff.affect(mob, Terror.class, Terror.DURATION);
//
//                    if (buff != null) buff.object = curUser.id();
//                }

				affected = mob;
                count++;
            }
		}

        int baseHP = Math.max( 0, curUser.HP - ( Dungeon.difficulty == Difficulties.IMPOSSIBLE ? 2 : 1 ) );

        curUser.damage( ( Random.IntRange( baseHP * 2 / 5, baseHP / 2 ) ), curUser, DamageType.MIND);

        GameScene.flash(SpellSprite.COLOUR_DARK - 0x660000);
        Sample.INSTANCE.play(Assets.SND_FALLING);
        Camera.main.shake(4, 0.3f);

		switch (count) {
            case 0:
                GLog.i( "Suddenly your whole mind is engulfed in pure agony!" );
                break;
            case 1:
                GLog.i( "Suddenly your whole mind and the " + affected.name + " start writhing in agony!" );
                break;
            default:
                GLog.i( "Suddenly your whole mind and the creatures all around you start writhing in agony!" );
		}



        super.doRead();
	}
	
	@Override
	public String desc() {
		return
			"Upon reading this parchment, a flash of mind-tearing agony will overwhelm both its reader " +
            "and all the present creatures in the radius of the effect, bringing most of them to a brink " +
            "of death - but not any further. The user of this scroll is partially protected from its effect. " +
            "\n\nRadius of effect of this scroll depends on magic skill of the reader.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 95 * quantity : super.price();
	}
}
