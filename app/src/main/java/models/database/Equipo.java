package models.database;

import java.util.ArrayList;
import java.util.List;

import models.Pokemon;

public class Equipo {

    private String nombre;
    private List<Pokemon> pokemonList;

    public Equipo(String nombre, List<Pokemon> pokemonList) {
        this.nombre = nombre;
        this.pokemonList = pokemonList;
    }

    public Equipo() {
        nombre = "";
        pokemonList = new ArrayList<Pokemon>();
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    private void setPokemonList(ArrayList<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }
}
