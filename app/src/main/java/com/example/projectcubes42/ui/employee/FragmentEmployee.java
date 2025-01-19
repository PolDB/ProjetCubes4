package com.example.projectcubes42.ui.employee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.LoginActivity;
import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.model.Employee;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentEmployee extends Fragment {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;

    // Boutons et éléments UI
    private Button addEmployeeButton, openBottomSheetButton, openAlertDialogSite, searchButton;
    private ImageView imageView; // exemple si besoin d'image

    // Référence au ViewModel
    private EmployeeViewModel employeeViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_employee, container, false);

        // Initialise les vues
        recyclerView           = root.findViewById(R.id.contactRecyclerView);
        addEmployeeButton      = root.findViewById(R.id.button_add_employee);
        openBottomSheetButton  = root.findViewById(R.id.button_sort_department);
        openAlertDialogSite    = root.findViewById(R.id.button_sort_site);
        searchButton           = root.findViewById(R.id.button_search_employee);
        // imageView = root.findViewById(R.id.votreImageView); // Si vous en avez besoin

        // Configuration de la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EmployeeAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Instancier (ou récupérer) le ViewModel
        employeeViewModel = new ViewModelProvider(requireActivity()).get(EmployeeViewModel.class);

        // Observer la liste filtrée pour mettre à jour l'adaptateur
        employeeViewModel.getFilteredEmployees().observe(getViewLifecycleOwner(), filteredList -> {
            if (filteredList != null) {
                adapter.updateData(filteredList);
            }
        });

        // Charger la liste des employés via le ViewModel
        employeeViewModel.loadEmployees();

        // Vérifier le rôle utilisateur pour afficher / cacher les boutons (admin/visiteur)
        checkUserRole();

        // Bouton pour ajouter un employé
        addEmployeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEmployee.class);
            startActivity(intent);
        });

        // Bouton pour filtrer par département
        openBottomSheetButton.setOnClickListener(v -> showFilterDialogDepartment());

        // Bouton pour filtrer par site
        openAlertDialogSite.setOnClickListener(v -> showFilterDialogSite());

        // Bouton de recherche
        searchButton.setOnClickListener(v -> showSearchDialog());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Si vous souhaitez recharger la liste quand on revient sur le fragment, décommentez:
        // employeeViewModel.loadEmployees();
        // checkUserRole() si nécessaire
    }

    // Méthode pour vérifier le rôle de l’utilisateur (admin ou visiteur) et gérer la visibilité
    private void checkUserRole() {
        SharedPreferences sharedPreferences = requireActivity()
                .getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", "visitor");

        Log.d("CHECK_USER_ROLE", "Rôle récupéré : " + userRole);

        if (!userRole.equals("admin")) {
            addEmployeeButton.setVisibility(View.GONE);
            Log.d("CHECK_USER_ROLE", "Utilisateur non admin, bouton caché");
        } else {
            addEmployeeButton.setVisibility(View.VISIBLE);
            openBottomSheetButton.setVisibility(View.VISIBLE);
            openAlertDialogSite.setVisibility(View.VISIBLE);
            Log.d("CHECK_USER_ROLE", "Utilisateur admin, boutons affichés");
        }
    }

    // Récupération d’un éventuel retour d’Activity (ex: reconnaissance vocale)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Exemple de gestion de la reconnaissance vocale
        if (requestCode == 100 && resultCode == requireActivity().RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results.contains("admin access")) { // Phrase secrète
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    }

    // -------------------------------------------------------------------
    //  Dialogue de filtre par département
    // -------------------------------------------------------------------
    private void showFilterDialogDepartment() {
        // On demande au ViewModel de charger les départements
        employeeViewModel.loadDepartments();
        employeeViewModel.getDepartments().observe(getViewLifecycleOwner(), departments -> {
            if (departments == null || departments.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Impossible de charger les départements",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String[] departmentNames = new String[departments.size()];
            for (int i = 0; i < departments.size(); i++) {
                departmentNames[i] = departments.get(i).getDepartment_name();
            }

            // Affichage du dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrer par services");
            builder.setItems(departmentNames, (dialog, which) -> {
                long selectedDepartmentId = departments.get(which).getIdDepartment();
                // Appel de la méthode de filtrage dans le ViewModel
                employeeViewModel.filterByDepartment(selectedDepartmentId);
            });
            builder.create().show();
        });
    }

    // -------------------------------------------------------------------
    //  Dialogue de filtre par site
    // -------------------------------------------------------------------
    private void showFilterDialogSite() {
        employeeViewModel.loadSites();
        employeeViewModel.getSites().observe(getViewLifecycleOwner(), sites -> {
            if (sites == null || sites.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Impossible de charger les sites",
                        Toast.LENGTH_SHORT).show();
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

    // -------------------------------------------------------------------
    //  Dialogue de recherche
    // -------------------------------------------------------------------
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Rechercher un employé");

        // Inflate le layout custom du dialog
        final View customLayout = LayoutInflater
                .from(requireContext())
                .inflate(R.layout.dialog_search, null);
        builder.setView(customLayout);

        final EditText searchInput = customLayout.findViewById(R.id.edit_search);

        builder.setPositiveButton("Rechercher", (dialog, which) -> {
            String query = searchInput.getText().toString().trim();
            // Appel de la logique de recherche dans le ViewModel
            employeeViewModel.searchEmployees(query);
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
