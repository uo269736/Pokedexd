package com.example.pokedexd.menus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pokedexd.buscadores.BuscarHabilidadActivity;
import com.example.pokedexd.buscadores.BuscarObjetosActivity;
import com.example.pokedexd.buscadores.BuscarPokemonActivity;
import com.example.pokedexd.buscadores.BuscarTiposActivity;
import com.example.pokedexd.R;

public class MenuBuscadorActivity extends AppCompatActivity {

    private Button btnMenuBuscadorPokemon;
    private Button btnMenuBuscadorTipos;
    private Button btnMenuBuscadorObjetos;
    private Button btnMenuBuscadorAtaques;
    private Button btnMenuBuscadorHabilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_buscador);
        //Inicializar variables globales.
        btnMenuBuscadorPokemon = (Button) findViewById(R.id.BuscadorBtnPokemon);
        btnMenuBuscadorTipos = (Button) findViewById(R.id.BuscadorBtnTipos);
        btnMenuBuscadorObjetos = (Button) findViewById(R.id.BuscadorBtnObjetos);
        btnMenuBuscadorAtaques = (Button) findViewById(R.id.BuscadorBtnAtaques);
        btnMenuBuscadorHabilidades = (Button) findViewById(R.id.BuscadorBtnHabilidades);

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscadorPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBuscadorActivity.this, BuscarPokemonActivity.class);
                startActivity(intent);
            }
        });

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscadorHabilidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBuscadorActivity.this, BuscarHabilidadActivity.class);
                startActivity(intent);
            }
        });

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscadorObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBuscadorActivity.this, BuscarObjetosActivity.class);
                startActivity(intent);
            }
        });

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscadorTipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuBuscadorActivity.this, BuscarTiposActivity.class);
                startActivity(intent);
            }
        });
    }
}