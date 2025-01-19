package com.example.projectcubes42.data.repository;

import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;

import java.util.List;

import retrofit2.Call;

public class SiteRepository {

    private final ApiService apiService;

    public SiteRepository() {
        // Instancie le client Retrofit et l’interface ApiService
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<Site>> getAllSites() {
        return apiService.getAllSites();
    }

    public Call<Site> getSiteById(Long siteId) {
        return apiService.getSiteById(siteId);
    }

    public Call<Site> updateSite(Long siteId, Site site) {
        return apiService.updateSite(siteId, site);
    }

    public Call<Void> deleteSite(Long siteId) {
        return apiService.deleteSite(siteId);
    }

    public Call<Site> addSite(Site site) {
        return apiService.addSite(site);
    }

    // Vous pourrez ajouter d’autres méthodes si nécessaire
}
