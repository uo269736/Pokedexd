package com.example.pokedexd.menus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pokedexd.R;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnMenuBuscador;
    private Button btnMenuEquipo;
    private Button btnMenuLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        //Inicializar variables globales.
        btnMenuBuscador = (Button) findViewById(R.id.MenuBtnBuscador);
        btnMenuEquipo = (Button) findViewById(R.id.MenuBtnEquipo);
        btnMenuLiga = (Button) findViewById(R.id.MenuBtnLiga);

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalActivity.this, MenuBuscadorActivity.class);
                startActivity(intent);
            }
        });
    }
}