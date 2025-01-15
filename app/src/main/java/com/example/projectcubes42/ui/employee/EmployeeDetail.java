package com.example.projectcubes42.ui.employee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetail extends AppCompatActivity {

    private TextView nameDetail, firstnameDetail, phoneDetail, emailDetail, departmentDetail, siteDetail;
    private Button btnEdit, btnSave, btnDelete;
    private Spinner spinnerService, spinnerSite;

    private Long employeeId;

    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Appel obligatoire
        setContentView(R.layout.activity_employee_detail); // Charge le layout

        // Récupération de l'ID de l'employé à partir de l'Intent
        employeeId = getIntent().getLongExtra("EMPLOYEE_ID", -1);
        if (employeeId == -1) {
            Toast.makeText(this, "Aucun employé trouvé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("EMPLOYEE_DETAIL", "ID reçu dans Intent : " + employeeId);

        // Initialisation des vues
        nameDetail = findViewById(R.id.nameDetail);
        firstnameDetail = findViewById(R.id.firstnameDetail);
        phoneDetail = findViewById(R.id.phoneDetail);
        emailDetail = findViewById(R.id.emailDetail);

        btnEdit = findViewById(R.id.employee_button_edit);
        btnSave = findViewById(R.id.employee_save_update);
        btnDelete = findViewById(R.id.employee_button_delete);

        spinnerService = findViewById(R.id.employeeDepartmentDetail);
        spinnerSite = findViewById(R.id.employeeSiteDetail);

        // Charger les données de l'employé
        getEmployeeById(employeeId);

        // Écouteurs pour les boutons
        btnSave.setOnClickListener(v -> saveEmployeeDetails());
        btnEdit.setOnClickListener(v -> enableFields(true));
        btnDelete.setOnClickListener(v -> deleteEmployee(employeeId));

        configureServiceSpinner();
        configureSiteSpinner();
    }


    private void enableFields(boolean enable) {
        nameDetail.setEnabled(enable);
        firstnameDetail.setEnabled(enable);
        phoneDetail.setEnabled(enable);
        emailDetail.setEnabled(enable);

        if (enable) {
            nameDetail.requestFocus();
            showKeyboard(nameDetail);
        }

        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void configureServiceSpinner() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Appel API pour récupérer les départements
        Call<List<Department>> call = apiService.getAllDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Department> departments = response.body();

                    // Ajouter les noms des départements pour le Spinner
                    ArrayAdapter<Department> adapter = new ArrayAdapter<>(EmployeeDetail.this,
                            android.R.layout.simple_spinner_item, departments);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(adapter);
                } else {
                    Log.e("API", "Erreur récupération départements : " + response.code());
                    Toast.makeText(EmployeeDetail.this, "Erreur récupération des départements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Log.e("API", "Échec API départements : " + t.getMessage());
                Toast.makeText(EmployeeDetail.this, "Échec connexion API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureSiteSpinner() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Appel API pour récupérer les sites
        Call<List<Site>> call = apiService.getAllSites();
        call.enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Site> sites = response.body();

                    // Ajouter les noms des sites pour le Spinner
                    ArrayAdapter<Site> adapter = new ArrayAdapter<>(EmployeeDetail.this,
                            android.R.layout.simple_spinner_item, sites);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSite.setAdapter(adapter);
                } else {
                    Log.e("API", "Erreur récupération sites : " + response.code());
                    Toast.makeText(EmployeeDetail.this, "Erreur récupération des sites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                Log.e("API", "Échec API sites : " + t.getMessage());
                Toast.makeText(EmployeeDetail.this, "Échec connexion API", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void saveEmployeeDetails() {
        String name = nameDetail.getText().toString().trim();
        String firstname = firstnameDetail.getText().toString().trim();
        String phone = phoneDetail.getText().toString().trim();
        String email = emailDetail.getText().toString().trim();

        // Récupérer les départements et sites sélectionnés
        Department selectedDepartment = (Department) spinnerService.getSelectedItem();
        Site selectedSite = (Site) spinnerSite.getSelectedItem();

        Long idDepartment = selectedDepartment != null ? selectedDepartment.getIdDepartment() : null;
        Long idSite = selectedSite != null ? selectedSite.getIdSite() : null;

        if (name.isEmpty() || firstname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mettre à jour l'employé
        Employee employee = new Employee(employeeId, name, firstname, phone, email, idDepartment, idSite);
        updateEmployee(employee);
    }

    private void updateEmployee(Employee employee) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Employee> call = apiService.updateEmployee(employee.getId(), employee);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EmployeeDetail.this, "Employé mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                    enableFields(false);
                } else {
                    Toast.makeText(EmployeeDetail.this, "Erreur lors de la mise à jour.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(EmployeeDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        finish();
    }

    private void deleteEmployee(Long id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteEmployee(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EmployeeDetail.this, "Employé supprimé avec succès !", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EmployeeDetail.this, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EmployeeDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    private void getEmployeeById(Long employeeId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Employee> call = apiService.getEmployeeById(employeeId);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employee employee = response.body();

                    // Affecter les valeurs des autres champs
                    nameDetail.setText(employee.getName());
                    firstnameDetail.setText(employee.getFirstname());
                    phoneDetail.setText(employee.getPhone());
                    emailDetail.setText(employee.getMail());

                    // Sélectionner la valeur appropriée dans le spinner du département
                    spinnerService.post(() -> {
                        ArrayAdapter<Department> adapter = (ArrayAdapter<Department>) spinnerService.getAdapter();
                        if (adapter != null) {
                            for (int i = 0; i < adapter.getCount(); i++) {
                                Department department = adapter.getItem(i);
                                if (department != null && employee.idDepartment() != null && department.getIdDepartment().equals(employee.idDepartment())) {
                                    spinnerService.setSelection(i);
                                    break;
                                }
                            }
                        } else {
                            Log.e("SPINNER", "L'adaptateur du spinner des départements est nul.");
                        }
                    });

                    // Sélectionner la valeur appropriée dans le spinner du site
                    spinnerSite.post(() -> {
                        ArrayAdapter<Site> adapter = (ArrayAdapter<Site>) spinnerSite.getAdapter();
                        if (adapter != null) {
                            for (int i = 0; i < adapter.getCount(); i++) {
                                Site site = adapter.getItem(i);
                                if (site != null && employee.idSite() != null && site.getIdSite().equals(employee.idSite())) {
                                    spinnerSite.setSelection(i);
                                    break;
                                }
                            }
                        } else {
                            Log.e("SPINNER", "L'adaptateur du spinner des sites est nul.");
                        }
                    });

                } else {
                    Toast.makeText(EmployeeDetail.this, "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(EmployeeDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



}
