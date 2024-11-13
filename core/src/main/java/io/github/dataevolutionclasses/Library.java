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
    private ArrayList<CardOnScreenData> cardOnScreenDatas = new ArrayList<>();
    // Spr
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, backSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    private String deckStr = "Deck: ";
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
        cardList = reader.getCardList();

        for (int i = 0; i < cardList.size(); i++){
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);

        viewCardList = new ArrayList<>(cardList);

        cardOnPage = new ArrayList<>();
        for (int i = 0; i < 15; i ++){
            cardOnPage.add(cardList.get(i).deepCopy());
        }

        cardInDeck = new ArrayList<Card>();
        nextButtonSprite = new Sprite(new Texture("btn_nextpage.png"));
        nextButtonSprite.setSize(100, 25);
        nextButtonSprite.setPosition(300, 0);

        prevButtonSprite = new Sprite(new Texture("btn_prevpage.png"));
        prevButtonSprite.setSize(100, 25);
        prevButtonSprite.setPosition(200, 0);

        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(0), 80, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(1), 190, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(2), 300, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(3), 410, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(4), 520, 400, 0.45f));


        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(5), 80, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(6), 190, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(7),300, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(8), 410, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(9), 520, 250, 0.45f));


        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(10), 80, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(11), 190, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(12), 300, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(13), 410, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(14), 520, 100, 0.45f));
        // Sprite
        spriteBatch = new SpriteBatch();
        bgSpr = new Sprite(new Texture("background.png"));

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
        playersDeckIcon = new Sprite(new Texture("youlose.png"));
        playersDeckIcon.setPosition(370, 480);


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
            drawCard(CoSD, spriteBatch);
        if (selectedCardNumber != -1)
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
        defaultFont.draw(spriteBatch, deckLayout, 10, 550);


        spriteBatch.end();
    }

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
            stringBuilder.setLength(0); stringBuilder.append(CoSD.getCard().getCost());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getCostTextX(), CoSD.getCostTextY());
            // Draw attack
            stringBuilder.setLength(0); stringBuilder.append(CoSD.getCard().getAttack());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getAttackTextX(), CoSD.getAttackTextY());
            // Draw shield
            stringBuilder.setLength(0); stringBuilder.append(CoSD.getCard().getShield());
            CoSD.getNumberFont().draw(batch, stringBuilder, CoSD.getShieldTextX(), CoSD.getShieldTextY());
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


    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                //To click onto the sprite of the playerDeckList
                if (worldCoords.x < 600 && worldCoords.x > 700 || worldCoords.y < 600 || worldCoords.y > 700)
                {


                }
                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByName(cardList);
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }
                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByCost(cardList);
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 510 && worldCoords.y <= 535) {
                    viewCardList = CardSorter.sortByAttack(cardList);
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }
                if (worldCoords.x >= 10 && worldCoords.x <= 110 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = CardSorter.sortByShield(cardList);
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }
                if (worldCoords.x >= 120 && worldCoords.x <= 220 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = CardSorter.sortByStage(cardList);
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }
                if (worldCoords.x >= 230 && worldCoords.x <= 330 && worldCoords.y >= 480 && worldCoords.y <= 505) {
                    viewCardList = cardList;
                    currentPage = 0;  // Reset to the first page
                    updateCardsOnPage();  // Refresh display for the first page
                    System.out.println("Cards sorted and displayed.");
                }

                if (worldCoords.x >= 200 && worldCoords.x <= 300 && worldCoords.y >= 0 && worldCoords.y <= 25) {
                    // Next button clicked
                    if (currentPage > 0) {
                        currentPage--;
                        updateCardsOnPage();
                    }
                }

                if (worldCoords.x >= 300 && worldCoords.x <= 400 && worldCoords.y >= 0 && worldCoords.y <= 25) {
                    // Previous button clicked
                    if ((currentPage + 1) * CARDS_PER_PAGE < cardList.size()) {
                        currentPage++;
                        updateCardsOnPage();
                    }
                }
// Check for card selection
                for (int i = 0; i < cardOnScreenDatas.size(); i++) {
                    // Check if the click is on a card (either front or back)
                    if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y) ||
                        cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        System.out.println("Selected");

                        // Calculate the actual index of the card in the full list
                        int actualIndex = currentPage * CARDS_PER_PAGE + i;  // Calculate the index in the full list

                        if (actualIndex >= 0 && actualIndex < viewCardList.size()) {
                            // Set selected card index based on actual index
                            selectedCardNumber = i;
                            clicked = true;
                            cardInDeck.add(viewCardList.get(actualIndex));  // Add the selected card to the deck
                            deckStr += viewCardList.get(actualIndex).getName() + ", ";  // Update the deck string
                            deckLayout.setText(defaultFont, deckStr, Color.RED, 500, Align.left, true);  // Update deck layout
                        }
                        break;
                    }
                }

                // Check if the back button is pressed
                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    buttonSound.play(0.4f);
                    game.setScreen(new Title(game));
                }

                return clicked;
            }
        });
    }
}
