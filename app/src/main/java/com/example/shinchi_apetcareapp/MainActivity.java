package com.example.shinchi_apetcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView hamburgerIcon;

    ImageView Btn1;
    ImageView Btn2;
    ImageView Btn3;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.nav_view_main);
        hamburgerIcon = findViewById(R.id.hamburger_icon);

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        // Find the SwitchMaterial in the header
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            SwitchMaterial themeSwitch = headerView.findViewById(R.id.theme_switch);
            if (themeSwitch != null) {
                // Set the initial state of the switch based on the current theme
                themeSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);

                themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    recreate();
                });
            }
        }

        Btn1 = findViewById(R.id.Pro);
        Btn2 = findViewById(R.id.Grooming);
        Btn3 = findViewById(R.id.carepage);

        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.product_category.class);
                startActivity(intent);
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.GroomingCat.class);
                startActivity(intent);
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.pet_care.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (id == R.id.nav_home) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (id == R.id.nav_grooming) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.GroomingCat.class);
                startActivity(intent);
            } else if (id == R.id.nav_care) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.pet_care.class);
                startActivity(intent);
            } else if (id == R.id.nav_products) {
                Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.product_category.class);
                startActivity(intent);
            } else if (id == R.id.nav_profile) {
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.Profile_Page.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, com.example.shinchi_apetcareapp.SignIn.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }, 250);

        return true;
    }
}
