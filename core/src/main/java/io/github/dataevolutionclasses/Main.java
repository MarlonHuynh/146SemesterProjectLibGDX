/*
    -- !!!ATTENTION!!!
    -- Hi if you're trying to catch yourself up on understanding the code, this is the recommended reading order:
    -- Card.java > CardReader.java > Main.java
    -- This will give you a better sense of how a CSV file is converted into Card objects, then stored, and eventually used
    -- by main.java. If you want to jump straight ahead into Main.java, that's okay too; the only thing you need
    -- to know about Card.java and CardReader.java is that Card.java stores information about cards like name
    -- and texture and CardReader reads the CardStats2.CSV file, converts it into Card objects, and stores
    -- objects in cardList to be used in Main.
    -- Yours truly, Marlon

    Class for Gameplay Scene
    Will be managed by SceneManager (TBD) in the future, but is currently used for testing UI and cards
*/

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
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private List<Card> cardList;                    // Master Card Storage (Do not change)
    private HashMap<String, Card> nameToCardHashmap = new HashMap<String, Card>(); // Master Card Storage (Do not change)
    private HashMap<String, Integer> nameToIntHashmap = new HashMap<String, Integer>(); // Master Card Storage (Do not change)
    private ArrayList<CardOnScreenData> cardOnScreenDatas;  // Houses all information about all cards displayed on the screen
    private ArrayList<Card> cardsInPlayerDeck = new ArrayList<Card>();
    private ArrayList<Card> cardsInPlayerHand = new ArrayList<Card>();
    private ArrayList<Card> cardsInPlayerField = new ArrayList<Card>();
    private SpriteBatch drawBatch;
    private boolean drawnBool = false;
    private int turnCount = 0;
    private int selectedCardNumber = -1;            // Changes when a player clicks on a card to the index of the selected card in cardOnScreenDatas
    private int prevSelectedCardNumber = -1;        // Keeps track of previous selected index
    private BitmapFont debugFont, noncardUIFont;
    private Sprite playerHealthSpr, enemyHealthSpr, playerCloudSpr, enemyCloudSpr, playerEnergySpr, enemyEnergySpr, bgSpr;
    private int playerHealth, enemyHealth, playerRecharge, enemyRecharge, playerEnergy, enemyEnergy;
    private String drawnStr;
    private GlyphLayout drawnTextLayout = new GlyphLayout();
    private Vector3 worldCoords = new Vector3();
    private int frameCounter = 0;
    private StringBuilder stringBuilder = new StringBuilder();

    private BitmapFont font;
    // Instantiated upon startup
    @Override
    public void create() {

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);    // Center the camera
        // Read and generate cards and place cards into cardList
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        for (int i = 0; i < cardList.size(); i++){
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
        // Initialize non-card Fonts and text
        debugFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        debugFont.getData().setScale(0.4f);
        noncardUIFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        noncardUIFont.getData().setScale(1f);
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        drawBatch = new SpriteBatch();
        drawnStr = "You can draw a card";
        drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
        // Initialize non-card sprites, with scale and position
        bgSpr = new Sprite(new Texture("background.png"));
        playerHealthSpr = new Sprite(new Texture("yourhealth.png"));
        playerHealthSpr.setScale(0.65f);
        playerHealthSpr.setPosition(-35, 180);
        enemyHealthSpr = new Sprite(new Texture("enemyhealth.png"));
        enemyHealthSpr.setScale(0.65f);
        enemyHealthSpr.setPosition(-35, 270);
        playerCloudSpr = new Sprite(new Texture("playercloud.png"));
        playerCloudSpr.setScale(0.5f);
        playerCloudSpr.setPosition(380, 180);
        enemyCloudSpr = new Sprite(new Texture("enemycloud.png"));
        enemyCloudSpr.setScale(0.5f);
        enemyCloudSpr.setPosition(380, 340);
        playerEnergySpr = new Sprite(new Texture("playerenergy.png"));
        playerEnergySpr.setScale(0.5f);
        playerEnergySpr.setPosition(380, 110);
        enemyEnergySpr = new Sprite(new Texture("enemyenergy.png"));
        enemyEnergySpr.setScale(0.5f);
        enemyEnergySpr.setPosition(380, 270);
        // Initialize stat variables
        playerHealth = 60;
        enemyHealth = 40;
        playerEnergy = 0;
        playerRecharge = 0;
        enemyEnergy = 0;
        enemyRecharge = 0;
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<CardOnScreenData>();
        // Create the cards in the player's deck
        // TODO: Initial deck will be a deck taken from from the library section
        List<String> strTemp = Arrays.asList("Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Bubble Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "A-Starfish");
        for (String s : strTemp) {
            cardsInPlayerDeck.add(nameToCardHashmap.get(s));
        }
        // Take random 5 cards from the player's deck to place in the player's hand and remove from deck
        for (int i = 0; i < 5; i++){
            int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
            cardsInPlayerHand.add(nameToCardHashmap.get(cardsInPlayerDeck.get(randomIndex).getName()));
            cardsInPlayerDeck.remove(randomIndex);
        }
        // Create all cards on screen
        // Enemy's hand (Index 0-4)
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        //  Player's hand (Index 5-9)
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        // Field top (enemy) (Index 10-12)
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        // Field bottom (player) (Index 13-15)
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        // Deck draw (Index 16), Trash (Index 17), and End Turn (Index 18)
        cardOnScreenDatas.add(new CardOnScreenData(35, viewport.getWorldWidth() * (1f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(36, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(38, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (4.75f / 16f), 0.4f));
        // Set up an input processor to handle clicks
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                boolean clicked = false;
                // Convert screen coordinates to world coordinates
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                // Loop through each sprite and check if the click is within a bounding rectangle
                for (int i = 0; i < cardOnScreenDatas.size(); i++) {
                    if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)
                        || cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        // Update selection variables accordingly to what was clicked and what was previously clicked
                        if (selectedCardNumber == -1) {
                            selectedCardNumber = i;
                            prevSelectedCardNumber = 0; // Initialized to 0 (enemy hand 1) at first to avoid null exceptions when retrieving card data. Doesn't affect any playerside logic.
                            clicked = true;
                            break;
                        }
                        else {
                            prevSelectedCardNumber = selectedCardNumber;
                            selectedCardNumber = i;
                            clicked = true;
                            break;
                        }
                    }
                }
                // If nothing has been clicked and nothing has been clicked, no additional logic needed so returns
                if (prevSelectedCardNumber == -1 && selectedCardNumber == -1 ) {
                    return clicked;
                }
                // Perform actions based on what was clicked and what was previously clicked
                CardOnScreenData currData = cardOnScreenDatas.get(selectedCardNumber);
                CardOnScreenData prevData = cardOnScreenDatas.get(prevSelectedCardNumber);
                // 1) Fielding Card logic (prev -> card in hand, curr -> player field blank)
                if (!prevData.getCard().getName().equals("Blank")                       // CONDITIONS: Previous select is not blank card
                    && currData.getCard().getName().equals("Blank")                     // Current select is blank card
                    && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
                    && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
                    && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
                    // Subtrack cost from energy if applicable
                    if (playerEnergy >= prevData.getCard().getCost()) {
                        playerEnergy -= prevData.getCard().getCost();
                    }
                    // Add card to field array and remove from hand array
                    cardsInPlayerField.add(prevData.getCard());
                    for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                        if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
                            cardsInPlayerHand.remove(i);
                            break;
                        }
                    }
                    // Remake the card's UI to be reflective of the swap
                    currData.remakeCard(prevData.getCardID(), currData.getX(), currData.getY(), currData.getScale());
                    prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
                }
                // 2) Trash Card logic (prev -> card in hand, curr -> trash)
                else if (prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9 // CONDITIONS: Previous select is in player hand
                    && !prevData.getCard().getName().equals("Blank")                // Previous select is not blank card
                    && currData.getCard().getName().equals("Trash")) {              // Current select is in trash
                    Card prevCard = prevData.getCard();
                    // Remove from hand
                    for (int i = 0; i < cardsInPlayerHand.size(); i++) {
                        if (cardsInPlayerHand.get(i).getName().equals(prevCard.getName())) {
                            cardsInPlayerHand.remove(i);
                            break;
                        }
                    }
                    // Remake Card UI
                    prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale());
                    // Change energy vars
                    playerEnergy++;
                    playerRecharge++;
                }
                // 3) Draw Card logic (current -> draw)
                else if (currData.getCard().getName().equals("Draw")    // CONDITIONS: Current select is trash
                    && cardsInPlayerHand.size() < 5                     // Less than 5 cards in hand
                    && cardsInPlayerDeck.size() > 0                     // Deck size is greater than 0
                    && drawnBool == false) {                            // Player hasnt drawn this turn yet
                    // Add card at random index to hand and remove card from cardsInPlayerDeck
                    int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
                    Card cardToAdd = cardsInPlayerDeck.get(randomIndex);
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
                    // Reset drawn bools
                    drawnBool = false;
                    drawnStr = "You can draw a card.";
                    drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
                    // Reset energy to energy recharge amount
                    playerEnergy = playerRecharge;
                    // Increase turn count and change text
                    turnCount++;
                }
                return clicked;
                }
        });
    }
    // Called every frame in render to draw the screen
    // Note: DO NOT MAKE NEW BATCHES OR VARIABLES EVERY FRAME THIS WILL TANK YOUR FPS
    public void draw(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);
        // Display FPS counter and position of cursor
        worldCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        //camera.unproject(worldCoords);
        drawBatch.setProjectionMatrix(camera.combined);
        drawBatch.begin();
        // Draw BG
        bgSpr.draw(drawBatch);
        // Draw debug FPS
        stringBuilder.setLength(0);
        stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
        debugFont.draw(drawBatch, stringBuilder, 520, 340);
        // Draw cursor X, Y
        stringBuilder.setLength(0);
        stringBuilder.append("X: ").append((int)worldCoords.x);
        debugFont.draw(drawBatch, stringBuilder, 520, 380);
        stringBuilder.setLength(0);
        stringBuilder.append("Y: ").append((int)worldCoords.y);
        debugFont.draw(drawBatch, stringBuilder, 520, 360);
        // Draw non-card UI sprites
        playerHealthSpr.draw(drawBatch);
        enemyHealthSpr.draw(drawBatch);
        playerEnergySpr.draw(drawBatch);
        enemyEnergySpr.draw(drawBatch);
        playerCloudSpr.draw(drawBatch);
        enemyCloudSpr.draw(drawBatch);
        // Draw non-card text UI
        debugFont.draw(drawBatch, drawnTextLayout, 5, 200);
        stringBuilder.setLength(0);
        stringBuilder.append("Cards Left: ");
        stringBuilder.append(cardsInPlayerDeck.size());
        debugFont.draw(drawBatch, stringBuilder, 5, 165);
        stringBuilder.setLength(0);
        stringBuilder.append("Turn ");
        stringBuilder.append(turnCount);
        debugFont.draw(drawBatch, stringBuilder, 540, 250);
        // Draw healths
        stringBuilder.setLength(0);
        stringBuilder.append(playerHealth);
        noncardUIFont.draw(drawBatch, stringBuilder, 35, 270);
        stringBuilder.setLength(0);
        stringBuilder.append(enemyHealth);
        noncardUIFont.draw(drawBatch, stringBuilder, 35, 360);
        // Draw player energy and recharge
        stringBuilder.setLength(0);
        stringBuilder.append(playerRecharge);
        noncardUIFont.draw(drawBatch, stringBuilder, 460, 265);
        stringBuilder.setLength(0);
        stringBuilder.append(playerEnergy);
        noncardUIFont.draw(drawBatch, stringBuilder, 460, 200);
        // Draw enemy energy and recharge
        stringBuilder.setLength(0);
        stringBuilder.append(enemyRecharge);
        noncardUIFont.draw(drawBatch, stringBuilder, 460, 425);
        stringBuilder.setLength(0);
        stringBuilder.append(enemyEnergy);
        noncardUIFont.draw(drawBatch, stringBuilder, 460, 360);
        // Draw every card on the screen
        for (CardOnScreenData CoSD : cardOnScreenDatas) {
            drawCard(CoSD, camera, drawBatch);
        }
        // Draw Select Sprite if needed
        if (selectedCardNumber != -1){
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(drawBatch);
        }
        drawBatch.end();
        //camera.update();
    }
    public void drawCard(CardOnScreenData CoSD, OrthographicCamera camera, SpriteBatch batch){
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
        // Increment frame counter
        frameCounter++;

        // Log memory every 100 frames
        if (frameCounter >= 100) {
            Gdx.app.log("Memory", "Used: " + Gdx.app.getJavaHeap() + " bytes");
            frameCounter = 0; // Reset counter after logging
        }
    }

    // Called when resizing window
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // Called when exiting
    @Override
    public void dispose() {
        drawBatch.dispose();
        debugFont.dispose();
        noncardUIFont.dispose();
        font.dispose();
        bgSpr.getTexture().dispose();
        playerHealthSpr.getTexture().dispose();
        enemyHealthSpr.getTexture().dispose();
        playerCloudSpr.getTexture().dispose();
        enemyCloudSpr.getTexture().dispose();
        playerEnergySpr.getTexture().dispose();
        enemyEnergySpr.getTexture().dispose();
    }
}
