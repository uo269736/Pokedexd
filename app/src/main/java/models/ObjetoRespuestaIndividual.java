package models;

import java.util.ArrayList;

public class ObjetoRespuestaIndividual {

    private ArrayList<DescripcionO> flavor_text_entries;
    private ArrayList<Nombre> names;
    private int id;

    public ArrayList<DescripcionO> getDescripciones() {
        return flavor_text_entries;
    }

    public void setDescripciones(ArrayList<DescripcionO> object) {
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
