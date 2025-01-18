package com.example.projectcubes42;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.projectcubes42.ui.employee.FragmentEmployee;
import com.example.projectcubes42.ui.employee.FragmentEmployeeVisitor;
import com.google.android.material.navigation.NavigationView;

public class Drawer_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView drawerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);




        // Initialiser la Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialiser le DrawerLayout et NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // Définir le listener pour la navigation

        // Accéder au header du NavigationView et récupérer l'ImageView
        View headerView = navigationView.getHeaderView(0);
        drawerImage = headerView.findViewById(R.id.imageView);

        // Ajouter un clic listener sur l'ImageView pour ouvrir LoginActivity
        drawerImage.setOnClickListener(v -> {
            Intent intent = new Intent(Drawer_activity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Configuration des destinations principales
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_service, R.id.nav_employee, R.id.nav_site, R.id.nav_homeview)
                .setOpenableLayout(drawerLayout)
                .build();

        // Configuration du NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_activity, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Effacer la session utilisateur
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_role", "visiteur"); // Revenir à visiteur
            editor.apply();

            // Rediriger vers Drawer_activity pour recharger FragmentEmployeeVisitor
            Intent intent = new Intent(this, Drawer_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}
