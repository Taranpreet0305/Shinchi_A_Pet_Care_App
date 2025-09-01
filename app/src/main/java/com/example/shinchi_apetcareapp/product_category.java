package com.example.shinchi_apetcareapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class product_category extends AppCompatActivity {

    private static final String TAG = "ProductCategoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_category);
        Log.d(TAG, "Activity layout (activity_product_category) set.");
        ImageView btn1 = findViewById(R.id.Food);
        ImageView btn2 =  findViewById(R.id.GroomingEss);
        ImageView btn3 =  findViewById(R.id.Meds);
        ImageView btn4 = findViewById(R.id.Toys);
        if (btn1 == null) {
            Log.e(TAG, "ImageView with ID 'Food' not found in activity_product_category.xml!");
            return;
        }
        if (btn2 == null) {
            Log.e(TAG, "ImageView with ID 'Grooming Essentials' not found in activity_product_category.xml!");
            return;
        }
        if (btn3 == null) {
            Log.e(TAG, "ImageView with ID 'Meds' not found in activity_product_category.xml!");
            return;
        }
        if (btn4 == null) {
            Log.e(TAG, "ImageView with ID 'Toys' not found in activity_product_category.xml!");
            return;
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Food ImageView clicked!");

                ProductFood productFoodFragment = new ProductFood();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Food ImageView clicked!");

                ProductGromming productFoodFragment = new ProductGromming();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Food ImageView clicked!");

                ProductMeds productFoodFragment = new ProductMeds();
                loadFragment(productFoodFragment, R.id.product_detail_fragment_container);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Food ImageView clicked!");

                ProductToys productFoodFragment = new ProductToys();
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
