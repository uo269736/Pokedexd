package com.example.pokedexd.buscadores;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.pokedexd.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapters.ListaTiposAdapter;
import models.Relacion;
import models.Tipo;
import models.TipoRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TiposFragment extends Fragment {

    private static final String TAG = "LISTATIPOS";

    private RecyclerView recyclerView;
    private ListaTiposAdapter listaTiposAdapter;
    private Retrofit retrofit;
    private Context context;
    private String nombreTipo;
    private String ataque;
    private List<String> debil;
    private List<String> eficaz;
    private List<String> inmune;


    public TiposFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tipos, container, false);
        context = root.getContext();
        listaTiposAdapter = new ListaTiposAdapter(context);
        recyclerView = root.findViewById(R.id.recycler_tipos);
        recyclerView.setAdapter(listaTiposAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BottomNavigationView navView = root.findViewById(R.id.nav_view_fragment);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        debil = new ArrayList<>();
        eficaz = new ArrayList<>();
        inmune = new ArrayList<>();
        nombreTipo = traducirTipoIngles(nombreTipo).toLowerCase();
        llenarListas();
        return root;
    }

    public static TiposFragment newInstance(String nombreTipo, String ataque) {
        TiposFragment fragment = new TiposFragment();
        fragment.nombreTipo = nombreTipo;
        fragment.ataque = ataque;
        return fragment;
    }

    private void llenarListas() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<TipoRespuestaIndividual> tipoRespuestaIndividualCall = service.obtenerDebilidades(nombreTipo);
        eficaz.clear();
        debil.clear();
        inmune.clear();
        tipoRespuestaIndividualCall.enqueue(new Callback<TipoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<TipoRespuestaIndividual> call, Response<TipoRespuestaIndividual> response) {
                if (response.isSuccessful()) {
                    TipoRespuestaIndividual tipoRespuestaIndividual = response.body();
                    List<String> mostrar = new ArrayList<>();
                    Relacion r = tipoRespuestaIndividual.getDamage_relations();
                    if (ataque.equals(BuscarTiposActivity.INFLIGE)) {
                        for(Tipo t : r.getHalf_damage_to())
                            debil.add(traducirTipoEspañol(t.getName()));
                        for(Tipo t : r.getDouble_damage_to())
                            eficaz.add(traducirTipoEspañol(t.getName()));
                        for(Tipo t : r.getNo_damage_to())
                            inmune.add(traducirTipoEspañol(t.getName()));
                        }
                    if(ataque.equals(BuscarTiposActivity.RECIBE)) {
                        for(Tipo t : r.getHalf_damage_from())
                            debil.add(traducirTipoEspañol(t.getName()));
                        for(Tipo t : r.getDouble_damage_from())
                            eficaz.add(traducirTipoEspañol(t.getName()));
                        for(Tipo t : r.getNo_damage_from())
                            inmune.add(traducirTipoEspañol(t.getName()));
                }
            }
            }

            @Override
            public void onFailure(Call<TipoRespuestaIndividual> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void cargarLista(String fortaleza) {
        listaTiposAdapter.removeListaTipos();
        switch(fortaleza) {
            case "debil":
                listaTiposAdapter.addListaTipos(debil);
                break;
            case "eficaz":
                listaTiposAdapter.addListaTipos(eficaz);
                break;
            case "inmune":
                listaTiposAdapter.addListaTipos(inmune);
                break;
        }
    }

    private String traducirTipoEspañol(String tipo) {
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            file = context.getAssets().open("TiposPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos != null) {
                    if (datos[1].toLowerCase().equals(tipo)
                            || datos[2].toLowerCase().equals(tipo)) {
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

    private String traducirTipoIngles(String tipo) {
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        tipo = tipo.toLowerCase();
        try {
            file = context.getAssets().open("TiposPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos != null) {
                    if (datos[1].toLowerCase().equals(tipo)
                            || datos[2].toLowerCase().equals(tipo)) {
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


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btx0:
                    cargarLista("inmune");
                    return true;

                case R.id.btx2:
                    cargarLista("eficaz");
                    return true;

                case R.id.btx12:
                    cargarLista("debil");
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        }
    };


}