package com.example.projectcubes42.ui.department;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.network.ApiService;
import com.example.projectcubes42.data.network.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDepartment extends Fragment {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private ApiService serviceApi;
    private Button addEmployeeButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflater le layout
        View root = inflater.inflate(R.layout.fragment_service, container, false);

        recyclerView = root.findViewById(R.id.contactRecyclerView);
        addEmployeeButton = root.findViewById(R.id.button_add_service);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addEmployeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddDepartment.class);
        startActivity(intent);
    });


        // Initialiser Retrofit et l'interface API
        serviceApi = ApiClient.getClient().create(ApiService.class);

        fetchServices();

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Rafraîchir les employés chaque fois que le fragment devient visible
        fetchServices();
    }


    private void fetchServices() {
        Call<List<Department>> call = serviceApi.getAllDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new DepartmentAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Échec du chargement des données", Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }}