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

import java.awt.*;
import java.util.ArrayList;
import java.util.List; // Util
import java.lang.Math;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private boolean isLeftButtonPressed = false;    // Input
    private List<Card> cardList;                    // Card Storage
    private int cardListIndex = 0;                  // Index to cycle through Card list=
    private float cardScale = 1f;                   // Card scale (affects all the other text placements according to the card size)
    private BitmapFont font;                        // Font
    private ArrayList<String> spriteNames;
    private ArrayList<CardOnScreenData> cardOnScreenDatas;
    private CardOnScreenData enemyHand1, enemyHand2, enemyHand3, enemyHand4, enemyHand5;
    private Sprite enemyHand1Spr, enemyHand2Spr, enemyHand3Spr, enemyHand4Spr, enemyHand5Spr;


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
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.6f);
        // Set up array of Sprite names and sprites to keep track of the sprites on screen for input handling
        cardOnScreenDatas = new ArrayList<CardOnScreenData>();
        spriteNames = new ArrayList<String>();
        // Create all cards on screen
        // Initialize multiple CardOnScreenData instances with specified positions
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(0), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(1), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(2), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(3), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (14 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(4), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (14 / 16f), 0.5f));
        //  Instances for the player’s hand
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(5), viewport.getWorldWidth() * (2 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(6), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(7), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(8), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(9), viewport.getWorldWidth() * (12 / 16f), viewport.getWorldHeight() * (2 / 16f), 0.5f));
        // Field top (enemy)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(10), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(11), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(12), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (10 / 16f), 0.5f));
        // Field bottom (player)
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(13), viewport.getWorldWidth() * (4.5f / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(14), viewport.getWorldWidth() * (7 / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        cardOnScreenDatas.add(new CardOnScreenData(cardList.get(15), viewport.getWorldWidth() * (9.5f / 16f), viewport.getWorldHeight() * (6 / 16f), 0.5f));
        for (int i = 0; i < cardOnScreenDatas.size(); i ++){
            spriteNames.add(cardOnScreenDatas.get(i).getCard().getName() + ", Name instance #" + Integer.toString(i + 1));
            System.out.println("spriteName added: " + cardOnScreenDatas.get(i).getCard().getName() + ", Sprite instance #" + Integer.toString(i + 1));
        }
        // Set up an input processor to handle clicks
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Check which sprite was clicked
                String clickedObject = getClickedObject(screenX, screenY);
                if (clickedObject != null) {
                    System.out.println(clickedObject + " clicked!");
                    return true; // Event handled
                }
                else{
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
                return spriteNames.get(i); // Return the name of the clicked sprite
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
        // Display FPS counter and position of cursor
        SpriteBatch fpsBatch = new SpriteBatch();
        fpsBatch.setProjectionMatrix(camera.combined);
        fpsBatch.begin();
        font.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
        font.draw(fpsBatch, "X: " + Gdx.input.getX(), 10, 60);
        font.draw(fpsBatch, "Y: " + Gdx.input.getY(), 10, 45);
        fpsBatch.end();
        // Test drawing card from drawCard function
        for (int i = 0; i < cardOnScreenDatas.size(); i++) {
            drawCard(cardOnScreenDatas.get(i), camera);
        }

    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {     // isLeftButtonPressed starts out False
                isLeftButtonPressed = true; // This + assigning it true right after makes it so only one instance of a left-click is detected until the left click button is released.
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
