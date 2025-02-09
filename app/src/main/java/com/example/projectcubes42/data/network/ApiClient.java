package com.example.projectcubes42.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//retrofit est un bibliothèque qui gère de façon autonome les appels http entrants et sortants.
public class ApiClient {
    public static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.16:8080/"; // Remplacez par votre URL de base

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
