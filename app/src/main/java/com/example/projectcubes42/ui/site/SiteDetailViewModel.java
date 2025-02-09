package com.example.projectcubes42.ui.site;

import android.app.AlertDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectcubes42.data.model.Employee;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.repository.SiteRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//viewModel rattaché à l'activité SiteDetail
public class SiteDetailViewModel extends ViewModel {

    private final SiteRepository repository;

    // LiveData pour stocker le site chargé
    private final MutableLiveData<Site> siteLiveData = new MutableLiveData<>();

    // LiveData pour afficher des messages Toast
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    // LiveData pour demander la fermeture de l'écran (Activity)
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>();
    private final MutableLiveData<List<Employee>> employeesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Employee>> filteredEmployeesLiveData = new MutableLiveData<>(new ArrayList<>());

    public SiteDetailViewModel(SiteRepository mockRepository) {
        this.repository = new SiteRepository();
    }
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
    public void confirmDeleteSite(Context context, Long departmentId, int nbDeSalaries) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Il y a " + nbDeSalaries + " salarié(s) dans ce service.\nVoulez-vous vraiment le supprimer ?")
                .setPositiveButton("D'accord", (dialog, which) -> {
                    // Si l'utilisateur confirme, on supprime
                    deleteSite(departmentId);
                })
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .show();
    }


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


    public int filterBySite(long siteId) {
        List<Employee> allEmployees = employeesLiveData.getValue();
        if (allEmployees == null) {
            return 0; // par exemple
        }

        List<Employee> newFilteredList = new ArrayList<>();
        for (Employee emp : allEmployees) {
            if (emp.idDepartment() == siteId) {
                newFilteredList.add(emp);
            }
        }
        filteredEmployeesLiveData.setValue(newFilteredList);

        // Retournez le nombre d'employés filtrés au lieu d'un nombre fixe
        return newFilteredList.size();
    }
}
