package com.example.projectcubes42.ui.site;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSiteViewModel extends ViewModel {

    private final SiteRepository repository;

    // LiveData pour afficher un message (Toast)
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour signaler la fermeture de l'écran
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();

    public AddSiteViewModel() {
        repository = new SiteRepository();
    }

    public LiveData<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    // Méthode pour ajouter un Site
    public void addSite(Site site) {
        repository.addSite(site).enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Site ajouté avec succès !");
                    // Signalement de la fermeture de l'écran
                    closeScreenEvent.setValue(true);
                } else {
                    toastMessageLiveData.setValue("Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                toastMessageLiveData.setValue("Échec : " + t.getMessage());
            }
        });
    }
}
