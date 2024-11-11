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


public class Help extends ScreenAdapter {
    // Window vars
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private SpriteBatch spriteBatch;
    private Sprite bgSpr, backSpr, HelpPage1;

    //
    private boolean clicked = false;
    private Vector3 worldCoords = new Vector3();

    // button sound effect
    private Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal("buttonSound.mp3"));

    //
    private Game game;
    public Help(Game game) {
        this.game = game;
    }


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
        bgSpr = new Sprite(new Texture("background.png"));
        backSpr = new Sprite(new Texture("btn_back.png"));
        HelpPage1 = new Sprite(new Texture("Help1.jpg"));
        HelpPage1.setSize(600, 600);
        backSpr.setScale(0.5f);
        backSpr.setPosition(-30, 550);
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

        //bgSpr.draw(spriteBatch);
        HelpPage1.draw(spriteBatch);
        backSpr.draw(spriteBatch);



        spriteBatch.end();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {

    }

    public void createInputProcessor(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                clicked = false;
                if (worldCoords.x < 0 || worldCoords.x > 600 || worldCoords.y < 0 || worldCoords.y > 600)
                    return false;

                if (backSpr.getBoundingRectangle().contains(worldCoords.x, worldCoords.y)) {
                    buttonSound.play();
                    game.setScreen(new Title(game));
                }

                return clicked;
            }
        });
    }
}
