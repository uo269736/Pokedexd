package models;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class TipoRespuestaIndividual {

    private Relacion damage_relations;

    public TipoRespuestaIndividual(Relacion damage_relations) {
        this.damage_relations = damage_relations;
    }

    public Relacion getDamage_relations() {
        return damage_relations;
    }

}
