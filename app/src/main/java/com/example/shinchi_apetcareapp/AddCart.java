package com.example.shinchi_apetcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class AddCart extends AppCompatActivity implements CartUpdateListener {

    private TextView productsTitle;
    private int cartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        // Correctly reference the TextView by its ID from the XML layout.
        productsTitle = findViewById(R.id.productsTitle);
        productsTitle.setText("Cart: " + cartCount + " items");
    }

    @Override
    public void onAddToCart() {
        cartCount++;
        productsTitle.setText("Cart: " + cartCount + " items");
    }

    // Since the layout you provided doesn't have a FragmentContainerView,
    // this method is no longer used and should be removed or refactored.
    // private void showFragment(Fragment fragment) {
    //     // This method will not work with the new layout, as there is no fragment container.
    // }
}
