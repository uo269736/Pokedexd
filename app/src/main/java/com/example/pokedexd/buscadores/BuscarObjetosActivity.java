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

import com.google.android.material.snackbar.Snackbar;

import com.example.pokedexd.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.pokedexd.adapters.ListaObjetosAdapter;
import com.example.pokedexd.models.DescripcionO;
import com.example.pokedexd.models.Nombre;
import com.example.pokedexd.models.Objeto;
import com.example.pokedexd.models.ObjetoRespuesta;
import com.example.pokedexd.models.ObjetoRespuestaIndividual;
import com.example.pokedexd.pokeapi.PokeapiService;
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

        //Boton para ir hacia atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("");
        Drawable d=getResources().getDrawable(R.drawable.action_bar);
        getSupportActionBar().setBackgroundDrawable(d);

        botonBuscar = (ImageButton) findViewById(R.id.ObjetoIbtBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText) findViewById(R.id.ObjetoEtxBuscador);
                String nombre = e.getText().toString();

                if ((nombre.isEmpty() || nombre.trim().equals(""))) {
                    aptoParaCargar = true;
                    offset = 0;
                    listaObjetosAdapter.eliminarObjetos();
                    obtenerDatos(offset);
                    Snackbar.make(findViewById(R.id.BuscarObjetos), R.string.msg_objeto_no_encontrado, Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(findViewById(R.id.BuscarObjetos), R.string.msg_objeto_no_encontrado, Snackbar.LENGTH_SHORT).show();
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
                        for (Nombre n:objetoRespuestaIndividual.getNames())
                            if(n.getLanguage().getName().equals("es")) {
                                o.setNombre(n.getName());
                                break;
                            }
                        if(o.getNombre()==null){
                            o.setNombre("Nombre no disponible en español");
                        }
                        for (DescripcionO d:objetoRespuestaIndividual.getDescripciones())
                            if(d.getLenguage().getName().equals("es")) {
                                o.setDescripcion(d.getText());
                                break;
                            }
                        if(o.getDescripcion()==null){
                            o.setDescripcion("Descripción no disponible en español");
                        }
                        ArrayList<Objeto> listaO = new ArrayList();
                        listaO.add(o);
                        if (opcion.equals("todos"))
                            listaObjetosAdapter.addListaObjetos(listaO);
                        else if (opcion.equals("uno"))
                            listaObjetosAdapter.addObjeto(listaO);
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarObjetos), R.string.msg_objeto_no_encontrado, Snackbar.LENGTH_SHORT).show();
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
                    ObjetoRespuestaIndividual objetoRespuestaIndividual = response.body();
                    if(objetoRespuestaIndividual!=null) {
                        System.out.println("https://pokeapi.co/api/v2/item/"+objetoRespuestaIndividual.getId());
                        Objeto o=new Objeto(objetoNombre, "https://pokeapi.co/api/v2/item/"+objetoRespuestaIndividual.getId());
                        agregarDescripcionNombre(o,"uno");
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                    Snackbar.make(findViewById(R.id.BuscarObjetos), R.string.msg_objeto_no_encontrado, Snackbar.LENGTH_SHORT).show();
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
        return nombreObjeto;
    }
}