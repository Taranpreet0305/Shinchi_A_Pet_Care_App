package com.example.shinchi_apetcareapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductGromming extends Fragment {

    public ProductGromming() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_gromming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addToCartBtn1 = view.findViewById(R.id.grooming_btn_1);
        Button addToCartBtn2 = view.findViewById(R.id.grooming_btn_2);
        Button addToCartBtn3 = view.findViewById(R.id.grooming_btn_3);
        Button addToCartBtn4 = view.findViewById(R.id.grooming_btn_4);
        Button goToCartBtn = view.findViewById(R.id.go_to_cart_btn);
        if (addToCartBtn1 != null) {
            addToCartBtn1.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Dryer", 1);
                Toast.makeText(getContext(), "Dryer added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn2 != null) {
            addToCartBtn2.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Pet Shampoo", 1);
                Toast.makeText(getContext(), "Pet Shampoo added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn3 != null) {
            addToCartBtn3.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Nail Clipper", 1);
                Toast.makeText(getContext(), "Nail Clipper added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn4 != null) {
            addToCartBtn4.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Grooming Set", 1);
                Toast.makeText(getContext(), "Grooming Set added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (goToCartBtn != null) {
            goToCartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddCart.class);
                startActivity(intent);
            });
        }
    }
}