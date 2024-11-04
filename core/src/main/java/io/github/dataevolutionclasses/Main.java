///*
//    Creates the gameplay screen of the game and all logic on what will be drawn on the gameplay screen, variables, and input handling.
//    Basically, if it has to do with the gameplay, the code will be here.
//*/
//
//package io.github.dataevolutionclasses;
//
//import com.badlogic.gdx.ApplicationAdapter; // Rendering
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.utils.Align;
//import com.badlogic.gdx.utils.ScreenUtils;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.Gdx; // Input
//import com.badlogic.gdx.InputAdapter;
//import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List; // Util
//
//public class Main extends ApplicationAdapter {
//    // Window vars
//    private OrthographicCamera camera;              // Camera
//    private FitViewport viewport;                   // Viewport
//    // Storage vars
//    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
//    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();      // Master Card Storage (Do not change)
//    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>(); // Master Card Storage (Do not change)
//    private ArrayList<CardOnScreenData> cardOnScreenDatas;                              // Houses all information about all cards spots displayed on the screen
//    private final ArrayList<Card> cardsInPlayerDeck = new ArrayList<>();                  // Cards in player's deck
//    private final ArrayList<Card> cardsInPlayerHand = new ArrayList<>();                  // Cards in player's hand
//    private final ArrayList<Card> cardsInPlayerField = new ArrayList<>();                 // Cards in player's field
//    private final ArrayList<Card> cardsInEnemyDeck = new ArrayList<>();
//    private final ArrayList<Card> cardsInEnemyHand = new ArrayList<>();
//    private final ArrayList<Card> cardsInEnemyField = new ArrayList<>();
//    // UI vars
//    private SpriteBatch spriteBatch;
//    private Sprite playerHealthSpr, enemyHealthSpr, playerCloudSpr, enemyCloudSpr, playerEnergySpr, enemyEnergySpr, bgSpr;
//    private BitmapFont debugFont, noncardUIFont;
//    private String drawnStr = "You can draw a card";
//    private final GlyphLayout drawnTextLayout = new GlyphLayout();
//    private final GlyphLayout playerHealthLayout = new GlyphLayout();
//    private final GlyphLayout enemyHealthLayout = new GlyphLayout();
//    private final Vector3 worldCoords = new Vector3();
//    private final StringBuilder stringBuilder = new StringBuilder();
//    // Stats vars
//    private int playerHealth, enemyHealth, playerRecharge, enemyRecharge, playerEnergy, enemyEnergy;
//    // Game State vars
//    private boolean drawnBool = false;              // Keeps track of whether player has drawn or not yet
//    private boolean discardBool = false;
//    private boolean drawnEnemyBool = false;
//    private final boolean discardEnemyBool = false;
//    private int turnCount = 0;                      // Turn #
//    private int selectedCardNumber = -1;            // Index of cardOnScreenDatas currently selected
//    private int prevSelectedCardNumber = -1;        // Index of cardOnScreenDatas previously selected
//    // Debug vars
//    private int frameCounter = 0;
//
//    // Instantiated upon startup
//    @Override
//    public void create() {
//        // Set up camera, viewport, and input processor
//        camera = new OrthographicCamera();
//        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
//        viewport.apply();
//        camera.position.set(300, 300, 0);  // Center at 600x600 middle
//        camera.update();
//        createInputProcessor();
//        // Read and generate cards and place cards into cardList
//        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
//        reader.generateCardsFromCSV();
//        cardList = reader.getCardList();
//        for (int i = 0; i < cardList.size(); i++){
//            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
//            nameToIntHashmap.put(cardList.get(i).getName(), i);
//        }
//        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
//        // Initialize drawBatch
//        spriteBatch = new SpriteBatch();
//        // Initialize non-card Fonts and text
//        debugFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
//        debugFont.getData().setScale(0.4f);
//        noncardUIFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
//        noncardUIFont.getData().setScale(1.2f);
//        drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
//        // Initialize non-card sprites, with scale and position
//        bgSpr = new Sprite(new Texture("background.png"));
//        playerHealthSpr = new Sprite(new Texture("yourhealth.png"));
//        playerHealthSpr.setScale(0.65f);
//        playerHealthSpr.setPosition(-35, 180);
//        enemyHealthSpr = new Sprite(new Texture("enemyhealth.png"));
//        enemyHealthSpr.setScale(0.65f);
//        enemyHealthSpr.setPosition(-35, 270);
//        playerCloudSpr = new Sprite(new Texture("playercloud.png"));
//        playerCloudSpr.setScale(0.5f);
//        playerCloudSpr.setPosition(380, 180);
//        enemyCloudSpr = new Sprite(new Texture("enemycloud.png"));
//        enemyCloudSpr.setScale(0.5f);
//        enemyCloudSpr.setPosition(380, 340);
//        playerEnergySpr = new Sprite(new Texture("playerenergy.png"));
//        playerEnergySpr.setScale(0.5f);
//        playerEnergySpr.setPosition(380, 110);
//        enemyEnergySpr = new Sprite(new Texture("enemyenergy.png"));
//        enemyEnergySpr.setScale(0.5f);
//        enemyEnergySpr.setPosition(380, 270);
//        // Initialize stat variables
//        playerHealth = 60;
//        enemyHealth = 40;
//        playerEnergy = 0;
//        playerRecharge = 0;
//        enemyEnergy = 0;
//        enemyRecharge = 0;
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
//        // Enemy Deck
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
//        // Create all cards on screen
//        // Enemy's hand (Index 0-4)
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInEnemyHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
//        //  Player's hand (Index 5-9)
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(0),  viewport.getWorldWidth() * (3.3f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(1),  viewport.getWorldWidth() * (5.64f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(2),  viewport.getWorldWidth() * (8 / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(3),  viewport.getWorldWidth() * (10.35f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        cardOnScreenDatas.add(new CardOnScreenData(cardsInPlayerHand.get(4),  viewport.getWorldWidth() * (12.7f / 16f), viewport.getWorldHeight() * (1.8f / 16f), 0.45f));
//        // Field top (enemy) (Index 10-12)
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
//        // Field bottom (player) (Index 13-15)
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        cardOnScreenDatas.add(new CardOnScreenData(37, viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
//        // Deck draw (Index 16), Trash (Index 17), and End Turn (Index 18)
//        cardOnScreenDatas.add(new CardOnScreenData(35, viewport.getWorldWidth() * (1f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
//        cardOnScreenDatas.add(new CardOnScreenData(36, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (1.5f / 16f), 0.4f));
//        cardOnScreenDatas.add(new CardOnScreenData(38, viewport.getWorldWidth() * (15f / 16f), viewport.getWorldHeight() * (4.75f / 16f), 0.4f));
//    }
//    // Called every frame in render to draw the screen
//    // Note: DO NOT MAKE NEW BATCHES OR VARIABLES EVERY FRAME THIS WILL TANK YOUR FPS VERY BADLY!!! 300fps -> 6fps
//    public void draw(){
//        // Clears screen and prepares batch for drawing
//        ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);
//        // Display FPS counter and position of cursor
//        worldCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//        //camera.unproject(worldCoords);
//        spriteBatch.setProjectionMatrix(camera.combined);
//        spriteBatch.begin();
//        // Draw BG
//        bgSpr.draw(spriteBatch);
//        // Draw debug FPS
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
//        // Draw non-card UI sprites
//        playerHealthSpr.draw(spriteBatch);
//        enemyHealthSpr.draw(spriteBatch);
//        playerEnergySpr.draw(spriteBatch);
//        enemyEnergySpr.draw(spriteBatch);
//        playerCloudSpr.draw(spriteBatch);
//        enemyCloudSpr.draw(spriteBatch);
//        // Draw non-card text UI
//        debugFont.draw(spriteBatch, drawnTextLayout, 5, 200);
//        stringBuilder.setLength(0);
//        stringBuilder.append("Cards Left: ");
//        stringBuilder.append(cardsInPlayerDeck.size());
//        debugFont.draw(spriteBatch, stringBuilder, 5, 165);
//        stringBuilder.setLength(0);
//        stringBuilder.append("Turn ");
//        stringBuilder.append(turnCount);
//        debugFont.draw(spriteBatch, stringBuilder, 540, 250);
//        // Draw healths
//        stringBuilder.setLength(0);
//        stringBuilder.append(playerHealth);
//        playerHealthLayout.setText(noncardUIFont, stringBuilder, Color.BLACK, 100, Align.center, true);
//        noncardUIFont.draw(spriteBatch, playerHealthLayout, 2, 270);
//        stringBuilder.setLength(0);
//        stringBuilder.append(enemyHealth);
//        enemyHealthLayout.setText(noncardUIFont, stringBuilder, Color.BLACK, 100, Align.center, true);
//        noncardUIFont.draw(spriteBatch, enemyHealthLayout, 2, 360);
//        // Draw player energy and recharge
//        stringBuilder.setLength(0);
//        stringBuilder.append(playerRecharge);
//        noncardUIFont.draw(spriteBatch, stringBuilder, 460, 265);
//        stringBuilder.setLength(0);
//        stringBuilder.append(playerEnergy);
//        noncardUIFont.draw(spriteBatch, stringBuilder, 460, 200);
//        // Draw enemy energy and recharge
//        stringBuilder.setLength(0);
//        stringBuilder.append(enemyRecharge);
//        noncardUIFont.draw(spriteBatch, stringBuilder, 460, 425);
//        stringBuilder.setLength(0);
//        stringBuilder.append(enemyEnergy);
//        noncardUIFont.draw(spriteBatch, stringBuilder, 460, 360);
//        // Draw every card on the screen
//        for (CardOnScreenData CoSD : cardOnScreenDatas) {
//            drawCard(CoSD, spriteBatch);
//        }
//        // Draw Select Sprite if needed
//        if (selectedCardNumber != -1){
//            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
//        }
//        spriteBatch.end();
//        //camera.update();
//    }
//    public void drawCard(CardOnScreenData CoSD, SpriteBatch batch){
//        // Draw cardback
//        CoSD.getCardbackSprite().draw(batch);
//        if (!CoSD.getCard().getName().equals("Draw") && !CoSD.getCard().getName().equals("Trash") && !CoSD.getCard().getName().equals("Blank") && !CoSD.getCard().getName().equals("End Turn")) {
//            // Draw creature
//            CoSD.getCardSprite().draw(batch);
//            // Draw text
//            CoSD.getNameFont().draw(batch, CoSD.getCard().getName(), CoSD.getNameX(), CoSD.getNameY());
//            CoSD.getNameFont().draw(batch, CoSD.getDescLayout(), CoSD.getDescX(), CoSD.getDescY());
//            // Draw cost
//            stringBuilder.setLength(0);
//            stringBuilder.append(CoSD.getCard().getCost());
//            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getCostTextX(), CoSD.getCostTextY());
//            // Draw attack
//            stringBuilder.setLength(0);
//            stringBuilder.append(CoSD.getCard().getAttack());
//            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getAttackTextX(), CoSD.getAttackTextY());
//            // Draw shield
//            stringBuilder.setLength(0);
//            stringBuilder.append(CoSD.getCard().getShield());
//            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getShieldTextX(), CoSD.getShieldTextY());
//        }
//    }
//
//    // Called every refresh rate for rendering
//    @Override
//    public void render() {
//        draw();
//        // Log memory every 100 frames
//        frameCounter++;
//        if (frameCounter >= 100) {
//            Gdx.app.log("Memory", "Used: " + Gdx.app.getJavaHeap() + " bytes");
//            frameCounter = 0; // Reset counter after logging
//        }
//    }
//
//    // Called when resizing window
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        camera.position.set(300, 300, 0); // Recenter camera on resize
//        camera.update();
//    }
//
//    // Called when exiting
//    @Override
//    public void dispose() {
//        spriteBatch.dispose();
//        debugFont.dispose();
//        noncardUIFont.dispose();
//        bgSpr.getTexture().dispose();
//        playerHealthSpr.getTexture().dispose();
//        enemyHealthSpr.getTexture().dispose();
//        playerCloudSpr.getTexture().dispose();
//        enemyCloudSpr.getTexture().dispose();
//        playerEnergySpr.getTexture().dispose();
//        enemyEnergySpr.getTexture().dispose();
//    }
//
//    public void createInputProcessor(){
//        Gdx.input.setInputProcessor(new InputAdapter() {
//            @Override
//            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//                boolean clicked = false;
//
//                // ---------- Check which card was clicked ----------
//                // Convert screen coordinates to world coordinates
//                Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
//                // Ignore input if outside the 600x600 game area
//                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
//                    return false;
//
//                for (int i = 0; i < cardOnScreenDatas.size(); i++) {
//                    if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
//                        // Update selection variables accordingly to what was clicked and what was previously clicked
//                        if (selectedCardNumber == -1) {
//                            selectedCardNumber = i;
//                            prevSelectedCardNumber = 0; // Initialized to 0 (enemy hand 1) at first to avoid null exceptions when retrieving card data. Doesn't affect any playerside logic.
//                            clicked = true;
//                            break;
//                        }
//                        else {
//                            prevSelectedCardNumber = selectedCardNumber;
//                            selectedCardNumber = i;
//                            clicked = true;
//                            break;
//                        }
//                    }
//                }
//                // If nothing has been clicked and nothing has been clicked, no additional logic needed so returns
//                if (prevSelectedCardNumber == -1 && selectedCardNumber == -1 )
//                    return clicked;
//
//                // ---------- Perform actions based on what was clicked and what was previously clicked ----------
//                CardOnScreenData currData = cardOnScreenDatas.get(selectedCardNumber);
//                CardOnScreenData prevData = cardOnScreenDatas.get(prevSelectedCardNumber);
//                // 1) Fielding Card logic (prev -> card in hand, curr -> player field blank)
//                if (!prevData.getCard().getName().equals("Blank")                       // CONDITIONS: Previous select is not blank card
//                    && !prevData.getCard().getName().equals("Trash")
//                    && !prevData.getCard().getName().equals("End Turn")
//                    && !prevData.getCard().getName().equals("Discard")
//                    && currData.getCard().getName().equals("Blank")                     // Current select is blank card
//                    && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
//                    && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
//                    && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
//                    // Subtract cost from energy if applicable
//                    if (playerEnergy >= prevData.getCard().getCost()) {
//                        playerEnergy -= prevData.getCard().getCost();
//                    }
//                    // Add card to field array and remove from hand array
//                    cardsInPlayerField.add(prevData.getCard());
//                    for (int i = 0; i < cardsInPlayerHand.size(); i++) {
//                        if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
//                            cardsInPlayerHand.remove(i);
//                            break;
//                        }
//                    }
//                    // Halves attack if puts down an evolution card
//                    prevData.getCard().setAttack( prevData.getCard().getAttack() / 2);
//                    // Remake the card's UI to be reflective of the swap
//                    currData.remakeCard(prevData.getCardID(), currData.getX(), currData.getY(), currData.getScale());
//                    prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
//                }
//                // 1.1) Fielding card logic for EVOLUTION
//                else if (!prevData.getCard().getName().equals("Blank")                       // CONDITIONS: Previous select is not blank card
//                    && !prevData.getCard().getName().equals("Trash")
//                    && !prevData.getCard().getName().equals("End Turn")
//                    && !prevData.getCard().getName().equals("Discard")
//                    && !currData.getCard().getName().equals("Blank")                     // Current select is not blank
//                    && (prevData.getCard().getStage() == (currData.getCard().getStage()+1)) // New card is 1 stage higher than previous card
//                    && (prevData.getCard().getType().equals(currData.getCard().getType()))  // New card is same type as previous card
//                    && prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9       // Previous select is in player hand
//                    && selectedCardNumber >= 13 && selectedCardNumber <= 15             // Current select is in bottom field (player field)
//                    && prevData.getCard().getCost() <= playerEnergy) {                  // Player has enough money to place card
//                    // Subtract cost from energy if applicable
//                    if (playerEnergy >= prevData.getCard().getCost()) {
//                        playerEnergy -= prevData.getCard().getCost();
//                    }
//                    // Add card to field array and remove from hand array
//                    cardsInPlayerField.add(prevData.getCard());
//                    for (int i = 0; i < cardsInPlayerHand.size(); i++) {
//                        if (cardsInPlayerHand.get(i).getName().equals(prevData.getCard().getName())) {
//                            cardsInPlayerHand.remove(i);
//                            break;
//                        }
//                    }
//                    // Add prevolution attack to current card's attack
//                    prevData.getCard().setAttack(prevData.getCard().getAttack() + currData.getCard().getAttack());
//                    // Remake the card's UI to be reflective of the swap
//                    currData.remakeCard(prevData.getCardID(), currData.getX(), currData.getY(), currData.getScale());
//                    prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale()); // ID 37 -> blank card
//                }
//                // 2) Trash Card logic (prev -> card in hand, curr -> trash)
//                else if (prevSelectedCardNumber >= 5 && prevSelectedCardNumber <= 9 // CONDITIONS: Previous select is in player hand
//                    && !prevData.getCard().getName().equals("Blank")                // Previous select is not blank card
//                    && currData.getCard().getName().equals("Trash")                 // Current select is in trash
//                    && !discardBool){                                       // Have not discarded this turn yet
//                    Card prevCard = prevData.getCard();
//                    // Remove from hand
//                    for (int i = 0; i < cardsInPlayerHand.size(); i++) {
//                        if (cardsInPlayerHand.get(i).getName().equals(prevCard.getName())) {
//                            cardsInPlayerHand.remove(i);
//                            break;
//                        }
//                    }
//                    // Remake Card UI
//                    prevData.remakeCard(37, prevData.getX(), prevData.getY(), prevData.getScale());
//                    // Change energy vars
//                    playerEnergy++;
//                    playerRecharge++;
//                    // Update discard bool
//                    discardBool = true;
//                }
//                // 3) Draw Card logic (current -> draw)
//                else if (currData.getCard().getName().equals("Draw")    // CONDITIONS: Current select is trash
//                    && cardsInPlayerHand.size() < 5                     // Less than 5 cards in hand
//                    && !cardsInPlayerDeck.isEmpty()                     // Deck size is greater than 0
//                    && !drawnBool) {                            // Player hasnt drawn this turn yet
//                    // Add card at random index to hand and remove card from cardsInPlayerDeck
//                    int randomIndex = (int) (Math.random() * cardsInPlayerDeck.size());
//                    Card cardToAdd = cardsInPlayerDeck.get(randomIndex);
//                    cardsInPlayerHand.add(cardToAdd); // Add card at randomIndex into hand
//                    cardsInPlayerDeck.remove(randomIndex);
//                    // Update drawnBool (drawn for the turn)
//                    drawnBool = true;
//                    drawnStr = "You already drawn this turn.";
//                    drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
//                    // Change card left text
//                    // Update Card UI
//                    for (int i = 5; i <= 9; i++) {
//                        if (cardOnScreenDatas.get(i).getCardID() == 37) { // Blank
//                            CardOnScreenData CoSD = cardOnScreenDatas.get(i);
//                            cardOnScreenDatas.get(i).remakeCard(cardToAdd, CoSD.getX(), CoSD.getY(), CoSD.getScale());
//                            break;
//                        }
//                    }
//                }
//                // 4) End turn logic (current -> End turn)
//                else if (currData.getCard().getName().equals(("End Turn"))) {
//                    // TODO: Attack enemy
//                    // 13 vs 10
//                    Card card13 = cardOnScreenDatas.get(13).getCard();
//                    Card card10 = cardOnScreenDatas.get(10).getCard();
//                    if (!card13.getName().equals("Blank") && !card10.getName().equals("Blank")){
//                        card13.setShield(card13.getShield() - card10.getAttack());
//                    }
//                    else if (!card13.getName().equals("Blank") && card10.getName().equals("Blank")){
//                        enemyHealth -= card13.getAttack();
//                    }
//                    // 14 vs 11
//                    Card card14 = cardOnScreenDatas.get(14).getCard();
//                    Card card11 = cardOnScreenDatas.get(11).getCard();
//                    if (!card14.getName().equals("Blank") && !card11.getName().equals("Blank")){
//                        card14.setShield(card14.getShield() - card11.getAttack());
//                    }
//                    else if (!card14.getName().equals("Blank") && card11.getName().equals("Blank")){
//                        enemyHealth -= card14.getAttack();
//                    }
//                    // 15 vs 12
//                    Card card15 = cardOnScreenDatas.get(15).getCard();
//                    Card card12 = cardOnScreenDatas.get(12).getCard();
//                    if (!card15.getName().equals("Blank") && !card12.getName().equals("Blank")){
//                        card15.setShield(card14.getShield() - card12.getAttack());
//                    }
//                    else if (!card15.getName().equals("Blank") && card12.getName().equals("Blank")){
//                        enemyHealth -= card15.getAttack();
//                    }
//                    // Reset drawn bools
//                    if (cardsInPlayerDeck.isEmpty()){
//                        drawnStr = "No more cards left.";
//                    }
//                    else {
//                        drawnBool = false;
//                        drawnStr = "You can draw a card.";
//                    }
//                    drawnTextLayout.setText(debugFont, drawnStr, Color.RED, 100, Align.left, true);
//                    // Reset appropriate bools
//                    playerEnergy = playerRecharge;
//                    discardBool = false;
//                    // Process the enemy turn logic
//                    processEnemyTurn();
//                    // Increase turn count
//                    turnCount++;
//                }
//                return clicked;
//            }
//        });
//    }
//
//    void processEnemyTurn(){
//        // TODO: Enemy AI
//        // TODO 0.1: Draw a card if able
//        if (!drawnEnemyBool && !cardsInEnemyHand.isEmpty() && cardsInEnemyHand.size() < 5){
//            // Add card at random index to hand and remove card from cardsInPlayerDeck
//            int randomIndex = (int) (Math.random() * cardsInEnemyDeck.size());
//            Card cardToAdd = cardsInEnemyDeck.get(randomIndex);
//            cardsInEnemyHand.add(cardToAdd); // Add card at randomIndex into hand
//            cardsInEnemyDeck.remove(randomIndex);
//            // Update drawnBool (drawn for the turn)
//            drawnEnemyBool = true;
//            // Change card left text
//            // Update Card UI
//            for (int i = 0; i <= 4; i++) {
//                if (cardOnScreenDatas.get(i).getCardID() == 37) { // Blank
//                    CardOnScreenData CoSD = cardOnScreenDatas.get(i);
//                    cardOnScreenDatas.get(i).remakeCard(cardToAdd, CoSD.getX(), CoSD.getY(), CoSD.getScale());
//                    break;
//                }
//            }
//            // Update discard bool
//            drawnBool = true;
//        }
//        // TODO 0.2: Place down a basic card or evolve if enough energy (Prioritize evolving cards)
//        // TODO 0.3: If not enough energy, discard least valuable card (Prioritize duplicate cards or cards of the same stage)
//        // TODO 0.4: Place down a card if evolve enough energy
//        // TODO 0.5: Draw a card if able and end turn
//    }
//}
