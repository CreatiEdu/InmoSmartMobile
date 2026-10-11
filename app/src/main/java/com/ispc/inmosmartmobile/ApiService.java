package com.ispc.inmosmartmobile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
}