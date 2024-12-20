/*
    Creates the gameplay screen of the game and all logic on what will be drawn on the gameplay screen, variables, and input handling.
    Basically, if it has to do with the gameplay, the code will be here.
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // Util

public class Gameplay extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage vars
    public List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();          // Master Card Storage (Do not change)
    private ArrayList<CardOnScreenData> cardOnScreenDatas;                              // Houses all information about all cards spots displayed on the screen
    private ArrayList<Card> cardsInPlayerDeck = new ArrayList<>();                // Cards in player's deck
    private final ArrayList<Card> cardsInPlayerHand = new ArrayList<>();                // Cards in player's hand
    private final Card[] cardsInPlayerField = new Card[3];                              // Cards in player's field
    private final ArrayList<Card> cardsInEnemyDeck = new ArrayList<>();                 // See above, with enemy.
    private final ArrayList<Card> cardsInEnemyHand = new ArrayList<>();
    private final Card[] cardsInEnemyField = new Card[3];
    // UI vars
    private SpriteBatch spriteBatch;
    private Sprite playerHealthSpr, enemyHealthSpr, playerCloudSpr, enemyCloudSpr, playerEnergySpr, enemyEnergySpr, bgSpr, loseSpr, winSpr, backSpr;
    private BitmapFont debugFont, noncardUIFont;
    private String drawnStr = "You can draw a card";
    private String enemyActionStr = "Last enemy action will be displayed here.";
    private final GlyphLayout drawnTextLayout = new GlyphLayout();
    private final GlyphLayout playerHealthLayout = new GlyphLayout();
    private final GlyphLayout enemyHealthLayout = new GlyphLayout();
    private final GlyphLayout enemyActionLayout = new GlyphLayout();
    private Vector3 worldCoords = new Vector3();
    private final StringBuilder stringBuilder = new StringBuilder();
    // Stats vars
    private int playerHealth, enemyHealth, playerRecharge, enemyRecharge, playerEnergy, enemyEnergy;
    // Game State vars
    private boolean drawnBool = false;              // Keeps track of whether player has drawn or not yet
    private boolean discardBool = false;            // Keeps track of whether player has discarded or not yet
    private boolean drawnEnemyBool = false;         // See above, with enemy
    private final boolean discardEnemyBool = false;
    private int turnCount = 0;                      // Turn #
    private int selectedCardNumber = -1;            // Index of cardOnScreenDatas currently selected
    private int prevSelectedCardNumber = -1;        // Index of cardOnScreenDatas previously selected
    private boolean isEnemyTurn = false;            // Prevents input from being processed when it's the enemy's turn
    private boolean winLoseActive = false;          // Determines whether a win or lost state has occurred yet
    private boolean win = false;                    // Determines whether the game end is a win or loss
    // Helper vars
    private int randomIndex;
    private boolean clicked;
    private Card cardToAdd;
    // Debug vars
    private int frameCounter = 0;
    // Sound effect vars
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));
    private Music gameBackMusic = Gdx.audio.newMusic(Gdx.files.internal("walking-beat-SUNRIZISH.mp3"));
    private Sound cardPlaceSound = Gdx.audio.newSound(Gdx.files.internal("PlaceCardSound.mp3"));

    // Define the scene
    private Game game;
    public Gameplay(Game game) {
        this.game = game;
    }

    // Instantiated upon startup
    // Sets up all the gameplay variable and initial position of the UI
    @Override
    public void show() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(300, 300, 0);  // Center at 600x600 middle
        camera.update();
        createInputProcessor();
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
        debugFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt")); debugFont.getData().setScale(0.4f);
        noncardUIFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt")); noncardUIFont.getData().setScale(1.2f);
        drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
        enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);
        // Initialize non-card sprites, with scale and position
        bgSpr = new Sprite(new Texture("background.png"));
        playerHealthSpr = new Sprite(new Texture("yourhealth.png")); playerHealthSpr.setScale(0.65f); playerHealthSpr.setPosition(-35, 180);
        enemyHealthSpr = new Sprite(new Texture("enemyhealth.png")); enemyHealthSpr.setScale(0.65f); enemyHealthSpr.setPosition(-35, 270);
        playerCloudSpr = new Sprite(new Texture("playercloud.png")); playerCloudSpr.setScale(0.5f); playerCloudSpr.setPosition(380, 180);
        enemyCloudSpr = new Sprite(new Texture("enemycloud.png")); enemyCloudSpr.setScale(0.5f); enemyCloudSpr.setPosition(380, 340);
        playerEnergySpr = new Sprite(new Texture("playerenergy.png")); playerEnergySpr.setScale(0.5f); playerEnergySpr.setPosition(380, 110);
        enemyEnergySpr = new Sprite(new Texture("enemyenergy.png")); enemyEnergySpr.setScale(0.5f); enemyEnergySpr.setPosition(380, 270);
        loseSpr = new Sprite(new Texture("youlose.png")); loseSpr.setPosition(50, 200);
        winSpr = new Sprite(new Texture("youwin.png")); winSpr.setPosition(50, 200);
        backSpr = new Sprite(new Texture("btn_back.png"));
        backSpr.setScale(0.35f);
        backSpr.setPosition(-60, 550);
        gameBackMusic.setVolume(0.4f);
        gameBackMusic.setLooping(true);
        gameBackMusic.play();
        // Initialize stat variables
        playerHealth = 60; enemyHealth = 40; playerEnergy = 0; playerRecharge = 0; enemyEnergy = 0; enemyRecharge = 0;
        // Create the cards in the player's deck
        if (Library.staticCardInDeck != null && Library.staticCardInDeck.size()>=40){
            cardsInPlayerDeck = Library.staticCardInDeck;
        }
        else{
            List<String> strTemp = Arrays.asList(
                "Bubble Sort", "Bubble Sort", "Seelection", "Seelection", "Eelnsertion Sort", "Eelnsertion Sort",
                "Bubble Sort", "Bubble Sort", "Seelection", "Seelection", "Eelnsertion Sort", "Eelnsertion Sort",
                "Surgeon Sort", "Surgeon Sort", "Shell Sort", "Shell Sort", "Quickfish Sort", "Quickfish Sort",
                "A-Starfish", "Bucket O' Fish", "Raydix Sort",
                "Parraykeet", "Parraykeet", "Sphinx List", "Sphinx List", "Bin. Canary Tree", "Bin. Canary Tree",
                "Parraykeet", "Parraykeet", "Sphinx List", "Sphinx List", "Bin. Canary Tree", "Bin. Canary Tree",
                "Quack Stack", "Quack Stack", "Hawkmap", "Hawkmap", "Quetzelqueueotl", "Quetzelqueueotl",
                "Grifminmax Heap", "Hippograph", "Bal. Canary Tree",
                "Recover Data", "Recover Data", "Recover Data", "Backup Data", "Backup Data", "Backup Data"
            );
            for (String s : strTemp) {
                Card card = nameToCardHashmap.get(s);
                if (card != null)
                    cardsInPlayerDeck.add(card);
                else
                    System.out.println("Null card: " + s);
            }
        }
        // Generate hand by taking 5 cards from deck
        for (int i = 0; i < 5; i++){
            int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
            cardsInPlayerHand.add(nameToCardHashmap.get((cardsInPlayerDeck.get(randomIndex).getName())).deepCopy());
            cardsInPlayerDeck.remove(randomIndex);
        }
        // Enemy Deck
        List<String> strTemp_e = Arrays.asList(
            "Bitbug", "Bitbug", "Stringer Bee", "Stringer Bee", "Int Ant", "Int Ant",
            "Bitbug", "Bitbug", "Stringer Bee", "Stringer Bee", "Int Ant", "Int Ant",
            "Beetlean", "Beetlean", "Pointerpede", "Pointerpede", "Bytebug", "Bytebug",
            "Tupletick", "Tupletick", "Wordbug", "Wordbug", "Referant", "Referant",
            "Parraykeet", "Parraykeet", "Sphinx List", "Sphinx List", "Bin. Canary Tree", "Bin. Canary Tree",
            "Parraykeet", "Parraykeet", "Sphinx List", "Sphinx List", "Bin. Canary Tree", "Bin. Canary Tree",
            "Quack Stack", "Quack Stack", "Hawkmap", "Hawkmap", "Quetzelqueueotl", "Quetzelqueueotl",
            "Grifminmax Heap", "Grifminmax Heap", "Hippograph", "Hippograph", "Bal. Canary Tree", "Bal. Canary Tree"
            );
        for (String s : strTemp_e) {
            Card card = nameToCardHashmap.get(s);
            if (card != null)
                cardsInEnemyDeck.add(card);
            else
                System.out.println("Null card: " + s);
        }
        // Generate hand by taking 5 cards from deck
        for (int i = 0; i < 5; i++){
            randomIndex = (int) (Math.random() * cardsInEnemyDeck.size());
            cardsInEnemyHand.add((nameToCardHashmap.get(cardsInEnemyDeck.get(randomIndex).getName())).deepCopy());
            cardsInEnemyDeck.remove(randomIndex);
        }
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<>();

        Card emptyCard = cardList.get(37);
        Card drawCard = cardList.get(35);
        Card trashCard = cardList.get(36);
        Card endCard = cardList.get(38);
        // Create all cards on screen
        // Enemy's hand (Index 0-4)
        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        //  Player's hand (Index 5-9)
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        // Field top (enemy) (Index 10-12)
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        // Field bottom (player) (Index 13-15)
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(emptyCard, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        // Deck draw (Index 16), Trash (Index 17), and End Turn (Index 18)
        cardOnScreenDatas.add(new CardOnScreenData(drawCard, viewport.getWorldWidth() * (1f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(trashCard, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(endCard, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (4.75f / 16f), 0.4f));
    }

    // Called every refresh rate for rendering
    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
        // Log memory every 100 frames
        /*
        frameCounter++;
        if (frameCounter >= 100) {
            Gdx.app.log("Memory", "Used: " + Gdx.app.getJavaHeap() + " bytes");
            frameCounter = 0; // Reset counter after logging
        }*/
    }

    // Called when resizing window
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(300, 300, 0); // Recenter camera on resize
        camera.update();
    }

    // Called when exiting
    // Disposes variables not in use
    @Override
    public void dispose() {
        spriteBatch.dispose();
        debugFont.dispose();
        noncardUIFont.dispose();
        bgSpr.getTexture().dispose();
        playerHealthSpr.getTexture().dispose();
        enemyHealthSpr.getTexture().dispose();
        playerCloudSpr.getTexture().dispose();
        enemyCloudSpr.getTexture().dispose();
        playerEnergySpr.getTexture().dispose();
        enemyEnergySpr.getTexture().dispose();
        gameBackMusic.dispose();
        cardPlaceSound.dispose();
    }

    // Called every frame in render to draw the screen
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
        stringBuilder.setLength(0); stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
        debugFont.draw(spriteBatch, stringBuilder, 520, 340);
        // Draw cursor X, Y
        stringBuilder.setLength(0); stringBuilder.append("X: ").append((int)worldCoords.x);
        debugFont.draw(spriteBatch, stringBuilder, 520, 380);
        stringBuilder.setLength(0); stringBuilder.append("Y: ").append((int)worldCoords.y);
        debugFont.draw(spriteBatch, stringBuilder, 520, 360);
        // Draw non-card UI sprites
        playerHealthSpr.draw(spriteBatch);
        enemyHealthSpr.draw(spriteBatch);
        playerEnergySpr.draw(spriteBatch);
        enemyEnergySpr.draw(spriteBatch);
        playerCloudSpr.draw(spriteBatch);
        enemyCloudSpr.draw(spriteBatch);
        backSpr.draw(spriteBatch);
        // Draw non-card text UI
        debugFont.draw(spriteBatch, drawnTextLayout, 5, 200);
        stringBuilder.setLength(0); stringBuilder.append("Cards Left: ");
        stringBuilder.append(cardsInPlayerDeck.size());
        debugFont.draw(spriteBatch, stringBuilder, 5, 165);
        stringBuilder.setLength(0); stringBuilder.append("Turn ");
        stringBuilder.append(turnCount);
        debugFont.draw(spriteBatch, stringBuilder, 540, 250);
        // Draw healths
        stringBuilder.setLength(0); stringBuilder.append(playerHealth);
        playerHealthLayout.setText(noncardUIFont, stringBuilder, Color.BLACK, 100, Align.center, true);
        noncardUIFont.draw(spriteBatch, playerHealthLayout, 2, 270);
        stringBuilder.setLength(0); stringBuilder.append(enemyHealth);
        enemyHealthLayout.setText(noncardUIFont, stringBuilder, Color.BLACK, 100, Align.center, true);
        noncardUIFont.draw(spriteBatch, enemyHealthLayout, 2, 360);
        // Draw player energy and recharge
        stringBuilder.setLength(0); stringBuilder.append(playerRecharge);
        noncardUIFont.draw(spriteBatch, stringBuilder, 450, 265);
        stringBuilder.setLength(0); stringBuilder.append(playerEnergy);
        noncardUIFont.draw(spriteBatch, stringBuilder, 450, 200);
        // Draw enemy energy and recharge
        stringBuilder.setLength(0); stringBuilder.append(enemyRecharge);
        noncardUIFont.draw(spriteBatch, stringBuilder, 450, 425);
        stringBuilder.setLength(0); stringBuilder.append(enemyEnergy);
        noncardUIFont.draw(spriteBatch, stringBuilder, 450, 360);
        // Draw every card on the screen
        for (CardOnScreenData CoSD : cardOnScreenDatas)
            CoSD.drawCard(CoSD, spriteBatch, stringBuilder);
        // Draw Select Sprite if needed
        if (selectedCardNumber != -1)
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
        debugFont.draw(spriteBatch, enemyActionLayout, 10, 440);
        if (winLoseActive){
            if (win){
                winSpr.draw(spriteBatch);
            }
            else
                loseSpr.draw(spriteBatch);
        }
        spriteBatch.end();
        //camera.update();
    }


    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // -----------------------------------------------------------------------------------------------
            // ------------------------------ Checks which UI part was clicked ------------------------------
            // -----------------------------------------------------------------------------------------------
            clicked = false;
            // Convert screen coordinates to world coordinates
            worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
            // Ignore input if outside the 600x600 game area or if its the enemy turn
            if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600 || isEnemyTurn)
                return false;
            for (int i = 0; i < cardOnScreenDatas.size(); i++) {
                if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    // Update selection variables accordingly to what was clicked and what was previously clicked
                    cardPlaceSound.play();
                    if (selectedCardNumber == -1) {
                        selectedCardNumber = i;
                        prevSelectedCardNumber = 0; // Initialized to 0 (enemy hand 1) at first to avoid null exceptions when retrieving card data. Doesn't affect any playerside logic.
                        clicked = true;
                        break;
                    }
                    else {
                        cardPlaceSound.play();
                        prevSelectedCardNumber = selectedCardNumber;
                        selectedCardNumber = i;
                        clicked = true;
                        break;
                    }
                }
            }
//            if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
//                buttonSound.play(0.5f);
//                game.setScreen(new Title(game));
//                dispose();
//            }
            clicked = false;
            // Get World Coords
            worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
            if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                return false;
            // Reduce transparency if button clicked
            if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                clicked = true;
                backSpr.setColor(1, 1, 1, 0.8f);
            }

            // If nothing has been clicked and nothing has been clicked, no additional logic needed so returns
            if (prevSelectedCardNumber == -1 && selectedCardNumber == -1 )
                return clicked;
            // -----------------------------------------------------------------------------------------------
            // ---------- Perform actions based on what was clicked and what was previously clicked ----------
            // -----------------------------------------------------------------------------------------------
            CardOnScreenData currData = cardOnScreenDatas.get(selectedCardNumber);
            CardOnScreenData prevData = cardOnScreenDatas.get(prevSelectedCardNumber);
            // 0) Spell Logic for single-use double-clicked spells (add player hp)
            if (currData.getCard().getType().equals("Spell") && prevData.getCard().getType().equals("Spell") && playerEnergy >= currData.getCard().getCost()){
                // Subtract cost
                playerEnergy -= currData.getCard().getCost();
                // Add 5 hp
                switch (currData.getCard().getName()) {
                    case "Recover Data":
                        playerHealth += 5;
                        break;
                    case "Backup Data":
                        playerHealth += 10;
                        break;
                    default:
                        break;
                }
                // Remove from hand
                for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                    if (cardsInPlayerHand.get(i).getName().equals(currData.getCard().getName())) {
                        cardsInPlayerHand.remove(i);
                        break;
                    }
                }
                // Remake the card's UI to be reflective of usage
                currData.remakeCard(37, currData.getX(), currData.getY(), currData.getScale());
            }
            else if (prevData.getCard().getType().equals("Spell")                    // Current select is blank card
                && !currData.getCard().getName().equals("Blank")
                && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
                && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
                && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
                // Subtract cost
                playerEnergy -= prevData.getCard().getCost();
                switch (prevData.getCard().getName()) {
                    case "Overclock":
                        currData.getCard().setAttack(currData.getCard().getAttack() + 2);
                        break;
                    case "Superoverclock":
                        currData.getCard().setAttack(currData.getCard().getAttack() + 4);
                        break;
                    case "Allocate memory":
                        currData.getCard().setShield(currData.getCard().getShield() + 2);
                        break;
                    case "Allocate more memory":
                        currData.getCard().setShield(currData.getCard().getShield() + 4);
                        break;
                    default:
                        break;
                }
                // Remake the card's UI to be reflective of the swap
                prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
            }
            // 1) Fielding Card logic (prev -> card in hand, curr -> player field blank)
            else if (!prevData.getCard().getName().equals("Blank")                       // CONDITIONS: Previous select is not blank card
            && !prevData.getCard().getType().equals("Spell")
            && !prevData.getCard().getName().equals("Trash")
            && !prevData.getCard().getName().equals("End Turn")
            && !prevData.getCard().getName().equals("Discard")
            && currData.getCard().getName().equals("Blank")                     // Current select is blank card
            && !prevData.getCard().getType().equals("Spell")
            && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
            && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
            && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
                // Subtract cost from energy if applicable
                if (playerEnergy >= prevData.getCard().getCost())
                    playerEnergy -= prevData.getCard().getCost();
                // Add card to field array and remove from hand array
                cardsInPlayerField[selectedCardNumber - 13] = prevData.getCard();
                for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                    if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
                        cardsInPlayerHand.remove(i);
                        break;
                    }
                }
                // Halves attack if puts down an higher stage card
                if (prevData.getCard().getStage() != 1) {
                    prevData.getCard().setAttack(prevData.getCard().getAttack() / 2);
                }
                // Remake the card's UI to be reflective of the swap
                currData.remakeCard(prevData.getCard(), currData.getX(), currData.getY(), currData.getScale());
                prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
            }
            // 1.1) Fielding card logic for EVOLUTION
            else if (!prevData.getCard().getName().equals("Blank")                       // CONDITIONS: Previous select is not blank card
            && !prevData.getCard().getName().equals("Trash")
            && !prevData.getCard().getName().equals("End Turn")
            && !prevData.getCard().getName().equals("Discard")
            && !currData.getCard().getName().equals("Blank")                     // Current select is not blank
            && (prevData.getCard().getStage() == (currData.getCard().getStage()+1)) // New card is 1 stage higher than previous card
            && (prevData.getCard().getType().equals(currData.getCard().getType()))  // New card is same type as previous card
            && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
            && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
            && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
                // Subtract cost from energy if applicable
                if (playerEnergy >= prevData.getCard().getCost()) {
                    playerEnergy -= prevData.getCard().getCost();
                }
                // Add card to field array and remove from hand array
                cardsInPlayerField[selectedCardNumber - 13] = prevData.getCard();
                for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                    if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
                        cardsInPlayerHand.remove(i);
                        break;
                    }
                }
                // Add prevolution attack to current card's attack
                prevData.getCard().setAttack(prevData.getCard().getAttack() + currData.getCard().getAttack());
                prevData.getCard().setShield(prevData.getCard().getShield() + currData.getCard().getShield());
                // Remake the card's UI to be reflective of the swap
                currData.remakeCard(prevData.getCard(), currData.getX(), currData.getY(), currData.getScale());
                prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
            }
            // 2) Trash Card logic (prev -> card in hand, curr -> trash)
            else if (prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9 // CONDITIONS: Previous select is in player hand
            && !prevData.getCard().getName().equals("Blank")                // Previous select is not blank card
            && currData.getCard().getName().equals("Trash")                 // Current select is in trash
            && !discardBool){                                       // Have not discarded this turn yet
                // Remove from hand
                for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                    if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
                        cardsInPlayerHand.remove(i);
                        break;
                    }
                }
                // Remake Card UI (set to blank)
                prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale());
                // Change energy vars
                playerEnergy++;
                playerRecharge++;
                // Update discard bool
                discardBool = true;
            }
            // 3) Draw Card logic (current -> draw)
            else if (currData.getCard().getName().equals("Draw")    // CONDITIONS: Current select is trash
            && cardsInPlayerHand.size() < 5                     // Less than 5 cards in hand
            && !cardsInPlayerDeck.isEmpty()                     // Deck size is greater than 0
            && !drawnBool) {                                    // Player hasnt drawn this turn yet
                // Add card at random index to hand and remove card from cardsInPlayerDeck
                randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
                cardToAdd = cardsInPlayerDeck.get(randomIndex).deepCopy();
                cardsInPlayerHand.add(cardToAdd); // Add card at randomIndex into hand
                cardsInPlayerDeck.remove(randomIndex);
                // Update drawnBool (drawn for the turn)
                drawnBool = true;
                drawnStr = "You already drawn this turn.";
                drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
                // Change card left text
                // Update Card UI
                for (int i = 5; i <= 9; i++) {
                    if (cardOnScreenDatas.get(i).getCardID() == 37) { // Blank
                        CardOnScreenData CoSD = cardOnScreenDatas.get(i);
                        cardOnScreenDatas.get(i).remakeCard(cardToAdd, CoSD.getX(), CoSD.getY(), CoSD.getScale());
                        break;
                    }
                }
            }
            // 4) End turn logic (current -> End turn)
            else if (currData.getCard().getName().equals(("End Turn"))) {
                // Attack Enemy
                processCardInteraction(0, 13, 10);
                processCardInteraction(0, 14, 11);
                processCardInteraction(0, 15, 12);
                // Reset drawn bools
                if (cardsInPlayerDeck.isEmpty()){
                    drawnStr = "No more cards left.";
                }
                else {
                    drawnBool = false;
                    drawnStr = "You can draw a card.";
                }
                drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
                // Process the enemy turn logic
                processEnemyTurnTimed();
                // Reset drawn status and energy, increment turn count
                playerEnergy = playerRecharge;
                discardBool = false;
                drawnEnemyBool = false;
                enemyEnergy = enemyRecharge;
                turnCount++;
            }
            return clicked;
            }

            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // Reset Btn Color
                backSpr.setColor(1, 1, 1, 1f);
                // Get World Coords
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));  // Convert screen coordinates to world coordinates
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600) // Check if the click is within the viewport
                    return false;
                // If back button is clicked, play sound and return to the Title screen
                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    buttonSound.play();
                    game.setScreen(new Title(game));
                    dispose();
                }
                return clicked;
            }
        });
    }

    // This method is used to simulate enemy actions
    public void processEnemyTurnTimed() {
        // Task 1: Draw a card if able at the start of each turn
        Gdx.app.postRunnable(() -> {
            isEnemyTurn = true;
            drawCardEnemy();
            enemyActionStr = "Attempting card drawing.";
            enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);

            // Task 2: Discard cards if necessary
            delayAndExecute(() -> {
                discardCardEnemy();
                enemyActionStr = "Attempting discard.";
                enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);

                // Task 3: Draw another card if able
                delayAndExecute(() -> {
                    drawCardEnemy();
                    enemyActionStr = "Attempting card drawing.";
                    enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);

                    // Task 4: Prioritize evolving or placing cards
                    delayAndExecute(() -> {
                        placeCardEnemy();
                        enemyActionStr = "Attempting evolving or placing cards.";
                        enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);

                        // Task 5: Attack the player
                        delayAndExecute(() -> {
                            attackPlayerEnemy();
                            enemyActionStr = "Enemy turn complete. It is your turn.";
                            enemyActionLayout.setText(debugFont, enemyActionStr, Color.RED, 100, Align.left, true);
                            isEnemyTurn = false;
                        }, 1000);
                    }, 1000);
                }, 1000);
            }, 1000);
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // --------------------------           HELPER METHODS          --------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    // Helper method to introduce delay between tasks
    private void delayAndExecute(Runnable task, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gdx.app.postRunnable(task);
        }).start();
    }

    // Helper method to process card battle interaction
    private void processCardInteraction(int attackingPlayerInt, int playerIndex, int enemyIndex) {
        Card playerCard = cardOnScreenDatas.get(playerIndex).getCard();
        Card enemyCard = cardOnScreenDatas.get(enemyIndex).getCard();

        // attackingPlayerInt == 0 means it's the player's turn to attack
        if (attackingPlayerInt == 0) {
            if (!playerCard.getName().equals("Blank") && !enemyCard.getName().equals("Blank")) {
                playerCard.setShield(playerCard.getShield() - enemyCard.getAttack());
                enemyCard.setShield(enemyCard.getShield() - playerCard.getAttack());
                if (playerCard.getShield() < 1){
                    cardsInPlayerField[playerIndex - 13] = null;
                    cardOnScreenDatas.get(playerIndex).remakeCard(37, cardOnScreenDatas.get(playerIndex).getX(), cardOnScreenDatas.get(playerIndex).getY(),cardOnScreenDatas.get(playerIndex).getScale());
                }
                if (enemyCard.getShield() < 1){
                    cardsInPlayerField[enemyIndex - 10] = null;
                    cardOnScreenDatas.get(enemyIndex).remakeCard(37, cardOnScreenDatas.get(enemyIndex).getX(), cardOnScreenDatas.get(enemyIndex).getY(),cardOnScreenDatas.get(enemyIndex).getScale());
                }
            } else if (!playerCard.getName().equals("Blank") && enemyCard.getName().equals("Blank")) {
                enemyHealth -= playerCard.getAttack();
            }
        }

        // attackingPlayerInt == 1 means it's the enemy's turn to attack
        else if (attackingPlayerInt == 1) {
            if (!enemyCard.getName().equals("Blank") && !playerCard.getName().equals("Blank")) {
                enemyCard.setShield(enemyCard.getShield() - playerCard.getAttack());
                playerCard.setShield(playerCard.getShield() - enemyCard.getAttack());
                if (playerCard.getShield() < 1){
                    cardsInPlayerField[playerIndex - 13] = null;
                    cardOnScreenDatas.get(playerIndex).remakeCard(37, cardOnScreenDatas.get(playerIndex).getX(), cardOnScreenDatas.get(playerIndex).getY(),cardOnScreenDatas.get(playerIndex).getScale());
                }
                if (enemyCard.getShield() < 1){
                    cardsInPlayerField[enemyIndex - 10] = null;
                    cardOnScreenDatas.get(enemyIndex).remakeCard(37, cardOnScreenDatas.get(enemyIndex).getX(), cardOnScreenDatas.get(enemyIndex).getY(),cardOnScreenDatas.get(enemyIndex).getScale());
                }
            } else if (!enemyCard.getName().equals("Blank") && playerCard.getName().equals("Blank")) {
                playerHealth -= enemyCard.getAttack();
            }
        }

        // Determine win or lose if applicable
        if (enemyHealth < 1) {
            win = true;
            winLoseActive = true;
        } else if (playerHealth < 1) {
            win = false;
            winLoseActive = true;
        }
    }

    // This method is used to draw card to enemy hand
    void drawCardEnemy(){
        if (cardsInEnemyHand.size() < 5 && !cardsInEnemyDeck.isEmpty() && !drawnEnemyBool) {

            // Selects random card to add to enemy hand
            int randomIndex = (int) (Math.random() * cardsInEnemyDeck.size());
            Card cardToAdd = cardsInEnemyDeck.get(randomIndex).deepCopy();
            cardsInEnemyHand.add(cardToAdd);
            cardsInEnemyDeck.remove(randomIndex);

            // Update enemy hand UI
            for (int i = 0; i <= 4; i++) {
                if (cardOnScreenDatas.get(i).getCardID() == 37) { // Blank card
                    cardOnScreenDatas.get(i).remakeCard(cardToAdd, cardOnScreenDatas.get(i).getX(), cardOnScreenDatas.get(i).getY(), cardOnScreenDatas.get(i).getScale());
                    break;
                }
            }

            drawnEnemyBool = true;
        }
    }

    // This method is used to place card on the field-slot
    void placeCardEnemy(){
        boolean placed = false;
        for (int i = 0; i < cardsInEnemyHand.size(); i++) { // Loop through every card in hand
            Card handCard = cardsInEnemyHand.get(i);
            for (int j = 10; j < 13; j++) { // Loop through every field slot
                CardOnScreenData fieldSlot = cardOnScreenDatas.get(j);
                Card fieldCard = fieldSlot.getCard();

                // Check if evolution is possible
                boolean canEvolve = fieldCard.getStage() == handCard.getStage() - 1 && fieldCard.getType().equals(handCard.getType()) && enemyEnergy >= handCard.getCost();

                // Check if placement on blank slot is possible
                boolean canPlaceOnBlank = enemyEnergy >= handCard.getCost() && fieldSlot.getCard().getName().equals("Blank");
                if (canEvolve || canPlaceOnBlank) {

                    // Decrease energy
                    enemyEnergy -= handCard.getCost();
                    if (canEvolve) {
                        // Evolution: add attack and shield to hand card
                        handCard.setAttack(fieldCard.getAttack() + handCard.getAttack());
                        handCard.setShield(fieldCard.getShield() + handCard.getShield());

                    } else if (handCard.getStage() > 1) {
                        // Place raw higher-stage card on blank slot, halve its attack
                        handCard.setAttack(handCard.getAttack() / 2);
                    }

                    // Place hand card on the field slot
                    fieldSlot.remakeCard(handCard.deepCopy(), fieldSlot.getX(), fieldSlot.getY(), fieldSlot.getScale());

                    // Replace hand card with a blank card in the hand display
                    for (int k = 0; k < 4; k++) {
                        if (cardOnScreenDatas.get(k).getCard().getName().equals(handCard.getName())) {
                            cardOnScreenDatas.get(k).remakeCard(37, cardOnScreenDatas.get(k).getX(), cardOnScreenDatas.get(k).getY(), cardOnScreenDatas.get(k).getScale());
                            break;
                        }
                    }

                    // Remove the placed card from enemy hand
                    cardsInEnemyHand.remove(handCard);
                    placed = true;
                    break;
                }
            }
            if (placed) break; // Exit the outer loop once a card has been placed
        }
    }

    // This method is used to discard a card on enemy hand
    void discardCardEnemy(){
        // Determine if field is full
        boolean full = true;
        for (int i = 0; i < 4; i++){
            if (cardOnScreenDatas.get(i).getCard().getName().equals("Blank")) {
                full = false;
                break;
            }
        }

        // Discard priority logic
        // Check if hand size exceeds the limit
        // Or if enemy recharge <10
        if (cardsInEnemyHand.size() >= 5 || (enemyRecharge < 10)) {
            Card discardCandidate = null;

            // Step 1: Prioritize discarding cards with high-cost if enough energy is available
            if (enemyEnergy >= 6 && cardsInEnemyHand.size() > 2) {
                for (Card card : cardsInEnemyHand) {
                    if (card.getCost() > 6) {
                        // Find the lowest-stage card in the hand
                        for (Card potentialCard : cardsInEnemyHand) {
                            if (discardCandidate == null || potentialCard.getStage() < discardCandidate.getStage()) {
                                discardCandidate = potentialCard;
                            }
                        }
                        break;
                    }
                }
            }

            // Step 2: If field is full, prioritize discarding basic cards (Stage 1)
            for (Card card : cardsInEnemyHand) {
                if (card.getStage() == 1 && full) { // Check if basic and field is full
                    discardCandidate = card;
                    break;
                }
            }

            // Step 3: Discard higher-stage cards that don't match any card type on the field
            if (discardCandidate == null) {
                for (Card card : cardsInEnemyHand) {
                    // Only consider cards with stage 2 (higher than basic)
                    if (card.getStage() == 2) {
                        boolean matchesFieldType = false;
                        for (Card fieldCard : cardsInEnemyField) {
                            // Check if this card has the same type as any card already placed on the field
                            if (fieldCard != null && fieldCard.getType().equals(card.getType())) {
                                matchesFieldType = true;
                                break;
                            }
                        }
                        // If no matching type found on the field, discard this card
                        if (!matchesFieldType) {
                            discardCandidate = card;
                            break; // Discard the first matching card
                        }
                    }
                }
            }

            // Step 4: Discard duplicates (higher to lower stages)
            if (discardCandidate == null) {
                for (int i = 0; i < cardsInEnemyHand.size(); i++) {
                    Card card = cardsInEnemyHand.get(i);
                    for (int j = i + 1; j < cardsInEnemyHand.size(); j++) {
                        if (card.getName().equals(cardsInEnemyHand.get(j).getName())) { //Found duplicate
                            discardCandidate = card;
                            break;
                        }
                    }
                    if (discardCandidate != null) {
                        break;
                    }
                }
            }

            // If a card was chosen to discard, proceed with discard
            if (discardCandidate != null) {
                cardsInEnemyHand.remove(discardCandidate); // Remove the card from the hand
                enemyEnergy++; // Gain energy as a result of discarding
                enemyRecharge++; // Recharge increases with discard
                // Update UI: replace card in hand with blank card
                for (int k = 0; k < 5; k++) {
                    if (cardOnScreenDatas.get(k).getCard().getName().equals(discardCandidate.getName())) {
                        cardOnScreenDatas.get(k).remakeCard(37, cardOnScreenDatas.get(k).getX(), cardOnScreenDatas.get(k).getY(), cardOnScreenDatas.get(k).getScale());
                        break;
                    }
                }
            }
        }
    }

    // This method is used to handle the enemy's attack phase during its turn
    void attackPlayerEnemy(){
        processCardInteraction(1, 13, 10);
        processCardInteraction(1, 14, 11);
        processCardInteraction(1, 15, 12);
    }
}
