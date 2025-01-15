package com.example.projectcubes42.ui.site;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import com.example.projectcubes42.ui.department.DepartmentDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteDetail extends AppCompatActivity {

    private TextView siteDetail;

    private Long id;
    private Button btnEdit, btnSave, btnDelete;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);


        Long siteId = getIntent().getLongExtra("SITE_ID", -1);
        Log.d("DEPARTMENT_ID", "ID reçu dans Intent : " + siteId); // Log pour vérifier l'ID

        siteDetail = findViewById(R.id.siteDetail);         // Vérifiez que R.id.nameDetail existe
        btnEdit = findViewById(R.id.site_button_edit);
        btnSave = findViewById(R.id.site_save_update);
        btnDelete = findViewById(R.id.site_button_delete);

        if (siteId != -1) {
            getSiteById(siteId);
        }

        btnSave.setOnClickListener(v -> {
            String siteName = siteDetail.getText().toString().trim();


            if (!siteName.isEmpty() ) {
                Site site = new Site(id, siteName);
                updateSite(siteId, site);
                enableFields(false);
                btnSave.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            enableFields(true);
            siteDetail.requestFocus();
            showKeyboard(siteDetail);
            btnSave.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        });


        btnDelete.setOnClickListener(v -> deleteSite(siteId));
    }

    private void enableFields(boolean enable) {
        siteDetail.setEnabled(enable);
    }


    private void updateSite(Long siteId, Site site) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Site> call = apiService.updateSite(siteId, site);

        call.enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SiteDetail.this, "Site mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SiteDetail.this, "Échec de la mise à jour.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                Toast.makeText(SiteDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }


    private void deleteSite(Long id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteSite(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SiteDetail.this, "Employé supprimé avec succès !", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SiteDetail.this, "Échec de la suppression.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SiteDetail.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSiteById(Long siteId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Site> call = apiService.getSiteById(siteId);

        call.enqueue(new Callback<Site>() {
            @Override
            public void onResponse(Call<Site> call, Response<Site> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Site site = response.body();
                    siteDetail.setText(site.getCity()); // Assurez-vous que `getCity()` existe dans `Site`
                    id = site.getIdSite();
                } else {
                    Toast.makeText(SiteDetail.this, "Impossible de charger les détails du site.", Toast.LENGTH_SHORT).show();
                    Log.e("SiteDetail", "Erreur réponse : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Site> call, Throwable t) {
                Toast.makeText(SiteDetail.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SiteDetail", "Erreur réseau", t);
            }
        });
    }

}



