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
    private String costText = "9";                  // Cost text
    private String attackText = "6";                // Attack text
    private String shieldText = "3";                // Shield text

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
        // Initial startup card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);
        // Initial startup card creature image
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        // Initialize startup Font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.6f);
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

    // Called every frame in render to draw the screen
    public void draw(){
        // Clears screen and prepares batch for drawing
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        // Display FPS counter
        SpriteBatch fpsBatch = new SpriteBatch();
        fpsBatch.setProjectionMatrix(camera.combined);
        fpsBatch.begin();
        font.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
        fpsBatch.end();
        // Test drawing card from drawCard function
        //drawCard(viewport.getWorldWidth()*(1/4f), viewport.getWorldHeight()/2, 0.5f, cardList.get(0), camera);
        // Player hand
        drawCard(viewport.getWorldWidth()*(2/16f), viewport.getWorldHeight()*(2/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(4.5f/16f), viewport.getWorldHeight()*(2/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(7/16f), viewport.getWorldHeight()*(2/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(9.5f/16f), viewport.getWorldHeight()*(2/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(12/16f), viewport.getWorldHeight()*(2/16f), 0.5f);
        // Enemy hand
        drawCard(viewport.getWorldWidth()*(2/16f), viewport.getWorldHeight()*(14/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(4.5f/16f), viewport.getWorldHeight()*(14/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(7/16f), viewport.getWorldHeight()*(14/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(9.5f/16f), viewport.getWorldHeight()*(14/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(12/16f), viewport.getWorldHeight()*(14/16f), 0.5f);
        // Field top (enemy)
        drawCard(viewport.getWorldWidth()*(4.5f/16f), viewport.getWorldHeight()*(10/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(7/16f), viewport.getWorldHeight()*(10/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(9.5f/16f), viewport.getWorldHeight()*(10/16f), 0.5f);
        // Field bottom (player)
        drawCard(viewport.getWorldWidth()*(4.5f/16f), viewport.getWorldHeight()*(6/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(7/16f), viewport.getWorldHeight()*(6/16f), 0.5f);
        drawCard(viewport.getWorldWidth()*(9.5f/16f), viewport.getWorldHeight()*(6/16f), 0.5f);
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
                System.out.println(cardListIndex);
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }
    /*
    This drawCard functions works with Main.java's variables specifically (for input testing purposes)
    */
    public void drawCard(float x, float y, float scale){
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        Sprite cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * scale, cardbackTexture.getHeight() * scale);
        // Initial card creature image
        cardCreatureSprite.setSize(cardCreatureSprite.getTexture().getWidth() * scale, cardCreatureSprite.getTexture().getHeight() * scale);
        // Set Font
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(scale * 0.6f);
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
        font.draw(batch, nameText, cardX+(midcardX*0.6f), cardY+(midcardY*1.82f)); // Draw name text
        GlyphLayout descLayout = new GlyphLayout();
        float descWidth = 100 * scale;
        descLayout.setText(font, descText, Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.draw(batch, costText, cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, attackText, cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, shieldText, cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // End batch and update camera frame
        batch.end();
        camera.update();

    }
    /*
    This drawCard functions works independently of Main.java
        x - x-coordinate of card
        y - y-coordiate of card
        scale - scale of the card
        card - Card object to be drawn
        camera - the camera of the scene
    */
    public void drawCard(float x, float y, float scale, Card card, OrthographicCamera camera){
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        Sprite cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * scale, cardbackTexture.getHeight() * scale);
        // Initial card creature image
        Texture cardCreatureTexture = card.getTexture();
        Sprite cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getTexture().getWidth() * scale, cardCreatureSprite.getTexture().getHeight() * scale);
        // Set Font
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(scale * 0.6f);
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
        float descWidth = 100 * scale;
        descLayout.setText(font, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.draw(batch, Integer.toString(card.getCost()), cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, Integer.toString(card.getAttack()), cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, Integer.toString(card.getShield()), cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // End batch and update camera frame
        batch.end();
        camera.update();
    }
}
