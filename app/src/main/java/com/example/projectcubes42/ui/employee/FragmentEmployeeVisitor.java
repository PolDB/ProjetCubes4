package com.example.projectcubes42.ui.employee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.Drawer_activity;
import com.example.projectcubes42.LoginActivity;
import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import com.example.projectcubes42.data.model.Employee;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEmployeeVisitor extends Fragment {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private ApiService employeeApi;
    private Button openBottomSheetButton, openAlertDialogSite, searchButton;
    private List<Employee> employeeList = new ArrayList<>();
    private List<Employee> filteredList = new ArrayList<>();
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_visitor, container, false);

        recyclerView = root.findViewById(R.id.contactRecyclerView);
        openBottomSheetButton = root.findViewById(R.id.button_sort_department_visitor);
        openAlertDialogSite = root.findViewById(R.id.button_sort_site_visitor);
        searchButton = root.findViewById(R.id.button_search_employee_visitor);
        imageView = root.findViewById(R.id.imageViewVisitor);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        openBottomSheetButton.setOnClickListener(v -> showFilterDialogDepartment());
        openAlertDialogSite.setOnClickListener(v -> showFilterDialogSite());
        searchButton.setOnClickListener(v -> showSearchDialog());
        imageView.setOnClickListener(v -> {  Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);});

        employeeApi = ApiClient.getClient().create(ApiService.class);
        fetchEmployees();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchEmployees();
    }


    private void fetchEmployees() {
        Call<List<Employee>> call = employeeApi.getAllEmployees();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Échec du chargement des employés");
                    Toast.makeText(requireContext(), "Aucun employé trouvé", Toast.LENGTH_SHORT).show();
                    return;
                }

                employeeList.clear();
                employeeList.addAll(response.body());
                filteredList.clear();
                filteredList.addAll(employeeList);

                adapter = new EmployeeAdapter(requireContext(), filteredList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e("API_FAILURE", "Erreur réseau : " + t.getMessage());
                Toast.makeText(requireContext(), "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showFilterDialogDepartment() {
        // Charger la liste des départements via l'API
        Call<List<Department>> call = employeeApi.getAllDepartments(); // Assurez-vous que la méthode existe dans ApiService
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Department> departments = response.body();

                    // Préparez un tableau contenant uniquement les noms des départements
                    String[] departmentNames = new String[departments.size()];
                    Log.d("DEPARTMENTS", "Départements chargés : " + departments.size());
                    for (int i = 0; i < departments.size(); i++) {
                        departmentNames[i] = departments.get(i).getDepartment_name();

                    }


                    // Créez l'AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Filtrer par services");

                    // Ajoutez la liste des départements
                    builder.setItems(departmentNames, (dialog, which) -> {
                        // Récupérez l'idDepartment du département sélectionné
                        long selectedDepartmentId = departments.get(which).getIdDepartment();

                        // Filtrez le RecyclerView
                        filterRecyclerViewByDepartment(selectedDepartmentId);
                    });

                    // Affichez la boîte de dialogue
                    builder.create().show();
                } else {
                    Toast.makeText(requireContext(), "Impossible de charger les départements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFilterDialogSite() {
        // Charger la liste des départements via l'API
        Call<List<Site>> call = employeeApi.getAllSites(); // Assurez-vous que la méthode existe dans ApiService
        call.enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Site> sites = response.body();

                    // Préparez un tableau contenant uniquement les noms des départements
                    String[] sitesNames = new String[sites.size()];
                    Log.d("DEPARTMENTS", "Départements chargés : " + sites.size());
                    for (int i = 0; i < sites.size(); i++) {
                        sitesNames[i] = sites.get(i).getCity();

                    }


                    // Créez l'AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Filtrer par site");

                    // Ajoutez la liste des départements
                    builder.setItems(sitesNames, (dialog, which) -> {
                        // Récupérez l'idDepartment du département sélectionné
                        long selectedSiteId = sites.get(which).getIdSite();

                        // Filtrez le RecyclerView
                        filterRecyclerViewBySite(selectedSiteId);
                    });

                    // Affichez la boîte de dialogue
                    builder.create().show();
                } else {
                    Toast.makeText(requireContext(), "Impossible de charger les départements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour filtrer les employés par departmentId
    private void filterRecyclerViewByDepartment(long departmentId) {
        // Si la liste filtrée est vide, appliquez le filtre sur la liste complète
        List<Employee> sourceList = filteredList.isEmpty() ? new ArrayList<>(employeeList) : new ArrayList<>(filteredList);

        // Réinitialisez la liste filtrée pour ne garder que les résultats
        filteredList.clear();
        for (Employee employee : sourceList) {
            if (employee.idDepartment() == departmentId) { // Assurez-vous que getIdDepartment() existe
                filteredList.add(employee);
            }
        }

        Log.d("FILTER", "Département sélectionné ID : " + departmentId);
        Log.d("FILTER", "Employés filtrés : " + filteredList.size());

        // Mettez à jour l'adaptateur
        updateRecyclerView();
    }

    private void filterRecyclerViewBySite(long siteId) {
        // Si la liste filtrée est vide, appliquez le filtre sur la liste complète
        List<Employee> sourceList = filteredList.isEmpty() ? new ArrayList<>(employeeList) : new ArrayList<>(filteredList);

        // Réinitialisez la liste filtrée pour ne garder que les résultats
        filteredList.clear();
        for (Employee employee : sourceList) {
            if (employee.idSite() == siteId) { // Assurez-vous que getIdSite() existe
                filteredList.add(employee);
            }
        }

        Log.d("FILTER", "Site sélectionné ID : " + siteId);
        Log.d("FILTER", "Employés filtrés : " + filteredList.size());

        // Mettez à jour l'adaptateur
        updateRecyclerView();
    }

    // Méthode pour mettre à jour RecyclerView
    private void updateRecyclerView() {
        if (adapter != null) {
            adapter.updateData(filteredList);
            Log.d("FILTER", "RecyclerView mis à jour avec " + filteredList.size() + " employés");
        } else {
            Log.e("FILTER_ERROR", "L'adaptateur est null");
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Rechercher un employé");

        // Ajouter un champ de texte
        final View customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_search, null);
        builder.setView(customLayout);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText searchInput = customLayout.findViewById(R.id.edit_search);

        // Boutons
        builder.setPositiveButton("Rechercher", (dialog, which) -> {
            String query = searchInput.getText().toString().trim();
            filterEmployees(query);
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void filterEmployees(String query) {
        if (query.isEmpty()) {
            // Restaurer la liste complète si la recherche est vide
            filteredList.clear();
            filteredList.addAll(employeeList);
        } else {
            // Convertir la requête en minuscules pour une recherche insensible à la casse
            String lowerCaseQuery = query.toLowerCase();
            filteredList.clear();

            // Filtrer les employés par nom ou prénom contenant la requête
            for (Employee employee : employeeList) {
                if ((employee.getName() != null && employee.getName().toLowerCase().contains(lowerCaseQuery)) ||
                        (employee.getFirstname() != null && employee.getFirstname().toLowerCase().contains(lowerCaseQuery))) {
                    filteredList.add(employee);
                }
            }
        }

        // Mettre à jour l'adaptateur
        if (adapter != null) {
            adapter.updateData(filteredList);
        }
    }
}


