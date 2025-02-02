package com.example.projectcubes42.testIntegration.CRUD.employee;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.employee.EmployeeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class EmployeeTest {

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

    private EmployeeViewModel employeeViewModel;

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Initialiser EmployeeViewModel avec le constructeur par défaut
        employeeViewModel = new EmployeeViewModel();

        // Utiliser la réflexion pour injecter le repository mocké dans EmployeeViewModel
        injectMockRepository(employeeViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(EmployeeViewModel viewModel, EmployeeRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans EmployeeViewModel
        Field repositoryField = EmployeeViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }

    @Test
    public void testGetAllEmployees_Success() {
        // Préparer les données simulées
        List<Employee> mockEmployees = Arrays.asList(
                new Employee("John Doe", "Developer", "0625947894", "Paul@gmail.com", 1L, 1L),
                new Employee("Jane Smith", "Designer", "0625947895", "Jane@gmail.com", 2L, 2L)
        );

        // Créer une LiveData factice avec les données
        MutableLiveData<List<Employee>> mockLiveData = new MutableLiveData<>();
        mockLiveData.setValue(mockEmployees);

        // Configurer le mock pour retourner la LiveData
        when(mockRepository.getAllEmployees()).thenReturn(mockLiveData);

        // Observer pour LiveData
        LiveData<List<Employee>> liveData = mockRepository.getAllEmployees();
        Observer<List<Employee>> observer = mock(Observer.class);
        liveData.observeForever(observer);

        // Vérifier que l'observateur a bien reçu les données
        verify(observer).onChanged(mockEmployees);
    }
}


