package com.example.projectcubes42.ui.department;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Department;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ServiceViewHolder> {

    private List<Department> departmentList;
    private OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(String departmentName);
    }

    public DepartmentAdapter(List<Department> serviceList) {
        this.departmentList = serviceList;
    }

    public DepartmentAdapter(List<Department> departmentList, OnDepartmentClickListener listener) {
        this.departmentList = departmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_service_items, parent, false);
        return new ServiceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Department department = departmentList.get(position);
        Log.d("CURRENT_ACTIVITY", "ID du département à la position " + position + ": " + department.getIdDepartment());
        holder.departmentTextView.setText(department.getDepartment_name());
        holder.departmentTextView.setOnClickListener(v -> {
            Log.d("CLICK_TEST", "TextView cliqué !");
            Intent intent = new Intent(v.getContext(), DepartmentDetail.class);
            Log.d("CURRENT_ACTIVITY", "ID envoyé : " + department.getIdDepartment());
            intent.putExtra("DEPARTMENT_ID", (long) department.getIdDepartment());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView departmentTextView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentTextView = itemView.findViewById(R.id.department_name);
        }
    }
}
