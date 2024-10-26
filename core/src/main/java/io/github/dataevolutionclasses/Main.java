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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.List; // Util


public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private boolean isLeftButtonPressed = false;    // Input
    private List<Card> cardList;                    // Master Card Storage (Do not change)
    private BitmapFont defaultFont;                         // Font
    private ArrayList<CardOnScreenData> cardOnScreenDatas;  // Houses all information about all cards displayed on the screen
    private ArrayList<String> CardInstancesNames;           // Named instances of the card for easy debugging
    private int selectedCardNumber = -1;            // Changes when a player clicks on a card (starts at -1 for no card selected)
    GlyphLayout descLayout = new GlyphLayout();
    float descWidth;

    private BitmapFont font;


    // Instantiated upon startup
    @Override
    public void create() {
        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                   // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        // Read and generate cards and place cards into Card list
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        // Initialize startup Font
        defaultFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        defaultFont.getData().setScale(0.6f);
        // Initialize card font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<CardOnScreenData>();
        CardInstancesNames = new ArrayList<String>();
        // Create all cards on screen
        // Enemy's hand (Instance 0-4)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(0), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(1), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(2), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(3), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(4), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.45f));
        //  Player's hand (Instance 5-9)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(5), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(6), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(7), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(8), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.45f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(9), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.45f));
        // Field top (enemy) (Instance 10-12)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(10), viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(11), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(12), viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (10.1f / 16f), 0.5f));
        // Field bottom (player) (Instance 14-15)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(13), viewport.getWorldWidth() * (4.2f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(14), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(15), viewport.getWorldWidth() * (9.8f / 16f), viewport.getWorldHeight() * (5.9f / 16f), 0.5f));
        // Deck draw (Instance 16)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(35), viewport.getWorldWidth() * (14.7f / 16f), viewport.getWorldHeight() * (6.2f / 16f), 0.5f));
        // Trash (Instance 17)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(36), viewport.getWorldWidth() * (14.7f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        for (int i = 0; i < cardOnScreenDatas.size(); i ++){
            CardInstancesNames.add("Card " + cardOnScreenDatas.get(i).getCard().getName() + ", Instance #" + Integer.toString(i + 1));
            System.out.println("spriteName added: " + CardInstancesNames.get(i));
        }
        // Set up an input processor to handle clicks
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Check which sprite was clicked
                String clickedObject = getClickedObject(screenX, screenY);
                if (clickedObject != null) { // Selected/clicked a card!
                    System.out.println(clickedObject + " clicked!");
                    System.out.println("Current Selected Card is numbered: " + selectedCardNumber);
                    return true; // Event handled
                }
                else{
                    selectedCardNumber = -1; // No card is selected
                    System.out.println("Current Selected Card is numbered: " + selectedCardNumber);
                    System.out.println("No object in sprites array was clicked!");
                }
                return false; // No sprite was clicked
            }
        });
    }
    // Method to check which sprite was clicked and return its name
    private String getClickedObject(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        // Loop through each sprite and check if the click is within its bounding rectangle
        for (int i = 0; i < cardOnScreenDatas.size(); i++) {
            if (cardOnScreenDatas.get(i).getCardSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)
                || cardOnScreenDatas.get(i).getCardbackSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                selectedCardNumber = i;
                return CardInstancesNames.get(i); // Return the name of the clicked sprite
            }
        }
        return null; // No sprite was clicked
    }

    // Called every refresh rate for rendering
    @Override
    public void render() {
        draw();
        manageInput();
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

    // Called every frame in render to draw the screen
    public void draw(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        // Draw Select Sprite if needed
        if (selectedCardNumber != -1){
            drawSelected(cardOnScreenDatas.get(selectedCardNumber), camera);
        }
        // Test drawing card from drawCard function
        for (int i = 0; i < cardOnScreenDatas.size(); i++) {
            drawCard(cardOnScreenDatas.get(i), camera);
        }
        // Display FPS counter and position of cursor
        int screenX =  Gdx.input.getX();
        int screenY =  Gdx.input.getY();
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        SpriteBatch fpsBatch = new SpriteBatch();
        fpsBatch.setProjectionMatrix(camera.combined);
        fpsBatch.begin();
        defaultFont.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 530, 540); // Display FPS in bottom-left corner
        defaultFont.draw(fpsBatch, "X: " + worldCoords.x, 530, 580);
        defaultFont.draw(fpsBatch, "Y: " + worldCoords.y, 530, 560);
        fpsBatch.end();

    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {     // isLeftButtonPressed starts out False
                isLeftButtonPressed = true; // This + assigning it true right after makes it so only one instance of a left-click is detected until the left click button is released.
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
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
        if (!card.getName().equals("Draw") && !card.getName().equals("Trash")) {
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
}
