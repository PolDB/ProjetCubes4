package com.example.projectcubes42.ui.employee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.EmployeeRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailViewModel extends ViewModel {

    private final EmployeeRepository repository;

    // LiveData pour stocker l'employé actuel
    private final MutableLiveData<Employee> employeeLiveData = new MutableLiveData<>();

    // LiveData pour stocker la liste des départements
    private final MutableLiveData<List<Department>> departmentsLiveData = new MutableLiveData<>();

    // LiveData pour stocker la liste des sites
    private final MutableLiveData<List<Site>> sitesLiveData = new MutableLiveData<>();

    // Eventuel LiveData pour écouter les résultats (succès/erreur)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();
    public EmployeeDetailViewModel() {
        repository = new EmployeeRepository();
    }

    // ------------------
    //   GETTERS
    // ------------------
    public LiveData<Employee> getEmployee() {
        return employeeLiveData;
    }

    public LiveData<List<Department>> getDepartments() {
        return departmentsLiveData;
    }

    public LiveData<List<Site>> getSites() {
        return sitesLiveData;
    }

    public LiveData<String> getToastMessage() {
        return toastMessageLiveData;
    }
    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }
    // ------------------
    //   CHARGEMENT
    // ------------------
    public void fetchEmployee(Long employeeId) {
        repository.getEmployeeById(employeeId).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    employeeLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Erreur lors de la récupération de l'employé.");
                    employeeLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur réseau : " + t.getMessage());
                employeeLiveData.setValue(null);
            }
        });
    }

    public void loadDepartments() {
        repository.getAllDepartments1().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    departmentsLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Erreur récupération des départements");
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                toastMessageLiveData.setValue("Échec connexion API départements : " + t.getMessage());
            }
        });
    }

    public void loadSites() {
        repository.getAllSites1().enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sitesLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Erreur récupération des sites");
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                toastMessageLiveData.setValue("Échec connexion API sites : " + t.getMessage());
            }
        });
    }

    // ------------------
    //  MISE À JOUR
    // ------------------
    public void updateEmployee(Employee employee) {
        repository.updateEmployee(employee.getId(), employee).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Employé mis à jour avec succès !");
                    closeScreenEvent.setValue(true);
                    // Recharger les détails mis à jour
                    fetchEmployee(employee.getId());

                } else {
                    toastMessageLiveData.setValue("Erreur lors de la mise à jour de l'employé");
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur réseau update : " + t.getMessage());
            }
        });
    }

    // ------------------
    //  SUPPRESSION
    // ------------------
    public void deleteEmployee(Long employeeId) {
        repository.deleteEmployee(employeeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Employé supprimé avec succès !");
                } else {
                    toastMessageLiveData.setValue("Erreur lors de la suppression de l'employé");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur réseau suppression : " + t.getMessage());
            }
        });
    }
}
