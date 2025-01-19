package com.example.projectcubes42.ui.site;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteDetailViewModel extends ViewModel {

    private final SiteRepository repository;

    // LiveData pour stocker le site chargé
    private final MutableLiveData<Site> siteLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages Toast
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour demander la fermeture de l'écran (Activity)
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();

    public SiteDetailViewModel() {
        this.repository = new SiteRepository();
    }

    // GETTERS
    public LiveData<Site> getSite() {
        return siteLiveData;
    }

    public LiveData<String> getToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    // -----------------------
    // CHARGER UN SITE
    // -----------------------
    public void fetchSite(Long siteId) {
        repository.getSiteById(siteId).enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful() && response.body() != null) {
                    siteLiveData.setValue(response.body());
                } else {
                    toastMessageLiveData.setValue("Impossible de charger les détails du site.");
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    // -----------------------
    // METTRE A JOUR LE SITE
    // -----------------------
    public void updateSite(Long siteId, Site site) {
        repository.updateSite(siteId, site).enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toastMessageLiveData.setValue("Site mis à jour avec succès !");
                    closeScreenEvent.setValue(true); // Fermer l'écran sur succès
                } else {
                    toastMessageLiveData.setValue("Échec de la mise à jour.");
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }

    // -----------------------
    // SUPPRIMER LE SITE
    // -----------------------
    public void deleteSite(Long siteId) {
        repository.deleteSite(siteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Site supprimé avec succès !");
                    closeScreenEvent.setValue(true); // Fermer l'écran sur succès
                } else {
                    toastMessageLiveData.setValue("Échec de la suppression.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessageLiveData.setValue("Erreur : " + t.getMessage());
            }
        });
    }
}
