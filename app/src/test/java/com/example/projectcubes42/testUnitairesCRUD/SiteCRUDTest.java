package com.example.projectcubes42.testUnitairesCRUD;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SiteCRUDTest {

    @Mock
    private Call<List<Site>> mockCall;

    @Mock
    private SiteRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule(); // ✅ Exécution synchrone de LiveData

    @Test
    public void testSaveDepartment_Success() {
        Site site = new Site(1L, "Paris");
        repository.addSite(site);
        verify(repository, times(1)).addSite(site);
    }

    @Test
    public void testGetDepartmentById_Success() throws Exception {
        // Arrange
        Site site = new Site(1L, "Paris");
        // Création d'un Call<Employee> simulé
        Call<Site> mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(Response.success(site)); // Simulation du succès

        when(repository.getSiteById(1L)).thenReturn(mockCall);

        // Act
        Call<Site> resultCall = repository.getSiteById(1L);
        Site result = resultCall.execute().body(); // Exécuter l'appel simulé

        // Assert
        assertNotNull(result);
        assertEquals("Paris", result.getCity());

    }

    @Test
    public void testUpdateDepartment_Success() {
        Site site = new Site(1L, "Paris");
        repository.updateSite(1L, site);
        verify(repository, times(1)).updateSite(1l, site);
    }

    @Test
    public void testDeleteDepartment_Success() {
        repository.deleteSite(3L);
        verify(repository, times(1)).deleteSite(3L);
    }

    @Test
    public void testGetAllDepartments_Success() throws Exception {
        // Arrange
        List<Site> departments = Arrays.asList(
                new Site(1L, "Paris"),
                new Site(2L, "Marseille")
        );

        // ✅ Simuler la réponse Retrofit
        when(mockCall.execute()).thenReturn(Response.success(departments));
        when(repository.getAllSites()).thenReturn(mockCall);

        // Act
        Call<List<Site>> resultCall = repository.getAllSites();
        List<Site> result = resultCall.execute().body(); // ✅ Exécuter l'appel simulé

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Paris", result.get(0).getCity());
        assertEquals("Marseille", result.get(1).getCity());

        System.out.println("✅ testGetAllDepartments_Success : Tous les départements ont bien été récupérés !");
    }
}
