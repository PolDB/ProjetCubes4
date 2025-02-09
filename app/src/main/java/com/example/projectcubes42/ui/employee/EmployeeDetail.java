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
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;

import java.util.List;
//il s'agit de l'activité qui affiche les détail d'un utilisateur dans l'interface admin
public class EmployeeDetail extends AppCompatActivity {

    private TextView nameDetail, firstnameDetail, phoneDetail, emailDetail;
    private Spinner spinnerService, spinnerSite;
    private Button btnEdit, btnSave, btnDelete;

    private Long employeeId;

    private EmployeeDetailViewModel viewModel;

    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        // 1. Récupération de l'ID de l'employé
        employeeId = getIntent().getLongExtra("EMPLOYEE_ID", -1);
        if (employeeId == -1) {
            Toast.makeText(this, "Aucun employé trouvé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Initialisation des vues
        nameDetail = findViewById(R.id.nameDetail);
        firstnameDetail = findViewById(R.id.firstnameDetail);
        phoneDetail = findViewById(R.id.phoneDetail);
        emailDetail = findViewById(R.id.emailDetail);

        spinnerService = findViewById(R.id.employeeDepartmentDetail);
        spinnerSite = findViewById(R.id.employeeSiteDetail);

        btnEdit = findViewById(R.id.employee_button_edit);
        btnSave = findViewById(R.id.employee_save_update);
        btnDelete = findViewById(R.id.employee_button_delete);

        // 3. Instanciation du ViewModel
        viewModel = new ViewModelProvider(this).get(EmployeeDetailViewModel.class);

        // 4. Observer les LiveData
        observeViewModel();

        // 5. Charger les données nécessaires
        viewModel.fetchEmployee(employeeId); // Récupère l'employé cible
        viewModel.loadDepartments();         // Charge la liste des départements
        viewModel.loadSites();// Charge la liste des sites
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                finish();  // L’Activity se termine ici
            }
        });

        // 6. Configuration des boutons
        btnSave.setOnClickListener(v -> saveEmployeeDetails());
        btnEdit.setOnClickListener(v -> enableFields(true));
        btnDelete.setOnClickListener(v -> {
            viewModel.deleteEmployee(employeeId);
            finish(); // On termine l'Activity après la suppression
        });

        // Par défaut, les champs sont en lecture seule
        enableFields(false);
    }

    private void observeViewModel() {
        // Observer l’employé récupéré
        viewModel.getEmployee().observe(this, employee -> {
            if (employee != null) {
                // Mise à jour des champs UI
                nameDetail.setText(employee.getName());
                firstnameDetail.setText(employee.getFirstname());
                phoneDetail.setText(employee.getPhone());
                emailDetail.setText(employee.getMail());

                // Sélection du département et du site dans les spinners
                selectDepartmentInSpinner(employee.idDepartment());
                selectSiteInSpinner(employee.idSite());
            }
        });

        // Observer la liste des départements
        viewModel.getDepartments().observe(this, departments -> {
            if (departments != null) {
                setupDepartmentSpinner(departments);
            }
        });

        // Observer la liste des sites
        viewModel.getSites().observe(this, sites -> {
            if (sites != null) {
                setupSiteSpinner(sites);
            }
        });

        // Observer les messages Toast
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(EmployeeDetail.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -------------------------------------------------------------
    //  Configurer les spinners (départements & sites)
    // -------------------------------------------------------------
    private void setupDepartmentSpinner(List<Department> departments) {
        ArrayAdapter<Department> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                departments
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);

        // Tente de sélectionner le bon département (si l'employé est déjà chargé)
        if (viewModel.getEmployee().getValue() != null) {
            selectDepartmentInSpinner(viewModel.getEmployee().getValue().idDepartment());
        }
    }

    private void setupSiteSpinner(List<Site> sites) {
        ArrayAdapter<Site> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sites
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSite.setAdapter(adapter);

        // Tente de sélectionner le bon site (si l'employé est déjà chargé)
        if (viewModel.getEmployee().getValue() != null) {
            selectSiteInSpinner(viewModel.getEmployee().getValue().idSite());
        }
    }

    private void selectDepartmentInSpinner(Long departmentId) {
        if (departmentId == null) return;
        if (spinnerService.getAdapter() instanceof ArrayAdapter) {
            ArrayAdapter<Department> adapter =
                    (ArrayAdapter<Department>) spinnerService.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                Department d = adapter.getItem(i);
                if (d != null && d.getIdDepartment().equals(departmentId)) {
                    spinnerService.setSelection(i);
                    break;
                }
            }
        }
    }

    private void selectSiteInSpinner(Long siteId) {
        if (siteId == null) return;
        if (spinnerSite.getAdapter() instanceof ArrayAdapter) {
            ArrayAdapter<Site> adapter =
                    (ArrayAdapter<Site>) spinnerSite.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                Site s = adapter.getItem(i);
                if (s != null && s.getIdSite().equals(siteId)) {
                    spinnerSite.setSelection(i);
                    break;
                }
            }
        }
    }

    // -------------------------------------------------------------
    //  Gestion des champs & boutons
    // -------------------------------------------------------------
    private void enableFields(boolean enable) {
        nameDetail.setEnabled(enable);
        firstnameDetail.setEnabled(enable);
        phoneDetail.setEnabled(enable);
        emailDetail.setEnabled(enable);
        spinnerService.setEnabled(enable);
        spinnerSite.setEnabled(enable);

        if (enable) {
            nameDetail.requestFocus();
            showKeyboard(nameDetail);
        }

        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // -------------------------------------------------------------
    //  Sauvegarde (mise à jour)
    // -------------------------------------------------------------
    private void saveEmployeeDetails() {
        // Récupération des valeurs
        String name = nameDetail.getText().toString().trim();
        String firstname = firstnameDetail.getText().toString().trim();
        String phone = phoneDetail.getText().toString().trim();
        String email = emailDetail.getText().toString().trim();

        Department selectedDepartment = (Department) spinnerService.getSelectedItem();
        Site selectedSite = (Site) spinnerSite.getSelectedItem();

        Long idDepartment = selectedDepartment != null ? selectedDepartment.getIdDepartment() : null;
        Long idSite = selectedSite != null ? selectedSite.getIdSite() : null;

        if (name.isEmpty() || firstname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construit l'employé à jour
        Employee employee = new Employee(employeeId, name, firstname, phone, email, idDepartment, idSite);
        // Appel au ViewModel pour la mise à jour
        viewModel.updateEmployee(employee);

        enableFields(false);
    }
}
