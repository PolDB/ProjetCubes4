package com.example.projectcubes42.ui.employee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.LoginActivity;
import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;

import java.util.ArrayList;
import java.util.List;
//Fragment qui affiche les employés du recyclerView pour l'interface utilisateur
public class FragmentEmployeeVisitor extends Fragment {

    private RecyclerView recyclerView;
    private EmployeeAdapterVisitor adapter;

    private Button openBottomSheetButton, openAlertDialogSite, searchButton;
    private ImageView imageView;

    private EmployeeViewModel employeeViewModel;
    private int clickCount = 0;
    private static final int REQUIRED_CLICKS = 5;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_visitor, container, false);

        // Initialisation des views
        recyclerView = root.findViewById(R.id.contactRecyclerView);
        openBottomSheetButton = root.findViewById(R.id.button_sort_department_visitor);
        openAlertDialogSite = root.findViewById(R.id.button_sort_site_visitor);
        searchButton = root.findViewById(R.id.button_search_employee_visitor);
        imageView = root.findViewById(R.id.imageViewVisitor);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EmployeeAdapterVisitor(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Récupération (ou création) du ViewModel
        employeeViewModel = new ViewModelProvider(requireActivity()).get(EmployeeViewModel.class);

        // Observers
        employeeViewModel.getFilteredEmployees().observe(getViewLifecycleOwner(), filteredList -> {
            if (filteredList != null) {
                adapter.updateData(filteredList);
            }
        });

        // Chargement initial des données
        employeeViewModel.loadEmployees();

        // Configuration des listeners
        openBottomSheetButton.setOnClickListener(v -> showFilterDialogDepartment());
        openAlertDialogSite.setOnClickListener(v -> showFilterDialogSite());
        searchButton.setOnClickListener(v -> showSearchDialog());
        imageView.setOnClickListener(v -> {
            clickCount++;

            if(clickCount ==  REQUIRED_CLICKS){
                clickCount=0;
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            }

        });

        return root;
    }

    // Au retour sur le fragment, on peut recharger si besoin
    @Override
    public void onResume() {
        super.onResume();
        employeeViewModel.loadEmployees();
        // Vous pouvez rappeler loadEmployees() si vous souhaitez rafraîchir
        // employeeViewModel.loadEmployees();
    }

    // Affichage du dialog pour filtrer par Département
    private void showFilterDialogDepartment() {
        // Charger la liste des départements via le ViewModel
        employeeViewModel.loadDepartments();
        employeeViewModel.getDepartments().observe(getViewLifecycleOwner(), departments -> {
            if (departments == null || departments.isEmpty()) {
                Toast.makeText(requireContext(), "Impossible de charger les départements", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] departmentNames = new String[departments.size()];
            for (int i = 0; i < departments.size(); i++) {
                departmentNames[i] = departments.get(i).getDepartment_name();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrer par services");
            builder.setItems(departmentNames, (dialog, which) -> {
                long selectedDepartmentId = departments.get(which).getIdDepartment();
                employeeViewModel.filterByDepartment(selectedDepartmentId);
            });
            builder.create().show();
        });
    }

    // Affichage du dialog pour filtrer par Site
    private void showFilterDialogSite() {
        // Charger la liste des sites via le ViewModel
        employeeViewModel.loadSites();
        employeeViewModel.getSites().observe(getViewLifecycleOwner(), sites -> {
            if (sites == null || sites.isEmpty()) {
                Toast.makeText(requireContext(), "Impossible de charger les sites", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] siteNames = new String[sites.size()];
            for (int i = 0; i < sites.size(); i++) {
                siteNames[i] = sites.get(i).getCity();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrer par site");
            builder.setItems(siteNames, (dialog, which) -> {
                long selectedSiteId = sites.get(which).getIdSite();
                employeeViewModel.filterBySite(selectedSiteId);
            });
            builder.create().show();
        });
    }

    // Affichage du dialog de recherche
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Rechercher un employé");

        final View customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_search, null);
        builder.setView(customLayout);

        final EditText searchInput = customLayout.findViewById(R.id.edit_search);

        builder.setPositiveButton("Rechercher", (dialog, which) -> {
            String query = searchInput.getText().toString().trim();
            employeeViewModel.searchEmployees(query);
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
