package com.example.pokedexd.equipos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokedexd.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import models.Pokemon;

public class AddPokemonEquipoActivity extends AppCompatActivity {

    private Button btnGuardar;
    private EditText nombrePokemon;
    private EditText nombreObjeto;
    private EditText nombreHabilidad;
    private EditText nombreAtaque1;
    private EditText nombreAtaque2;
    private EditText nombreAtaque3;
    private EditText nombreAtaque4;
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon_equipo);

        Intent intent   = getIntent();
        String nombre   = intent.getStringExtra(CrearEquipoActivity.NOMBRE_POKEMON);

        btnGuardar      = (Button) findViewById(R.id.btnGuardarPokemon);
        nombrePokemon   = (EditText) findViewById(R.id.textNombrePokemon);
        nombrePokemon.setText(nombre);
        nombrePokemon.setEnabled(false);
        nombreObjeto    = (EditText) findViewById(R.id.textNombreObjeto);
        nombreHabilidad = (EditText) findViewById(R.id.textNombreHabilidad);
        nombreAtaque1   = (EditText) findViewById(R.id.textAtaque1);
        nombreAtaque2   = (EditText) findViewById(R.id.textAtaque2);
        nombreAtaque3   = (EditText) findViewById(R.id.textAtaque3);
        nombreAtaque4   = (EditText) findViewById(R.id.textAtaque4);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    pokemon = new Pokemon(nombrePokemon.getText().toString(),"https://pokeapi.co/api/v2/pokemon/" + nombrePokemon.getText().toString());
                    pokemon.setObjeto(encontrarNombre(nombreObjeto.getText().toString()));
                    pokemon.setHabilidad(nombreHabilidad.getText().toString());
                    ArrayList<String> ataques = new ArrayList<>();
                    ataques.add(nombreAtaque1.getText().toString());
                    ataques.add(nombreAtaque2.getText().toString());
                    ataques.add(nombreAtaque3.getText().toString());
                    ataques.add(nombreAtaque4.getText().toString());
                    pokemon.setAtaques(ataques);

                    Intent intentResultado = new Intent();
                    intentResultado.putExtra(CrearEquipoActivity.NUEVO_POKEMON,pokemon);
                    setResult(RESULT_OK,intentResultado);
                    finish();
                }
            }
        });
    }

    protected String encontrarNombre(String nombreObjeto){
        InputStream file              = null;
        InputStreamReader reader      = null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("ObjetosPokemon.csv");
            reader         = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[0].toLowerCase().equals(nombreObjeto.toLowerCase()) || datos[1].toLowerCase().equals(nombreObjeto.toLowerCase())) {
                        return datos[0];
                    }
                }
            }
            return nombreObjeto;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return nombreObjeto;
    }

    protected boolean validar() {
        if(!validarObjeto()) {
            Snackbar.make(findViewById(R.id.AddPokemonEquipo), +R.string.msg_objeto_no_encontrado, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!validarHabilidad()) {
            Snackbar.make(findViewById(R.id.AddPokemonEquipo), +R.string.msg_habilidad_no_encontrada, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(!validarAtaques()) {
            Snackbar.make(findViewById(R.id.AddPokemonEquipo), +R.string.msg_ataques_no_encontrados, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    protected boolean validarObjeto(){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("ObjetosPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[0].toLowerCase().equals(nombreObjeto.getText().toString().toLowerCase()) || datos[1].toLowerCase().equals(nombreObjeto.getText().toString().toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    protected boolean validarHabilidad(){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("HabilidadesPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[1].toLowerCase().equals(nombreHabilidad.getText().toString().toLowerCase()) || datos[2].toLowerCase().equals(nombreHabilidad.getText().toString().toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    protected boolean validarAtaques(){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("AtaquesPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            int aux=0;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[0].toLowerCase().equals(nombreAtaque1.getText().toString().toLowerCase()) ||
                            datos[1].toLowerCase().equals(nombreAtaque1.getText().toString().toLowerCase()))
                        aux++;
                    else if(datos[0].toLowerCase().equals(nombreAtaque2.getText().toString().toLowerCase()) ||
                            datos[1].toLowerCase().equals(nombreAtaque2.getText().toString().toLowerCase()))
                        aux++;
                    else if(datos[0].toLowerCase().equals(nombreAtaque3.getText().toString().toLowerCase()) ||
                            datos[1].toLowerCase().equals(nombreAtaque3.getText().toString().toLowerCase()))
                        aux++;
                    else if(datos[0].toLowerCase().equals(nombreAtaque4.getText().toString().toLowerCase()) ||
                            datos[1].toLowerCase().equals(nombreAtaque4.getText().toString().toLowerCase()))
                        aux++;
                }
            }
            if(aux==4)
                return true;
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}