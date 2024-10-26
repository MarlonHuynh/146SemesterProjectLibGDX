/*
    Handles information about a card on the screen
    card - the card to be displayed on screen
    cardSprite - the Sprite currently being displayed on the screen
    cardbackSprite - the cardback's Sprite

    Why?
    - Card class can only store information about a card and cannot display on its own
    - By grouping the cardSprite (which is what we actually render) and card class together
    - we can group all card information together coehesively.
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import java.awt.*;

public class CardOnScreenData {

    private Card card;
    private Sprite cardSprite;
    private Sprite cardbackSprite;
    private Sprite selectedSprite;
    private float x, y;
    private float scale;
    //
    private BitmapFont nameFont;
    private float nameX;
    private float nameY;
    private float descWidth;
    private GlyphLayout descLayout;
    private float descX;
    private float descY;
    private BitmapFont numberFont;
    private float costTextX;
    private float costTextY;
    private float attackTextX;
    private float attackTextY;
    private float shieldTextX;
    private float shieldTextY;

    public CardOnScreenData(Card card, float x, float y, float scale){
        this.card = card;
        this.cardSprite = new Sprite(card.getTexture());
        this.cardbackSprite = new Sprite(new Texture("cardback2.png"));
        if (card.getName().equals("Draw")){
            this.cardbackSprite = new Sprite(new Texture("cardback3.png"));
        }
        else if (card.getName().equals("Trash")){
            this.cardbackSprite = new Sprite(new Texture("cardback4.png"));
        }
        this.selectedSprite = new Sprite(new Texture("CardbackHighlight.png"));
        this.x = x;
        this.y = y;
        this.scale = scale;
        // Set card sizing and position (doesn't directly draw)
        selectedSprite.setSize(selectedSprite.getTexture().getWidth() * scale, selectedSprite.getTexture().getHeight() * scale);
        cardbackSprite.setSize(cardbackSprite.getTexture().getWidth() * scale, cardbackSprite.getTexture().getHeight() * scale);
        cardSprite.setSize(cardSprite.getTexture().getWidth() * scale, cardSprite.getTexture().getHeight() * scale);
        float cardX = (x - ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2)); // Bottom left corner x
        float cardY = (y - ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2)); // Bottom left corner y
        float midcardX = ((cardbackSprite.getWidth() * cardbackSprite.getScaleX()) / 2);  // Distance from the card's left edge to middle
        float midcardY = ((cardbackSprite.getHeight() * cardbackSprite.getScaleY()) / 2); // Distance from the card's bottom edge to middle
        cardbackSprite.setPosition(cardX, cardY);
        selectedSprite.setPosition(cardX - (midcardX * 0.1f), cardY - (midcardY * 0.06f));
        cardSprite.setPosition(cardX + (midcardX / 3.5f), cardY + (midcardY / 1.3f));

        // Set text sizing and position (doesn't directly draw)
        nameFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        nameFont.getData().setScale(scale * 0.75f);
        nameX =  cardX + (midcardX * 0.16f);
        nameY = cardY + (midcardY * 1.88f);
        descWidth = 130 * scale;
        descLayout = new GlyphLayout();
        descLayout.setText(nameFont, card.getDesc(), Color.BLACK, 130 * scale, Align.left, true);
        descX = cardX + (midcardX * 0.16f);
        descY = cardY + (midcardY * 0.54f);
        numberFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
        numberFont.getData().setScale(scale * 0.9f);
        costTextX = cardX + (midcardX * 0.2f);
        costTextY =  cardY + (midcardY * 1.62f);
        attackTextX = cardX + (midcardX * 1.64f);
        attackTextY =  cardY + (midcardY * 0.56f);
        shieldTextX = cardX + (midcardX * 1.64f);
        shieldTextY =  cardY + (midcardY * 0.22f);
        // Debug
        // System.out.println("COSD: " + Float.toString(x) + ", " + Float.toString(y) + ", Scale: " + Float.toString(scale));
    }
    // Getters and Setters
    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }

    public Sprite getCardSprite() { return cardSprite; }
    public void setCardSprite(Sprite cardSprite) { this.cardSprite = cardSprite; }

    public Sprite getSelectedSprite() { return selectedSprite; }
    public void setSelectedSprite(Sprite selectedSprite) {this.selectedSprite = selectedSprite; }

    public Sprite getCardbackSprite() { return cardbackSprite; }
    public void setCardbackSprite(Sprite cardbackSprite) { this.cardbackSprite = cardbackSprite; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }

    public BitmapFont getNameFont() { return nameFont; }
    public void setNameFont(BitmapFont nameFont) { this.nameFont = nameFont; }

    public float getNameX() { return nameX; }
    public void setNameX(float nameX) { this.nameX = nameX; }

    public float getNameY() { return nameY; }
    public void setNameY(float nameY) { this.nameY = nameY; }

    public float getDescWidth() { return descWidth; }
    public void setDescWidth(float descWidth) { this.descWidth = descWidth; }

    public GlyphLayout getDescLayout() { return descLayout; }
    public void setDescLayout(GlyphLayout descLayout) { this.descLayout = descLayout; }

    public float getDescX() { return descX; }
    public void setDescX(float descX) { this.descX = descX; }

    public float getDescY() { return descY; }
    public void setDescY(float descY) { this.descY = descY; }

    public BitmapFont getNumberFont() { return numberFont; }
    public void setNumberFont(BitmapFont numberFont) { this.numberFont = numberFont; }

    public float getCostTextX() { return costTextX; }
    public void setCostTextX(float costTextX) { this.costTextX = costTextX; }

    public float getCostTextY() { return costTextY; }
    public void setCostTextY(float costTextY) { this.costTextY = costTextY; }

    public float getAttackTextX() { return attackTextX; }
    public void setAttackTextX(float attackTextX) { this.attackTextX = attackTextX; }

    public float getAttackTextY() { return attackTextY; }
    public void setAttackTextY(float attackTextY) { this.attackTextY = attackTextY; }

    public float getShieldTextX() { return shieldTextX; }
    public void setShieldTextX(float shieldTextX) { this.shieldTextX = shieldTextX; }

    public float getShieldTextY() { return shieldTextY; }
    public void setShieldTextY(float shieldTextY) { this.shieldTextY = shieldTextY; }
}
