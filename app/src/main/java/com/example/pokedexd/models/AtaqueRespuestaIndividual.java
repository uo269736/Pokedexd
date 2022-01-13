package com.example.pokedexd.models;

import java.util.ArrayList;

public class AtaqueRespuestaIndividual {

    private ArrayList<DescripcionA> flavor_text_entries;
    private ArrayList<Nombre> names;
    private int id;
    private Tipo type;

    public ArrayList<Nombre> getNames() {
        return names;
    }

    public void setDescripciones(ArrayList<DescripcionA> object) {
        this.flavor_text_entries = object;
    }
    public ArrayList<DescripcionA> getFlavorTextEntries() { return flavor_text_entries;}

    public void setNames(ArrayList<Nombre> names) { this.names = names;}

    public int getId() { return id; }
    public void setId(int id) { this.id = id;}

    public void setType(Tipo type) {
        this.type = type;
    }

    public Tipo getType() { return this.type;}
}
