package com.example.projectcubes42.testIntegration.CRUD.site;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.DepartmentRepository;
import com.example.projectcubes42.data.repository.SiteRepository;
import com.example.projectcubes42.ui.department.DepartmentViewModel;
import com.example.projectcubes42.ui.site.SiteViewModel;

import org.junit.After;
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

import retrofit2.Callback;

public class SiteTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private SiteRepository mockRepository;

    @Mock
    private Observer<String> toastObserver;

    @Mock
    private Observer<Boolean> closeScreenObserver;

    @Mock
    private Observer<List<Site>> siteObserver; // Corrected observer name

    @Captor
    private ArgumentCaptor<Callback<Site>> callbackCaptor;

    private SiteViewModel siteViewModel;

    @Before
    public void setUp() throws Exception {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Initialiser DepartmentViewModel avec le constructeur par défaut
        siteViewModel = new SiteViewModel();

        // Utiliser la réflexion pour injecter le repository mocké dans DepartmentViewModel
        injectMockRepository(siteViewModel, mockRepository);
    }

    /**
     * Utilise la réflexion pour injecter le repository mocké dans le ViewModel.
     *
     * @param viewModel      L'instance de ViewModel où injecter le mock
     * @param mockRepository Le repository mocké à injecter
     * @throws Exception En cas d'erreur de réflexion
     */
    private void injectMockRepository(SiteViewModel viewModel, SiteRepository mockRepository) throws Exception {
        // Remplacez "repository" par le nom exact du champ dans DepartmentViewModel
        Field repositoryField = SiteViewModel.class.getDeclaredField("repository");
        repositoryField.setAccessible(true);
        repositoryField.set(viewModel, mockRepository);
    }

    @Test
    public void testGetAllSite_Success() {
        // Préparer les données simulées
        List<Site> mockSites = Arrays.asList(
                new Site(1L, "Paris")

        );

        // Créer une LiveData factice avec les données
        MutableLiveData<List<Site>> mockLiveData = new MutableLiveData<>();
        mockLiveData.setValue(mockSites);

        // Configurer le mock pour retourner la LiveData

        // Observer la LiveData du ViewModel
        siteViewModel.getSites().observeForever(siteObserver);

    }

}
