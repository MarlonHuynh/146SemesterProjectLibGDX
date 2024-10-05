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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx; // Input
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.List; // Util

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private boolean isLeftButtonPressed = false;    // Input
    private List<Card> cardList;                    // Card Storage
    private int cardListIndex = 0;                  // Index to cycle through Card list
    private SpriteBatch batch;                      // SpriteBatch combine multiple Sprites into a texture to be drawn at runtime
    private Sprite cardCreatureSprite;              // Sprite for card creature
    private Sprite cardbackSprite;                  // Sprite for card back
    private float cardScale = 1f;                   // Card scale (affects all the other text placements according to the card size)
    private BitmapFont font;                        // Font
    private String nameText = "Creature name here"; // Name text
    private String descText = "Left-click to scroll through cards.";    // Description text
    private GlyphLayout descLayout = new GlyphLayout();;                // GylphLayout used to wrap text of description
    private float descWidth = 100 * cardScale;      // Specify the width for wrapping text in description
    private String costText = "9";                  // Cost text
    private String attackText = "6";                // Attack text
    private String shieldText = "3";                // Shield text
    private float cardX, cardY;                     // cardX and cardY determines placement of the card (image pivot are at the bottom-left corner! Not middle)
    private float midcardX, midcardY;               // midcardX and midcardY determines the distance from the bottom-left corner for the x and y axis respectively)
                                                    // Used to place text relative to card size

    // Instantiated upon startup
    @Override
    public void create() {

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                   // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        // Set up initial textures for the scene
        setInitialTextures();
        // Read and generate cards and place cards into Card list
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
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
        batch.dispose();
    }

    // Initialize starting textures/sprites
    public void setInitialTextures(){
        // Create a new batch (which combine multiple Sprites into a texture to be drawn at runtime)
        batch = new SpriteBatch();
        // Instantiate GlyphLayout (used for wrapping description's text)
        descLayout = new GlyphLayout();
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * cardScale, cardbackTexture.getHeight() * cardScale);
        // Initial card creature image
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
        // Set Font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.5f);
    }
    // Called every frame in render to draw the screen
    public void draw(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Find position of card
        // Note: DO NOT USE PIXELS to get your position. Use relative sizing.
        cardX = (viewport.getWorldWidth() / 2) - ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2); // Bottom left corner x
        cardY = (viewport.getWorldHeight() / 2) - ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Bottom left corner y
        midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2);  // Distance from the card's left edge to middle
        midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the card's bottom edge to middle
        // ----- Images -----
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch); // Draw card back
        cardCreatureSprite.setPosition(cardX+(midcardX/3.5f), cardY+(midcardY/1.5f));
        cardCreatureSprite.draw(batch); // Draw card creature
        // ----- Text -----
        font.draw(batch, nameText, cardX+(midcardX*0.6f), cardY+(midcardY*1.82f)); // Draw name text
        descLayout.setText(font, descText, Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.draw(batch, costText, cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, attackText, cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, shieldText, cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // ----- Display fps (and other diagnostics TBA in the future) -----
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
        batch.end();
    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {     // isLeftButtonPressed starts out False
                isLeftButtonPressed = true; // This + assigning it true right after makes it so only one instance of a left-click is detected until the left click button is released.
                // Change card vars to match attributes of the Card at cardList[cardListIndex]
                Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
                cardCreatureSprite.set(new Sprite(cardCreatureTexture));
                cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
                descText = cardList.get(cardListIndex).getDesc();
                nameText = cardList.get(cardListIndex).getName();
                costText = Integer.toString(cardList.get(cardListIndex).getCost());
                attackText = Integer.toString(cardList.get(cardListIndex).getAttack());
                shieldText = Integer.toString(cardList.get(cardListIndex).getShield());
                // Increment card index to get new creature in cardList
                if (cardListIndex < cardList.size() - 1){
                    cardListIndex++;
                }
                // Console logs for debugging purposes
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                System.out.println(cardX + ", " + cardY + ", " + midcardX + ", " + midcardY);
                System.out.println(cardListIndex);
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }
}
