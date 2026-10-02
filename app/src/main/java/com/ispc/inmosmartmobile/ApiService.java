package com.ispc.inmosmartmobile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("register/")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}