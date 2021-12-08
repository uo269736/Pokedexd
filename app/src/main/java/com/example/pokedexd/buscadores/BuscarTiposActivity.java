package com.example.pokedexd.buscadores;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import adapters.ListaTiposAdapter;
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
    private RecyclerView recyclerView;
    private ListaTiposAdapter listaTiposAdapter;

    private static final int NUM_TIPOS = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos);

        spTipos = findViewById(R.id.spTipos);
        recyclerView = findViewById(R.id.recycler_tipos);
        listaTiposAdapter = new ListaTiposAdapter(this);
        recyclerView.setAdapter(listaTiposAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        rellenarSpinner();
        /*spTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accionSpinner(navView.getMenu().getItem(navView.getSelectedItemId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

    }

    /*private void accionSpinner(MenuItem item) {
        if (!spTipos.getSelectedItem().toString().equals("")) {
            switch (item.getItemId()) {
                case R.id.btDebil:
                    //Creamos el framento de información
                    TiposFragment debil = new TiposFragment().
                            newInstance(spTipos.getSelectedItem().toString(), "debil", BuscarTiposActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, debil).commit();

                case R.id.btEficaz:
                    TiposFragment eficaz = new TiposFragment().newInstance(spTipos.getSelectedItem().toString(), "eficaz", BuscarTiposActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, eficaz).commit();

                case R.id.btInmune:

                    TiposFragment inmune = new TiposFragment().newInstance(spTipos.getSelectedItem().toString(), "inmune", BuscarTiposActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, inmune).commit();
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        }

    }*/

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
                        String tipo = traducirTipoEspañol(t.getName());
                        if(tipo!="")
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

    private String traducirTipoEspañol(String tipo) {
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
                    if (datos[1].toLowerCase().equals(tipo.toLowerCase()) || datos[2].toLowerCase().equals(tipo.toLowerCase())) {
                        return datos[2]; //es el nombre en español
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
            if (!spTipos.getSelectedItem().toString().equals("")) {
                switch (item.getItemId()) {
                    case R.id.btDebil:
                        //Creamos el framento de información
                       /* TiposFragment debil = new TiposFragment().
                                newInstance(traducirTipoIngles(spTipos.getSelectedItem().toString()), "debil");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, debil).commit();*/
                        cargarLista(traducirTipoIngles(spTipos.getSelectedItem().toString()),"debil");
                        return true;

                    case R.id.btEficaz:
                       /* TiposFragment eficaz = new TiposFragment().newInstance(traducirTipoIngles(spTipos.getSelectedItem().toString()), "eficaz");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, eficaz).commit();*/
                        cargarLista(traducirTipoIngles(spTipos.getSelectedItem().toString()),"eficaz");
                        return true;

                    case R.id.btInmune:

                       /* TiposFragment inmune = new TiposFragment().newInstance(traducirTipoIngles(spTipos.getSelectedItem().toString()), "inmune");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFragments, inmune).commit();*/
                        cargarLista(traducirTipoIngles(spTipos.getSelectedItem().toString()),"inmune");
                        return true;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }

            }
            return false;
        }
    };

    private String traducirTipoIngles(String tipo) {
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
                    if (datos[1].toLowerCase().equals(tipo.toLowerCase()) || datos[2].toLowerCase().equals(tipo.toLowerCase())) {
                        return datos[1]; //es el nombre en ingles
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

    private void cargarLista(String nombreTipo, String fortaleza) {
        PokeapiService service = retrofit.create(PokeapiService.class);

        Call<TipoRespuestaIndividual> tipoRespuestaIndividualCall = service.obtenerDebilidades(nombreTipo.toLowerCase());
        tipoRespuestaIndividualCall.enqueue(new Callback<TipoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<TipoRespuestaIndividual> call, Response<TipoRespuestaIndividual> response) {
                if (response.isSuccessful()) {
                    TipoRespuestaIndividual tipoRespuestaIndividual = response.body();
                    List<String> mostrar = new ArrayList<>();
                    switch (fortaleza) {
                        case "debil":
                            for (Tipo t : tipoRespuestaIndividual.getDamage_relations().getHalf_damage_to())
                                mostrar.add(traducirTipoEspañol(t.getName()));
                            break;
                        case "eficaz":
                            for (Tipo t : tipoRespuestaIndividual.getDamage_relations().getDouble_damage_to())
                                mostrar.add(traducirTipoEspañol(t.getName()));
                            break;
                        case "inmune":
                            for (Tipo t : tipoRespuestaIndividual.getDamage_relations().getNo_damage_to())
                                mostrar.add(traducirTipoEspañol(t.getName()));
                            break;
                    }
                    listaTiposAdapter.removeListaTipos();
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
