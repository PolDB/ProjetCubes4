package com.example.projectcubes42;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import androidx.lifecycle.LiveData;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.employee.AddEmployeeViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SaveEmployeeTest {

    @Mock
    private EmployeeRepository repository;


    @Mock
    private AddEmployeeViewModel viewModel;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new AddEmployeeViewModel();

    }

    @Test
    public void testSaveEmployee_Success() {
        // Arrange
        Employee employee = new Employee("Daubrive", "Paul", "0625944475","paul@gmail.com", 1L, 1L);

        // Act
        repository.saveEmployee(employee);

        // Assert
        verify(repository).saveEmployee(employee);
        System.out.println("Le test testSaveEmployee_Success a réussi : l’employé a bien été sauvegardé !");
    }


}
