package com.example.shinchi_apetcareapp;

import static android.content.ContentValues.TAG; // Potential issue: Using a generic TAG

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shinchi_apetcareapp.BasicGrooming;
import com.example.shinchi_apetcareapp.DeluxeSpa;
import com.example.shinchi_apetcareapp.R;

public class GroomingCat extends AppCompatActivity {

    private static final String TAG = "GroomingCatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grooming_cat);
        Log.d(TAG, "Activity layout (activity_grooming_cat) set.");

        ImageView btn1 = findViewById(R.id.BasicGroom);
        ImageView btn2 = findViewById(R.id.StandardGroom);
        ImageView btn3 = findViewById(R.id.PremiumSpa);
        ImageView btn4 = findViewById(R.id.DeluxeSpa);

        if (btn1 == null) {
            Log.e(TAG, "ImageView with ID 'Basic Grooming' not found in activity_product_category.xml!");
            return;
        }
        if (btn2 == null) {
            Log.e(TAG, "ImageView with ID 'Standard Grooming' not found in activity_product_category.xml!");
            return;
        }
        if (btn3 == null) {
            Log.e(TAG, "ImageView with ID 'Premium Grooming' not found in activity_product_category.xml!");
            return;
        }
        if (btn4 == null) {
            Log.e(TAG, "ImageView with ID 'Deluxe Grooming' not found in activity_product_category.xml!");
            return;
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "BasicGroom ImageView clicked!");
                BasicGrooming productFoodFragment = new BasicGrooming();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "StandardGroom ImageView clicked!");
                com.example.shinchi_apetcareapp.StandardGrooming productFoodFragment = new com.example.shinchi_apetcareapp.StandardGrooming();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PremiumGroom ImageView clicked!");
                com.example.shinchi_apetcareapp.PremiumSpa productFoodFragment = new com.example.shinchi_apetcareapp.PremiumSpa();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DeluxeGroom ImageView clicked!");
                DeluxeSpa productFoodFragment = new DeluxeSpa();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
    }

    private void loadFragment(Fragment fragment, int containerId) {
        Log.d(TAG, "loadFragment called for container ID: " + getResources().getResourceEntryName(containerId));
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (findViewById(containerId) == null) {
            Log.e(TAG, "Fragment container with ID " + getResources().getResourceEntryName(containerId) + " not found!");
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d(TAG, "Fragment transaction committed to container " + getResources().getResourceEntryName(containerId));
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
