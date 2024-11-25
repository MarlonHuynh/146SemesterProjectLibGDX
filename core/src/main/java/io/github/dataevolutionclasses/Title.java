/*
    Manages Game's scenes and changing of scenes
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;
/**
 * The Title screen displays the main menu of the game, allowing the player
 * to navigate to other screens such as gameplay, library, help, or exit the game.
 *
 * <p>This screen includes a title, menu buttons, and sound effects for button clicks.</p>
 */
public class Title extends ScreenAdapter {
    private static boolean introPlayed = false;
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Spr
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, playBtn, libBtn, helpBtn, creditsBtn, exitBtn, titleSpr, mixSpr, mix2Spr;
    private Sprite hawkSpr, mantaSpr, bugSpr, fishSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    private Sound buttonSound;
    private Music menuBackMusic;
    private Animation<TextureRegion> intro;
    private Array<TextureRegion> animationFrames = new Array<>();
    // Vars
    private boolean dropSecondBg = false;
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();
    private float time; // Keeps track of elapsed time
    // Game
    private Game game;
    public Title(Game game) {
        this.game = game;
    }
    /**
     * Initializes the Title screen by setting up the camera, viewport, and input processor,
     * and loading textures for the background, buttons, title, and other elements.
     */

    public void show() {
        // Set up camera, viewport, and input processor
        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                       // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(300, 300, 0);  // Center at 600x600 middle
        camera.update();
        // Sprite
        spriteBatch = new SpriteBatch();
        bgSpr = new Sprite(new Texture("bg_lightgreen.png"));
        titleSpr = new Sprite(new Texture("background.png"));
        titleSpr = new Sprite(new Texture("Title.png"));
        titleSpr.setPosition(140, 350);

        // Btn Spr
        playBtn = new Sprite(new Texture("btn_play.png"));
        playBtn.setPosition(200, 270);
        libBtn = new Sprite(new Texture("btn_lib.png"));
        libBtn.setPosition(200, 210);
        helpBtn = new Sprite(new Texture("btn_help.png"));
        helpBtn.setPosition(200, 150);
        creditsBtn = new Sprite(new Texture("btn_credits.png"));
        creditsBtn.setPosition(200, 90);
        exitBtn = new Sprite(new Texture("btn_exit.png"));
        exitBtn.setPosition(200, 30);

        hawkSpr = new Sprite(new Texture("titlehawk.png"));
        hawkSpr.setScale(1.1f);
        fishSpr = new Sprite(new Texture("titlefish.png"));
        fishSpr.setScale(1.1f);
        bugSpr = new Sprite(new Texture("titlebug.png"));
        bugSpr.setScale(1.1f);
        mantaSpr = new Sprite(new Texture("titlemanta.png"));
        mantaSpr.setScale(1.1f);

        mixSpr = new Sprite(new Texture("titlemix.png"));
        mixSpr.setPosition(-20,0);
        mixSpr.setColor(1, 1, 1, 0.2f);
        mix2Spr = new Sprite(new Texture("titlemix.png"));
        mix2Spr.setPosition(-20, 768);
        mix2Spr.setColor(1, 1, 1, 0.2f);

        buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));
        menuBackMusic = Gdx.audio.newMusic(Gdx.files.internal("happy-thoughtful-song-SUNRIZISH.mp3"));
        menuBackMusic.setVolume(0.5f);
        menuBackMusic.setLooping(true);
        menuBackMusic.play();
        // Add buttons to list
        btnList.add(playBtn);
        btnList.add(libBtn);
        btnList.add(helpBtn);
        btnList.add(creditsBtn);
        btnList.add(exitBtn);

        // Load all frames from the folder
        animationFrames = new Array<>();
        int frameCount = 72; // Replace with the actual number of frames in the folder
        for (int i = 1; i <= frameCount; i++) {
            // Load each frame sequentially
            Texture frameTexture = new Texture(Gdx.files.internal("birdanims/bird-" + i + ".png"));
            animationFrames.add(new TextureRegion(frameTexture));
        }
        intro = new Animation<>(0.1f, animationFrames, Animation.PlayMode.NORMAL);
        // make Inp processor
        createInputProcessor();
    }
    /**
     * Renders the Title screen and updates visuals for each frame.
     *
     * @param delta the time elapsed since the last frame in seconds
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }
    /**
     * Draws the title screen, including background, buttons, and title sprite.
     */
    public void draw() {
        // Update the time
        ScreenUtils.clear(245 / 255f, 1250 / 255f, 205 / 255f, 1f);
        time += Gdx.graphics.getDeltaTime();
        camera.position.set(300, 300, 0);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Draw all title stuff
        bgSpr.draw(spriteBatch);
        mixSpr.draw(spriteBatch);
        mix2Spr.draw(spriteBatch);
        bugSpr.draw(spriteBatch);
        fishSpr.draw(spriteBatch);
        hawkSpr.draw(spriteBatch);
        mantaSpr.draw(spriteBatch);
        playBtn.draw(spriteBatch);
        libBtn.draw(spriteBatch);
        helpBtn.draw(spriteBatch);
        creditsBtn.draw(spriteBatch);
        exitBtn.draw(spriteBatch);
        titleSpr.draw(spriteBatch);
        // Oscillate positions using sine wave
        float offsetX = (float) Math.sin(time * 2) * 3; // Adjust speed and amplitude
        float offsetY = (float) Math.cos(time * 2) * 3;
        float angle = (float) Math.sin(time * 2) * 1; // Oscillate between -1 and +1 degrees
        // Update sprite position
        hawkSpr.setPosition(0 + offsetX, 0 + offsetY);
        bugSpr.setRotation(angle);
        mantaSpr.setPosition(0, 0 - offsetY / 2);
        fishSpr.setPosition(5, 0 - offsetY);
        titleSpr.setPosition(140, 350 - offsetY / 4);
        // Update sprite positions
        float delta = Gdx.graphics.getDeltaTime();
        // Update and loop mixSpr position
        mixSpr.setY(mixSpr.getY() - delta * 10); // Move mixSpr at a constant speed (10 pixels/second)
        if (mixSpr.getY() + mixSpr.getWidth() < 0) { // Check if mixSpr is off-screen
            mixSpr.setY(600); // Reset to the right of the screen
        }
        mix2Spr.setY(mix2Spr.getY() - delta * 10);
        if (mix2Spr.getY() + mix2Spr.getWidth() < 0) {
            mix2Spr.setY(600);
        }
        spriteBatch.end();
        // Draw intro (with title stuff loaded behind)
        if (!introPlayed) {
            spriteBatch.begin();
            if (!dropSecondBg) {
                bgSpr.draw(spriteBatch);
            }
            if (intro != null) {
                TextureRegion currentFrame = intro.getKeyFrame(time, false); // Looping animation
                spriteBatch.draw(currentFrame, 0, 0); // Draw animation at position (100, 100)
            }
            spriteBatch.end();
            if (intro.getKeyFrameIndex(time) == 68) {
                dropSecondBg = true;
            }
            if (intro.isAnimationFinished(time)) {
                introPlayed = true;
            }
        }
    }
    /**
     * Handles resizing of the viewport and adjusts it to maintain aspect ratio.
     *
     * @param width  the new width of the viewport
     * @param height the new height of the viewport
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        menuBackMusic.dispose();
    }

    /**
     * Sets up an input processor for touch events, handling user clicks on the screen.
     * Detects touches within the viewport and triggers appropriate actions based on
     * which button was clicked (e.g., starting gameplay, navigating to library or help, or exiting).
     */
    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Get World Coords
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                // Reduce transparency of card if clicked down
                for (int i = 0; i < btnList.size(); i++) {
                    if (btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        clicked = true;
                        btnList.get(i).setColor(1, 1, 1, 0.8f);
                    }
                }
                return clicked;
            }
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                int indexClicked = -1;
                clicked = false;
                // Get World Coords
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                // Reset all buttons to full alpha
                for (int i = 0; i < btnList.size(); i++) {
                        btnList.get(i).setColor(1, 1, 1, 1f);
                }
                // Check which btn was clicked
                for (int i = 0; i < btnList.size(); i++) {
                    if (btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        clicked = true;
                        indexClicked = i;
                    }
                }
                // Perform action based on btn clicked
                if (indexClicked == 0){
                    playButtonSound();
                    game.setScreen(new Gameplay(game));
                    dispose();
                }
                else if (indexClicked == 1){
                    playButtonSound();
                    game.setScreen(new Library(game));
                    dispose();
                }
                else if (indexClicked == 2){
                    playButtonSound();
                    game.setScreen(new Help(game));
                    dispose();
                }
                else if (indexClicked == 3){
                    playButtonSound();
                    game.setScreen(new Credits(game));
                    dispose();
                }
                else if (indexClicked == 4){
                    Gdx.app.exit();
                }

                return clicked;
            }
        });
    }
    /**
     * Plays the button click sound effect at a reduced volume.
     */
    public void playButtonSound(){
        buttonSound.play(0.3f);
    }
}
