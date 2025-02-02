package com.example.projectcubes42.testIntegration.CRUD.site;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;
import com.example.projectcubes42.ui.site.AddSiteViewModel;

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

public class AddSiteTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private SiteRepository mockRepository;

    @Mock
    private Call<Site> mockCall;

    @Mock
    private Observer<String> toastObserver;

    @Mock
    private Observer<Boolean> closeScreenObserver;

    @Mock
    private Observer<List<Site>> employeeObserver; // Observer pour List<Employee>

    @Captor
    private ArgumentCaptor<Callback<Site>> callbackCaptor;

    private AddSiteViewModel addSiteViewModel;


    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);


        addSiteViewModel = new AddSiteViewModel(mockRepository);



        // Utiliser la réflexion pour injecter le repository mocké dans EmployeeViewModel
        injectMockRepository(addSiteViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(AddSiteViewModel viewModel, SiteRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans EmployeeViewModel
        Field repositoryField = AddSiteViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }

    @Test
    public void testAddSite_Success() {
        // Préparer les données de test
        Site site = new Site(1L, "Paris");

        // Attacher les observateurs
        addSiteViewModel.getToastMessageLiveData().observeForever(toastObserver);
        addSiteViewModel.getCloseScreenEvent().observeForever(closeScreenObserver);

        // Configurer le repository pour retourner le mockCall
        when(mockRepository.addSite(site)).thenReturn(mockCall);

        // Appeler la méthode à tester
        addSiteViewModel.addSite(site);

        // Capturer le callback passé à enqueue
        verify(mockCall).enqueue(callbackCaptor.capture());
        Callback<Site> capturedCallback = callbackCaptor.getValue();

        // Simuler une réponse réussie
        Response<Site> successResponse = Response.success(site);
        capturedCallback.onResponse(mockCall, successResponse);

        // Vérifier que les LiveData ont été mis à jour correctement
        verify(toastObserver).onChanged("Site ajouté avec succès !");
        verify(closeScreenObserver).onChanged(true);
    }}
