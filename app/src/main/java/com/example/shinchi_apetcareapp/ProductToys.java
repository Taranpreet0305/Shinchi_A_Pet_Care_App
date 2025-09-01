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

public class ProductToys extends Fragment {

    public ProductToys() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_toys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addToCartBtn1 = view.findViewById(R.id.toys_btn_1);
        Button addToCartBtn2 = view.findViewById(R.id.toys_btn_2);
        Button addToCartBtn3 = view.findViewById(R.id.toys_btn_3);
        Button addToCartBtn4 = view.findViewById(R.id.toys_btn_4);
        Button goToCartBtn = view.findViewById(R.id.go_to_cart_btn);

        if (addToCartBtn1 != null) {
            addToCartBtn1.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Scratcher", 1);
                Toast.makeText(getContext(), "Scratcher added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn2 != null) {
            addToCartBtn2.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Cat Toy", 1);
                Toast.makeText(getContext(), "Cat Toy added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn3 != null) {
            addToCartBtn3.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Pet Ball", 1);
                Toast.makeText(getContext(), "Pet Ball added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn4 != null) {
            addToCartBtn4.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Mouse Toy", 1);
                Toast.makeText(getContext(), "Mouse Toy added to cart!", Toast.LENGTH_SHORT).show();
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