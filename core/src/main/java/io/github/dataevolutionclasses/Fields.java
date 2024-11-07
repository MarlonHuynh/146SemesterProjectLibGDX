package io.github.dataevolutionclasses;

import java.util.HashMap;

public class Fields {
    // using HashMap to create types of fields based on the CSV types
    private HashMap<String, Card> fields;

    public Fields() {
        fields = new HashMap<>();
        fields.put("algorithm", null);
        fields.put("data structure", null);
        fields.put("data type", null);
    }

    public Card summonToField(Card card, Player player) {
        String environment = card.getType().toLowerCase(); // Match environment to card type
        // Check if the field corresponds to the type of Card
        if (!fields.containsKey(environment)) {
            return null;
        }

        Card currentCard = fields.get(environment);

        // Summon egg
        if (card.getStage() == 0 && card.getCost() == 0) {
            if (currentCard == null) {
                clearField(environment);
                fields.put(environment, card);
                return card; // Summon Egg to field
            } else {
                return null; // Field already has a monster, can't summon
            }
        }

        // Summon a monster
        if (card.getStage() > 0 && card.getCost() > 0) {
            if (player.getEnergy() < card.getCost()) {
                return null; // Not enough energy to summon
            }
            // Egg is stage 0, can only summon if currentCard's stage is lower by 1
            if (currentCard != null && currentCard.getStage() == card.getStage() - 1) {
                clearField(environment);
                fields.put(environment, card);
                player.setEnergy(player.getEnergy() - card.getCost());
                return card;
            } else {
                return null; // Cannot summon
            }
        }
        return null; // Fallback case
    }

    public void clearField(String environment) {
        environment = environment.toLowerCase();
        if (fields.containsKey(environment)) {
            Card currentCard = fields.get(environment);
            if (currentCard != null) {
                fields.put(environment, null); // Clear the field
            }
        }
    }
}
