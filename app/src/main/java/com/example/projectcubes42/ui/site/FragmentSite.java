package com.example.projectcubes42.ui.site;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Site;

import java.util.List;
//Fragment qui affiche les sites du recyclerView
public class FragmentSite extends Fragment {

    private RecyclerView recyclerView;
    private SiteAdapter adapter;
    private Button addEmployeeButton;

    // ViewModel
    private SiteViewModel siteViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_site, container, false);

        // Récupération des références UI
        recyclerView = root.findViewById(R.id.contactRecyclerView);
        addEmployeeButton = root.findViewById(R.id.button_add_site);

        // Configuration de la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Créer (ou récupérer) le ViewModel
        siteViewModel = new ViewModelProvider(requireActivity()).get(SiteViewModel.class);

        // Observer la liste des sites
        siteViewModel.getSites().observe(getViewLifecycleOwner(), siteList -> {
            if (siteList != null) {
                updateRecyclerView(siteList);
            }
        });

        // Observer les messages (Toast)
        siteViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton pour ajouter un Site
        addEmployeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddSite.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recharger la liste des sites lorsqu’on revient sur le fragment
        siteViewModel.loadSites();
    }

    // Méthode pour initialiser ou mettre à jour l’adaptateur
    private void updateRecyclerView(List<Site> siteList) {
        if (adapter == null) {
            adapter = new SiteAdapter(siteList);
            recyclerView.setAdapter(adapter);
        } else {
            // Si l'adaptateur existe déjà, on peut mettre à jour sa liste
            adapter.updateData(siteList);
        }
    }
}
