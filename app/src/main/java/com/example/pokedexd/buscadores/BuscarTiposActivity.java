package com.example.pokedexd.buscadores;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapters.ListaTiposAdapter;
import models.PokemonRespuesta;
import models.Relacion;
import models.Tipo;
import models.TipoRespuesta;
import models.TipoRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarTiposActivity extends AppCompatActivity {

    private static final String TAG = "BUSCARTIPOS";
    private Spinner spTipos;
    private Retrofit retrofit;
    private String tipoSeleccionado;
    private RecyclerView recyclerView;
    private ListaTiposAdapter listaTiposAdapter;
    private BottomNavigationView nav;

    private static final int NUM_TIPOS = 18;

    public static final String INFLIGE="inflige";
    public static final String RECIBE="recibe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos);

        spTipos = findViewById(R.id.spTipos);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        listaTiposAdapter = new ListaTiposAdapter(this);
        recyclerView = findViewById(R.id.recycler_tipos);
        recyclerView.setAdapter(listaTiposAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nav = findViewById(R.id.nav_view);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        rellenarSpinner();
        spTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void accionSpinner() {
        if (!spTipos.getSelectedItem().toString().equals("")) {
            this.tipoSeleccionado = spTipos.getSelectedItem().toString().toLowerCase();
            nav.setSelectedItemId(R.id.btDebil);
        }

    }

    private void rellenarSpinner() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<TipoRespuesta> tipoCall = service.obtenerListaTipos(NUM_TIPOS);

        tipoCall.enqueue(new Callback<TipoRespuesta>() {
            @Override
            public void onResponse(Call<TipoRespuesta> call, Response<TipoRespuesta> response) {
                if (response.isSuccessful()) {
                    TipoRespuesta tipoRespuesta = response.body();
                    ArrayList<Tipo> tipos = tipoRespuesta.getResults();
                    ArrayList<String> nombres = new ArrayList<>();
                    nombres.add("");
                    for (Tipo t : tipos) {
                        String tipo = traducirTipo(t.getName());
                        if (tipo != "")
                            //nombres.add(tipo.substring(0, 1).toUpperCase() + tipo.substring(1));
                            nombres.add(tipo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(BuscarTiposActivity.this, android.R.layout.simple_spinner_item, nombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTipos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<TipoRespuesta> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String traducirTipo(String tipo) {
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            file = getAssets().open("TiposPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos != null) {
                    if (datos[1].toLowerCase().equals(tipo.toLowerCase())) {
                        return datos[2]; //es el nombre en español
                    }

                    if(datos[2].toLowerCase().equals((tipo.toLowerCase()))) {
                        return datos[1];
                    }
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(spTipos!=null && spTipos.getSelectedItem()!=null) {
                if (!spTipos.getSelectedItem().toString().equals("")) {
                    switch (item.getItemId()) {
                        case R.id.btDebil:

                            llenarListas("debil");
                            return true;

                        case R.id.btEficaz:
                            llenarListas("eficaz");
                            return true;

                        case R.id.btInmune:
                            llenarListas("inmune");
                            return true;

                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }

                }
            }
            return false;
        }
    };

    private void llenarListas(String fortaleza) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<TipoRespuestaIndividual> tipoRespuestaIndividualCall = service.obtenerDebilidades(traducirTipo(tipoSeleccionado).toLowerCase());
        tipoRespuestaIndividualCall.enqueue(new Callback<TipoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<TipoRespuestaIndividual> call, Response<TipoRespuestaIndividual> response) {
                if (response.isSuccessful()) {
                    TipoRespuestaIndividual tipoRespuestaIndividual = response.body();
                    List<String> mostrar = new ArrayList<>();
                    Relacion r = tipoRespuestaIndividual.getDamage_relations();
                    switch(fortaleza) {
                        case "debil":
                            for(Tipo t : r.getHalf_damage_to())
                                mostrar.add(traducirTipo(t.getName()));
                            break;
                        case "eficaz":
                            for(Tipo t : r.getDouble_damage_to())
                                mostrar.add(traducirTipo(t.getName()));
                            break;
                        case "inmune":
                            for(Tipo t : r.getNo_damage_to())
                                mostrar.add(traducirTipo(t.getName()));
                            break;
                    }

                listaTiposAdapter.addListaTipos(mostrar);
                }
            }

            @Override
            public void onFailure(Call<TipoRespuestaIndividual> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
