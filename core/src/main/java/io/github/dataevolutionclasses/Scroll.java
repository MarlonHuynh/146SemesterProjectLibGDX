package io.github.dataevolutionclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Scroll {
    private Slider slider;
    private Skin skin;

    // Create the slider and add it to the stage
    public void createSlider(Stage stage) {
        // Create the skin programmatically
        skin = createSliderSkin();

        // Create the slider with range from 0 to 100 and step size of 1
        slider = new Slider(0, 100, 1, true, skin);
        slider.setPosition(0, 0);  // Set position on screen
        slider.setSize(10, 700);  // Set size of the slider
//Gdx.graphics.getHeight()
        // Add the slider to the stage for rendering
        stage.addActor(slider);
    }
    public float getSliderValue(){
        return slider.getValue();
    }

    // Helper method to create a basic slider skin programmatically
    private Skin createSliderSkin() {
        Skin skin = new Skin();

        // Create a pixmap for the slider background
        Pixmap pixmapBackground = new Pixmap(20, 690, Pixmap.Format.RGBA8888);
        pixmapBackground.setColor(Color.GRAY);
        pixmapBackground.fill();
        Texture backgroundTexture = new Texture(pixmapBackground);
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        // Create a pixmap for the slider knob
        Pixmap pixmapKnob = new Pixmap(20, 690, Pixmap.Format.RGBA8888);
        pixmapKnob.setColor(Color.WHITE);
        pixmapKnob.fillCircle(10, 10, 10);  // Create a circular knob
        Texture knobTexture = new Texture(pixmapKnob);
        Drawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knobTexture));

        // Dispose pixmaps after creating textures
        pixmapBackground.dispose();
        pixmapKnob.dispose();

        // Create SliderStyle and assign background and knob textures
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = backgroundDrawable;
        sliderStyle.knob = knobDrawable;

        // Add the style to the skin
        skin.add("default-vertical", sliderStyle);

        return skin;
    }

    // Dispose resources
    public void dispose() {
        skin.dispose();
    }
}
