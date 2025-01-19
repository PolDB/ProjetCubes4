package com.example.projectcubes42.ui.department;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddDepartment extends AppCompatActivity {

    private EditText editTextService;
    private FloatingActionButton buttonSendFormService;

    // ViewModel
    private AddDepartmentViewModel viewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_service);

        // 1. Instancier le ViewModel
        viewModel = new ViewModelProvider(this).get(AddDepartmentViewModel.class);

        // 2. Initialiser les vues
        editTextService = findViewById(R.id.editTextDepartment);
        buttonSendFormService = findViewById(R.id.buttonSendFormDepartement);

        // 3. Observer le ViewModel
        observeViewModel();

        // 4. Click Listener
        buttonSendFormService.setOnClickListener(v -> sendServiceData());
    }

    private void observeViewModel() {
        // Afficher un Toast en cas de message
        viewModel.getToastMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Log.d("AddDepartment", message);
                // Vous pouvez utiliser Toast si vous préférez
                // Toast.makeText(AddDepartment.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Fermer l'Activity quand l'événement est émis
        viewModel.getCloseScreenEvent().observe(this, shouldClose -> {
            if (Boolean.TRUE.equals(shouldClose)) {
                // Vous pouvez vider les champs avant de terminer, ou le faire avant
                clearFields();
                finish();
            }
        });
    }

    private void sendServiceData() {
        String department_name = editTextService.getText().toString().trim();
        if (department_name.isEmpty()) {
            Log.e("AddDepartment", "Le nom du département est vide.");
            return;
        }

        // Construire l'objet Department
        Department department = new Department(null, department_name);

        // Appel ViewModel
        viewModel.addDepartment(department);

        // Optionnel : si vous souhaitez vider les champs tout de suite
        // clearFields();
    }

    private void clearFields() {
        editTextService.setText("");
    }
}
