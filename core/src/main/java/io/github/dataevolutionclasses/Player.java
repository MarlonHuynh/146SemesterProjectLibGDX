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
        this.alreadyTrashed = false;
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

    public void setAlreadyTrashed(boolean alreadyTrashed) {
        this.alreadyTrashed = alreadyTrashed;
    }

    public void setHandCard(ArrayList<Card> handCard) {
        this.handCard = handCard;
    }

    //    TrashCard method:
//      Moi luot (co quyen) duoc trash 1 la tren hand
//      Trash -> +1 energy
    public Card trashCard(Card card) {
        // Check if the player has already trashed a card or if there’s only one card left
        if (alreadyTrashed || handCard.size() <= 1) {
            System.out.println("Unable to trash card this round.");
            return null;
        }

        // Ensure the card to trash is in hand
        if (handCard.contains(card)) {
            handCard.remove(card);
            energy++;
            alreadyTrashed = true;
            System.out.println("You have just trashed " + card.getName());
            System.out.println("Your energy: " + energy);
            return card;
        } else {
            System.out.println("Card not found in hand, cannot trash.");
            return null;
        }
    }

    public void resetTrashStatus() {
        alreadyTrashed = false;
    }
    // Sort handCards by stages
    // Using quickSort (quickest way to sort)
    private void quickSortByStages(int low, int high){
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
                handCard.set(j,temp);
            }
        }
        Card temp = handCard.get(i+1);
        handCard.set(i+1,handCard.get(high));
        handCard.set(high,temp);
        return i+1;
    }
    public void sortHandCard(){
        if(!handCard.isEmpty()){
            quickSortByStages(0,handCard.size()-1);
        }
    }

    public Card drawCard(){
        if(!deck.isEmpty()){
            Card drawnCard = deck.remove(0);
            handCard.add(drawnCard);
            return drawnCard;
        }
        return null;
    }

    public void endTurn(){
        alreadyTrashed = false;
        System.out.println("Turn ended.");
    }

}
