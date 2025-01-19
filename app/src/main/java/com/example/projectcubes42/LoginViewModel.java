package com.example.projectcubes42.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.AuthResponse;
import com.example.projectcubes42.data.model.LoginRequest;
import com.example.projectcubes42.data.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final AuthRepository repository;

    // LiveData pour publier le résultat (AuthResponse) en cas de succès
    private final MutableLiveData<AuthResponse> authResponseLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages d’erreur / info (toast)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<AuthResponse> getAuthResponseLiveData() {
        return authResponseLiveData;
    }

    public LiveData<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }

    // Méthode pour déclencher la connexion
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            toastMessageLiveData.setValue("Veuillez remplir tous les champs");
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        repository.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    toastMessageLiveData.setValue("Connexion échouée, vous n'êtes pas admin");
                } else {
                    // On publie la réponse pour que l’Activity puisse la gérer
                    authResponseLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur serveur : " + t.getMessage());
            }
        });
    }
}
