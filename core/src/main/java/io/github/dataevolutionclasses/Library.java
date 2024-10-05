package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.List;
public class Library extends ApplicationAdapter {
    private SpriteBatch batch;                  // Batch for drawing
    private BitmapFont font;                    // Font for text
    private List<Card> cardList;                // List of cards to display
    private float miniCardScale = 0.5f;         // Mini card scale
    private int cardsPerRow = 3;                // Cards per row
    private float cardSpacing = 10f;            // Space between cards
    private GlyphLayout layout;                  // GlyphLayout for text

    public void CardLibrary(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();               // Create a new SpriteBatch
        font = new BitmapFont();                  // Create a new BitmapFont
        layout = new GlyphLayout();               // Create a new GlyphLayout
        font.getData().setScale(miniCardScale * 0.5f); // Scale the font
    }

    @Override
    public void render() {
        // Clear the screen
        batch.begin();
        //drawLibrary(50, 600); // Adjust the y-position based on your viewport
        batch.end();
    }

    /*
    public void drawLibrary(float initialX, float initialY) {
        float currentX = initialX;
        float currentY = initialY;

        for (int i = 0; i < cardList.size(); i++) {
            Card currentCard = cardList.get(i);
            drawMiniCard(currentCard, currentX, currentY); // Call to draw mini card

            // Move to the next position
            if ((i + 1) % cardsPerRow == 0) {
                currentX = initialX; // Reset X for new row
                currentY -= (currentCard.getHeight() * miniCardScale) + cardSpacing; // Move down
            } else {
                //currentX += (currentCard.getWidth() * miniCardScale) + cardSpacing; // Move right
            }
        }
    }

    private void drawMiniCard(Card card, float x, float y) {
        // Draw the card back
        Texture cardbackTexture = new Texture("cardback2.png"); // Load the card back texture
        Texture cardCreatureTexture = card.getTexture();         // Get the card texture

        // Draw the card back
        batch.draw(cardbackTexture, x, y, cardbackTexture.getWidth() * miniCardScale, cardbackTexture.getHeight() * miniCardScale);

        // Draw the card creature sprite
        batch.draw(cardCreatureTexture, x + 20, y + 40, cardCreatureTexture.getWidth() * miniCardScale, cardCreatureTexture.getHeight() * miniCardScale);

        // Draw the card name, cost, attack, and shield text
        layout.setText(font, card.getName());
        font.draw(batch, layout, x + 10, y + 130 * miniCardScale);
        layout.setText(font, "Cost: " + card.getCost());
        font.draw(batch, layout, x + 10, y + 110 * miniCardScale);
        layout.setText(font, "ATK: " + card.getAttack());
        font.draw(batch, layout, x + 10, y + 90 * miniCardScale);
        layout.setText(font, "DEF: " + card.getShield());
        font.draw(batch, layout, x + 10, y + 70 * miniCardScale);
    }*/

    @Override
    public void dispose() {
        batch.dispose(); // Dispose of the batch
        font.dispose();  // Dispose of the font
    }

    /*
    // Set Font
    font = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
     */
}
