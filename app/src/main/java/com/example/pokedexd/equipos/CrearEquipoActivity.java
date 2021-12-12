package com.example.pokedexd.equipos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.pokedexd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.EquipoPokemonAdapter;
import models.Pokemon;
import models.PokemonRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearEquipoActivity extends AppCompatActivity {

    protected static final String NUEVO_POKEMON = "nuevo_pokemon";
    protected static final String NOMBRE_POKEMON = "nombre_pokemon";

    protected static final int AÑADIR_POKEMON = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ImageButton btnAddPokemon;
    private Button btnGuardar;
    private Button btnExportar;
    private EditText nombrePokemon;
    private Retrofit retrofit;
    private ArrayList<Pokemon> equipoPokemon;
    private RecyclerView recyclerView;
    private EquipoPokemonAdapter equipoAdapter;
    private EditText editTextNombreEquipo;

    private int idPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equipo);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pokedexd-a8a61-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        equipoPokemon = new ArrayList<Pokemon>();

        btnAddPokemon = (ImageButton) findViewById(R.id.CrearEquipoIbtAñade);
        btnGuardar = (Button) findViewById(R.id.CrearEquipoBtnGuardar);
        btnExportar = (Button) findViewById(R.id.CrearEquipoBtnExportar);
        nombrePokemon = (EditText) findViewById(R.id.CrearEquipoEtxBuscaPokemon);
        recyclerView = (RecyclerView) findViewById(R.id.CrearEquipoRecListaEquipo);
        editTextNombreEquipo = (EditText) findViewById(R.id.editTextNombreEquipo);

        equipoAdapter = new EquipoPokemonAdapter(this);
        recyclerView.setAdapter(equipoAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager= new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        retrofit= new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnAddPokemon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(equipoPokemon.size()<6) {
                     btnAddPokemon.setEnabled(false);
                     PokeapiService service = retrofit.create(PokeapiService.class);
                     Call<PokemonRespuestaIndividual> pokemonRespuestaCall = service.obtenerPokemon(nombrePokemon.getText().toString().trim().toLowerCase());

                     pokemonRespuestaCall.enqueue(new Callback<PokemonRespuestaIndividual>() {
                         @Override
                         public void onResponse(Call<PokemonRespuestaIndividual> call, Response<PokemonRespuestaIndividual> response) {
                             if (response.isSuccessful() && !nombrePokemon.getText().toString().isEmpty()) {
                                 idPokemon = response.body().getId();
                                 Intent intent = new Intent(CrearEquipoActivity.this, AddPokemonEquipoActivity.class);
                                 intent.putExtra(NOMBRE_POKEMON, nombrePokemon.getText().toString().toLowerCase());
                                 startActivityForResult(intent, AÑADIR_POKEMON);
                             } else {
                                 Snackbar.make(findViewById(R.id.CrearEquipo), +R.string.msg_pokemon_no_encontrado, Snackbar.LENGTH_SHORT).show();
                                 btnAddPokemon.setEnabled(true);
                             }
                         }

                         @Override
                         public void onFailure(Call<PokemonRespuestaIndividual> call, Throwable t) {
                             Snackbar.make(findViewById(R.id.CrearEquipo), +R.string.msg_fallo, Snackbar.LENGTH_SHORT).show();
                         }
                     });
                 } else {
                     Snackbar.make(findViewById(R.id.CrearEquipo), R.string.msg_equipo_completo, Snackbar.LENGTH_SHORT).show();
                 }
             }
        });

        btnAddPokemon.setEnabled(true);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextNombreEquipo.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.CrearEquipoBtnGuardar), "Ponga un nombre al equipo", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (equipoPokemon.size() == 6) {
                    String id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> map = new HashMap<>();
                        for (int i = 0; i < equipoPokemon.size(); i++) {
                            map.put("Habilidad", equipoPokemon.get(i).getHabilidad());
                            map.put("Objeto", equipoPokemon.get(i).getObjeto());
                            map.put("Ataques", equipoPokemon.get(i).getAtaques().toString());

                    mDatabase.child("users").child(id).child("Equipos").child(editTextNombreEquipo.getText().toString()).child(equipoPokemon.get(i).getName()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Log.d("Registro", task.getException().getMessage());
                                Toast.makeText(CrearEquipoActivity.this, "No se pudieron meter los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });}
                        Toast.makeText(CrearEquipoActivity.this, "Equipo creado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CrearEquipoActivity.this, MisEquiposActivity.class);
                        startActivity(intent);
                    } else {
                    Snackbar.make(findViewById(R.id.CrearEquipoBtnGuardar), "El equipo debe ser de 6 pokemon", Snackbar.LENGTH_SHORT).show();
                }}
            }
        });


        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXPORT DE UN POKEMON
                //    Thundurus-Therian @ Leftovers
                //    Ability: Volt Absorb
                //    - Nasty Plot
                //    - Thunderbolt
                //    - Hidden Power [Ice]
                //    - Agility

                String equipoPokemonShowdown="";
                for (Pokemon p: equipoPokemon) {
                    equipoPokemonShowdown+=p.stringShowdown()+"\n";
                }
                ClipData clip = ClipData.newPlainText("text", equipoPokemonShowdown);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(findViewById(R.id.CrearEquipo), R.string.msg_equipo_exportar, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Nos aseguramos que el resultado fue OK
        if(requestCode== AÑADIR_POKEMON) {
            if (resultCode == RESULT_OK) {
                Pokemon nuevoPokemon = data.getParcelableExtra(NUEVO_POKEMON);
                nuevoPokemon.setId(idPokemon);
                equipoPokemon.add(nuevoPokemon);
                equipoAdapter.eliminarPokemon();
                equipoAdapter.addListaPokemon(equipoPokemon);

                Snackbar.make(findViewById(R.id.CrearEquipo),nuevoPokemon.getName()+" ha sido añadido al equipo",Snackbar.LENGTH_SHORT).show();
                btnAddPokemon.setEnabled(true);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("CrearEquipoActivity", "Añadir pokemon cancelado");
            }
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            eliminarPokemon(equipoPokemon.get(viewHolder.getAdapterPosition()));
        }
    };

    private void eliminarPokemon(Pokemon pokemon){
        equipoPokemon.remove(pokemon);
        equipoAdapter.eliminarPokemon(pokemon);
    }

}