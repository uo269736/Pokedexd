package com.example.pokedexd.buscadores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pokedexd.R;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import com.example.pokedexd.adapters.ListaPokemonAdapter;
import models.Pokemon;
import models.PokemonRespuesta;
import models.PokemonRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarPokemonActivity extends AppCompatActivity {

    private static final String TAG="BUSCARPOKEMON";

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;
    private int  offset;
    private boolean aptoParaCargar;
    private ImageButton botonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pokemon);

        recyclerView = (RecyclerView) findViewById(R.id.PokemonRecLista);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        //detectar que hace scroll y al llegar al final obtener nuevos elementos
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if(aptoParaCargar){
                        if((visibleItemCount + pastVisibleItems)>=totalItemCount){
                            Log.i(TAG,"Llegamos al final");
                            aptoParaCargar =false;
                            offset+=20;
                            obtenerDatos(offset);
                        }
                    }
                }
            }
        });

        //Boton para ir hacia atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("");
        Drawable d=getResources().getDrawable(R.drawable.action_bar);
        getSupportActionBar().setBackgroundDrawable(d);

        botonBuscar= (ImageButton) findViewById(R.id.PokemonIbtBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e= (EditText) findViewById(R.id.PokemonEtxBuscador);
                String nombre= e.getText().toString();

                if((nombre.isEmpty() || nombre.trim().equals(""))){
                    aptoParaCargar=true;
                    offset=0;
                    listaPokemonAdapter.eliminarPokemon();
                    obtenerDatos(offset);
                    Snackbar.make(findViewById(R.id.BuscarPokemon), R.string.msg_pokemon_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
                else{
                    buscarPokemon(nombre);
                }
            }
        });

        retrofit= new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aptoParaCargar =true;
        offset=0;
        obtenerDatos(offset);
    }

    private void obtenerDatos(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(20,offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar =true;
                if (response.isSuccessful()) {
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    listaPokemonAdapter.addListaPokemon(listaPokemon);
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarPokemon), R.string.msg_pokemon_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar =true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void buscarPokemon(String pokemon) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRespuestaIndividual> pokemonRespuestaCall = service.obtenerPokemon(pokemon.toLowerCase());

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuestaIndividual>() {
            @Override
            public void onResponse(Call<PokemonRespuestaIndividual> call, Response<PokemonRespuestaIndividual> response) {
                aptoParaCargar =true;
                if (response.isSuccessful()) {
                        PokemonRespuestaIndividual pokemonRespuestaIndividual = response.body();
                        if(pokemonRespuestaIndividual!=null) {
                            ArrayList<Pokemon> listaPokemon = new ArrayList<>();
                            listaPokemon.add(new Pokemon(pokemonRespuestaIndividual.getName(), "https://pokeapi.co/api/v2/pokemon/" + pokemonRespuestaIndividual.getId()));
                            listaPokemonAdapter.addPokemon(listaPokemon);
                            aptoParaCargar =false;
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarPokemon), R.string.msg_pokemon_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuestaIndividual> call, Throwable t) {
                aptoParaCargar =true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}