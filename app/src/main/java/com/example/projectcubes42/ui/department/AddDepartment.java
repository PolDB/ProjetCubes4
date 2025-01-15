package com.example.projectcubes42.ui.department;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDepartment extends AppCompatActivity {

    private EditText editTextService;
    private FloatingActionButton buttonSendFormService;
    Long id_department;
    String department_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_service);

        // Initialiser les vues avec findViewById
        editTextService = findViewById(R.id.editTextDepartment);
        buttonSendFormService = findViewById(R.id.buttonSendFormDepartement);




        // Ajouter un listener au bouton "Enregistrer"
        buttonSendFormService.setOnClickListener(v -> sendServiceData());

    }


    public void sendServiceData() {
        department_name = editTextService.getText().toString().trim();

        if (department_name.isEmpty()) {
            Log.e("API", "Le nom du département est vide.");
            return;
        }
        // Créez une instance de l'objet Employee
        Department department = new Department(id_department, department_name);

        // Obtenez l'instance de Retrofit et de l'interface ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Appelez la méthode addEmployee
        Call<Department> call = apiService.addDepartment(department);

        call.enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Service ajouté avec succès !");
                } else {
                    Log.e("API", "Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Log.e("API", "Échec : " + t.getMessage());
            }
        });




        clearFields();
        finish();
    }




    private void clearFields() {
        // Effacer le texte des champs
        editTextService.setText("");
    }
}
