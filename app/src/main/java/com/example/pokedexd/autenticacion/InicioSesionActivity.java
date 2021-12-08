package com.example.pokedexd.autenticacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokedexd.R;
import com.example.pokedexd.menus.MenuPrincipalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioSesionActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnNuevoUsuario;
    private Button btnIniciarSesion;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmailIniciarSesion);
        editTextPassword = findViewById(R.id.editTextContraseñaIniciarSesion);

        btnNuevoUsuario = findViewById(R.id.btnRegistroActivity);
        btnNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicioSesionActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

    }

    private void iniciarSesion() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        Log.d("d",email + password);

        if (!password.isEmpty() && !email.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(InicioSesionActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InicioSesionActivity.this, MenuPrincipalActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(InicioSesionActivity.this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else
            Toast.makeText(InicioSesionActivity.this, "No puede haber campos vacíos.", Toast.LENGTH_SHORT).show();
    }


}