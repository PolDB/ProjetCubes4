package com.example.projectcubes42.ui.site;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSite extends AppCompatActivity {

    private EditText editTextSite;
    private FloatingActionButton buttonSendFormSite;

    Long id;
    String city;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_site);

        // Initialiser les vues avec findViewById
        editTextSite = findViewById(R.id.editTextSite);
        buttonSendFormSite = findViewById(R.id.buttonSendFormSite);

        // Configurer le Spinner


        // Ajouter un listener au bouton "Enregistrer"
        buttonSendFormSite.setOnClickListener(v -> sendSiteData());

    }


    public void sendSiteData() {
        city = editTextSite.getText().toString().trim();
        // Créez une instance de l'objet Employee
        Site site = new Site(id, city);

        // Obtenez l'instance de Retrofit et de l'interface ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Appelez la méthode addEmployee
        Call<Site> call = apiService.addSite(site);

        call.enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Employé ajouté avec succès !");
                } else {
                    Log.e("API", "Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                Log.e("API", "Échec : " + t.getMessage());
            }
        });




        clearFields();
        finish();
    }




    private void clearFields() {
        // Effacer le texte des champs
        editTextSite.setText("");
    }
}
