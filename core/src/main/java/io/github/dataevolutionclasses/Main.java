package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter; // Rendering
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx; // Input
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont; // Text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.List; // Util

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;              // Camera
    private FitViewport viewport;                   // Viewport
    private boolean isLeftButtonPressed = false;    // Input
    private List<Card> cardList;                    // Card Storage
    private int cardListIndex = 0;                  // Index to cycle through Card list
    private SpriteBatch batch;                      // SpriteBatch combine multiple Sprites into a texture to be drawn at runtime
    private Sprite cardCreatureSprite;              // Sprite for card creature
    private Sprite cardbackSprite;                  // Sprite for card back
    private float cardScale = 1f;                   // Card scale (affects all the other text placements according to the card size)
    private BitmapFont font;                        // Font
    private String nameText = "Creature name here"; // Name text
    private String descText = "Left-click to scroll through cards.";    // Description text
    private String costText = "9";                  // Cost text
    private String attackText = "6";                // Attack text
    private String shieldText = "3";                // Shield text

    private Stage stage;

    private Scroll scroll;

    @Override
    public void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        scroll = new Scroll();
        scroll.createSlider(stage); //Add the slider into the stage

        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);                   // 600x600 is the virtual world size
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
        // Read and generate cards and place cards into Card list
        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();
        // Initial startup card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);
        // Initial startup card creature image
        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);
        // Initialize startup Font
        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.5f);
    }

    // Called every refresh rate for rendering
    @Override
    public void render() {
        drawAll();
        manageInput();
        stage.act(); //Process any UI events
        stage.draw(); //Draw the stage (including the slider)
    }

    // Called when resizing window
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true); //Upadte the stage viewport
    }

    // Called when exiting
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        scroll.dispose();
    }

    public void drawAll(){
        // Clears screen and prepares batch for drawing
        for (int i = 0; i < cardList.size() && i < 35; i++) {
//            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//            SpriteBatch fpsBatch = new SpriteBatch();
//            fpsBatch.setProjectionMatrix(camera.combined);
//            fpsBatch.begin();
//            font.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); // Display FPS in bottom-left corner
//            fpsBatch.end();
            if (i % 3 == 0){
                //drawCard(35, 50, 0.3f, cardList.get(i), camera);
                drawCard(45, (i / 3)*100 + 50 /*0/3 = 0*//*3/3 = 1*/, 0.3f, cardList.get(i), camera);
            } else if (i%3 == 1) {
                drawCard(110, (i / 3)*100 + 50 /*1/3 = 0*//*4/3 = 1*/, 0.3f, cardList.get(i), camera);
            } else if (i%3 == 2) {
                drawCard(175, (i / 3)*100 + 50/*2/3 = 0*//*5/3 = 1*/, 0.3f, cardList.get(i), camera);
            }
            //drawCard((i % 3)*50+35, (i/3)*100, 0.3f, cardList.get(i), camera);

        }
//        drawCard(viewport.getWorldWidth()*(3/4f), viewport.getWorldHeight()/2, 1f);
    }
    public void manageInput(){
        // Check for left mouse button click
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {     // isLeftButtonPressed starts out False
                isLeftButtonPressed = true; // This + assigning it true right after makes it so only one instance of a left-click is detected until the left click button is released.
                // Change card vars to match attributes of the Card at cardList[cardListIndex]
                Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
                cardCreatureSprite.set(new Sprite(cardCreatureTexture));
                cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
                descText = cardList.get(cardListIndex).getDesc();
                nameText = cardList.get(cardListIndex).getName();
                costText = Integer.toString(cardList.get(cardListIndex).getCost());
                attackText = Integer.toString(cardList.get(cardListIndex).getAttack());
                shieldText = Integer.toString(cardList.get(cardListIndex).getShield());
                // Increment card index to get new creature in cardList
                if (cardListIndex < cardList.size() - 1){
                    cardListIndex++;
                }
                // Console logs for debugging purposes
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                System.out.println("Left mouse button clicked at (" + x + ", " + y + ")");
                System.out.println(cardListIndex);
            }
        } else {
            // Reset the flag when the button is released to prevent multiple left clicks being detected per frame
            isLeftButtonPressed = false;
        }
    }
    public void drawCard(float x, float y, float scale, Card card, OrthographicCamera camera){
        // Initial card back image
        Texture cardbackTexture = new Texture("cardback2.png");
        Sprite cardbackSprite = new Sprite(cardbackTexture);
        cardbackSprite.setSize(cardbackTexture.getWidth() * scale, cardbackTexture.getHeight() * scale);
        // Initial card creature image
        Texture cardCreatureTexture = card.getTexture();
        Sprite cardCreatureSprite = new Sprite(cardCreatureTexture);
        cardCreatureSprite.setSize(cardCreatureSprite.getTexture().getWidth() * scale, cardCreatureSprite.getTexture().getHeight() * scale);
        // Set Font
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(scale * 0.5f);
        // Create batch of sprite to be drawn
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Find position of card
        // Note: DO NOT USE PIXELS to get your position. Use relative sizing.
        float cardX = (x - ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2)); // Bottom left corner x
        float cardY = (y - ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2)); // Bottom left corner y
        float midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2);  // Distance from the card's left edge to middle
        float midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the card's bottom edge to middle
        // ----- Images -----
        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch); // Draw card back
        cardCreatureSprite.setPosition(cardX+(midcardX/3.5f), cardY+(midcardY/1.5f));
        cardCreatureSprite.draw(batch); // Draw card creature
        // ----- Text -----
        font.draw(batch, card.getName(), cardX+(midcardX*0.6f), cardY+(midcardY*1.82f)); // Draw name text
        GlyphLayout descLayout = new GlyphLayout();
        float descWidth = 100 * scale;
        descLayout.setText(font, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
        font.draw(batch, descLayout, cardX+(midcardX*0.25f), cardY+(midcardY*0.5f)); // Draw description
        font.draw(batch, Integer.toString(card.getCost()), cardX+(midcardX*0.25f), cardY+(midcardY*1.82f)); // Draw cost text
        font.draw(batch, Integer.toString(card.getAttack()), cardX+(midcardX*1.6f), cardY+(midcardY*0.5f)); // Draw attack text
        font.draw(batch, Integer.toString(card.getShield()), cardX+(midcardX*1.6f), cardY+(midcardY*0.25f)); // Draw shield text
        // End batch and update camera frame
        batch.end();
        camera.update();
    }
}
