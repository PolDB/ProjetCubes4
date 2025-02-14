package com.example.projectcubes42.ui.employee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//viewModel rattaché au fragment FragmentEmployee
public class EmployeeViewModel extends ViewModel {

    private final EmployeeRepository repository;
    public EmployeeViewModel() {
        this.repository = new EmployeeRepository();
    }
    // LiveData principal qui contient la liste complète des employés
    private LiveData<List<Employee>> employeesLiveData;
    private LiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData contenant la liste filtrée en fonction des critères
    private final MutableLiveData<List<Employee>> filteredEmployeesLiveData = new MutableLiveData<>(new ArrayList<>());

    // LiveData pour les départements et sites (pour le popup de sélection)
    private LiveData<List<Department>> departmentsLiveData;
    private LiveData<List<Site>> sitesLiveData;




    // Méthode pour initialiser le flux LiveData des employés
    public void loadEmployees() {
        this.employeesLiveData = repository.getAllEmployees();
        // On observe la liste complète des employés pour la copier dans la liste filtrée
        this.employeesLiveData.observeForever(employees -> {
            if (employees != null) {
                // Au premier chargement, la liste filtrée = la liste complète
                filteredEmployeesLiveData.setValue(new ArrayList<>(employees));
            } else {
                filteredEmployeesLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public LiveData<List<Employee>> getEmployees() {
        return employeesLiveData;
    }

    public LiveData<List<Employee>> getFilteredEmployees() {
        return filteredEmployeesLiveData;
    }
    public LiveData<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }
    // Chargement des départements et sites depuis le repository
    public void loadDepartments() {
        departmentsLiveData = repository.getAllDepartments();
    }

    public void loadSites() {
        sitesLiveData = repository.getAllSites();
    }

    public LiveData<List<Department>> getDepartments() {
        return departmentsLiveData;
    }

    public LiveData<List<Site>> getSites() {
        return sitesLiveData;
    }

    // ------------------
    //   LOGIQUE METIER
    // ------------------

    // Filtre par département
    public void filterByDepartment(long departmentId) {
        List<Employee> allEmployees = employeesLiveData.getValue();
        if (allEmployees == null) return;

        List<Employee> newFilteredList = new ArrayList<>();
        for (Employee emp : allEmployees) {
            if (emp.idDepartment() == departmentId) {
                newFilteredList.add(emp);
            }
        }
        filteredEmployeesLiveData.setValue(newFilteredList);
    }

    // Filtre par site
    public void filterBySite(long siteId) {
        List<Employee> allEmployees = employeesLiveData.getValue();
        if (allEmployees == null) return;

        List<Employee> newFilteredList = new ArrayList<>();
        for (Employee emp : allEmployees) {
            if (emp.idSite() == siteId) {
                newFilteredList.add(emp);
            }
        }
        filteredEmployeesLiveData.setValue(newFilteredList);
    }

    // Recherche par nom/prénom
    public void searchEmployees(String query) {
        List<Employee> allEmployees = employeesLiveData.getValue();
        if (allEmployees == null) return;

        if (query.isEmpty()) {
            // Si la recherche est vide, on restaure toute la liste
            filteredEmployeesLiveData.setValue(new ArrayList<>(allEmployees));
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Employee> newFilteredList = new ArrayList<>();
        for (Employee emp : allEmployees) {
            if ((emp.getName() != null && emp.getName().toLowerCase().contains(lowerCaseQuery)) ||
                    (emp.getFirstname() != null && emp.getFirstname().toLowerCase().contains(lowerCaseQuery))) {
                newFilteredList.add(emp);
            }
        }
        filteredEmployeesLiveData.setValue(newFilteredList);
    }

    //méthode pour chercher un employé en fonction de son département
    public Call<List<Employee>> fetchEmployeesByDepartment(Long departmentId) {
        return repository.getEmployeesByDepartment(departmentId);
    }

    //méthode pour chercher un employé en fonction de son département
    public Call<List<Employee>> fetchEmployeesBySite(Long siteId) {
        return repository.getEmployeesBySite(siteId);
    }


}
