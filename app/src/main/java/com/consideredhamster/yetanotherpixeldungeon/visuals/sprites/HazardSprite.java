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
package com.consideredhamster.yetanotherpixeldungeon.visuals.sprites;

import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.Hazard;
import com.watabou.utils.PointF;

public abstract class HazardSprite extends Image {

    private TextureFilm textures;
    public Hazard hazard;

	public HazardSprite() {
        super();

        if (textures == null){
            texture( asset() );
            textures = new TextureFilm( texture, 16, 16 );
        }
    }

    protected abstract String asset();
    public abstract int spritePriority();

    public void link( Hazard hazard ) {

        this.hazard = hazard;
        hazard.sprite = this;

        frame( textures.get( hazard.var ) );

        place( hazard.pos );
    }

    public void place( int cell ) {

        final int csize = DungeonTilemap.SIZE;

        point( new PointF(
            ((cell % Level.WIDTH) + 0.5f) * csize - width * 0.5f,
            ((cell / Level.WIDTH) + 0.5f) * csize - height * 0.5f
        ) );

        origin.set( width / 2, height / 2 );
    }

    public void changeFrame( int var ) {
        frame( textures.get( var ) );
    }
	
	@Override
	public void update() {
		super.update();
		
		visible = Dungeon.visible[ hazard.pos ];
	}

}
