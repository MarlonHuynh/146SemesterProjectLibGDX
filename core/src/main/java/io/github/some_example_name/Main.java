package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private OrthographicCamera camera;
    private FitViewport viewport;

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
    }

    // Called every refresh rate for rendering
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, viewport.getWorldWidth()/4, viewport.getWorldHeight()/4, viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        batch.end();
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
