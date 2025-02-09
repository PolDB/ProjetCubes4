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
//viewModel rataché à l'activité AddEmployee
public class AddEmployeeViewModel extends ViewModel {

    private final EmployeeRepository repository;

    // LiveData pour les listes de départements et sites
    private final MutableLiveData<List<Department>> departmentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Site>> sitesLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages (toast) dans l’UI
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour fermer l’écran après succès (optionnel)
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();

    public AddEmployeeViewModel() {
        repository = new EmployeeRepository();
    }
    public AddEmployeeViewModel(EmployeeRepository repository) {
        this.repository = repository;
    }

    // -------------------------
    //  GETTERS
    // -------------------------
    public LiveData<List<Department>> getDepartmentsLiveData() {
        return departmentsLiveData;
    }

    public LiveData<List<Site>> getSitesLiveData() {
        return sitesLiveData;
    }

    public LiveData<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    // -------------------------
    //  CHARGER DEPARTEMENTS
    // -------------------------
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

    // -------------------------
    //  CHARGER SITES
    // -------------------------
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

    // -------------------------
    //  SAUVEGARDER EMPLOYÉ
    // -------------------------
    public void saveEmployee(Employee employee) {
        repository.saveEmployee(employee).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Employé ajouté avec succès !");
                    // Signaler à l'UI de se fermer (optionnel)
                    closeScreenEvent.setValue(true);
                } else {
                    toastMessageLiveData.setValue("Erreur lors de l'ajout de l'employé");
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                toastMessageLiveData.setValue("Échec de l'ajout de l'employé : " + t.getMessage());
            }
        });
    }
}
