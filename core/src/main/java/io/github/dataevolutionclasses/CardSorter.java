package io.github.dataevolutionclasses;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CardSorter {
    public static List<Card> sortByName(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparing(Card::getName));
    }

    public static List<Card> sortByShield(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparingInt(Card::getShield));
    }

    public static List<Card> sortByAttack(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparingInt(Card::getAttack));
    }

    public static List<Card> sortByCost(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparingInt(Card::getCost));
    }

    public static List<Card> sortByStage(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparingInt(Card::getStage));
    }

    private static List<Card> mergeSort(List<Card> cards, Comparator<Card> comparator) {
        if (cards.size() < 2) return cards;

        int mid = cards.size() / 2;
        List<Card> left = new ArrayList<>(cards.subList(0, mid));
        List<Card> right = new ArrayList<>(cards.subList(mid, cards.size()));

        left = mergeSort(left, comparator);
        right = mergeSort(right, comparator);

        return merge(left, right, comparator);
    }

    private static List<Card> merge(List<Card> left, List<Card> right, Comparator<Card> comparator) {
        List<Card> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i++));
        }

        while (j < right.size()) {
            merged.add(right.get(j++));
        }

        return merged;
    }
}

