package com.example.projectcubes42.testIntegration.employee;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.employee.EmployeeDetailViewModel;
import com.example.projectcubes42.ui.employee.EmployeeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private EmployeeRepository mockRepository;

    @Mock
    private Call<Employee> mockCall;

    @Mock
    private Observer<String> toastObserver;

    @Mock
    private Observer<Boolean> closeScreenObserver;

    @Mock
    private Observer<List<Employee>> employeeObserver; // Observer pour List<Employee>

    @Captor
    private ArgumentCaptor<Callback<Employee>> callbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> voidCallbackCaptor;

    private EmployeeDetailViewModel employeeDetailViewModel;

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);
        employeeDetailViewModel = new EmployeeDetailViewModel();



        // Utiliser la réflexion pour injecter le repository mocké dans EmployeeViewModel
        injectMockRepository(employeeDetailViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(EmployeeDetailViewModel viewModel, EmployeeRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans EmployeeViewModel
        Field repositoryField = EmployeeDetailViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }



    @Test
    public void testUpdateEmployee_Success() {
        // Étape 1 : Préparer les données simulées
        Employee employeeToUpdate = new Employee("John Doe", "Developer", "0625947894", "Paul@gmail.com", 1L, 1L);

        // Créer un mock de Call<Employee> pour updateEmployee
        Call<Employee> mockUpdateCall = mock(Call.class);

        // Créer un mock de Call<Employee> pour getEmployeeById
        Call<Employee> mockGetCall = mock(Call.class);

        // Simuler un employé existant retourné par l'API
        when(mockRepository.getEmployeeById(employeeToUpdate.getId())).thenReturn(mockGetCall);

        // Configurer le Repository pour retourner le Call mocké pour updateEmployee
        when(mockRepository.updateEmployee(employeeToUpdate.getId(), employeeToUpdate)).thenReturn(mockUpdateCall);

        // Forcer l'observation du LiveData
        employeeDetailViewModel.getToastMessage().observeForever(toastObserver);

        // Étape 2 : Appeler la méthode à tester
        employeeDetailViewModel.updateEmployee(employeeToUpdate);

        // Vérifier que enqueue est appelé
        verify(mockUpdateCall).enqueue(callbackCaptor.capture());

        // Étape 3 : Simuler la réponse réussie de l'API
        Response<Employee> successResponse = Response.success(employeeToUpdate);
        System.out.println("Callback onResponse appelé !");  // Debug
        callbackCaptor.getValue().onResponse(mockUpdateCall, successResponse);

        // Étape 4 : Vérifier que les LiveData ont été mis à jour correctement
        verify(toastObserver).onChanged("Employé mis à jour avec succès !");

    }

    @Test
    public void testDeleteEmployee_Success() {
        // Étape 1 : Préparer les données simulées
        Long employeeId = 1L;

        // Créer un mock de Call<Void> pour deleteEmployee
        Call<Void> mockDeleteCall = mock(Call.class);

        // Configurer le Repository pour retourner le Call mocké lorsqu'on supprime un employé
        when(mockRepository.deleteEmployee(employeeId)).thenReturn(mockDeleteCall);

        // Forcer l'observation du LiveData
        employeeDetailViewModel.getToastMessage().observeForever(toastObserver);

        // Étape 2 : Appeler la méthode à tester
        employeeDetailViewModel.deleteEmployee(employeeId);

        // Vérifier que enqueue est appelé
        verify(mockDeleteCall).enqueue(voidCallbackCaptor.capture());

        // Étape 3 : Simuler une réponse réussie de l'API
        Response<Void> successResponse = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockDeleteCall, successResponse);

        // Étape 4 : Vérifier que le message Toast a bien été mis à jour
        verify(toastObserver).onChanged("Employé supprimé avec succès !");
    }



}