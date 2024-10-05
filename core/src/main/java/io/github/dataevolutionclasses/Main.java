/*
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
    // Viewport
    private OrthographicCamera camera;
    private FitViewport viewport;
    private boolean isLeftButtonPressed = false;
    // Card Storage
    private List<Card> cardList;
    private int cardListIndex = 0;
    // Render/Texture/Text
    private SpriteBatch batch;
    private Sprite cardCreatureSprite;
    private Sprite cardbackSprite;
    private BitmapFont font;
    private String nameText = "Creature name here";
    private String descText = "Left-click to scroll through cards.";
    private GlyphLayout descLayout;
    private String costText = "9";
    private String attackText = "6";
    private String shieldText = "3";
    private float cardScale = 1f;
    private float descWidth = 100*cardScale; // Specify the width for wrapping
    private float cardX, cardY, midcardX, midcardY;

    // Instantiated upon startup
    @Override
    public void create() {
        // Set up initial textures for the scene
        setInitialTextures();
        // Set up layouts
        descLayout = new GlyphLayout();
        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera); // 600x600 is the virtual world size
        viewport.apply();
        // Center the camera
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        // Read and generate cards
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
        // 2D imaging vars
        batch = new SpriteBatch();
        // Card back
        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * cardScale, cardbackTexture.getHeight() * cardScale);
        // Card creature
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
        // Font
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
        midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2); // Distance from the left edge to middle
        midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the bottom edge to middle
        // ----- Images -----
        // Card back
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch);
        // Card creature
        cardCreatureSprite.setPosition(cardX+(midcardX/3.5f), cardY+(midcardY/1.5f));
        cardCreatureSprite.draw(batch);
        // ----- Text -----
        // Name
        font.draw(batch, nameText, cardX+(midcardX*0.6f), cardY+(midcardY*1.82f));
        // Desc
        descLayout.setText(font, descText, Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f));
        // Cost
        font.draw(batch, costText, cardX+(midcardX*0.25f), cardY+(midcardY*1.82f));
        // Attack
        font.draw(batch, attackText, cardX+(midcardX*1.6f), cardY+(midcardY*0.5f));
        // Shield
        font.draw(batch, shieldText, cardX+(midcardX*1.6f), cardY+(midcardY*0.25f));
        batch.end();
    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {
                // The button was just pressed (first frame detection)
                isLeftButtonPressed = true;
                // Console logs
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                System.out.println(cardX + ", " + cardY + ", " + midcardX + ", " + midcardY);
                System.out.println(cardListIndex);
                // Change card vars
                Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
                cardCreatureSprite.set(new Sprite(cardCreatureTexture));
                cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
                descText = cardList.get(cardListIndex).getDesc();
                nameText = cardList.get(cardListIndex).getName();
                costText = Integer.toString(cardList.get(cardListIndex).getCost());
                attackText = Integer.toString(cardList.get(cardListIndex).getAttack());
                shieldText = Integer.toString(cardList.get(cardListIndex).getShield());
                // Increment card index to get new creature from cardList next click
                if (cardListIndex < cardList.size() - 1){
                    cardListIndex++;
                }
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }
}
