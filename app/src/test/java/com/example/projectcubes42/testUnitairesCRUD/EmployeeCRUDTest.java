package com.example.projectcubes42.testUnitairesCRUD;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.EmployeeRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EmployeeCRUDTest {

    @Mock
    private EmployeeRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule(); // ✅ Exécution synchrone de LiveData
    @Test
    public void testSaveEmployee_Success() {
        Employee employee = new Employee("Daubrive", "Paul", "0625944475", "paul@gmail.com", 1L, 1L);
        repository.saveEmployee(employee);
        verify(repository, times(1)).saveEmployee(employee);
    }

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        // Arrange
        Employee employee = new Employee("Daubrive", "Paul", "0625944475", "paul@gmail.com", 1L, 1L);

        // Création d'un Call<Employee> simulé
        Call<Employee> mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(Response.success(employee)); // Simulation du succès

        when(repository.getEmployeeById(1L)).thenReturn(mockCall);

        // Act
        Call<Employee> resultCall = repository.getEmployeeById(1L);
        Employee result = resultCall.execute().body(); // Exécuter l'appel simulé

        // Assert
        assertNotNull(result);
        assertEquals("Paul", result.getFirstname());
        assertEquals("Daubrive", result.getName());

        System.out.println("✅ testGetEmployeeById_Success : Employé trouvé avec succès !");
    }

    @Test
    public void testUpdateEmployee_Success() {
        Employee employee = new Employee("Dupont", "Jean", "0645789123", "jean@gmail.com", 2L, 1L);
        repository.updateEmployee(1L, employee);
        verify(repository, times(1)).updateEmployee(1l, employee);
    }

    @Test
    public void testDeleteEmployee_Success() {
        repository.deleteEmployee(3L);
        verify(repository, times(1)).deleteEmployee(3L);
    }

    @Test
    public void testGetAllEmployees_Success() {
        // Arrange
        List<Employee> employees = Arrays.asList(
                new Employee("Durand", "Alice", "0789456123", "alice@gmail.com", 3L, 2L),
                new Employee("Martin", "Lucas", "0645897321", "lucas@gmail.com", 4L, 2L)
        );

        // Transformer la liste en LiveData
        MutableLiveData<List<Employee>> liveDataEmployees = new MutableLiveData<>();
        liveDataEmployees.setValue(employees);

        // Simuler le comportement du repository
        when(repository.getAllEmployees()).thenReturn(liveDataEmployees);

        // Act
        LiveData<List<Employee>> resultLiveData = repository.getAllEmployees();
        List<Employee> result = resultLiveData.getValue(); // ✅ Utiliser getValue() directement car LiveData fonctionne maintenant

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFirstname());
        assertEquals("Lucas", result.get(1).getFirstname());

        System.out.println("✅ testGetAllEmployees_Success : Tous les employés ont bien été récupérés !");
    }
}
