package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ButtonTest extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private TextButton button;

    private Game game;
    public ButtonTest(Game game) {
        this.game = game;
    }


    public void create() {
        // Create the stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the skin from assets (uiskin.json should be in the assets folder)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create a button with the label "Click Me"
        button = new TextButton("Click Me", skin);

        // Position the button in the center of the screen
        button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);

        // Add a listener to handle click events
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked!");
            }
        });

        // Add the button to the stage
        stage.addActor(button);
    }
    
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        // Dispose of assets
        stage.dispose();
        skin.dispose();
    }
}

