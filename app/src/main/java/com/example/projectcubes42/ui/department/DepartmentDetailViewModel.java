package com.example.projectcubes42.ui.department;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.DepartmentRepository;
import com.example.projectcubes42.data.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentDetailViewModel extends ViewModel {

    private final DepartmentRepository repository;
    private EmployeeRepository employeeRepository;

    // LiveData pour stocker le département courant
    private final MutableLiveData<Department> departmentLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages (Toast)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour signaler la fermeture de l'écran
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();
    private final MutableLiveData<List<Employee>> employeesLiveData = new MutableLiveData<>(new ArrayList<>());

    // LiveData pour stocker la liste filtrée des employés
    private final MutableLiveData<List<Employee>> filteredEmployeesLiveData = new MutableLiveData<>(new ArrayList<>());
    public DepartmentDetailViewModel() {
        this.repository = new DepartmentRepository();
        this.employeeRepository = new EmployeeRepository();
    }

    // GETTERS
    public LiveData<Department> getDepartment() {
        return departmentLiveData;
    }

    public LiveData<String> getToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    // ---------------------------
    // CHARGER UN DEPARTEMENT
    // ---------------------------
    public void fetchDepartment(Long departmentId) {
        repository.getDepartmentById(departmentId).enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if (response.isSuccessful() && response.body() != null) {
                    departmentLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Échec de la récupération du département.");
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    // ---------------------------
    // METTRE A JOUR UN DEPARTEMENT
    // ---------------------------
    public void updateDepartment(Long departmentId, Department department) {
        repository.updateDepartment(departmentId, department).enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toastMessageLiveData.setValue("Département mis à jour avec succès !");
                    // Optionnel : rafraîchir le département depuis l'API
                    fetchDepartment(departmentId);

                    // On peut signaler qu'on souhaite fermer l'écran
                    closeScreenEvent.setValue(true);
                } else {
                    toastMessageLiveData.setValue("Échec de la mise à jour.");
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }

    // ---------------------------
    // SUPPRIMER UN DEPARTEMENT
    // ---------------------------
    public void confirmDeleteDepartment(Context context, Long departmentId, int nbDeSalaries) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Il y a " + nbDeSalaries + " salarié(s) dans ce service.\nVoulez-vous vraiment le supprimer ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    // Si l'utilisateur confirme, on supprime
                    deleteDepartment(departmentId);
                })
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void deleteDepartment(Long departmentId) {
        Log.d("DEBUG", "deleteDepartment id: " + departmentId);
        repository.deleteDepartment(departmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Département supprimé avec succès !");
                    closeScreenEvent.setValue(true);
                } else {
                    toastMessageLiveData.setValue("Échec de la suppression.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }

    public int filterByDepartment(long departmentId) {
        // Récupération des employés via votre repository
        employeeRepository.getAllEmployees().equals(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> allEmployees = response.body();

                    // Filtrer
                    List<Employee> newFilteredList = new ArrayList<>();
                    for (Employee emp : allEmployees) {
                        if (emp.idDepartment() == departmentId) {
                            newFilteredList.add(emp);
                        }
                    }
                    filteredEmployeesLiveData.setValue(newFilteredList);
                }
            }
            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                // Gérer l'erreur
            }
        });
        return 0;
    }

}
