package com.example.pokedexd.menus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pokedexd.BuscarPokemonActivity;
import com.example.pokedexd.R;

public class MenuBuscadorActivity extends AppCompatActivity {

    private Button btnMenuBuscadorPokemon;
    private Button btnMenuBuscadorTipos;
    private Button btnMenuBuscadorObjetos;
    private Button btnMenuBuscadorAtaques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_buscador);
        //Inicializar variables globales.
        btnMenuBuscadorPokemon = (Button) findViewById(R.id.BuscadorBtnPokemon);
        btnMenuBuscadorTipos = (Button) findViewById(R.id.BuscadorBtnTipos);
        btnMenuBuscadorObjetos = (Button) findViewById(R.id.BuscadorBtnObjetos);
        btnMenuBuscadorAtaques = (Button) findViewById(R.id.BuscadorBtnAtaques);

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscadorPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBuscadorActivity.this, BuscarPokemonActivity.class);
                startActivity(intent);
            }
        });
    }
}