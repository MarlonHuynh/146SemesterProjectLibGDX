package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager extends Game implements ApplicationListener {
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use default Arial font
        this.setScreen(new Title(this)); // Set initial screen to Title
    }

    public void render() {
        super.render(); // important!!!
    }

    public void dispose(){
        batch.dispose();
        font.dispose();
    }

}
