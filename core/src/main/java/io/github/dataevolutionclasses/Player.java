package io.github.dataevolutionclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    // Declare Deck, Health, Hand, Energy
    private ArrayList<Card> deck;
    private int health;
    private ArrayList<Card> handCard;
    private int energy;
    private boolean alreadyTrashed;

    public Player() {
        this.deck = new ArrayList<>();
        this.health = 40;
        this.handCard = new ArrayList<Card>(5);
        this.energy = 0;
        this.alreadyTrashed = true;
    }

    public int getEnergy() {
        return energy;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public int getHealth() {
        return health;
    }

    public ArrayList<Card> getHandCard() {
        return handCard;
    }

    public boolean isAlreadyTrashed() {
        return alreadyTrashed;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    //    TrashCard method:
//      Moi luot (co quyen) duoc trash 1 la tren hand
//      Trash -> +1 energy
    public void trashCard(){
        if (alreadyTrashed || handCard.size() <= 1){
            System.out.println("Unable to trash card this round");
        }
        else {
            Scanner scnr = new Scanner(System.in);
            System.out.println("Choose one of these card to trash");
            for (int i=0; i<handCard.size(); i++){
                System.out.println(handCard.get(i).getName());
            }
            int choice;
            // Prevent trolling
            while (true){
                System.out.println("Card you want to trash is:");
                choice = scnr.nextInt();
                if (choice >0 && choice < handCard.size()){
                    break;
                }
                else {
                    System.out.println("-1000 social credit");
                }
            }

            Card cardtoTrash = handCard.get(choice);
            handCard.remove(cardtoTrash);
            energy++;
            alreadyTrashed = true;
            System.out.println("You have just trash "+ cardtoTrash.getName() );
            System.out.println("Your energy: " + energy);
        }
    }

    //Sort handCards by stages
    //Using quickSort (quickest way to sort)

    public void quickSortByStages(int low, int high){
        if (low < high){
            int a = partition(low, high);
            quickSortByStages(low, a-1);
            quickSortByStages(a+1,high);
        }
    }

    private int partition(int low, int high ){
        int pivot = handCard.get(high).getStage();
        int i = low -1;
        for (int j = low; j<= high -1; j++){
            if (handCard.get(j).getStage() <= pivot){
                i++;
                Card temp = handCard.get(i);
                handCard.set(i,handCard.get(j));
                handCard.set(i,temp);
            }
        }
        Card temp = handCard.get(i+1);
        handCard.set(i+1,handCard.get(high));
        handCard.set(high,temp);
        return i+1;
    }

    public void sortHandCard(){
        if(handCard.isEmpty()){
            return;
        }
        else {
            quickSortByStages(0,handCard.size()-1);
        }
    }
}
