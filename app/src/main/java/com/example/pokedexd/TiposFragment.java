package com.example.pokedexd;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapters.ListaTiposAdapter;
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
    private String fortaleza;


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
        recyclerView = root.findViewById(R.id.recycerTipos);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        cargarLista();
        recyclerView.setAdapter(listaTiposAdapter);

        return root;
    }

    public static TiposFragment newInstance(String nombreTipo, String fortaleza) {
        TiposFragment fragment = new TiposFragment();
        fragment.nombreTipo = nombreTipo;
        fragment.fortaleza = fortaleza;
        return fragment;
    }

    private void cargarLista() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeapiService service = retrofit.create(PokeapiService.class);

        Call<TipoRespuestaIndividual> tipoRespuestaIndividualCall = service.obtenerDebilidades(nombreTipo);
        tipoRespuestaIndividualCall.enqueue(new Callback<TipoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<TipoRespuestaIndividual> call, Response<TipoRespuestaIndividual> response) {
                if (response.isSuccessful()) {
                    TipoRespuestaIndividual tipoRespuestaIndividual = response.body();
                    List<String> mostrar = new ArrayList<>();
                    switch (fortaleza) {
                        case "debil":
                            for (Tipo t : tipoRespuestaIndividual.getDamage_relations().getHalf_damage_to())
                                mostrar.add("aay");
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


}