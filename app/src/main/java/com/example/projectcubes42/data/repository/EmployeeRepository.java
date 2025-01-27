package com.example.projectcubes42.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {

    private final ApiService apiService;

    public EmployeeRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Employee>> getAllEmployees() {
        MutableLiveData<List<Employee>> employeesLiveData = new MutableLiveData<>();
        apiService.getAllEmployees().enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    employeesLiveData.setValue(response.body());
                } else {
                    // En cas de réponse vide ou non successful, on peut setValue(null) ou envoyer une liste vide
                    employeesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                employeesLiveData.setValue(null);
            }
        });
        return employeesLiveData;
    }

    public LiveData<List<Department>> getAllDepartments() {
        MutableLiveData<List<Department>> departmentsLiveData = new MutableLiveData<>();
        apiService.getAllDepartments().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    departmentsLiveData.setValue(response.body());
                } else {
                    departmentsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                departmentsLiveData.setValue(null);
            }
        });
        return departmentsLiveData;
    }

    public LiveData<List<Site>> getAllSites() {
        MutableLiveData<List<Site>> sitesLiveData = new MutableLiveData<>();
        apiService.getAllSites().enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sitesLiveData.setValue(response.body());
                } else {
                    sitesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                sitesLiveData.setValue(null);
            }
        });
        return sitesLiveData;
    }

    public Call<Employee> getEmployeeById(Long employeeId) {
        return apiService.getEmployeeById(employeeId);
    }

    public Call<Employee> updateEmployee(Long id, Employee employee) {
        return apiService.updateEmployee(id, employee);
    }

    public Call<Void> deleteEmployee(Long id) {
        return apiService.deleteEmployee(id);
    }

    public Call<List<Department>> getAllDepartments1() {
        return apiService.getAllDepartments();
    }

    public Call<List<Site>> getAllSites1() {
        return apiService.getAllSites();
    }

    public Call<Employee> saveEmployee(Employee employee) {
        return apiService.saveEmployee(employee);
    }
    public Call<List<Employee>> getEmployeesByDepartment(Long departmentId) {
        return apiService.getEmployeesByDepartment(departmentId);
    }

    public Call<List<Employee>> getEmployeesBySite(Long siteId) {
        return apiService.getEmployeesBySite(siteId);
    }
}
