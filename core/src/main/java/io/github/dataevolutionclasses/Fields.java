package io.github.dataevolutionclasses;
import java.util.HashMap;

public class Fields {
    // using hashMap to create 4 types of fields
    // implement summon method
    private HashMap<String, Card> fields;

    public Fields(){
        fields = new HashMap<>();
        fields.put("forest", null);
        fields.put("ocean", null);
        fields.put("mountain", null);
        fields.put("desert", null);
    }

    public Card summonToField(Card card, Player player){
        String environment = card.getType().toLowerCase();
        // Kiem tra xem field co giong type cua Card khong
        if (!fields.containsKey(environment)){
            return null;
        }

        Card currentCard = fields.get(environment);

        // Summon egg
        if (card.getStage() == 0 && card.getCost() ==0){
            if (currentCard == null){
                clearField(environment);
                fields.put(environment, card);
                // summon Egg to field siu siu
                return card;
            }
            else {
                // field already have a monster, cant summon :(
                return null;
            }
        }

        // summon a monster
        if (card.getStage() >0 && card.getCost() >0){
            if (player.getEnergy() < card.getCost()){
                // not enough energy to summon
                return null;
            }
            // Egg is stage 0
            // In order to summon, have to discard one card on field with stage lower 1
            if (currentCard.getStage() == card.getStage()-1) {
                clearField(environment);
                fields.put(environment, card);
                player.setEnergy(player.getEnergy() - card.getCost());
                return card;
            }
            else {
                return null;
            }
        }
        return null;
    }

    public void clearField(String environment){
        environment = environment.toLowerCase();
            if (fields.containsKey(environment)){
                Card currentCard = fields.get(environment);
                if (currentCard != null) {
                    fields.put(environment, null);
                }
            }
        }
    }

