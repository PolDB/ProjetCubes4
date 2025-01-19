package com.example.projectcubes42.ui.site;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Site;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddSite extends AppCompatActivity {

    private EditText editTextSite;
    private FloatingActionButton buttonSendFormSite;

    // ViewModel
    private AddSiteViewModel viewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_site);

        // 1. Instancier le ViewModel
        viewModel = new ViewModelProvider(this).get(AddSiteViewModel.class);

        // 2. Initialiser les vues
        editTextSite = findViewById(R.id.editTextSite);
        buttonSendFormSite = findViewById(R.id.buttonSendFormSite);

        // 3. Observer le ViewModel
        observeViewModel();

        // 4. Click Listener pour le bouton "Enregistrer"
        buttonSendFormSite.setOnClickListener(v -> sendSiteData());
    }

    private void observeViewModel() {
        // Observer les messages (Toast)
        viewModel.getToastMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Log.d("AddSite", message);
                // Ou en Toast :
                // Toast.makeText(AddSite.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Observer l'événement de fermeture
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                clearFields();
                finish();
            }
        });
    }

    private void sendSiteData() {
        String city = editTextSite.getText().toString().trim();

        if (city.isEmpty()) {
            Log.e("AddSite", "Le nom du site est vide.");
            return;
        }

        // Construire l'objet Site
        Site site = new Site(null, city);

        // Appel au ViewModel
        viewModel.addSite(site);

        // Optionnel : si vous voulez déjà vider le champ :
        // clearFields();
    }

    private void clearFields() {
        editTextSite.setText("");
    }
}
