package com.example.projectcubes42;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.data.model.AuthResponse;
import com.example.projectcubes42.data.model.LoginRequest;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private ApiService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);

        authService = ApiClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);

        authService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("LOGIN_ERROR", "Code HTTP: " + response.code());
                    Log.e("LOGIN_ERROR", "Réponse du serveur: " + response.errorBody());
                    onLoginFailed();
                    return;
                }

                AuthResponse authResponse = response.body();
                Log.d("LOGIN_SUCCESS", "Utilisateur: " + authResponse.getUsername() + " - Role: " + authResponse.getRole());

                // Stocker le rôle en local
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_role", authResponse.getRole());
                editor.apply();

                // Redirection vers Drawer_activity
                Intent intent = new Intent(LoginActivity.this, Drawer_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("openEmployeeFragment", true); // Pour ouvrir FragmentEmployee
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erreur serveur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                onLoginFailed();
            }
        });
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Connexion échouée, vous n'êtes pas admin", Toast.LENGTH_SHORT).show();
        finish();

    }
    private void onLoginSuccess(String role) {
        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();

        // Sauvegarde du rôle
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_role", role);
        editor.apply();

        // Redirection vers Drawer_activity
        Intent intent = new Intent(LoginActivity.this, Drawer_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}