package com.example.jwimsatt.flashcards;

/**
 * Object for storing the flashcard sides
 */
public class Card {
    private String side1;
    private String side2;
    private int number;
    private int id;

    Card() {
        side1 = " ";
        side2 = " ";
        number = 0;
        id = 0;
    }

    public void setSide1(String first){
        side1 = first;
    }

    public void setSide2(String second){
        side2 = second;
    }

    public void setNumber(int num){
        number = num;
    }

    public void setId(int given) { id = given; }

    public String getSide1(){
        return side1;
    }

    public String getSide2(){
        return side2;
    }

    public int getNumber(){
        return number;
    }

    public int getId(){ return id; }
}
