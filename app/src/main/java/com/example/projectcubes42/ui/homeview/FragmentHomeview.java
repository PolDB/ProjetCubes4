package com.example.projectcubes42.ui.homeview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcubes42.R;
import com.example.projectcubes42.ui.employee.EmployeeAdapter;

public class FragmentHomeview extends Fragment {
    private RecyclerView contactRecyclerView;
    private TextView textViewExample;
    private EmployeeAdapter adapterContact;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Associer le ViewModel si nécessaire
        HomeviewViewModel addSiteViewModel = new ViewModelProvider(this).get(HomeviewViewModel.class);
        // Gonfler la vue
        View root = inflater.inflate(R.layout.fragment_homeview, container, false);

        return root;
    }

        @Override
    public void onResume(){
        super.onResume();
    }

}
