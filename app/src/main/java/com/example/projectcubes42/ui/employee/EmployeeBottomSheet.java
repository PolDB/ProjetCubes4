package com.example.projectcubes42.ui.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;
import com.example.projectcubes42.data.network.ApiClient;
import com.example.projectcubes42.data.network.ApiService;
import com.example.projectcubes42.ui.department.DepartmentAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeBottomSheet extends BottomSheetDialogFragment implements DepartmentAdapter.OnDepartmentClickListener {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private ApiService apiService;

    private OnDepartmentSelectedListener onDepartmentSelectedListener;

    public interface OnDepartmentSelectedListener {
        void onDepartmentSelected(String departmentName);
    }

    public void setOnDepartmentSelectedListener(OnDepartmentSelectedListener listener) {
        this.onDepartmentSelectedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_department, container, false);

        recyclerView = view.findViewById(R.id.departmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        apiService = ApiClient.getClient().create(ApiService.class);



        return view;
    }

    private void fetchDepartments() {
        Call<List<Department>> call = apiService.getAllDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Department> departmentList = response.body();
                    adapter = new DepartmentAdapter(departmentList, EmployeeBottomSheet.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("EmployeeBottomSheet", "Erreur de chargement des départements");
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Log.e("EmployeeBottomSheet", "Erreur lors de l'appel API", t);
            }
        });
    }

    @Override
    public void onDepartmentClick(String departmentName) {
        if (onDepartmentSelectedListener != null) {
            onDepartmentSelectedListener.onDepartmentSelected(departmentName);
            dismiss();
        }
    }
}
