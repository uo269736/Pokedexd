package com.example.pokedexd.equipos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pokedexd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MisEquiposActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_equipos);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();


    }
}