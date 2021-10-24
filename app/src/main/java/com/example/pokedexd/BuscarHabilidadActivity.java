package com.example.pokedexd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import adapters.ListaHabilidadesAdapter;
import models.Habilidad;
import models.HabilidadRespuesta;
import models.HabilidadRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarHabilidadActivity extends AppCompatActivity {

    private static final String TAG = "BUSCARHABILIDAD";

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaHabilidadesAdapter listaHabilidadesAdapter;
    private int offset;
    private boolean aptoParaCargar;
    private ImageButton botonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_habilidad);

        recyclerView = (RecyclerView) findViewById(R.id.HabilidadRecLista);
        listaHabilidadesAdapter = new ListaHabilidadesAdapter(this);
        recyclerView.setAdapter(listaHabilidadesAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        //detectar que hace scroll y al llegar al final obtener nuevos elementos
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, "Llegamos al final");
                            aptoParaCargar = false;
                            offset += 20;
                            obtenerDatos(offset);
                        }
                    }
                }
            }
        });

        botonBuscar = (ImageButton) findViewById(R.id.HabilidadIbtBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText) findViewById(R.id.HabilidadEtxBuscador);
                String nombre = e.getText().toString();

                if ((nombre.isEmpty() && nombre.equals(" ") && nombre.trim().equals(""))) {
                    aptoParaCargar = true;
                    offset = 0;
                    obtenerDatos(offset);
                } else {
                    buscarHabilidad(nombre);
                }

            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aptoParaCargar = true;
        offset = 0;
        obtenerDatos(offset);
    }

    private void obtenerDatos(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<HabilidadRespuesta> habilidadRespuestaCall = service.obtenerListaHabilidades(20, offset);

        habilidadRespuestaCall.enqueue(new Callback<HabilidadRespuesta>() {
            @Override
            public void onResponse(Call<HabilidadRespuesta> call, Response<HabilidadRespuesta> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    HabilidadRespuesta habilidadRespuesta = response.body();
                    ArrayList<Habilidad> listaHabilidades = habilidadRespuesta.getResults();
                    for (Habilidad h : listaHabilidades) {
                        agregarDescripcionNombre(h,"todos");
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HabilidadRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void agregarDescripcionNombre(Habilidad h, String opcion){
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<HabilidadRespuestaIndividual> habilidadRespuestaCall = service.obtenerHabilidad(h.getName().toLowerCase().replace(" ","-"));

        habilidadRespuestaCall.enqueue(new Callback<HabilidadRespuestaIndividual>() {
            @Override
            public void onResponse(Call<HabilidadRespuestaIndividual> call, Response<HabilidadRespuestaIndividual> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    HabilidadRespuestaIndividual habilidadRespuestaIndividual = response.body();
                    if(habilidadRespuestaIndividual.getNames().size()>0 && habilidadRespuestaIndividual.getDescripciones().size()>0) {
                        if (habilidadRespuestaIndividual.getNames().size() > 1)
                            h.setNombre(habilidadRespuestaIndividual.getNames().get(5).getName());
                        else
                            h.setNombre(habilidadRespuestaIndividual.getNames().get(0).getName());
                        if (h.getId() < 77)
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(13).getFlavor_text());
                        else if (h.getId() < 124)
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(10).getFlavor_text());
                        else if (h.getId() < 165)
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(7).getFlavor_text());
                        else if (h.getId() < 192)
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(4).getFlavor_text());
                        else if (h.getId() < 234)
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(5).getFlavor_text());
                        else
                            h.setDescripcion(habilidadRespuestaIndividual.getDescripciones().get(0).getFlavor_text());
                        ArrayList<Habilidad> listaH = new ArrayList();
                        listaH.add(h);
                        if (opcion.equals("todos"))
                            listaHabilidadesAdapter.addListaHabilidades(listaH);
                        else if (opcion.equals("uno"))
                            listaHabilidadesAdapter.addHabilidad(listaH);
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HabilidadRespuestaIndividual> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void buscarHabilidad(String habilidad) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        String habilidadNombre=encontrarNombre(habilidad);
        Call<HabilidadRespuestaIndividual> habilidadRespuestaCall = service.obtenerHabilidad(habilidadNombre.toLowerCase().replace(" ","-"));

        habilidadRespuestaCall.enqueue(new Callback<HabilidadRespuestaIndividual>() {
            @Override
            public void onResponse(Call<HabilidadRespuestaIndividual> call, Response<HabilidadRespuestaIndividual> response) {
                aptoParaCargar =true;
                if (response.isSuccessful()) {
                    if(habilidadNombre.equals("") || habilidadNombre.isEmpty()){
                        offset=0;
                        aptoParaCargar=true;
                        listaHabilidadesAdapter.eliminarHabilidades();
                        obtenerDatos(offset);
                    }
                    else{
                        HabilidadRespuestaIndividual habilidadRespuestaIndividual = response.body();
                        if(habilidadRespuestaIndividual!=null) {
                            Habilidad h=new Habilidad(habilidadNombre, "https://pokeapi.co/api/v2/ability/"+habilidadRespuestaIndividual.getId());
                            agregarDescripcionNombre(h,"uno");
                        }
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HabilidadRespuestaIndividual> call, Throwable t) {
                aptoParaCargar =true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    protected String encontrarNombre(String nombreHabilidad){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("HabilidadesPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                   if(datos[1].toLowerCase().equals(nombreHabilidad.toLowerCase()) || datos[2].toLowerCase().equals(nombreHabilidad.toLowerCase())) {
                       return datos[0];
                   }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}