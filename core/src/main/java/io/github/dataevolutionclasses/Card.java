package io.github.dataevolutionclasses;

import com.badlogic.gdx.graphics.Texture;

public class Card {
    private Texture texture;
    private String name;
    private int stage;
    private int cost;
    private int attack;
    private int shield;
    public Card(Texture i, String n, int st, int c, int a, int sh){
        texture = i;
        name = n;
        stage = st;
        cost = c;
        attack = a;
        shield = sh;
    }
    public Card(String n, int st, int c, int a, int sh){
        texture = new Texture("cardsprites/balancedbinarytree.png");
        name = n;
        stage = st;
        cost = c;
        attack = a;
        shield = sh;
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
}
