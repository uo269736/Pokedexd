package com.example.pokedexd.pokeapi;

import com.example.pokedexd.models.AtaqueRespuesta;
import com.example.pokedexd.models.AtaqueRespuestaIndividual;
import com.example.pokedexd.models.HabilidadRespuesta;
import com.example.pokedexd.models.HabilidadRespuestaIndividual;
import com.example.pokedexd.models.ObjetoRespuesta;
import com.example.pokedexd.models.ObjetoRespuestaIndividual;
import com.example.pokedexd.models.PokemonRespuesta;
import com.example.pokedexd.models.PokemonRespuestaIndividual;
import com.example.pokedexd.models.TipoRespuesta;
import com.example.pokedexd.models.TipoRespuestaIndividual;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset")int offset);

    @GET("pokemon/{name}")
    Call<PokemonRespuestaIndividual> obtenerPokemon(@Path("name") String nombre);

    @GET("ability")
    Call<HabilidadRespuesta> obtenerListaHabilidades(@Query("limit") int limit, @Query("offset")int offset);

    @GET("ability/{name}")
    Call<HabilidadRespuestaIndividual> obtenerHabilidad(@Path("name") String nombre);

    @GET("item")
    Call<ObjetoRespuesta> obtenerListaObjetos(@Query("limit") int limit, @Query("offset")int offset);

    @GET("item/{name}")
    Call<ObjetoRespuestaIndividual> obtenerObjeto(@Path("name") String nombre);

    @GET("type")
    Call<TipoRespuesta> obtenerListaTipos(@Query("limit") int limit);

    @GET("type/{name}")
    Call<TipoRespuestaIndividual> obtenerDebilidades(@Path("name") String nombre);

    @GET("move")
    Call<AtaqueRespuesta> obtenerListaAtaque(@Query("limit") int limit, @Query("offset") int offset);

    @GET("move/{name}")
    Call<AtaqueRespuestaIndividual> obtenerAtaque(@Path("name") String nombre);
}
