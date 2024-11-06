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
