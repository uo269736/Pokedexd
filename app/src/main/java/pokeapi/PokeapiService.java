package pokeapi;

import models.HabilidadRespuesta;
import models.HabilidadRespuestaIndividual;
import models.ObjetoRespuesta;
import models.ObjetoRespuestaIndividual;
import models.PokemonRespuesta;
import models.PokemonRespuestaIndividual;
import models.TipoRespuesta;
import models.TipoRespuestaIndividual;
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
}
