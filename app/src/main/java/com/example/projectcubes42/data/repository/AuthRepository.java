package com.example.projectcubes42.data.repository;

import com.example.projectcubes42.data.model.AuthResponse;
import com.example.projectcubes42.data.model.LoginRequest;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import retrofit2.Call;

public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository() {
        // Instancie le service via Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    // Méthode pour se connecter
    public Call<AuthResponse> login(LoginRequest request) {
        return apiService.login(request);
    }
}
