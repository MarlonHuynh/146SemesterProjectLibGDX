/*
    Class for Gameplay Scene
    Will be managed by SceneManager (TBD) in the future, but is currently used for testing UI and cards
*/

package io.github.dataevolutionclasses;

// Render
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
// Input
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
// Text
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private String worldText = "Left-click to scroll through cards.";
    private String creatureText = "DefaultName";
    private String costText = "-1";
    private String attackText = "-1";
    private String shieldText = "-1";

    // Instantiated upon startup
    @Override
    public void create() {
        // Set up initial textures for the scene
        setInitialTextures();
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
        font = new BitmapFont();
        font.getData().setScale(2.0f);
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
        // Font
        font.draw(batch, worldText, viewport.getWorldWidth()/4, viewport.getWorldHeight()/4);
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
                Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
                cardCreatureSprite.set(new Sprite(cardCreatureTexture));
                cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * 2, cardCreatureSprite.getHeight() * 2);
                worldText = cardList.get(cardListIndex).getName();
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
