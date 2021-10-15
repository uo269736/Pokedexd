package models;

import java.util.ArrayList;

public class PokemonRespuestaIndividual {

    private String name;
    private int id;

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setOrder(int order) {
        this.id = order;
    }
}
