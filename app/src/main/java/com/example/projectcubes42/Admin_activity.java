package com.example.projectcubes42;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Admin_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button buttonSendPassword =  findViewById(R.id.buttonSendPassword);

        buttonSendPassword.setOnClickListener(View -> {
            Intent intent = new Intent(Admin_activity.this, Drawer_activity.class);
            startActivity(intent);
        });
    }
}