package models;

public class Idioma {

    private int id;
    private String name;


    public Idioma(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
