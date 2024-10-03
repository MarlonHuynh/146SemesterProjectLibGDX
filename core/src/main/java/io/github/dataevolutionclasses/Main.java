/*
    Class for Gameplay Scene
    Will be managed by SceneManager (TBD) in the future, but is currently used for testing UI and cards
*/

package io.github.dataevolutionclasses;

// Render
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
// Input
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
// Text
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
// Util
import java.util.List;

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
    private float descWidth = 250; // Specify the width for wrapping
    private String creatureText = "DefaultName";
    private String costText = "9";
    private String attackText = "6";
    private String shieldText = "3";

    // Instantiated upon startup
    @Override
    public void create() {
        // UI
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
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats.csv");
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
        Texture cardbackTexture = new Texture("cardback.png");
        cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * 1.8f, cardbackTexture.getHeight() * 1.8f);
        // Card creature
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * 2, cardCreatureSprite.getHeight() * 2);
        // Font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(1.0f);
    }
    // Called every frame in render to draw the screen
    public void draw(){
        // Clears screen
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // Draws batch of sprites
        batch.begin();
        float cardX = (viewport.getWorldWidth() / 2) - (cardbackSprite.getWidth() / 2);
        float cardY = (viewport.getWorldHeight() / 2) - (cardbackSprite.getHeight() / 2);
        // Cardback
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch);
        // Card creature
        cardCreatureSprite.setPosition(cardX + 50, cardY + 175);
        cardCreatureSprite.draw(batch);
        // ----- Text -------
        // Creature name
        font.draw(batch, nameText, viewport.getWorldWidth()/4+55, viewport.getWorldHeight()/2+235);
        // Desc
        descLayout.setText(font, descText, Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, viewport.getWorldWidth()/4, viewport.getWorldHeight()/4);
        // Cost
        font.draw(batch, costText, viewport.getWorldWidth()/4, viewport.getWorldHeight()/2+235);
        // Attack
        font.draw(batch, attackText, viewport.getWorldWidth()/2+125, viewport.getWorldHeight()/2-225);
        // Shield
        font.draw(batch, shieldText, viewport.getWorldWidth()/2+125, viewport.getWorldHeight()/2-150);
        batch.end();
    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {
                // The button was just pressed (first frame detection)
                isLeftButtonPressed = true;
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                // Change card vars
                Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
                cardCreatureSprite.set(new Sprite(cardCreatureTexture));
                cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * 2, cardCreatureSprite.getHeight() * 2);
                descText = cardList.get(cardListIndex).getDesc();
                nameText = cardList.get(cardListIndex).getName();
                costText = Integer.toString(cardList.get(cardListIndex).getCost());
                attackText = Integer.toString(cardList.get(cardListIndex).getAttack());
                shieldText = Integer.toString(cardList.get(cardListIndex).getShield());
                if (cardListIndex < cardList.size() - 1){ // Increment card index
                    cardListIndex++;
                }
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }
}
