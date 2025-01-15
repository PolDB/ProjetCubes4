package com.example.projectcubes42.ui.employee;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private Context context;

    // Constructeur corrigé
    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_employee_items, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        Log.d("ADAPTER_BIND", "Nom : " + employee.getName() + ", Département : " + employee.idDepartment());
        // Validation des données avant l'affichage
        if (employee.getId() != null && employee.getName() != null && employee.getFirstname() != null) {
            holder.nameTextView.setText(employee.getId() + " " + employee.getName() + " " + employee.getFirstname() + "" + employee.idDepartment());
        } else {
            holder.nameTextView.setText("Données manquantes");
        }

        holder.imageView.setImageResource(R.drawable.baseline_call_24);

        holder.nameTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EmployeeDetail.class);
            Log.d("CURRENT_ACTIVITY", "ID envoyé : " + employee.getId());
            intent.putExtra("EMPLOYEE_ID", employee.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void updateData(List<Employee> newEmployeeList) {
        if (newEmployeeList == null) {
            Log.e("ADAPTER", "La nouvelle liste d'employés est null !");
            return;
        }

        Log.d("ADAPTER", "Nouveaux employés : " + newEmployeeList.size());

        // Vide l'ancienne liste
      // Ajoute les nouveaux employés

        Log.d("ADAPTER", "Liste des employés mise à jour : " + this.employeeList.size());

        notifyDataSetChanged(); // Notifie le RecyclerView
    }



    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_name);
            imageView = itemView.findViewById(R.id.contact_phone);
        }
    }
}
