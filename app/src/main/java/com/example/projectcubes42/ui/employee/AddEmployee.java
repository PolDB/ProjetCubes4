package com.example.projectcubes42.ui.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddEmployee extends AppCompatActivity {

    private EditText editTextName, editTextFirstname, editTextPhone, editTextEmailAddress;
    private Spinner spinnerService, spinnerSite;
    private FloatingActionButton buttonSendForm;

    private AddEmployeeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_employee);

        // 1. Instancier (ou récupérer) le ViewModel
        viewModel = new ViewModelProvider(this).get(AddEmployeeViewModel.class);

        // 2. Initialiser les vues
        editTextName         = findViewById(R.id.editTextName);
        editTextFirstname    = findViewById(R.id.editTextFirstname);
        editTextPhone        = findViewById(R.id.editTextPhone);
        editTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        spinnerService       = findViewById(R.id.spinnerService);
        spinnerSite          = findViewById(R.id.spinnerSite);
        buttonSendForm       = findViewById(R.id.buttonSendForm);

        // 3. Observer les LiveData du ViewModel
        observeViewModel();

        // 4. Charger les départements et sites
        viewModel.loadDepartments();
        viewModel.loadSites();

        // 5. Ajouter un listener au bouton d'envoi
        buttonSendForm.setOnClickListener(v -> onSendEmployeeData());
    }

    // Méthode pour observer le ViewModel
    private void observeViewModel() {
        // Observer la liste des départements
        viewModel.getDepartmentsLiveData().observe(this, departments -> {
            if (departments != null) {
                setupDepartmentsSpinner(departments);
            }
        });

        // Observer la liste des sites
        viewModel.getSitesLiveData().observe(this, sites -> {
            if (sites != null) {
                setupSitesSpinner(sites);
            }
        });

        // Observer les messages Toast
        viewModel.getToastMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(AddEmployee.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Observer l'événement de fermeture d'écran
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                finish(); // Ferme l'Activity après succès
            }
        });
    }

    // Configuration du spinner des départements
    private void setupDepartmentsSpinner(List<Department> departments) {
        // Ajouter une option par défaut
        List<Department> deptList = new ArrayList<>();
        deptList.add(new Department(-1L, "Sélectionnez un département"));
        deptList.addAll(departments);

        ArrayAdapter<Department> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                deptList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);
    }

    // Configuration du spinner des sites
    private void setupSitesSpinner(List<Site> sites) {
        // Ajouter une option par défaut
        List<Site> siteList = new ArrayList<>();
        siteList.add(new Site(-1L, "Sélectionnez un site"));
        siteList.addAll(sites);

        ArrayAdapter<Site> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                siteList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSite.setAdapter(adapter);
    }

    // Méthode appelée lors du clic sur le bouton "Envoyer"
    private void onSendEmployeeData() {
        String name      = editTextName.getText().toString().trim();
        String firstname = editTextFirstname.getText().toString().trim();
        String phone     = editTextPhone.getText().toString().trim();
        String mail      = editTextEmailAddress.getText().toString().trim();

        // Récupérer le département et le site sélectionnés
        Department selectedDepartment = (Department) spinnerService.getSelectedItem();
        Site selectedSite             = (Site) spinnerSite.getSelectedItem();

        // Validations
        if (selectedDepartment == null || selectedDepartment.getIdDepartment() == -1L) {
            Toast.makeText(this, "Veuillez sélectionner un département valide.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSite == null || selectedSite.getIdSite() == -1L) {
            Toast.makeText(this, "Veuillez sélectionner un site valide.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty() || firstname.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construire l'objet Employee
        Employee employee = new Employee(name, firstname, phone, mail,
                selectedDepartment.getIdDepartment(),
                selectedSite.getIdSite());

        // Appeler la méthode du ViewModel pour sauvegarder
        viewModel.saveEmployee(employee);

        // Si vous voulez vider les champs tout de suite
        clearFields();
    }

    private void clearFields() {
        editTextName.setText("");
        editTextFirstname.setText("");
        editTextPhone.setText("");
        editTextEmailAddress.setText("");
        spinnerService.setSelection(0);
        spinnerSite.setSelection(0);
    }
}
