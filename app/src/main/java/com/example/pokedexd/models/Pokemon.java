package com.example.pokedexd.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Pokemon implements Parcelable {

    private int id;
    private String name;
    private String url;

    private ArrayList<String> ataques;
    private String habilidad;
    private String objeto;

    protected Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        ataques = in.createStringArrayList();
        habilidad = in.readString();
        objeto = in.readString();
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Pokemon (String name, String url){
        this.name = name;
        this.url  = url;
    }

    public Pokemon (String name) {
        this.name = name;
        this.url  = "https://pokeapi.co/api/v2/pokemon/" + name;
    }

    public int getId() {
        String [] urlPartes= url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length-1]);
    }

    public int getId2() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getAtaques() {
        return ataques;
    }

    public void setAtaques(ArrayList<String> ataques) {
        this.ataques = ataques;
    }

    public String getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeStringList(ataques);
        parcel.writeString(habilidad);
        parcel.writeString(objeto);
    }

    public String stringShowdown(){
        return getName()+" @ "+getObjeto() +"\n"+
                "Ability: "+getHabilidad() +"\n"+
                "-"+getAtaques().get(0)+"\n"+
                "-"+getAtaques().get(1)+"\n"+
                "-"+getAtaques().get(2)+"\n"+
                "-"+getAtaques().get(3)+"\n";
    }
}
