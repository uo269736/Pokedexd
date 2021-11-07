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
import com.example.pokedexd.menus.MenuPrincipalSesionIniciadaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnRegistro;
    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String nombre;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        btnRegistro = (Button) findViewById(R.id.btnRegistrarse);
        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = editTextNombre.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (!password.isEmpty() && !email.isEmpty() && !nombre.isEmpty()){
                    if (password.length() >= 6)
                        registrarUsuario();
                    else
                        Toast.makeText(RegistroActivity.this, "La contraseña debe de ser de 6 caracteres o más", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(RegistroActivity.this, "No pueden haber campos vacíos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarUsuario() {
        Log.d("d",email + password);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Registro", "Registrandose");
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("email", email);
                    map.put("contraseña", password);

                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Log.d("Registro", "signInWithEmail:success");
                                Toast.makeText(RegistroActivity.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistroActivity.this, MenuPrincipalSesionIniciadaActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("Registro", task2.getException().getMessage());
                                Toast.makeText(RegistroActivity.this, "No se pudieron meter los datos correctamente", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    Log.d("Registro", task.getException().getMessage());
                    Toast.makeText(RegistroActivity.this, "No se puede crear", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}