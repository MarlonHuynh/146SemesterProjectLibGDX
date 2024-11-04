package io.github.dataevolutionclasses;


import com.badlogic.gdx.ApplicationAdapter; // Rendering
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // Util


public class Main extends ApplicationAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage vars
    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();      // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>(); // Master Card Storage (Do not change)
    private ArrayList<CardOnScreenData> cardOnScreenDatas;                              // Houses all information about all cards spots displayed on the screen
    private final ArrayList<Card> cardsInPlayerDeck = new ArrayList<>();                  // Cards in player's deck
    private final ArrayList<Card> cardsInPlayerHand = new ArrayList<>();                  // Cards in player's hand
    private final ArrayList<Card> cardsInEnemyDeck = new ArrayList<>();
    private final ArrayList<Card> cardsInEnemyHand = new ArrayList<>();
    // UI vars
    private SpriteBatch spriteBatch;
    private Sprite bgSpr;
    private BitmapFont debugFont, noncardUIFont;
    private String drawnStr = "You can draw a card";
    private final GlyphLayout drawnTextLayout = new GlyphLayout();
    private final Vector3 worldCoords = new Vector3();
    private final StringBuilder stringBuilder = new StringBuilder();
    private int selectedCardNumber = -1;            // Index of cardOnScreenDatas currently selected
    // Debug vars
    private int frameCounter = 0;

    private Stage stage;
    private Scroll scroll;


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
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
//        for (int i = 0; i < cardList.size(); i++){
//            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
//            nameToIntHashmap.put(cardList.get(i).getName(), i);
//        }
//        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
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

//        // Create the cards in the player's deck
//        List<String> strTemp = Arrays.asList(
//            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
//            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
//            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl" );
//        for (String s : strTemp) {
//            cardsInPlayerDeck.add(nameToCardHashmap.get(s));
//        }
//        for (int i = 0; i < 5; i++){ // Take random 5 cards from the player's deck to place in the player's hand and remove from deck
//            int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
//            cardsInPlayerHand.add(nameToCardHashmap.get(cardsInPlayerDeck.get(randomIndex).getName()));
//            cardsInPlayerDeck.remove(randomIndex);
//        }
//        //Enemy Deck
//        List<String> strTemp_e = Arrays.asList(
//            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
//            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
//            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl");
//        for (String s : strTemp_e) {
//            cardsInEnemyDeck.add(nameToCardHashmap.get(s));
//        }
//        for (int i = 0; i < 5; i++){
//            int randomIndex = (int) (Math.random() * cardsInEnemyDeck.size());
//            cardsInEnemyHand.add(nameToCardHashmap.get(cardsInEnemyDeck.get(randomIndex).getName()));
//            cardsInEnemyDeck.remove(randomIndex);
//        }
//        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
//        cardOnScreenDatas = new ArrayList<>();
        // Create all cards on screen
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
    }
    // Called every frame in render to draw the screen
    // Note: DO NOT MAKE NEW BATCHES OR VARIABLES EVERY FRAME THIS WILL TANK YOUR FPS VERY BADLY!!! 300fps -> 6fps
    public void draw(){
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

//        // Draw every card on the screen
//        for (CardOnScreenData CoSD : cardOnScreenDatas) {
//            drawCard(CoSD, spriteBatch);
//        }
//        // Draw Select Sprite if needed
//        if (selectedCardNumber != -1){
//            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
//        }
        spriteBatch.end();
        //camera.update();
    }
//    public void drawCard(CardOnScreenData CoSD, SpriteBatch batch){
////        // Draw cardback
////        CoSD.getCardbackSprite().draw(batch);
////        if (!CoSD.getCard().getName().equals("Draw") && !CoSD.getCard().getName().equals("Trash") && !CoSD.getCard().getName().equals("Blank") && !CoSD.getCard().getName().equals("End Turn")) {
////            // Draw creature
////            CoSD.getCardSprite().draw(batch);
////            // Draw text
////            CoSD.getNameFont().draw(batch, CoSD.getCard().getName(), CoSD.getNameX(), CoSD.getNameY());
////            CoSD.getNameFont().draw(batch, CoSD.getDescLayout(), CoSD.getDescX(), CoSD.getDescY());
////            // Draw cost
////            stringBuilder.setLength(0);
////            stringBuilder.append(CoSD.getCard().getCost());
////            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getCostTextX(), CoSD.getCostTextY());
////            // Draw attack
////            stringBuilder.setLength(0);
////            stringBuilder.append(CoSD.getCard().getAttack());
////            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getAttackTextX(), CoSD.getAttackTextY());
////            // Draw shield
////            stringBuilder.setLength(0);
////            stringBuilder.append(CoSD.getCard().getShield());
////            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getShieldTextX(), CoSD.getShieldTextY());
////        }
//
//
//    }
public void drawAll(){

    ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);
    worldCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);

//        stringBuilder.setLength(0);
//        stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
//        debugFont.draw(spriteBatch, stringBuilder, 520, 340);
//        // Draw cursor X, Y
//        stringBuilder.setLength(0);
//        stringBuilder.append("X: ").append((int)worldCoords.x);
//        debugFont.draw(spriteBatch, stringBuilder, 520, 380);
//        stringBuilder.setLength(0);
//        stringBuilder.append("Y: ").append((int)worldCoords.y);
//        debugFont.draw(spriteBatch, stringBuilder, 520, 360);

//        spriteBatch.setProjectionMatrix(camera.combined);
//        spriteBatch.begin();
//        bgSpr.draw(spriteBatch);




    // Clears screen and prepares batch for drawing
    for (int i = 0; i < cardList.size() && i < 35; i++) {
//            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//            SpriteBatch fpsBatch = new SpriteBatch();
//            fpsBatch.setProjectionMatrix(camera.combined);
//            fpsBatch.begin();
//            font.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
//            fpsBatch.end();
        if (i % 3 == 0){
            //drawCard(35, 50, 0.3f, cardList.get(i), camera);
            drawCard(45, ((35-i) / 3)*100 + 50 /*0/3 = 0*//*3/3 = 1*/, 0.3f, cardList.get(i), camera);
        } else if (i%3 == 1) {
            drawCard(110, ((35-i) / 3)*100 + 50 /*1/3 = 0*//*4/3 = 1*/, 0.3f, cardList.get(i), camera);
        } else if (i%3 == 2) {
            drawCard(175, ((35-i) / 3)*100 +50 /*2/3 = 0*//*5/3 = 1*/, 0.3f, cardList.get(i), camera);
        }
        //drawCard((i % 3)*50+35, (i/3)*100, 0.3f, cardList.get(i), camera);

    }
//        drawCard(viewport.getWorldWidth()*(3/4f), viewport.getWorldHeight()/2, 1f);
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
        draw();
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
        viewport.update(width, height);
        camera.position.set(300, 300, 0); // Recenter camera on resize
        camera.update();
    }


    // Called when exiting
    @Override
    public void dispose() {
        spriteBatch.dispose();
        debugFont.dispose();
        noncardUIFont.dispose();
        bgSpr.getTexture().dispose();
    }
}



