package io.github.dataevolutionclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class Scroll {
    private Slider slider;
    private Skin skin;

    // Create the slider and add it to the stage
    public void createSlider(Stage stage) {
        // Create the skin programmatically
        skin = createSliderSkin();

        // Create the slider with range from 0 to 100 and step size of 1
        slider = new Slider(0, 100, 1, false, skin);
        slider.setPosition(50, Gdx.graphics.getHeight() / 2);  // Set position on screen
        slider.setSize(200, 50);  // Set size of the slider

        // Add the slider to the stage for rendering
        stage.addActor(slider);
    }

    // Helper method to create a basic slider skin programmatically
    private Skin createSliderSkin() {
        Skin skin = new Skin();

        // Create a pixmap for the slider background
        Pixmap pixmap = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        Texture backgroundTexture = new Texture(pixmap);
        skin.add("slider_background", new TextureRegionDrawable(new TextureRegion(backgroundTexture)));

        // Create a pixmap for the slider knob
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(10, 10, 10);  // Create a circular knob
        Texture knobTexture = new Texture(pixmap);
        skin.add("slider_knob", new TextureRegionDrawable(new TextureRegion(knobTexture)));

        // Dispose the pixmap after creating textures
        pixmap.dispose();

        // Create SliderStyle and assign background and knob textures
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable("slider_background");
        sliderStyle.knob = skin.getDrawable("slider_knob");

        // Add the style to the skin
        skin.add("default-horizontal", sliderStyle);

        return skin;
    }

    // Dispose resources
    public void dispose() {
        skin.dispose();
    }
}
