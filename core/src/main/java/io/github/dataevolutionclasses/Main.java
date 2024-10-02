package io.github.dataevolutionclasses;

// Render
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
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
    private SpriteBatch batch;
    private Texture image;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private boolean isLeftButtonPressed = false;
    private List<Card> cardList;
    private int cardListIndex = 0;
    private BitmapFont font;
    private String worldText = "Left-click to scroll through cards.";

    // Instantiated upon startup
    @Override
    public void create() {
        // 2D imaging vars
        batch = new SpriteBatch();
        image = new Texture("cardsprites/balancedbinarytree.png");
        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera); // 600x600 is the virtual world size
        viewport.apply();
        // Center the camera
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        // Cards
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        // Font
        font = new BitmapFont();
        font.getData().setScale(2.0f);
    }

    // Called every refresh rate for rendering
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, viewport.getWorldWidth()/4, viewport.getWorldHeight()/4, viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        font.draw(batch, worldText, viewport.getWorldWidth()/4, viewport.getWorldHeight()/4);
        batch.end();
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {
                // The button was just pressed (first frame detection)
                isLeftButtonPressed = true;
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                image = cardList.get(cardListIndex).getTexture();
                worldText = cardList.get(cardListIndex).getName();
                if (cardListIndex < cardList.size() - 1){
                    cardListIndex++;
                }
            }
        } else {
            // Reset the flag when the button is released
            isLeftButtonPressed = false;
        }
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
        image.dispose();
    }
}
