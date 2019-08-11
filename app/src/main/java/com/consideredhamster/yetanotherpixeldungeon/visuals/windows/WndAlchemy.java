package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.food.Food;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.EmptyBottle;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.Potion;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.levels.Terrain;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.DungeonTilemap;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RedButton;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class WndAlchemy extends Window{

    public static final int MODE_BREW		= 1;
    public static final int MODE_COOK       = 2;

    private static final float GAP		    = 2;
    private static final int WIDTH		    = 116;

    private ItemButton[] inputs;// = new WndBlacksmith.ItemButton[2];

    private RedButton btnCombine;

    private ItemSlot output;
    private ItemSlot baseItemSlot;

    private Item baseItem;

    private Emitter smokeEmitter;
    private Emitter bubbleEmitter;

    private int mode;

    private static final int BTN_SIZE	= 28;

    public WndAlchemy( int mode ){

        super();
        this.mode = mode;

        IconTitle titlebar = new IconTitle();
        titlebar.icon( DungeonTilemap.tile( Terrain.ALCHEMY ) );
        titlebar.label( "Alchemy Pot" );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        int h = 0;

        h += titlebar.height() + GAP;

        String hint = this.mode == MODE_BREW ?
                "Select two herbs to fill an empty bottle with potion." :
                "You may select a herb to cook with your piece of raw meat.";

        BitmapTextMultiline message = PixelScene.createMultiline( hint, 6 );

        message.maxWidth = WIDTH;
        message.measure();

        message.y = h;
        add( message );

        h += message.height() + GAP;

        int w = WIDTH;

        inputs = this.mode == MODE_BREW ?
            new ItemButton[2] :
            new ItemButton[1] ;

        int lastBtnIndex = inputs.length - 1;

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new ItemButton(){
                @Override
                protected void onClick() {

                    super.onClick();

                    if( item != null ){

                        if (!item.collect()){
                            Dungeon.level.drop(item, Dungeon.hero.pos);
                        }

                        item = null;

                        slot.clearIcon();
                        slot.item = null;

                        //slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                    }

                    GameScene.selectItem( itemSelector, WndBag.Mode.HERB, "select" );
                }
            };

            inputs[i].setRect(15, h, BTN_SIZE, BTN_SIZE);
            add(inputs[i]);
            h += BTN_SIZE + 2;
        }

        updateBaseItem();

        baseItemSlot = new ItemSlot( baseItem ){
            @Override
            protected void onClick() {
                super.onClick();
                if ( visible && item.name() != null ){
                    GameScene.show( new WndInfoItem( item ) );
                }
            }
            @Override
            public void item( Item item ) {
                super.item( item );
                topLeft.text( Integer.toString( item.quantity ) );
                topLeft.visible = true;
            }
        };

        baseItemSlot.setRect( 15, inputs[ lastBtnIndex ].bottom() + 2, BTN_SIZE, BTN_SIZE );

        ColorBlock baseItemBG = new ColorBlock( baseItemSlot.width(), baseItemSlot.height(), 0x9991938C );

        baseItemBG.x = baseItemSlot.left();
        baseItemBG.y = baseItemSlot.top();

        add(baseItemBG);
        add(baseItemSlot);

        h += BTN_SIZE + 2;

        Image arrow = Icons.get(Icons.RESUME);
        arrow.hardlight( 20, 20, 20 );
        arrow.x = (w - arrow.width)/2f;
        arrow.y = baseItemSlot.top() + (baseItemSlot.height() - arrow.height)/2f;
        //PixelScene.align(arrow);
        add(arrow);

        output = new ItemSlot(){
            @Override
            protected void onClick() {
                super.onClick();
                if ( visible && item.name() != null ){
                    GameScene.show( new WndInfoItem( item ) );
                }
            }
        };

        output.setRect(w - BTN_SIZE - 15, baseItemSlot.top(), BTN_SIZE, BTN_SIZE);

        ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
        outputBG.x = output.left();
        outputBG.y = output.top();
        add(outputBG);

        add(output);
        output.visible = false;

        bubbleEmitter = new Emitter();
        smokeEmitter = new Emitter();

        bubbleEmitter.pos(
            outputBG.x + ( BTN_SIZE - 16 ) / 2f,
            outputBG.y + ( BTN_SIZE - 16 ) / 2f,
        16, 16 );

        smokeEmitter.pos(
            bubbleEmitter.x, bubbleEmitter.y,
            bubbleEmitter.width, bubbleEmitter.height
        );

        bubbleEmitter.autoKill = false;
        smokeEmitter.autoKill = false;

        add(bubbleEmitter);
        add(smokeEmitter);

        h += 4;
        float btnWidth = ( w - 14 ) / 2f;

        btnCombine = new RedButton( this.mode == MODE_BREW ? "Brew" : "Cook" ){
            @Override
            protected void onClick() {
                super.onClick();
                combine();
            }
        };

        btnCombine.setRect(5, h, btnWidth, 18);
        //PixelScene.align(btnCombine);
        btnCombine.enable(false);
        add(btnCombine);

        RedButton btnCancel = new RedButton( "Cancel"){
            @Override
            protected void onClick() {
                super.onClick();
                onBackPressed();
            }
        };
        btnCancel.setRect(w - 5 - btnWidth, h, btnWidth, 18);
        //PixelScene.align(btnCancel);
        add(btnCancel);

        h += btnCancel.height();

        resize(w, h);

        updateState();

    }

    private void updateBaseItem(){

        if( this.mode == MODE_BREW ){

            baseItem = Dungeon.hero.belongings.getItem( EmptyBottle.class );

            if( baseItem == null ) {
                baseItem = new EmptyBottle().quantity( 0 );
            }

        } else {

            baseItem = Dungeon.hero.belongings.getItem( MeatRaw.class );
            if( baseItem == null ){
                baseItem = new MeatRaw().quantity( 0 );
            }

        }
    }

    protected WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect( Item item ) {

            if (item != null) {

                for ( ItemButton input : inputs ) {
                    if (input.item == null){
                        input.item(item.detach(Dungeon.hero.belongings.backpack));
                        break;
                    }
                }
            }
            updateState();
        }
    };

    private void updateState(){

        if( mode == MODE_BREW && baseItem.quantity() > 0 && filterInput( Herb.class).size() == 2 ){

            //potion creation requires two herbs, there are no alternatives (for now?)

            Potion potion = getBrewResult();

            if( potion != null ){

                potion.dud = true;

                output.item( potion );
                output.visible = true;

                btnCombine.enable( true );

            } else {

                btnCombine.enable(false);
                output.visible = false;

            }

        } else if( mode == MODE_COOK && baseItem.quantity() > 0 ){

            // we can cook meat without any herbs, getting a simple stewed meat in the process

            output.item( getCookResult());
            output.visible = true;

            btnCombine.enable(true);

        } else {

            btnCombine.enable(false);
            output.visible = false;

        }
    }

    private void combine(){

        ArrayList<Herb> herbs = filterInput(Herb.class);

        Item result = null;

        if ( mode == MODE_BREW && baseItem.quantity() > 0 && herbs.size() == 2 ){

            //potion creation
            result = getBrewResult();

            baseItem.detach(Dungeon.hero.belongings.backpack);
            baseItem = Dungeon.hero.belongings.getItem( EmptyBottle.class );

            if( baseItem == null ){
                baseItem = new EmptyBottle().quantity( 0 );
            }

            Statistics.potionsCooked++;
            Badges.validatePotionsCooked();

        } else if( mode == MODE_COOK && baseItem.quantity() > 0 ) {

            //meat cooking
            result = getCookResult();

            baseItem.detach(Dungeon.hero.belongings.backpack);
            baseItem = Dungeon.hero.belongings.getItem( MeatRaw.class );

            if( baseItem == null ){
                baseItem = new MeatRaw().quantity( 0 );
            }

        }

        if (result != null){

            result.identify();
            output.item( result );

            if (!result.collect()){
                Dungeon.level.drop(result, Dungeon.hero.pos);
            }

            for (int i = 0; i < inputs.length; i++){
                inputs[i].clear();
                inputs[i].item = null;
            }

            baseItemSlot.item(baseItem);

            if( mode == MODE_BREW || baseItem == null || baseItem.quantity == 0 ){
                btnCombine.enable( false );
            }

            bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.2f, 10 );
            smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
            Sample.INSTANCE.play( Assets.SND_PUFF );

        }
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].item != null){
                if (!inputs[i].item.collect()){
                    Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
                }
            }
        }
        super.onBackPressed();
    }

    private Food getCookResult(){

        Item item = inputs[0].item;

        try{
            if (item instanceof Herb){
                return ((Herb)item).cooking.newInstance();
            }
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        }

        return new MeatStewed();
    }

    private Potion getBrewResult() {

        // this algorithm is much simpler =P
        // (except for the ugly try/catch)

        Herb herb1 = (Herb)inputs[0].item;
        Herb herb2 = (Herb)inputs[1].item;

        try{

            if( herb1.getClass() == herb2.getClass() ) {
                return herb1.mainPotion.newInstance();
            }

            for( Class<? extends Potion> check : herb1.subPotions ){
                for( Class<? extends Potion> with : herb2.subPotions ){
                    if( check == with ){
                        return check.newInstance();
                    }
                }
            }

            return new UnstablePotion();

        } catch ( InstantiationException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        }

        return null;
    }

    private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
        ArrayList<T> filtered = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++){
            Item item = inputs[i].item;
            if (item != null && itemClass.isInstance(item)){
                filtered.add((T)item);
            }
        }
        return filtered;
    }
}
