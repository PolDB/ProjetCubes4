package com.example.projectcubes42;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcubes42.ui.employee.FragmentEmployee;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirige directement vers DrawerActivity
        Intent intent = new Intent(MainActivity.this, Drawer_activity.class);
        startActivity(intent);

        // Terminez MainActivity pour ne pas revenir à cette activité en appuyant sur "Retour"
        finish();
    }
}
