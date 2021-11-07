package com.example.pokedexd.menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pokedexd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Usuario;

public class MenuPrincipalSesionIniciadaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnMenuBuscador;
    private Button btnMenuEquipo;
    private Button btnMenuLiga;
    private TextView txtBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_sesion_iniciada);
        //Inicializar variables globales.
        btnMenuBuscador = (Button) findViewById(R.id.MenuBtnBuscador);
        btnMenuEquipo = (Button) findViewById(R.id.MenuBtnEquipo);
        btnMenuLiga = (Button) findViewById(R.id.MenuBtnLiga);
        txtBienvenida = (TextView) findViewById(R.id.txtBienvenida);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("users").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Usuario user = task.getResult().getValue(Usuario.class);
                    txtBienvenida.setText("Bienvenido " + user.getNombre() + "!");
                }
            }
        });

        //Pasar a las respectivas pantallas al clickar el boton.
        btnMenuBuscador.setOnClickListener(new View.OnClickListener() { //Pasar al buscador cuadno se clicka
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalSesionIniciadaActivity.this, MenuBuscadorActivity.class);
                startActivity(intent);
            }
        });


    }
}