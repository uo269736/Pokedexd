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

import adapters.ListaObjetosAdapter;
import models.Objeto;
import models.ObjetoRespuesta;
import models.ObjetoRespuestaIndividual;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarObjetosActivity extends AppCompatActivity {

    private static final String TAG="BUSCAROBJETOS";

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaObjetosAdapter listaObjetosAdapter;
    private int  offset;
    private boolean aptoParaCargar;
    private ImageButton botonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_objeto);

        recyclerView = (RecyclerView) findViewById(R.id.ObjetoRecLista);
        listaObjetosAdapter = new ListaObjetosAdapter(this);
        recyclerView.setAdapter(listaObjetosAdapter);
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

        botonBuscar = (ImageButton) findViewById(R.id.ObjetoIbtBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText) findViewById(R.id.ObjetoEtxBuscador);
                String nombre = e.getText().toString();

                if ((nombre.isEmpty() && nombre.equals(" ") && nombre.trim().equals(""))) {
                    aptoParaCargar = true;
                    offset = 0;
                    obtenerDatos(offset);
                } else {
                    buscarObjeto(nombre);
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
        Call<ObjetoRespuesta> objetoRespuestaCall = service.obtenerListaObjetos(20, offset);

        objetoRespuestaCall.enqueue(new Callback<ObjetoRespuesta>() {
            @Override
            public void onResponse(Call<ObjetoRespuesta> call, Response<ObjetoRespuesta> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    ObjetoRespuesta objetoRespuesta = response.body();
                    ArrayList<Objeto> listaObjetos = objetoRespuesta.getResults();
                    for (Objeto o : listaObjetos) {
                        agregarDescripcionNombre(o,"todos");
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ObjetoRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void agregarDescripcionNombre(Objeto o, String opcion){
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<ObjetoRespuestaIndividual> objetoRespuestaCall = service.obtenerObjeto(o.getName().toLowerCase().replace(" ","-"));

        objetoRespuestaCall.enqueue(new Callback<ObjetoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<ObjetoRespuestaIndividual> call, Response<ObjetoRespuestaIndividual> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    ObjetoRespuestaIndividual objetoRespuestaIndividual = response.body();
                    if(objetoRespuestaIndividual.getNames().size()>0 && objetoRespuestaIndividual.getDescripciones().size()>0) {
                        if (objetoRespuestaIndividual.getNames().size() > 1)
                            o.setNombre(objetoRespuestaIndividual.getNames().get(5).getName());
                        else
                            o.setNombre(objetoRespuestaIndividual.getNames().get(0).getName());
                        if (o.getId() < 402 && objetoRespuestaIndividual.getDescripciones().size()>=13)
                            o.setDescripcion(objetoRespuestaIndividual.getDescripciones().get(13).getText());
                        else
                            o.setDescripcion(objetoRespuestaIndividual.getDescripciones().get(0).getText());
                        ArrayList<Objeto> listaO = new ArrayList();
                        listaO.add(o);
                        if (opcion.equals("todos"))
                            listaObjetosAdapter.addListaObjetos(listaO);
                        else if (opcion.equals("uno"))
                            listaObjetosAdapter.addObjeto(listaO);
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ObjetoRespuestaIndividual> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void buscarObjeto(String objeto) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        String objetoNombre=encontrarNombre(objeto);
        Call<ObjetoRespuestaIndividual> objetoRespuestaCall = service.obtenerObjeto(objetoNombre.toLowerCase().replace(" ","-"));

        objetoRespuestaCall.enqueue(new Callback<ObjetoRespuestaIndividual>() {
            @Override
            public void onResponse(Call<ObjetoRespuestaIndividual> call, Response<ObjetoRespuestaIndividual> response) {
                aptoParaCargar =true;
                if (response.isSuccessful()) {
                    if(objetoNombre.equals("") || objetoNombre.isEmpty()){
                        offset=0;
                        aptoParaCargar=true;
                        listaObjetosAdapter.eliminarObjetos();
                        obtenerDatos(offset);
                    }
                    else{
                        ObjetoRespuestaIndividual objetoRespuestaIndividual = response.body();
                        if(objetoRespuestaIndividual!=null) {
                            System.out.println("https://pokeapi.co/api/v2/item/"+objetoRespuestaIndividual.getId());
                            Objeto o=new Objeto(objetoNombre, "https://pokeapi.co/api/v2/item/"+objetoRespuestaIndividual.getId());
                            agregarDescripcionNombre(o,"uno");
                        }
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ObjetoRespuestaIndividual> call, Throwable t) {
                aptoParaCargar =true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    protected String encontrarNombre(String nombreObjeto){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("ObjetosPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[0].toLowerCase().equals(nombreObjeto.toLowerCase()) || datos[1].toLowerCase().equals(nombreObjeto.toLowerCase())) {
                        return datos[0];
                    }
                }
            }
            return nombreObjeto;
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