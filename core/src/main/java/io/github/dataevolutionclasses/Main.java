package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private boolean isLeftButtonPressed = false;
    private List<Card> cardList;
    private int cardListIndex = 0;
    private SpriteBatch batch;
    private Sprite cardCreatureSprite;
    private Sprite cardbackSprite;
    private float cardScale = 1f;
    private BitmapFont font;
    private String nameText = "Creature name here";
    private String descText = "Left-click to scroll through cards.";
    private String costText = "9";
    private String attackText = "6";
    private String shieldText = "3";
    private Stage stage;
    private Scroll scroll;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        scroll = new Scroll();
        scroll.createSlider(stage);

        camera = new OrthographicCamera();
        viewport = new FitViewport(600, 600, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();

        Texture cardbackTexture = new Texture("cardback2.png");
        cardbackSprite = new Sprite(cardbackTexture);

        Texture cardCreatureTexture = new Texture("hashmap.png");
        cardCreatureSprite = new Sprite(cardCreatureTexture);

        font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        font.getData().setScale(cardScale * 0.5f);
    }

    @Override
    public void render() {
        drawAll();
        manageInput();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        scroll.dispose();
    }

    public void drawAll() {
        for (int i = 0; i < cardList.size() && i < 35; i++) {
            if (i % 3 == 0) {
                drawCard(i, camera);
            } else if (i % 3 == 1) {
                drawCard(i, camera);
            } else if (i % 3 == 2) {
                drawCard(i, camera);
            }
        }
    }

    public void manageInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isLeftButtonPressed) {
                isLeftButtonPressed = true;
                updateCardAttributes();
                cardListIndex = (cardListIndex < cardList.size() - 1) ? cardListIndex + 1 : cardListIndex;
            }
        } else {
            isLeftButtonPressed = false;
        }
    }

    private void updateCardAttributes() {
        Texture cardCreatureTexture = cardList.get(cardListIndex).getTexture();
        cardCreatureSprite.set(new Sprite(cardCreatureTexture));
        cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * cardScale, cardCreatureSprite.getHeight() * cardScale);
        descText = cardList.get(cardListIndex).getDesc();
        nameText = cardList.get(cardListIndex).getName();
        costText = Integer.toString(cardList.get(cardListIndex).getCost());
        attackText = Integer.toString(cardList.get(cardListIndex).getAttack());
        shieldText = Integer.toString(cardList.get(cardListIndex).getShield());
    }

    public void drawCard(int index, OrthographicCamera camera) {
        float x = (index % 3) * 50 + 35;
        float y = (index / 3) * 100;
        float scale = 0.3f;

        // Card visuals
        cardbackSprite.setSize(cardbackSprite.getWidth() * scale, cardbackSprite.getHeight() * scale);
        cardCreatureSprite.setSize(cardCreatureSprite.getWidth() * scale, cardCreatureSprite.getHeight() * scale);

        // Start batch
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float cardX = x - (cardbackSprite.getWidth() / 2);
        float cardY = y - (cardbackSprite.getHeight() / 2);
        float midcardX = cardbackSprite.getWidth() / 2;
        float midcardY = cardbackSprite.getHeight() / 2;

        cardbackSprite.setPosition(cardX, cardY);
        cardbackSprite.draw(batch);

        cardCreatureSprite.setPosition(cardX + (midcardX / 3.5f), cardY + (midcardY / 1.5f));
        cardCreatureSprite.draw(batch);

        font.draw(batch, nameText, cardX + (midcardX * 0.6f), cardY + (midcardY * 1.82f));
        GlyphLayout descLayout = new GlyphLayout();
        descLayout.setText(font, descText, Color.BLACK, 100 * scale, Align.left, true);
        font.draw(batch, descLayout, cardX + (midcardX * 0.25f), cardY + (midcardY * 0.5f));
        font.draw(batch, costText, cardX + (midcardX * 0.25f), cardY + (midcardY * 1.82f));
        font.draw(batch, attackText, cardX + (midcardX * 1.6f), cardY + (midcardY * 0.5f));
        font.draw(batch, shieldText, cardX + (midcardX * 1.6f), cardY + (midcardY * 0.25f));

        batch.end();
        camera.update();
    }
}
