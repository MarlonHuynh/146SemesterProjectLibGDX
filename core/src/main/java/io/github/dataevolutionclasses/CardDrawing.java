//package io.github.dataevolutionclasses;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.Align;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class CardDrawing {
//
//    // Static variables for card list and map
//    private static List<Card> cardList;
//    private static HashMap<Card, Integer> cardToIntMap;
//
//    // Instance variables for the card's properties
//    private Card card;
//    private float x, y, scale;
//    private Sprite cardSprite, cardbackSprite, selectedSprite;
//    private BitmapFont nameFont, numberFont;
//    private GlyphLayout descLayout;
//    private float nameX, nameY, descWidth, descX, descY;
//    private float costTextX, costTextY, attackTextX, attackTextY, shieldTextX, shieldTextY;
//
//    // SpriteBatch for drawing
//    private SpriteBatch batch;
//
//    // Constructor to initialize the CardDrawing instance with a card, position, and scale
//    public CardDrawing(Card card, float x, float y, float scale) {
//        this.card = card;
//        this.x = x;
//        this.y = y;
//        this.scale = scale;
//        this.batch = batch;
//
//        loadSprites();
//        calculatePositions();
//        initializeFonts();
//    }
//
//    // Draw the background (card back)
//    public void drawBackground(OrthographicCamera camera) {
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        cardbackSprite.draw(batch);
//        batch.end();
//    }
//
//    // Draw the card details (name, description, stats, etc.)
//    public void drawCardDetails() {
//        // Draw card name
//        nameFont.draw(batch, card.getName(), nameX, nameY);
//
//        // Draw card description
//        descLayout.setText(nameFont, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
//        nameFont.draw(batch, descLayout, descX, descY);
//
//        // Draw card stats (cost, attack, shield)
//        numberFont.draw(batch, String.valueOf(card.getCost()), costTextX, costTextY);
//        numberFont.draw(batch, String.valueOf(card.getAttack()), attackTextX, attackTextY);
//        numberFont.draw(batch, String.valueOf(card.getShield()), shieldTextX, shieldTextY);
//    }
//
//    // Draw all cards in the list
//    public static void drawCards(List<Card> cardList, OrthographicCamera camera, SpriteBatch batch, float x, float y, float scale) {
//        // Set up projection matrix for batch
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//
//        // Loop through all cards and draw them
//        for (int i = 0; i < cardList.size(); i++) {
//            Card card = cardList.get(i);
//
//            // Calculate card position (simple grid layout)
//            float cardX = x + (i % 5) * 150;  // Example for 5 cards per row
//            float cardY = y - (i / 5) * 180;  // Adjust Y for row positioning
//
//            // Create a new CardDrawing instance and draw it
//            CardDrawing cardDrawingInstance = new CardDrawing(card, cardX, cardY, scale);
//            cardDrawingInstance.drawBackground(camera);  // Draw the background (card back)
//            cardDrawingInstance.drawCardDetails();      // Draw card details (name, stats, etc.)
//        }
//
//        batch.end();
//    }
//
//    // Load the card and background sprites
//    private void loadSprites() {
//        cardSprite = new Sprite(card.getTexture());
//        cardbackSprite = loadCardbackSprite();
//        selectedSprite = new Sprite(new Texture("CardbackHighlight.png"));
//
//        // Scale sprites
//        float scaleFactor = scale * 1.2f;
//        selectedSprite.setSize(selectedSprite.getTexture().getWidth() * scale, selectedSprite.getTexture().getHeight() * scale);
//        cardbackSprite.setSize(cardbackSprite.getTexture().getWidth() * scale, cardbackSprite.getTexture().getHeight() * scale);
//        cardSprite.setSize(cardSprite.getTexture().getWidth() * scaleFactor, cardSprite.getTexture().getHeight() * scaleFactor);
//    }
//
//    // Determine the appropriate cardback sprite based on card properties
//    private Sprite loadCardbackSprite() {
//        String texturePath = "cardback2.png"; // Default
//
//        switch (card.getType()) {
//            case "Algorithm":
//                texturePath = "cardback_algo" + card.getStage() + ".png";
//                break;
//            case "Data Structure":
//                texturePath = "cardback_datastruct" + card.getStage() + ".png";
//                break;
//            case "Data Type":
//                texturePath = "cardback_datatype" + card.getStage() + ".png";
//                break;
//            default:
//                if (card.getName().equals("Draw")) texturePath = "cardback3.png";
//                else if (card.getName().equals("Trash")) texturePath = "discard.png";
//                else if (card.getName().equals("Blank")) texturePath = "CardbackBlank.png";
//                else if (card.getName().equals("End Turn")) texturePath = "endturn.png";
//                break;
//        }
//        return new Sprite(new Texture(texturePath));
//    }
//
//    // Calculate positions for the card's elements
//    private void calculatePositions() {
//        float cardX = x - (cardbackSprite.getWidth() * 0.5f);
//        float cardY = y - (cardbackSprite.getHeight() * 0.5f);
//        float midcardX = cardbackSprite.getWidth() * 0.5f;
//        float midcardY = cardbackSprite.getHeight() * 0.5f;
//
//        // Set sprite positions
//        cardbackSprite.setPosition(cardX, cardY);
//        selectedSprite.setPosition(cardX - (midcardX * 0.1f), cardY - (midcardY * 0.06f));
//        cardSprite.setPosition(cardX + (midcardX / 3.5f), cardY + (midcardY / 1.6f));
//
//        // Set text positions
//        selectedSprite.setAlpha(0.2f);
//        nameX = cardX + (midcardX * 0.16f);
//        nameY = cardY + (midcardY * 1.92f);
//        descX = cardX + (midcardX * 0.16f);
//        descY = cardY + (midcardY * 0.54f);
//        costTextX = cardX + (midcardX * 0.2f);
//        costTextY = cardY + (midcardY * 1.7f);
//        attackTextX = cardX + (midcardX * 1.64f);
//        attackTextY = cardY + (midcardY * 0.56f);
//        shieldTextX = cardX + (midcardX * 1.64f);
//        shieldTextY = cardY + (midcardY * 0.22f);
//    }
//
//    // Initialize fonts
//    private void initializeFonts() {
//        nameFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
//        nameFont.getData().setScale(scale * 0.65f);
//        nameFont.getData().setLineHeight(nameFont.getLineHeight() * 2.5f);
//
//        numberFont = new BitmapFont(Gdx.files.internal("ui/dpcomic.fnt"));
//        numberFont.getData().setScale(scale * 0.8f);
//
//        descWidth = 130 * scale;
//        descLayout = new GlyphLayout();
//        descLayout.setText(nameFont, card.getDesc(), Color.BLACK, descWidth, Align.left, true);
//    }
//
//    // Static method to generate the map from card to index (useful for lookup)
//    public static void generateCardToIntMap() {
//        if (cardToIntMap == null) {
//            cardToIntMap = new HashMap<>();
//            for (int i = 0; i < cardList.size(); i++) {
//                cardToIntMap.put(cardList.get(i), i);
//            }
//        }
//    }
//
//    // Dispose resources (important for memory management)
//    public void dispose() {
//        nameFont.dispose();
//        numberFont.dispose();
//        cardSprite.getTexture().dispose();
//        cardbackSprite.getTexture().dispose();
//        selectedSprite.getTexture().dispose();
//    }
//
//    // Static getter for the card list
//    public static List<Card> getCardList() {
//        return cardList;
//    }
//}
