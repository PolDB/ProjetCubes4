package com.example.projectcubes42.repository;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import java.util.List;

import retrofit2.Call;

public class EmployeeRepository {
    private final ApiService apiService;

    public EmployeeRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<Employee>> getEmployees(){
        return apiService.getAllEmployees();
    }

    public Call<Employee> addEmployee(Employee employee){
        return apiService.saveEmployee(employee);
    }
}