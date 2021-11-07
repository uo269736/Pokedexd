package models;

import java.util.List;

public class Relacion {

    private List<Tipo> double_damage_to;
    private List<Tipo> half_damage_to;
    private List<Tipo> no_damage_to;

    public Relacion(List<Tipo> double_damage_to, List<Tipo> half_damage_to, List<Tipo> no_damage_to) {
        this.double_damage_to = double_damage_to;
        this.half_damage_to = half_damage_to;
        this.no_damage_to = no_damage_to;
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

}
