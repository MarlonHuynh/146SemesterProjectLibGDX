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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;


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


    // Instantiated upon startup
    @Override
    public void create() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(300, 300, 0);  // Center at 600x600 middle
        camera.update();
        // Read and generate cards and place cards into cardList
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        for (int i = 0; i < cardList.size(); i++){
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
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

        // Create the cards in the player's deck
        List<String> strTemp = Arrays.asList(
            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl" );
        for (String s : strTemp) {
            cardsInPlayerDeck.add(nameToCardHashmap.get(s));
        }
        for (int i = 0; i < 5; i++){ // Take random 5 cards from the player's deck to place in the player's hand and remove from deck
            int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
            cardsInPlayerHand.add(nameToCardHashmap.get(cardsInPlayerDeck.get(randomIndex).getName()));
            cardsInPlayerDeck.remove(randomIndex);
        }
        //Enemy Deck
        List<String> strTemp_e = Arrays.asList(
            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl");
        for (String s : strTemp_e) {
            cardsInEnemyDeck.add(nameToCardHashmap.get(s));
        }
        for (int i = 0; i < 5; i++){
            int randomIndex = (int) (Math.random() * cardsInEnemyDeck.size());
            cardsInEnemyHand.add(nameToCardHashmap.get(cardsInEnemyDeck.get(randomIndex).getName()));
            cardsInEnemyDeck.remove(randomIndex);
        }
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<>();
        // Create all cards on screen
        // Enemy's hand (Index 0-4)
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        //  Player's hand (Index 5-9)
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        // Field top (enemy) (Index 10-12)
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        // Field bottom (player) (Index 13-15)
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        // Deck draw (Index 16), Trash (Index 17), and End Turn (Index 18)
        cardOnScreenDatas.add(new CardOnScreenData(35, viewport.getWorldWidth() * (1f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(36, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(38, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (4.75f / 16f), 0.4f));
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

        // Draw every card on the screen
        for (CardOnScreenData CoSD : cardOnScreenDatas) {
            drawCard(CoSD, spriteBatch);
        }
        // Draw Select Sprite if needed
        if (selectedCardNumber != -1){
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
        }
        spriteBatch.end();
        //camera.update();
    }
    public void drawCard(CardOnScreenData CoSD, SpriteBatch batch){
        // Draw cardback
        CoSD.getCardbackSprite().draw(batch);
        if (!CoSD.getCard().getName().equals("Draw") && !CoSD.getCard().getName().equals("Trash") && !CoSD.getCard().getName().equals("Blank") && !CoSD.getCard().getName().equals("End Turn")) {
            // Draw creature
            CoSD.getCardSprite().draw(batch);
            // Draw text
            CoSD.getNameFont().draw(batch, CoSD.getCard().getName(), CoSD.getNameX(), CoSD.getNameY());
            CoSD.getNameFont().draw(batch, CoSD.getDescLayout(), CoSD.getDescX(), CoSD.getDescY());
            // Draw cost
            stringBuilder.setLength(0);
            stringBuilder.append(CoSD.getCard().getCost());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getCostTextX(), CoSD.getCostTextY());
            // Draw attack
            stringBuilder.setLength(0);
            stringBuilder.append(CoSD.getCard().getAttack());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getAttackTextX(), CoSD.getAttackTextY());
            // Draw shield
            stringBuilder.setLength(0);
            stringBuilder.append(CoSD.getCard().getShield());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getShieldTextX(), CoSD.getShieldTextY());
        }
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



