package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // Util

public class DeckList extends ScreenAdapter
{
    public List<Card> deck;
    public static List<Card> cardList;
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Scroll scroll;
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private Sprite cardbackSprite;                  // Sprite for card back
    private Sprite cardCreatureSprite;              // Sprite for card creature
    private BitmapFont font;                        // Font
    private float cardScale = 1f;                   // Card scale (affects all the other text placements according to the card size)
    private Game game;
    public DeckList(Game game) {
        this.game = game;
    }

    public void draw()
    {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        scroll = new Scroll();
        scroll.createSlider(stage); //Add the slider into the stage

        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                   // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        // Read and generate cards and place cards into Card list
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/TestDeck.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        // Initial startup card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);
        // Initial startup card creature image
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        // Initialize startup Font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.5f);
    }

    public void create()
    {
        // Initialize drawBatch
        spriteBatch = new SpriteBatch();

        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/TestDeck.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();

        // Static initializer block to populate initial cards
        // Create multiple decks
        List<Card> decks = new ArrayList<>();

        // Create first deck
        List<String> deck1 = Arrays.asList(
            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl" );

        //Add the first deck to the list of decks
        for (int i = 0; i < cardList.size(); i++) {
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }

        for (String str : deck1){
            deck.add(nameToCardHashmap.get(str).deepCopy());
        }
    }

}


