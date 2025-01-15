package com.example.projectcubes42.ui.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
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

public class AddEmployee extends AppCompatActivity {

    private EditText editTextName, editTextFirstname, editTextPhone, editTextEmailAddress;
    private Spinner spinnerService, spinnerSite;
    private FloatingActionButton buttonSendForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_employee);

        // Initialiser les vues
        editTextName = findViewById(R.id.editTextName);
        editTextFirstname = findViewById(R.id.editTextFirstname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerSite = findViewById(R.id.spinnerSite);
        buttonSendForm = findViewById(R.id.buttonSendForm);

        // Configurer les spinners
        configureServiceSpinner();
        configureSiteSpinner();

        // Ajouter un listener au bouton d'envoi
        buttonSendForm.setOnClickListener(v -> sendEmployeeData());
    }

    private void configureServiceSpinner() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Department>> call = apiService.getAllDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Department> departments = new ArrayList<>();
                    departments.add(new Department(-1L, "Sélectionnez un département")); // Option par défaut
                    departments.addAll(response.body());

                    Log.d("DEBUG_DEPT", "Taille de la liste : " + departments.size());
                    for (Department dept : departments) {
                        Log.d("DEBUG_DEPT", "Dept ID: " + dept.getIdDepartment() +
                                ", Name: " + dept.getDepartment_name());}

                        ArrayAdapter<Department> adapter = new ArrayAdapter<>(AddEmployee.this,
                                android.R.layout.simple_spinner_item, departments);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerService.setAdapter(adapter);
                    } else{
                        Log.e("API", "Erreur récupération départements : " + response.code());
                        Toast.makeText(AddEmployee.this, "Erreur récupération des départements", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Log.e("API", "Échec API départements : " + t.getMessage());
                Toast.makeText(AddEmployee.this, "Échec connexion API", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void configureSiteSpinner() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Site>> call = apiService.getAllSites();
        call.enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Site> sites = new ArrayList<>();
                    sites.add(new Site(-1L, "Sélectionnez un site")); // Option par défaut
                    sites.addAll(response.body());

                    Log.d("DEBUG_DEPT", "Taille de la liste : " + sites.size());
                    for (Site si : sites) {
                        Log.d("DEBUG_DEPT", "Dept ID: " + si.getIdSite() +
                                ", Name: " + si.getCity());}


                    ArrayAdapter<Site> adapter = new ArrayAdapter<>(AddEmployee.this,
                            android.R.layout.simple_spinner_item, sites);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSite.setAdapter(adapter);
                } else {
                    Log.e("API", "Erreur récupération sites : " + response.code());
                    Toast.makeText(AddEmployee.this, "Erreur récupération des sites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                Log.e("API", "Échec API sites : " + t.getMessage());
                Toast.makeText(AddEmployee.this, "Échec connexion API", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendEmployeeData() {
        // Récupérer les valeurs des champs
        String name = editTextName.getText().toString().trim();
        String firstname = editTextFirstname.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String mail = editTextEmailAddress.getText().toString().trim();

        // Récupérer les objets sélectionnés dans les spinners
        Department selectedDepartment = (Department) spinnerService.getSelectedItem();
        Site selectedSite = (Site) spinnerSite.getSelectedItem();

        // Validation des champs obligatoires
        if (selectedDepartment == null || selectedDepartment.getIdDepartment() == null || selectedDepartment.getIdDepartment() == -1L) {
            Toast.makeText(this, "Veuillez sélectionner un département valide.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSite == null || selectedSite.getIdSite() == null || selectedSite.getIdSite() == -1L) {
            Toast.makeText(this, "Veuillez sélectionner un site valide.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || firstname.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer les IDs pour l'API
        Long idDepartment = selectedDepartment.getIdDepartment();
        Long idSite = selectedSite.getIdSite();

        // Créer un objet Employee
        Employee employee = new Employee(name, firstname, phone, mail, idDepartment, idSite);

        // Appel de l'API pour ajouter l'employé
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Employee> call = apiService.saveEmployee(employee);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployee.this, "Employé ajouté avec succès", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AddEmployee.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(AddEmployee.this, "Échec de l'ajout de l'employé", Toast.LENGTH_SHORT).show();
            }
        });

        clearFields();
        finish();
    }



    private void clearFields() {
        // Effacer les champs après envoi
        editTextName.setText("");
        editTextFirstname.setText("");
        editTextPhone.setText("");
        editTextEmailAddress.setText("");
        spinnerService.setSelection(0);
        spinnerSite.setSelection(0);
    }
}
