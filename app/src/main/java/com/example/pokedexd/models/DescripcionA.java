package com.example.pokedexd.models;

public class DescripcionA {
    private String flavor_text;
    private Lenguaje language;

    public DescripcionA(String flavor_text, Lenguaje language) {
        this.flavor_text = flavor_text;
        this.language = language;
    }

    public String getFlavor_text() {
        return flavor_text;
    }

    public Lenguaje getLenguage() {
        return language;
    }
}
