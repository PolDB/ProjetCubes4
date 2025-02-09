package com.example.projectcubes42.data.model;
// classe pour un service
public class Department {
    private Long idDepartment;
    private String department_name;

    // Constructeur
    public Department(Long idDepartment, String department_name) {
        this.idDepartment = idDepartment;
        this.department_name = department_name;
    }

    // Getters
    public Long getIdDepartment() {
        return idDepartment;
    }

    public String getDepartment_name() {
        return department_name;
    }

    @Override
    public String toString() {
        return department_name; // Affiche uniquement le nom dans le Spinner
    }
}
