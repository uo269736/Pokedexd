package com.example.pokedexd.models;

import java.util.ArrayList;

public class AtaqueRespuesta {

    private ArrayList<Ataque> results;

    public AtaqueRespuesta(ArrayList<Ataque> results) {
        this.results = results;
    }

    public ArrayList<Ataque> getResults() { return results; }
}
