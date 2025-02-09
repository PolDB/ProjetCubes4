package com.example.projectcubes42.ui.site;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.ui.department.DepartmentDetail;
import com.example.projectcubes42.ui.employee.EmployeeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//il s'agit de l'activité qui affiche les site dans l'interface admin
public class SiteDetail extends AppCompatActivity {

    private TextView siteDetail;
    private Button btnEdit, btnSave, btnDelete;
    private Long siteId;

    private SiteDetailViewModel viewModel;
    private EmployeeViewModel employeeViewModel;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);

        // 1. Récupération de l'ID du site depuis l'intent
        siteId = getIntent().getLongExtra("SITE_ID", -1);
        Log.d("SITE_ID", "ID reçu dans Intent : " + siteId);

        // 2. Initialiser les vues
        siteDetail = findViewById(R.id.siteDetail);
        btnEdit = findViewById(R.id.site_button_edit);
        btnSave = findViewById(R.id.site_save_update);
        btnDelete = findViewById(R.id.site_button_delete);

        // 3. Instancier le ViewModel
        viewModel = new ViewModelProvider(this).get(SiteDetailViewModel.class);
        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        // 4. Observer le ViewModel
        observeViewModel();

        // 5. Charger le site si l'ID est valide
        if (siteId != -1) {
            viewModel.fetchSite(siteId);
        }

        // 6. Gérer les clics sur les boutons
        btnSave.setOnClickListener(v -> onSaveClicked());
        btnEdit.setOnClickListener(v -> onEditClicked());
        btnDelete.setOnClickListener(v -> onDeleteClicked());
    }

    private void observeViewModel() {
        // Observer le Site LiveData
        viewModel.getSite().observe(this, site -> {
            if (site != null) {
                siteDetail.setText(site.getCity());
            }
        });

        // Observer les messages Toast
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(SiteDetail.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Observer l'événement de fermeture de l'écran
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                finish();
            }
        });
    }

    // --------------------
    //  Boutons
    // --------------------
    private void onSaveClicked() {
        String siteName = siteDetail.getText().toString().trim();
        if (!siteName.isEmpty()) {
            // Construire un nouvel objet Site
            Site site = new Site(siteId, siteName);
            // Appel ViewModel pour update
            viewModel.updateSite(siteId, site);

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
        siteDetail.requestFocus();
        showKeyboard(siteDetail);
        btnSave.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
    }

    private void onDeleteClicked() {
        // 1. Appel à la méthode du EmployeeViewModel pour récupérer la liste des employés
        employeeViewModel.fetchEmployeesBySite(siteId).enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int nbDeSalaries = response.body().size();

                    // 2. Ouvrir la boîte de dialogue avec le bon nombre de salariés
                    viewModel.confirmDeleteSite(SiteDetail.this, siteId, nbDeSalaries);

                } else {
                    Toast.makeText(SiteDetail.this, "Impossible de récupérer la liste des salariés.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(SiteDetail.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableFields(boolean enable) {
        siteDetail.setEnabled(enable);
    }
}
