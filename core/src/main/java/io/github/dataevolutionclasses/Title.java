/*
    Manages Game's scenes and changing of scenes
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class Title extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    // Spr
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, playBtn, libBtn, helpBtn, exitBtn, titleSpr, creaturesSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();
    private Sound buttonSound;

    //
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();

    private Game game;
    public Title(Game game) {
        this.game = game;
    }


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
        playBtn = new Sprite(new Texture("btn_play.png"));
        playBtn.setPosition(200, 260);
        libBtn = new Sprite(new Texture("btn_lib.png"));
        libBtn.setPosition(200, 190);
        helpBtn = new Sprite(new Texture("btn_help.png"));
        helpBtn.setPosition(200, 120);
        exitBtn = new Sprite(new Texture("btn_exit.png"));
        exitBtn.setPosition(200, 50);
        titleSpr = new Sprite(new Texture("background.png"));
        titleSpr = new Sprite(new Texture("Title.png"));
        titleSpr.setPosition(140, 350);
        creaturesSpr = new Sprite(new Texture("creatures.png"));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));


        // Add buttons to list
        btnList.add(playBtn);
        btnList.add(libBtn);
        btnList.add(helpBtn);
        btnList.add(exitBtn);
        // make Inp processor
        createInputProcessor();
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    public void draw() {
        ScreenUtils.clear(245 / 255f, 1250 / 255f, 205 / 255f, 1f);
        camera.position.set(300, 300, 0); // Recenter camera on resize
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        bgSpr.draw(spriteBatch);
        creaturesSpr.draw(spriteBatch);
        playBtn.draw(spriteBatch);
        libBtn.draw(spriteBatch);
        helpBtn.draw(spriteBatch);
        exitBtn.draw(spriteBatch);
        titleSpr.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // Called when exiting
    @Override
    public void dispose() {

    }


    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int indexClicked = -1;
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;
                // ---------- Check which card was clicked ----------
                // Convert screen coordinates to world coordinates
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                for (int i = 0; i < btnList.size(); i++) {
                    if (btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y) || btnList.get(i).getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                        clicked = true;
                        indexClicked = i;
                    }
                }
                if (indexClicked == 0){ // Play
                    playButtonSound();
                    game.setScreen(new Gameplay(game));
                }
                else if (indexClicked == 1){
                    playButtonSound();
                    game.setScreen(new LibraryTemp(game));
                }
                else if (indexClicked == 2){
                    playButtonSound();
                    game.setScreen(new Help(game));
                }
                else if (indexClicked == 3){
                    Gdx.app.exit();
                }

                return clicked;
            }
        });
    }

    public void playButtonSound(){
        buttonSound.play(0.3f);
    }
}
