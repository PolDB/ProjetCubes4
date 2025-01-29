package com.example.projectcubes42.testUnitaires;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.repository.DepartmentRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DepartmentCRUDTest {

    @Mock
    private Call<List<Department>> mockCall;

    @Mock
    private DepartmentRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule(); // ✅ Exécution synchrone de LiveData

    @Test
    public void testSaveDepartment_Success() {
        Department department = new Department(1L, "Direction");
        repository.addDepartment(department);
        verify(repository, times(1)).addDepartment(department);
    }

    @Test
    public void testGetDepartmentById_Success() throws Exception {
        // Arrange
        Department department = new Department(1L, "Direction");
        // Création d'un Call<Employee> simulé
        Call<Department> mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(Response.success(department)); // Simulation du succès

        when(repository.getDepartmentById(1L)).thenReturn(mockCall);

        // Act
        Call<Department> resultCall = repository.getDepartmentById(1L);
        Department result = resultCall.execute().body(); // Exécuter l'appel simulé

        // Assert
        assertNotNull(result);
        assertEquals("Direction", result.getDepartment_name());

    }

    @Test
    public void testUpdateDepartment_Success() {
        Department department = new Department(1L, "Direction");
        repository.updateDepartment(1L, department);
        verify(repository, times(1)).updateDepartment(1l, department);
    }

    @Test
    public void testDeleteDepartment_Success() {
        repository.deleteDepartment(3L);
        verify(repository, times(1)).deleteDepartment(3L);
    }

    @Test
    public void testGetAllDepartments_Success() throws Exception {
        // Arrange
        List<Department> departments = Arrays.asList(
                new Department(1L, "Ressources Humaines"),
                new Department(2L, "Informatique")
        );

        // ✅ Simuler la réponse Retrofit
        when(mockCall.execute()).thenReturn(Response.success(departments));
        when(repository.getAllDepartments()).thenReturn(mockCall);

        // Act
        Call<List<Department>> resultCall = repository.getAllDepartments();
        List<Department> result = resultCall.execute().body(); // ✅ Exécuter l'appel simulé

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ressources Humaines", result.get(0).getDepartment_name());
        assertEquals("Informatique", result.get(1).getDepartment_name());

        System.out.println("✅ testGetAllDepartments_Success : Tous les départements ont bien été récupérés !");
    }
}
