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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import java.io.IOException;

import com.consideredhamster.yetanotherpixeldungeon.Difficulties;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.InterlevelScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.TitleScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;

public class WndGame extends Window {
	
	private static final String TXT_SETTINGS	= "Settings";
    private static final String TXT_JOURNAL		= "Journal";
	private static final String TXT_CHALLENGES  = "Challenges";
	private static final String TXT_RANKINGS	= "Rankings";
	private static final String TXT_TUTORIAL	= "Tutorial";
	private static final String TXT_START		= "Start New Game";
	private static final String TXT_MENU		= "Main Menu";
	private static final String TXT_EXIT		= "Exit Game";
	private static final String TXT_RETURN		= "Return to Game";


    private static final String TXT_VERSION   	= "Version: %s";
    private static final String TXT_DIFFICULTY 	= "Difficulty: %s";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	private int pos;
	
	public WndGame() {
		
		super();

		addButton(new RedButton(TXT_SETTINGS) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndSettings(true));
            }
        });

        addButton( new RedButton( TXT_JOURNAL ) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndCatalogus());
            }
        } );

        addButton( new RedButton( TXT_TUTORIAL ) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndTutorial());
            }
        } );
		
//		if (Dungeon.challenges > 0) {
//			addButton( new RedButton(TXT_CHALLENGES) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
//				}
//			} );
//		}
		
		if (!Dungeon.hero.isAlive()) {

			addButton(new RedButton(TXT_START) {

                {
                    icon = Icons.get(Dungeon.hero.heroClass);
                }

                @Override
                protected void onClick() {
                    Dungeon.hero = null;
                    YetAnotherPixelDungeon.challenges(Dungeon.challenges);
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.noStory = true;
                    Game.switchScene(InterlevelScene.class);
                }
            });
			
//			addButton( new RedButton( TXT_RANKINGS ) {
//				@Override
//				protected void onClick() {
//					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
//					Game.switchScene( RankingsScene.class );
//				}
//			} );
//		} else {



//            addButton(new RedButton(TXT_JOURNAL) {
//                @Override
//                protected void onClick() {
//                    hide();
//                    GameScene.show(new WndJournal());
//                }
//            });
        }
				
		addButtons(
                new RedButton(TXT_MENU) {
                    @Override
                    protected void onClick() {
                        try {
                            Dungeon.saveAll();
                        } catch (IOException e) {
                            // Do nothing
                        }
                        Game.switchScene(TitleScene.class);
                    }
                }, new RedButton(TXT_EXIT) {
                    @Override
                    protected void onClick() {
                        Game.instance.finish();
                    }
                }
        );
		
		addButton( new RedButton( TXT_RETURN ) {
			@Override
			protected void onClick() {
				hide();
			}
		} );


        BitmapTextMultiline showDifficulty = PixelScene.createMultiline(
                Utils.format(TXT_DIFFICULTY, Difficulties.NAMES[Dungeon.difficulty]), 6
        );

        showDifficulty.hardlight( 0xAAAAAA );
        showDifficulty.maxWidth = WIDTH - GAP * 2;
        showDifficulty.measure();
        showDifficulty.x = ( WIDTH - showDifficulty.width() ) / 2 + GAP;
        showDifficulty.y = pos + ( BTN_HEIGHT / 2 - showDifficulty.height() ) / 2 + GAP * 2;
        add(showDifficulty);

        BitmapTextMultiline showVersion = PixelScene.createMultiline(
                Utils.format( TXT_VERSION, Game.version ), 6
        );

        showVersion.hardlight( 0xAAAAAA );
        showVersion.maxWidth = WIDTH - GAP * 2;
        showVersion.measure();
        showVersion.x = ( WIDTH - showVersion.width() ) / 2 + GAP;
        showVersion.y = pos + ( BTN_HEIGHT - showVersion.height() / 2 ) / 2 + GAP * 2;
        add(showVersion);

		resize( WIDTH, pos + BTN_HEIGHT + GAP );
	}
	
	private void addButton( RedButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
	
	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
}
