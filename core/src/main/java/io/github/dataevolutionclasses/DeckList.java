package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter; // Rendering
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

import java.util.ArrayList;
import java.util.List; // Util

public class DeckList<Deck> extends Main
{
    private SpriteBatch batch;
    private List<Card> deck;
    public void draw()
    {
    }

    //Small test of list of cards
    public static final List<Card> cardNames = new ArrayList<>();

    // Static initializer block to populate initial cards
    // Create multiple decks
    List<Card> decks = new ArrayList<>();


    // Create first deck
    ArrayList<Card> deck1 = new ArrayList<>();
        deck1.add();
        deck1.addCard(new Card("Quick Sort", "O"));
        deck1.addCard(new Card("MergeSort", "1"));

    // Add the first deck to the list of decks
        decks.add(deck1);

    // Create second deck
    Deck deck2 = new Deck();
        deck2.addCard(new Card("Sort1", "O"));
        deck2.addCard(new Card("Sort2", "O(N)"));
        deck2.addCard(new Card("Sort3", "N^2"));

    // Add the second deck to the list of decks
        decks.add(deck2);

    // Print the contents of each deck
    for (Deck deck : decks) {
    System.out.println("Deck contents:");
    System.out.println(deck);
}

}
