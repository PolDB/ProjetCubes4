package com.example.projectcubes42.testIntegration.CRUD.department;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.DepartmentRepository;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.department.AddDepartmentViewModel;
import com.example.projectcubes42.ui.employee.AddEmployeeViewModel;

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

public class AddDepartmentTest {

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

    private AddDepartmentViewModel addDepartmentViewModel;
   

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);


        addDepartmentViewModel = new AddDepartmentViewModel(mockRepository);


        
        // Utiliser la réflexion pour injecter le repository mocké dans EmployeeViewModel
        injectMockRepository(addDepartmentViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(AddDepartmentViewModel viewModel, DepartmentRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans EmployeeViewModel
        Field repositoryField = AddDepartmentViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }

    @Test
    public void testSaveEmployee_Success() {
        // Préparer les données de test
        Department department = new Department(1L, "Direction");

        // Attacher les observateurs
        addDepartmentViewModel.getToastMessageLiveData().observeForever(toastObserver);
        addDepartmentViewModel.getCloseScreenEvent().observeForever(closeScreenObserver);

        // Configurer le repository pour retourner le mockCall
        when(mockRepository.addDepartment(department)).thenReturn(mockCall);

        // Appeler la méthode à tester
        addDepartmentViewModel.addDepartment(department);

        // Capturer le callback passé à enqueue
        verify(mockCall).enqueue(callbackCaptor.capture());
        Callback<Department> capturedCallback = callbackCaptor.getValue();

        // Simuler une réponse réussie
        Response<Department> successResponse = Response.success(department);
        capturedCallback.onResponse(mockCall, successResponse);

        // Vérifier que les LiveData ont été mis à jour correctement
        verify(toastObserver).onChanged("Service ajouté avec succès !");
        verify(closeScreenObserver).onChanged(true);
    }}
