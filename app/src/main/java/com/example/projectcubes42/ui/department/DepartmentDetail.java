package com.example.projectcubes42.ui.department;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentDetail extends AppCompatActivity {

    private EditText departmentDetail;
    private Button btnEdit, btnSave, btnDelete;
    private Long id_department;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);


       Long departmentId = getIntent().getLongExtra("DEPARTMENT_ID", -1);
        Log.d("DEPARTMENT_ID", "ID reçu dans Intent : " + departmentId); // Log pour vérifier l'ID

        departmentDetail = findViewById(R.id.departmentDetail);         // Vérifiez que R.id.nameDetail existe
        btnEdit = findViewById(R.id.service_button_edit);
        btnSave = findViewById(R.id.service_save_update);
        btnDelete = findViewById(R.id.service_button_delete);

        if (departmentId != -1) {
            getDepartmentById(departmentId);
        }

        btnSave.setOnClickListener(v -> {
            String departmentName = departmentDetail.getText().toString().trim();


            if (!departmentName.isEmpty() ) {
                Department department = new Department(id_department, departmentName);
                updateDepartment(departmentId, department);
                enableFields(false);
                btnSave.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            }
        });
        btnEdit.setOnClickListener(v -> {
            enableFields(true);
            departmentDetail.requestFocus();
            showKeyboard(departmentDetail);
            btnSave.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        });

        btnDelete.setOnClickListener(v -> deleteDepartment(departmentId));
    }


    private void enableFields(boolean enable) {
        departmentDetail.setEnabled(enable);
    }

    private void updateDepartment(Long departmentId, Department department) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Department> call = apiService.updateDepartment(departmentId, department);

        call.enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                Log.e("UPDATE_RESPONSE", "Code: " + response.code() + ", Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DepartmentDetail.this, "Employé mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DepartmentDetail.this, "Échec de la mise à jour.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Toast.makeText(DepartmentDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    private void deleteDepartment(Long id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteDepartement(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DepartmentDetail.this, "Employé supprimé avec succès !", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DepartmentDetail.this, "Échec de la suppression.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DepartmentDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDepartmentById(Long departmentId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Department> call = apiService.getDepartmentById(departmentId);

        call.enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Department department = response.body();
                    // Remplir les champs avec les données de l'employé
                    departmentDetail.setText(department.getDepartment_name());

                    id_department = department.getIdDepartment();  // Conservez l'ID pour l'édition et la suppression
                } else {
                    Toast.makeText(DepartmentDetail.this, "Échec de la récupération des données.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Toast.makeText(DepartmentDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}