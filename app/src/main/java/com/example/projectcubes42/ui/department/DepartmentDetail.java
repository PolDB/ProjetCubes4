package com.example.projectcubes42.ui.department;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.ui.employee.EmployeeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentDetail extends AppCompatActivity {

    private EditText departmentDetail;
    private Button btnEdit, btnSave, btnDelete;
    private Long departmentId;

    // ViewModels
    private DepartmentDetailViewModel viewModel;
    private EmployeeViewModel employeeViewModel;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        // 1. Récupération de l'ID depuis l'Intent
        departmentId = getIntent().getLongExtra("DEPARTMENT_ID", -1);
        Log.d("DEPARTMENT_ID", "ID reçu dans Intent : " + departmentId);

        // 2. Initialisation des vues
        departmentDetail = findViewById(R.id.departmentDetail);
        btnEdit = findViewById(R.id.service_button_edit);
        btnSave = findViewById(R.id.service_save_update);
        btnDelete = findViewById(R.id.service_button_delete);

        // 3. Instancier les ViewModels
        viewModel = new ViewModelProvider(this).get(DepartmentDetailViewModel.class);
        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        // 4. Observer le ViewModel
        observeViewModel();

        // 5. Charger les données si l'ID est valide
        if (departmentId != -1) {
            viewModel.fetchDepartment(departmentId);
        }

        // 6. Gérer les clics
        btnSave.setOnClickListener(v -> onSaveClicked());
        btnEdit.setOnClickListener(v -> onEditClicked());
        btnDelete.setOnClickListener(v -> onDeleteClicked());
    }

    private void observeViewModel() {
        // Observer le département chargé
        viewModel.getDepartment().observe(this, department -> {
            if (department != null) {
                departmentDetail.setText(department.getDepartment_name());
            }
        });

        // Observer les messages Toast
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(DepartmentDetail.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Observer l'événement de fermeture
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                finish();
            }
        });
    }

    // -------------------------
    // Gestion des actions UI
    // -------------------------
    private void onSaveClicked() {
        String departmentName = departmentDetail.getText().toString().trim();
        if (!departmentName.isEmpty()) {
            // Construire l'objet Department
            Department department = new Department(departmentId, departmentName);
            viewModel.updateDepartment(departmentId, department);

            enableFields(false);
            btnSave.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("RestrictedApi")
    private void onEditClicked() {
        enableFields(true);
        departmentDetail.requestFocus();
        showKeyboard(departmentDetail); // Si cette méthode est dispo
        btnSave.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
    }

    private void onDeleteClicked() {
        // 1. Appel à la méthode du EmployeeViewModel pour récupérer la liste des employés
        employeeViewModel.fetchEmployeesByDepartment(departmentId).enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int nbDeSalaries = response.body().size();

                    // 2. Ouvrir la boîte de dialogue avec le bon nombre de salariés
                    viewModel.confirmDeleteDepartment(DepartmentDetail.this, departmentId, nbDeSalaries);

                } else {
                    Toast.makeText(DepartmentDetail.this, "Impossible de récupérer la liste des salariés.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(DepartmentDetail.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -------------------------
    //  Méthodes utilitaires
    // -------------------------
    private void enableFields(boolean enable) {
        departmentDetail.setEnabled(enable);
    }
}
