package io.github.dataevolutionclasses;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CardSorter {

    // Merge sort for name sorting (ascending order)
    public static List<Card> sortByName(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparing(Card::getName));
    }

    // Merge sort for stage sorting (ascending order)
    public static List<Card> sortByStage(List<Card> cards) {
        return mergeSort(new ArrayList<>(cards), Comparator.comparingInt(Card::getStage));
    }

    // Counting sort for shield, sorted in descending order
    public static List<Card> sortByShield(List<Card> cards) {
        return countingSort(cards, Card::getShield, 10);
    }

    // Counting sort for attack, sorted in descending order
    public static List<Card> sortByAttack(List<Card> cards) {
        return countingSort(cards, Card::getAttack, 10);
    }

    // Counting sort for cost, sorted in descending order
    public static List<Card> sortByCost(List<Card> cards) {
        return countingSort(cards, Card::getCost, 10);
    }

    // Merge sort helper for sorting by name and stage
    private static List<Card> mergeSort(List<Card> cards, Comparator<Card> comparator) {
        if (cards.size() < 2) return cards;

        int mid = cards.size() / 2;
        List<Card> left = new ArrayList<>(cards.subList(0, mid));
        List<Card> right = new ArrayList<>(cards.subList(mid, cards.size()));

        left = mergeSort(left, comparator);
        right = mergeSort(right, comparator);

        return merge(left, right, comparator);
    }

    // Merge function for merge sort
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

    // Counting sort for numeric attributes in descending order
    private static List<Card> countingSort(List<Card> cards, java.util.function.ToIntFunction<Card> attribute, int maxRange) {
        // Determine the minimum and maximum values in the attribute range
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (Card card : cards) {
            int value = attribute.applyAsInt(card);
            if (value < min) min = value;
            if (value > max) max = value;
        }

        // Calculate the range of values and shift if minimum is not zero
        int range = max - min + 1;
        int[] countArray = new int[range];

        // Populate count array with adjusted indices
        for (Card card : cards) {
            int value = attribute.applyAsInt(card) - min; // Shift values by `min`
            countArray[value]++;
        }

        // Create sorted list in descending order
        List<Card> sortedCards = new ArrayList<>();
        for (int i = countArray.length - 1; i >= 0; i--) {
            while (countArray[i] > 0) {
                for (Card card : cards) {
                    if (attribute.applyAsInt(card) - min == i) {
                        sortedCards.add(card);
                        countArray[i]--;
                    }
                }
            }
        }

        return sortedCards;
    }
}
