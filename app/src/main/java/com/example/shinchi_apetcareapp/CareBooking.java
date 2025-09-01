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

public class CareBooking extends Fragment {

    private final double PET_CARE_PRICE = 2500.00;

    public CareBooking() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_care_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button bookNowButton = view.findViewById(R.id.book_now_btn);
        bookNowButton.setOnClickListener(v -> {
            if (CartManager.getInstance().isUserLoggedIn()) {
                Intent intent = new Intent(getActivity(), PaymentPage.class);
                intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, PET_CARE_PRICE);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Please sign in to book this service.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
            }
        });
    }
}

