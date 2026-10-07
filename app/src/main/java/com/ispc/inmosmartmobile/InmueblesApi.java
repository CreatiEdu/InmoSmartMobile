package com.ispc.inmosmartmobile;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InmueblesApi {
    @GET("propiedades")
    Call<List<Inmueble>> getInmueblesFiltrados(
            @Query("tipoOperacion") String tipoOperacion,
            @Query("ciudad") String ciudad,
            @Query("precioMax") Double precioMax
    );
}
