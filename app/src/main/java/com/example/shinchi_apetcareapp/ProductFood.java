package com.example.shinchi_apetcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ProductFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_food);

        CartManager.getInstance().initialize(this);

        Button addToCartBtn = findViewById(R.id.addToCartBtn);
        Button goToCartBtn = findViewById(R.id.goToCartBtn);

        addToCartBtn.setOnClickListener(v -> {
            // Add a product to the cart with a quantity
            CartManager.getInstance().addItem("Pet Food", 1);
        });

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProductFood.this, AddCart.class);
            startActivity(intent);
        });
    }
}
