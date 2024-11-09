package io.github.dataevolutionclasses;

import com.badlogic.gdx.Game;

public class GameManager extends Game {
    @Override
    public void create() {
        setScreen(new Title(this)); // Set initial screen to Title
    }
}
