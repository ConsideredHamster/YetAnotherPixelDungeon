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

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class ScrollPane extends Component {

    protected static final int INDICATOR_COLOR		= 0xFF7b8073;
    protected static final float INDICATOR_ALPHA	= 0.5f;
    protected static final float INDICATOR_SOLID	= 1.0f;

	protected TouchController controller;
	protected Component content;
    protected ColorBlock indicator;
	
	protected float minX;
	protected float minY;
	protected float maxX;
	protected float maxY;
	
	public ScrollPane( Component content ) {
		super();
		
		this.content = content;
		addToBack( content );
		
		width = content.width();
		height = content.height();
		
		content.camera = new Camera( 0, 0, 1, 1, PixelScene.defaultZoom );
		Camera.add( content.camera );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		Camera.remove( content.camera );
	}
	
	public void scrollTo( float x, float y ) {
		content.camera.scroll.set( x, y );
	}
	
	@Override
	protected void createChildren() {
		controller = new TouchController();
		add( controller );

        indicator = new ColorBlock( 1, 1, INDICATOR_COLOR );
        indicator.am = INDICATOR_ALPHA;
        add(indicator);
	}
	
	@Override
	protected void layout() {
		
		content.setPos( 0, 0 );
		controller.x = x;
		controller.y = y;
		controller.width = width;
		controller.height = height;
		
		Point p = camera().cameraToScreen( x, y );
		Camera cs = content.camera;
		cs.x = p.x;
		cs.y = p.y;
		cs.resize( (int)width, (int)height );

        indicator.visible = height < content.height();
        if (indicator.visible) {
            indicator.scale.set( 2, height * height / content.height() );
            indicator.x = right() - indicator.width();
            indicator.y = y;
        }
	}
	
	public Component content() {
		return content;
	}
	
	public void onClick( float x, float y ) {		
	}
	
	public class TouchController extends TouchArea {
		
		private float dragThreshold;
		
		public TouchController() {
			super( 0, 0, 0, 0 );
			dragThreshold = PixelScene.defaultZoom * 8;
		}
		
		@Override
		protected void onClick( Touch touch ) {
			if (dragging) {
				
				dragging = false;
                indicator.am = INDICATOR_ALPHA;
				
			} else {
				
				PointF p = content.camera.screenToCamera( (int)touch.current.x, (int)touch.current.y );
				ScrollPane.this.onClick( p.x, p.y );

			}
		}	
		
		private boolean dragging = false;
		private PointF lastPos = new PointF();
		
		@Override
		protected void onDrag( Touch t ) {		
			if (dragging) {
				
				Camera c = content.camera;
				
				c.scroll.offset( PointF.diff( lastPos, t.current ).invScale( c.zoom ) );
				if (c.scroll.x + width > content.width()) {
					c.scroll.x = content.width() - width;
				}
				if (c.scroll.x < 0) {
					c.scroll.x = 0;
				}
				if (c.scroll.y + height > content.height()) {
					c.scroll.y = content.height() - height;
				}
				if (c.scroll.y < 0) {
					c.scroll.y = 0;
				}

                indicator.y = y + height * c.scroll.y / content.height();
				
				lastPos.set( t.current );	
				
			} else if (PointF.distance( t.current, t.start ) > dragThreshold) {

                indicator.am = INDICATOR_SOLID;
                dragging = true;
                lastPos.set( t.current );

			}
		}	
	}
}
