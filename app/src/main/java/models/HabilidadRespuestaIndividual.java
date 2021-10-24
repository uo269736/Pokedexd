package models;

import java.util.ArrayList;

public class HabilidadRespuestaIndividual {

    private ArrayList<DescripcionH> flavor_text_entries;
    private ArrayList<Nombre> names;
    private int id;

    public ArrayList<DescripcionH> getDescripciones() {
        return flavor_text_entries;
    }

    public void setDescripciones(ArrayList<DescripcionH> object) {
        this.flavor_text_entries = object;
    }

    public ArrayList<Nombre> getNames() {
        return names;
    }

    public void setNames(ArrayList<Nombre> names) {
        this.names = names;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
