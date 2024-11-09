/*
    Manages Game's scenes and changing of scenes
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.*;
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
    private Sprite bgSpr, playBtn, libBtn, helpBtn, exitBtn, titleSpr;
    private ArrayList<Sprite> btnList = new ArrayList<>();

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
        bgSpr = new Sprite(new Texture("background.png"));
        playBtn = new Sprite(new Texture("btn_play.png"));
        playBtn.setPosition(200, 400);
        libBtn = new Sprite(new Texture("btn_lib.png"));
        libBtn.setPosition(200, 300);
        helpBtn = new Sprite(new Texture("btn_help.png"));
        helpBtn.setPosition(200, 200);
        exitBtn = new Sprite(new Texture("btn_exit.png"));
        exitBtn.setPosition(200, 100);
        titleSpr = new Sprite(new Texture("background.png"));
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
        playBtn.draw(spriteBatch);
        libBtn.draw(spriteBatch);
        helpBtn.draw(spriteBatch);
        exitBtn.draw(spriteBatch);

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
                    game.setScreen(new Gameplay(game));
                }
                else if (indexClicked == 1){
                    game.setScreen(new DeckList(game));
                }
                else if (indexClicked == 2){

                }
                else if (indexClicked == 3){

                }

                return clicked;
            }
        });
    }
}
