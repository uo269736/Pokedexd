package com.example.pokedexd.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedexd.R;
import com.example.pokedexd.autenticacion.InicioSesionActivity;
import com.example.pokedexd.autenticacion.PerfilActivity;
import com.example.pokedexd.equipos.MisEquiposActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnMenuBuscador;
    private Button btnMenuEquipo;
    private Button btnSesion;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Inicializar variables globales.
        btnMenuBuscador  = (Button) findViewById(R.id.MenuBtnBuscador);
        btnMenuEquipo    = (Button) findViewById(R.id.MenuBtnEquipo);
        btnSesion        = (Button) findViewById(R.id.MenuBtnSesion);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        //Comprueba si ya se ha iniciado sesión. Si ya hay una sesión se carga el nombr edel usuario, sino se abre la opción a que inicie sesión.
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            btnSesion.setText("Login");
            btnSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MenuPrincipalActivity.this, InicioSesionActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            btnSesion.setText("Perfil");
            btnSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MenuPrincipalActivity.this, PerfilActivity.class);
                    startActivity(intent);
                }
            });
        }

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscador.setOnClickListener(new View.OnClickListener() { //Pasar al buscador cuadno se clicka
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalActivity.this, MenuBuscadorActivity.class);
                startActivity(intent);
            }
        });

        btnMenuEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (user == null) {
                    intent = new Intent(MenuPrincipalActivity.this, InicioSesionActivity.class);
                    Toast.makeText(MenuPrincipalActivity.this, "Se ha de iniciar sesión para Crear un Equipo", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(MenuPrincipalActivity.this, MisEquiposActivity.class);
                }
                startActivity(intent);
            }
        });
    }
}