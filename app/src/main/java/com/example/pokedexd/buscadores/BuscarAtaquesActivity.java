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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.pokedexd.adapters.ListaAtaquesAdapter;
import models.Ataque;
import models.AtaqueRespuesta;
import models.AtaqueRespuestaIndividual;
import models.DescripcionA;

import models.Nombre;
import pokeapi.PokeapiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarAtaquesActivity extends AppCompatActivity {

    private static final String TAG = "BUSCARATAQUE";

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaAtaquesAdapter listaAtaquesAdapter;
    private int offset;
    private boolean aptoParaCargar;
    private ImageButton botonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_ataques);

        recyclerView = findViewById(R.id.AtaqueRecLista);
        listaAtaquesAdapter = new ListaAtaquesAdapter(this);
        recyclerView.setAdapter(listaAtaquesAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

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

        //Boton para ir hacia atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("");
        Drawable d=getResources().getDrawable(R.drawable.action_bar);
        getSupportActionBar().setBackgroundDrawable(d);

        botonBuscar = findViewById(R.id.AtaqueIbtBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText) findViewById(R.id.AtaqueEtxBuscador);
                String nombre = e.getText().toString();

                if ((nombre.isEmpty() || nombre.trim().equals(""))) {
                    aptoParaCargar = true;
                    offset = 0;
                    listaAtaquesAdapter.eliminarAtaques();
                    obtenerDatos(offset);
                    Snackbar.make(findViewById(R.id.BuscarAtaques), R.string.msg_ataque_no_encontrado, Snackbar.LENGTH_SHORT).show();
                } else {
                    buscarAtaque(nombre);
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
        Call<AtaqueRespuesta> ataqueRespuestaCall = service.obtenerListaAtaque(20, offset);

        ataqueRespuestaCall.enqueue(new Callback<AtaqueRespuesta>() {
            @Override
            public void onResponse(Call<AtaqueRespuesta> call, Response<AtaqueRespuesta> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    AtaqueRespuesta ataqueRespuesta = response.body();
                    ArrayList<Ataque> listaAtaques = ataqueRespuesta.getResults();
                    for (Ataque a : listaAtaques) {
                        agregarDescripcionNombre(a,"todos");
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarAtaques), R.string.msg_ataque_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AtaqueRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void agregarDescripcionNombre(Ataque a, String opcion){
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<AtaqueRespuestaIndividual> ataqueRespuestaCall = service.obtenerAtaque(a.getName().toLowerCase().replace(" ","-"));

        ataqueRespuestaCall.enqueue(new Callback<AtaqueRespuestaIndividual>() {
            @Override
            public void onResponse(Call<AtaqueRespuestaIndividual> call, Response<AtaqueRespuestaIndividual> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    AtaqueRespuestaIndividual ataqueRespuestaIndividual = response.body();
                    if(ataqueRespuestaIndividual.getNames().size()>0 && ataqueRespuestaIndividual.getFlavorTextEntries().size()>0) {
                        for (Nombre n:ataqueRespuestaIndividual.getNames())
                            if(n.getLanguage().getName().equals("es")) {
                                a.setNombre(n.getName());
                                break;
                            }
                        if(a.getNombre()==null){
                            a.setNombre("Nombre no disponible en español");
                        }
                        for (DescripcionA d:ataqueRespuestaIndividual.getFlavorTextEntries())
                            if(d.getLenguage().getName().equals("es")) {
                                a.setDescription(d.getFlavor_text());
                                break;
                            }
                        if(a.getDescription()==null){
                            a.setDescription("Descripción no disponible en español");
                        }
                        a.setType(traducirTipo(ataqueRespuestaIndividual.getType().getName()));
                        ArrayList<Ataque> ataques = new ArrayList();
                        ataques.add(a);
                        if (opcion.equals("todos"))
                            listaAtaquesAdapter.addListaAtaques(ataques);
                        else if (opcion.equals("uno"))
                            listaAtaquesAdapter.addAtaque(ataques);
                    }
                } else {
                    Log.e(TAG, "Falla ataqueRepuestaIndividual, onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarAtaques), R.string.msg_ataque_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AtaqueRespuestaIndividual> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void buscarAtaque(String ataque) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        String ataqueNombre=traducirNombre(ataque);
        Call<AtaqueRespuestaIndividual> ataqueRespuestaIndividualCall = service.obtenerAtaque(ataqueNombre.toLowerCase().replace(" ","-"));

        ataqueRespuestaIndividualCall.enqueue(new Callback<AtaqueRespuestaIndividual>() {
            @Override
            public void onResponse(Call<AtaqueRespuestaIndividual> call, Response<AtaqueRespuestaIndividual> response) {
                aptoParaCargar =true;
                if (response.isSuccessful()) {
                    AtaqueRespuestaIndividual ataqueRespuestaIndividual = response.body();
                    if (ataqueRespuestaIndividual != null) {
                        Ataque a = new Ataque(ataqueNombre);
                        agregarDescripcionNombre(a, "uno");
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarAtaques), R.string.msg_ataque_no_encontrado, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AtaqueRespuestaIndividual> call, Throwable t) {
                aptoParaCargar =true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    protected String traducirNombre(String nombreAtaque){
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("AtaquesPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[1].toLowerCase().equals(nombreAtaque.toLowerCase())) {
                        return datos[0]; //nombre en español
                    }
                    else if(datos[0].toLowerCase().equals(nombreAtaque.toLowerCase())) {
                        return datos[1]; //nombre en ingles
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
        return nombreAtaque;
    }

    private String traducirTipo(String tipo) {
        InputStream file = null;
        InputStreamReader reader= null;
        BufferedReader bufferedReader = null;

        try{
            file= getAssets().open("TiposPokemon.csv");
            reader = new InputStreamReader(file);
            bufferedReader= new BufferedReader(reader);

            String line = null;
            while((line= bufferedReader.readLine())!=null) {
                String[] datos = line.split(";");
                if(datos !=null){
                    if(datos[1].toLowerCase().equals(tipo.toLowerCase())) {
                        return datos[2]; //nombre en español
                    }
                    else if(datos[2].toLowerCase().equals(tipo.toLowerCase())) {
                        return datos[1]; //nombre en ingles
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