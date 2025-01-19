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
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.data.model.AuthResponse;
import com.example.projectcubes42.ui.login.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    // ViewModel
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);

        // 1. Instancier le ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 2. Observer le ViewModel
        observeViewModel();

        // 3. Définir l'action du bouton
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            loginViewModel.login(username, password);
        });
    }

    private void observeViewModel() {
        // Observer la réponse Auth en cas de succès
        loginViewModel.getAuthResponseLiveData().observe(this, authResponse -> {
            if (authResponse != null) {
                // Stocker le rôle localement
                saveUserRole(authResponse.getRole());
                // Redirection
                goToDrawerActivity();
            }
        });

        // Observer les messages (erreur, info)
        loginViewModel.getToastMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                // S'il y a eu un échec, on peut éventuellement terminer l'Activity
                if (message.startsWith("Connexion échouée")) {
                    finish();
                }
            }
        });
    }

    private void saveUserRole(String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_role", role);
        editor.apply();
    }

    private void goToDrawerActivity() {
        Intent intent = new Intent(LoginActivity.this, Drawer_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("openEmployeeFragment", true); // Pour ouvrir FragmentEmployee
        startActivity(intent);
        finish();
    }
}
