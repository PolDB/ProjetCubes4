package com.example.projectcubes42.testIntegration.CRUD.department;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.DepartmentRepository;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.department.DepartmentDetailViewModel;
import com.example.projectcubes42.ui.employee.EmployeeDetailViewModel;

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

public class DepartmentDetailTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private DepartmentRepository mockRepository;

    @Mock
    private Call<Department> mockCall;

    @Mock
    private Observer<String> toastObserver;

    @Mock
    private Observer<Boolean> closeScreenObserver;

    @Mock
    private Observer<List<Department>> employeeObserver; // Observer pour List<Employee>

    @Captor
    private ArgumentCaptor<Callback<Department>> callbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> voidCallbackCaptor;

    private DepartmentDetailViewModel departmentDetailViewModel;

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);
        departmentDetailViewModel = new DepartmentDetailViewModel();



        // Utiliser la réflexion pour injecter le repository mocké dans EmployeeViewModel
        injectMockRepository(departmentDetailViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(DepartmentDetailViewModel viewModel, DepartmentRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans EmployeeViewModel
        Field repositoryField = DepartmentDetailViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }



    @Test
    public void testUpdateDepartment_Success() {
        // Étape 1 : Préparer les données simulées
        Department department = new Department(1L, "Direction");

        // Créer un mock de Call<Employee> pour updateEmployee
        Call<Department> mockUpdateCall = mock(Call.class);

        // Créer un mock de Call<Employee> pour getEmployeeById
        Call<Department> mockGetCall = mock(Call.class);

        // Simuler un employé existant retourné par l'API
        when(mockRepository.getDepartmentById(department.getIdDepartment())).thenReturn(mockGetCall);

        // Configurer le Repository pour retourner le Call mocké pour updateEmployee
        when(mockRepository.updateDepartment(department.getIdDepartment(), department)).thenReturn(mockUpdateCall);

        // Forcer l'observation du LiveData
        departmentDetailViewModel.getToastMessage().observeForever(toastObserver);

        // Étape 2 : Appeler la méthode à tester
        departmentDetailViewModel.updateDepartment(1L, department);

        // Vérifier que enqueue est appelé
        verify(mockUpdateCall).enqueue(callbackCaptor.capture());

        // Étape 3 : Simuler la réponse réussie de l'API
        Response<Department> successResponse = Response.success(department);
        System.out.println("Callback onResponse appelé !");  // Debug
        callbackCaptor.getValue().onResponse(mockUpdateCall, successResponse);

        // Étape 4 : Vérifier que les LiveData ont été mis à jour correctement
        verify(toastObserver).onChanged("Département mis à jour avec succès !");

    }

    @Test
    public void testDeleteDepartment_Success() {
        // Étape 1 : Préparer les données simulées
        Long departmentId = 1L;

        // Créer un mock de Call<Void> pour deleteEmployee
        Call<Void> mockDeleteCall = mock(Call.class);

        // Configurer le Repository pour retourner le Call mocké lorsqu'on supprime un employé
        when(mockRepository.deleteDepartment(departmentId)).thenReturn(mockDeleteCall);

        // Forcer l'observation du LiveData
        departmentDetailViewModel.getToastMessage().observeForever(toastObserver);

        // Étape 2 : Appeler la méthode à tester
        departmentDetailViewModel.deleteDepartment(departmentId);

        // Vérifier que enqueue est appelé
        verify(mockDeleteCall).enqueue(voidCallbackCaptor.capture());

        // Étape 3 : Simuler une réponse réussie de l'API
        Response<Void> successResponse = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockDeleteCall, successResponse);

        // Étape 4 : Vérifier que le message Toast a bien été mis à jour
        verify(toastObserver).onChanged("Département supprimé avec succès !");
    }



}