package io.github.dataevolutionclasses;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CardSorter {
    public static void sortByName(List<Card> cards) {
        Collections.sort(cards, Comparator.comparing(Card::getName));
    }

    public static void sortByStage(List<Card> cards) {
        Collections.sort(cards, Comparator.comparingInt(Card::getStage));
    }

    public static void sortByCost(List<Card> cards) {
        Collections.sort(cards, Comparator.comparingInt(Card::getCost));
    }

    public static void sortByAttack(List<Card> cards) {
        Collections.sort(cards, Comparator.comparingInt(Card::getAttack));
    }

    public static void sortByShield(List<Card> cards) {
        Collections.sort(cards, Comparator.comparingInt(Card::getShield));
    }
}
package io.github.dataevolutionclasses;

import java.util.List;

public class CardSorter {

    public static void sortCardsByName(List<Card> cardList) {
        cardList.sort((card1, card2) -> card1.getName().compareToIgnoreCase(card2.getName()));
    }

    public static void sortCardsByStage(List<Card> cardList) {
        cardList.sort((card1, card2) -> Integer.compare(card1.getStage(), card2.getStage())); // Ascending
    }

    public static void sortCardsByCost(List<Card> cardList) {
        cardList.sort((card1, card2) -> Integer.compare(card1.getCost(), card2.getCost())); // Ascending
    }

    public static void sortCardsByShield(List<Card> cardList) {
        cardList.sort((card1, card2) -> Integer.compare(card1.getShield(), card2.getShield())); // Ascending
    }

    public static void sortCardsByAttack(List<Card> cardList) {
        cardList.sort((card1, card2) -> Integer.compare(card1.getAttack(), card2.getAttack())); // Ascending
    }
}
