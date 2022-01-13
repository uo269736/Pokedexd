package com.example.pokedexd.models;

public class Tipo {

    private String name;
    private String url;

    public Tipo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {return this.url;}

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
