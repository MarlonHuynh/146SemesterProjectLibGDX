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
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<CardOnScreenData>();
        CardInstancesNames = new ArrayList<String>();
        // Create all cards on screen
        // Enemy's hand (Instance 1-5)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(0), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(1), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(2), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(3), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(4), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.4f));
        //  Player's hand (Instance 6-10)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(5), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(6), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(7), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(8), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.4f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(9), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.4f));
        // Field top (enemy) (Instance 11-13)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(10), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(11), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(12), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        // Field bottom (player) (Instance 15-16)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(13), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(14), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(15), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        // Deck draw (Instance 17)
        // TODO: To be replaced with cardList.get(number) of the card that just looks like a deck, no monster, just a pile of cards.
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(35), viewport.getWorldWidth() * (14 / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
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
            if (cardOnScreenDatas.get(i).getSprite().getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                selectedCardNumber = i + 1;
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
        // Test drawing card from drawCard function
        for (int i = 0; i < cardOnScreenDatas.size(); i++) {
            drawCard(cardOnScreenDatas.get(i), camera);
        }
        int screenX =  Gdx.input.getX();
        int screenY =  Gdx.input.getY();
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        // Display FPS counter and position of cursor
        SpriteBatch fpsBatch = new SpriteBatch();
        fpsBatch.setProjectionMatrix(camera.combined);
        fpsBatch.begin();
        defaultFont.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
        defaultFont.draw(fpsBatch, "X: " + screenX, 10, 60);
        defaultFont.draw(fpsBatch, "Y: " + screenY, 10, 45);
        fpsBatch.end();

    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {     // isLeftButtonPressed starts out False
                isLeftButtonPressed = true; // This + assigning it true right after makes it so only one instance of a left-click is detected until the left click button is released.
                // Console logs for debugging purposes
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                //System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                //System.out.println(cardListIndex);
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }

    public void drawCard(CardOnScreenData cardOnScreenData, OrthographicCamera camera){
        Card card = cardOnScreenData.getCard();
        float x = cardOnScreenData.getX();
        float y = cardOnScreenData.getY();
        float scale = cardOnScreenData.getScale();
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        Sprite cardbackSprite = cardOnScreenData.getBackSprite();
        cardbackSprite.setSize(cardbackTexture.getWidth() * scale, cardbackTexture.getHeight() * scale);
        // Initial card creature image
        Texture cardCreatureTexture = card.getTexture();
        Sprite cardCreatureSprite = cardOnScreenData.getSprite();
        cardCreatureSprite.setSize(cardCreatureSprite.getTexture().getWidth() * scale, cardCreatureSprite.getTexture().getHeight() * scale);
        // Set Font
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.setColor(Color.BLACK);
        font.getData().setScale(scale * 0.7f);
        // Create batch of sprite to be drawn
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Find position of card
        // Note: DO NOT USE PIXELS to get your position. Use relative sizing.
        float cardX = (x - ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2)); // Bottom left corner x
        float cardY = (y - ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2)); // Bottom left corner y
        float midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2);  // Distance from the card's left edge to middle
        float midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the card's bottom edge to middle
        // ----- Images -----
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch); // Draw card back
        cardCreatureSprite.setPosition(cardX+(midcardX/3.5f), cardY+(midcardY/1.5f));
        cardCreatureSprite.draw(batch); // Draw card creature
        // ----- Text -----
        font.draw(batch, card.getName(), cardX+(midcardX*0.6f), cardY+(midcardY*1.82f)); // Draw name text
        GlyphLayout descLayout = new GlyphLayout();
        float descWidth = 120 * scale;
        font.getData().setScale(scale * 0.6f);
        descLayout.setText(font, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.getData().setScale(scale * 0.8f);
        font.draw(batch, Integer.toString(card.getCost()), cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, Integer.toString(card.getAttack()), cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, Integer.toString(card.getShield()), cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // End batch and update camera frame
        batch.end();
        camera.update();
    }
}
