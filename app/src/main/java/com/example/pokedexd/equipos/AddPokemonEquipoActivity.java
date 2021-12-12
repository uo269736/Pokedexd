package com.example.pokedexd.equipos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokedexd.R;

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

        Intent intent = getIntent();
        String nombre = intent.getStringExtra(CrearEquipoActivity.NOMBRE_POKEMON);

        btnGuardar = (Button) findViewById(R.id.btnGuardarPokemon);
        nombrePokemon = (EditText) findViewById(R.id.textNombrePokemon);
        nombrePokemon.setText(nombre);
        nombrePokemon.setEnabled(false);
        nombreObjeto = (EditText) findViewById(R.id.textNombreObjeto);
        nombreHabilidad = (EditText) findViewById(R.id.textNombreHabilidad);
        nombreAtaque1 = (EditText) findViewById(R.id.textAtaque1);
        nombreAtaque2 = (EditText) findViewById(R.id.textAtaque2);
        nombreAtaque3 = (EditText) findViewById(R.id.textAtaque3);
        nombreAtaque4 = (EditText) findViewById(R.id.textAtaque4);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pokemon = new Pokemon(nombrePokemon.getText().toString(),"https://pokeapi.co/api/v2/pokemon/"+nombrePokemon.getText().toString());
                pokemon.setObjeto(nombreObjeto.getText().toString());
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
        });
    }
}