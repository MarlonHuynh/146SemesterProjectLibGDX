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
    private boolean drawnBool = false;
    private int turnCount = 0;
    private int selectedCardNumber = -1;            // Changes when a player clicks on a card (starts at -1 for no card selected)
    private int prevSelectedCardNumber = -1;
    private BitmapFont debugFont, noncardUIFont;                         // Font
    private Sprite playerHealthSpr, enemyHealthSpr, playerCloudSpr, enemyCloudSpr, playerEnergySpr, enemyEnergySpr, bgSpr;
    private int playerHealth, enemyHealth, playerRecharge, enemyRecharge, playerEnergy, enemyEnergy;
    private String drawnText;
    private GlyphLayout drawnTextLayout = new GlyphLayout();

    private BitmapFont font;
    // Instantiated upon startup
    @Override
    public void create() {
        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                   // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        // Read and generate cards and place cards into cardList
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        for (int i = 0; i < cardList.size(); i++){                        // Populate the nameToCard Hashmap (For easier searches of name to Card type);
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i));
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
        // Initialize debug Font
        debugFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        debugFont.getData().setScale(0.4f);
        noncardUIFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        noncardUIFont.getData().setScale(1f);
        // Initialize card font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        // Initialize non-card sprites and text
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
        drawnText = "You can draw a card.";
        drawnTextLayout.setText(debugFont, drawnText, Color.RED, 100, Align.left, true);
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
        List<String> strTemp = Arrays.asList("Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Bubble Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "A-Starfish");
        for (String s : strTemp) {
            cardsInPlayerDeck.add(nameToCardHashmap.get(s));
        }
        // Take random 5 cards from the player's deck to initialize in the player's hand
        for (int i = 0; i < 5; i++){
            int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size()); // Gets a random int from index 0 to last index of cardsInPlayerDeck
            cardsInPlayerHand.add(nameToCardHashmap.get(cardsInPlayerDeck.get(randomIndex).getName())); // Add card at randomIndex into hand
            cardsInPlayerDeck.remove(randomIndex);                           // Remove the card from cardsInPlayerDeck
        }
        // Create all cards on screen
        // Enemy's hand (Instance 0-4)
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(10,  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        //  Player's hand (Instance 5-9)
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
        // Field top (enemy) (Instance 10-12)
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        // Field bottom (player) (Instance 13-15)
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        // Deck draw (Instance 16)
        cardOnScreenDatas.add(new CardOnScreenData(35, viewport.getWorldWidth() * (1f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        // Trash (Instance 17)
        cardOnScreenDatas.add(new CardOnScreenData(36, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
        // End Turn (Instance 18)
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

                if (prevSelectedCardNumber != -1 && selectedCardNumber != -1) {
                    CardOnScreenData currData = cardOnScreenDatas.get(selectedCardNumber);
                    CardOnScreenData prevData = cardOnScreenDatas.get(prevSelectedCardNumber);
                    // 1) Fielding Card logic
                    if (!prevData.getCard().getName().equals("Blank") && !prevData.getCard().getName().equals("Trash")
                        && !prevData.getCard().getName().equals("Draw") && currData.getCard().getName().equals("Blank")
                        && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
                        && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
                        && prevData.getCard().getCost() <= playerEnergy) {                  // Checks if enough energy to place down
                        // Swaps blank with creature card
                        if (playerEnergy >= prevData.getCard().getCost()) {
                            playerEnergy -= prevData.getCard().getCost();
                        }
                        // Remove from the player hand array and place in player field array
                        cardsInPlayerField.add(prevData.getCard());
                        for (int i = 0; i < cardsInPlayerHand.size(); i++){
                            if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())){
                                cardsInPlayerHand.remove(i);
                                break;
                            }
                        }
                        // Remake the card's UI
                        currData.remakeCard(prevData.getCardID(), currData.getX(), currData.getY(), currData.getScale());
                        prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale());

                    }
                    // 2) Trash Card logic
                    else if (prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9 // Card is in player hand
                        &&  currData.getCard().getName().equals("Trash")) {  // Current select is in trash
                        Card prevCard = prevData.getCard();
                        // Remove from hand
                        for (int i = 0; i < cardsInPlayerHand.size(); i++){
                            if (cardsInPlayerHand.get(i).getName().equals(prevCard.getName())){
                                cardsInPlayerHand.remove(i);
                                break;
                            }
                        }
                        // Remake Card UI
                        prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale());
                        // Change energy vars
                        playerEnergy ++;
                        playerRecharge ++;
                    }
                    // 3) Draw Card logic
                    else if (currData.getCard().getName().equals("Draw") // Current select is in trash
                    && cardsInPlayerHand.size() < 5 // Less than 5 cards in hand
                    && cardsInPlayerDeck.size() > 0 // Deck contains card
                    && drawnBool == false) {           // Player hasnt drawn this turn yet
                        // Add card at random index to hand and remove card from cardsInPlayerDeck
                        int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
                        Card cardToAdd = cardsInPlayerDeck.get(randomIndex);
                        cardsInPlayerHand.add(cardToAdd); // Add card at randomIndex into hand
                        cardsInPlayerDeck.remove(randomIndex);
                        // Update drawnBool (drawn for the turn)
                        drawnBool = true;
                        drawnText = "You already drawn this turn.";
                        drawnTextLayout.setText(debugFont, drawnText, Color.RED, 100, Align.left, true);
                        // Update Card UI
                        for (int i = 5; i <= 9; i++){
                            if (cardOnScreenDatas.get(i).getCardID() == 37){ // Blank
                                CardOnScreenData CoSD = cardOnScreenDatas.get(i);
                                cardOnScreenDatas.get(i).remakeCard(cardToAdd, CoSD.getX(), CoSD.getY(), CoSD.getScale());
                                break;
                            }
                        }
                    }
                    else if (currData.getCard().getName().equals(("End Turn"))){
                        // Reset drawn bools
                        drawnBool = false;
                        drawnText = "You can draw a card.";
                        drawnTextLayout.setText(debugFont, drawnText, Color.RED, 100, Align.left, true);
                        // Reset energy to energy recharge amount
                        playerEnergy = playerRecharge;
                        // Increase turn count
                        turnCount++;
                    }

                }
                return clicked;
            }
        });
    }
    // Called every frame in render to draw the screen
    public void draw(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);
        // Display FPS counter and position of cursor
        int screenX =  Gdx.input.getX();
        int screenY =  Gdx.input.getY();
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        SpriteBatch drawBatch = new SpriteBatch();
        drawBatch.setProjectionMatrix(camera.combined);
        drawBatch.begin();
        bgSpr.draw(drawBatch);
        debugFont.draw(drawBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 520, 340);
        debugFont.draw(drawBatch, "X: " + (int)worldCoords.x, 520, 380);
        debugFont.draw(drawBatch, "Y: " + (int)worldCoords.y, 520, 360);
        // Draw non-card UI sprites
        playerHealthSpr.draw(drawBatch);
        enemyHealthSpr.draw(drawBatch);
        playerEnergySpr.draw(drawBatch);
        enemyEnergySpr.draw(drawBatch);
        playerCloudSpr.draw(drawBatch);
        enemyCloudSpr.draw(drawBatch);
        // End non-method batch
        drawBatch.end();
        // Draw every card on the screen
        for (CardOnScreenData CoSD : cardOnScreenDatas) {
            drawCard(CoSD, camera);
        }// Draw Select Sprite if needed
        if (selectedCardNumber != -1){
            drawSelected(cardOnScreenDatas.get(selectedCardNumber), camera);
        }
        // Draw non-card text UI
        drawBatch.begin();
        debugFont.draw(drawBatch, drawnTextLayout, 5, 200);
        debugFont.draw(drawBatch, "Cards Left", 5, 165);
        debugFont.draw(drawBatch, "in Deck: " + Integer.toString(cardsInPlayerDeck.size()), 5, 150);
        debugFont.draw(drawBatch, "Turn " + Integer.toString(turnCount), 540, 250);
        noncardUIFont.draw(drawBatch, Integer.toString(playerHealth), 35, 270);
        noncardUIFont.draw(drawBatch, Integer.toString(enemyHealth), 35, 360);
        noncardUIFont.draw(drawBatch, Integer.toString(playerRecharge), 460, 265);
        noncardUIFont.draw(drawBatch, Integer.toString(playerEnergy), 460, 200);
        noncardUIFont.draw(drawBatch, Integer.toString(enemyRecharge), 460, 425);
        noncardUIFont.draw(drawBatch, Integer.toString(enemyEnergy), 460, 360);
        drawBatch.end();
    }
    public void drawSelected(CardOnScreenData CoSD, OrthographicCamera camera) {
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        CoSD.getSelectedSprite().draw(batch);
        batch.end();
        camera.update();
    }
    public void drawCard(CardOnScreenData CoSD, OrthographicCamera camera){
        Card card = CoSD.getCard();
        // Create batch of sprite to be drawn
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Draw cardback
        CoSD.getCardbackSprite().draw(batch);
        if (!card.getName().equals("Draw") && !card.getName().equals("Trash") && !card.getName().equals("Blank") && !card.getName().equals("End Turn")) {
            // Draw creature
            CoSD.getCardSprite().draw(batch);
            // Draw text
            CoSD.getNameFont().draw(batch, card.getName(), CoSD.getNameX(), CoSD.getNameY());
            CoSD.getNameFont().draw(batch, CoSD.getDescLayout(), CoSD.getDescX(), CoSD.getDescY());
            CoSD.getNumberFont().draw(batch, Integer.toString(card.getCost()), CoSD.getCostTextX(), CoSD.getCostTextY());
            CoSD.getNumberFont().draw(batch, Integer.toString(card.getAttack()), CoSD.getAttackTextX(), CoSD.getAttackTextY());
            CoSD.getNumberFont().draw(batch, Integer.toString(card.getShield()), CoSD.getShieldTextX(), CoSD.getShieldTextY());
        }
        // End batch and update camera frame
        batch.end();
        camera.update();
    }

    // Called every refresh rate for rendering
    @Override
    public void render() {
        draw();
    }

    // Called when resizing window
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // Called when exiting
    @Override
    public void dispose() {
    }
}
