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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // Util
import java.util.stream.Collectors;

public class Library extends ApplicationAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage vars
    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private List<Card> viewCardList;                                                        // Master Card Storage (Do not change)
    private ArrayList<CardOnScreenData> cardOnScreenDatas;                              // Houses all information about all cards spots displayed on the screen
    private SpriteBatch spriteBatch;
    private BitmapFont debugFont, noncardUIFont;
    private String drawnStr = "You can draw a card";
    private final GlyphLayout drawnTextLayout = new GlyphLayout();
    private final Vector3 worldCoords = new Vector3();
    private final StringBuilder stringBuilder = new StringBuilder();
    private int frameCounter = 0;
    private Stage stage;
//    private Scroll scroll;
    private Stage uiStage; // New stage for UI elements
    private Skin skin; // Skin for styling UI elements
    private Integer lastEvent;
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();          // Master Card Storage (Do not change)
    private List<Card> cardOnPage;
    private ArrayList<Card> cardInDeck;
//    private ArrayList<CardOnScreenData> cardOnScreenDatas = new ArrayList<>();
    // Spr
    private Sprite bgSpr, playBtn, libBtn, helpBtn, exitBtn, titleSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    private String deckStr = "Deck: ";
    private GlyphLayout deckLayout = new GlyphLayout();
    private BitmapFont defaultFont;
    private boolean clicked = false;
    private int selectedCardNumber = -1;


    // Instantiated upon startup
    @Override
    public void create() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//        scroll = new Scroll();
//        scroll.createSlider(stage); //Add the slider into the stage
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
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


//        cardOnPage = new ArrayList<>();
//        for (int i = 0; i < 15; i ++){
//            cardOnPage.add(cardList.get(i).deepCopy());
//        }

        viewCardList = new ArrayList<>(cardList);

        CardOnScreenData.staticSetCardList(cardList);   // Sets the static cardList in CardOnScreenData so it knows which cardList to reference
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<>();
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
        TextButton sortByHealthButton = new TextButton("Sort by Shield", skin);
        TextButton sortByAttackButton = new TextButton("Sort by Attack", skin);
        TextButton unsorted = new TextButton("Remove sorting", skin);

        // Add button listeners (you can implement sorting later)
        sortByNameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Name logic
                viewCardList = CardSorter.sortByName(cardList);
            }
        });
        sortByStageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Stage logic
                viewCardList = CardSorter.sortByStage(cardList);
                System.out.println("Sorting by Stage");
            }
        });
        sortByCostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Cost logic
                viewCardList = CardSorter.sortByCost(cardList);
                System.out.println("Sorting by Cost");
            }
        });
        sortByHealthButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Health logic
                viewCardList = CardSorter.sortByShield(cardList);
                System.out.println("Sorting by Shield");
            }
        });
        sortByAttackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Sort by Attack logic
                viewCardList = CardSorter.sortByAttack(cardList);
                System.out.println("Sorting by Attack");
            }
        });

        unsorted.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                viewCardList = cardList;
            }
        });

        // Add buttons to the table in a vertical layout
        menuTable.add(sortByNameButton).fillX().pad(10); // Pad for spacing
//        menuTable.row(); // Move to the next row
        menuTable.add(sortByStageButton).fillX().pad(10);
//        menuTable.row();
        menuTable.add(sortByCostButton).fillX().pad(10);
//        menuTable.row();
        menuTable.add(sortByHealthButton).fillX().pad(10);
//        menuTable.row();
        menuTable.add(sortByAttackButton).fillX().pad(10);
//        menuTable.row();
        menuTable.add(unsorted).fillX().pad(10);
        // Add the table to the UI stage
        uiStage.addActor(menuTable);
    }

    private void createInputProcessor() {
    }

    public void drawAll(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(245/255f, 1250/255f, 205/255f, 1f);

        worldCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Draw background
        bgSpr.draw(spriteBatch);

        // Draw debug FPS and cursor coordinates
        stringBuilder.setLength(0);
        stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
        debugFont.draw(spriteBatch, stringBuilder, 520, 340);
        stringBuilder.setLength(0);
        stringBuilder.append("X: ").append((int)worldCoords.x);
        debugFont.draw(spriteBatch, stringBuilder, 520, 380);
        stringBuilder.setLength(0);
        stringBuilder.append("Y: ").append((int)worldCoords.y);
        debugFont.draw(spriteBatch, stringBuilder, 520, 360);

        ArrayList<CardOnScreenData>  cardOnScreenDatas = new ArrayList<>();
        for (int i = 0; i < viewCardList.size() && i < 10; i++) {
            int x = (i % 5) * 100 + 55; //45
//            int y = ((35-i) / 3)* 140 + 50; //90
            int y = ((35-i) / 5) * 50 ;
            cardOnScreenDatas.add(new CardOnScreenData(viewCardList.get(i), x, y, 0.45f));
        }
        // Draw each card on screen
        for (CardOnScreenData CoSD : cardOnScreenDatas) {
            drawCard(CoSD, spriteBatch);
        }
        spriteBatch.end();
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
//    public void adjustCamera() {
//        float cardHeight = 150;  // Assume each card has a height of 150 units
//        float totalHeight = cardHeight * 11;
//        float visibleHeight = viewport.getWorldHeight();
//
//
//        // Calculate the maximum scrollable range
//        float maxScroll = totalHeight - visibleHeight;
//
//
//        // Get the slider value and calculate the camera Y offset
//        float sliderValue = scroll.getSliderValue();  // Value from 0 to 100
//        float yOffset = (sliderValue / 100f) * maxScroll;
//
//
//        // Adjust the camera's position
//        camera.position.y = totalHeight - (visibleHeight / 2) - yOffset;
//        camera.update();
//    }
    // Called every refresh rate for rendering
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stage.act(); //Process any UI events
//        adjustCamera();
        drawAll();
        stage.draw(); //Draw the stage (including the slider)
        // Draw the UI stage for the menu
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
//        scroll.dispose();
        spriteBatch.dispose();
        debugFont.dispose();
        noncardUIFont.dispose();
        bgSpr.getTexture().dispose();
    }

    //kms
    //kmsFUCK THIS
}
