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
package com.consideredhamster.yetanotherpixeldungeon.visuals.ui;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;

public class TagDanger extends Tag {

    private static final float ENABLED	= 1.0f;
    private static final float DISABLED	= 0.3f;
	
	public static final int COLOR	= 0xFF4C4C;
	
	private BitmapText number;
	private Image icon;
	
	private int enemyIndex = 0;
	
	private int lastNumber = -1;

    private boolean enabled = true;

    public TagDanger() {
		super( 0xFF4C4C );
		
		setSize( 24, 16 );
		
		visible = false;
	}

    private void enable( boolean value ) {
        enabled = value;

        if (icon != null) {
            icon.alpha( value ? ENABLED : DISABLED );
        }

        if (number != null) {
            number.alpha( value ? ENABLED : DISABLED );
        }
    }

    private void visible( boolean value ) {
        bg.visible = value;

        if (icon != null) {
            icon.visible = value;
        }

        if (number != null) {
            number.visible = value;
        }
    }
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		number = new BitmapText( PixelScene.font1x );
		add( number );
		
		icon = Icons.SKULL.get();
		add( icon );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = right() - 10;
		icon.y = y + (height - icon.height) / 2;
		
		placeNumber();
	}
	
	private void placeNumber() {
		number.x = right() - 11 - number.width();
		number.y = PixelScene.align( y + (height - number.baseLine()) / 2 );
	}
	
	@Override
	public void update() {
		
		if (Dungeon.hero.isAlive()) {
			int v =  Dungeon.hero.visibleEnemies();
			if (v != lastNumber) {
				lastNumber = v;
				if (visible = lastNumber > 0) {
					number.text( Integer.toString( lastNumber ) );
					number.measure();
					placeNumber();

					flash();
				}
			}

            if (!Dungeon.hero.ready) {
                enable( false );
            } else {
                enable( visible );
            }

        } else {
			enable( false );
            visible( false );
		}
		
		super.update();
	}
	
	@Override
	protected void onClick() {
		
		Mob target = next();

		if( target != null ) {
            Camera.main.target = null;
            Camera.main.focusOn(target.sprite);
        }
	}

    public Mob next() {

        Mob target = Dungeon.hero.visibleEnemy( enemyIndex++ );

        if( target != null ) {
            TagAttack.instance.target(target);
            HealthIndicator.instance.target(target);
        }

        return target;

    }
}
