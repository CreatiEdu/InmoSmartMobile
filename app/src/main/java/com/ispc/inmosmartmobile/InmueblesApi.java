package com.ispc.inmosmartmobile;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InmueblesApi {
    @GET("api/mobile/propiedades/")
    Call<List<Inmueble>> getInmueblesFiltrados(
            @Query("tipoOperacion") String tipoOperacion,
            @Query("ciudad") String ciudad,
            @Query("precioMax") Double precioMax
    );

    @GET("api/mobile/propiedades/{id}/")
    Call<PropiedadDetalle> getDetalle(@Path("id") int id);
}