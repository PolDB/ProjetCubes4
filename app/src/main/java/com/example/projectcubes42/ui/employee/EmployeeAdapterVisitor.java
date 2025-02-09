package com.example.projectcubes42.ui.employee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.data.model.Employee;

import java.util.List;
//il s'agit de la classe, qui représente les données qui sont envoyées et affichées dans le recyclerView pour la page visiteur

public class EmployeeAdapterVisitor extends RecyclerView.Adapter<EmployeeAdapterVisitor.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private Context context;

    // Constructeur corrigé
    public EmployeeAdapterVisitor(Context context, List<Employee> employeeList) {
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
            holder.nameTextView.setText(employee.getName() + " " + employee.getFirstname());
        } else {
            holder.nameTextView.setText("Données manquantes");
        }

        holder.imageView.setImageResource(R.drawable.baseline_call_24);

        holder.nameTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EmployeeDetailVisitor.class);
            Log.d("CURRENT_ACTIVITY", "ID envoyé : " + employee.getId());
            intent.putExtra("EMPLOYEE_ID", employee.getId());
            context.startActivity(intent);
        });
        holder.imageView.setOnClickListener(v -> {
            String phoneNumber = employee.getPhone();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                makePhoneCall(phoneNumber);
            } else {
                Toast.makeText(context, "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
            }
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

        // Met à jour la liste
        this.employeeList.clear();
        this.employeeList.addAll(newEmployeeList);

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

    private void makePhoneCall(String phoneNumber) {
        // Vérifiez les permissions pour les versions Android 6.0 et supérieures
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            // Permission déjà accordée, lancer l'appel
            String dial = "tel:" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(dial));
            context.startActivity(intent);
        }
    }

}
