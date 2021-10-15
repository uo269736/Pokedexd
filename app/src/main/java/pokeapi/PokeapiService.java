package pokeapi;

import models.PokemonRespuesta;
import models.PokemonRespuestaIndividual;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset")int offset);

    @GET("pokemon/{name}")
    Call<PokemonRespuestaIndividual> obtenerPokemon(@Path("name") String nombre);
}
