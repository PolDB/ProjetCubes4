package com.example.projectcubes42.data.network;

import android.util.Log;

import com.example.projectcubes42.data.model.AuthResponse;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.LoginRequest;
import com.example.projectcubes42.data.model.Site;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {

    @GET("api/employees")
    Call<List<Employee>> getAllEmployees();

    @GET("api/employees/{id}")
    Call<Employee> getEmployeeById(@Path("id") Long id);

    @POST("api/employees")
    Call<Employee> saveEmployee(@Body Employee employee);

    @PUT("api/employees/{id}")
    Call<Employee> updateEmployee(@Path("id") Long id, @Body Employee employee);

    @DELETE("api/employees/{id}")
    Call<Void> deleteEmployee(@Path("id") Long id);

    @GET("api/department")
    Call<List<Department>> getAllDepartments();

    @GET("api/department/{id}")
    Call<Department> getDepartmentById(@Path("id") Long id);

    //ajout de la méthode pour récupérer un employé depuis un département
    @GET("api/department/{id}/employees")
    Call<List<Employee>> getEmployeesByDepartment(@Path("id") Long departmentId);

    //ajout de la méthode pour récupérer un employé depuis un site
    @GET("api/site/{id}/employees")
    Call<List<Employee>> getEmployeesBySite(@Path("id") Long departmentId);

    @POST("api/department")
    Call<Department> addDepartment(@Body Department department);

    @PUT("api/department/{id}")
    Call<Department> updateDepartment(@Path("id") Long id, @Body Department department);

    @DELETE("api/department/{id}")
    Call<Void> deleteDepartement(@Path("id") Long id);

    @GET("api/site")
    Call<List<Site>> getAllSites();

    @GET("api/site/{id}")
    Call<Site> getSiteById(@Path("id") Long id);

    @POST("api/site")
    Call<Site> addSite(@Body Site site);

    @PUT("api/site/{id}")
    Call<Site> updateSite(@Path("id") Long id, @Body Site site);

    @DELETE("api/site/{id}")
    Call<Void> deleteSite(@Path("id") Long id);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);


}
