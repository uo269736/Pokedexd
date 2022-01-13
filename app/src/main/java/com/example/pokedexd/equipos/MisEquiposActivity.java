package com.example.pokedexd.equipos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.pokedexd.adapters.MisEquiposAdapter;
import com.example.pokedexd.models.Pokemon;
import com.example.pokedexd.models.database.Equipo;

public class MisEquiposActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private ArrayList<Equipo> misEquipos;
    private RecyclerView recyclerView;
    private Button btnCrearEquipo;
    private Context context;
    private MisEquiposAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_equipos);

        recyclerView = findViewById(R.id.rvMisEquipos);
        context = this;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();

        //Boton para ir hacia atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("");
        Drawable d=getResources().getDrawable(R.drawable.action_bar);
        getSupportActionBar().setBackgroundDrawable(d);

        btnCrearEquipo = (Button) findViewById(R.id.btnCrearEquipo);

        btnCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisEquiposActivity.this, CrearEquipoActivity.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        misEquipos = new ArrayList<>();
        cargarEquipos();
    }

    private void cargarEquipos() {
        mDatabase.child("users").child(uid).child("Equipos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.e("firebase", String.valueOf(task.getResult().getValue()));
                    String nombreEquipo = "";
                    String nombrePokemon = "";
                    String objeto = "";
                    String habilidad = "";
                    String ataques = "";
                    int pokemonId = 0;
                    ArrayList<String> ataquesList = new ArrayList<>();
                    for (DataSnapshot equipo : task.getResult().getChildren()) {
                        nombreEquipo = equipo.getKey();
                        Log.e("firebase, nombre equipo", String.valueOf(nombreEquipo));
                        List<Pokemon> pokemonList = new ArrayList<>();
                        for (DataSnapshot pokemon : task.getResult().child(nombreEquipo).getChildren()) {
                            nombrePokemon = pokemon.getKey();
                            Pokemon p = new Pokemon(nombrePokemon);
                            Log.e("firebase, nombre pokemon", String.valueOf(pokemon.getKey()));
                            for (DataSnapshot atributo : task.getResult().child(nombreEquipo).child(nombrePokemon).getChildren()) {
                                if (atributo.getKey().equals("Ataques")) {
                                    ataques = String.valueOf(task.getResult().child(nombreEquipo).child(nombrePokemon).child(atributo.getKey()).getValue());
                                    String[] atq = ataques.replace("[", "").replace("]", "").split(",");
                                    ataquesList = new ArrayList<String>(Arrays.asList(atq.clone()));
                                    p.setAtaques(ataquesList);
                                    Log.e("firebase, ataques", ataquesList.toString());
                                } else if (atributo.getKey().equals("Objeto")) {
                                    objeto = String.valueOf(task.getResult().child(nombreEquipo).child(nombrePokemon).child(atributo.getKey()).getValue());
                                    p.setObjeto(objeto);
                                    Log.e("firebase, objeto", objeto);
                                } else if (atributo.getKey().equals("Habilidad")) {
                                    habilidad = String.valueOf(task.getResult().child(nombreEquipo).child(nombrePokemon).child(atributo.getKey()).getValue());
                                    p.setHabilidad(habilidad);
                                    Log.e("firebase, habilidad", habilidad);
                                } else if (atributo.getKey().equals("PokemonId")) {
                                    pokemonId = Integer.parseInt(String.valueOf(task.getResult().child(nombreEquipo).child(nombrePokemon).child(atributo.getKey()).getValue()));
                                    p.setId(pokemonId);
                                    Log.e("firebase, pokemonId", Integer.toString(pokemonId));
                                }
                            }
                            pokemonList.add(p);
                        }
                        Equipo e = new Equipo(nombreEquipo, pokemonList);
                        misEquipos.add(e);
                    }
                    adapter = new MisEquiposAdapter(context, misEquipos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MisEquiposActivity.this, "No se pueden cargar los equipos", Toast.LENGTH_SHORT).show();
                    Log.e("firebase", "Error.");
                }
            }
        });
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            eliminarEquipo(misEquipos.get(viewHolder.getAdapterPosition()));
        }
    };

    private void eliminarEquipo(Equipo equipo) {
        misEquipos.remove(equipo);
        adapter.eliminarEquipo(equipo);
        mDatabase.child("users").child(uid).child("Equipos").child(equipo.getNombre()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.e("firebase", "Se ha borrado el equipo correctamente " + equipo.getNombre());
                }
            }
        });
    }
}