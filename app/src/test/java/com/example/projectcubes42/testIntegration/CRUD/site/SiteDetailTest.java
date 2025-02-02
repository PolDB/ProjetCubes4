package com.example.projectcubes42.testIntegration.CRUD.site;

import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;
import com.example.projectcubes42.ui.site.SiteDetailViewModel;
import com.example.projectcubes42.ui.site.SiteViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteDetailTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private SiteRepository mockRepository;

    @Mock
    private Call<Site> mockCall;

    @Mock
    private Call<Void> mockDeleteCall;

    @Mock
    private Observer<String> toastObserver;

    @Captor
    private ArgumentCaptor<Callback<Site>> callbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> voidCallbackCaptor;

    private SiteDetailViewModel siteViewModel;

    @Before
    public void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Initialiser les mockCall pour éviter le retour null
        mockCall = mock(Call.class);
        mockDeleteCall = mock(Call.class);

        // Injecter le mock repository dans le ViewModel
        siteViewModel = new SiteDetailViewModel(mockRepository);
    }

    @Test
    public void testUpdateSite_Success() {
        // 🔹 1. Préparer les données simulées
        Site site = new Site(1L, "Paris");

        // Simuler le comportement du repository
        when(mockRepository.updateSite(site.getIdSite(), site)).thenReturn(mockCall);

        // Observer le LiveData
        siteViewModel.getToastMessage().observeForever(toastObserver);

        // 🔹 2. Appeler la méthode à tester
        siteViewModel.updateSite(site.getIdSite(), site);


        // 🔹 4. Simuler la réponse réussie de l'API
        Response<Site> successResponse = Response.success(site);

    }

    @Test
    public void testDeleteSite_Success() {
        // 🔹 1. Préparer les données simulées
        Long siteId = 1L;

        // Simuler le comportement du repository
        when(mockRepository.deleteSite(siteId)).thenReturn(mockDeleteCall);

        // Observer le LiveData
        siteViewModel.getToastMessage().observeForever(toastObserver);

        // 🔹 2. Appeler la méthode à tester
        siteViewModel.deleteSite(siteId);

        // 🔹 4. Simuler une réponse réussie de l'API
        Response<Void> successResponse = Response.success(null);




    }
}
