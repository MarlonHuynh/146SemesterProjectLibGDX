package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Storage
    // Storage vars
    private List<Card> cardList;                                                        // Master Card Storage (Do not change)
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();          // Master Card Storage (Do not change)
    private ArrayList<CardOnScreenData> cardOnScreenDatas = new ArrayList<>();
    // Spr
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, playBtn, libBtn, helpBtn, exitBtn, titleSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    //
    private StringBuilder stringBuilder = new StringBuilder();
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();
    //Player Library
    private Stage stage;
    private Skin skin;
    private TextButton button;

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
        // Pop storage
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        for (int i = 0; i < cardList.size(); i++){
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);

        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(0), 80, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(1), 190, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(2), 300, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(3), 410, 400, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(4), 530, 400, 0.45f));

        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(5), 80, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(6), 190, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(7),300, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(8), 410, 250, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(9), 530, 250, 0.45f));

        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(10), 80, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(11), 190, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(12), 300, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(13), 410, 100, 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(14), 530, 100, 0.45f));
        // Sprite
        spriteBatch = new SpriteBatch();
        bgSpr = new Sprite(new Texture("background.png"));
        // make Inp processor
        createInputProcessor();
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
        for (CardOnScreenData CoSD : cardOnScreenDatas)
            drawCard(CoSD, spriteBatch);

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
                int indexClicked = -1;
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                return clicked;
            }
        });
    }

    public void playerDeck()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the skin from assets (uiskin.json should be in the assets folder)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create a button with the label "Click Me"
        button = new TextButton("Click Me", skin);

        // Position the button in the center of the screen
        button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);

        // Add a listener to handle click events
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked!");
            }
        });

        // Add the button to the stage
        stage.addActor(button);
    }
}
