package io.github.dataevolutionclasses;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CardSorter {
    public static List<Card> sortByName(List<Card> cards) {
        return cards.stream().sorted(Comparator.comparing(Card::getName)).collect(Collectors.toList());
//        cards.stream().map(e->e.getName()).collect(Collectors.toList()).forEach(System.out::println);
//        Collections.sort(cards, Comparator.comparing(Card::getName));
//        cards.stream().map(e->e.getName()).collect(Collectors.toList()).forEach(System.out::println);
    }

    public static List<Card> sortByStage(List<Card> cards) {
//        Collections.sort(cards, Comparator.comparingInt(Card::getStage));
        return cards.stream().sorted(Comparator.comparingInt(Card::getStage)).collect(Collectors.toList());
    }

    public static List<Card> sortByCost(List<Card> cards) {
//        Collections.sort(cards, Comparator.comparingInt(Card::getCost));
        return cards.stream().sorted(Comparator.comparingInt(Card::getCost)).collect(Collectors.toList());
    }

    public static List<Card> sortByAttack(List<Card> cards) {
//        Collections.sort(cards, Comparator.comparingInt(Card::getAttack));
        return cards.stream().sorted(Comparator.comparingInt(Card::getAttack)).collect(Collectors.toList());
    }

    public static List<Card> sortByShield(List<Card> cards) {
//        Collections.sort(cards, Comparator.comparingInt(Card::getShield));
        return cards.stream().sorted(Comparator.comparingInt(Card::getShield)).collect(Collectors.toList());
    }
}
