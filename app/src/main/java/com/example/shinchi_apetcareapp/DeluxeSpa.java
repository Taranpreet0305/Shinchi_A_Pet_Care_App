package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DeluxeSpa extends Fragment {

    private static final double DELUXE_SPA_PRICE = 3000.00;

    public DeluxeSpa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deluxe_spa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the CartManager to ensure auth state is available
        CartManager.getInstance().initialize(requireContext());

        TextView bookNowButton = view.findViewById(R.id.book_now_btn);

        if (bookNowButton != null) {
            bookNowButton.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {
                    // User is logged in, navigate to the payment page.
                    Intent intent = new Intent(getActivity(), PaymentPage.class);
                    intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, DELUXE_SPA_PRICE);
                    startActivity(intent);
                } else {
                    // User is not logged in, navigate to the sign-in page.
                    Intent intent = new Intent(getActivity(), SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }
}