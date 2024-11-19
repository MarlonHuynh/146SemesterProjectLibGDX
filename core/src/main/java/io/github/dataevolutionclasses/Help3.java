package io.github.dataevolutionclasses;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Help3 extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private SpriteBatch spriteBatch;
    private Sprite backSpr, prevSpr, nextSpr, HelpPage3;       // Sprites for background and help elements

    //State variables for input and screen interaction
    private boolean clicked = false;    //Tracks if the screen was clicked
    private Vector3 worldCoords = new Vector3();    //Stores coordinates in world space

    // button sound effect
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));
    private Sound pageSound = Gdx.audio.newSound(Gdx.files.internal("turnPageSound.mp3"));

    // Game instance to manage screen transitions
    private Game game;
    /**
     * Constructor for Help screen.
     *
     * @param game the Game instance used to manage screens.
     */
    public Help3(Game game) {
        this.game = game;
    }

    /**
     * Initializes the Help screen by setting up the camera, viewport, and input processor,
     * and loading textures for the sprites.
     */


    public void show() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(300, 300, 0);  // Center at 600x600 middle
        camera.update();
        createInputProcessor();


        // Sprite
        spriteBatch = new SpriteBatch();
        backSpr = new Sprite(new Texture("btn_back.png"));
        prevSpr = new Sprite(new Texture("btn_prevpage.png"));
        nextSpr = new Sprite(new Texture("btn_nextpage.png"));
        HelpPage3 = new Sprite(new Texture("Help3.jpg"));

        // Set up sprite properties
        HelpPage3.setSize(600, 600); // Help page fills the viewport
        backSpr.setScale(0.5f);         // Scale down the back button
        backSpr.setPosition(-30, 550);  // Position the back button
        prevSpr.setScale(0.5f);
        prevSpr.setPosition(-30, 10);
        nextSpr.setScale(0.5f);
        nextSpr.setPosition(430, 10);
    }

    /**
     * Renders the Help screen and updates visuals for each frame.
     *
     * @param delta the time elapsed since the last frame in seconds.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    /**
     * Draws the help screen, including the background, help page, and back button.
     */

    public void draw() {
        ScreenUtils.clear(245 / 255f, 1250 / 255f, 205 / 255f, 1f); // Clear the screen with a color
        camera.position.set(300, 300, 0); // Recenter camera on resize
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        HelpPage3.draw(spriteBatch);
        backSpr.draw(spriteBatch);
        prevSpr.draw(spriteBatch);
        nextSpr.draw(spriteBatch);

        spriteBatch.end();
    }
    /**
     * Handles resizing of the viewport and adjusts it to maintain aspect ratio.
     *
     * @param width the new width of the viewport.
     * @param height the new height of the viewport.
     */

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Releases resources when this screen is no longer needed.
     */
    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
    /**
     * Sets up an input processor for touch events, handling user clicks on the screen.
     * Detects touches within the viewport and triggers a screen change if the back button is pressed.
     */
    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));  // Convert screen coordinates to world coordinates
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600) // Check if the click is within the viewport
                    return false;

                // If back button is clicked, play sound and return to the Title screen
                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    buttonSound.play();
                    game.setScreen(new Title(game));
                    dispose();
                }

                // if prev button is clicked
                if (prevSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    pageSound.play();
                    game.setScreen(new Help2(game));
                    dispose();
                }

                // if next button is clicked
                if (nextSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    pageSound.play();
                    game.setScreen(new Help4(game));
                    dispose();
                }
                return clicked;
            }
        });
    }
}
