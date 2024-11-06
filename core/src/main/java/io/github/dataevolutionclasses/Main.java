package io.github.dataevolutionclasses;




import com.badlogic.gdx.ApplicationAdapter; // Rendering
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx; // Input
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


import java.util.List; // Util




public class Main extends ApplicationAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage vars
    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private SpriteBatch spriteBatch;
    private Sprite bgSpr;
    private BitmapFont debugFont, noncardUIFont;
    private String drawnStr = "You can draw a card";
    private final GlyphLayout drawnTextLayout = new GlyphLayout();
    private final Vector3 worldCoords = new Vector3();
    private final StringBuilder stringBuilder = new StringBuilder();
    private int frameCounter = 0;
    private Stage stage;
    private Scroll scroll;
    private Stage uiStage; // New stage for UI elements
    private Skin skin; // Skin for styling UI elements




    // Instantiated upon startup
    @Override
    public void create() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        scroll = new Scroll();
        scroll.createSlider(stage); //Add the slider into the stage
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        camera.update();
        // Read and generate cards and place cards into cardList
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateFirst35CardsFromFile();
        cardList = reader.getCardList();
        // Initialize drawBatch
        spriteBatch = new SpriteBatch();
        // Initialize non-card Fonts and text
        debugFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        debugFont.getData().setScale(0.4f);
        noncardUIFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        noncardUIFont.getData().setScale(1.2f);
        drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
        // Initialize non-card sprites, with scale and position
        bgSpr = new Sprite(new Texture("background.png"));
        // Initialize UI Stage and Skin
        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Ensure you have a skin file


        // Create the menu and buttons
        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top().right(); // Align to the top-right corner


        TextButton sortByNameButton = new TextButton("Sort by Name", skin);
        TextButton sortByStageButton = new TextButton("Sort by Stage", skin);
        TextButton sortByCostButton = new TextButton("Sort by Cost", skin);
        TextButton sortByHealthButton = new TextButton("Sort by Health", skin);
        TextButton sortByAttackButton = new TextButton("Sort by Attack", skin);


        // Add button listeners (you can implement sorting later)
        // Inside create() method
        sortByNameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Name logic
                CardSorter.sortByName(cardList);
                System.out.println("Sorting by Name");
                drawAll();  // Redraw the cards after sorting
            }
        });


        sortByStageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Stage logic
                CardSorter.sortByStage(cardList);
                System.out.println("Sorting by Stage");
                drawAll();  // Redraw the cards after sorting
            }
        });


        sortByCostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Cost logic
                CardSorter.sortByCost(cardList);
                System.out.println("Sorting by Cost");
                drawAll();  // Redraw the cards after sorting
            }
        });


        sortByHealthButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Health logic
                // Assuming "getShield()" represents health for the sake of this example
                CardSorter.sortByShield(cardList);
                System.out.println("Sorting by Shield");
                drawAll();  // Redraw the cards after sorting
            }
        });


        sortByAttackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Attack logic
                CardSorter.sortByAttack(cardList);
                System.out.println("Sorting by Attack");
                drawAll();  // Redraw the cards after sorting
            }
        });


        // Add buttons to the table in a vertical layout
        menuTable.add(sortByNameButton).fillX().pad(10); // Pad for spacing
        menuTable.row(); // Move to the next row
        menuTable.add(sortByStageButton).fillX().pad(10);
        menuTable.row();
        menuTable.add(sortByCostButton).fillX().pad(10);
        menuTable.row();
        menuTable.add(sortByHealthButton).fillX().pad(10);
        menuTable.row();
        menuTable.add(sortByAttackButton).fillX().pad(10);
        // Add the table to the UI stage
        uiStage.addActor(menuTable);
    }


    public void drawAll(){


        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);
        // Display FPS counter and position of cursor
        worldCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        //camera.unproject(worldCoords);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Draw BG
        bgSpr.draw(spriteBatch);
        // Draw debug FPS
        stringBuilder.setLength(0);
        stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
        debugFont.draw(spriteBatch, stringBuilder, 520, 340);
        // Draw cursor X, Y
        stringBuilder.setLength(0);
        stringBuilder.append("X: ").append((int)worldCoords.x);
        debugFont.draw(spriteBatch, stringBuilder, 520, 380);
        stringBuilder.setLength(0);
        stringBuilder.append("Y: ").append((int)worldCoords.y);
        debugFont.draw(spriteBatch, stringBuilder, 520, 360);


        System.out.println("Card List Size: " + cardList.size());
        for (Card card : cardList) {
            System.out.println("Card Name: " + card.getName());
        }


        for (int i = 0; i < cardList.size() && i < 35; i++) {
            if (i % 3 == 0){
                drawCard(45, ((35-i) / 3)*100 + 50 /*0/3 = 0*//*3/3 = 1*/, 0.3f, cardList.get(i), camera);
            } else if (i%3 == 1) {
                drawCard(110, ((35-i) / 3)*100 + 50 /*1/3 = 0*//*4/3 = 1*/, 0.3f, cardList.get(i), camera);
            } else if (i%3 == 2) {
                drawCard(175, ((35-i) / 3)*100 +50 /*2/3 = 0*//*5/3 = 1*/, 0.3f, cardList.get(i), camera);
            }
        }
        spriteBatch.end();
    }
    public void drawCard(float x, float y, float scale, Card card, OrthographicCamera camera){
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        Sprite cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * scale, cardbackTexture.getHeight() * scale);
        // Initial card creature image
        Texture cardCreatureTexture = card.getTexture();
        Sprite cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getTexture().getWidth() * scale, cardCreatureSprite.getTexture().getHeight() * scale);
        // Set Font
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(scale * 0.5f);
        // Create batch of sprite to be drawn
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Find position of card
        // Note: DO NOT USE PIXELS to get your position. Use relative sizing.
        float cardX = (x - ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2)); // Bottom left corner x
        float cardY = (y - ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2)); // Bottom left corner y
        float midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2);  // Distance from the card's left edge to middle
        float midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the card's bottom edge to middle
        // ----- Images -----
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch); // Draw card back
        cardCreatureSprite.setPosition(cardX+(midcardX/3.5f), cardY+(midcardY/1.5f));
        cardCreatureSprite.draw(batch); // Draw card creature
        // ----- Text -----
        font.draw(batch, card.getName(), cardX+(midcardX*0.6f), cardY+(midcardY*1.82f)); // Draw name text
        GlyphLayout descLayout = new GlyphLayout();
        float descWidth = 100 * scale;
        descLayout.setText(font, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.draw(batch, Integer.toString(card.getCost()), cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, Integer.toString(card.getAttack()), cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, Integer.toString(card.getShield()), cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // End batch and update camera frame
        batch.end();
        camera.update();
    }
    public void adjustCamera() {
        float cardHeight = 110;  // Assume each card has a height of 150 units
        float totalHeight = cardHeight * 11;
        float visibleHeight = viewport.getWorldHeight();


        // Calculate the maximum scrollable range
        float maxScroll = totalHeight - visibleHeight;


        // Get the slider value and calculate the camera Y offset
        float sliderValue = scroll.getSliderValue();  // Value from 0 to 100
        float yOffset = (sliderValue / 100f) * maxScroll;


        // Adjust the camera's position
        camera.position.y = totalHeight - (visibleHeight / 2) - yOffset;
        camera.update();
    }
    // Called every refresh rate for rendering
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stage.act(); //Process any UI events
        adjustCamera();
        drawAll();
        stage.draw(); //Draw the stage (including the slider)
//        // Draw the UI stage for the menu
        uiStage.act();
        uiStage.draw(); // Draw the UI menu
        // Log memory every 100 frames
        frameCounter++;
        if (frameCounter >= 100) {
            Gdx.app.log("Memory", "Used: " + Gdx.app.getJavaHeap() + " bytes");
            frameCounter = 0; // Reset counter after logging
        }
    }




    // Called when resizing window
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); //Upadte the stage viewport
        viewport.update(width, height);
        camera.update();
    }




    // Called when exiting
    @Override
    public void dispose() {
        stage.dispose();
        scroll.dispose();
        spriteBatch.dispose();
        debugFont.dispose();
        noncardUIFont.dispose();
        bgSpr.getTexture().dispose();
    }
}







