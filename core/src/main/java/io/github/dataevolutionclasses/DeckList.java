package io.github.dataevolutionclasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // Util

public class DeckList extends ApplicationAdapter
{
    public List<Card> deck;
    public static List<Card> cardList;
    private final HashMap<String, Card> nameToCardHashmap = new HashMap<>();            // Master Card Storage (Do not change)
    private final HashMap<String, Integer> nameToIntHashmap = new HashMap<>();

    public void create()
    {

        CardReader reader = new CardReader("core/src/main/java/io/github/dataevolutionclasses/CardStats2.csv");
        reader.generateCardsFromCSV();
        cardList = reader.getCardList();

        // Static initializer block to populate initial cards
        // Create multiple decks
        List<Card> decks = new ArrayList<>();


        // Create first deck
        List<String> deck1 = Arrays.asList(
            "Bubble Sort", "Bubble Sort", "Seelection Sort", "Seelection Sort", "Eelnsertion Sort", "Eelnsertion Sort", "Surgeon Sort", "Surgeon Sort", "A-Starfish", "Raydix Sort",
            "Parraykeet","Parraykeet","Parraykeet","Bin. Canary Tree","Bin. Canary Tree","Bal. Canary Tree",
            "Quetzelqueueotl", "Quetzelqueueotl", "Quetzelqueueotl" );

        //Add the first deck to the list of decks
        for (int i = 0; i < cardList.size(); i++) {
            nameToCardHashmap.put(cardList.get(i).getName(), cardList.get(i)); // Populate the nameToCard and nameToInt Hashmap (For easier searches of name to Card type);
            nameToIntHashmap.put(cardList.get(i).getName(), i);
        }

        for (String str : deck1){
            deck.add(nameToCardHashmap.get(str).deepCopy());
        }






    }

}


