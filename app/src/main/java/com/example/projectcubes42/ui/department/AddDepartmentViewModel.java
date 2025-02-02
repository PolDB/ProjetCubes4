package com.example.projectcubes42.ui.department;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.repository.DepartmentRepository;
import com.example.projectcubes42.ui.employee.AddEmployeeViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDepartmentViewModel extends ViewModel {

    private final DepartmentRepository repository;

    // LiveData pour afficher des messages (toast)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour fermer l'écran après succès
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();

    public AddDepartmentViewModel(){repository = new DepartmentRepository();}
    public AddDepartmentViewModel(DepartmentRepository repository) {
        this.repository = repository;
    }

    public LiveData<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    public void addDepartment(Department department) {
        repository.addDepartment(department).enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Service ajouté avec succès !");
                    // Signal pour fermer l'écran
                    closeScreenEvent.setValue(true);
                } else {
                    toastMessageLiveData.setValue("Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                toastMessageLiveData.setValue("Échec : " + t.getMessage());
            }
        });
    }
}
