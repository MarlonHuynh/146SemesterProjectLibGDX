package io.github.dataevolutionclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
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
    List<String> deck1 = Arrays.asList(
        "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
        "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
        "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl" );

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
