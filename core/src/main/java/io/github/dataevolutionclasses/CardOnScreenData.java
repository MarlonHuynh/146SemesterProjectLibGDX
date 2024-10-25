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
    private float x, y;
    private float scale;

    public CardOnScreenData(Card card, float x, float y, float scale){
        this.card = card;
        this.cardSprite = new Sprite(card.getTexture());
        this.cardbackSprite = new Sprite(new Texture("cardback2.png"));
        this.x = x;
        this.y = y;
        this.scale = scale;
        System.out.println("COSD: " + Float.toString(x) + ", " + Float.toString(y) + ", Scale: " + Float.toString(scale));
    }
    public Sprite getSprite(){
        return cardSprite;
    }
    public Card getCard(){
        return card;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getScale(){
        return scale;
    }
    public Sprite getBackSprite(){
        return cardbackSprite;
    }
    public void setSprite(Sprite newSprite){
        cardSprite = newSprite;
    }


}
