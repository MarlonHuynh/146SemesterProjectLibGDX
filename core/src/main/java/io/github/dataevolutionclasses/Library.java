package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    // Storage
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

    //
    private StringBuilder stringBuilder = new StringBuilder();
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();
    private int selectedCardNumber = -1;

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
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        for (int i = 0; i < cardList.size(); i++){
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }
        CardOnScreenData.staticSetCardList(cardList);

        cardOnPage = new ArrayList<>();
        for (int i = 0; i < 15; i ++){
            cardOnPage.add(cardList.get(i).deepCopy());
        }

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
        // Sprite
        spriteBatch = new SpriteBatch();
        bgSpr = new Sprite(new Texture("background.png"));

        // Sprite for player deck
        playersDeckIcon = new Sprite(new Texture("btn_deck.png"));
        playersDeckIcon.setPosition(370, 500);

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

        backSpr.draw(spriteBatch);

        for (CardOnScreenData CoSD : cardOnScreenDatas)
            drawCard(CoSD, spriteBatch);
        if (selectedCardNumber != -1)
            cardOnScreenDatas.get(selectedCardNumber).getSelectedSprite().draw(spriteBatch);
        defaultFont.draw(spriteBatch, deckLayout, 10, 550);

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
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                for (int i = 0; i < cardOnScreenDatas.size(); i++) {
                    if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        System.out.println("Selected");
                        // Update selection variables accordingly to what was clicked and what was previously clicked
                        selectedCardNumber = i;
                        clicked = true;
                        cardInDeck.add(cardList.get(selectedCardNumber));
                        deckStr += cardList.get(selectedCardNumber).getName() + ", ";
                        deckLayout.setText(defaultFont, deckStr, Color.RED, 500, Align.left, true);
                        break;
                    }
                }
                //To click onto the sprite of the playerDeckList
                //Creation of coordinates for x and y of sprite deck button
                float xMin = playersDeckIcon.getX();
                float xMax = xMin + playersDeckIcon.getWidth();
                float yMin = playersDeckIcon.getY();
                float yMax = yMin + playersDeckIcon.getHeight();

                if (worldCoords.x <= xMax && worldCoords.x >= xMin  &&
                    worldCoords.y <= yMax && worldCoords.y >= yMin)
                {
                    //check if empty, then do nothing
                    if(cardInDeck.isEmpty())
                    {
                        cardOnScreenDatas.add(new CardOnScreenData(cardOnPage.get(0), 80, 400, 0.45f));
                    }
                    else
                    {
                        //Removes all current cards being displayed
                        while (!cardOnScreenDatas.isEmpty())
                        {
                            cardOnScreenDatas.remove(0);
                        }

                    }

                    //Remove below clearing method after implementation
                    while (!cardOnScreenDatas.isEmpty())
                    {
                        cardOnScreenDatas.remove(0);
                    }
                    //Stop removing from here
                }
                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    buttonSound.play(0.4f);
                    game.setScreen(new Title(game));
                }

                return clicked;
            }
        });
    }
}
