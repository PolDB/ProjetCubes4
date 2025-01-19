package com.example.projectcubes42.ui.department;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;

import java.util.List;

public class FragmentDepartment extends Fragment {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private Button addEmployeeButton;

    // Référence à notre ViewModel
    private DepartmentViewModel departmentViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);

        recyclerView = root.findViewById(R.id.contactRecyclerView);
        addEmployeeButton = root.findViewById(R.id.button_add_service);

        // Configuration de la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Instanciation (ou récupération) du ViewModel
        departmentViewModel = new ViewModelProvider(requireActivity())
                .get(DepartmentViewModel.class);

        // Observer la liste des départements pour mettre à jour l'adaptateur
        departmentViewModel.getDepartments().observe(getViewLifecycleOwner(), departments -> {
            if (departments != null) {
                updateRecyclerView(departments);
            }
        });

        // Observer les messages d'erreur pour afficher un Toast
        departmentViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Charger les départements
        departmentViewModel.loadDepartments();

        // Bouton "ajouter un département"
        addEmployeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddDepartment.class);
            startActivity(intent);
        });

        return root;
    }

    // En revenant sur le fragment
    @Override
    public void onResume() {
        super.onResume();
        // Recharger si nécessaire
        departmentViewModel.loadDepartments();
    }

    // Méthode utilitaire pour mettre à jour le RecyclerView
    private void updateRecyclerView(List<Department> departmentList) {
        if (adapter == null) {
            adapter = new DepartmentAdapter(departmentList);
            recyclerView.setAdapter(adapter);
        } else {
            // Si l'adaptateur existe déjà, on peut mettre à jour ses données
            adapter.updateData(departmentList);
        }
    }
}
