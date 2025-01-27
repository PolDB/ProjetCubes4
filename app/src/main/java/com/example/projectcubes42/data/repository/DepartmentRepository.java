package com.example.projectcubes42.data.repository;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import java.util.List;

import retrofit2.Call;

public class DepartmentRepository {

    private final ApiService apiService;

    public DepartmentRepository() {
        // Initialisation de l'interface API
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<Department>> getAllDepartments() {
        return apiService.getAllDepartments();
    }

    public Call<Department> getDepartmentById(Long departmentId) {
        return apiService.getDepartmentById(departmentId);
    }

    // Mettre à jour un département
    public Call<Department> updateDepartment(Long departmentId, Department department) {
        return apiService.updateDepartment(departmentId, department);
    }

    // Supprimer un département
    public Call<Void> deleteDepartment(Long departmentId) {
        return apiService.deleteDepartement(departmentId);
    }

    public Call<Department> addDepartment(Department department) {
        return apiService.addDepartment(department);
    }


}
