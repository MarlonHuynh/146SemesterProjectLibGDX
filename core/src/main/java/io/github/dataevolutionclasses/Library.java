package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage vars
    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();          // Master Card Storage (Do not change)
    private List<Card> cardOnPage;
    private ArrayList<Card> cardInDeck;
    static ArrayList<Card> staticCardInDeck;
    private ArrayList<CardOnScreenData> cardOnScreenDatas = new ArrayList<>();
    // Spr
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, backSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    private String deckStr = "Amount of Cards in Deck: ";
    private GlyphLayout deckLayout = new GlyphLayout();
    private BitmapFont defaultFont;
    // Sprite for Player Deck button
    private Sprite playersDeckIcon;
    private Sprite sortByNameSprite;
    private Sprite sortByCostSprite;
    private Sprite sortByAttackSprite;
    private Sprite sortByShieldSprite;
    private Sprite sortByStageSprite;
    private Sprite removeSortSprite;
    private List<Card> viewCardList;                                                        // Master Card Storage (Do not change)
    private StringBuilder stringBuilder = new StringBuilder();
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();
    private int selectedCardNumber = -1;
    private int currentPage = 0;
    private final int CARDS_PER_PAGE = 15;
    private Sprite nextButtonSprite;
    private Sprite prevButtonSprite;
    // button sound effect
    private Sound buttonSound = buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));

    //
    private Game game;
    public Library(Game game) {
        this.game = game;
    }


    public void show() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(300, 300, 0);  // Center at 600x600 middle
        camera.update();
        createInputProcessor();
        // Pop storage
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateFirst35CardsFromFile();
        List<Card> tempCardList = reader.getCardList();
        cardList = new ArrayList<>();
        for (int i = 0; i < 33; i++){
            cardList.add(tempCardList.get(i));
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);

        viewCardList = new ArrayList<>(cardList);

        cardOnPage = new ArrayList<>();
        for (int i = 0; i < 15; i ++){
            cardOnPage.add(cardList.get(i).deepCopy());
        }

        /**
         * Displays the first set of cards to start
         * Loads other important buttons and basic cards
         * Loads the starting sprites as well as the buttons used for sorting
         */
        cardInDeck = new ArrayList<Card>();
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(0), 80, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(1), 190, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(2), 300, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(3), 410, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(4), 530, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(5), 80, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(6), 190, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(7),300, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(8), 410, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(9), 530, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(10), 80, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(11), 190, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(12), 300, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(13), 410, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(14), 530, 100, 0.45f));
        nextButtonSprite = new Sprite(new Texture("btn_nextpage.png"));
        nextButtonSprite.setSize(100, 25);
        nextButtonSprite.setPosition(300, 0);

        prevButtonSprite = new Sprite(new Texture("btn_prevpage.png"));
        prevButtonSprite.setSize(100, 25);
        prevButtonSprite.setPosition(200, 0);
        updateCardsOnPage();
        // Sprite
        spriteBatch = new SpriteBatch();
        bgSpr = new Sprite(new Texture("bg_orange.png"));

        //Sprite buttons for Sorting
        sortByNameSprite = new Sprite(new Texture("btn_sortbyname.png"));
        sortByNameSprite.setSize(100,25);
        sortByNameSprite.setPosition(10, 510);

        sortByCostSprite = new Sprite(new Texture("btn_sortbycost.png"));
        sortByCostSprite.setSize(100,25);
        sortByCostSprite.setPosition(120, 510);

        sortByAttackSprite = new Sprite(new Texture("btn_sortbyattack.png"));
        sortByAttackSprite.setSize(100,25);
        sortByAttackSprite.setPosition(230, 510);

        sortByShieldSprite = new Sprite(new Texture("btn_sortbyshield.png"));
        sortByShieldSprite.setSize(100,25);
        sortByShieldSprite.setPosition(10, 480);

        sortByStageSprite = new Sprite(new Texture("btn_sortbystage.png"));
        sortByStageSprite.setSize(100,25);
        sortByStageSprite.setPosition(120, 480);

        removeSortSprite = new Sprite(new Texture("btn_removesorting.png"));
        removeSortSprite.setSize(100,25);
        removeSortSprite.setPosition(230, 480);

        // Sprite for player deck
        playersDeckIcon = new Sprite(new Texture("btn_deck.png"));
        playersDeckIcon.setPosition(370, 500);

        /**
         * More setting of our spirtes and text
         */
        defaultFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        defaultFont.getData().setScale(0.4f);
        deckLayout.setText(defaultFont, deckStr, Color.RED, 500, Align.left, true);
        defaultFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        defaultFont.getData().setScale(0.4f);
        deckLayout.setText(defaultFont, deckStr, Color.RED, 600, Align.left, true);
        backSpr = new Sprite(new Texture("btn_back.png"));
        backSpr.setScale(0.5f);
        backSpr.setPosition(-30, 550);
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    /**
     * The draw function to draw out our function and also set our camera
     */
    public void draw() {
        ScreenUtils.clear(245 / 255f, 1250 / 255f, 205 / 255f, 1f);
        camera.position.set(300, 300, 0); // Recenter camera on resize
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        bgSpr.draw(spriteBatch);
        playersDeckIcon.draw(spriteBatch);
        sortByNameSprite.draw(spriteBatch);
        sortByAttackSprite.draw(spriteBatch);
        sortByStageSprite.draw(spriteBatch);
        sortByCostSprite.draw(spriteBatch);
        sortByShieldSprite.draw(spriteBatch);
        removeSortSprite.draw(spriteBatch);
        nextButtonSprite.draw(spriteBatch);
        prevButtonSprite.draw(spriteBatch);
        backSpr.draw(spriteBatch);
        for (CardOnScreenData CoSD : cardOnScreenDatas)
            CoSD.drawCard(CoSD, spriteBatch, stringBuilder);
        if (selectedCardNumber != -1)
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
        defaultFont.draw(spriteBatch, deckLayout, 10, 550);
        defaultFont.draw(spriteBatch, "Must have 40 cards to view deck.", 350, 570);

        spriteBatch.end();
    }

    /**
     * Used to update the cards in order to reflect what is being shown
     */
    private void updateCardsOnPage() {
        int start = currentPage * CARDS_PER_PAGE;
        int end = Math.min(start + CARDS_PER_PAGE, viewCardList.size());
        cardOnPage = new ArrayList<>(viewCardList.subList(start, end));

        // Update `cardOnScreenDatas` to reflect `cardOnPage`
        cardOnScreenDatas.clear();
        for (int i = 0; i < cardOnPage.size(); i++) {
            cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(i), 80 + (110 * (i % 5)), 400 - (150 * (i / 5)), 0.45f));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    // Called when exiting
    @Override
    public void dispose() {


    }

    /**
     * Handles all the input for when the mouse clicks on a certain area
     */
    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                // Reduce transparency if button clicked
                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    clicked = true;
                    backSpr.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }

                //Sorting functions buttons
                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByName(viewCardList);
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    sortByNameSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }
                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByCost(viewCardList);
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    sortByCostSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByAttack(viewCardList);
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    sortByAttackSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }
                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = CardSorter.sortByShield(viewCardList);
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    sortByShieldSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }

                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = CardSorter.sortByStage(viewCardList);
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    sortByStageSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }

                //NOTE remove sort also quits deck
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = cardList;
                    buttonSound.play();
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                    clicked = true;
                    removeSortSprite.setColor(1, 1, 1, 0.8f);
                    return clicked;
                }

                //Buttons to go next page or previous page
                if (worldCoords.x >= 200 && worldCoords.x <= 300 && worldCoords.y >= 0 && worldCoords.y <= 25) {
                    // Previous button clicked
                    if (currentPage > 0) {
                        buttonSound.play();
                        prevButtonSprite.setColor(1, 1, 1, 0.8f);
                        currentPage--;
                        updateCardsOnPage();
                    }
                }

                if (worldCoords.x >= 300 && worldCoords.x <= 400 && worldCoords.y >= 0 && worldCoords.y <= 25) {
                    // Next button clicked
                    if ((currentPage + 1) * CARDS_PER_PAGE < cardList.size()) {
                        buttonSound.play();
                        nextButtonSprite.setColor(1, 1, 1, 0.8f);
                        currentPage++;
                        updateCardsOnPage();
                    }
                }
                // Check for card selection
                for (int i = 0; i < cardOnScreenDatas.size(); i++) {
                    // Check if the click is on a card (either front or back)
                    if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y) ||
                        cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        System.out.println("Selected " + cardInDeck.size());

                        // Calculate the actual index of the card in the full list
                        int actualIndex = currentPage * CARDS_PER_PAGE + i;  // Calculate the index in the full list
                        int cardCount = 0;
                        if (actualIndex >= 0 && actualIndex < viewCardList.size()) {
                            // Set selected card index based on actual index
                            selectedCardNumber = i;
                            clicked = true;
                            if (cardInDeck.size() >= 40) {
                                break;
                            } else {
                                cardInDeck.add(viewCardList.get(actualIndex));  // Add the selected card to the deck

                                // Update the displayed count of selected cards
                                String selectedCountStr = deckStr + cardInDeck.size();
                                deckLayout.setText(defaultFont, selectedCountStr, Color.RED, 500, Align.left, true);  // Update deck layout with count
                            }
                        }
                        break;
                    }
                }

                //To click onto the sprite of the playerDeckList
                //Creation of coordinates for x and y of sprite deck button
                float xMin = playersDeckIcon.getX();
                float xMax = xMin + playersDeckIcon.getWidth();
                float yMin = playersDeckIcon.getY();
                float yMax = yMin + playersDeckIcon.getHeight();
                //Will use for toggling button
                int counter = 0;

                if (worldCoords.x <= xMax && worldCoords.x >= xMin  && worldCoords.y <= yMax && worldCoords.y >= yMin && cardInDeck.size() == 40){
                    viewCardList = cardInDeck;
                    updateCardsOnPage();
                    }

                return clicked;
            }

            /**
             * More checks when the button is being pressed
             */
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
                    staticCardInDeck = cardInDeck;
                    buttonSound.play();
                    game.setScreen(new Title(game));
                    dispose();
                }

                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 510 && worldCoords.y <= 535){
                    sortByNameSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 510 && worldCoords.y <= 535){
                    sortByCostSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 510 && worldCoords.y <= 535){
                    sortByAttackSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 480 && worldCoords.y <= 505){
                    sortByShieldSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 480 && worldCoords.y <= 505){
                    sortByStageSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 480 && worldCoords.y <= 505){
                    removeSortSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 200 && worldCoords.x <= 300 && worldCoords.y >= 0 && worldCoords.y <= 25){
                    prevButtonSprite.setColor(1, 1, 1, 1f);
                }
                if (worldCoords.x >= 300 && worldCoords.x <= 400 && worldCoords.y >= 0 && worldCoords.y <= 25){
                    nextButtonSprite.setColor(1, 1, 1, 1f);
                }

                return clicked;
            }
        });
    }

    public void playButtonSound(){
        buttonSound.play(0.3f);
    }
}
