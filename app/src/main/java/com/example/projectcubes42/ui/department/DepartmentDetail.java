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

public class DepartmentDetail extends AppCompatActivity {

    private EditText departmentDetail;
    private Button btnEdit, btnSave, btnDelete;
    private Long departmentId;

    // ViewModel
    private DepartmentDetailViewModel viewModel;

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

        // 3. Instancier le ViewModel
        viewModel = new ViewModelProvider(this).get(DepartmentDetailViewModel.class);

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
        viewModel.deleteDepartment(departmentId);
    }

    // -------------------------
    //  Méthodes utilitaires
    // -------------------------
    private void enableFields(boolean enable) {
        departmentDetail.setEnabled(enable);
    }
}
