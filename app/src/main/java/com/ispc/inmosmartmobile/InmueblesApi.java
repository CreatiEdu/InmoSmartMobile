package com.ispc.inmosmartmobile;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Header;
import retrofit2.http.DELETE;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InmueblesApi {
    @GET("api/mobile/propiedades/")
    Call<List<Inmueble>> getInmueblesFiltrados(
            @Query("tipoOperacion") String tipoOperacion,
            @Query("ciudad") String ciudad,
            @Query("precioMax") Double precioMax
    );

    @GET("api/mobile/propiedades/{id}/")
    Call<PropiedadDetalle> getDetalle(@Header("Authorization") String token, @Path("id") int id);

    @GET("api/mobile/favoritos/")
    Call<List<FavoritoResponse>> obtenerFavoritos(
            @Header("Authorization") String token
    );

    @POST("api/mobile/favoritos/")
    Call<FavoritoResponse> agregarFavorito(
            @Header("Authorization") String token,
            @Body AgregarFavoritoRequest request
    );

    @DELETE("api/mobile/favoritos/{id_propiedad}/")
    Call<Void> eliminarFavorito(
            @Header("Authorization") String token,
            @Path("id_propiedad") int idPropiedad
    );
}