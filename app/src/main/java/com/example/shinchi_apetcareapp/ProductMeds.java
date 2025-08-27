package com.example.shinchi_apetcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ProductMeds extends Fragment {

    private static final String PREFS_NAME = "cart_prefs";
    private static final String CART_COUNT_KEY = "cart_count";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_meds, container, false);

        Button addToCartBtn = view.findViewById(R.id.meds_btn);
        Button goToCartBtn = view.findViewById(R.id.go_to_cart_btn);

        if (addToCartBtn != null) {
            addToCartBtn.setOnClickListener(v -> {
                incrementCartCount();
                Toast.makeText(getContext(), "Medicine added to cart!", Toast.LENGTH_SHORT).show();
            });
        }

        if (goToCartBtn != null) {
            goToCartBtn.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Navigating to cart!", Toast.LENGTH_SHORT).show();
                // Add navigation logic to go to the cart activity/fragment
            });
        }

        return view;
    }

    // Increments the cart count and saves it.
    private void incrementCartCount() {
        if (getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int currentCount = prefs.getInt(CART_COUNT_KEY, 0);
            currentCount++;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(CART_COUNT_KEY, currentCount);
            editor.apply();
        }
    }
}