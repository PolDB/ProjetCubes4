package com.example.projectcubes42.ui.site;

import android.annotation.SuppressLint;
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
import com.example.projectcubes42.data.model.Site;
import com.example.projectcubes42.ui.department.DepartmentDetail;

import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteViewHolder> {

    private List<Site> siteList;

    public SiteAdapter(List<Site> siteList) {
        this.siteList = siteList;
    }

    @NonNull
    @Override
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_site_items, parent, false);
        return new SiteViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SiteViewHolder holder, int position) {
        Site site = siteList.get(position);
        holder.nameTextView.setText(site.getIdSite() + " " + site.getCity()) ;
        holder.nameTextView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SiteDetail.class);
            Log.d("CURRENT_ACTIVITY", "ID envoyé : " + site.getIdSite());
            if (site != null && site.getIdSite() != null) {
                intent.putExtra("SITE_ID", site.getIdSite());
            } else {
                Log.e("SiteAdapter", "Site ou son ID est null !");
            }
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }

    public static class SiteViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public SiteViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.site_name);
            imageView = itemView.findViewById(R.id.site_image);


        }
    }
}
