package io.github.dataevolutionclasses;

import com.badlogic.gdx.graphics.Texture;

public class Card {
    private Texture texture;
    private String name;
    private String type;
    private int stage;
    private int cost;
    private int attack;
    private int shield;
    private String desc;
    public Card(Texture texture, String name, String type, int stage, int cost, int attack, int shield, String desc){
        this.texture = texture;
        this.name = name;
        this.type = type;
        this.stage = stage;
        this.cost = cost;
        this.attack = attack;
        this.shield = shield;
        this.desc = desc;
    }
    public Card(String name, String type, int stage, int cost, int attack, int shield, String desc){
        texture = new Texture("cardsprites/balancedbinarytree.png");
        this.name = name;
        this.type = type;
        this.stage = stage;
        this.cost = cost;
        this.attack = attack;
        this.shield = shield;
        this.desc = desc;

    }
    public Card(){
        texture = new Texture("cardsprites/balancedbinarytree.png");
        name = "DefaultCardName";
        stage = -1;
        cost = -1;
        attack = -1;
        shield = -1;
    }
    // Getters
    public Texture getTexture(){
        return texture;
    }
    public String getName(){
        return name;
    }
    public int getStage(){
        return stage;
    }
    public int getCost(){
        return cost;
    }
    public int getAttack(){
        return attack;
    }
    public int getShield(){
        return shield;
    }
    public String getDesc(){
        return desc;
    }

    // Setters
    public void setTexture(String path){
        texture = new Texture(path);
    }
    public void setName(String name){
        this.name = name;
    }
    public void setStage(int stage){
        this.stage = stage;
    }
    public void setCost(int cost){
        this.cost = cost;
    }
    public void setAttack(int attack){
        this.attack = attack;
    }
    public void setShield(int shield){
        this.shield = shield;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void print(){
        System.out.println(name + " | " + type + " | " + stage + " | "
            + cost + " | " + attack + " | " + shield + " | " + desc);
    }

}
