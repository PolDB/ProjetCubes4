package com.example.projectcubes42.testIntegration.employee;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.repository.EmployeeRepository;
import com.example.projectcubes42.ui.employee.AddEmployeeViewModel;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeTest {

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

    private AddEmployeeViewModel addEmployeeViewModel;
    private EmployeeViewModel employeeViewModel;
    private EmployeeDetailViewModel employeeDetailViewModel;

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Initialiser AddEmployeeViewModel avec le repository mocké
        addEmployeeViewModel = new AddEmployeeViewModel(mockRepository);

        // Initialiser EmployeeViewModel avec le constructeur par défaut
        employeeViewModel = new EmployeeViewModel();

        employeeDetailViewModel = new EmployeeDetailViewModel();



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
    public void testSaveEmployee_Success() {
        // Préparer les données de test
        Employee employee = new Employee("Daubrive", "Paul", "0625944475", "pauldaubrive@gmail.com", 1L, 1L);

        // Attacher les observateurs
        addEmployeeViewModel.getToastMessageLiveData().observeForever(toastObserver);
        addEmployeeViewModel.getCloseScreenEvent().observeForever(closeScreenObserver);

        // Configurer le repository pour retourner le mockCall
        when(mockRepository.saveEmployee(employee)).thenReturn(mockCall);

        // Appeler la méthode à tester
        addEmployeeViewModel.saveEmployee(employee);

        // Capturer le callback passé à enqueue
        verify(mockCall).enqueue(callbackCaptor.capture());
        Callback<Employee> capturedCallback = callbackCaptor.getValue();

        // Simuler une réponse réussie
        Response<Employee> successResponse = Response.success(employee);
        capturedCallback.onResponse(mockCall, successResponse);

        // Vérifier que les LiveData ont été mis à jour correctement
        verify(toastObserver).onChanged("Employé ajouté avec succès !");
        verify(closeScreenObserver).onChanged(true);
    }}
