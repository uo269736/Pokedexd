package com.example.pokedexd.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedexd.R;
import com.example.pokedexd.menus.MenuPrincipalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.database.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView nombreBienvenida;
    private EditText correo;
    private EditText nombre;
    private Button   btnCerrarSesion;
    private Button   btnEditar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase         = database.getReference();
        mAuth             = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String       uid  = user.getUid();

        nombreBienvenida  = findViewById(R.id.txtNombre);
        btnCerrarSesion   = findViewById(R.id.btnCerrarSesion);
        btnEditar         = findViewById(R.id.btnEditar);
        correo            = findViewById(R.id.txtCorreo);
        nombre            = findViewById(R.id.editTextName);

        //No se puede editar este campo
        correo.setKeyListener(null);

        mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Usuario user    = task.getResult().getValue(Usuario.class);
                    String username = String.valueOf(user.getNombre());
                    String email    = String.valueOf(user.getEmail());
                    correo.setText(email);
                    nombre.setText(username);
                    nombreBienvenida.setText("¡Bienvenido " + username + "!");
                } else {
                    Toast.makeText(PerfilActivity.this,"No se ha podido cargar el perfil.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String username = nombre.getText().toString();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child("users").child(uid).child("nombre").setValue(nombre.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PerfilActivity.this, "Se ha editado el nombre de usuario correctamente.", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else
                            Toast.makeText(PerfilActivity.this,"No se ha podido editar el nombre de usuario.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(PerfilActivity.this, "Se ha cerrado sesión correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PerfilActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
            }
        });
    }
}