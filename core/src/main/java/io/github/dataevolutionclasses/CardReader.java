/*
    Class for reading cards from CSV file to Card objects and doubles as a storage of all cards
*/

package io.github.dataevolutionclasses;

import com.badlogic.gdx.graphics.Texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardReader {
    private String filePath;
    private List<Card> cardList;

    public CardReader(String filePath) {
        this.filePath = filePath;
        this.cardList = new ArrayList<>();
    }
    public void generateCardsFromCSV() {
        // Read CSV file into string list 'data'
        List<String[]> data = new ArrayList<>();
        String line;
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert string array into Card list
        for (int i = 1; i < data.size(); i++){
            for (int j = 0; j + 6 < data.get(i).length; j += 6){
                Card newCard = new Card(data.get(i)[0], data.get(i)[1],
                    Integer.parseInt(data.get(i)[2]), Integer.parseInt(data.get(i)[3]),
                    Integer.parseInt(data.get(i)[4]), Integer.parseInt(data.get(i)[5]),
                    data.get(i)[6]);
                cardList.add(newCard);
            }
        }
        // Assign textures to cards (done separately because the CSV file does not have texture paths)
        assignTexturePathToCard();
        // Print all card in console for debugging purposes
        System.out.println("Printing data: ");
        printCardList();
    }
    public void generateFirst35CardsFromFile() {
        // Read CSV file into string list 'data'
        List<String[]> data = new ArrayList<>();
        String line;
        String delimiter = ",";
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null && lineCount < 36) {
                String[] values = line.split(delimiter);
                data.add(values);
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert string array into Card list
        for (int i = 1; i < data.size(); i++){
            for (int j = 0; j + 6 < data.get(i).length; j += 6){
                Card newCard = new Card(data.get(i)[0], data.get(i)[1],
                    Integer.parseInt(data.get(i)[2]), Integer.parseInt(data.get(i)[3]),
                    Integer.parseInt(data.get(i)[4]), Integer.parseInt(data.get(i)[5]),
                    data.get(i)[6]);
                cardList.add(newCard);
            }
        }
        // Assign textures to cards (done separately because the CSV file does not have texture paths)
        assignTexturePathToCard();
        // Print all card in console for debugging purposes
        System.out.println("Printing data: ");
        printCardList();
    }
    // Returns a copy of the Card list
    public List<Card> getCardList() {
        return cardList;
    }
    // Prints all cards
    public void printCardList() {
        for (Card card : cardList){
            card.print();
        }
    }
    // Assigns a texture path to all the cards in the Card list
    public void assignTexturePathToCard(){
        String path;
        for (Card card : cardList){
            switch (card.getName()){
                case "Bubble Sort":
                    card.setTexture("cardsprites/bubblesort.png");
                    break;
                case "Seelection Sort":
                    card.setTexture("cardsprites/selectionsort.png");
                    break;
                case "Eelnsertion Sort":
                    card.setTexture("cardsprites/insertionsort.png");
                    break;
                case "Surgeon Sort":
                    card.setTexture("cardsprites/mergesort.png");
                    break;
                case "Shell Sort":
                    card.setTexture("cardsprites/shellsort.png");
                    break;
                case "Quickfish Sort":
                    card.setTexture("cardsprites/quicksort.png");
                    break;
                case "A-Starfish":
                    card.setTexture("cardsprites/astar.png");
                    break;
                case "Bucket O' Fish":
                    card.setTexture("cardsprites/bucketsort.png");
                    break;
                case "Raydix Sort":
                    card.setTexture("cardsprites/radixsort.png");
                    break;
                case "Parraykeet":
                    card.setTexture("cardsprites/array.png");
                    break;
                case "Sphinx List":
                    card.setTexture("cardsprites/linkedlist.png");
                    break;
                case "Bin. Canary Tree":
                    card.setTexture("cardsprites/binarytree.png");
                    break;
                case "Quack Stack":
                    card.setTexture("cardsprites/stack.png");
                    break;
                case "Hawkmap":
                    card.setTexture("cardsprites/hashmap.png");
                    break;
                case "Quetzelqueueotl":
                    card.setTexture("cardsprites/queue.png");
                    break;
                case "Grifminmax Heap":
                    card.setTexture("cardsprites/minmaxheap.png");
                    break;
                case "Hippograph":
                    card.setTexture("cardsprites/graph.png");
                    break;
                case "Bal. Canary Tree":
                    card.setTexture("cardsprites/balancedbinarytree.png");
                    break;
                case "Bitbug":
                    card.setTexture("cardsprites/bit.png");
                    break;
                case "Stringer Bee":
                    card.setTexture("cardsprites/string.png");
                    break;
                case "Int Ant":
                    card.setTexture("cardsprites/int.png");
                    break;
                case "Beetlean":
                    card.setTexture("cardsprites/boolean.png");
                    break;
                case "Pointerpede":
                    card.setTexture("cardsprites/pointer.png");
                    break;
                case "Bytebug":
                    card.setTexture("cardsprites/byte.png");
                    break;
                case "Tupletick":
                    card.setTexture("cardsprites/tuple.png");
                    break;
                case "Wordbug":
                    card.setTexture("cardsprites/word.png");
                    break;
                case "Referant":
                    card.setTexture("cardsprites/reference.png");
                    break;
                case "Allocate memory":
                    card.setTexture("cardsprites/plusshield.png");
                    break;
                case "Allocate more memory":
                    card.setTexture("cardsprites/plusshield.png");
                    break;
                case "Overclock":
                    card.setTexture("cardsprites/plusattack.png");
                    break;
                case "Superoverclock":
                    card.setTexture("cardsprites/plusattack.png");
                    break;
                case "Recover Data":
                    card.setTexture("cardsprites/plushealth.png");
                    break;
                case "Backup Data":
                    card.setTexture("cardsprites/plushealth.png");
                    break;
                case "Send Bug":
                    card.setTexture("cardsprites/sendbug.png");
                    break;
                case "Send Malware":
                    card.setTexture("cardsprites/sendbug.png");
                    break;
                default:
                    card.setTexture("cardsprites/balancedbinarytree.png");
                    break;
            }

        }
    }
}
