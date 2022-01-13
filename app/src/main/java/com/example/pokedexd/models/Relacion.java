package com.example.pokedexd.models;

import java.util.List;

public class Relacion {

    private List<Tipo> double_damage_to; //eficaz
    private List<Tipo> half_damage_to; //poco eficaz
    private List<Tipo> no_damage_to; //no afecta a
    private List<Tipo> double_damage_from; //débil
    private List<Tipo> half_damage_from; //resistente
    private List<Tipo> no_damage_from; //inmune

    public Relacion(List<Tipo> double_damage_from, List<Tipo> double_damage_to, List<Tipo> half_damage_from,
    List<Tipo> half_damage_to, List<Tipo> no_damage_from, List<Tipo> no_damage_to) {
        this.double_damage_to = double_damage_to;
        this.half_damage_to = half_damage_to;
        this.no_damage_to = no_damage_to;
        this.no_damage_from = no_damage_from;
        this.double_damage_from = double_damage_from;
        this.half_damage_from = half_damage_from;
    }

    public List<Tipo> getDouble_damage_to() {
        return double_damage_to;
    }

    public List<Tipo> getHalf_damage_to() {
        return half_damage_to;
    }

    public List<Tipo> getNo_damage_to() {
        return no_damage_to;
    }

    public List<Tipo> getDouble_damage_from() {
        return double_damage_from;
    }

    public List<Tipo> getHalf_damage_from() {
        return half_damage_from;
    }

    public List<Tipo> getNo_damage_from() {
        return no_damage_from;
    }
}
