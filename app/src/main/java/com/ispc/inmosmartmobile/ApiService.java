package com.ispc.inmosmartmobile;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {

    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register/")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("perfil/")
    Call<PerfilResponse> getPerfil(@Header("Authorization") String token);

    @PUT("perfil/")
    Call<PerfilResponse> actualizarPerfil(@Header("Authorization") String token,
                                          @Body PerfilRequest perfilRequest);

    @POST("contacto/")
    Call<ResponseBody> enviarContacto(@Body ContactoRequest body);

    @GET("api/mobile/favoritos/")
    Call<List<FavoritoResponse>> obtenerFavoritos(
            @Header("Authorization") String token
    );

    @POST("baja/")
    Call<Void> darDeBaja(@Header("Authorization") String token);

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

    @GET("mobile/turnos/")
    Call<List<TurnoModel>> obtenerTurnos(@Header("Authorization") String token);

    @POST("mobile/turnos/")
    Call<TurnoModel> agendarTurno(@Header("Authorization") String token, @Body AgendarTurnoRequest request);

    @DELETE("mobile/turnos/{id}/")
    Call<Void> cancelarTurno(@Header("Authorization") String token, @Path("id") int idTurno);
}

