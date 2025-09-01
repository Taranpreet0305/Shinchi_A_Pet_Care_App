package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductMeds extends Fragment {

    public ProductMeds() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_meds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView medsName1 = view.findViewById(R.id.meds_name_1);
        TextView medsName2 = view.findViewById(R.id.meds_name_2);
        TextView medsName3 = view.findViewById(R.id.meds_name_3);
        TextView medsName4 = view.findViewById(R.id.meds_name_4);
        medsName1.setText("Pain Relief");
        medsName2.setText("First-Aid Kit");
        medsName3.setText("Vaccination");
        medsName4.setText("Antibiotics");
        Button addToCartBtn1 = view.findViewById(R.id.meds_btn_1);
        Button addToCartBtn2 = view.findViewById(R.id.meds_btn_2);
        Button addToCartBtn3 = view.findViewById(R.id.meds_btn_3);
        Button addToCartBtn4 = view.findViewById(R.id.meds_btn_4);
        Button goToCartBtn = view.findViewById(R.id.go_to_cart_btn);

        if (addToCartBtn1 != null) {
            addToCartBtn1.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Pain Relief", 1);
                Toast.makeText(getContext(), "Pain Relief added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn2 != null) {
            addToCartBtn2.setOnClickListener(v -> {
                CartManager.getInstance().addItem("First-Aid Kit", 1);
                Toast.makeText(getContext(), "First-Aid Kit added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn3 != null) {
            addToCartBtn3.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Vaccination", 1);
                Toast.makeText(getContext(), "Vaccination added to cart!", Toast.LENGTH_SHORT).show();
            });
        }
        if (addToCartBtn4 != null) {
            addToCartBtn4.setOnClickListener(v -> {
                CartManager.getInstance().addItem("Antibiotics", 1);
                Toast.makeText(getContext(), "Antibiotics added to cart!", Toast.LENGTH_SHORT).show();
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