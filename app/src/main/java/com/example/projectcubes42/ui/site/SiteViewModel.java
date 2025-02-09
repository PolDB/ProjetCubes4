package com.example.projectcubes42.ui.site;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//viewModel rattaché au fragment FragmentSite
public class SiteViewModel extends ViewModel {

    private final SiteRepository repository;
    private final MutableLiveData<List<Site>> sitesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    public SiteViewModel() {
        repository = new SiteRepository();
    }

    // Exposition de la liste des sites pour l’UI
    public LiveData<List<Site>> getSites() {
        return sitesLiveData;
    }

    // Exposition de la LiveData pour les messages (Toast)
    public LiveData<String> getToastMessage() {
        return toastMessageLiveData;
    }

    // Méthode pour charger/récupérer la liste des sites
    public void loadSites() {
        repository.getAllSites().enqueue(new Callback<List<Site>>() {
            @Override
            public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sitesLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Échec du chargement des données");
                }
            }

            @Override
            public void onFailure(Call<List<Site>> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }
}
