package models;

public class DescripcionA {
    private String flavor_text;
    private Idioma language;

    public DescripcionA(String flavor_text, Idioma language) {
        this.flavor_text = flavor_text;
        this.language = language;
    }

    public String getFlavor_text() {
        return flavor_text;
    }

    public Idioma getLanguage() {
        return language;
    }

    public int getIdIdioma() {
        return language.getId();
    }
}
