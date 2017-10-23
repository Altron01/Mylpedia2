package com.altron.mylpedia2.Misc.Custom_Classes;

public class Deck {
    private String Name;
    private int Id;

    public Deck(String name, int id){
        Name = name;
        Id = id;
    }
    public Deck(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }
}
