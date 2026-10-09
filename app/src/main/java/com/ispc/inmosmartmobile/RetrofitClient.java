package com.ispc.inmosmartmobile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://inmosmart.pythonanywhere.com/";
    private static Retrofit retrofit = null;

    public static InmueblesApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(InmueblesApi.class);
    }
}