package com.example.pokedexd.menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokedexd.R;
import com.example.pokedexd.autenticacion.InicioSesionActivity;
import com.example.pokedexd.autenticacion.RegistroActivity;
import com.example.pokedexd.equipos.ActivityAux;
import com.example.pokedexd.ligas.ActivityAuxLigas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.database.Usuario;

public class MenuPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnMenuBuscador;
    private Button btnMenuEquipo;
    private Button btnMenuLiga;
    private Button btnIniciarSesion;
    private Button btnCerrarSesion;

    private TextView txtBienvenida;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Inicializar variables globales.
        btnMenuBuscador = (Button) findViewById(R.id.MenuBtnBuscador);
        btnMenuEquipo = (Button) findViewById(R.id.MenuBtnEquipo);
        btnMenuLiga = (Button) findViewById(R.id.MenuBtnLiga);
        btnIniciarSesion = (Button) findViewById(R.id.menuBtnInicioSesion);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        txtBienvenida = (TextView) findViewById(R.id.txtBienvenidaMenuPrincipal);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        //Comprueba si ya se ha iniciado sesión. Si ya hay una sesión se carga el nombr edel usuario, sino se abre la opción a que inicie sesión.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            btnCerrarSesion.setVisibility(View.GONE);
            txtBienvenida.setVisibility(View.GONE);
        } else {
            btnIniciarSesion.setVisibility(View.GONE);
            uid = user.getUid();
            Log.d("d", uid);
            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        Usuario user = task.getResult().getValue(Usuario.class);
                        txtBienvenida.setText("Bienvenido " + user.getNombre() + "!");
                    }
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

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalActivity.this, InicioSesionActivity.class);
                startActivity(intent);
            }
        });

        btnMenuLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (user == null) {
                    intent = new Intent(MenuPrincipalActivity.this, InicioSesionActivity.class);
                    Toast.makeText(MenuPrincipalActivity.this, "Se ha de iniciar sesión para Crear una Liga", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(MenuPrincipalActivity.this, ActivityAuxLigas.class);
                }
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
                    intent = new Intent(MenuPrincipalActivity.this, ActivityAux.class);
                }
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

    }

}