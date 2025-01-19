package com.example.projectcubes42.ui.department;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.repository.DepartmentRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentViewModel extends ViewModel {

    private final DepartmentRepository repository;

    // LiveData pour contenir la liste des départements
    private final MutableLiveData<List<Department>> departmentsLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages (toast, etc.)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    public DepartmentViewModel() {
        repository = new DepartmentRepository();
    }

    // Exposition de la liste sous forme d'un LiveData pour l'observer dans la Vue
    public LiveData<List<Department>> getDepartments() {
        return departmentsLiveData;
    }

    // Exposition d'un LiveData pour les messages toast
    public LiveData<String> getToastMessage() {
        return toastMessageLiveData;
    }

    // Méthode pour charger (ou recharger) les départements
    public void loadDepartments() {
        repository.getAllDepartments().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    departmentsLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Échec du chargement des données");
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }
}
